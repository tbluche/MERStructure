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

import java.awt.Point;
import java.util.Random;

/**
 * Represents a single symbol, out of any context, in terms of bounding box, baseline and class. 
 * @author Théodore Bluche
 *  build
 */
public class Symbol {

	/**
	 * ID generator
	 */
	public static int sidg = 0;
	
	/**
	 * Crisp ratio of the symbol height for the position of the baseline (LaTeX expressions)
	 */
	public static final double[] BASELINE_RATIO = {1., 0.7, 1., .75};
	
	/**
	 * Bounding box
	 */
	public  int					xmin, xmax, ymin, ymax;
	/**
	 * Symbol class
	 */
	private int					theClass = 1;
	/**
	 * Symbol ID
	 */
	public  int 				sid		= 0;
	
	public Symbol(int[] bb)
	{
		this.sid = ++Symbol.sidg;
		this.setProp(bb[0], bb[1], bb[2], bb[3]);
	}
	
	public Symbol(int i, int j, int k, int l) {
		
		this.sid = ++Symbol.sidg;
		
		this.setProp(i, j, k, l);
	}
	
	/**
	 * Sets the bounding box of the symbol.
	 * @param i xmin
	 * @param j xmax
	 * @param k ymin
	 * @param l ymax
	 */
	private void setProp(int i, int j, int k, int l)
	{
		xmin = i;
		xmax = j;
		ymin = k;
		ymax = l;
	}

	/**
	 * Equality test. 
	 * @param s another symbol
	 * @return whether both symbols are equal
	 */
	public boolean equals(Symbol s)
	{
		if (s==null)
			return false;
		else
			return this.sid==s.sid;
	}
	
	/**
	 * Check whether a point is within the bounding box of the symbol
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return whether the point <code>(x, y)</code> is in the symbol's bounding box
	 */
	protected boolean isIn(int x, int y)
	{
		return ((x>=xmin) && (x<=xmax) && (y>=ymin) && (y<=ymax));
	}
	
	/**
	 * 
	 * @return an array with the parameters to draw the bounding box in the form <code>x, y, height, width</code>
	 */
	protected int[] getParamToDraw()
	{
		int[] res = {xmin, ymin, this.getHeight(), this.getWidth()}; 
		return res;
	}
	
	/**
	 * @return the height of the symbol
	 */
	public int getHeight() { return ymax-ymin; }
	
	/**
	 * @return the width of the symbol
	 */
	public int getWidth() { return xmax-xmin; }
	
	/**
	 * @return the ratio width/height
	 */
	public double getRatio() { return ((double)this.getWidth())/this.getHeight(); }
	
	/**
	 * @return the y-centre of the symbol
	 */
	public double getCenter() { return ((double)ymin+ymax)/2.; }
	
	/**
	 * @return the symbol class
	 */
	public int getSymbolClass() { return theClass; }
	
	/**
	 * @param i hypothetic class
	 * @return the position of the baseline
	 */
	public int getBaseline(int i) { return (int)Math.floor(ymin+BASELINE_RATIO[i]*this.getHeight()); }
	
	/**
	 * @return the position of the baseline
	 */
	public int getBaseline() { return getBaseline(theClass-1); }
	
	/**
	 * Set the class fo the symbol
	 * @param c symbol class
	 */
	public void setClass(int c) { theClass = c; }
	
	/**
	 * Calculate the distance from another symbol
	 * @param s another symbol
	 * @return the distance to <code>s</code>
	 */
	public int distanceCentroids(Symbol s)
	{
		int xcenter1 = (s.xmin+s.xmax)/2;
		int xcenter2 = (this.xmin+this.xmax)/2;
		int ycenter1 = (s.ymin+s.ymax)/2;
		int ycenter2 = (this.ymin+this.ymax)/2;
		int dx = (xcenter1 - xcenter2);
		int dy = (ycenter1 - ycenter2);
		return dx*dx + dy*dy;
	}
	
	/**
	 * Calculate the distance to another symbol between two closest points
	 * @param s another symbol
	 * @return the distance between to closest points
	 */
	public int distance(Symbol s)
	{
		int dx=0, dy=0;
		
		if (this.xmax>s.xmin && s.xmax>this.xmin)
			dx = 0;
		else if (s.xmin>this.xmax)
			dx = s.xmin-this.xmax;
		else
			dx = this.xmin-s.xmax;
		
		if (this.ymax>s.ymin && s.ymax>this.ymin)
			dy = 0;
		else if (s.ymin>this.ymax)
			dy = s.ymin-this.ymax;
		else
			dy = this.ymin-s.ymax;
		
		return (int)Math.floor(Math.sqrt(dx*dx+dy*dy));
	}
	
	/**
	 * Compute the distance of a point to the bounding box
	 * @param p a point
	 * @return the distance
	 */
	public int distance(Point p)
	{
		int dx=0,dy=0;
		
		if (p.x>xmax) dx = xmax-p.x;
		else if (p.x<xmin) dx = xmin-p.x;
		
		if (p.y>ymax) dy = ymax-p.y;
		else if (p.y<ymin) dy = ymin-p.y;
		
		return (int)Math.floor(Math.sqrt(dx*dx+dy*dy));
	}

	/**
	 * Sets the id of the symbol
	 * @param id new id
	 */
	public void setId(int id) { this.sid = id; }
	
	/**
	 * Adds a gaussian deformation and offset to the symbol
	 */
	public void addGaussian()
	{
		Random r = new Random();
		double 	dx = r.nextGaussian(),
				dy = r.nextGaussian(),
				dh = r.nextGaussian(),
				dw = r.nextGaussian();
		dx *= 0.05*this.getWidth();
		dw *= 0.05*this.getWidth();
		dy *= 0.05*this.getHeight();
		dh *= 0.05*this.getHeight();
		xmin = (int)Math.ceil(xmin+dx);
		xmax = (int)Math.ceil(xmax+dx+dw);
		ymin = (int)Math.ceil(ymin+dy);
		ymax = (int)Math.ceil(ymax+dy+dh);
	}

	/**
	 * Compute the center of the bounding box
	 * @return the center of the bounding box
	 */
	public Point getCentroid() 
	{
		return new Point(
					xmin, (ymin+ymax)/2	
					);
	}
	
	public int area()
	{
		return this.getHeight()*this.getWidth();
	}

	public Region getBoundingBox() 
	{
		return new Region (xmin, xmax, ymin, ymax);
	}
}
