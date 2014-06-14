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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import main.Parameters;

import tools.ArrayTools;

import core.me.Context;
import core.me.Relationship;

@SuppressWarnings("serial")
public class SelectRelationship extends JFrame implements ActionListener {

	private JButton btnNone, btnInline, btnSuperscript, btnSubscript, btnUpper, btnUnder;
	private Context theParent;
	private Context theChild;
	
	public SelectRelationship(Context parent, Context child)
	{
		super("Select a Relationship...");
		
		theParent = parent;
		theChild = child;
		
		this.setLayout(new GridLayout(6, 1));
		
		btnNone = new JButton("None");
		btnInline = new JButton("Inline");
		btnSuperscript = new JButton("Superscript");
		btnSubscript = new JButton("Subscript");
		btnUpper = new JButton("Upper");
		btnUnder = new JButton("Under");
		
		btnNone.addActionListener(this);
		btnInline.addActionListener(this);
		btnSuperscript.addActionListener(this);
		btnSubscript.addActionListener(this);;
		btnUpper.addActionListener(this);
		btnUnder.addActionListener(this);
		
		this.add(btnNone);
		this.add(btnInline);
		this.add(btnSuperscript);
		this.add(btnSubscript);
		this.add(btnUpper);
		this.add(btnUnder);
		
		this.setBounds(450, 200, 0, 0);
		this.pack();
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source==btnInline)
			theChild.setRelationship(
					new Relationship(
							theParent, theChild, 
							ArrayTools.crispDist(
									Parameters.NB_OF_RELATIONSHIP_CLASSES, 
									Relationship.INLINE
									)
									)
			);
		else if (source==btnSuperscript)
			theChild.setRelationship(
					new Relationship(
							theParent, theChild, 
							ArrayTools.crispDist(
									Parameters.NB_OF_RELATIONSHIP_CLASSES, 
									Relationship.SUPERSCRIPT
									)
									)
			);
		else if (source==btnSubscript)
			theChild.setRelationship(
					new Relationship(
							theParent, theChild, 
							ArrayTools.crispDist(
									Parameters.NB_OF_RELATIONSHIP_CLASSES, 
									Relationship.SUBSCRIPT
									)
									)
			);
		else if (source==btnUpper)
			theChild.setRelationship(
					new Relationship(
							theParent, theChild, 
							ArrayTools.crispDist(
									Parameters.NB_OF_RELATIONSHIP_CLASSES, 
									Relationship.UPPER
									)
									)
			);
		else if (source==btnUnder)
			theChild.setRelationship(
					new Relationship(
							theParent, theChild, 
							ArrayTools.crispDist(
									Parameters.NB_OF_RELATIONSHIP_CLASSES, 
									Relationship.UNDER
									)
									)
			);
		else 
			theChild.setParent(theParent);
		this.dispose();
	}
	
	
}
