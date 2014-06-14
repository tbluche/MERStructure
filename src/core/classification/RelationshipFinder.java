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


package core.classification;

import gui.MainWindow;

import java.util.Stack;
import java.util.Vector;

import main.Parameters;

import tools.ArrayTools;

import core.me.Context;
import core.me.Expression;
import core.me.Relationship;

public class RelationshipFinder {
	
	public static double THRESHOLD = 0.5;
	
	private Expression expr_;
	
	public RelationshipFinder( Expression e )
	{
		expr_ = e;
	}
	
	/**
	 * Find the parent of each symbol, and the corrseponding relationship
	 */
	@SuppressWarnings("unchecked")
//	public void parentAndRelate()
//	{
//		// Initialize the stack of seen symbols
//		Stack<Context> seen = new Stack<Context>();
//		
//		// Retrieve the list of symbols
//		Vector<Context> vc = expr_.getSymbols();
//		// Get the first symbol...
//		Context considered = expr_.findLeftMost();
//		// ... which has no parent: we add it to the stack of seen symbols
//		seen.add(considered);
//		
//		// Define...
//		// The working stack
//		Stack<Context> seen_clone;
//		// The candidate for parenting, and the best parent found so far
//		Context possibleParent, bestParent;
//		// Has a parent been found
//		boolean parented;
//		// The variables for the confidence of a relationship
//		double confidence, max_confidence;
//		// The considered relationship, and the best found so far
//		int relation, bestRelation;
//		// The confidence values for relationship (RC)
//		double[] rc;
//		// The confidence values for relationship (fuzzy regions)
//		double[] fuzzy;
//		// The baseline confidence
//		double[] baseline;
//		// The YNC confidence
//		double[] possibleChild;
//		// The array containing the relationship sorted by confidence
//		int[] sortedRels;
//		
//		// We browse the symbols in the list
//		for (int i=1; i<vc.size(); i++)
//		{
//			considered = vc.get(i);
//			// Initializations
//			parented = false;
//			max_confidence = 0.;
//			bestRelation = 0;
//			bestParent = considered;
//			// Clone the stack to work with it (e.g. pop symbols)
//			seen_clone = (Stack<Context>) seen.clone();
//			
//			// Keep searching while no parent has been found, and the list of possible parent is not empty 
//			while (!parented && !seen_clone.empty())
//			{
//				// Remove symbol's parent to avoid confusion
//				considered.remParent();
//				// Consider the next possible parent
//				possibleParent = seen_clone.pop();
//				
//				// Test for this parent
//				try {
//					considered.setParent(possibleParent);
//					// Retrieve the confidence values
//					rc = Classifiers.get().getRelationship(considered);
//					fuzzy = possibleParent.getFuzzyRegions().memberships(considered);
//					baseline = considered.baselineScore(possibleParent);
//					possibleChild = considered.possibleChildOf(possibleParent);
//					
//					// Loop on relationships
//					// Sort the RC relationships
//					sortedRels = ArrayTools.indexSort(rc);
//					int r=0;
//					// Keep looking for a possible relationship while it is not parented,
//					//   in order of the most likely relationships
//					while (!parented && r<Parameters.NB_OF_RELATIONSHIP_CLASSES)
//					{
//						relation = sortedRels[r];		// Get the considered relationship
//						confidence = rc[relation];		// Get the confidence for it
//						
//						if (!possibleParent.hasChild(relation))
//						{
//							// Different processing if it is inline or not
//							if (relation == Relationship.INLINE)
//							{
//								confidence = Math.sqrt(confidence);
//								confidence *= baseline[Utils.maxIndex(baseline)];
//	//							confidence /= 2;
//								confidence *= possibleChild[0];
//							}
//							else
//							{
//								confidence += fuzzy[relation];
//								confidence /= 2;
//								confidence *= possibleChild[1];
//							}
//							
//							// If the confidence is high enough, we can do the parenting for this relationship
//							if (confidence > THRESHOLD)
//							{
//								parented = true;
//								considered.setRelationship( new Relationship(possibleParent, considered, relation) );
//								if (r>0) {/* Reinforce this symbol class */}
//							}
//							// If not, it can be the best parent amongst all symbols
//							else if (confidence > max_confidence) 
//							{
//								max_confidence = confidence;
//								bestParent = possibleParent;
//								bestRelation = relation;
//							}
//						}
//						
//						r++;
//						
//					} // go to next possible relationship
//					
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			} // go to next possible parent
//			
//			// If it is still not parented, we parent it to the best candidate found
//			if (!parented)
//				considered.setRelationship( new Relationship(bestParent, considered, bestRelation) );
//			
//			// The symbol is now seen, we add it to the stack
//			seen.push(considered);
//		} // go to next symbol
//	}
	
	public void parentAndRelate()
	{
		// Initialize the stack of seen symbols
		Stack<BaselineStructure> lastBL = new Stack<BaselineStructure>();
		
		// Retrieve the list of symbols
		Vector<Context> vc = expr_.getSymbols();
		// Get the first symbol...
		Context considered = expr_.findLeftMost();
		// ... which has no parent: we add it to the stack of seen symbols
		BaselineStructure dominantBLS = new BaselineStructure(considered);
		lastBL.add(dominantBLS);
		
		// Define...
		// The working stack
		Stack<BaselineStructure> lastBL_clone;
		// The candidate baseline
		BaselineStructure consideredBL;
		// The list of candidate baselines
		Vector<BaselineStructure> candidatesBL;
		// The candidate for parenting, and the best parent found so far
		Context possibleParent;
		BaselineStructure bestParent;
		// Has a parent been found
		boolean parented;
		// The variables for the confidence of a relationship
		double confidence, max_confidence;
		// The considered relationship, and the best found so far
		int relation, bestRelation;
		// The confidence values for relationship (RC)
		double[] rc;
		// The confidence values for relationship (fuzzy regions)
		double[] fuzzy;
		// The baseline confidence
		double[] baseline;
		// The YNC confidence
		double[] possibleChild;
		// The array containing the relationship sorted by confidence
		int[] sortedRels;
		
		// We browse the symbols in the list
		for (int i=1; i<vc.size(); i++)
		{
			// Retrieve the next symbol
			considered = vc.get(i);
			// Not yet parented
			parented = false;
			// Default best parent
			bestParent = dominantBLS;
			// Default best relation
			bestRelation = Relationship.INLINE;
			
			max_confidence = 0.;
				
			// ----------------
			// BEGIN INLINE TEST
			
			// Start with the dominant baseline
			candidatesBL = new Vector<BaselineStructure>();
			candidatesBL.add(dominantBLS);
			
			while (!parented && candidatesBL.size()>0)
			{
				
				consideredBL = candidatesBL.remove(0);
				possibleParent = consideredBL.getDominantSymbol();
				relation = Relationship.INLINE;
				confidence = 0.;
				
				// Retrieve the confidence values
				try {
					rc = considered.getVirtualRelationship(possibleParent);
					fuzzy = possibleParent.getFuzzyRegions().memberships(considered);
					baseline = considered.baselineScore(possibleParent);
					possibleChild = considered.possibleChildOf(possibleParent);
				} catch(Exception e) {
					rc = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
					fuzzy = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
					baseline = new double[Parameters.NB_OF_SYMBOL_CLASSES];
					possibleChild = new double[2];
					MainWindow.inst.addResultText("Error in the classification");
				}
				
				confidence = rc[Relationship.INLINE]*Parameters.COEFF_RCI;
//				confidence += baseline[Utils.maxIndex(baseline)]*Parameters.COEFF_BL;
				confidence += baseline[considered.getSymbolClass()-1]*Parameters.COEFF_BL;
				confidence += possibleChild[0]*Parameters.COEFF_YNI;
				confidence /= (Parameters.COEFF_BL+Parameters.COEFF_RCI+Parameters.COEFF_YNI);
				
				// If the confidence is high enough, we can do the parenting for this relationship
				if (confidence > THRESHOLD)
				{
					parented = true;
					considered.setRelationship( new Relationship(possibleParent, considered, relation) );
					consideredBL.updateStructure(considered);
					lastBL.push(consideredBL);
				}
				// If not, it can be the best parent amongst all symbols
				else 
				{
					if (confidence > max_confidence) 
					{
						max_confidence = confidence;
						bestParent = consideredBL;
						bestRelation = relation;
					}
					Vector<BaselineStructure> nested = consideredBL.getNestedBaselines();
					if (nested!=null) candidatesBL.addAll(nested);
				}
			} 
			// END OF THE INLINE TEST
			// ----------------------
			
			if (!parented)
			{
				// ----------------
				// BEGIN CHILD TEST
				
				lastBL_clone = (Stack<BaselineStructure>) lastBL.clone();
				
				do
				{
					consideredBL = lastBL_clone.pop();
					possibleParent = consideredBL.getDominantSymbol();
					
					// Retrieve the confidence values
					try {
						rc = considered.getVirtualRelationship(possibleParent);
						fuzzy = possibleParent.getFuzzyRegions().memberships(considered);
						baseline = considered.baselineScore(possibleParent);
						possibleChild = considered.possibleChildOf(possibleParent);
					} catch(Exception e) {
						rc = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
						fuzzy = new double[Parameters.NB_OF_RELATIONSHIP_CLASSES];
						baseline = new double[Parameters.NB_OF_SYMBOL_CLASSES];
						possibleChild = new double[2];
						MainWindow.inst.addResultText("Error in the classification - 2");
					}
					
					// Loop on relationships
					// Sort the RC relationships
					sortedRels = ArrayTools.indexSort(rc);
					int r=0;
					// Keep looking for a possible relationship while it is not parented,
					//   in order of the most likely relationships
					while (!parented && r<Parameters.NB_OF_RELATIONSHIP_CLASSES)
					{
						relation = sortedRels[r];		// Get the considered relationship
						confidence = Parameters.COEFF_RCC*rc[relation];		// Get the confidence for it
						
						if (!possibleParent.hasChild(relation) && relation!=Relationship.INLINE)
						{
					
							confidence += Parameters.COEFF_FR*fuzzy[relation];
							confidence += Parameters.COEFF_YNC*possibleChild[1];
							confidence /= (Parameters.COEFF_BL+Parameters.COEFF_RCC+Parameters.COEFF_YNC);
							
							// If the confidence is high enough, we can do the parenting for this relationship
							if (confidence > THRESHOLD)
							{
								parented = true;
								considered.setRelationship( new Relationship(possibleParent, considered, relation) );
								BaselineStructure newNode = new BaselineStructure(considered); 
								consideredBL.addChild(newNode);
								lastBL.push(newNode);
							}
							// If not, it can be the best parent amongst all symbols
							else 
							{
								if (confidence > max_confidence) 
								{
									max_confidence = confidence;
									bestParent = consideredBL;
									bestRelation = relation;
								}
								
							}
						} // end if no parent
						
						r++;
					} // end loop on relationships
							
				} while(!parented && !consideredBL.equals(dominantBLS));
				
				// END OF CHILD TEST
				// -----------------
			}
			
			
			if (!parented)
			{
				possibleParent = bestParent.getDominantSymbol();
				BaselineStructure newNode = new BaselineStructure(considered); 
				if (!possibleParent.hasChild(bestRelation)) 
				{
					considered.setRelationship( new Relationship(possibleParent, considered, bestRelation) );
					if (bestRelation==Relationship.INLINE)
						bestParent.updateStructure(considered);
					else
						bestParent.addChild(newNode);
				}
				lastBL.push(newNode);
			}
		
			
		// next symbol
		} 
		// no more symbols
		
		
	}

}
