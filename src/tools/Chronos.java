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


package tools;

import java.util.Date;

/**
 * This class is used to measure execution times.
 * @author Théodore Bluche
 *  tools
 */
public class Chronos {

	// Constants
	public static final long HOU = 3600*1000;
	public static final long MIN = 60*1000;
	public static final long SEC = 1000;

	public Date start;
	
	public Chronos() { start = new Date(); }
	
	/**
	 * Starts the chronometer.
	 * @return time of the start
	 */
	public long start() {
		start = new Date();
		return start.getTime();
	}
	
	/**
	 * Stops the chronometer.
	 * @return the number of milliseconds since start.
	 */
	public long stop(){	return (new Date()).getTime() - start.getTime(); }
}
