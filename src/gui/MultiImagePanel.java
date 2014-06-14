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

import java.util.Vector;

import core.me.Expression;


public class MultiImagePanel {
	
	private int iter = -1;
	private Vector<ImagePanel> panels;
	
	public MultiImagePanel()
	{
		panels = new Vector<ImagePanel>();
	}
	
	public int add(ImagePanel ip)
	{
		panels.add(ip);
		iter = panels.size()-1;
		return iter;
	}
	
	public ImagePanel getCurrent()
	{
		return panels.get(iter);
	}
	
	public int getIter() { return iter+1; }
	
	public ImagePanel next()
	{
		if (panels.size()==0) return null;
		iter++;
		iter = iter % panels.size();
		return panels.get(iter);
	}
	
	public ImagePanel prev()
	{
		if (panels.size()==0) return null;
		iter--;
		if (iter<0) iter+=panels.size();
		iter = iter%panels.size();
		return panels.get(iter);
	}
	
	public Vector<Expression> expressions()
	{
		Vector<Expression> ve = new Vector<Expression>();
		for (ImagePanel ip: panels)
			ve.add(ip.getExpression());
		return ve;
	}

	public int nbIp() { return panels.size(); }

}
