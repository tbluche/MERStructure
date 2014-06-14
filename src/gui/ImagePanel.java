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


package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;

import core.classification.Cluster;
import core.classification.fuzzy.FuzzyRegion;
import core.me.Context;
import core.me.Expression;
import core.me.Region;


/**
 * 
 * This class reprensents the Image Panel, where the expression is displayed. Additional informations such as 
 * baselines, bounding boxes, parenting, are also displayed. It handles the symbol selection.
 * 
 * @author Théodore Bluche
 *  gui
 */
public class ImagePanel extends JPanel implements MouseListener{
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage bi;
	private Expression    expr_;
	private Context       selected;
	private boolean		  wait_parent=false;
	
	public static final int XOFFSET = 5;
	public static final int YOFFSET = 10;
	
	public static final Color cSelected = Color.GREEN;
	public static final Color cBox = Color.RED;
	public static final Color cRegion = Color.ORANGE;
	public static final Color cFuzzy = new Color(150,200,250);
	
	public static boolean SHOW_PARENT   = false;
	public static boolean SHOW_BASELINE = false;
	public static boolean SHOW_REGION   = false;
	public static boolean SHOW_ME		= true;
	public static boolean SHOW_CLUSTERS = false;
	
	
	public ImagePanel(BufferedImage b, Expression e){
		bi = b;
		expr_ = e;
		setPreferredSize(new Dimension(bi.getWidth()+10,bi.getHeight()+15));
		this.addMouseListener(this);
	}
	
	public ImagePanel(Expression e) {
		expr_ = e;
		bi = new BufferedImage(e.getDimension().width, e.getDimension().height, BufferedImage.TYPE_INT_RGB);
		for (int x=0;x<bi.getWidth();x++)
			for (int y=0;y<bi.getHeight(); y++)
				bi.setRGB(x, y, Color.WHITE.getRGB());
		setPreferredSize(new Dimension(bi.getWidth()+10,bi.getHeight()+15));
		this.addMouseListener(this);
	}
	
	/**
	 * Get the expression corresponding to the panel
	 * @return the expression.
	 */
	public Expression getExpression(){ return expr_; }
	
	/**
	 * 
	 * @return the selected symbol
	 */
	public Context getSelected(){ return selected; }
	
	/**
	 * Method used to indicate that we are waiting for the parent selection, not an usual symbol selection.
	 */
	public void waitParent()
	{ 
		if (selected!=null)
			if (selected.hasParent())
			{
				selected.clearForRelationship(selected.getRelationship());
				selected.remParent();
			}
		wait_parent = true; 
	}

	/**
	 * Display everything
	 */
	public void paintComponent(Graphics g) {

		
		
		g.setColor(Color.WHITE);
		if (bi == null)
			g.drawString("No Image",10,40);
		else
			if (SHOW_ME) g.drawImage(bi,XOFFSET,YOFFSET,bi.getWidth(), bi.getHeight(), null);
			else		 g.fillRect(XOFFSET,YOFFSET,bi.getWidth(), bi.getHeight());
		// Retrieve symbols
		
		 Vector<Context> vect = expr_.getSymbols();
		 g.setColor(cBox);
		 
		 double[] dist;
		 int baseline;
		 
//		 int meTop = expr_.getTop();
//		 int meBottom = expr_.getBottom();
		 
//		 int top, bottom;
		
		 // For all symbols...
		 for(Context c: vect)
		 {
			 int[] args = c.getParamToDraw();					// Get parameters used to draw the meta info
			 dist = c.getSymbolClassObject().confidences();		// Get the distribution over class for the symbol
//			 reg = c.getRegion();
			 
			 // ---
			 // TWO CASES
			 // ---
			 
			 // A. The considered symbol is the one selected
			 if (c==selected) 
			 {
				 if (!SHOW_REGION && selected.getSymbolClass()>0)
				 {
					  // Draw symbol region
					 g.setColor(cRegion);
					 FuzzyRegion[] fr = selected.getFuzzyRegions().getRegions(selected.getSymbolClass()).getRegions();
					 int[] reg;
					 for (int i=0; i<fr.length; i++)
					 {
						 reg = fr[i].getParamsToDraw();
						 g.setColor(cRegion);
						 g.drawRect(reg[0]+XOFFSET, reg[1]+YOFFSET, reg[3], reg[2]);
						 g.setColor(cFuzzy);
						 g.drawRect(reg[4]+XOFFSET, reg[5]+YOFFSET, reg[7], reg[6]);
					 }
				 }
				
				 // Draw baselines if it is not drawn globally
				 if (!SHOW_BASELINE)
					 for (int i=0; i<dist.length; i++)
					 {
						 baseline = (int) Math.floor(c.getSymbol().getBaseline(i)+YOFFSET);
						 g.setColor(new Color(0,0,255,(int)Math.floor(255*dist[i])));
						 g.drawLine(0,baseline, bi.getWidth(), baseline);
					 }
				 
				 // Draw relationships if it's not drawn globally
				 g.setColor(cSelected); 	// Draw the boxes in green
				 if (c.getParent() != null && !SHOW_PARENT)
				 {
					 int[] pargs = c.getParent().getParamToDraw();
					 g.drawLine(args[0]+ImagePanel.XOFFSET, args[1]+args[2]/2+ImagePanel.YOFFSET, 
							 pargs[0]+pargs[3]+ImagePanel.XOFFSET, pargs[1]+pargs[2]/2+ImagePanel.YOFFSET);
				 }
			 }
			 
			 // B. The considered symbol is not selected
			 else g.setColor(cBox);	// Draw the boxes in red
			 
			 // In any case, draw the boxes
			 g.drawRect(args[0]+ImagePanel.XOFFSET, args[1]+ImagePanel.YOFFSET, args[3], args[2]);
			 
			 // If all relationships are to be displayed
			 if (c.getParent() != null && SHOW_PARENT)
			 {
				 int[] pargs = c.getParent().getParamToDraw();
				 g.drawLine(args[0]+ImagePanel.XOFFSET, args[1]+args[2]/2+ImagePanel.YOFFSET, 
						 pargs[0]+pargs[3]+ImagePanel.XOFFSET, pargs[1]+pargs[2]/2+ImagePanel.YOFFSET);
			 }
			 
			 // If all baselines are to be displayed
			 if (SHOW_BASELINE)
				 for (int i=0; i<dist.length; i++)
				 {
					 baseline = (int) Math.floor(c.getSymbol().getBaseline(i)+YOFFSET);
					 g.setColor(new Color(0,0,255,(int)Math.floor(255*dist[i])));
					 g.drawLine(0,baseline, bi.getWidth(), baseline);
				 }
			 
			 if (SHOW_REGION && selected.getSymbolClass()>0)
			 {
				  // Draw symbol region
				 g.setColor(cRegion);
				 FuzzyRegion[] fr = c.getFuzzyRegions().getRegions(c.getSymbolClass()).getRegions();
				 int[] reg;
				 for (int i=0; i<fr.length; i++)
				 {
					 reg = fr[i].getParamsToDraw();
					 g.setColor(cRegion);
					 g.drawRect(reg[0]+XOFFSET, reg[1]+YOFFSET, reg[3], reg[2]);
					 g.setColor(cFuzzy);
					 g.drawRect(reg[4]+XOFFSET, reg[5]+YOFFSET, reg[7], reg[6]);
				 }
			 }
			 
			 if (SHOW_CLUSTERS)
			 {
				 g.setColor(Color.GRAY);
				 Vector<Cluster> vcl = expr_.getClusters();
				 if (vcl != null)
					 for (Cluster cl: vcl)
					 {
						 Region cl_reg = cl.getRegion();
						 g.drawRect(
								 cl_reg.left+XOFFSET, cl_reg.top+YOFFSET,
								 cl_reg.right-cl_reg.left,
								 cl_reg.bottom-cl_reg.top
						 );
					 }
			 }
			 
		 }
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int x = e.getX()-ImagePanel.XOFFSET;
		int y = e.getY()-ImagePanel.YOFFSET;
		Vector<Context> vect = expr_.getSymbols();
		boolean found = false;
		for(Context c: vect)
		{
			if (c.isIn(x, y)) 
			{
				if (wait_parent)
				{
					new SelectRelationship(c,selected);
					selected = null;
					wait_parent = false;
				}
				else
					selected = c; 
				found = true;
			}
		}
		if (!found && !wait_parent) selected=null;
		repaint();

		MainWindow.inst.updateProps();
		MainWindow.inst.updateClassPanel();
		MainWindow.inst.updateTopMenu();

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Save the image of the expression.
	 * @param name : name of the file
	 * @throws IOException
	 */
	public void save(String name) throws IOException {
		File f = new File(name);
		ImageIO.write(bi, "png", f);
	}
}
