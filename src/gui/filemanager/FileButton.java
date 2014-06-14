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


package gui.filemanager;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class FileButton extends JButton implements MouseListener {

	private String  filename = "";
	private int     type;
	
	public static final int TYPE_OTH = -1;
	public static final int TYPE_DIR = 0;
	public static final int TYPE_XML = 1;
	public static final int TYPE_TEX = 2;
	public static final int TYPE_MOD = 3;
	public static final int TYPE_IMG = 4;
	
	public FileButton(String name)
	{
		super(name);
		
		filename = name;
		String[] parts = name.split("\\.");
		
		if (parts.length<=1)
			type = TYPE_DIR;
		else
		{
			String ext = parts[parts.length-1];
			if (ext.equalsIgnoreCase("xml")) type = TYPE_XML;
			else if (ext.equalsIgnoreCase("tex")) type = TYPE_TEX;
			else if (ext.equalsIgnoreCase("model")) type = TYPE_MOD;
			else if (ext.equalsIgnoreCase("bmp") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg"))
				type = TYPE_IMG;
			else type = TYPE_OTH;
		}
			
		switch (type)
		{
		case TYPE_DIR:
			setBackground(Color.orange);
			break;
		case TYPE_OTH:
			setBackground(Color.LIGHT_GRAY);
			break;
		default:
			setBackground(Color.cyan);	
		}
		
		this.addMouseListener(this);
	}
	
	public int getType() { return type; }
	public String getName() { return filename; }

	@Override
	public void mouseClicked(MouseEvent e) {
		if (type==TYPE_DIR)
		{
			String ws = FileManager.get().getWorkspace();
			FileManager.get().changeWorkspace(ws+filename+"\\");
			FileManager.get().update();
		}
		else if (type>0)
		{
			FileManager.get().displayMenu(this.filename, this.getType());
		}
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
	
}
