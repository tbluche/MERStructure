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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.classification.Statistics;
import core.imageproc.ImageProc;
import core.me.Expression;
import exceptions.InvalidInterpretation;

import tools.XMLCreator;


import gui.results.ExpressionPanel;
import gui.results.PlotPanel;
import gui.results.RCWindow;
import gui.results.ResultView;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements MouseListener, ActionListener{

	private   ImageProc  image_;
	protected ImagePanel panel_;
	private   Expression expr_;
	private   PropPanel  props;
	private   JPanel     optPanel;
	private   JPanel     classPanel;
	
	private   ResultView rv = null;
	
	private MultiImagePanel mip_;
	
	private JButton	   classifySymbols;
	private JButton    classifyRelationships;
	private JButton	   autoParent;
	private JButton    selectParent;
	private JButton    remParentBtn;
	private JButton    openBtn;
	private JButton    RelMatBtn;
	private JButton    getMLatex;
	private JButton    displayMEProp;
	private JButton    processExpr;
	private JButton    nextExpr;
	private JButton    prevExpr;
	
	private TopMenu	   tm;
	
	
	protected Vector<JFrame> dependencies;
	
	public static MainWindow inst = null;
	
	
	public MainWindow(ImageProc I)
	{
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mip_ = new MultiImagePanel();
		image_ = I;
		expr_  = new Expression(image_.segment());
		expr_.setDimension(new Dimension(image_.largeur(), image_.hauteur()));
		panel_ = new ImagePanel(I.getBI(), expr_);
		mip_.add(panel_);
		
		inst = this;
		
		this.build();
		
		this.addMouseListener(this);
		
		this.dependencies = new Vector<JFrame>();
	}
	
	public void setMip(MultiImagePanel mip)
	{
		this.mip_ = mip;
		this.setImagePanel(this.mip_.getCurrent());
	}
	
	public void setImage(ImageProc im)
	{
		image_ = im;
		expr_ = new Expression(image_.segment());
		expr_.setDimension(new Dimension(image_.largeur(), image_.hauteur()));
		this.remove(panel_);
		panel_ = new ImagePanel(im.getBI(), expr_);
		buildImagePanel();
		
		mip_.add(panel_);
	}
	
	public void setExpression(Expression e)
	{
		image_ = null;
		expr_ = e;
		this.remove(panel_);
		panel_ = new ImagePanel(expr_);
		buildImagePanel();
		
		mip_.add(panel_);
	}
	
	public void setImagePanel(ImagePanel ip)
	{
		image_ = null;
		expr_ = ip.getExpression();
		this.remove(panel_);
		panel_ = ip;
		buildImagePanel();
	}
	
	public void buildImagePanel()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridwidth=2;
		gbc.gridx = 0;
		gbc.gridy = 0;

		panel_.setBorder(new TitledBorder("Segmented Image: "+mip_.getIter()+"/"+mip_.nbIp()));
		panel_.validate();
		panel_.repaint();
		this.add(panel_, gbc);
		this.validate();
		this.repaint();
		this.pack();
	}
	
	public void prevExpression()
	{
		this.setImagePanel(mip_.prev());
	}
	
	public void nextExpression()
	{
		this.setImagePanel(mip_.next());
	}
	
	public void clearExpressions()
	{
		this.expr_ = null;
		this.mip_  = new MultiImagePanel();
	}
	
	
	private void build()
	{
		
		tm = new TopMenu();
		
		this.setJMenuBar(tm);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		buildImagePanel();
		
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.gridy = 1;

		classifySymbols 		= new JButton("Classify all symbols");
		autoParent 				= new JButton("L-to-R Parenting");
		selectParent 			= new JButton("Select Parent");
		remParentBtn 			= new JButton("Remove Parent");
		classifyRelationships 	= new JButton("Classify all relationships");
		openBtn 				= new JButton("Image");
		RelMatBtn 				= new JButton("Relationships table");
		getMLatex				= new JButton("Get meta-Latex");
		displayMEProp			= new JButton("ME Properties");
		processExpr				= new JButton("Process input");
		prevExpr				= new JButton("<< Prev.");
		nextExpr				= new JButton("Next >>");
		
		classifySymbols.addMouseListener(this);
		classifyRelationships.addMouseListener(this);
		autoParent.addMouseListener(this);
		selectParent.addMouseListener(this);
		remParentBtn.addMouseListener(this);
		openBtn.addMouseListener(this);
		RelMatBtn.addMouseListener(this);
		getMLatex.addMouseListener(this);
		displayMEProp.addMouseListener(this);
		processExpr.addMouseListener(this);
		prevExpr.addMouseListener(this);
		nextExpr.addMouseListener(this);
		
		optPanel = new JPanel(new GridLayout(5,2));
		optPanel.add(prevExpr);
		optPanel.add(nextExpr);
		optPanel.add(classifySymbols);
		optPanel.add(classifyRelationships);
		optPanel.add(autoParent);
		optPanel.add(selectParent);
		optPanel.add(remParentBtn);
		optPanel.add(RelMatBtn);
		optPanel.add(displayMEProp);
		optPanel.add(processExpr);
		
		this.add(optPanel, gbc);
		
		gbc.gridwidth = 1;
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		
		classPanel = new ClassificationPanel(null);
		classPanel.setBorder(new TitledBorder("Visualizations"));
		
		this.add(classPanel,gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		
		props = new PropPanel(null);
		props.setBorder(new TitledBorder("Symbol Properties"));
		this.add(props, gbc);
		
		this.setBounds(300, 10, 0, 0);
		this.pack();
		setVisible(true);
		setTitle("MLMER - GUI");
				
	}
	
	public Dimension imLocation()
	{
		return new Dimension(image_.largeur(), image_.hauteur());
	}
	
	public void updateProps()
	{
		this.remove(props);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
//		gbc.gridheight = 3;
		
		props = new PropPanel(((ImagePanel)panel_).getSelected());
		props.setBorder(new TitledBorder("Symbol Properties"));
		this.add(props, gbc);
		this.validate();
		this.repaint();
		this.pack();
	}
	
	public void updateTopMenu()
	{
		tm.setSelected(((ImagePanel)panel_).getSelected());
	}
	
	public void updateClassPanel()
	{
		this.remove(classPanel);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		
		classPanel = new ClassificationPanel(((ImagePanel)panel_).getSelected());
		classPanel.setBorder(new TitledBorder("Result of the classifications"));
		this.add(classPanel, gbc);
		this.validate();
		this.repaint();
		this.pack();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object source = e.getSource();
		if (source==classifySymbols)
			expr_.classifySymbols();
		else if (source==classifyRelationships)
			expr_.classifyRelationships();
		else if (source==autoParent)
			expr_.autoParenting();
		else if (source==selectParent)
			panel_.waitParent();
		else if (source==remParentBtn)
			panel_.getSelected().remParent();
		else if (source==openBtn)
		{
			String file = chooseFile("F:\\Oxford\\Project\\New Dataset\\ME\\", "bmp");
			ImageProc im = new ImageProc(file);
			this.setImage(im);
		}
		else if (source==RelMatBtn)
		{
//			RelationshipMatrix rm = new RelationshipMatrix(this.expr_);
//			try {
//				Chronos chr = new Chronos();
//				chr.start();
//				rm.build();
//				rm.buildRelationships();
//				System.out.println("Relationships classified in "+chr.stop()+"ms.");
////				this.updateVisualPanel(rm.visualize(), "Relationships Visualization");
//				JFrame njf = new JFrame("Relationships Visualizer");
//				njf.add(rm.visualize());
//				njf.setVisible(true);
//				njf.pack();
//				dependencies.add(njf);
//			} catch (Exception e1) {
//				System.out.println("An error occured while trying to build the relationships matrix: ");
//				e1.printStackTrace();
//			}
			new RCWindow(expr_);
		}
		else if (source==getMLatex) MainWindow.inst.addResultText(expr_.buildMetaLatex());
		else if (source==displayMEProp)
		{
			JFrame njf = new JFrame("Expression Properties");
			njf.add(new ExpressionPanel(expr_));
			double[] xarr = {1., 2., 3., 4., 5., 8., 3., 5., 0.};
			double[] yarr = {5., 3., 4., 1., 2., 2., 6., 10., 2.3};
			njf.add(new PlotPanel(xarr, yarr, "x_label", "y_label"));
			njf.pack();
			njf.setVisible(true);
		}
//		else if (source==processExpr)
//		{
//			expr_.process();
//			this.addResultText(expr_.printInterpretations());
//		}
		else if (source==processExpr)
		{
			for (Expression expr: mip_.expressions()) expr.process();
			this.addResultText(expr_.printInterpretations());
		}
		else if (source==prevExpr)
			this.prevExpression();
		else if (source==nextExpr)
			this.nextExpression();
		
			
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static String chooseFile(String chemin, String filtre){
		JFileChooser choix = new JFileChooser(new File(chemin));
		choix.setPreferredSize(new Dimension(1000, 1000));
		FileNameExtensionFilter f =  new FileNameExtensionFilter(null,filtre);
		choix.setFileFilter(f);
		if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) return choix.getSelectedFile().getAbsolutePath();
		return "";
	}
	
	public static void captureJPanel( JPanel jp, String filename ) {
		BufferedImage bufferedImage;
		bufferedImage = new BufferedImage( jp.getWidth(), jp.getHeight(), BufferedImage.TYPE_INT_RGB );
		    Graphics g = bufferedImage.createGraphics();
		    jp.paint( g );
		   
		    try {
		    ImageIO.write( bufferedImage, "png", new File(filename) ); }
		   
		    catch (Exception e) {
		     System.out.println("An error occued while saving the panel: " );
		     e.printStackTrace();
		    }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	public Expression getExpression() {
		return expr_;
	}
	
	public void exportAllInterpretations(String file) {
		XMLCreator.createXML(mip_.expressions(), file);
		
	}

	public void exportAllList(String file) {
		XMLCreator.listsOfSymbolXML(mip_.expressions(), file);
	}

	public void exportHTML(String file) 
	{
		XMLCreator.htmlSave(mip_.expressions(), file);
	}
	
	public Vector<Statistics> compareAll(Vector<Expression> ve)
	{
		Vector<Statistics> result = new Vector<Statistics>();
		Vector<Expression> mipe = mip_.expressions();
		if (mipe.size()!=ve.size())
			MainWindow.inst.addResultText("The number of equations in the workspace and in the file must be the same");
		else
			for (int i=0; i<ve.size(); i++)
				result.add(mipe.get(i).compare(ve.get(i)));
		return result;
	}
	
	public void assignAllIds(Vector<Vector<Integer>> ids) throws InvalidInterpretation
	{
		Vector<Expression> mipe = mip_.expressions();
		if (mipe.size()!=ids.size())
			MainWindow.inst.addResultText("The number of equations in the workspace and in the file must be the same");
		else
			for (int i=0; i<ids.size(); i++)
					mipe.get(i).assignIds(ids.get(i));
	}
	
	public void addResultText(String str)
	{
		if (rv==null) rv=new ResultView();
		rv.setVisible(true);
		rv.append(str);
	}
	
	public void updateImagePanel()
	{
		panel_.repaint();
	}
	

}
