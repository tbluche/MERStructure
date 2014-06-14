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

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class is used to display results in a textual form.
 * @author Théodore Bluche
 *  gui
 */
@SuppressWarnings("serial")
public class ResultView extends JFrame{
	
	private JTextArea text;
	
	public ResultView()
	{
		super("Results view");
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		this.build();
		
		
		this.pack();
		this.setVisible(true);
		
	}

	/**
	 * Builds the content of the result view
	 */
	private void build() {
		text = new JTextArea(25,40);
		JScrollPane jsp = new JScrollPane(text);
		this.add(jsp);
	}
	
	/**
	 * Add some text in the result view
	 * @param str : text to append in the result view
	 */
	public void append(String str)
	{
		text.append("\n---\n");
		text.append(str);
	}

}
