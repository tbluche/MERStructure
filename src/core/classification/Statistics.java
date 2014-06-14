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

public class Statistics {
	
	private int 	nbSymbols 				= 1;
	private int 	errorSymbol 			= 0;
	private int 	errorRelationship 		= 0;
	private int 	errorParenting  		= 0;
	private double 	correctnessSymbol 		= 1.;
	private double 	correctnessRelationship	= 1.;
	private String	elatex					="";
	private String	alatex					="";
	private String	mlatex					="";

	public Statistics(){}
	
	// Setters
	public void setNbSymbols(int n)							{ nbSymbols=n; }
	public void setErrorSymbol(int n)						{ errorSymbol=n; }
	public void setErrorParenting(int n)					{ errorParenting=n; }
	public void setErrorRelationship(int n)					{ errorRelationship=n; }
	public void setCorrectnessSymbol( double d )			{ correctnessSymbol=d; }
	public void setCorrectnessSymbol( double[] ds )			{ setCorrectnessSymbol(arithmeticMean(ds)); }
	public void setCorrectnessRelationship( double d )		{ correctnessRelationship=d; }
	public void setCorrectnessRelationship( double[] ds )	{ setCorrectnessRelationship(arithmeticMean(ds)); }
	public void setExpressionLatex(String ltx) 				{ this.elatex=ltx; }
	public void setExpectedLatex(String ltx) 				{ this.alatex=ltx; }
	public void setFoundLatex(String ltx) 					{ this.mlatex=ltx; }
	
	// Getters
	public int    getNbSymbols()              { return nbSymbols; }
	public int    getErrorSymbol()            { return errorSymbol; }
	public int    getErrorParenting()         { return errorParenting; }
	public int    getErrorRelationship()      { return errorRelationship; }
	public int    getErrorSymbolPC()          { return (100*errorSymbol)/nbSymbols; }
	public int    getErrorParentingPC()       { return (100*errorParenting)/nbSymbols; }
	public int    getErrorRelationshipPC()    { return (100*errorRelationship)/nbSymbols; }
	public double getCorrectnessSymbol()      { return correctnessSymbol; }
	public double getCorrectnessRelationship(){ return correctnessRelationship; }
	
	public static double harmonicMean( double[] ds )
	{
		double sum = 0;
		int i = 0;
		for (double d: ds)
			if (d>0) { sum += 1/d; i++; }
		if (sum==0.)
			return 0.;
		else
			return i/sum;
	}
	
	public static double arithmeticMean( double[] ds )
	{
		double sum = 0;
		int i = 0;
		for (double d: ds)
			if (d>0) { sum += d; i++; }
		if (sum==0.)
			return 0.;
		else
			return sum/i;
	}
	
	public String toString()
	{
		String str = "";
		str += "Result of ME classification: \n";
		str += "----------\n";
		str += " - Symbol count: "+nbSymbols+"\n";
		str += " - Symbol errors (%): "+errorSymbol+" ("+((double)errorSymbol*100/nbSymbols)+"%)\n";
		str += " - Parenting errors (%): "+errorParenting+" ("+((double)errorParenting*100/nbSymbols)+"%)\n";
		str += " - Relationship errors (%): "+errorRelationship+" ("+((double)errorRelationship*100/nbSymbols)+"%)\n";
		str += " - Symbol correctness: "+correctnessSymbol+"\n";
		str += " - Relationship correctness: "+correctnessRelationship+"\n";
		str += "----------\n";
		return str;
	}
	
	public static Statistics merge(Vector<Statistics> vs)
	{
		int n = vs.size();
		int nbSym  = 0, 
			parErr = 0,
			symErr = 0,
			relErr = 0;
		double symbolCorrectness = 0.;
		double relationshipCorrectness = 0.;
		
		for (Statistics s: vs)
		{
			nbSym  += s.getNbSymbols();
			parErr += s.getErrorParenting();
			symErr += s.getErrorSymbol();
			relErr += s.getErrorRelationship();
			symbolCorrectness += s.getCorrectnessSymbol();
			relationshipCorrectness += s.getCorrectnessRelationship();
		}
		
		if (n>0)
		{
			symbolCorrectness /= n;
			relationshipCorrectness /= n;
		}
		
		Statistics s = new Statistics();
		s.setNbSymbols(nbSym);
		s.setErrorSymbol(symErr);
		s.setErrorRelationship(relErr);
		s.setErrorParenting(parErr);
		s.setCorrectnessRelationship(relationshipCorrectness);
		s.setCorrectnessSymbol(symbolCorrectness);
		
		return s;
	}
	
	public String getLatexResult()
	{
		String str = "";
		String bcell = "\\begin{minipage}[c][2cm]{0.2\\linewidth} ";
		String ecell = "  \\end{minipage}";
		String begin = bcell+"\\begin{equation*}";
		String end   = "\\end{equation*}"+ecell;
//		begin = "\\(";
//		end = "\\)";
		str += "\\hline ";
		str += begin+this.elatex+end;
		str += " & ";
		str += begin+this.alatex+end;
		str += " & ";
		str += begin+this.mlatex+end;
		str += " & ";
		str += bcell;
		str += "$N="+this.nbSymbols+"$";
		str += "\\newline";
		str += "$e_s="+getErrorSymbolPC()+"\\%$, $e_p="+getErrorParentingPC()+"\\%$, $e_r="+getErrorRelationshipPC()+"\\%$";
		str += "\\newline";
		str += "$C_s="+(((int)(100*getCorrectnessSymbol()))/100.)+"$, $C_r="+(((int)(100*getCorrectnessRelationship()))/100.)+"$";
		str += ecell;
		str += " \\\\ ";
		return str;
	}
}
