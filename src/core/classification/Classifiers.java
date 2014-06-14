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

import java.util.Random;

import core.me.Context;
import core.me.Relationship;
import core.me.SymbolClass;

import main.Parameters;

import tools.ArrayTools;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.SerializationHelper;
import weka.core.Utils;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

/**
 * 
 * This class represents the classifiers used in the system. It is used to train, or load the classifiers, as well 
 * as to classify instances
 * 
 * @author Théodore Bluche
 *  build
 */
public class Classifiers {
	
	private BayesNet 				SCA    = (BayesNet) null;
	private MultilayerPerceptron 	SCB    = (MultilayerPerceptron) 	null;
	private MultilayerPerceptron 	SCC1   = (MultilayerPerceptron) 	null;
	private MultilayerPerceptron 	SCC2   = (MultilayerPerceptron)		null;
	private MultilayerPerceptron 	SCC3   = (MultilayerPerceptron) 	null;
	private CostSensitiveClassifier	RC	   = (CostSensitiveClassifier) 	null;
	private J48						YNC	   = (J48) null;
	
	private Instances				dataStructSCA  = (Instances) null;
	private Instances				dataStructSCB  = (Instances) null;
	private Instances				dataStructSCC1 = (Instances) null;
	private Instances				dataStructSCC2 = (Instances) null;
	private Instances				dataStructSCC3 = (Instances) null;
	private Instances				dataStructRC   = (Instances) null;
	private Instances				dataStructYC   = (Instances) null;
	
	public static String 			dbase = "jdbc:odbc:oxproject";
	private static Classifiers		uinst = null;
	
	/**
	 * This object is a singleton
	 * @param train: indicates whether the classifiers have to be trained
	 * @return the <code>Classifiers</code> unique instance
	 */
	public static Classifiers getInst(boolean train)
	{
		if (uinst==null)
			uinst = new Classifiers(train);
		return uinst;
	}
	
	/**
	 * Private constructor for the <code>Classifiers</code> object
	 * @param train
	 */
	private Classifiers(boolean train)
	{
		SCA  = new BayesNet();
		SCB  = new MultilayerPerceptron();
		SCC1 = new MultilayerPerceptron();
		SCC2 = new MultilayerPerceptron();
		SCC3 = new MultilayerPerceptron();
		
		RC = new CostSensitiveClassifier();
		YNC = new J48();
		
		if (train)
		{
			try {
				this.trainSC();
			} catch (Exception e) {
				System.out.println("The system encountered the following error while training SC:");
				e.printStackTrace();
			}
			try {
				this.trainRC();
			} catch (Exception e) {
				System.out.println("The system encountered the following error while training RC:");
				e.printStackTrace();
			}
			try {
				this.trainYNC();
			} catch (Exception e) {
				System.out.println("The system encountered the following error while training YNC:");
				e.printStackTrace();
			}
		}
		else
		{
			try {
				readSC("SCA.model", "SCB.model", "SCC1.model","SCC2.model","SCC3.model");
				readRC("RC.model");
				readYNC("YNC.model");
			} catch (Exception e) {
				System.out.println("Error while reading the classifiers: ");
				e.printStackTrace();
			}
		}
		
		// Strutures Creations
		FastVector labels = new FastVector();
		labels.addElement("0");
		labels.addElement("1");
		labels.addElement("2");
		labels.addElement("3");
		labels.addElement("4");
		
		FastVector clabels = new FastVector();
		clabels.addElement("1");
		clabels.addElement("2");
		clabels.addElement("3");
		clabels.addElement("4");
		
		FastVector clabels2 = new FastVector();
		clabels2.addElement("0");
		clabels2.addElement("1");
		clabels2.addElement("2");
		clabels2.addElement("3");
		clabels2.addElement("4");
		
		FastVector clabels3 = new FastVector();
		clabels3.addElement("Y");
		clabels3.addElement("N");
		
		// Creating the structure for SC
		FastVector attrs = new FastVector();
		attrs.addElement(new Attribute("RATIO"));
		attrs.addElement(new Attribute("CLASS",clabels));
		dataStructSCA = new Instances("SCA-STRUCT", attrs, 0);
		dataStructSCA.setClassIndex(1);
		
		FastVector attrsB = new FastVector();
		attrsB.addElement(new Attribute("H2"));
		attrsB.addElement(new Attribute("D2"));
		attrsB.addElement(new Attribute("DX"));
		attrsB.addElement(new Attribute("PCLASS",clabels));
		attrsB.addElement(new Attribute("CLASS",clabels));
		dataStructSCB = new Instances("SCB-STRUCT", attrsB, 0);
		dataStructSCB.setClassIndex(4);
		
		FastVector attrsC1 = new FastVector();
		FastVector attrsC2 = new FastVector();
		FastVector attrsC3 = new FastVector();
		attrsC1.addElement(new Attribute("LH"));
		attrsC1.addElement(new Attribute("LD"));
		attrsC1.addElement(new Attribute("LDX"));
		attrsC1.addElement(new Attribute("LCLASS",clabels));
		attrsC1.addElement(new Attribute("CLASS",clabels));
		
		attrsC2.addElement(new Attribute("EH"));
		attrsC2.addElement(new Attribute("ED"));
		attrsC2.addElement(new Attribute("EDX"));
		attrsC2.addElement(new Attribute("ECLASS",clabels));
		attrsC2.addElement(new Attribute("CLASS",clabels));
		
		attrsC3.addElement(new Attribute("SH"));
		attrsC3.addElement(new Attribute("SD"));
		attrsC3.addElement(new Attribute("SDX"));
		attrsC3.addElement(new Attribute("SCLASS",clabels));
		attrsC3.addElement(new Attribute("CLASS",clabels));
		
		dataStructSCC1 = new Instances("SCC1-STRUCT", attrsC1, 0);
		dataStructSCC1.setClassIndex(4);
		
		dataStructSCC2 = new Instances("SCC2-STRUCT", attrsC2, 0);
		dataStructSCC2.setClassIndex(4);
		
		dataStructSCC3 = new Instances("SCC3-STRUCT", attrsC3, 0);
		dataStructSCC3.setClassIndex(4);
		
		FastVector attrs2 = new FastVector();
		attrs2.addElement(new Attribute("H2"));
		attrs2.addElement(new Attribute("D2"));
		attrs2.addElement(new Attribute("DX"));
		attrs2.addElement(new Attribute("CLASS", clabels));
		attrs2.addElement(new Attribute("PCLASS", clabels));
		attrs2.addElement(new Attribute("RELID",clabels2));
		dataStructRC = new Instances("RC-STRUCT", attrs2, 0);
		dataStructRC.setClassIndex(5);
		
		FastVector attrs3 = new FastVector();
		attrs3.addElement(new Attribute("PCLASS", clabels));
		attrs3.addElement(new Attribute("CCLASS", clabels));
		attrs3.addElement(new Attribute("RAREA"));
		attrs3.addElement(new Attribute("H"));
		attrs3.addElement(new Attribute("D"));
		attrs3.addElement(new Attribute("V"));
		attrs3.addElement(new Attribute("YN",clabels3));
		dataStructYC = new Instances("YC-STRUCT", attrs3, 0);
		dataStructYC.setClassIndex(6);
	}
	
	public void trainSC() throws Exception
	{
		String sql;
		
		// ---
		// Connect to the database
		// ---
		InstanceQuery query = new InstanceQuery();
		query.setDatabaseURL(dbase);
		query.setUsername("");
		query.setPassword("");
		
		
		// ---
		// ---
		// SCA
		// ---
		// ---
		
		sql =  "SELECT ";
		sql += "CR.ratio, CR.class ";
		sql += "FROM Class_ratio AS CR;";
		
		query.setQuery(sql);
		Instances data = query.retrieveInstances();
		
		// ---
		// Setting options
		// ---
		String[] options = Utils.splitOptions("-D -Q weka.classifiers.bayes.net.search.local.K2 -- -P 1 -S BAYES -E weka.classifiers.bayes.net.estimate.SimpleEstimator -- -A 0.5");
		SCA.setOptions(options);
		data.setClassIndex(data.numAttributes()-1);
		
		// ---
		// Train the classifier
		// ---
		System.out.println("Building SCA ...");
		SCA.buildClassifier(data);
		System.out.println("Done.");
		
		// ---
		// Classifier evaluation
		// ---
		System.out.println("Cross-validation for SCA...");
		Evaluation eval = new Evaluation(data);
		eval.crossValidateModel(SCA, data, 10, new Random(1));
		System.out.println("Done.");
		System.out.println(eval.toSummaryString("\n Results for SCA: \n\n", false));
		
		
		
		
		// ---
		// ---
		// SCB
		// ---
		// ---
		
		sql = "SELECT ";
		sql += "Data.H2, Data.D2, Data.DX, ";
		sql += "Data.PARENT_CHAR AS PCLASS, ";
		sql += "Data.CLASS ";
		sql += "FROM Data ";
		sql += "WHERE (((Data.SEGERR)=0) AND (Data.PARENT_CHAR<>'0') );";
		
		query.setQuery(sql);
		data = query.retrieveInstances();
		
		// ---
		// Setting options
		// ---
		options = Utils.splitOptions("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a");
		SCB.setOptions(options);
		data.setClassIndex(data.numAttributes()-1);
		
		// ---
		// Train the classifier
		// ---
		System.out.println("Building SCB ...");
		SCB.buildClassifier(data);
		System.out.println("Done.");
		
		// ---
		// Classifier evaluation
		// ---
		System.out.println("Cross-validation for SCB...");
		eval = new Evaluation(data);
		eval.crossValidateModel(SCB, data, 10, new Random(1));
		System.out.println("Done.");
		System.out.println(eval.toSummaryString("\n Results for SCB: \n\n", false));
		
		
		// ---
		// ---
		// SCC
		// ---
		// ---
		
		// ----
		// SCC1
		// ----
		
		sql = "SELECT ";
		sql += "Data.LH, Data.LD, Data.LDX, Data.LCLASS, ";
		sql += "Data.CLASS ";
		sql += "FROM Data ";
		sql += "WHERE ( (Data.SEGERR)=0  AND ( (Data.LCLASS)<>'0' ) );";
		
		query.setQuery(sql);
		data = query.retrieveInstances();
		
		// ---
		// Setting options
		// ---
		options = Utils.splitOptions("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a");
		SCC1.setOptions(options);
		data.setClassIndex(data.numAttributes()-1);
		
		// ---
		// Train the classifier
		// ---
		System.out.println("Building SCC1 ...");
		SCC1.buildClassifier(data);
		System.out.println("Done.");
		
		// ---
		// Classifier evaluation
		// ---
		System.out.println("Cross-validation for SCC1...");
		eval = new Evaluation(data);
		eval.crossValidateModel(SCC1, data, 10, new Random(1));
		System.out.println("Done.");
		System.out.println(eval.toSummaryString("\n Results for SCC1: \n\n", false));
		
		// ----
		// SCC2
		// ----
		
		sql = "SELECT ";
		sql += "Data.EH, Data.ED, Data.EDX, Data.ECLASS, ";
		sql += "Data.CLASS ";
		sql += "FROM Data ";
		sql += "WHERE ( (Data.SEGERR)=0  AND ( (Data.ECLASS)<>'0' ) );";
		
		query.setQuery(sql);
		data = query.retrieveInstances();
		
		// ---
		// Setting options
		// ---
//		options = Utils.splitOptions("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a");
		SCC2.setOptions(options);
		data.setClassIndex(data.numAttributes()-1);
		
		// ---
		// Train the classifier
		// ---
		System.out.println("Building SCC2 ...");
		SCC2.buildClassifier(data);
		System.out.println("Done.");
		
		// ---
		// Classifier evaluation
		// ---
		System.out.println("Cross-validation for SCC2...");
		eval = new Evaluation(data);
		eval.crossValidateModel(SCC2, data, 10, new Random(1));
		System.out.println("Done.");
		System.out.println(eval.toSummaryString("\n Results for SCC2: \n\n", false));
		
		
		// ----
		// SCC3
		// ----
		
		sql = "SELECT ";
		sql += "Data.SH, Data.SD, Data.SDX, Data.SCLASS, ";
		sql += "Data.CLASS ";
		sql += "FROM Data ";
		sql += "WHERE ( (Data.SEGERR)=0  AND ( (Data.SCLASS)<>'0' ) );";
		
		query.setQuery(sql);
		data = query.retrieveInstances();
		
		// ---
		// Setting options
		// ---
//		options = Utils.splitOptions("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a");
		SCC3.setOptions(options);
		data.setClassIndex(data.numAttributes()-1);
		
		// ---
		// Train the classifier
		// ---
		System.out.println("Building SCC3 ...");
		SCC3.buildClassifier(data);
		System.out.println("Done.");
		
		// ---
		// Classifier evaluation
		// ---
		System.out.println("Cross-validation for SCC3...");
		eval = new Evaluation(data);
		eval.crossValidateModel(SCC3, data, 10, new Random(1));
		System.out.println("Done.");
		System.out.println(eval.toSummaryString("\n Results for SCC3: \n\n", false));
		
	}
	
	public void trainRC() throws Exception
	{
		// ---
		// Retrieve the instances in the database
		// ---
		InstanceQuery query = new InstanceQuery();
		query.setDatabaseURL(dbase);
		query.setUsername("");
		query.setPassword("");
		
		String sql = "SELECT ";
		sql += "Data.H2, Data.D2, Data.DX, ";
		sql += "Data.CLASS, Data.PARENT_CHAR AS PCLASS, ";
		sql += "Data.RELID ";
		sql += "FROM Data ";
		sql += "WHERE (((Data.SEGERR)=0) AND (Data.PARENT_CHAR<>'0') );";
		
		query.setQuery(sql);
		Instances data = query.retrieveInstances();
		
		// ---
		// Setting options
		// ---
//		String[] options = Utils.splitOptions("-L 0.2 -M 0.2 -N 50 -V 0 -S 0 -E 20 -H 5 ");
		String[] options = Utils.splitOptions("-cost-matrix \"[0.0 1.0 1.0 0.1 0.1; 1.0 0.0 1.0 0.1 0.1; 1.0 1.0 0.0 0.1 0.1; 10.0 10.0 10.0 0.0 1.0; 10.0 10.0 10.0 1.0 0.0]\" -S 1 -W weka.classifiers.functions.MultilayerPerceptron -- -L 0.2 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a");
		RC.setOptions(options);
		data.setClassIndex(data.numAttributes()-1);
		
		// ---
		// Train
		// ---
		System.out.println("Building RC...");
		RC.buildClassifier(data);
		System.out.println("Done.");
		
		// ---
		// Evaluation
		// ---
		System.out.println("Cross-validation for RC...");
		Evaluation eval = new Evaluation(data);
		eval.crossValidateModel(RC, data, 10, new Random(1));
		System.out.println("Done.");
		System.out.println(eval.toSummaryString("\n Results for RC: \n\n", false));
		
	}
	
	public void trainYNC() throws Exception
	{
		// ---
		// Retrieve the instances in the database
		// ---
		InstanceQuery query = new InstanceQuery();
		query.setDatabaseURL(dbase);
		query.setUsername("");
		query.setPassword("");
		
		String sql = "SELECT ";
		sql += "YNCdata.PCLASS, YNCdata.CCLASS, YNCdata.RAREA, YNCdata.H, YNCdata.D, YNCdata.V, ";
		sql += "YNCdata.YN ";
		sql += "FROM YNCdata ";
		
		query.setQuery(sql);
		Instances data = query.retrieveInstances();
		
		// ---
		// Setting options
		// ---
		String[] options = Utils.splitOptions("-R -N 3 -Q 1 -M 30");
		YNC.setOptions(options);
		data.setClassIndex(data.numAttributes()-1);
		
		// ---
		// Train
		// ---
		System.out.println("Building YC...");
		YNC.buildClassifier(data);
		System.out.println("Done.");
		
		// ---
		// Evaluation
		// ---
		System.out.println("Cross-validation for YNC...");
		Evaluation eval = new Evaluation(data);
		eval.crossValidateModel(YNC, data, 10, new Random(1));
		System.out.println("Done.");
		System.out.println(eval.toSummaryString("\n Results for YNC: \n\n", false));
		
	}
	
	public Classifiers saveSC(String filename1, String filename2, String filename3, String filename4, String filename5) throws Exception
	{
		SerializationHelper.write(filename1, SCA);
		SerializationHelper.write(filename2, SCB);
		SerializationHelper.write(filename3, SCC1);
		SerializationHelper.write(filename4, SCC2);
		SerializationHelper.write(filename5, SCC3);
		return this;
	}
	
	public Classifiers saveRC(String filename) throws Exception
	{
		SerializationHelper.write(filename, RC);
		return this;
	}
	
	public Classifiers saveYNC(String filename) throws Exception
	{
		SerializationHelper.write(filename, YNC);
		return this;
	}
	
	public MultilayerPerceptron readSC(String filename1, String filename2, String filename3,String filename4,String filename5) throws Exception
	{
		SCA  = (BayesNet) SerializationHelper.read(filename1);
		SCB  = (MultilayerPerceptron) SerializationHelper.read(filename2);
		SCC1 = (MultilayerPerceptron) SerializationHelper.read(filename3);
		SCC2 = (MultilayerPerceptron) SerializationHelper.read(filename4);
		SCC3 = (MultilayerPerceptron) SerializationHelper.read(filename5);
		return SCC1;
	}
	
	public CostSensitiveClassifier readRC(String filename) throws Exception
	{
		RC = (CostSensitiveClassifier) SerializationHelper.read(filename);
		return RC;
	}
	
	public J48 readYNC(String filename) throws Exception
	{
		YNC = (J48) SerializationHelper.read(filename);
		return YNC;
	}

	
	public Instances getDataStructSCA() { return dataStructSCA; }
	
	public Instances getDataStructSCB() { return dataStructSCB; }
	
	public Instances getDataStructSCC(int index)
	{
		switch (index)
		{
		case Relationship.INLINE:
			return dataStructSCC1;
		case Relationship.SUPERSCRIPT:
			return dataStructSCC2;
		case Relationship.SUBSCRIPT:
			return dataStructSCC3;
		}
		return null;
	}
	
	public Instances getDataStructRC(){ return dataStructRC; }
	
	public Instances getDataStructYNC() { return dataStructYC; }
	
	public double[] getSymbolClassA(Context c) throws Exception
	{
		Instance inst = c.buildSCAInstance();
		double[] dist = SCA.distributionForInstance(inst);
		if (dist.length != Parameters.NB_OF_SYMBOL_CLASSES) 
		{
			double[] dist2 = new double[Parameters.NB_OF_SYMBOL_CLASSES];
			for (int i=0; i<Parameters.NB_OF_SYMBOL_CLASSES; i++)
				dist2[i] = (i<dist.length) ? dist[i] : 0.;
			dist = dist2;
		}
		return dist;
	}
	
	public double[] getSymbolClassB(Context c) throws Exception
	{
		Instance inst = c.buildSCBInstance();
		double[] dist = SCB.distributionForInstance(inst);
		if (dist.length != Parameters.NB_OF_SYMBOL_CLASSES) 
		{
			double[] dist2 = new double[Parameters.NB_OF_SYMBOL_CLASSES];
			for (int i=0; i<Parameters.NB_OF_SYMBOL_CLASSES; i++)
				dist2[i] = (i<dist.length) ? dist[i] : 0.;
			dist = dist2;
		}
		return dist;
	}
	
	public double[] getSymbolClassC(Context c, int index) throws Exception
	{
		Instance inst = c.buildSCCInstance(index);
		double[] dist = null;
		switch (index)
		{
		case Relationship.INLINE:
			dist = SCC1.distributionForInstance(inst);
			break;
		case Relationship.SUPERSCRIPT:
			dist = SCC2.distributionForInstance(inst);
			break;
		case Relationship.SUBSCRIPT:
			dist = SCC3.distributionForInstance(inst);
			break;
		case Relationship.UPPER:
			dist = ArrayTools.crispDist(Parameters.NB_OF_SYMBOL_CLASSES, SymbolClass.VARRANGE_IND);
			break;
		case Relationship.UNDER:
			dist = ArrayTools.crispDist(Parameters.NB_OF_SYMBOL_CLASSES, SymbolClass.VARRANGE_IND);
			break;
		}
		if (dist.length != Parameters.NB_OF_SYMBOL_CLASSES) 
		{
			double[] dist2 = new double[Parameters.NB_OF_SYMBOL_CLASSES];
			for (int i=0; i<Parameters.NB_OF_SYMBOL_CLASSES; i++)
				dist2[i] = (i<dist.length) ? dist[i] : 0.;
			dist = dist2;
		}
		return dist;
	}
	
	public double[] getRelationship(Context c) throws Exception
	{
		Instance inst = c.buildRCInstance();
		double[] dist = RC.distributionForInstance(inst);
		
		return dist;
	}
	
	public double[] getPossibleChild(Context p, Context c) throws Exception
	{
		Instance inst = this.buildYNCInstance(p,c);
		double[] dist = YNC.distributionForInstance(inst);
		
		return dist;
	}
	
	private Instance buildYNCInstance(Context p, Context c) 
	{
		double RAREA=0, H=0, D=0, V=0.;
		int parentClass=1, childClass=1;
		
		RAREA = ((double)c.area())/p.area();
		H = c.getH(p);
		D = c.getD(p);
		V = c.getDX(p);
		parentClass = (p.getSymbolClass()>0) ? p.getSymbolClass() : 1;
		childClass = (c.getSymbolClass()>0) ? c.getSymbolClass() : 1;
		
		double[] values = new double[7];
		values[0] = dataStructYC.attribute(0).indexOfValue(""+parentClass);
		values[1] = dataStructYC.attribute(1).indexOfValue(""+childClass);
		values[2] = RAREA; 
		values[3] = H;
		values[4] = D;
		values[5] = V;
		values[6] = dataStructYC.attribute(6).indexOfValue("N");
		
		
		Instance inst = new Instance(1.0, values);
		inst.setDataset(dataStructYC);
		inst.setClassMissing();

		
		return inst;
	}

	public SymbolClass classifySymbol(Context c) throws Exception
	{
		boolean hasParent = c.hasParent();
		boolean hasSub	  = c.hasSub();
		boolean hasSup	  = c.hasSup();
		boolean hasHor    = c.hasHor();
		boolean hasUpp	  = c.hasUpp();
		boolean hasUnd    = c.hasUnd();
		
		double[] distA, distB, distC1, distC2, distC3, distC4, distC5;
		
		distA  = getSymbolClassA(c);
		for (int i=0; i<Parameters.NB_OF_SYMBOL_CLASSES; i++) distA[i] /= SymbolClass.PROPCL[i];
		Utils.normalize(distA);
		distB  = (hasParent) ? getSymbolClassB(c) : ArrayTools.evenDist(Parameters.NB_OF_SYMBOL_CLASSES);
		distC1 = (hasHor) ? getSymbolClassC(c,Relationship.INLINE)      : ArrayTools.evenDist(Parameters.NB_OF_SYMBOL_CLASSES);
		distC2 = (hasSup) ? getSymbolClassC(c,Relationship.SUPERSCRIPT) : ArrayTools.evenDist(Parameters.NB_OF_SYMBOL_CLASSES);
		distC3 = (hasSub) ? getSymbolClassC(c,Relationship.SUBSCRIPT)   : ArrayTools.evenDist(Parameters.NB_OF_SYMBOL_CLASSES);
		distC4 = (hasUpp) ? getSymbolClassC(c, Relationship.UPPER)      : ArrayTools.evenDist(Parameters.NB_OF_SYMBOL_CLASSES);
		distC5 = (hasUnd) ? getSymbolClassC(c, Relationship.UNDER)      : ArrayTools.evenDist(Parameters.NB_OF_SYMBOL_CLASSES);
		
		return new SymbolClass(distC1, distC2, distC3, distC4, distC5, distA, distB);
	}

	public static Classifiers get() {
		if (uinst==null)
			uinst = new Classifiers(false);
		return uinst;
	}

	public double[] getVirtualRelationship(Instance inst) throws Exception {
		return RC.distributionForInstance(inst);
	}

}