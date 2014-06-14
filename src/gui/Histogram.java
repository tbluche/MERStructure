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


package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Histogram extends JPanel implements MouseListener {
	
	private double[] bars;
	private int		 height;
	private int      width;
	
	public Histogram(double[] arr)
	{
		build(arr, 100, 100);
	}
	
	public Histogram(double[] arr, boolean small)
	{
		if (small) build(arr, 50, 50); else build(arr, 100, 100);
	}
	
	public Histogram(double[] arr, int h, int w) {
		build(arr,h,w);
	}

	private void build(double[] arr, int height, int width)
	{
		this.bars = arr;
		this.height = height;
		this.width = width;
		this.setPreferredSize(new Dimension(width+20, height+20));
		this.addMouseListener(this);
	}
	
	public void paintComponent(Graphics g) {
		
		g.setColor(Color.WHITE);
		g.fillRect(5, 5, width, height);
		
		g.setColor(Color.BLACK);
		g.drawRect(5, 5, width, height);
		
		if (bars != null)
		{
			int len = this.bars.length;
			int steps = 2*len+1;
			int w = (int) Math.floor(width/steps);
			int h;
			
			
			for (int i=0; i<len; i++)
			{
				h = (int) Math.floor(0.8*height*bars[i]);
				g.setColor(Color.BLUE);
				g.fillRect((2*i+1)*w+5, (int)Math.floor(0.8*height-h+5),
						    w, h);
				g.setColor(Color.BLACK);
				g.drawString(""+i,
						     (int)Math.floor((2*i+1.3)*w)+5, (int) Math.floor(0.9*height+1));
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JFrame njf = new JFrame("Histogram");
		njf.add(new Histogram(this.bars, 400, 600));
		njf.pack();
		njf.setVisible(true);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
