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


package gui.filemanager;

import exceptions.InvalidInterpretation;
import gui.MainWindow;
import gui.MultiImagePanel;
import gui.results.PlotWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.jdom.JDOMException;

import core.classification.Statistics;
import core.imageproc.ImageProc;
import core.me.Expression;


import tools.LatexParser;
import tools.XMLCreator;

@SuppressWarnings("serial")
public class FileManager extends JFrame {
	
	private static FileManager ui = null;
	private static String      ws = "C:\\";
	
	private Vector<Vector<JButton>> menus;
	
	private String selectedFile = ""; 
	private JScrollPane listOfFiles;
	private JPanel jpMenu;
	
	public static FileManager get()
	{
		if (ui == null) ui = new FileManager();
		return ui;
	}
	
	private FileManager()
	{
		super();
		setTitle("File Manager");
		build();
		pack();
		setVisible(true);
	}

	private void build() {
		
		if (listOfFiles!=null) this.remove(listOfFiles);
		if (jpMenu!=null) this.remove(jpMenu);
		
		this.setLayout(new GridLayout(2, 1));
		
		JPanel jp = new JPanel();
		
		Vector<FileButton> vbutton = this.retrieveButtons();
		jp.setLayout(new GridLayout(vbutton.size()+1, 1));
		
		JButton parentBtn = new JButton("\\..");
		parentBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { FileManager.get().parentDir(); }
		});
		parentBtn.setBackground(Color.yellow);
		jp.add(parentBtn);
		
		for (FileButton fb: vbutton)
			jp.add(fb);
		
		listOfFiles = new JScrollPane(jp);
		listOfFiles.setPreferredSize(new Dimension(400, 300));
		
		this.add(listOfFiles);
		jpMenu = new JPanel();
		this.add(jpMenu);
		this.buildMenus();
		
	}
	
	private void buildMenus()
	{
		// Initialize vector of menus
		menus = new Vector<Vector<JButton>>(4);
		
		// Menu for XML
		JButton loadExpr 	= new JButton("Load Single Expression");
		JButton loadAllExpr = new JButton("Load All Expressions");
		JButton loadList	= new JButton("Load IDs for Single Expression");
		JButton loadListAll	= new JButton("Load IDs for All Expressions");
		JButton compareSing = new JButton("Compare with Single Expression");
		JButton compareAll	= new JButton("Compare with All Expressions");
		
		loadExpr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					MainWindow.inst.setExpression(XMLCreator.extractExpressionFromXML(ws+selectedFile));
				} catch (JDOMException e1) {
					System.out.println("JDOM Error: ");
					e1.printStackTrace();
				} catch (IOException e1) {
					System.out.println("Error while opening the file.");
					e1.printStackTrace();
				} catch (InvalidInterpretation e1) {
					System.out.println(e1);
				}
			}
		});
		
		loadAllExpr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Vector<Expression> allExprs = XMLCreator.extractExpressionsFromXML(ws+selectedFile);
					for (Expression ex: allExprs) MainWindow.inst.setExpression(ex);
				} catch (JDOMException e1) {
					System.out.println("JDOM Error: ");
					e1.printStackTrace();
				} catch (IOException e1) {
					System.out.println("Error while opening the file.");
					e1.printStackTrace();
				} catch (InvalidInterpretation e1) {
					System.out.println(e1);
				}
			}
		});
		
		loadList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					MainWindow.inst.getExpression().assignIds(XMLCreator.extractSymbolIds(ws+selectedFile));
				} catch (JDOMException e1) {
					System.out.println("JDOM Error: ");
					e1.printStackTrace();
				} catch (IOException e1) {
					System.out.println("Error while opening the file.");
					e1.printStackTrace();
				} catch (InvalidInterpretation e1) {
					System.out.println(e1);
				}
			}
		});
		
		loadListAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					MainWindow.inst.assignAllIds(XMLCreator.extractAllSymbolIds(ws+selectedFile));
				} catch (JDOMException e1) {
					System.out.println("JDOM Error: ");
					e1.printStackTrace();
				} catch (IOException e1) {
					System.out.println("Error while opening the file.");
					e1.printStackTrace();
				} catch (InvalidInterpretation e1) {
					System.out.println(e1);
				}
			}
		});
		
		compareAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Vector<Statistics> comparison = MainWindow.inst.compareAll(
									XMLCreator.extractExpressionsFromXML(ws+selectedFile)
								);
					for(Statistics st: comparison) MainWindow.inst.addResultText(st.toString()+"\n\n");
					String res="";
					for(Statistics st: comparison) res += st.getLatexResult()+"\n";
					MainWindow.inst.addResultText(res);
					new PlotWindow(comparison);
				} catch (JDOMException e1) {
					System.out.println("JDOM Error: ");
					e1.printStackTrace();
				} catch (IOException e1) {
					System.out.println("Error while opening the file.");
					e1.printStackTrace();
				} catch (InvalidInterpretation e1) {
					System.out.println(e1);
				}
			}
		});
		
		compareSing.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Statistics comparison = MainWindow.inst.getExpression().compare(
									XMLCreator.extractExpressionFromXML(ws+selectedFile)
								);
					MainWindow.inst.addResultText(comparison.toString());
				} catch (JDOMException e1) {
					System.out.println("JDOM Error: ");
					e1.printStackTrace();
				} catch (IOException e1) {
					System.out.println("Error while opening the file.");
					e1.printStackTrace();
				} catch (InvalidInterpretation e1) {
					System.out.println(e1);
				}
			}
		});
		
		Vector<JButton> xmlMenu = new Vector<JButton>();
		xmlMenu.add(loadExpr);
		xmlMenu.add(loadAllExpr);
		xmlMenu.add(loadList);
		xmlMenu.add(loadListAll);
		xmlMenu.add(compareSing);
		xmlMenu.add(compareAll);
		
		// TEX Menu
		JButton loadAllTex = new JButton("Load Expressions");
		
		loadAllTex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					MainWindow.inst.clearExpressions();
					MainWindow.inst.addResultText("Trying to parse: "+ws+selectedFile);
					LatexParser latpars = new LatexParser(ws+selectedFile);
					MultiImagePanel mip = latpars.latexToMip();
					if (mip.expressions().size()>0) MainWindow.inst.setMip( mip );
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		Vector<JButton> texMenu = new Vector<JButton>();
		texMenu.add(loadAllTex);
		
		// MOD Menu
		JButton loadSCA  = new JButton("Read SCA");
		JButton loadSCB  = new JButton("Read SCB");
		JButton loadSCC1 = new JButton("Read SCC1");
		JButton loadSCC2 = new JButton("Read SCC2");
		JButton loadSCC3 = new JButton("Read SCC3");
		JButton loadSCC4 = new JButton("Read SCC4");
		JButton loadSCC5 = new JButton("Read SCC5");
		JButton loadRC   = new JButton("Read RC");
		
		
		Vector<JButton> modMenu = new Vector<JButton>();
		modMenu.add(loadSCA);
		modMenu.add(loadSCB);
		modMenu.add(loadSCC1);
		modMenu.add(loadSCC2);
		modMenu.add(loadSCC3);
		modMenu.add(loadSCC4);
		modMenu.add(loadSCC5);
		modMenu.add(loadRC);
		
		// IMG Menu
		JButton openImg = new JButton("Open (add)");
		JButton newImg = new JButton("Open (new)");
		
		openImg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { ImageProc im = new ImageProc(ws+selectedFile); MainWindow.inst.setImage(im); }
		});
		
		newImg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{ 
				MainWindow.inst.clearExpressions();
				ImageProc im = new ImageProc(ws+selectedFile); 
				MainWindow.inst.setImage(im); 
			}
		});
		
		Vector<JButton> imgMenu = new Vector<JButton>();
		imgMenu.add(openImg);
		imgMenu.add(newImg);
		
		
		menus.add(xmlMenu);
		menus.add(texMenu);
		menus.add(modMenu);
		menus.add(imgMenu);
	}
	
	public void displayMenu(String file, int type)
	{
		this.setSelected(file);
		
		this.remove(jpMenu);
		Vector<JButton> theMenu = menus.get(type-1);
		jpMenu = new JPanel();
		jpMenu.setLayout(new GridLayout(theMenu.size(), 1));
		jpMenu.setPreferredSize(new Dimension(400, 100));
		jpMenu.setBorder(new TitledBorder("What do you want to do? ("+selectedFile+")"));
		
		for (JButton btn: theMenu)
			jpMenu.add(btn);
		
		this.add(jpMenu);
		this.validate();
		this.repaint();
		this.pack();
	}
	
	private void setSelected(String file) { selectedFile = file; }

	public void update()
	{
		this.build();
		this.validate();
		this.repaint();
		String[] testr = ws.split("\\\\");
		for (String str: testr)
			System.out.print(str+" - ");
		System.out.println();
	}
	
	public void parentDir()
	{
		String[] testr = ws.split("\\\\");
		String new_ws = "";
		if (testr.length>1)
		{
			for (int i=0; i<testr.length-1; i++)
				new_ws += testr[i]+"\\";
			this.changeWorkspace(new_ws);
			this.update();
		}
	}
	
	private Vector<FileButton> retrieveButtons() {
		String[] list = (new File(getWorkspace())).list();
		Vector<FileButton> vb = new Vector<FileButton>();
		for (String str: list)
			vb.add( new FileButton(str) );
		return vb;
	}

	public String getWorkspace() { return ws; }
	public void changeWorkspace(String new_ws) { ws = new_ws; }
}
