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

import java.io.FileNotFoundException;

import core.classification.Classifiers;
import core.imageproc.ImageProc;

import tools.LatexParser;
import gui.MainWindow;
import gui.MultiImagePanel;
import gui.filemanager.FileManager;

public class TestsImProc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileManager fm = FileManager.get();
		fm.changeWorkspace("C:\\Documents and Settings\\Administrateur\\workspace\\oxproject\\");
		fm.update();
		
//		Classifiers cc = 
			Classifiers.getInst(false);
		try {
//			cc.trainSC();
//			cc.saveSC("SCA.model","SCB.model","SCC1.model","SCC2.model","SCC3.model");
//			cc.trainRC(); 
//			cc.saveRC("RC.model");
//			cc.trainYNC();
//			cc.saveYNC("YNC.model");
		} catch (Exception e) {
			System.out.println("The following error was encountered while trying to write serialized models: ");
			e.printStackTrace();
		}
	
		
		ImageProc im = new ImageProc("C:\\Documents and Settings\\Administrateur\\workspace\\oxproject\\sumessai.bmp");
		new MainWindow(im);
		
		try {
			MainWindow.inst.clearExpressions();
			LatexParser latpars = new LatexParser(FileManager.get().getWorkspace()+"small_essai.tex");
			MultiImagePanel mip = latpars.latexToMip();
			if (mip.expressions().size()>0) MainWindow.inst.setMip( mip );
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

	}

}
