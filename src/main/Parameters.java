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


package main;

/**
 * This class represents the important parameters of the system.
 * @author Théodore Bluche
 *  main
 */
public class Parameters {
	
	/*
	 * THRESHOLDS
	 */
	
	public static double T_BASELINE_SCORE = 0.4;
	public static double T_BASELINE_SUP_SCORE = 0.5;
	public static double T_BASELINE_SUB_SCORE = 0.5;
	public static double T_BASELINE_UPP_SCORE = 0.26;
	public static double T_BASELINE_UND_SCORE = 0.26;
	
	/*
	 * COEFFS
	 */
	public static final int COEFF_FR  = 5;
	public static final int COEFF_BL  = 5;
	public static final int COEFF_RCI = 1;
	public static final int COEFF_RCC = 1;
	public static final int COEFF_YNI = 1;
	public static final int COEFF_YNC = 2;
	
	
	/*
	 * MISC
	 */

	public static double SCORE_WEIGHT_BASELINE = .16;
	public static int    ITERATIONS            = 5;
	
	public static final int NB_OF_SYMBOL_CLASSES = 4;
	public static final int NB_OF_RELATIONSHIP_CLASSES = 5;

	
	
}
