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

import java.awt.Point;
import java.util.Vector;

import core.me.Context;
import core.me.Expression;
import core.me.Region;

public class Cluster {

	private Context 		mainSymbol;
	private Vector<Context> symbols;
	private Point			centroid;
	private Region			region;
	
	public Cluster(Context c)
	{
		mainSymbol = c;
		symbols = new Vector<Context>();
		symbols.add(c);
		centroid = c.getCentroid();
		region = c.getBoundingBox();
	}
	
	public Cluster() { symbols = new Vector<Context>(); }
	
	public void addSymbol(Context c) 
	{
		symbols.add(c); 
	}
	
	public void addSymbols(Vector<Context> vc) 
	{ 
		symbols.addAll(vc);
	}
	
	public void computeMainSymbol()
	{
		int area = 0;
		Context c = null;
		for (Context c1: symbols)
			if (c1.area()>area) {area=c1.area(); c = c1; }
		mainSymbol = c;
	}
	
	public void computeRegion()
	{
		if (symbols.size()>0)
		{
			region = symbols.get(0).getBoundingBox();
			for (int i=1; i<symbols.size(); i++)
				region = region.merge(symbols.get(i).getBoundingBox());
		}
	}
	
	public void computeCentroid() 
	{
		int n = symbols.size();
		int x=0,y=0;
		for (Context c: symbols)
		{
			Point p = c.getCentroid();
			x += p.x;
			y += p.y;
		}
		centroid = new Point(x/n, y/n);
	}
	
	public int distance(Cluster cl)
	{
		int dx=0, dy=0;
		
		if (this.getRegion().right>=cl.getRegion().left && cl.getRegion().right>=this.getRegion().left)
			dx = 0;
		else if (cl.getRegion().left>this.getRegion().right)
			dx = cl.getRegion().left-this.getRegion().right;
		else
			dx = this.getRegion().left-cl.getRegion().right;
		
		if (this.getRegion().bottom>=cl.getRegion().top && cl.getRegion().bottom>=this.getRegion().top)
			dy = 0;
		else if (cl.getRegion().top>this.getRegion().bottom)
			dy = cl.getRegion().top-this.getRegion().bottom;
		else
			dy = this.getRegion().top-cl.getRegion().bottom;
		
		return (int)Math.floor(Math.sqrt(dx*dx+dy*dy));
	}
	
	public Point getCentroid() { return centroid; }
	public Vector<Context> getSymbols() { return symbols; }
	public Context getMainSymbol() { return mainSymbol; }
	public Region getRegion() { return region; }
	
	public Cluster merge(Cluster cl)
	{
		Cluster merged = new Cluster();
		merged.addSymbols(symbols);
		merged.addSymbols(cl.getSymbols()); 
		merged.computeCentroid();
		merged.computeMainSymbol();
		merged.computeRegion();
		return merged;
	}
	
	@SuppressWarnings("unchecked")
	public static Vector<Vector<Cluster>> clusterize(Expression e)
	{
		Vector<Vector<Cluster>> iterations = new Vector<Vector<Cluster>>();
		Vector<Cluster> clusters;
		
//		int maxDist = ((e.getBottom()-e.getTop())>(e.getRight()-e.getLeft())) ? (e.getBottom()-e.getTop()) : (e.getRight()-e.getLeft());
		Vector<Context> vc = e.getSymbols();
//		int n = vc.size();
		
		// Initialization
		clusters = new Vector<Cluster>();
		for (Context c: vc)
			clusters.add(new Cluster(c));
		iterations.add((Vector<Cluster>) clusters.clone());
		
		int i=0;
		int threshDist;
		while (clusters.size()>1)
		{
//			System.out.println("Iteration #"+(i+1)+":\n---");
			Vector<Cluster> newClusters = new Vector<Cluster>();
			threshDist = i;
			
			// While there is more than one cluster left
			while (clusters.size()>1)
			{
				// Take one cluster
				Cluster considered = clusters.remove(0);
				
				// Find another cluster to merge it with
				int j=0; 
				while (j<clusters.size())
				{
					// If one is found: merge them
					if (considered.distance(clusters.get(j))<threshDist)
					{
						Cluster cl2 = clusters.remove(j);
						considered = considered.merge(cl2);
					}
					else j++;
				}
				
				// Add the considered cluster to the new clusters
				newClusters.add(considered);
			
//				System.out.println(considered);
			}
			
			// Add non-merged clusters
//			newClusters.addAll(clusters);
			for (Cluster cl: clusters)
			{
//				System.out.println("Remaining: "+cl);
				newClusters.add(cl);
			}	
			
			// Update iterations
			clusters = (Vector<Cluster>) newClusters.clone();
			iterations.add(newClusters);
			i++;
		}
		
		int nbClust = 0;
		i = 0;
		while (i<iterations.size())
		{
			if (iterations.get(i).size()==nbClust)
				iterations.remove(i);
			else
			{
				nbClust = iterations.get(i).size();
				i++;
			}
		}
		
		System.out.println(iterations.size()+" steps.");
		
		for (Vector<Cluster> vcl : iterations)
		{
			System.out.println("Iteration:");
			for (Cluster cl: vcl)
				System.out.println(cl);
		}
		
		return iterations;
	}
	
	public String toString()
	{
		String str = "Cluster: ";
		for (Context c: symbols)
			str += c.getSymbolId()+" ";
		str += "("+this.mainSymbol.getSymbolId()+")";
		return str;
	}
}
