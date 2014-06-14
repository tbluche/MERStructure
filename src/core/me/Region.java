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

public class Region {

	public int left;
	public int right;
	public int top=-1;
	public int bottom=-1;
	
	public Region(int left, int right)
	{
		this.left  = left;
		this.right = right;
	}

	public Region(int left2, int right2, int top2, int bottom2) {
		this.left   = left2;
		this.right  = right2;
		this.top    = top2;
		this.bottom = bottom2;
	}
	
	public Region merge(Region r)
	{
		return new Region(
				(this.left<r.left) 		? this.left 	: r.left,
				(this.right>r.right) 	? this.right 	: r.right,
				(this.top<r.top) 		? this.top 		: r.top,
				(this.bottom>r.bottom)	? this.bottom 	: r.bottom
						);
	}
	
	public double distance(Point p)
	{
		int dx=0,dy=0;
		
		if (p.x>right) dx = right-p.x;
		else if (p.x<left) dx = left-p.x;
		
		if (p.y>bottom) dy = bottom-p.y;
		else if (p.y<top) dy = top-p.y;
		
		return (int)Math.floor(Math.sqrt(dx*dx+dy*dy));
	}
	
	public Point distanceVector(Point p)
	{
		int dx=0,dy=0;
		
		if (p.x>right) dx = p.x-right;
		else if (p.x<left) dx = p.x-left;
		
		if (p.y>bottom) dy = p.y-bottom;
		else if (p.y<top) dy = p.y-top;
		
		return new Point(dx, dy);
	}
	
	public boolean isIn(Point p)
	{
		return (p.x>=left && p.x<=right && p.y>=top && p.y<=bottom);
	}
	
	public String toString()
	{
		String str = "("+left+", "+top+")->("+right+", "+bottom+")";
		return str;
	}

	public int height() {return (top==-1 || bottom==-1)?-1:bottom-top;}
	public int width() {return right-left;}
}
