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


package gui.results;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PlotPanel extends JPanel {

	private double[] xvalues;
	private double[] yvalues;
	
	private String xlabel;
	private String ylabel;
	
	public PlotPanel (	double[] xarr,
						double[] yarr,
						String   x_label,
						String   y_label	)
	{
		this.update(xarr, yarr, x_label, y_label);
		this.setPreferredSize(new Dimension(340, 360));
	}
	
	public void update(	double[] xarr,
						double[] yarr,
						String   x_label,
						String   y_label	)

	{
		xvalues = xarr;
		yvalues = yarr;
		xlabel  = x_label;
		ylabel  = y_label;
		this.repaint();
		this.validate();
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, 340, 360);
		
		g.setColor(Color.black);
		g.drawString(ylabel, 5, 15);
		g.drawString(xlabel, 180, 340);
		
		g.drawLine(20, 320, 20,  20);
		g.drawLine(20, 320, 320, 320);
		
		double[] xrange = range(xvalues);
		double[] yrange = range(yvalues);
		
		g.drawString(""+(((int)(100*xrange[0]))/100.), 20, 330);
		g.drawString(""+(((int)(100*xrange[1]))/100.), 320, 330);
		g.drawString(""+(((int)(100*yrange[0]))/100.), 5, 320);
		g.drawString(""+(((int)(100*yrange[1]))/100.), 5, 35);
		
		g.setColor(Color.blue);
		for (int i=0; i<xvalues.length; i++)
		{
			double x = (xvalues[i]-xrange[0])/(xrange[1]-xrange[0]);
			double y = (yvalues[i]-yrange[0])/(yrange[1]-yrange[0]);
			g.drawOval((int)(20+300*x-2), (int)(320-300*y-2), 4, 4);
		}
		
		
	}
	
	private double[] range(double[] arr)
	{
		double[] r = new double[2];
		double min = arr[0];
		double max = arr[0];
		
		for (double d: arr)
		{
			if (d<min) min=d;
			if (d>max) max=d;
		}
		
//		min = (double) ( (int) (min*100) ) / 100;
//		max = (double) ( (int) (max*100) ) / 100;
		
		if (min==max)
		{
			min -= 1;
			max += 1;
		}
		
		r[0] = min;
		r[1] = max;
		
		return r;
	}
}
