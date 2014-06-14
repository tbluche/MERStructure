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

import core.me.Expression;
import tools.XMLCreator;

public class Interpretation {
	
	private String   			mlatex;
	private boolean  			validity;
	private double  	 		sScore;
	private double	        	rScore;
	private org.jdom.Document 	xml;
	
	public Interpretation (Expression e)
	{
		this.mlatex   = e.buildMetaLatex();
		this.validity = e.isValid();
		this.sScore   = e.sScore();
		this.rScore   = e.rScore();
		this.xml      = XMLCreator.xmlInterpretation(e);
	}
	
	public String getMetaLatex(){ return this.mlatex; }
	
	public boolean getValidity(){ return this.validity;	}
	
	public double getSymbolScore(){ return this.sScore;	}
	
	public double getRelatioshipScore()	{ return this.rScore; }
	
	public double getScore()
	{
		if (this.getSymbolScore()+this.getRelatioshipScore()>0)
			return 2*this.sScore*this.rScore/(this.sScore+this.rScore);
		else
			return 0.;
	}
	
	public org.jdom.Document getXML(){ return this.xml; }
	
	public String toString()
	{
		String str = "";
		str += (this.getValidity()) ? "<Valid>" : "<Not Valid>";
		str += " : ";
		str += this.getMetaLatex();
		str += " : ";
		str += this.getSymbolScore();
		str += " * ";
		str += this.getRelatioshipScore();
		str += " = ";
		str += this.getScore();
		return str;
	}

}
