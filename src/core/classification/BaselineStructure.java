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

import java.util.Vector;

import core.me.Context;

public class BaselineStructure {
	
	private Context 					dominantSymbol	= (Context)						null;
	private Vector<BaselineStructure> 	nestedBaselines	= (Vector<BaselineStructure>)	null;
	
	public BaselineStructure(Context c)
	{
		this.dominantSymbol = c;
	}
	
	public Context getDominantSymbol() { return this.dominantSymbol; }
	public Vector<BaselineStructure> getNestedBaselines() { return this.nestedBaselines; }
	
	public void updateStructure(Context c)
	{
		this.dominantSymbol  = c;
		this.nestedBaselines = null;
	}
	
	public void addChild(BaselineStructure bl)
	{
		if (nestedBaselines == null) nestedBaselines = new Vector<BaselineStructure>();
		nestedBaselines.add(bl);
	}

	
}
