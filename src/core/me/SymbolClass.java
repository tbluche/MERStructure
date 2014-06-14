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

import tools.ArrayTools;
import weka.core.Utils;
import main.Parameters;

public class SymbolClass {
	
	private int 	  classOf   = 0;
	private double[]  mixedDist = null;
	private double[]  distA     = null;
	private double[]  distB	    = null;
	private double[]  distC	    = null;
	private double[]  distC1	= null;
	private double[]  distC2	= null;
	private double[]  distC3	= null;
	private double[]  distC4	= null;
	private double[]  distC5	= null;
	
	public static final double COEFFA = .35;
	public static final double COEFFB = .10; // 15
	public static final double COEFFC = .55; // 50
	
	public static final double COEFFC1 = 1.;
	public static final double COEFFC2 = 1.;
	public static final double COEFFC3 = 1.;
	public static final double COEFFC4 = 1.;
	public static final double COEFFC5 = 1.;
	
	public static final double[] PROPCL = {.21, .066, .659, .066};
	
	public static final int SMALL		= 1;
	public static final int DESCENDING  = 2;
	public static final int ASCENDING	= 4;
	public static final int VARRANGE    = 3;
	
	public static final int SMALL_IND		= 0;
	public static final int DESCENDING_IND  = 1;
	public static final int ASCENDING_IND	= 2;
	public static final int VARRANGE_IND    = 3;
	
	public static final String[] TEXTCLASS = {"no", "small", "descending", "ascending", "variable range"}; 
	
	public SymbolClass()
	{
		distA	  = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		distB	  = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		distC	  = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		distC1	  = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		distC2	  = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		distC3	  = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		distC4	  = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		distC5	  = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		mixedDist = new double[Parameters.NB_OF_SYMBOL_CLASSES];
	}
	
	public SymbolClass( double[] C1, double[] C2, double[] C3, double[] C4, double[] C5,
			double[] A, double[] B)
	{
		distA  = A;
		distB  = B;
		distC1 = C1;
		distC2 = C2;
		distC3 = C3;
		distC4 = C4;
		distC5 = C5;
		this.mixDist();
		this.inferClass();
	}


	public void mixDist() 
	{
		distC	  = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		mixedDist = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		for (int i=0; i<Parameters.NB_OF_SYMBOL_CLASSES; i++)
		{
			distC[i] = 0.;
			distC[i] += COEFFC1*this.distC1[i];
			distC[i] += COEFFC2*this.distC2[i];
			distC[i] += COEFFC3*this.distC3[i];
			distC[i] += COEFFC4*this.distC4[i];
			distC[i] += COEFFC5*this.distC5[i];
		}
		Utils.normalize(distC);
		
		
		for (int i=0; i<Parameters.NB_OF_SYMBOL_CLASSES; i++)
		{
			mixedDist[i] = COEFFA*this.distA[i];
			mixedDist[i] += COEFFB*this.distB[i];
			mixedDist[i] += COEFFC*this.distC[i];
		}
		
		Utils.normalize(mixedDist);
		
	}
	
	public void inferClass() { this.set(Utils.maxIndex(mixedDist)); }

	public void set(int i) { classOf = i+1; }
	public int get() { return classOf; }
	
	public double[] confidences() { return mixedDist; }
	public double[] getDistA() { return distA; }
	public double[] getDistB() { return distB; }
	public double[] getDistC() { return distC; }
	public double[] getDistC(int index)
	{
		switch (index)
		{
		case Relationship.INLINE:
			return distC1;
		case Relationship.SUPERSCRIPT:
			return distC2;
		case Relationship.SUBSCRIPT:
			return distC3;
		case Relationship.UPPER:
			return distC4;
		case Relationship.UNDER:
			return distC5;
		}
		
		return null;
	}
	
	public void manualSet(int id)
	{
		double[] d = ArrayTools.crispDist(Parameters.NB_OF_SYMBOL_CLASSES, id);
		mixedDist = d;
		set(id);
	}
	
	public void manualSet( double[] d )
	{
		mixedDist = d;
		Utils.normalize(mixedDist);
		inferClass();
	}

	public double getConfidence() { return (this.get()>0) ? this.mixedDist[this.get()-1] : 1.; }
	public double getConfidence(int aClass) { return this.mixedDist[aClass-1]; }

}
