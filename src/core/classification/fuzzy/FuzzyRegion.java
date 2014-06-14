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


package core.classification.fuzzy;

import java.awt.Point;

import core.me.Region;

public class FuzzyRegion {

	public static final int INTERPOL_LINEAR = 0;
	public static final int INTERPOL_EXP	= 1;
	
	
	
	private Region crisp;
	private Region margin;
//	private int interpolation;
	
	
	public FuzzyRegion(Region crisp, Region marg, int interp)
	{
		this.crisp = crisp;
		margin = marg;	
//		interpolation = interp;
	}
	
	public double membership(Point p)
	{
		if (crisp.isIn(p)) return 1.;
		else 
		{
			Point vect = crisp.distanceVector(p);
			int dx=vect.x, dy=vect.y;
			if ( (p.x<(crisp.left-margin.left)) 
					|| (p.x>(crisp.right+margin.right)) 
					|| (p.y<(crisp.top-margin.top))
					|| (p.y>(crisp.bottom+margin.bottom))) 
				return 0.;
			else
			{
				int d;
				if (dy==0) d=(dx>0) ? margin.right : margin.left;
				else if (dx==0) d=(dy>0) ? margin.bottom : margin.top;
				else
				{
					double theta = Math.atan(((double)Math.abs(dy))/Math.abs(dx));
					double a = (dx>0) ? margin.right : margin.left;
					double b = (dy>0) ? margin.bottom : margin.top;
					double cos = Math.cos(theta);
					double sin = Math.sin(theta);
					d = (int) Math.floor(a*cos*cos+b*sin*sin);
				}
				double dist = Math.sqrt(dx*dx+dy*dy);
				if (dist<d) return (d-dist)/d;
				else 
					{return 0.; }
			}
		}
	}
	
	public int[] getParamsToDraw()
	{
		int[] res = {crisp.left, crisp.top, crisp.height(), crisp.width(), crisp.left-margin.left, crisp.top-margin.top, crisp.height()+margin.top+margin.bottom, crisp.width()+margin.left+margin.right};
		return res;
	}
}
