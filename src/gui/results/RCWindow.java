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

import gui.Histogram;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import main.Parameters;

import core.me.Context;
import core.me.Expression;

@SuppressWarnings("serial")
public class RCWindow extends JFrame {

	private int dim = 0;
	private Expression expr_;
	private int display = 0;
	private JPanel jf = null;
	
	public static final int DISPLAY_RC = 0;
	public static final int DISPLAY_FR = 1;
	public static final int DISPLAY_BL = 2;
	public static final int DISPLAY_YN = 3;
	
	public RCWindow(Expression e)
	{
		super("Relationship Matrix");
		expr_ = e;
		dim = e.nbSymbols();
		this.build();
		this.pack();
		this.setVisible(true);
	}

	private void build() {
		
		JMenuBar jm = new JMenuBar();
		
		JMenu displayMenu = new JMenu("Display");
		
		JMenuItem jmiRC = new JMenuItem("Relationship Classifier");
		JMenuItem jmiFR = new JMenuItem("Fuzzy Region Classifier");
		JMenuItem jmiBL = new JMenuItem("Baseline Classifier");
		JMenuItem jmiYN = new JMenuItem("Possible Child Classifier");
		
		jmiRC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {display = DISPLAY_RC; updatePanel(); }
		});
		
		jmiFR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {display = DISPLAY_FR; updatePanel(); }
		});
		
		jmiBL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {display = DISPLAY_BL; updatePanel(); }
		});
		
		jmiYN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {display = DISPLAY_YN; updatePanel(); }
		});
		
		displayMenu.add(jmiRC);
		displayMenu.add(jmiFR);
		displayMenu.add(jmiBL);
		displayMenu.add(jmiYN);
		
		jm.add(displayMenu);
		
		this.setJMenuBar(jm);
		
		this.updatePanel();
		
	}
	
	public Histogram getHist(Context p, Context c, int displayType)
	{
		int size = 400/dim;
		double[] arr;
		
		switch (displayType)
		{
		case DISPLAY_RC:
			try {
				arr = c.getVirtualRelationship(p);
			} catch (Exception e) {
				System.out.println("Erreur dans la classification virtuelle!");
				arr = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
			}
			break;
		case DISPLAY_FR:
			arr = p.getFuzzyRegions().memberships(c);
			break;
		case DISPLAY_BL:
			arr = c.baselineScore(p);
			break;
		case DISPLAY_YN:
			try {
				arr = c.possibleChildOf(p);
			} catch (Exception e) {
				arr = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
				e.printStackTrace();
			}
			break;
		default:
			arr = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
			break;	
		}
		
		return new Histogram(arr,size,size);
	}
	
	private void updatePanel()
	{
		if (jf!=null) this.remove(jf);
		jf = new JPanel();
		
		GridLayout gl = new GridLayout(dim, dim); 
		gl.setHgap(15);
		gl.setVgap(15);
		
		jf.setLayout(gl);
		Vector<Context> vc = expr_.getSymbols();
		
		jf.add(new JPanel());
		
		for (int j=1; j<dim; j++)
		{
			JLabel jl = new JLabel( ""+vc.get(j).getSymbolId() );
			jl.setHorizontalAlignment( SwingConstants.CENTER );
			jf.add(jl);
		}
		
		Context p,c;
		
		for (int i=0; i<dim-1; i++)
		{
			p = vc.get(i);
			JLabel jl = new JLabel( ""+p.getSymbolId() );
			jl.setHorizontalAlignment( SwingConstants.CENTER );
			jf.add(jl);
			for (int j=1; j<dim; j++)
			{
				c = vc.get(j);
				JPanel cell = (j<=i) ? new JPanel() : this.getHist(p,c, display); 
				jf.add(cell);
			}
		}
		
		jf.setPreferredSize(new Dimension(400+15*(dim+4), 400+15*(dim+4)));
		this.add(jf);
		this.validate();
		this.repaint();
	}
	
	
	
}
