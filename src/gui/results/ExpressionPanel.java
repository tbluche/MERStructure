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

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import core.me.Expression;


public class ExpressionPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private Expression expr_ = (Expression) null;
	
	public ExpressionPanel(Expression e)
	{
		expr_ = e;
		this.build();
	}
	
	private void build()
	{
		this.setLayout(new GridLayout(5, 2));
		
		this.add(new JLabel("Meta-Latex: "));
		this.add(new JLabel(expr_.buildMetaLatex()));
		
		this.add(new JLabel("Valid: "));
		String validity = (expr_.isValid()) ? "Yes" : "No";
		this.add(new JLabel(validity));
		
		this.add(new JLabel("Symbol score: "));
		this.add(new JLabel(""+expr_.sScore()));
		
		this.add(new JLabel("Relationship score: "));
		this.add(new JLabel(""+expr_.rScore()));
		
		this.add(new JLabel("Score: "));
		this.add(new JLabel(""+expr_.score()));
	}
}
