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


package core.me;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.Vector;

import core.classification.Cluster;
import core.classification.Interpretation;
import core.classification.RelationshipFinder;
import core.classification.Statistics;


import exceptions.InvalidInterpretation;
import gui.MainWindow;

import tools.Chronos;
import tools.XMLCreator;

import main.Parameters;

public class Expression {
	
	private Vector<Context> symbols 				= (Vector<Context>) null;
	private Vector<Interpretation> interpretations 	= (Vector<Interpretation>) null;
	
	private int clusterIter = -1;
	private Vector<Vector<Cluster>> clusters = (Vector<Vector<Cluster>>) null;
	
	private int top    = -1;
	private int bottom = -1;
	private int left   = -1;
	private int right  = -1;
	
	private Dimension dim = null;
	
	private String latex_ = "";
	
	public Expression()
	{
		symbols = new Vector<Context>();
	}
	
	public Expression(Vector<Context> v)
	{
		symbols = v;
		orderSymbols();
	}
	
	public void addGaussian()
	{
		for (Context c: symbols)
			c.addGaussian();
	}
	
	public void setDimension(Dimension d)
	{
		this.dim = d;
	}
	
	public Dimension getDimension()
	{
		return this.dim;
	}
	
	public void setLatex(String ltx) { this.latex_ = ltx; MainWindow.inst.addResultText("Latex set to "+ltx); }
	public String getLatex() { return this.latex_; }
	
	public Context getContext(int i)
	{
		if (i>0 && i<symbols.size())
			return symbols.get(i);
		return null;
	}
	
	public Context getContextById(int id)
	{

		Iterator<Context> iter = symbols.iterator();
		Context c;
		while (iter.hasNext())
		{
			c = iter.next();
			if (c.getSymbolId()==id) return c;
		}
		return (Context) null;
	}
	
	public int nbSymbols()
	{
		return symbols.size();
	}
	
	public void orderSymbols()
	{
		Vector<Context> v = new Vector<Context>();
		Context c;
		while (symbols.size()>0)
		{
			c = findLeftMost(symbols);
			v.add(c);
			symbols.remove(c);
		}
		symbols = v;
	}
	
	public void clusterize()
	{
		clusters = Cluster.clusterize(this);
		if (clusters.size()>0) clusterIter = 0;
	}
	
	public Vector<Cluster> getClusters()
	{
		if (clusterIter>=0)
			return clusters.get(clusterIter);
		return null;
	}
	
	public void nextClusters()
	{
		if (clusterIter+1<clusters.size()) clusterIter++;
	}
	
	public void prevCusters()
	{
		if (clusterIter>0) clusterIter--;
	}
	
	public void autoParenting()
	{
		for (int i=1; i<symbols.size(); i++)
			symbols.get(i).remParent().setParent(symbols.get(i-1));
	}
	
	public static Context findLeftMost(Vector<Context> v)
	{
		Context res = v.get(0);
		int left = v.get(0).getSymbol().xmin;
		int current;
		for (Context c: v)
		{
			current = c.getSymbol().xmin;
			if (current<left)
			{
				left = current;
				res = c;
			}
		}
		return res;
	}
	
	public int getDominantBaseline()
	{
		Context c = findLeftMost(symbols);
		return c.getBaseline();
	}
	
	public void addSymbol(int[] bb)
	{
		symbols.add(new Context(bb));
	}
	
	
	public Vector<Context> getSymbols()
	{
		return symbols;
	}
	
	public void classifySymbols()
	{
		for (Context c: symbols)
		{
			try {
				c.classifySymbol();
			} catch (Exception e) {
				System.out.println("Error while classifying a symbol: ");
				e.printStackTrace();
			}
		}
	}
	
	public void classifyRelationships()
	{
		for (Context c: symbols)
		{
			try {
				c.classifyRelationship();
			} catch (Exception e) {
				System.out.println("Error while classifying a relationship: ");
				e.printStackTrace();
			}
		}
	}
	
	public void reinitRegions()
	{
		for (Context c: symbols)
			c.reinitRegion();
	}

	public int findRight() {
		right = 0;
		for(Context c: symbols)
			if (c.getSymbol().xmax>right) right=c.getSymbol().xmax;
		return right;
	}
	
	private int findLeft() {
		return symbols.get(0).getSymbol().xmin;
	}
	
	private int findTop() {
		int ttop = symbols.get(0).getSymbol().ymin;
		for(Context c: symbols)
			if (c.getSymbol().ymin<ttop) ttop=c.getSymbol().ymin;
		return ttop;
	}
	
	private int findBottom() {
		int bot = 0;
		for(Context c: symbols)
			if (c.getSymbol().ymax>bot) bot=c.getSymbol().ymax;
		return bot;
	}
	
	public int getTop()
	{
		if (this.top==-1) this.top = this.findTop();
		return this.top;
	}
	
	public int getBottom()
	{
		if (this.bottom==-1) this.bottom = this.findBottom();
		return this.bottom;
	}
	
	public int getLeft()
	{
		if (this.left==-1) this.left = this.findLeft();
		return this.left;
	}
	
	public int getRight()
	{
		if (this.right==-1) this.right = this.findRight();
		return this.right;
	}

	public Context findLeftMost() {
		return findLeftMost(symbols);
	}
	
	public static String buildMetaLatex(Context start)
	{
		String str = start.getMetaLatex();
		if (start.hasSup()) str += "^{"+buildMetaLatex(start.getSup())+"}";
		if (start.hasSub()) str += "_{"+buildMetaLatex(start.getSub())+"}";
		if (start.hasUpp()) str += "^{"+buildMetaLatex(start.getUpp())+"}";
		if (start.hasUnd()) str += "_{"+buildMetaLatex(start.getUnd())+"}";
		if (start.hasHor()) str += " "+buildMetaLatex(start.getHor());
		return str;
	}

	public String buildMetaLatex() {
		return Expression.buildMetaLatex(this.findLeftMost());
	}

	public void clearRelationships() {
		for (Context c: symbols)
			c.removeChildren();
	}
	
	public boolean isValid()
	{
		boolean valid = true;
		for (int i=1; i<symbols.size(); i++)
			valid = valid && symbols.get(i).hasParent();
		return valid;
	}
	
	public double score()
	{
		return 2*this.sScore()*this.rScore()/(this.sScore()+this.rScore());
	}
	
	public double sScore()
	{
		double score = 0.;
		double s     = 0.;
		for (Context c: symbols)
		{
			s = c.getSCConfidence();
			if (s>0) score += 1./s;
		}
		if (score>0.) score = 1./score;
		score *= this.nbSymbols();
		return score;
	}
	
	public double rScore()
	{
		double score = 0.;
		double s     = 0.;
		for (Context c: symbols)
		{
			s = c.getRCConfidence();
			if (s>0) score += 1./s;
		}
		if (score>0.) score = 1./score;
		score *= (this.nbSymbols()-1);
		return score;
	}
	
	public Interpretation addCurrentInterpretation()
	{
		Interpretation in = new Interpretation(this);
		interpretations.add(in);
		return in;
	}
	
	public Vector<Interpretation> process()
	{
		interpretations = new Vector<Interpretation>();
//		this.clearRelationships();
//		this.reinitRegions();
		this.classifySymbols();
		for (int iter=0; iter<Parameters.ITERATIONS; iter++)
		{
			Chronos chr = new Chronos();
			this.clearRelationships();
			this.bestRelationships();
			this.addCurrentInterpretation();
			this.classifySymbols();
			this.addCurrentInterpretation();
			MainWindow.inst.addResultText("Iteration computed in "+chr.stop()+"ms.");
		}
		return interpretations;
	}

	public void bestRelationships() 
	{
//		RelationshipMatrix rm = new RelationshipMatrix(this);
//		try {
//			rm.build();
//			rm.buildRelationships();
//		} catch (Exception e)
//		{
//			System.out.println("An error occured while trying to build the relationships matrix: ");
//			e.printStackTrace();
//		}
		RelationshipFinder rf = new RelationshipFinder(this);
		rf.parentAndRelate();
	}
	
	public String printInterpretations()
	{
		String str = "";
		str += "Interpretations of the expression:\n";
		str += "----------------------------------\n";
		str += "(Valid, Meta-LaTeX, Symbol Score, Relationship Score, Global Score)\n";
		for (Interpretation in: interpretations)
			str += in.toString()+"\n";
		str += "----------------------------------\n";
		return str;
	}
	
	public void exportCurrentInterpretation(String file)
	{
		XMLCreator.createXML(this,file);
	}
	
	public void exportListSymbols(String file)
	{
		XMLCreator.listOfSymbolXML(symbols, file);
	}
	
	public boolean equals(Expression e)
	{
		boolean eq = this.getSymbols().size() == e.getSymbols().size();
		Iterator<Context> iterThis  = this.getSymbols().iterator();
		Iterator<Context> iterOther = e.getSymbols().iterator();
		while (eq && iterThis.hasNext())
		{
			Context c1 = iterThis.next();
			Context c2 = iterOther.next();
			eq = eq && c1.equals(c2) && c1.getSymbolClass()==c2.getSymbolClass();
			if (c1.hasParent())
				if (!c2.hasParent()) return false;
				else
					eq = eq && c1.getParent().equals(c2.getParent()) && c1.getRelationship()==c2.getRelationship();
			else if (c2.hasParent()) return false;
		}
		return eq;
	}
	
	public Statistics compare(Expression e)
	{
		boolean eq = this.getSymbols().size() == e.getSymbols().size();
		if (!eq) MainWindow.inst.addResultText("The expressions do not have the same amount of symbols!\n");
		
		Iterator<Context> iterThis  = this.getSymbols().iterator();
		Iterator<Context> iterOther = e.getSymbols().iterator();
		
		int n = this.getSymbols().size(); 
		double[] correctnessSymbol = new double[n];
		double[] correctnessRelationship = new double[n];
		int symErr=0,parErr=0,relErr=0; 
		
		int i = 0;
		boolean parentingErrorFound;
		while (eq && iterThis.hasNext())
		{
			Context c1 = iterThis.next();
			Context c2 = iterOther.next();
			
			parentingErrorFound = false;
			
			if (c1.getSymbolClass()!=c2.getSymbolClass()) symErr++;
			if (c1.getRelationship()!=c2.getRelationship()) relErr++;
			if (c1.hasParent())
			{
				if (!c2.hasParent()) {parErr++; parentingErrorFound=true;}
				else if (c1.getParent().getSymbolId()!=c2.getParent().getSymbolId()) {parErr++; parentingErrorFound=true;}
			}
			else if (c2.hasParent()) {parErr++; parentingErrorFound=true;}
			
			correctnessSymbol[i] = c1.getSCConfidence(c2.getSymbolClass());
			if (parentingErrorFound)
				correctnessRelationship[i] = c1.getVirtualRelConfidence(c2.getParent(), c2.getRelationship());
			else
				correctnessRelationship[i] = c1.getRelConfidence(c2.getRelationship());
			
			i++;
		}
		
		Statistics stat = new Statistics();
		stat.setNbSymbols(n);
		stat.setErrorParenting(parErr);
		stat.setErrorSymbol(symErr);
		stat.setErrorRelationship(relErr);
		stat.setCorrectnessRelationship(correctnessRelationship);
		stat.setCorrectnessSymbol(correctnessSymbol);
		stat.setExpressionLatex(this.getLatex());
		stat.setFoundLatex(this.buildMetaLatex());
		stat.setExpectedLatex(e.buildMetaLatex());
		
		return stat;
		
	}
	
	public void assignIds(Vector<Integer> ids) throws InvalidInterpretation
	{
		if (this.getSymbols().size()!=ids.size()) throw new InvalidInterpretation("not enough id's for symbols");
		else
			for (int i=0; i<ids.size(); i++)
				this.symbols.get(i).setSymbolId(ids.get(i));
	}

	public boolean autoProcessVRDataset(int[] symClasses) {
		if (this.symbols.size()!=4)
			return false;
		else
		{
			Context c = symbols.get(0);
			Context upperSym=c, underSym=c, varrangeSym=c, inlineSym=c;
			for (Context c1: symbols)
			{
				if (c1.getSymbol().ymin < upperSym.getSymbol().ymin) 	upperSym = c1;
				if (c1.getSymbol().ymax > underSym.getSymbol().ymax) 	underSym = c1;
				if (c1.getSymbol().xmin < varrangeSym.getSymbol().xmin) varrangeSym = c1;
				if (c1.getSymbol().xmax > inlineSym.getSymbol().xmax) 	inlineSym = c1;
			}
			varrangeSym.setClass(3);
			upperSym.setClass(symClasses[0]);
			underSym.setClass(symClasses[1]);
			inlineSym.setClass(symClasses[2]);
			
			upperSym.setParent(varrangeSym);
			underSym.setParent(varrangeSym);
			inlineSym.setParent(varrangeSym);
			
			varrangeSym.setRelationship(Relationship.INLINE);
			upperSym.setRelationship(Relationship.UPPER);
			underSym.setRelationship(Relationship.UNDER);
			inlineSym.setRelationship(Relationship.INLINE);
			
			varrangeSym.addChild(inlineSym, Relationship.INLINE);
			varrangeSym.addChild(upperSym, Relationship.UPPER);
			varrangeSym.addChild(underSym, Relationship.UNDER);
			
			return true;
		}
	}

	public boolean autoProcessELIDataset(int[] symClasses, int[] relClasses) {
		if (this.symbols.size()!=3)
			return false;
		else
		{
			
			this.autoParenting();
			
			Context c1 = symbols.get(0),
					c2 = symbols.get(1),
					c3 = symbols.get(2);

			c1.setClass(symClasses[0]);
			c2.setClass(symClasses[1]);
			c3.setClass(symClasses[2]);
			
			c2.setRelationship(relClasses[0]);
			c1.addChild(c2, relClasses[0]);
			if (relClasses[1]==3) 	{ c1.addChild(c3, Relationship.INLINE); c3.setRelationship(Relationship.INLINE); }
			else					{ c2.addChild(c3, relClasses[1]);  c3.setRelationship(relClasses[1]);  }
			
			return true;
		}
	}
	
	public boolean autoProcessELIVRDataset(int symClass, int relClass) {
		if (this.symbols.size()!=2)
			return false;
		else
		{
			
			this.autoParenting();
			
			Context c1 = symbols.get(0),
					c2 = symbols.get(1);

			c1.setClass(symClass);
			c2.setClass(SymbolClass.VARRANGE_IND);
			
			c2.setRelationship(relClass);
			c1.addChild(c2, relClass);
			
			return true;
		}
	}

	public Object[][] autoProcessYNCVRDataset(int[] symClasses) {
		
		if (this.symbols.size()!=4) return null;
		else
		{
			Object[][] result = new Object[6][7];
			Context c = symbols.get(0);
			Context upperSym=c, underSym=c, varrangeSym=c, inlineSym=c;
			for (Context c1: symbols)
			{
				if (c1.getSymbol().ymin < upperSym.getSymbol().ymin) 	upperSym = c1;
				if (c1.getSymbol().ymax > underSym.getSymbol().ymax) 	underSym = c1;
				if (c1.getSymbol().xmin < varrangeSym.getSymbol().xmin) varrangeSym = c1;
				if (c1.getSymbol().xmax > inlineSym.getSymbol().xmax) 	inlineSym = c1;
			}
			
			varrangeSym.setClass(SymbolClass.VARRANGE_IND);
			upperSym.setClass(symClasses[0]);
			underSym.setClass(symClasses[1]);
			inlineSym.setClass(symClasses[2]);
			
			result[0][0] = varrangeSym.getSymbolClass()+"";
			result[0][1] = upperSym.getSymbolClass()+"";
			result[0][2] = ((double)upperSym.area())/varrangeSym.area();
			result[0][3] = upperSym.getH(varrangeSym);
			result[0][4] = upperSym.getD(varrangeSym);
			result[0][5] = upperSym.getDX(varrangeSym);
			result[0][6] = "Y";
			
			result[1][0] = varrangeSym.getSymbolClass()+"";
			result[1][1] = underSym.getSymbolClass()+"";
			result[1][2] = ((double)underSym.area())/varrangeSym.area();
			result[1][3] = underSym.getH(varrangeSym);
			result[1][4] = underSym.getD(varrangeSym);
			result[1][5] = underSym.getDX(varrangeSym);
			result[1][6] = "Y";
			
			result[2][0] = varrangeSym.getSymbolClass()+"";
			result[2][1] = inlineSym.getSymbolClass()+"";
			result[2][2] = ((double)inlineSym.area())/varrangeSym.area();
			result[2][3] = inlineSym.getH(varrangeSym);
			result[2][4] = inlineSym.getD(varrangeSym);
			result[2][5] = inlineSym.getDX(varrangeSym);
			result[2][6] = "N";
			
			result[3][0] = upperSym.getSymbolClass()+"";
			result[3][1] = underSym.getSymbolClass()+"";
			result[3][2] = ((double)underSym.area())/upperSym.area();
			result[3][3] = underSym.getH(upperSym);
			result[3][4] = underSym.getD(upperSym);
			result[3][5] = underSym.getDX(upperSym);
			result[3][6] = "N";
			
			result[4][0] = upperSym.getSymbolClass()+"";
			result[4][1] = inlineSym.getSymbolClass()+"";
			result[4][2] = ((double)inlineSym.area())/upperSym.area();
			result[4][3] = inlineSym.getH(upperSym);
			result[4][4] = inlineSym.getD(upperSym);
			result[4][5] = inlineSym.getDX(upperSym);
			result[4][6] = "N";
			
			result[5][0] = underSym.getSymbolClass()+"";
			result[5][1] = inlineSym.getSymbolClass()+"";
			result[5][2] = ((double)inlineSym.area())/underSym.area();
			result[5][3] = inlineSym.getH(underSym);
			result[5][4] = inlineSym.getD(underSym);
			result[5][5] = inlineSym.getDX(underSym);
			result[5][6] = "N";
			
			return result;
		}
	}

	public Object[][] autoProcessYNCELIDataset(int[] symClasses, int[] relClasses) {
		
		if (this.symbols.size()!=3) return null;
		else
		{
			Object[][] result = new Object[3][7];
			Context c1 = symbols.get(0), c2 = symbols.get(1), c3 = symbols.get(2);
			c1.setClass(symClasses[0]);
			c2.setClass(symClasses[1]);
			c3.setClass(symClasses[2]);
			result[0][0] = c1.getSymbolClass()+"";
			result[1][0] = c2.getSymbolClass()+"";
			result[2][0] = c1.getSymbolClass()+"";
			result[0][1] = c2.getSymbolClass()+"";
			result[1][1] = c3.getSymbolClass()+"";
			result[2][1] = c3.getSymbolClass()+"";
			result[0][2] = ((double)c2.area())/c1.area();
			result[1][2] = ((double)c3.area())/c2.area();
			result[2][2] = ((double)c3.area())/c1.area();
			result[0][3] = c2.getH(c1);
			result[1][3] = c3.getH(c2);
			result[2][3] = c3.getH(c1);
			result[0][4] = c2.getD(c1);
			result[1][4] = c3.getD(c2);
			result[2][4] = c3.getD(c1);
			result[0][5] = c2.getDX(c1);
			result[1][5] = c3.getDX(c2);
			result[2][5] = c3.getDX(c1);
			result[0][6] = (relClasses[0]==0) ? "N" : "Y";
			result[1][6] = (relClasses[1]==0 || relClasses[1]==4) ? "N" : "Y";
			result[2][6] = "N";
			return result;
		}
	}
	
	public Object[][] autoProcessYNCELIVRDataset(int symClass, int relClass) {
		
		if (this.symbols.size()!=2) return null;
		else
		{
			Object[][] result = new Object[1][7];
			Context c1 = symbols.get(0), c2 = symbols.get(1);
			c1.setClass(symClass);
			c2.setClass(SymbolClass.VARRANGE_IND);
			result[0][0] = c1.getSymbolClass()+"";
			result[0][1] = c2.getSymbolClass()+"";
			result[0][2] = ((double)c2.area())/c1.area();
			result[0][3] = c2.getH(c1);
			result[0][4] = c2.getD(c1);
			result[0][5] = c2.getDX(c1);
			result[0][6] = "Y";
			return result;
		}
	}

}
