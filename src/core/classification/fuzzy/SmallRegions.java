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

import core.me.Context;
import core.me.Region;
import core.me.Symbol;

public class SmallRegions extends FuzzyRegions{

	public SmallRegions(Context c) {
		super(c);
	}

	@Override
	public void createRegions(Context c) {
		Region crisp, margin;
		Symbol s = c.getSymbol();
		
		int h=s.getHeight(), 
			w=s.getWidth(),
			x=s.xmax,
			m=(int) s.getCenter();
		
//		double minV = -0.48,
//			   maxV = 0.11,
//			   minD = 0.25,
//			   maxD = 1.18;
		
		double 	moyV=-0.15,
				stdV=0.1,
				moyD=0.74,
				stdD=0.19,
				hmoy=1.11,
				hmin=0.63;
		
		crisp  = new Region(
				(int)(x-w*(moyV+stdV)),(int)(x-w*(moyV-stdV)),
				(int)(m-h*(moyD+stdD)),(int)(m-h*(moyD-stdD)) 
				);
		int vmargin = (int)(h/hmin - crisp.height());
		int hmargin = (int)(h/hmoy - crisp.width());
		margin = new Region( hmargin/2, hmargin/2, vmargin/2, vmargin/2);
		rsuperscript = new FuzzyRegion(crisp, margin, FuzzyRegion.INTERPOL_LINEAR);
		
//		minV = -0.44;
//		maxV = 0.23;
//		minD = -0.83;
//		maxD = -0.08;
		
		moyV=-0.12;
		stdV=0.11;
		moyD=-0.47;
		stdD=0.17;
		hmoy=1.11;
		hmin=0.66;
		
		crisp  = new Region(
				(int)(x-w*(moyV+stdV)),(int)(x-w*(moyV-stdV)),
				(int)(m-h*(moyD+stdD)),(int)(m-h*(moyD-stdD)) 
				);
		vmargin = (int)(h/hmin - crisp.height());
		hmargin = (int)(h/hmoy - crisp.width());
		margin = new Region( hmargin/2, hmargin/2, vmargin/2, vmargin/2);
		rsubscript   = new FuzzyRegion(crisp, margin, FuzzyRegion.INTERPOL_LINEAR);
		
//		crisp  = new Region(0,0,0,0);
//		margin = new Region(0,0,0,0);
//		rupper       = new FuzzyRegion(crisp, margin, FuzzyRegion.INTERPOL_LINEAR);
//		
//		crisp  = new Region(0,0,0,0);
//		margin = new Region(0,0,0,0);
//		runder		 = new FuzzyRegion(crisp, margin, FuzzyRegion.INTERPOL_LINEAR);
		
		moyV = 0.65;
		stdV = 0.07;
		moyD = 0.85;
		stdD = 0.07;
		hmoy=3.45;
		hmin=1.83;
		
		crisp  = new Region(
					s.xmin, (int)(x-w*(moyV-stdV)),
					(int)(m-h*(moyD+stdD)),(int)(m-h*(moyD-stdD)) 
					);
		vmargin = (int)(h/hmin - crisp.height());
		hmargin = (int)(h/hmoy - 2*w*stdV);
		margin = new Region( (int)(0.15*w), (int)(x+0.15*w-crisp.right), vmargin/2, vmargin/2);
		rupper       = new FuzzyRegion(crisp, margin, FuzzyRegion.INTERPOL_LINEAR);
		
		moyD = -0.9;
		stdD = 0.1;
		hmoy=3.45;
		hmin=1.83;
		crisp  = new Region(
					s.xmin, (int)(x-w*(moyV-stdV)),
					(int)(m-h*(moyD+stdD)),(int)(m-h*(moyD-stdD)) 
					);
		vmargin = (int)(h/hmin - crisp.height());
		hmargin = (int)(h/hmoy - 2*w*stdV);
		margin = new Region( (int)(0.15*w), (int)(x+0.15*w-crisp.right), vmargin/2, vmargin/2);
		runder		 = new FuzzyRegion(crisp, margin, FuzzyRegion.INTERPOL_LINEAR);
	}

}
