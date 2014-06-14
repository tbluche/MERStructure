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
import java.awt.Graphics;

import javax.swing.JPanel;

import core.me.Context;
import core.me.Relationship;
import core.me.SymbolClass;


@SuppressWarnings("serial")
public class ClassificationPanel extends JPanel {

	private Context context_ = (Context) null;
	
	public ClassificationPanel(Context c){
		this.setPreferredSize(new Dimension(220, 300));
		this.setLayout(null);
		this.setContext(c);
	}
	
	public void setContext(Context c)
	{
		this.context_ = c;
		this.repaint();
		this.validate();
	}
	
	public void paintComponent(Graphics g)
	{
		Histogram c1, c2, c3, c4, c5, a, b, c, r, s;
//		Histogram f;
		SymbolClass sc;
		Relationship rc;
		
		if (context_ != null)
		{
			sc = context_.getSymbolClassObject();
			rc = context_.getRelationshipObject();
			
			r = new Histogram(rc.confidences(), 50, 50);
			r.setBounds(4, 184, 56, 56);
			this.add(r);
			
			s = new Histogram(sc.confidences(), 50, 50);
			s.setBounds(124, 184, 56, 56);
			this.add(s);
			
			a = new Histogram(sc.getDistA(), 50, 50);
			a.setBounds(4, 94, 56, 56);
			this.add(a);
			g.drawLine(34, 124, 132, 220);
			
			if (context_.hasParent())
			{
//				if (context_.getSymbolClass()>0)
//				{
//					f = new Histogram(context_.getParent().getFuzzyRegions().memberships(context_,context_.getSymbolClass()), 50, 50);
//					f.setBounds(64, 184, 56, 56);
//					this.add(f);
//				}
				
				b = new Histogram(sc.getDistB(), 50, 50);
				b.setBounds(64, 94, 56, 56);
				this.add(b);
				g.drawLine(99, 124, 132, 220);
			}
			
			if (context_.hasChildren())
			{
				c = new Histogram(sc.getDistC(), 50, 50);
				c.setBounds(124, 94, 56, 56);
				this.add(c);
				g.drawLine(134, 124, 132, 220);
				
				if (context_.hasHor())
				{
					c5 = new Histogram(sc.getDistC(Relationship.INLINE), 35, 35);
					c5.setBounds(10, 10, 41, 41);
					this.add(c5);
					g.drawLine(31, 50, 155, 99);
				}
				
				if (context_.hasSup())
				{
					c4 = new Histogram(sc.getDistC(Relationship.SUPERSCRIPT), 35, 35);
					c4.setBounds(50, 10, 41, 41);
					this.add(c4);
					g.drawLine(71, 50, 155, 99);
				}	
				
				if (context_.hasSub())
				{
					c1 = new Histogram(sc.getDistC(Relationship.SUBSCRIPT), 35, 35);
					c1.setBounds(90, 10, 41, 41);
					this.add(c1);
					g.drawLine(111, 50, 155, 99);
				}
				
				if (context_.hasUpp())
				{
					c2 = new Histogram(sc.getDistC(Relationship.UPPER), 35, 35);
					c2.setBounds(130, 10, 41, 41);
					this.add(c2);
					g.drawLine(152, 50, 155, 99);
				}
				
				if (context_.hasUnd())
				{
					c3 = new Histogram(sc.getDistC(Relationship.UNDER), 35, 35);
					c3.setBounds(170, 10, 41, 41);
					this.add(c3);
					g.drawLine(173, 50, 155, 99);
				}
			}
		}
	}
}
