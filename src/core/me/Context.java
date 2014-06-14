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


package core.me;

import java.awt.Point;
import java.util.Vector;

import core.classification.Classifiers;
import core.classification.fuzzy.ContextFuzzyRegions;


import main.Parameters;

import tools.ArrayTools;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class Context {

	private Symbol 	  theSymbol = (Symbol) null;
	
	private SymbolClass theClass = (SymbolClass) null;
	private Relationship theRel  = (Relationship) null;
	
	private Context   parent    = (Context) null;
	private Context   horSymbol = (Context) null;
	private Context   subSymbol = (Context) null;
	private Context   supSymbol = (Context) null;
	private Context   uppSymbol = (Context) null;
	private Context   undSymbol = (Context) null;
	
	private Region    region_   = (Region) null;
	private ContextFuzzyRegions cfr_ = (ContextFuzzyRegions) null;
	
	
	public Context(int[] bb)
	{
		theSymbol = new Symbol(bb);
		
		theClass = new SymbolClass();
		theRel = new Relationship(this, null);
		
		region_ = new Region(bb[0], bb[1]);
		cfr_ = new ContextFuzzyRegions(this);
	}
	
	public Symbol getSymbol() { return this.theSymbol; }
	public int getSymbolId() { return this.theSymbol.sid; }
	public Region getRegion() { return this.region_; }
	public SymbolClass getSymbolClassObject() { return this.theClass; }
	public int getSymbolClass() { return this.theClass.get(); }
	public Relationship getRelationshipObject() { return this.theRel; }
	public int getRelationship() { return this.theRel.get(); }
	public ContextFuzzyRegions getFuzzyRegions() { return this.cfr_; }
	
	public Context remParent()
	{
		if (this.theRel!=null)
			this.clearForRelationship(this.theRel.get());
		this.theRel = new Relationship(this, null);
		this.parent = null;
		return this;
	}
	
	public Context setParent(Context c)
	{
		this.parent = c;
		return this;
	}
	
	public Context getParent() { return this.parent; }
	
	public boolean hasParent() { return (this.getParent()!=null); }
	
	public boolean hasChildren()
	{
		return horSymbol!=null || subSymbol!=null || supSymbol !=null || undSymbol!=null || uppSymbol!=null;
	}
	
	public boolean hasChild(int relation)
	{
		switch (relation)
		{
		case Relationship.INLINE:
			return this.hasHor();
		case Relationship.SUPERSCRIPT:
			return this.hasSup();
		case Relationship.SUBSCRIPT:
			return this.hasSub();
		case Relationship.UPPER:
			return this.hasUpp();
		case Relationship.UNDER:
			return this.hasUnd();
		default:
			return this.hasChildren();	
		}
	}
	
	public Symbol getParentSymbol()
	{
		return this.getParent().getSymbol();
	}
	
	public void setClass(int n){ this.theClass.manualSet(n); }
	public void setClass(double[] d){ this.theClass.manualSet(d); }
	
	public void setRelationship(Relationship r) { this.theRel = r; }
	
	public void setRelationship(double[] d, int rel)
	{
		clearForRelationship(rel);
		
		if (this.hasParent())
			this.getParent().addChild(this, rel);

		this.theRel.setDist(d);
		this.theRel.set(rel);
	}
	
	public void setRelationship(double[] d)
	{
		this.theRel.setDist(d);
		int relation = Utils.maxIndex(d);
		this.theRel.set(relation);
		clearForRelationship(relation);
		if (this.hasParent())
			this.getParent().addChild(this, relation);
	}
	
	public void clearForRelationship(int rel)
	{
		if (this.hasParent() && rel>=0)
		{
			if (this.theRel != null) this.getParent().removeChild(this.theRel.get());
			
			Context c = this.getParent().getChild(rel);
			this.getParent().removeChild(rel);
			if (c!=null) c.remParent();
		}
	}
	
	public void setRelationship(int r)
	{
		double[] d = ArrayTools.crispDist(Parameters.NB_OF_RELATIONSHIP_CLASSES, r);
		this.setRelationship(d, r);
	}
	
	public Context addChild(Context s, int relcode)
	{
		switch (relcode)
		{
		case Relationship.INLINE:
			this.horSymbol = s;
			break;
		case Relationship.SUPERSCRIPT:
			this.supSymbol = s;
			break;
		case Relationship.SUBSCRIPT:
			this.subSymbol = s;
			break;
		case Relationship.UPPER:
			this.uppSymbol = s;
			break;
		case Relationship.UNDER:
			this.undSymbol = s;
			break;
		}
		return this;
	}
	
	public Context removeChild(int relcode)
	{
		switch (relcode)
		{
		case Relationship.INLINE:
			this.horSymbol = (Context)null;
			break;
		case Relationship.SUPERSCRIPT:
			this.supSymbol = (Context)null;
			break;
		case Relationship.SUBSCRIPT:
			this.subSymbol = (Context)null;
			break;
		case Relationship.UPPER:
			this.uppSymbol = (Context)null;
			break;
		case Relationship.UNDER:
			this.undSymbol = (Context)null;
			break;
		}
		return this;
	}
	
	public boolean hasHor()	{ return (this.horSymbol!=null); }
	public boolean hasSub() { return (this.subSymbol!=null); }
	public boolean hasSup() { return (this.supSymbol!=null); }
	public boolean hasUpp() { return (this.uppSymbol!=null); }
	public boolean hasUnd() { return (this.undSymbol!=null); }
	
	public boolean isIn(int x, int y) { return theSymbol.isIn(x, y); }
	
	public int[] getParamToDraw() { return theSymbol.getParamToDraw(); }
	
	public Context getChild(int index)
	{
		switch (index)
		{
		case Relationship.INLINE:
			return horSymbol;
		case Relationship.SUPERSCRIPT:
			return supSymbol;
		case Relationship.SUBSCRIPT:
			return subSymbol;
		case Relationship.UPPER:
			return uppSymbol;
		case Relationship.UNDER:
			return undSymbol;
		}
		
		return null;
	}
	
	public Instance buildSCAInstance() {
		Instances dataStruc = Classifiers.getInst(false).getDataStructSCA();
		
		double[] values = new double[2];
		values[0] = this.theSymbol.getRatio();
		values[1]= dataStruc.attribute(1).indexOfValue("0");
		
		Instance inst = new Instance(1.0, values);
		inst.setDataset(dataStruc);
		inst.setClassMissing();
		return inst;
	}
	
	public Instance buildSCBInstance() {
		Instances dataStruc = Classifiers.getInst(false).getDataStructSCB();
		
		double H=0, D=0, DX=0.;
		int parentClass=1;
		
		if (this.getParent()!=null)
		{
			H = this.getH();
			D = this.getD();
			DX= this.getDX();
			parentClass = this.getParentSymbol().getSymbolClass();
		}
		
		double[] values = new double[5];
		values[0] = H;
		values[1] = D;  
		values[2] = DX;
		values[3] = dataStruc.attribute(3).indexOfValue(Integer.toString(parentClass));
		values[4] = dataStruc.attribute(4).indexOfValue("0");
		
		
		Instance inst = new Instance(1.0, values);
		inst.setDataset(dataStruc);
		inst.setClassMissing();

		
		return inst;
	}
	
	public Instance buildSCCInstance(int index) {
		switch (index)
		{
		case Relationship.INLINE:
			return buildSCC1Instance();
		case Relationship.SUPERSCRIPT:
			return buildSCC2Instance();
		case Relationship.SUBSCRIPT:
			return buildSCC3Instance();
		case Relationship.UPPER:
			return buildSCC4Instance();
		case Relationship.UNDER:
			return buildSCC5Instance();
		}
		return null;
	}
	
	public Instance buildSCC1Instance()
	{
		Instances dataStruc = Classifiers.getInst(false).getDataStructSCC(Relationship.INLINE);
		
		double LH, LD, LDX;
		int LCLASS;
		double height = (double) this.theSymbol.getHeight();
		
		LH = height/((double)this.horSymbol.getHeight());
		LD = (this.theSymbol.getCenter()-this.horSymbol.getCenter())/height;
		LDX= this.getHor().getDX();
		LCLASS = this.horSymbol.getSymbolClass();
		
		double[] values = new double[5];
		values[0] = LH;
		values[1] = LD;
		values[2] = LDX;
		values[3] = dataStruc.attribute(3).indexOfValue(Integer.toString(LCLASS));
		values[4] = dataStruc.attribute(4).indexOfValue("0");
		
		Instance inst = new Instance(1.0, values);
		inst.setDataset(dataStruc);
		inst.setClassMissing();
		return inst;
		
	}
	
	public Instance buildSCC2Instance()
	{
		Instances dataStruc = Classifiers.getInst(false).getDataStructSCC(Relationship.SUPERSCRIPT);
		
		double EH, ED, EDX;
		int ECLASS;
		double height = (double) this.theSymbol.getHeight();
		
		EH = height/((double)this.supSymbol.getHeight());
		ED = (this.theSymbol.getCenter()-this.supSymbol.getCenter())/height;
		EDX= this.getSup().getDX();
		ECLASS = this.supSymbol.getSymbolClass();
		
		double[] values = new double[5];
		values[0] = EH;
		values[1] = ED;
		values[2] = EDX;
		values[3] = dataStruc.attribute(3).indexOfValue(Integer.toString(ECLASS));
		values[4] = dataStruc.attribute(4).indexOfValue("0");
		
		Instance inst = new Instance(1.0, values);
		inst.setDataset(dataStruc);
		inst.setClassMissing();
		return inst;
		
	}
	
	public Instance buildSCC3Instance()
	{
		Instances dataStruc = Classifiers.getInst(false).getDataStructSCC(Relationship.SUBSCRIPT);
		
		double SH, SD, SDX;
		int SCLASS;
		double height = (double) this.theSymbol.getHeight();
		
		SH = height/((double)this.subSymbol.getHeight());
		SD = (this.theSymbol.getCenter()-this.subSymbol.getCenter())/height;
		SDX= this.getSub().getDX();
		SCLASS = this.subSymbol.getSymbolClass();
		
		double[] values = new double[5];
		values[0] = SH;
		values[1] = SD;
		values[2] = SDX;
		values[3] = dataStruc.attribute(3).indexOfValue(Integer.toString(SCLASS));
		values[4] = dataStruc.attribute(4).indexOfValue("0");
		
		Instance inst = new Instance(1.0, values);
		inst.setDataset(dataStruc);
		inst.setClassMissing();
		return inst;
		
	}
	
	public Instance buildSCC4Instance()
	{
		return (Instance) null;
	}
	
	public Instance buildSCC5Instance()
	{
		return (Instance) null;
	}
	
	public Instance buildRCInstance(){
		
		Instances dataStruc = Classifiers.getInst(false).getDataStructRC();
		
		double H=0, D=0, DX=0.;
		int parentClass=1;
		
		if (this.getParent()!=null)
		{
			H = ((double)this.getParentSymbol().getHeight())/this.theSymbol.getHeight();
			D = (this.getParentSymbol().getCenter() - this.theSymbol.getCenter())/this.getParentSymbol().getHeight();
			DX= this.getDX();
			parentClass = this.getParentSymbol().getSymbolClass();
		}
		
		double[] values = new double[6];
		values[0] = H;
		values[1] = D; 
		values[2] = DX;
		values[3] = dataStruc.attribute(3).indexOfValue(""+this.theClass.get());
		values[4] = dataStruc.attribute(4).indexOfValue(""+parentClass);
		values[5] = dataStruc.attribute(5).indexOfValue("0");
		
		
		Instance inst = new Instance(1.0, values);
		inst.setDataset(dataStruc);
		inst.setClassMissing();

		
		return inst;

	}
	
	public void classifySymbol() throws Exception
	{
		theClass = Classifiers.getInst(false).classifySymbol(this);
	}
	
	public void classifyRelationship() throws Exception
	{
		this.setRelationship(Classifiers.getInst(false).getRelationship(this));
	}


	public int distance(Context c) { return theSymbol.distance(c.getSymbol()); }
	
	public Context nearest(Vector<Context> vc)
	{
		Context near = vc.get(0);
		int distance = this.distance(near);
		int td;
		for (Context c : vc)
		{
			td = this.distance(c);
			if (td<distance)
			{
				td = distance;
				near = c;
			}
		}
		
		return near;
	}

	public int getBaseline() { return theSymbol.getBaseline(); }
	public void reinitRegion() { region_ = new Region(theSymbol.xmin, theSymbol.xmax); }
	public int getCenterY() { return (int) Math.floor(theSymbol.getCenter()); }
	public int getBaseline(int k) {  return theSymbol.getBaseline(k); }

	public String getMetaLatex() {
		String[] res = {"a", "p", "b", "\\sum"};
		return ""+res[this.getSymbolClass()-1];
	}

	public Context getSup() { return supSymbol;	}
	public Context getSub() { return subSymbol; }
	public Context getHor() { return horSymbol; }
	public Context getUpp() { return uppSymbol; }
	public Context getUnd() { return undSymbol; }
	
	public int getHeight()   { return theSymbol.getHeight(); }
	public int getWidth()    { return theSymbol.getWidth();  }
	public double getRatio() { return ((double)this.getWidth())/this.getHeight(); }	
	public double getH()     { return ((double)this.getParent().getHeight())/this.getHeight(); }
	public double getD()     { return (this.getParent().getCenter()-this.getCenter())/this.getParent().getHeight(); }
	public double getDX()    { return ((double)this.getParent().getSymbol().xmax-this.getSymbol().xmin)/this.getParent().getWidth(); }
	public double getH(Context c)     { return ((double)c.getHeight())/this.getHeight(); }
	public double getD(Context c)     { return (c.getCenter()-this.getCenter())/c.getHeight(); }
	public double getDX(Context c)    { return ((double)c.getSymbol().xmax-this.getSymbol().xmin)/c.getWidth(); }
	public double getCenter(){ return theSymbol.getCenter(); }
	

	public void removeChildren() {
		for (int i=0;i<Parameters.NB_OF_RELATIONSHIP_CLASSES;i++) this.removeChild(i);
		this.remParent();
	}

	public double getSCConfidence() { return this.theClass.getConfidence(); }
	public double getSCConfidence(int aClass) { return this.theClass.getConfidence(aClass); }

	public double getRCConfidence() { return (this.hasParent()) ? this.theRel.getRCConfidence() : 1.; }
	public double getRCConfidence(int aRelation) { return this.theRel.getRCConfidence(aRelation); }
	
	public double getRelConfidence() { return (this.hasParent()) ? this.theRel.getConfidence() : 1.; }
	public double getRelConfidence(int aRelation) { return this.theRel.getConfidence(aRelation); }
	public double getVirtualRelConfidence(Context parent, int aRelation) { return Relationship.getVirtualConfidences(parent,this)[aRelation]; }
	
	public void setSymbolId(int id) { this.getSymbol().setId(id); }
	
	public boolean equals(Context c) { return this.getSymbolId()==c.getSymbolId(); }
	
	
	public Object[] getDatasetLine()
	{
		Object[] line = new Object[48];
		
		line[1] = 0;
		line[2] = this.getSymbol().ymin;
		line[3] = this.getSymbol().ymax;
		line[4] = this.getSymbol().xmin;
		line[5] = this.getSymbol().xmax;
		
		line[8] = this.getRelationship();
		
		line[13] = this.getHeight();
		line[14] = this.getWidth();
		line[15] = this.getRatio();
		
		line[47] = this.getSymbolClass();
		
		if (this.hasHor())
		{
			line[24] = this.getHor().getH();
			line[25] = this.getHor().getD();
			line[42] = this.getHor().getDX();
			line[32] = this.getHor().getSymbolClass();
		}
		if (this.hasSup())
		{
			line[26] = this.getSup().getH();
			line[27] = this.getSup().getD();
			line[43] = this.getSup().getDX();
			line[33] = this.getSup().getSymbolClass();
		}
		if (this.hasSub())
		{
			line[28] = this.getSub().getH();
			line[29] = this.getSub().getD();
			line[44] = this.getSub().getDX();
			line[34] = this.getSub().getSymbolClass();
		}
		if (this.hasUpp())
		{
			line[35] = this.getUpp().getH();
			line[36] = this.getUpp().getD();
			line[45] = this.getUpp().getDX();
			line[39] = this.getUpp().getSymbolClass();
		}
		if (this.hasUnd())
		{
			line[37] = this.getUnd().getH();
			line[38] = this.getUnd().getD();
			line[46] = this.getUnd().getDX();
			line[40] = this.getUnd().getSymbolClass();
		}
		if (this.hasParent())
		{
			line[22] = this.getH();
			line[23] = this.getD();
			line[41] = this.getDX();
			line[31] = this.getParent().getSymbolClass();
		}
		
		return line;
	}
	
	public void addGaussian() { this.getSymbol().addGaussian(); }
	public Point getCentroid() { return theSymbol.getCentroid(); }
	public int area() { return theSymbol.area(); }
	public Region getBoundingBox() { return theSymbol.getBoundingBox(); }
	
	public double[] baselineScore(Context c)
	{
		double[] temp = new double[Parameters.NB_OF_SYMBOL_CLASSES];
		double dominantBaseline = c.getBaseline();
		double max=0.;
		for (int k=0 ; k<Parameters.NB_OF_SYMBOL_CLASSES ; k++)
		{
			double alpha = Parameters.SCORE_WEIGHT_BASELINE*this.getHeight();
			temp[k] = ( alpha / (alpha + Math.abs(this.getBaseline(k)-dominantBaseline)) );
			if (temp[k]>max) max=temp[k];
		}
		return temp;
	}
	
	public double[] baselineScore() { return (this.hasParent()) ? this.baselineScore(this.getParent()) : null ;}

	public double[] getVirtualRelationship(Context p) throws Exception {
		
		Classifiers cc = Classifiers.get();
		Instances dataStruc = cc.getDataStructRC();
		
		double H=0, D=0, DX=0.;
		int parentClass=1;
		
		H = this.getH(p);
		D = this.getD(p);
		DX = this.getDX(p);
		parentClass = p.getSymbolClass();
		
		double[] values = new double[6];
		values[0] = H;
		values[1] = D; 
		values[2] = DX;
		values[3] = dataStruc.attribute(3).indexOfValue(""+this.theClass.get());
		values[4] = dataStruc.attribute(4).indexOfValue(""+parentClass);
		values[5] = dataStruc.attribute(5).indexOfValue("0");
		
		
		Instance inst = new Instance(1.0, values);
		inst.setDataset(dataStruc);
		inst.setClassMissing();

		
		return cc.getVirtualRelationship(inst);
	}
	
	public double[] possibleChildOf(Context p) throws Exception
	{
		return Classifiers.get().getPossibleChild(p, this);
	}
}
