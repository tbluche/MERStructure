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

import gui.filemanager.FileManager;
import tools.DatasetBuilder;

public class tests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileManager fm = FileManager.get();
		fm.changeWorkspace("C:\\Documents and Settings\\Administrateur\\workspace\\oxproject\\");
		fm.update();
		DatasetBuilder dsb = new DatasetBuilder();
		dsb.buildELI();
		dsb.buildELIVR();
		dsb.buildVR();
		dsb.createCSV("test.csv");
		dsb.createCSV2("YNCdata.csv");
		
		
		
	}

}
