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

import main.Parameters;

import core.me.Context;
import core.me.Relationship;

public abstract class FuzzyRegions {
	
	public FuzzyRegion rinline;
	public FuzzyRegion rsuperscript;
	public FuzzyRegion rsubscript;
	public FuzzyRegion rupper;
	public FuzzyRegion runder;
	
	
	public FuzzyRegions (Context c) 
	{
		createRegions(c);
	}
	
	public abstract void createRegions(Context c);
	
	public double[] memberships(Context c)
	{
		Point p = c.getCentroid();
		double[] results = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
		
		results[Relationship.INLINE]		 = 0;
		results[Relationship.SUPERSCRIPT] 	 = rsuperscript.membership(p);
		results[Relationship.SUBSCRIPT] 	 = rsubscript.membership(p);
		results[Relationship.UPPER] 		 = rupper.membership(p);
		results[Relationship.UNDER] 		 = runder.membership(p);
		
		return results;
	}
	
	public FuzzyRegion[] getRegions() 
	{
		FuzzyRegion[] regs = {rsuperscript, rsubscript, rupper, runder};
		return regs;
	}
	
}
