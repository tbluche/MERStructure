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

import gui.MainWindow;
import gui.filemanager.FileManager;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;

import core.imageproc.ImageProc;
import core.me.Context;
import core.me.Expression;



public class DatasetBuilder {
	
	private Object[][] 	data;
	private Object[][] 	data2;
	private int 		line = 0;
	private int 		line2= 0;
	
	public static final char[] class1 = {'a', 'z', 'e', 'r', 'u','o','s','m','x','c','v','n'};
	public static final char[] class2 = {'y','p','q','g'};
	public static final char[] class3 = {'0','1','2','3','4','5','6','7','8','9','A','Z','E','R','T','Y','U','I','O','P',
				         'Q','S','D','F','G','H','J','K','L','M','W','X','C','V','B','N','t','d','h','k',
				         'l','b'};
	public static final String[] class4 = {"\\sum", "\\prod", "\\bigcup", "\\bigcap"};
	public static final char[][] symbols = {class1, class2, class3};
	
	public DatasetBuilder()
	{
		data = new Object[10000][48];
		line = 0;
		
		data2 = new Object[15000][4];
		line2 = 0;
		 
	}
	
	public void buildVR()
	{
		
		String eq = "";
		int total=0;
		char x,y,z;
		for (int a=0; a<3; a++)
			for (int b=0; b<3; b++)
				for (int vr=0; vr<4; vr++)
					for (int c=0; c<3; c++)
					{
							x = symbols[a][(int) Math.floor(symbols[a].length*Math.random())];
							y = symbols[b][(int) Math.floor(symbols[b].length*Math.random())];
							z = symbols[c][(int) Math.floor(symbols[c].length*Math.random())];
							eq = class4[vr]+"^{"+x+"}_{"+y+"}{"+z+"}";						
							MainWindow.inst.addResultText("Processing expression #"+total+": "+eq+"...");
							int[] symClasses = {a, b, c};
							ImageProc ip = getImage(eq);
							Expression expr = new Expression(ip.segment());
							expr.addGaussian();
							buildVRLines(expr, symClasses);
							buildYNCVRLines(expr, symClasses);
							total++;
					}
	}
	
	public void buildELI()
	{
		
		String[] op = {"", "^{", "_{", "}{"};
		String eq = "";
		int total=0;
		for (int a=0; a<6; a++)
			for (int aop=0; aop<3; aop++)
				for (int b=0; b<6; b++)
					for (int bop=0; bop<4; bop++)
						for (int c=0; c<6; c++)
							if (aop>0 || bop<3)
							{
								eq = "";
								eq += symbols[a/2][(int) Math.floor(symbols[a/2].length*Math.random())];
								eq += op[aop];
								eq += symbols[b/2][(int) Math.floor(symbols[b/2].length*Math.random())];
								eq += op[bop];
								eq += symbols[c/2][(int) Math.floor(symbols[c/2].length*Math.random())];
								if (aop>0) eq += "}";
								if (bop>0 && bop<3) eq += "}";
								MainWindow.inst.addResultText("Processing equation #"+total+": "+eq+"...");
								int[] symClasses = {a/2, b/2, c/2};
								int[] relClasses = {aop, bop};
								ImageProc ip = getImage(eq);
								Expression expr = new Expression(ip.segment());
								expr.addGaussian();
								buildELILines(expr, symClasses, relClasses);
								buildYNCELILines(expr, symClasses, relClasses);
								total++;
							}
	}
	
	public void buildELIVR()
	{
		
		String[] op = {" ", "^{", "_{"};
		String eq = "";
		int total=0;
		for (int a=0; a<6; a++)
			for (int aop=0; aop<3; aop++)
				for (int b=0; b<2; b++)
				{
					eq = "";
					eq += symbols[a/2][(int) Math.floor(symbols[a/2].length*Math.random())];
					eq += op[aop];
					eq += class4[(int) Math.floor(class4.length*Math.random())];
					if (aop>0) eq += "}";
					MainWindow.inst.addResultText("Processing equation #"+total+": "+eq+"...");
					ImageProc ip = getImage(eq);
					Expression expr = new Expression(ip.segment());
					expr.addGaussian();
					buildELIVRLines(expr, a/2, aop);
					buildYNCELIVRLines(expr, a/2, aop);
					total++;
				}
	}
	
	private ImageProc getImage(String me)
	{
		BufferedImage bi = LatexParser.getLatexImage(me);
		return new ImageProc(bi);
	}
	
	private void buildVRLines(Expression expr, int[] symClasses)
	{
		if (expr.autoProcessVRDataset(symClasses))
			for (Context c: expr.getSymbols())	data[line++] = c.getDatasetLine();
		else
			data[line++][1] = 1;
	}
	
	private void buildELILines(Expression expr, int[] symClasses, int[] relClasses)
	{
		if (expr.autoProcessELIDataset(symClasses, relClasses))
			for (Context c: expr.getSymbols())	data[line++] = c.getDatasetLine();
		else
			data[line++][1] = 1;
	}
	
	private void buildELIVRLines(Expression expr, int symClass, int relClass)
	{
		if (expr.autoProcessELIVRDataset(symClass, relClass))
			for (Context c: expr.getSymbols())	data[line++] = c.getDatasetLine();
		else
			data[line++][1] = 1;
	}
	
	private void buildYNCELILines(Expression expr, int[] symClasses, int[] relClasses)
	{
		Object[][] lines = expr.autoProcessYNCELIDataset(symClasses, relClasses);
		if (lines != null) for (Object[] aLine :  lines)	data2[line2++] = aLine;
	}
	
	private void buildYNCVRLines(Expression expr, int[] symClasses)
	{
		Object[][] lines = expr.autoProcessYNCVRDataset(symClasses);
		if (lines != null) for (Object[] aLine :  lines)	data2[line2++] = aLine;
	}
	
	private void buildYNCELIVRLines(Expression expr, int symClass, int relClass)
	{
		Object[][] lines = expr.autoProcessYNCELIVRDataset(symClass, relClass);
		if (lines != null) for (Object[] aLine :  lines)	data2[line2++] = aLine;
	}
	
	public void createCSV(String path)
	{
		try{
		    FileWriter file = new FileWriter(FileManager.get().getWorkspace()+path);
		    BufferedWriter out = new BufferedWriter(file);
		    for (int i=0; i<line; i++)
		    	out.write(stringLine(data[i]));
		    out.close();
	    }catch (Exception e){
		      System.err.println("Error: " + e.getMessage());
	    }
	}
	
	public void createCSV2(String path)
	{
		// Write data for Y/N Classifier
		try{
		    FileWriter file = new FileWriter(FileManager.get().getWorkspace()+path);
		    BufferedWriter out = new BufferedWriter(file);
		    for (int i=0; i<line2; i++)
		    	out.write(stringLine(data2[i]));
		    out.close();
	    }catch (Exception e){
		      System.err.println("Error: " + e.getMessage());
	    }
	}
	
	public static String stringLine(Object[] aLine)
	{
		String str = "";
		for (Object o: aLine)
			str += (o==null) ? "0;" : o+";";
		return str.substring(0, str.length()-1)+"\n";
	}

}
