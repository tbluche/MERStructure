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

import tools.ArrayTools;
import main.Parameters;
import core.me.Context;
import core.me.SymbolClass;

public class ContextFuzzyRegions {

	private FuzzyRegions[] regions = new FuzzyRegions[Parameters.NB_OF_SYMBOL_CLASSES];
	private Context ref;
	
	public ContextFuzzyRegions(Context c)
	{
		regions[SymbolClass.SMALL_IND]      = new SmallRegions(c);
		regions[SymbolClass.DESCENDING_IND] = new DescendingRegions(c);
		regions[SymbolClass.ASCENDING_IND]  = new AscendingRegions(c);
		regions[SymbolClass.VARRANGE_IND]   = new VarrangeRegions(c);
		ref = c;
	}
	
	public double[] memberships(Context child)
	{
		double[] results = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
		double[] classfuzziness = ref.getSymbolClassObject().confidences();
		
		for (int i=0; i<Parameters.NB_OF_SYMBOL_CLASSES; i++)
			results = ArrayTools.add(results, 
									ArrayTools.scale(
											regions[i].memberships(child)
											,classfuzziness[i]
											         )
									);
		
//		try{Utils.normalize(results);} catch(Exception e){}
		
		return results;
	}
	
	public double[] memberships(Context child, int aClass)
	{
		return regions[aClass-1].memberships(child);
	}
	
	public FuzzyRegions getRegions(int theClass)
	{
		return regions[theClass-1];
	}
}
