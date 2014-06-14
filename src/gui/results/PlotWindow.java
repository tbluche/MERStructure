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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import core.classification.Statistics;


@SuppressWarnings("serial")
public class PlotWindow extends JFrame {

	private Vector<Statistics> 	data;
	private PlotPanel			pp;
	private int					nb;
	
	private static final int nbSymbolAttr 	= 0;
	private static final int parErrAttr 	= 1;
	private static final int relErrAttr 	= 2;
	private static final int symErrAttr 	= 3;
	private static final int symCorrAttr 	= 4;
	private static final int relCorrAttr 	= 5;
	
	private int x = nbSymbolAttr;
	private int y = symCorrAttr;
	
	private static final String[] labels = {"Number of symbols", 
											"% of parenting errors",
											"% of relationship errors",
											"% of symbol errors",
											"Symbol correctness score",
											"Relationship correctness score"};
	
	public PlotWindow(Vector<Statistics> vs)
	{
		super("Visualization");
		data = vs;
		nb = vs.size();
		build();
		pack();
		setVisible(true);
	}
	
	private void build()
	{
		
		JMenuBar mb = new JMenuBar();
		
		JMenu xmenu = new JMenu("x axis");
		JMenu ymenu = new JMenu("y axis");
		
		JMenuItem xNbSym  = new JMenuItem("Number of Symbols");
		JMenuItem xparErr = new JMenuItem("% of Parenting Errors");
		JMenuItem xrelErr = new JMenuItem("% of Relationship Errors");
		JMenuItem xsymErr = new JMenuItem("% of Symbol Errors");
		JMenuItem xrelCor = new JMenuItem("Relationship Correctness");
		JMenuItem xsymCor = new JMenuItem("Symbol Correctness");
		
		JMenuItem yNbSym  = new JMenuItem("Number of Symbols");
		JMenuItem yparErr = new JMenuItem("% of Parenting Errors");
		JMenuItem yrelErr = new JMenuItem("% of Relationship Errors");
		JMenuItem ysymErr = new JMenuItem("% of Symbol Errors");
		JMenuItem yrelCor = new JMenuItem("Relationship Correctness");
		JMenuItem ysymCor = new JMenuItem("Symbol Correctness");
		
		xmenu.add(xNbSym);
		xmenu.add(xparErr);
		xmenu.add(xrelErr);
		xmenu.add(xsymErr);
		xmenu.add(xrelCor);
		xmenu.add(xsymCor);
		
		ymenu.add(yNbSym);
		ymenu.add(yparErr);
		ymenu.add(yrelErr);
		ymenu.add(ysymErr);
		ymenu.add(yrelCor);
		ymenu.add(ysymCor);
		
		mb.add(xmenu);
		mb.add(ymenu);
		
		this.setJMenuBar(mb);
		
		xNbSym.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {x=nbSymbolAttr;updatePlot();}
		});
		xparErr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {x=parErrAttr;updatePlot();}
		});
		xsymErr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {x=symErrAttr;updatePlot();}
		});
		xrelErr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {x=relErrAttr;updatePlot();}
		});
		xsymCor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {x=symCorrAttr;updatePlot();}
		});
		xrelCor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {x=relCorrAttr;updatePlot();}
		});
		yNbSym.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {y=nbSymbolAttr;updatePlot();}
		});
		yparErr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {y=parErrAttr;updatePlot();}
		});
		ysymErr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {y=symErrAttr;updatePlot();}
		});
		yrelErr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {y=relErrAttr;updatePlot();}
		});
		ysymCor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {y=symCorrAttr;updatePlot();}
		});
		yrelCor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {y=relCorrAttr;updatePlot();}
		});
		
		pp = new PlotPanel(extractData(x),extractData(y), 
							labels[x], labels[y]);
		this.add(pp);
		
	}
	
	public double[] extractData(int attr)
	{
		double[] d = new double[nb];
		for (int i=0; i<nb; i++)
		{
			Statistics s = data.get(i);
			switch (attr){
				case nbSymbolAttr: d[i]=s.getNbSymbols(); break;
				case parErrAttr: d[i]=(double)s.getErrorParenting()/s.getNbSymbols(); break;
				case symErrAttr: d[i]=(double)s.getErrorSymbol()/s.getNbSymbols(); break;
				case relErrAttr: d[i]=(double)s.getErrorRelationship()/s.getNbSymbols();break;
				case symCorrAttr: d[i]=s.getCorrectnessSymbol();break;
				case relCorrAttr: d[i]=s.getCorrectnessRelationship();break;
			}
		}
		return d;
	}
	
	public void updatePlot()
	{
		pp.update(extractData(x), extractData(y),
				  labels[x], labels[y]);
		this.repaint();
		this.validate();
		this.pack();
	}
	
}
