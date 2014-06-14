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

import gui.MainWindow;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import core.imageproc.ImageProc;

import tools.LatexParser;

public class LatexToImage extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private MainWindow ref;
	private JTextField  latexFormula;
	private JButton     ok;
	private JButton     cancel;
	
	public LatexToImage(MainWindow parent)
	{
		super("Write a formula...");
		this.ref = parent;
		this.build();
		this.pack();
		this.setVisible(true);
	}
	
	public void build(){
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		
		
		latexFormula = new JTextField("                                           ");
		latexFormula.setFont(new Font("Arial",Font.TRUETYPE_FONT,25));
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		
		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.insets = new Insets(30,30,30,30);
		this.add(latexFormula,gbc);
		
		gbc.gridy = 2;
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.insets = new Insets(0,30,30,0);
		gbc.anchor = GridBagConstraints.EAST;
		this.add(ok, gbc);
		
		gbc.gridx = 2;
		gbc.insets = new Insets(0,0,30,30);
		gbc.anchor = GridBagConstraints.WEST;
		this.add(cancel, gbc);
		
		cancel.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				dispose();
            }

        });
		
		ok.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				ImageProc im = new ImageProc(getLatexImage());
				ref.setImage(im);
				dispose();
            }
        });
	}
	
	private BufferedImage getLatexImage() {
		String latex = this.latexFormula.getText();

		return LatexParser.getLatexImage(latex);
	}
	
	

}
