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


package core.me;

import tools.ArrayTools;
import weka.core.Utils;
import core.classification.Classifiers;
import main.Parameters;

/**
 * This class represents a relationship between two symbols
 * @author Théodore Bluche
 *  core.me
 */
public class Relationship {
	
	private int		  relation  = -1;
	private double[]  rdist	    = null;
	private double[]  fuzzy		= null;
	
	private Context   symbol	= null;
	private Context	  parent	= null;
	
	public static final int INLINE 		= 0;
	public static final int SUPERSCRIPT = 1;
	public static final int SUBSCRIPT	= 2;
	public static final int UPPER		= 3;
	public static final int UNDER		= 4;
	
	public static final String[] TEXTREL = {"inline", "superscript", "subscript", "upper", "under"}; 

	/**
	 * Create a new empty relationship
	 * @param child symbol of the relationship
	 * @param aParent parent of this symbol
	 */
	public Relationship(Context child, Context aParent)
	{
		rdist	  = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
		fuzzy	  = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
		symbol	  = child;
		parent    = aParent;
	}
	
	/**
	 * Create a relationship given the confidence values
	 * @param child symbol of the relationship
	 * @param aParent parent of this symbol
	 * @param rc RC confidences
	 * @param fuzz Fuzzy confidences
	 */
	public Relationship(Context child, Context aParent, double[] rc, double[] fuzz)
	{
		symbol	  = child;
		parent    = aParent;
		setDist(rc);
		setFuzz(fuzz);
	}
	
	/**
	 * Create the relationship. It clears the relationship for the parent, parent the child and child the parent.
	 * RC, BL and FR are calculated with the classifiers. 
	 * @param parent the parent
	 * @param child the child
	 * @param relationship the relationship
	 */
	public Relationship(Context aParent, Context child, int relationship)
	{
		child.clearForRelationship(relationship);
		child.setParent(aParent);
		aParent.addChild(child, relationship);
		
		symbol = child;
		parent = aParent;
		
		try {
			this.relation = relationship;
			this.setDist(Classifiers.get().getRelationship(child));
			this.setFuzz(parent.getFuzzyRegions().memberships(child));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Create the relationship. It clears the relationship for the parent, parent the child and child the parent.
	 * RC, BL and FR are st by <code>d</code>. 
	 * @param parent the parent
	 * @param child the child
	 * @param d the relationship distribution
	 */
	public Relationship(Context aParent, Context child, double[] d)
	{
		int relationship = Utils.maxIndex(d);
		
		symbol = child;
		parent = aParent;
		
		child.clearForRelationship(relationship);
		child.setParent(parent);
		parent.addChild(child, relationship);
		
		this.relation = relationship;
		this.setDist(d);
		this.setFuzz(d);
		
	}


	public void setDist(double[] d) { rdist = d; }
	public void setFuzz(double[] d) { fuzzy = d; }
	public void set(int rel) { relation = rel; }
	public int get() {return relation;}
	public double[] RCconfidences() {return rdist;}
	public double getRCConfidence(){ 
		if (relation>=0) return rdist[relation]; 
		else return 0.;
	}
	public double getRCConfidence(int aRelation) 
	{ 
		if (aRelation>=0) return rdist[aRelation]; 
		else return 0.;
	}
	public double[] fuzziness() {return fuzzy;}
	public double getFuzziness() {	return fuzzy[relation]; }
	public double getFuzziness(int aRelation) { return fuzzy[aRelation]; }
	
	public double[] computeMixedDist()
	{
		double[] dist = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
		if (parent != null)
			try {
				dist = ArrayTools.scale(fuzzy, Parameters.COEFF_FR);
				dist[0] = symbol.baselineScore(parent)[symbol.getSymbolClass()-1]*Parameters.COEFF_BL;
				rdist[0] *= rdist[0]*Parameters.COEFF_RCI;
				for (int i=1; i<rdist.length; i++) rdist[i] *= rdist[i]*Parameters.COEFF_RCC;
				dist = ArrayTools.add(dist, rdist);
				double[] yn = symbol.possibleChildOf(parent);
				dist[0] *= yn[0]*Parameters.COEFF_YNI;
				for (int i=1; i<dist.length; i++) dist[i] *= yn[1]*Parameters.COEFF_YNC;
				Utils.normalize(dist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return dist;
	}
	
	public double[] confidences() 
	{
		return computeMixedDist();
	}
	public double getConfidence()
	{ 
		if (relation>=0) return computeMixedDist()[relation]; 
		else return 0.;
	}
	public double getConfidence(int aRelation) 
	{ 
		if (aRelation>=0) return computeMixedDist()[aRelation]; 
		else return 0.;
	}
	
	public static double[] getVirtualConfidences(Context aParent, Context aChild)
	{
		double[] dist = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
		if (aParent!=null)	
			try {
				double[] aFuzzy = aParent.getFuzzyRegions().memberships(aChild);
				double[] aRdist = aChild.getVirtualRelationship(aParent);
				dist = ArrayTools.scale(aFuzzy, Parameters.COEFF_FR);
				dist[0] = aChild.baselineScore(aParent)[aChild.getSymbolClass()-1]*Parameters.COEFF_BL;
				aRdist[0] *= aRdist[0]*Parameters.COEFF_RCI;
				for (int i=1; i<aRdist.length; i++) aRdist[i] *= aRdist[i]*Parameters.COEFF_RCC;
				dist = ArrayTools.add(dist, aRdist);
				double[] yn = aChild.possibleChildOf(aParent);
				dist[0] *= yn[0]*Parameters.COEFF_YNI;
				for (int i=1; i<dist.length; i++) dist[i] *= yn[1]*Parameters.COEFF_YNC;
				Utils.normalize(dist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return dist;
	}
}
