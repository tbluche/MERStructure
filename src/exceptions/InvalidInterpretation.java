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


package exceptions;

@SuppressWarnings("serial")
public class InvalidInterpretation extends Exception {
	
	private String cause = "N/A";

	public InvalidInterpretation() {
		super();
	}

	public InvalidInterpretation(String arg0) {
		super(arg0);
		cause = arg0;
	}

	public InvalidInterpretation(Throwable arg0) {
		super(arg0);
	}

	public InvalidInterpretation(String arg0, Throwable arg1) {
		super(arg0, arg1);
		cause = arg0;
	}
	
	public String toString()
	{
		return "This expression interpretation is invalid. Cause: "+cause;
	}

}
