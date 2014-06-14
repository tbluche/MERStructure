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


//package main;
//
//import build.Classifiers;
//import build.Symbol;
//
//public class main {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//
//		// Init the classifiers object
//		Classifiers CC = Classifiers.getInst(false);
//		
//		// Get the saved models
//		try {
//			CC.readSC("SC.model");
//			CC.readRC("RC.model");
//		} catch (Exception e) {
//			System.out.println("Error reading the models: ");
//			e.printStackTrace();
//		}
//		
//		
//		// Test 1
//		
//		System.out.println("---------\n TEST #1 \n---------");
//		
//		// Create symbols
//		int[] bbp = {28, 35, 15, 31};
//		Symbol sp = new Symbol(bbp);
////		System.out.println("p: main properties:: \n"+sp.getProperties().toString());
//		int[] bba = {37, 43, 9, 13};
//		Symbol sa = new Symbol(bba);
////		System.out.println("a: main properties:: \n"+sa.getProperties().toString());
//		
//		try {
//			int spclass = CC.getSymbolClass(sp);
//			int saclass = CC.getSymbolClass(sa);
//			System.out.println("Classes: p: "+spclass+", a: "+saclass);
//			sp.getProperties().setClass(spclass);
//			sa.getProperties().setClass(saclass);
//			
//			sa.remParent()
//			  .setParent(sp);
////			System.out.println("a( <p) :\n"+sa.getProperties().toString());
//			//CC.viewRC();
//			int spsarel = CC.getRelationship(sa);
//			System.out.println("Relationship p>a: "+spsarel);
//			
//			sp.setChild(sa, spsarel);
//			
//			spclass = CC.getSymbolClass(sp);
//			saclass = CC.getSymbolClass(sa);
//			System.out.println("Classes: p: "+spclass+", a: "+saclass);
//			sp.getProperties().setClass(spclass);
//			sa.getProperties().setClass(saclass);
//			
//			sa.remParent()
//			  .setParent(sp);
//			spsarel = CC.getRelationship(sa);
//			System.out.println("Relationship p>a: "+spsarel);
//			
//			
//		} catch (Exception e) {
//			System.out.println("The symbol recognition yielded the following error: ");
//			e.printStackTrace();
//		}
//		
//		// Test 2
//		
//		System.out.println("---------\n TEST #2 \n---------");
//		
//		int[] bb1 = {20, 36, 23, 44};
//		int[] bb2 = {34, 39, 15, 22};
//		int[] bb3 = {42, 46, 23, 26};
//		
//		Symbol s1 = new Symbol(bb1);
//		Symbol s2 = new Symbol(bb2);
//		Symbol s3 = new Symbol(bb3);
//		
//		s2.setParent(s1);
//		s3.setParent(s2);
//		
//		try{
//			int c1, c2, c3;
//			int r1, r2;
//			
//			c1 = CC.getSymbolClass(s1);
//			c2 = CC.getSymbolClass(s2);
//			c3 = CC.getSymbolClass(s3);
//			
//			s1.getProperties().setClass(c1);
//			s2.getProperties().setClass(c2);
//			s3.getProperties().setClass(c3);
//			
//			System.out.println("Classes: "+c1+", "+c2+", "+c3);
//			
//			s2.setParent(s1);
//			s3.setParent(s2);
//			
//			r1 = CC.getRelationship(s2);
//			r2 = CC.getRelationship(s3);
//			
//			System.out.println("Relationships: "+r1+", "+r2);
//			
//			s1.setChild(s2, r1);
//			s2.setChild(s3, r2);
//			
//			c1 = CC.getSymbolClass(s1);
//			c2 = CC.getSymbolClass(s2);
//			c3 = CC.getSymbolClass(s3);
//			
//			s1.getProperties().setClass(c1);
//			s2.getProperties().setClass(c2);
//			s3.getProperties().setClass(c3);
//			
//			System.out.println("Classes: "+c1+", "+c2+", "+c3);
//		
//		} catch (Exception e) {
//			System.out.println("The symbol recognition yielded the following error: ");
//			e.printStackTrace();
//		}
//
//	}
//
//}
