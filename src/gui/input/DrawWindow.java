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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import core.imageproc.ImageProc;

@SuppressWarnings("serial")
public class DrawWindow extends JFrame{

	private MainWindow ref;
	private DrawPanel	dp;
	private JButton     ok;
	private JButton     cancel;
	
	public DrawWindow(MainWindow parent)
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
		
		
		
		dp = new DrawPanel();
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		
		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(30,30,30,30);
		this.add(dp,gbc);
		
		gbc.gridy = 2;
		gbc.gridx = 1;
		gbc.gridwidth = 1;
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
				ImageProc im = new ImageProc(dp.getBI());
				ref.setImage(im);
				dispose();
            }

        });
	}
}
