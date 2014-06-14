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


package core.imageproc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import core.me.Context;


public class ImageProc {
	
	private BufferedImage bi   = (BufferedImage)null;
	private String        path = "";

	/**
	 * Constucteur de l'objet Image grâce à son emplacement
	 * @param chemin : emplacement de l'image
	 */
	public ImageProc(String chemin) {
		
		try {
			bi = ImageIO.read(new File(chemin));
		} 
		catch (FileNotFoundException e) { System.out.println("Le fichier n'a pas pu être trouvé");} 
		catch (IOException e) { System.out.println("Impossible de lire l'image");}
		
		path=chemin;
	}
	
	/**
	 * Constructeur d'un objet <code>Image</code> à partir d'un <code>BufferedImage</code>
	 * @param b : <code>BufferedImage</code> utilisé pour définir l'image
	 */
	public ImageProc(BufferedImage b){
		bi=b;
		path = "Image créée à partir d'un BufferedImage";
	}
	
	/**
	 * Renvoie le chemin vers l'image
	 * @return le chemin de l'image
	 */
	public String getPath(){
		return path;
	}
	
	/**
	 * Indique les quatre paramètres : alpha, rouge, vert, bleu d'un pixel
	 * @param x : abscisse du pixel
	 * @param y : ordonnée du pixel
	 * @return le tableau [alpha rouge vert bleu]
	 */
	public int[] getRGB(int x, int y) {
		int[] rgb = new int[4];
		int p = bi.getRGB(x, y);
		rgb[0] = (p & 0xff000000) >> 24;
		rgb[1] = (p & 0xff0000) >> 16;
		rgb[2] = (p & 0xff00) >> 8;
		rgb[3] = (p & 0xff);
		return rgb;
	}
	
	/**
	 * Indique le niveau de gris d'un pixel
	 * @param x : abscisse du pixel
	 * @param y : ordonnée du pixel
	 * @return le niveau de gris du pixel
	 */
	public int getGrey(int x, int y){
		// On récupère les valeurs R,G,B
		int[] p = this.getRGB(x, y);
		// On renvoie la moyenne
		return (p[1]+p[2]+p[3])/3;
	}
	
	/**
	 * @return la hauteur de l'image
	 */
	public int hauteur(){
		return bi.getHeight();
	}
	
	/**
	 * @return la largeur de l'image
	 */
	public int largeur(){
		return bi.getWidth();
	}
	
	public Vector<Context> segment ()
	{
		int max_labels = 100;
		
		int[][] labels = new int[this.largeur()][this.hauteur()];
		int[]   equiv  = new int[max_labels];
		boolean[] used = new boolean[max_labels];
		int[][] bbs    = new int[max_labels][4];
		
		Vector<Context> result = new Vector<Context>();
		
		for (int i=0; i<max_labels; i++)
		{
			equiv[i] = i;
			used[i]  = false;
		}
		
		int current_label = 0;
		int[] neigh;
		int nb_nei;
		int min_label;
		
		// First Pass
		for (int y=1; y<this.hauteur()-1; y++)
			for (int x=1; x<this.largeur()-1; x++)	
			{
				if (this.getGrey(x, y)<250)
				{
					neigh  = new int[4];
					nb_nei = 0;
					
					if (labels[x-1][y]>0)   neigh[nb_nei++] = labels[x-1][y];
					if (labels[x-1][y-1]>0) neigh[nb_nei++] = labels[x-1][y-1];
					if (labels[x][y-1]>0)   neigh[nb_nei++] = labels[x][y-1];
					if (labels[x+1][y-1]>0) neigh[nb_nei++] = labels[x+1][y-1];
					
					if (nb_nei==0) 
						labels[x][y] = ++current_label;
					else
					{
						min_label = max_labels;
						for (int i=0; i<nb_nei; i++)
							if (neigh[i]<min_label) min_label = equiv[neigh[i]];
						labels[x][y] = equiv[min_label];
						for (int i=0; i<nb_nei; i++)
							if (equiv[neigh[i]]>min_label) equiv[neigh[i]]=equiv[min_label];
					}
					
				}
			}
		
		// Table update
		for (int i=0; i<max_labels; i++)
			while (equiv[i] > equiv[equiv[i]]) equiv[i] = equiv[equiv[i]];
		
		// Second pass
		int actLabel;
		for (int x=1; x<this.largeur(); x++)
			for (int y=1; y<this.hauteur()-1; y++)
			{
				if (labels[x][y]>0)
				{
					actLabel = equiv[labels[x][y]]; 
					if (!used[actLabel])
					{
						bbs[actLabel][0] = x;
						bbs[actLabel][1] = x;
						bbs[actLabel][2] = y;
						bbs[actLabel][3] = y;
						used[actLabel] = true;
					}
					else 
					{
						if (bbs[actLabel][0] > x) bbs[actLabel][0] = x;
						if (bbs[actLabel][1] < x) bbs[actLabel][1] = x;
						if (bbs[actLabel][2] > y) bbs[actLabel][2] = y;
						if (bbs[actLabel][3] < y) bbs[actLabel][3] = y;
					}
				}
			}
		
		
//		System.out.println("Result of the segmentation ("+this.hauteur()+", "+this.largeur()+"): ");
		
		for (int i=0; i<max_labels; i++)
			if (used[i]) 
			{
				result.add(new Context(bbs[i]));
//				System.out.println("Symbol detected @ ("+bbs[i][0]+", "+bbs[i][2]+") ; ("+bbs[i][1]+", "+bbs[i][3]+").");
			}
		
		return result;
	}

	public BufferedImage getBI() {
		return this.bi;
	}
	
}
