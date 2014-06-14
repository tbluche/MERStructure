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

import gui.filemanager.FileManager;
import gui.input.DrawWindow;
import gui.input.LatexToImage;
import gui.results.PlotWindow;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import org.jdom.JDOMException;

import core.classification.Statistics;
import core.imageproc.ImageProc;
import core.me.Context;
import core.me.Expression;

import exceptions.InvalidInterpretation;

import tools.ArrayTools;
import tools.LatexParser;
import tools.XMLCreator;

import main.Parameters;


/**
 * 
 * This class represent the GUI menu bar, with the different options. 
 * 
 * @author Théodore Bluche
 *  gui
 */
@SuppressWarnings("serial")
public class TopMenu extends JMenuBar implements ActionListener {
	
	private MainWindow  iw;
	
	private JMenuItem    saveXMLint;
	private JMenuItem    saveXMLlist;
	private JMenuItem	 saveXMLAllInt;
	private JMenuItem	 saveXMLAllList;
	private JMenuItem    saveImage;
	private JMenuItem	 saveView;
	private JMenuItem    saveHTML;
	private JMenuItem	 setClass1;
	private JMenuItem	 setClass2;
	private JMenuItem	 setClass3;
	private JMenuItem	 setClass4;
	private JMenuItem	 setRel1;
	private JMenuItem	 setRel2;
	private JMenuItem	 setRel3;
	private JMenuItem    setRel4;
	private JMenuItem    setRel5;
	
	private JMenuItem    loadExpr;
	private JMenuItem	 loadAllExpr;
	private JMenuItem    loadIds;
	private JMenuItem    compare;
	private JMenuItem    compareAll;
	private JMenuItem    loadLatex;
	
	private JMenuItem	 prevExpr;
	private JMenuItem	 nextExpr;
	
	private JMenuItem    clusterize;
	private JMenuItem    nextCluster;
	private JMenuItem    prevCluster;
	
	private JMenu		setClassMenu;
	private JMenu		setRelMenu;
	
	private Context selected = (Context) null;
	
	public TopMenu()
	{
		super();
		iw = MainWindow.inst;
		this.build();
	}
	
	/**
	 * Create the menus
	 */
	private void build()
	{
		
		// Define menus
		
		JMenu F=new JMenu("File");
		JMenu C=new JMenu("Clusters");
		JMenu P=new JMenu("Display");
		JMenu S=new JMenu("Save");
		JMenu L=new JMenu("Load");
		JMenu G=new JMenu("Go to...");
		
		setClassMenu = new JMenu("Set Class...");
		setRelMenu   = new JMenu("Set Relationship...");
		
		// Define menu items
		
		JMenuItem F_Open=new JMenuItem("Open...");
		JMenuItem F_Draw=new JMenuItem("Draw");
		JMenuItem F_Latex=new JMenuItem("Enter a LaTeX formula");
		JMenuItem F_Close=new JMenuItem("Close");
		JMenuItem P_Menu1=new JMenuItem("Show Parents");
		JMenuItem P_Menu2=new JMenuItem("Hide Parents");
		JMenuItem P_Menu3=new JMenuItem("Show Baselines");
		JMenuItem P_Menu4=new JMenuItem("Hide Baselines");
		JMenuItem P_Menu5=new JMenuItem("Show Regions");
		JMenuItem P_Menu6=new JMenuItem("Hide Regions");
		JMenuItem P_Menu7=new JMenuItem("Show Expression");
		JMenuItem P_Menu8=new JMenuItem("Hide Expression");
		JMenuItem P_Menu9=new JMenuItem("Show Workspace");
		JMenuItem showClu=new JMenuItem("Show Clusters");
		JMenuItem hideClu=new JMenuItem("Hide Clusters");
		JMenuItem showRes=new JMenuItem("Show Result view");
		
		clusterize  = new JMenuItem("Clusterize");
		nextCluster = new JMenuItem("Next Clusters"); 
		prevCluster = new JMenuItem("Previous Clusters");
		
		saveImage      = new JMenuItem("Save Image");
		saveView       = new JMenuItem("Save View");
		saveXMLint     = new JMenuItem("Export Interpretation in XML");
		saveXMLlist    = new JMenuItem("Export list of symbols");
		saveXMLAllInt  = new JMenuItem("Export All Interpretations in XML");
		saveXMLAllList = new JMenuItem("Export lists of all expressions");
		saveHTML	 = new JMenuItem("Save HTML");
		setClass1    = new JMenuItem("small");
		setClass2    = new JMenuItem("descending");
		setClass3    = new JMenuItem("ascending");
		setClass4	 = new JMenuItem("varibale range");
		setRel1		 = new JMenuItem("inline");
		setRel2		 = new JMenuItem("superscript");
		setRel3		 = new JMenuItem("subscript");
		setRel4		 = new JMenuItem("upper");
		setRel5		 = new JMenuItem("under");
		
		loadLatex	 = new JMenuItem("Load set of ME from Latex");
		loadExpr	 = new JMenuItem("Load Expression from XML...");
		loadAllExpr  = new JMenuItem("Load all Expressions from XML...");
		loadIds	 	 = new JMenuItem("Load Ids from XML...");
		compare		 = new JMenuItem("Compare with interpreation");
		compareAll	 = new JMenuItem("Compare All expressions with interpretations");
		
		prevExpr	 = new JMenuItem("Previous Expression");
		nextExpr	 = new JMenuItem("Next Expression");
		
		// Add the options to the menus
		
		F.add(F_Open);
		F.add(F_Draw);
		F.add(F_Latex);
		F.add(F_Close);
		
		C.add(clusterize);
		C.add(nextCluster);
		C.add(prevCluster);
		
		P.add(P_Menu1);
		P.add(P_Menu2);
		P.add(P_Menu3);
		P.add(P_Menu4);
		P.add(P_Menu5);
		P.add(P_Menu6);
		P.add(P_Menu7);
		P.add(P_Menu8);
		P.add(showClu);
		P.add(hideClu);
		P.add(P_Menu9);
		P.add(showRes);
		
		S.add(saveImage);
		S.add(saveView);
		S.add(saveXMLlist);
		S.add(saveXMLint);
		S.add(saveXMLAllInt);
		S.add(saveXMLAllList);
		S.add(saveHTML);
		
		L.add(loadLatex);
		L.add(loadExpr);
		L.add(loadAllExpr);
		L.add(loadIds);
		L.add(compare);
		L.add(compareAll);
		
		G.add(prevExpr);
		G.add(nextExpr);
		
		setClassMenu.add(setClass1);
		setClassMenu.add(setClass2);
		setClassMenu.add(setClass3);
		setClassMenu.add(setClass4);
		setRelMenu.add(setRel1);
		setRelMenu.add(setRel2);
		setRelMenu.add(setRel3);
		setRelMenu.add(setRel4);
		setRelMenu.add(setRel5);
		
		// Add the menus
		this.add(F);
//		this.add(C);
		this.add(P);
		this.add(S);
		this.add(L);
		this.add(G);
		this.add(setClassMenu);
		this.add(setRelMenu);
		
		
		// Specify action listeners
		F_Open.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				String file = MainWindow.chooseFile("F:\\Oxford\\Project\\New Dataset\\ME\\", "bmp");
				ImageProc im = new ImageProc(file);
				iw.setImage(im);
            }

        });
		
		F_Close.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				iw.dispose();
				for (JFrame jf : iw.dependencies)
					jf.dispose();
            }

        });
		
		F_Draw.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				new DrawWindow(iw);
            }

        });
		
		F_Latex.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				new LatexToImage(iw);
            }

        });
		
		P_Menu1.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {ImagePanel.SHOW_PARENT=true;iw.panel_.repaint();}
        });
		P_Menu2.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {ImagePanel.SHOW_PARENT=false;iw.panel_.repaint();}
        });
		P_Menu3.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {ImagePanel.SHOW_BASELINE=true;iw.panel_.repaint();}
        });
		P_Menu4.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {ImagePanel.SHOW_BASELINE=false;iw.panel_.repaint();}
        });
		P_Menu5.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {ImagePanel.SHOW_REGION=true;iw.panel_.repaint();}
        });
		P_Menu6.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {ImagePanel.SHOW_REGION=false;iw.panel_.repaint();}
        });
		P_Menu7.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {ImagePanel.SHOW_ME=true;iw.panel_.repaint();}
        });
		P_Menu8.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {ImagePanel.SHOW_ME=false;iw.panel_.repaint();}
        });
		showClu.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {ImagePanel.SHOW_CLUSTERS=true;iw.panel_.repaint();}
        });
		hideClu.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {ImagePanel.SHOW_CLUSTERS=false;iw.panel_.repaint();}
        });
		P_Menu9.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {FileManager.get().setVisible(true);}
        });
		showRes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {MainWindow.inst.addResultText("");}
		});
		
		clusterize.addActionListener(this);
		nextCluster.addActionListener(this);
		prevCluster.addActionListener(this);
		
		saveImage.addActionListener(this);
		saveView.addActionListener(this);
		saveXMLint.addActionListener(this);
		saveXMLlist.addActionListener(this);
		saveXMLAllInt.addActionListener(this);
		saveXMLAllList.addActionListener(this);
		saveHTML.addActionListener(this);
		
		setClass1.addActionListener(this);
		setClass2.addActionListener(this);
		setClass3.addActionListener(this);
		setClass4.addActionListener(this);
		
		setRel1.addActionListener(this);
		setRel2.addActionListener(this);
		setRel3.addActionListener(this);
		setRel4.addActionListener(this);
		setRel5.addActionListener(this);
		
		loadExpr.addActionListener(this);
		loadAllExpr.addActionListener(this);
		loadIds.addActionListener(this);
		compare.addActionListener(this);
		compareAll.addActionListener(this);
		loadLatex.addActionListener(this);
		
		prevExpr.addActionListener(this);
		nextExpr.addActionListener(this);
		
		this.setSelected((Context)null);
	}
	
	/**
	 * Record which symbol is currently selected, and show/hide menus consequently.
	 * @param sel : the selected symbol
	 */
	public void setSelected(Context sel)
	{
		selected = sel;
		if (selected==null) this.hideContextMenu(); else this.showContextMenu();
		this.validate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem)e.getSource();
		if (source==saveImage)
		{
			final JFrame njf = new JFrame("Enter a name...");
			final JTextField fname = new JTextField("myimage.bmp");
			fname.setFont(new Font("Arial",Font.TRUETYPE_FONT,25));
			JButton promptOk = new JButton("Save");
			njf.setLayout(new GridLayout(1,2));
			njf.add(fname);
			njf.add(promptOk);
			njf.setBounds(50, 50, 0, 0);
			njf.pack();
			njf.setVisible(true);
			
			
			promptOk.addActionListener(new ActionListener() {
				@Override
	            public void actionPerformed(ActionEvent arg0) {
					try{
						String filename = fname.getText();
						iw.panel_.save(FileManager.get().getWorkspace()+filename);
						Thread.sleep(5000);
						njf.dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}
	            }

	        });
		
		}
		else if (source==saveView)
		{
			final JFrame njf = new JFrame("Enter a name...");
			final JTextField fname = new JTextField("myview.png");
			fname.setFont(new Font("Arial",Font.TRUETYPE_FONT,25));
			JButton promptOk = new JButton("Save");
			njf.setLayout(new GridLayout(1,2));
			njf.add(fname);
			njf.add(promptOk);
			njf.setBounds(50, 50, 0, 0);
			njf.pack();
			njf.setVisible(true);
			
			promptOk.addActionListener(new ActionListener() {
				@Override
	            public void actionPerformed(ActionEvent arg0) {
					String filename = fname.getText();
					MainWindow.captureJPanel(iw.panel_, FileManager.get().getWorkspace()+filename);
					try {
						Thread.sleep(3000);
						njf.dispose();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	            }

	        });
		}
		else if (source==saveXMLint)
		{
			iw.getExpression().exportCurrentInterpretation("xml_interpretation.xml");
		}
		else if (source==saveXMLAllInt)
		{
			iw.exportAllInterpretations("xml_interpretations.xml");
		}
		else if (source==saveXMLAllList)
		{
			iw.exportAllList("xml_lists.xml");
		}
		else if (source==saveXMLlist)
		{
			iw.getExpression().exportListSymbols("xml_list.xml");
		}
		else if (source==saveHTML)
		{
			iw.exportHTML("html_list.html");
		}
		else if (source==setClass1)
			selected.setClass(0);
		else if (source==setClass2)
			selected.setClass(1);
		else if (source==setClass3)
			selected.setClass(2);
		else if (source==setClass4)
			selected.setClass(3);
		else if (source==setRel1)
			selected.setRelationship(ArrayTools.crispDist(Parameters.NB_OF_RELATIONSHIP_CLASSES, 0));
		else if (source==setRel2)
			selected.setRelationship(ArrayTools.crispDist(Parameters.NB_OF_RELATIONSHIP_CLASSES, 1));
		else if (source==setRel3)
			selected.setRelationship(ArrayTools.crispDist(Parameters.NB_OF_RELATIONSHIP_CLASSES, 2));
		else if (source==setRel4)
			selected.setRelationship(ArrayTools.crispDist(Parameters.NB_OF_RELATIONSHIP_CLASSES, 3));
		else if (source==setRel5)
			selected.setRelationship(ArrayTools.crispDist(Parameters.NB_OF_RELATIONSHIP_CLASSES, 4));
		else if (source==loadLatex)
		{
			String path = MainWindow.chooseFile("\\", "tex");
			if (!path.equals(""))
			{
				try {
					MainWindow.inst.clearExpressions();
					LatexParser latpars = new LatexParser(path);
					MainWindow.inst.setMip( latpars.latexToMip() );
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if (source==loadExpr)
		{
			String path = MainWindow.chooseFile("\\", "xml");
			if (!path.equals(""))
				try {
					MainWindow.inst.setExpression(XMLCreator.extractExpressionFromXML(path));
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
		else if (source==loadAllExpr)
		{
			String path = MainWindow.chooseFile("\\", "xml");
			if (!path.equals(""))
				try {
					Vector<Expression> allExprs = XMLCreator.extractExpressionsFromXML(path);
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
		else if (source==loadIds)
		{
			String path = MainWindow.chooseFile("", "xml");
			if (!path.equals(""))
				try {
					MainWindow.inst.getExpression().assignIds(XMLCreator.extractSymbolIds(path));
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
		else if (source==compare)
		{
			String path = MainWindow.chooseFile("\\", "xml");
			if (!path.equals(""))
				try {
					Statistics comparison = MainWindow.inst.getExpression().compare(
									XMLCreator.extractExpressionFromXML(path)
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
		else if (source==compareAll)
		{
			String path = MainWindow.chooseFile("\\", "xml");
			if (!path.equals(""))
				try {
					Vector<Statistics> comparison = MainWindow.inst.compareAll(
									XMLCreator.extractExpressionsFromXML(path)
								);
					for(Statistics st: comparison) MainWindow.inst.addResultText(st.toString()+"\n\n");
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
		else if (source==prevExpr)
			MainWindow.inst.prevExpression();
		else if (source==nextExpr)
			MainWindow.inst.nextExpression();
		else if (source==clusterize)
		{
			MainWindow.inst.getExpression().clusterize();
			MainWindow.inst.updateImagePanel();
		}
		else if (source==nextCluster)
		{
			MainWindow.inst.getExpression().nextClusters();
			MainWindow.inst.updateImagePanel();
		}
		else if (source==prevCluster)
		{
			MainWindow.inst.getExpression().prevCusters();
			MainWindow.inst.updateImagePanel();
		}

	}

	/**
	 * Hides the menus corresponding to selected symbol
	 */
	public void hideContextMenu()
	{
		setClassMenu.setVisible(false);
		setRelMenu.setVisible(false);
	}
	
	/**
	 * Show the menus corresponding to selected symbol
	 */
	public void showContextMenu()
	{
		setClassMenu.setVisible(true);
		setRelMenu.setVisible(true);
	}

}
