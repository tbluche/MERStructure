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


package gui.input;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements MouseMotionListener, MouseListener{
	
	private BufferedImage bi;
	@SuppressWarnings("unused")
	private boolean writing = false;
	
	private int lastx=-1;
	private int lasty=-1;
	
	public DrawPanel(){
		bi = new BufferedImage(320,240,BufferedImage.TYPE_INT_RGB);
		for (int x=0; x<bi.getWidth(); x++)
			for (int y=0; y<bi.getHeight(); y++)
				bi.setRGB(x, y, Color.WHITE.getRGB());
		setPreferredSize(new Dimension(bi.getWidth()+10,bi.getHeight()+15));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		writing = false;
	}
	
	public void paintComponent (Graphics g)
	{
		g.drawImage(bi,0,0,bi.getWidth(), bi.getHeight(), null);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, bi.getWidth(), bi.getHeight());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x=e.getX();
		int y=e.getY();
		
		
		if (x>0 && y>0 && x<bi.getWidth() && y<bi.getHeight())
			if (lastx>0)
				drawLine(lastx, lasty, x, y);
		lastx = x;
		lasty = y;
		
		repaint();
		
	}
	
	public void drawLine(int i, int j, int k, int l)
	{
		int x1 = (i<k) ? i : k;
		int x2 = (i>k) ? i : k;
		int y1 = (j<l) ? j : l;
		int y2 = (j>l) ? j : l;
		
		int y;
		
		if (x2>x1)
			for (int x=x1; x<x2; x++)
			{
				y = (int) Math.floor(((double)(y2*(x-x1)+y1*(x2-x)))/(x2-x1));
				bi.setRGB(x, y, 0);
				bi.setRGB(x-1, y, 0);
				bi.setRGB(x+1, y, 0);
				bi.setRGB(x, y-1, 0);
				bi.setRGB(x, y+1, 0);
				bi.setRGB(x+1, y-1, 0);
				bi.setRGB(x+1, y+1, 0);
				bi.setRGB(x-1, y-1, 0);
				bi.setRGB(x-1, y+1, 0);
			}
		else
			for (y=y1; y<y2; y++)
			{
				bi.setRGB(x1, y, 0);
				bi.setRGB(x1-1, y, 0);
				bi.setRGB(x1+1, y, 0);
			}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		writing = true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		writing = false;
		lastx = -1;
		lasty = -1;
		
	}
	
	public BufferedImage getBI()
	{
		return bi;
	}

}
