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


package tools;

import weka.core.Utils;

public class ArrayTools {
	
	public static double[] evenDist(int size)
	{
		double[] d = new double[size];
		for (int i=0; i<size; i++)
			d[i] = 1.;
		Utils.normalize(d);
		return d;
	}
	
	public static double[] crispDist(int size, int index)
	{
		double[] d = new double[size];
		for (int i=0; i<size; i++)
			d[i] = (i==index) ? 1. : 0.;
		return d;
	}
	
	public static double[] scale(double[] d, double s)
	{
		double[] res = new double[d.length];
		for (int i=0; i<d.length; i++)
			res[i] = s*d[i];
		return res;
	}
	
	public static double[] add(double[] d1, double[] d2)
	{
		int n = (d1.length<d2.length) ? d1.length : d2.length;
		double[] d = new double[n];
		for (int i=0; i<n; i++)
			d[i] = d1[i] + d2[i];
		return d;
	}

	public static int[] indexSort( double[] d )
	{
		double[] d2 = d.clone();
		int[] result = new int[d2.length];
		for (int i=0; i<d2.length; i++)
		{
			int n = Utils.maxIndex(d2);
			result[i] = n;
			d2[n] = -1;
		}
		return result;
	}
	
	public static String arr2str(double[] arr)
	{
		String str = "< ";
		for (int i=0; i<arr.length; i++)
			str += arr[i]+" ";
		str += ">";
		return str;
	}
}
