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

import gui.ImagePanel;
import gui.MainWindow;
import gui.MultiImagePanel;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import core.imageproc.ImageProc;
import core.me.Expression;


public class LatexParser {
	
	private Vector<String> expressions;
	
	public LatexParser(String path) throws FileNotFoundException
	{
		BufferedReader in  = new BufferedReader(new FileReader(path));
		expressions = new Vector<String>();
		String line;

		try 
		{
			
			line = in.readLine();
			Pattern pattern = Pattern.compile("&begin\\{equation\\}(.*)&end\\{equation\\}");;
			Matcher matcher;
			
			while(line!=null)
			{
				MainWindow.inst.addResultText("Processing line: "+line);
				line = line.replace("\\begin", "&begin").replace("\\end", "&end");
				MainWindow.inst.addResultText("Rewritten line: "+line);
				matcher = pattern.matcher(line);
			    if(matcher.matches()) 
			    {
			    	expressions.add(matcher.group(1));
			    	MainWindow.inst.addResultText("Found: "+matcher.group(1));
			    }
			   	line = in.readLine();
			   	System.out.println("Pas de ligne: "+line==null);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public String toString()
	{
		String str = "Expressions \r\n";
		for (String s:expressions)
			str += s+"\r\n";
		return str;
	}
	
	public static BufferedImage getLatexImage(String latex) {

		TeXFormula formula = new TeXFormula(latex);
		TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 48);
		icon.setInsets(new Insets(5, 5, 5, 5));
		
		BufferedImage image = new BufferedImage(icon.getIconWidth()+75, icon.getIconHeight()+75, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor(Color.white);
		g2.fillRect(0,0,icon.getIconWidth()+75,icon.getIconHeight()+75);
		JLabel jl = new JLabel();
		jl.setForeground(new Color(0, 0, 0));
		icon.paintIcon(jl, g2, 37, 37);
		
		return image;
	}
	
	public BufferedImage getLatexImage(int index)
	{
		if (index<expressions.size())
			return LatexParser.getLatexImage(expressions.get(index));
		return null;
	}
	
	public MultiImagePanel latexToMip()
	{
		MultiImagePanel mip = new MultiImagePanel();
		
		BufferedImage bi;
		int i=1;
		for (String str: expressions)
		{
			MainWindow.inst.addResultText("Processing expression "+(i++)+": "+str);
			bi = getLatexImage(str);
			ImageProc ip = new ImageProc(bi);
			Expression e = new Expression(ip.segment());
			e.setLatex(str);
			e.setDimension(new Dimension(ip.largeur(), ip.hauteur()));
			mip.add( new ImagePanel(bi, e) );
		}
		
		return mip;
		
	}

}
