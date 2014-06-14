/************************************************************************************
 *    This file is part of MERStructure.                                            *
 *                                                                                  *
 *    Foobar is free software: you can redistribute it and/or modify                *
 *    it under the terms of the GNU General Public License as published by          *
 *    the Free Software Foundation, either version 3 of the License, or             *
 *    (at your option) any later version.                                           *
 *                                                                                  *
 *    MERStructure is distributed in the hope that it will be useful,               *
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of                *
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                 *
 *    GNU General Public License for more details.                                  *
 *                                                                                  *
 *    You should have received a copy of the GNU General Public License             *
 *    along with MERStructure.  If not, see <http://www.gnu.org/licenses/>.         *
 *                                                                                  *
 ***********************************************************************************/


package core.classification;

import gui.Histogram;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import core.me.Context;
import core.me.Expression;
import core.me.Region;
import core.me.Relationship;

import main.Parameters;

import weka.core.Utils;

public class RelationshipMatrix {

	public static final double THRESHOLD = 0.5; 
	
	private Expression expr_ = (Expression) null;
	private int        dim   = 0;
	private double[][] rels_ = null;
	
	public RelationshipMatrix(Expression expr)
	{
		this.expr_ = expr;
		this.dim = expr_.nbSymbols();
		this.rels_ = new double[(dim*(dim-1))/2][Parameters.NB_OF_RELATIONSHIP_CLASSES];
	}
	
	public double[] getRelationshipDist(int i, int j)
	{
		if (i>=j)
		{
			System.out.println("The relations are only left-to-right!");
			return null;
		}
		else
		{
			int index = j-i-1 + i*dim - (i*(i+1))/2;
			return rels_[index];
		}
	}
	
	public int getRelationship(int i, int j)
	{
		if (i>=j)
		{
			System.out.println("The relations are only left-to-right!");
			return -1;
		}
		else
		{
			int index = j-i-1 + i*dim - (i*(i+1))/2;
			return Utils.maxIndex(rels_[index]);
		}
	}
	
	public boolean setRelationship(int i, int j, double[] rel)
	{
		if (i>=j)
		{
			System.out.println("The relations are only left-to-right!");
			return false;
		}
		else
		{
			int index = j-i-1 + i*dim - (i*(i+1))/2;
			rels_[index] = rel;
			return true;
		}
	}
	
	public void build() throws Exception
	{
		Vector<Context> vc = expr_.getSymbols();
		Context parent, child;
		Classifiers cc = Classifiers.getInst(false);
		double[] rel;
		
		for (int i=0; i<vc.size(); i++)
		{
			parent = vc.get(i);
			for (int j=i+1; j<vc.size(); j++)
			{
				child = vc.get(j);
				child.remParent().setParent(parent);
				rel = cc.getRelationship(child);
				this.setRelationship(i, j, rel);
				child.remParent();
			}
		}
	}
	
	public Vector<Context> candidatesForRelation(int i, int rel)
	{
		double[] dist;
		Context child;
		
		Vector<Context> vc = expr_.getSymbols();
		
		Vector<Context> candidates = new Vector<Context>();
//		System.out.print("Candidates for rel "+rel+" for symbol#"+i+": ");
		for (int j=i+1; j<dim; j++)
		{
			dist = this.getRelationshipDist(i, j);
			child = vc.get(j);
			
			if ((dist[rel]>THRESHOLD))
			{
				candidates.add(child);
			}
		}
		return candidates;
	}
	
	public JPanel visualize()
	{
		JPanel jf = new JPanel();
		
		GridLayout gl = new GridLayout(dim, dim); 
		gl.setHgap(15);
		gl.setVgap(15);
		
		jf.setLayout(gl);
		Vector<Context> vc = expr_.getSymbols();
		
		jf.add(new JPanel());
		
		for (int j=1; j<dim; j++)
		{
			JLabel jl = new JLabel( ""+vc.get(j).getSymbolId() );
			jl.setHorizontalAlignment( SwingConstants.CENTER );
			jf.add(jl);
		}
		
		for (int i=0; i<dim-1; i++)
		{
			JLabel jl = new JLabel( ""+vc.get(i).getSymbolId() );
			jl.setHorizontalAlignment( SwingConstants.CENTER );
			jf.add(jl);
			for (int j=1; j<dim; j++)
			{
				jf.add(this.getHist(i,j));
			}
		}
		
		jf.setPreferredSize(new Dimension(400+15*(dim+4), 400+15*(dim+4)));
		jf.setVisible(true);
		
		
		return jf;
	}
	
	public JPanel getHist(int i, int j)
	{
		int size = 400/dim;
		if (i>=j)
			return new JPanel();
		else
			return new Histogram(this.getRelationshipDist(i, j),size,size);
	}
	
	/**
	 * Extract and parent the line beginning with <code>startSymbol</code> and defined by the bounds
	 * @param startSymbol : first symbol of the line
	 * @param rightLimit 
	 * @return nested baselines
	 */
	public Vector<Context> extractLine(Context startSymbol, Region roi)
	{
		int dominantBaseline = startSymbol.getBaseline();
		
//		expr_.reinitRegions();
		
		Vector<Context> line = new Vector<Context>();
		Vector<Context> vc   = expr_.getSymbols();
		
		int i = vc.indexOf(startSymbol);
		Context c = startSymbol;
		Context c_prev = startSymbol;
		
		i++;
		if (i<dim) c = vc.get(i);
		
		boolean hasSub = false;
		boolean hasSup = false;
		boolean hasUpp = false;
		boolean hasUnd = false;
		boolean done   = false;
		double[] temp = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		
		while (c.getSymbol().xmax<=roi.right && i<dim)
		{
			if (!c.hasParent())
			{
				double inlineScore_rel = ( Math.sqrt( this.getRelationshipDist(c_prev, c)[0] ) );
				double max=0.;
				for (int k=0 ; k<Parameters.NB_OF_SYMBOL_CLASSES ; k++)
				{
					temp[k] = ( Parameters.SCORE_WEIGHT_BASELINE / (Parameters.SCORE_WEIGHT_BASELINE + Math.abs(c.getBaseline(k)-dominantBaseline)) )* inlineScore_rel;
					if (temp[k]>max) max=temp[k];
				}
				double inlineScore = max;
								
								
				
				// Test if the symbol is the next one on the line
				if (inlineScore > Parameters.T_BASELINE_SCORE)
				{
					c_prev.getRegion().right = c.getRegion().left;
					c.getRegion().bottom = (c_prev.getRegion().bottom> c.getSymbol().ymax) ? c_prev.getRegion().bottom : c.getSymbol().ymax;
					c.getRegion().top = (c_prev.getRegion().top < c.getSymbol().ymin) ? c_prev.getRegion().top : c.getSymbol().ymin;				
					c.setParent(c_prev);
					c.setRelationship(this.getRelationshipDist(c_prev, c), Relationship.INLINE);
					c_prev.addChild(c, 0);
					c_prev = c;
					hasSub = false;
					hasSup = false;
				}
				
				else
				{
					if (!hasUpp && !done)
					{
						double uppScore = this.getRelationshipDist(c_prev, c)[Relationship.UPPER];
						if (uppScore>Parameters.T_BASELINE_UPP_SCORE && c.getCenterY()>roi.top && c.getCenterY()<roi.bottom)
						{
							c.getRegion().top = (c_prev.getRegion().top > c.getSymbol().ymin) ? c_prev.getRegion().top : c.getSymbol().ymin;
							c.getRegion().bottom = (c_prev.getSymbol().ymin < c.getSymbol().ymax) ? c_prev.getSymbol().ymin : c.getSymbol().ymax;
							c.setParent(c_prev);
							c.setRelationship(this.getRelationshipDist(c_prev, c), Relationship.UPPER);
							c_prev.addChild(c, Relationship.UPPER);
							line.add(c);
							hasUpp = true;
							done   = true;
						}
					}
					
					if (!hasUnd && !done)
					{
						double undScore = this.getRelationshipDist(c_prev, c)[Relationship.UNDER];
						if (undScore>Parameters.T_BASELINE_UND_SCORE && c.getCenterY()>roi.top && c.getCenterY()<roi.bottom)
						{
							c.getRegion().top = (c_prev.getSymbol().ymax > c.getSymbol().ymin) ? c_prev.getSymbol().ymax : c.getSymbol().ymin;
							c.getRegion().bottom = (c_prev.getRegion().bottom > c.getSymbol().ymax) ? c_prev.getRegion().bottom : c.getSymbol().ymax;
							c.setParent(c_prev);
							c.setRelationship(this.getRelationshipDist(c_prev, c), Relationship.UNDER);
							c_prev.addChild(c, Relationship.UNDER);
							line.add(c);
							hasUnd = true;
							done = true;
						}
					}
					
					if (!hasSup && !done)
					{
						double supScore = this.getRelationshipDist(c_prev, c)[Relationship.SUPERSCRIPT];
						if (supScore>Parameters.T_BASELINE_SUP_SCORE && c.getCenterY()>roi.top && c.getCenterY()<roi.bottom && c.getSymbol().xmin>c_prev.getSymbol().xmax)
						{
							c.getRegion().bottom = (c_prev.getCenterY()> c.getSymbol().ymax) ? c_prev.getCenterY() : c.getSymbol().ymax;
							c.getRegion().top = (c_prev.getRegion().top< c.getSymbol().ymin) ? c_prev.getRegion().top : c.getSymbol().ymin;
							c.setParent(c_prev);
							c.setRelationship(this.getRelationshipDist(c_prev, c), Relationship.SUPERSCRIPT);
							c_prev.addChild(c, Relationship.SUPERSCRIPT);
							line.add(c);
							hasSup = true;
							done = true;
						}
					}
					
					if (!hasSub && !done)
					{
						double subScore = this.getRelationshipDist(c_prev, c)[Relationship.SUBSCRIPT];
						if (subScore>Parameters.T_BASELINE_SUB_SCORE && c.getCenterY()>roi.top && c.getCenterY()<roi.bottom && c.getSymbol().xmin>c_prev.getSymbol().xmax)
						{
							c.getRegion().top = (c_prev.getCenterY()< c.getSymbol().ymin) ? c_prev.getCenterY() : c.getSymbol().ymin;
							c.getRegion().bottom = (c_prev.getRegion().bottom> c.getSymbol().ymax) ? c_prev.getRegion().bottom : c.getSymbol().ymax;
							c.setParent(c_prev);
							c.setRelationship(this.getRelationshipDist(c_prev, c), Relationship.SUBSCRIPT);
							c_prev.addChild(c, Relationship.SUBSCRIPT);
							line.add(c);
							hasSub = true;
							done = true;
						}
					}
					
				}
					
			}	// end hasParent
				
			// Next symbol
			i++;
			if (i<dim) c = vc.get(i);
			done = false;
			
		} // end while
		
	
		c_prev.getRegion().right = roi.right;
		
		return line;
	}
	
	public void buildRelationships()
	{
		expr_.reinitRegions();
		expr_.clearRelationships();
		
		// List of start symbols in the different baselines, built iteratively
		Vector<Context> startSymbols = new Vector<Context>();
		Vector<Context> nested;
		
		// We begin with the main symbol
		startSymbols.add(expr_.findLeftMost());
		
		// Useful variables
		Context considered;
		int right, top, bottom;
		
		// While some baselines have not been considered...
		while (!startSymbols.isEmpty())
		{
			// Pick the next starting symbol
			considered = startSymbols.remove(startSymbols.size()-1);
			// Define the limits
			right  = (considered.hasParent()) ? considered.getParent().getRegion().right : expr_.findRight();
			top    = (considered.getRegion().top==-1) ? expr_.getTop() : considered.getRegion().top;
			bottom = (considered.getRegion().bottom==-1) ? expr_.getBottom() : considered.getRegion().bottom;
			// Build the line and retrieve the next baselines
			nested = this.extractLine(considered, new Region(0, right, top, bottom));
			if ( !(nested.isEmpty()) ) startSymbols.addAll(nested);
		}
	}

	private double[] getRelationshipDist(Context cPrev, Context c) {
		Vector<Context> vc = expr_.getSymbols();
		return getRelationshipDist(vc.indexOf(cPrev), vc.indexOf(c));
	}
	
}
