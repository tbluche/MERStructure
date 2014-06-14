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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import core.me.Context;
import core.me.Relationship;
import core.me.Symbol;
import core.me.SymbolClass;


@SuppressWarnings("serial")
public class PropPanel extends JPanel {
	
	private Context			 cont = (Context) null;
	private JLabel[]		 labs = null;
	
	public PropPanel(Context c)
	{
		this.labs = new JLabel[30];
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(220, 300));
		this.setContext(c);
	}
	
	public void setContext(Context c)
	{
		if (c != null)
			this.cont = c;
		else 
			this.cont = null;
		this.updateProp();
	}
	
	
	public void updateProp()
	{
		if (cont != null)
		{
			Symbol prop = cont.getSymbol();
			labs[0] = new JLabel(" "+prop.sid);
			labs[1] = new JLabel(prop.xmin+", "+prop.xmax+", "+prop.ymin+", "+prop.ymax);
			labs[2] = new JLabel(prop.getHeight()+"/"+prop.getWidth());
			labs[3] = new JLabel(""+prop.getRatio());
			labs[4] = new JLabel(""+prop.getCenter());
			labs[5] = new JLabel(SymbolClass.TEXTCLASS[cont.getSymbolClass()]);
			labs[6] = cont.hasParent() ? new JLabel(Relationship.TEXTREL[cont.getRelationship()]) : new JLabel("No parent");
			labs[7] = (cont.getParent()==null)? new JLabel("-") : new JLabel(SymbolClass.TEXTCLASS[cont.getParent().getSymbolClass()]);

		}
		else
			for (int i=0; i<labs.length; i++)
				labs[i] = new JLabel(" - ");
		this.setFields();
	}
	
	public void setFields()
	{
		String[] fields = {"Symbol #",
				"Bounding box",
				"Height/Width",	"Ratio", "Centre",
				"Symbol class",
				"Relationship", "Parent Class"
				}; 
				
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipadx = 10;
		gbc.ipady = 5;
		
		for (int i=0; i<fields.length; i++)
		{
			gbc.gridy = i;
			
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.EAST;
			this.add(new JLabel(fields[i]), gbc);
			
			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.WEST;
			this.add(labs[i], gbc);
		}
		
	}

}
