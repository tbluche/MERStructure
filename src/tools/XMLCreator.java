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

import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import main.Parameters;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import core.me.Context;
import core.me.Expression;
import core.me.Relationship;

import exceptions.InvalidInterpretation;
import gui.filemanager.FileManager;


/**
 * This class provides tools to save and load data in XML format.<br>
 * In particular, it is possible to save an interpretation for an expression, which consist of relationships, 
 * symbols classes, and the associated confidence.<br>
 * It is also possible to load an interpretation, but it is important to note the an interpretation is relative
 * to symbol id's. To first step is then to load the id's.
 * @author Théodore Bluche
 *  tools
 */
@SuppressWarnings("unchecked")
public class XMLCreator {
	
	/**
	 * Write an XML file containing the interpretation of an expression
	 * @param e		: expression which interpretation is to be saved
	 * @param file	: path to the file to be created
	 */
	public static void createXML(Expression e, String file)
	{
		// Create the main node (root) of the XML
		Element el = new Element("expression");
		el.setAttribute(new Attribute("width", e.getDimension().width+""));		// Save the width ...
		el.setAttribute(new Attribute("height", e.getDimension().height+""));	// ... and the height.
		el.addContent(createSymbolNode(e.findLeftMost()));						// Add the expression interpretation
		
		// Create the document
		Document doc = new Document(el);
		try {
			// Write the file
		    XMLOutputter outputter = new XMLOutputter();
		    Format f = outputter.getFormat();		// Set the format
		    f.setIndent("     "); 					//	- indent
		    f.setLineSeparator("\r\n"); 			//  - new lines
		    outputter.setFormat(f);
		    outputter.output(doc, new FileWriter(new File(FileManager.get().getWorkspace()+file)));
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
	}
	
	/**
	 * Create an XML file to save a set of expressions.
	 * 
	 * @param ve	: Set of expressions
	 * @param file	: path to the file
	 * @see   createXML(Expression, String)
	 */
	public static void createXML(Vector<Expression> ve, String file)
	{
		// Set the root node
		Element root = new Element("expressions");
		root.setAttribute(new Attribute("count", ve.size()+""));	// Save the number of expressions
		// Save all expressions as in createXML(Expression, String)
		for (Expression e: ve)
		{
			Element el = new Element("expression");
			// Set the attributes
			el.setAttribute(new Attribute("width", e.getDimension().width+""));
			el.setAttribute(new Attribute("height", e.getDimension().height+""));
			// The only child node corrspond to the dominant symbol
			el.addContent(createSymbolNode(e.findLeftMost()));
			root.addContent(el);
		}
		// Save the document
		Document doc = new Document(root);
		try {
		    XMLOutputter outputter = new XMLOutputter();
		    Format f = outputter.getFormat();
		    f.setIndent("     ");
		    f.setLineSeparator("\r\n"); 
		    outputter.setFormat(f);
		    outputter.output(doc, new FileWriter(new File(FileManager.get().getWorkspace()+file)));
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
	}
	
	/**
	 * Single expression interpretation in XML to store it in an object, or example <code>Interpretation</code>
	 * @param e : the expression
	 * @return an XML document for the interpretation
	 * @see core.classification.Interpretation
	 */
	public static Document xmlInterpretation(Expression e)
	{
		return new Document(createSymbolNode(e.findLeftMost()));
	}
	
	/**
	 * Create the XML node for a symbol. <br>
	 * It contains:
	 * <ul>
	 * <li> ID, class, relationship
	 * <li> confidence values for different classes and relationships
	 * <li> bounding boxes
	 * <li> children symbols
	 * </ul>
	 * @param c : context associated with a symbol
	 * @return the XML node for this symbol
	 */
	public static Element createSymbolNode(Context c)
	{
		Element e;
		double[] d;
		
		// Create the node
		Element el = new Element("symbol");
		// Set the attributes
		el.setAttribute(new Attribute("id",    ""+c.getSymbolId()));
		el.setAttribute(new Attribute("class", c.getSymbolClass()+""));
		el.setAttribute(new Attribute("rel",   c.getRelationship()+""));
		
		// Child: bounding box
		e = new Element("boundingBox");
		e.addContent( new Element("xmin").addContent(c.getSymbol().xmin+"") );
		e.addContent( new Element("xmax").addContent(c.getSymbol().xmax+"") );
		e.addContent( new Element("ymin").addContent(c.getSymbol().ymin+"") );
		e.addContent( new Element("ymax").addContent(c.getSymbol().ymax+"") );
		
		el.addContent(e);
		
		// Child: confidence on the symbol class
		e = new Element("symbolClass");
		d = c.getSymbolClassObject().confidences();
		for (int i=0; i<Parameters.NB_OF_SYMBOL_CLASSES; i++)
			e.addContent( new Element("class").setAttribute("id", (i+1)+"").addContent(d[i]+"") );
		
		el.addContent(e);
		
		// Child: confidence on the relationship
		e = new Element("relationshipClass");
		d = c.getRelationshipObject().confidences();
		for (int i=0; i<Parameters.NB_OF_RELATIONSHIP_CLASSES; i++)
			e.addContent( new Element("class").setAttribute("id", i+"").addContent(d[i]+"") );
		
		el.addContent(e);
		
		// Add children symbols
		if (c.hasSup()) el.addContent(createSymbolNode(c.getSup()));
		if (c.hasSub()) el.addContent(createSymbolNode(c.getSub()));
		if (c.hasUpp()) el.addContent(createSymbolNode(c.getUpp()));
		if (c.hasUnd()) el.addContent(createSymbolNode(c.getUnd()));
		if (c.hasHor()) el.addContent(createSymbolNode(c.getHor()));
		
		return el;
	}
	
	/**
	 * Write an XML file containing a list of symbols
	 * @param vc	: list of symbols
	 * @param file	: path to the file
	 */
	public static void listOfSymbolXML(Vector<Context> vc, String file)
	{
		// XML node with the list of symbols
		Element e = listOfSymbols(vc);
		
		// Creation of the XML document
		Document doc = new Document(e);
		
		// Write the file
		try {
		    XMLOutputter outputter = new XMLOutputter();
		    Format f = outputter.getFormat();
		    f.setIndent("     "); 
		    f.setLineSeparator("\r\n"); 
		    outputter.setFormat(f);
		    outputter.output(doc, new FileWriter(new File(FileManager.get().getWorkspace()+file)));
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
	}
	
	/**
	 * Create an XML node containing a list of symbols along with their id.
	 * @param vc	: list of symbols
	 * @return the corresponding XML node
	 */
	public static Element listOfSymbols(Vector<Context> vc)
	{
		// Create the root node
		Element e = new Element("expression");
		
		// Add symbols nodes with id attribute
		for (Context c: vc)
			e.addContent( new Element("symbol") . setAttribute(new Attribute("id", ""+c.getSymbolId())));
		
		return e;
	}
	
	/**
	 * Create <code>div</code> node for the HTML representation of the bounding boxes
	 * @param e	: input expression
	 * @return the <code>div</code> HTML node
	 */
	public static Element htmlExpression(Expression e)
	{
		String style;
		
		// Create the expression box
		Element root = new Element("div");
		root.setAttribute(new Attribute("class", "equation"));
		style = "position:relative;";
		style += "display:inline-block;";
		style += "margin:20px;";
		style += "width:"+e.getDimension().width*2+"px;";
		style += "height:"+e.getDimension().height*2+"px";
		root.setAttribute(new Attribute("style",style));
		
		int zIndex = 500;
		
		// Create the symbol boxes
		for (Context c : e.getSymbols())
		{
			int[] params = c.getParamToDraw();
			style = "position:absolute;";
			style += "top:"+params[1]*2+"px;";
			style += "left:"+params[0]*2+"px;";
			style += "width:"+params[3]*2+"px;";
			style += "height:"+params[2]*2+"px;";
			style += "z-index:"+(++zIndex)+";";
			style += "font-size:"+((2*params[2]*2)/3)+"px;";
			style += "padding-top:"+((params[2]*2)/4)+"px;";
			style += "padding-left:"+((params[3]*2)/5)+"px;";
			Element el = new Element("div");
			el.setAttribute(new Attribute("class", "symbol"));
			el.setAttribute(new Attribute("style", style));
			
			// Create the choice list
			Element select = new Element("select");
			Element option;
			// Define the options
			Vector<Element> options = new Vector<Element>();
			option = new Element("option");
			option.setAttribute(new Attribute("value","0"));
			option.addContent("choose class");
			options.add(option);
			
			option = new Element("option");
			option.setAttribute(new Attribute("value","1"));
			option.addContent("small");
			options.add(option);
			
			option = new Element("option");
			option.setAttribute(new Attribute("value","2"));
			option.addContent("descending");
			options.add(option);
			
			option = new Element("option");
			option.setAttribute(new Attribute("value","3"));
			option.addContent("ascending");
			options.add(option);
			
			option = new Element("option");
			option.setAttribute(new Attribute("value","4"));
			option.addContent("variable range");
			options.add(option);
			
			// Add the options to the list
			select.addContent(options);
			
			el.addContent(select);
			
			Element text = new Element("div");
			text.addContent(" ");
			el.addContent(text);
			
			root.addContent(el);
		}
		
		return root;
	}
	
	/**
	 * Create an HTML document for the representation of expressions
	 * @param ve	: list of expression	
	 * @param file	: path to the file to save
	 */
	public static void htmlSave(Vector<Expression> ve, String file)
	{
		Element root = new Element("html");
		
		Element head = new Element("head");
		
		Element title = new Element("title");
		title.addContent("Symbol Classification");
		head.addContent(title);
		
		Element jquery = new Element("script");
		jquery.setAttribute( new Attribute("type", "text/javascript") );
		jquery.setAttribute( new Attribute("src", "jquery.js") );
		jquery.addContent(" ");
		head.addContent(jquery);
		
		Element script = new Element("script");
		script.setAttribute( new Attribute("type", "text/javascript") );
		script.setAttribute( new Attribute("src", "symclass.js") );
		script.addContent(" ");
		head.addContent(script);
		
		root.addContent(head);
		
		Element body = new Element("body");
		Element style = new Element("style");
		style.addContent("body { background-color:#f7fcff; color:#1a22c9 }  ");
		style.addContent(".equation { background-color:grey;  border: 1px solid black; } ");
		style.addContent(".symbol   { background-color:white; border: 1px solid red;   }");
		body.addContent(style);
		
		Element h1 = new Element("h1");
		h1.addContent("Classify the symbols in the following expressions");
		body.addContent(h1);
		
		Element seeExamples = new Element("a");
		seeExamples.setAttribute( new Attribute("href", "examples.html") );
		seeExamples.setAttribute( new Attribute("target", "_blank") );
		seeExamples.addContent("See examples...");
		body.addContent(seeExamples);
		body.addContent(new Element("br"));
		
		Element seeExplain = new Element("a");
		seeExplain.setAttribute( new Attribute("href", "index.html") );
		seeExplain.addContent("Back to the explanations...");
		body.addContent(seeExplain);
		body.addContent(new Element("br"));
		
		Element input = new Element("input");
		input.setAttribute( new Attribute("type", "text") );
		input.setAttribute( new Attribute("id", "field") );
		
		
		Element fieldOf = new Element("div");
		fieldOf.setAttribute( new Attribute("style", "display:block;padding:5px;margin:20px;border:1px solid green;text-align:center;background-color:#aaff56") );
		fieldOf.addContent("Field of study/research/work : ");
		fieldOf.addContent(input);
		
		body.addContent(fieldOf);
		
		
		for (Expression e: ve)
		{
			Element content = htmlExpression(e);
			body.addContent(content);
		}
		
		Element button = new Element("button");
		button.addContent("Submit");
		
		body.addContent(button);
		
		root.addContent(body);
		
		Document doc = new Document(root);
		try {
		    XMLOutputter outputter = new XMLOutputter();
		    Format f = outputter.getFormat();
		    f.setIndent("     "); // use two space indent
		    f.setLineSeparator("\r\n"); 
		    outputter.setFormat(f);
		    outputter.output(doc, new FileWriter(new File(file)));
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
	}
	
	/**
	 * Extract a symbol (without its children) from an XML node representing a symbol
	 * @param symbolElement	: XML node
	 * @return the symbol object
	 * @throws InvalidInterpretation
	 */
	public static Context extractSymbol(Element symbolElement) throws InvalidInterpretation
	{
		// Parameters initialisations
		int[] boundingBox 	= new int[4];
		int   id			= 0;
		int   rel;
		double[] dist		;
		double[] rdist		;
		
		// Relevant elements
		Element 
			boundingBoxElement 			= symbolElement.getChild("boundingBox"), 
			symbolClassElement			= symbolElement.getChild("symbolClass"), 
			relationshipClassElement	= symbolElement.getChild("relationshipClass");
		
		
		Context plainSymbol;
		
		// Find the bounding boxes
		List listChildren = boundingBoxElement.getChildren();
		if (listChildren.size()!=4) throw new InvalidInterpretation("the number of coordinates for the bounding box must be 4.");
		else for (int i=0; i<4; i++) boundingBox[i] = Integer.parseInt( ((Element)listChildren.get(i)).getText() );
		
		// Create the symbol
		plainSymbol = new Context(boundingBox);
		
		// Set the id for the symbol
		id = Integer.parseInt(symbolElement.getAttributeValue("id"));
		plainSymbol.setSymbolId(id);
		
		// Get the symbol class distribution
		dist = classDistributionExtraction(symbolClassElement);
		if (dist.length != Parameters.NB_OF_SYMBOL_CLASSES) throw new InvalidInterpretation("there must be "+Parameters.NB_OF_SYMBOL_CLASSES+" symbol classes.");
		else plainSymbol.setClass(dist);
		
		// Get the relationship class distribution
		rdist = classDistributionExtraction(relationshipClassElement);
		rel   = Integer.parseInt(symbolElement.getAttributeValue("rel"));
		if (rdist.length != Parameters.NB_OF_RELATIONSHIP_CLASSES) throw new InvalidInterpretation("there must be "+Parameters.NB_OF_RELATIONSHIP_CLASSES+" relationship classes.");
		else plainSymbol.setRelationship(rdist,rel);
		
		return plainSymbol;
	}

	/**
	 * Extract the class distribution from a class node in an XML document
	 * @param listOfClasses	: class node
	 * @return	an array containing the distribution
	 */
	public static double[] classDistributionExtraction(Element listOfClasses)
	{
		double[] dist;
		
		List classes = listOfClasses.getChildren("class");
		
		dist = new double[classes.size()];
		
		for (int i=0; i<classes.size(); i++)
			dist[i] = Double.parseDouble( ((Element)classes.get(i)).getText() );
		
		return dist;
	}
	
	/**
	 * Exctract the children of a symbol from an XML node
	 * @param symbolElement	: XML node for the parent symbol
	 * @return	a list of symbols beginning with the parent symbol, and continuing with its children, grand-children etc.
	 * @throws InvalidInterpretation
	 */
	public static Vector<Context> extractChildren(Element symbolElement) throws InvalidInterpretation
	{
		// Initialize result
		Vector<Context> result = new Vector<Context>();
		
		// Extract the symbol which children are to be found
		Context parent = extractSymbol(symbolElement);
		
		// Add it to the results
		result.add(parent);
		
		// Retrieve the list of children
		List children = symbolElement.getChildren("symbol");
		// Create iterator on children
		Iterator iterChildren = children.iterator();
		// Define vars
		Element childElement;
		int relationship;
		
		// For all the children
		while (iterChildren.hasNext())
		{
			// Get the child (XML)
			childElement = (Element) iterChildren.next();
			// Get the relationship
			relationship = Integer.parseInt( childElement.getAttributeValue("rel") );
			// Retrieve its children, grand-children, etc
			Vector<Context> nested = extractChildren(childElement);
			// Get the child (bis) (Context)
			Context child = nested.get(0);
			// Set it as a child
			child.setRelationship(
					new Relationship(
							parent, child,
							ArrayTools.crispDist(Parameters.NB_OF_RELATIONSHIP_CLASSES, relationship)
					)
			);
			// Add the list (child, its children & grand-children)
			result.addAll(nested);
		} // next child
		
		// Return the list
		return result;
	}
	
	/**
	 * Extract the expression form an XML file containing only one expression
	 * @param file	: XML file
	 * @return	the expression
	 * @throws JDOMException
	 * @throws IOException
	 * @throws InvalidInterpretation
	 */
	public static Expression extractExpressionFromXML(String file) throws JDOMException, IOException, InvalidInterpretation
	{
		Expression expr;
		
		SAXBuilder sxb = new SAXBuilder();

   	 	Document document = sxb.build(new File(file));
	    Element  root     = document.getRootElement();
	    
	    Element dominantSymbol = root.getChild("symbol");
	    
	    expr = new Expression(extractChildren(dominantSymbol));
	    expr .setDimension(
	    		new Dimension (  
	    				Integer.parseInt( root.getAttributeValue("width") ),
	    				Integer.parseInt( root.getAttributeValue("height"))
	    			)
	    	);
	    
	    return expr;

	}
	
	
	public static Vector<Expression> extractExpressionsFromXML(String file) throws InvalidInterpretation, JDOMException, IOException
	{
		Expression expr;
		Vector<Expression> exprs = new Vector<Expression>();
		
		SAXBuilder sxb = new SAXBuilder();

   	 	Document document = sxb.build(new File(file));
	    Element  root     = document.getRootElement();
	    
	    List xmlExprs = root.getChildren("expression");
	    Iterator iterExpr = xmlExprs.iterator();
	    
	    while (iterExpr.hasNext())
	    {
	    	Element oneExpr = (Element)iterExpr.next();
	    	Element dominantSymbol = oneExpr.getChild("symbol");
	    
		    expr = new Expression(extractChildren(dominantSymbol));
		    expr .setDimension(
		    		new Dimension (  
		    				Integer.parseInt( oneExpr.getAttributeValue("width") ),
		    				Integer.parseInt( oneExpr.getAttributeValue("height"))
		    			)
		    	);
		    
		    exprs.add(expr);
	    }
	   
	    
	    return exprs;
	}
	
	/**
	 * Extract the list of symbol ids from an XML file containing only one expression
	 * @param file	: XML file
	 * @return	a list of ids
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Vector<Integer> extractSymbolIds(String file) throws JDOMException, IOException
	{
		SAXBuilder sxb = new SAXBuilder();

   	 	Document document = sxb.build(new File(file));
	    Element  root     = document.getRootElement();
	    
	    return extractSymbolIdsFromNode(root);
	}
	
	public static Vector<Vector<Integer>> extractAllSymbolIds(String file) throws JDOMException, IOException
	{
		SAXBuilder sxb = new SAXBuilder();

   	 	Document document = sxb.build(new File(file));
	    Element  root     = document.getRootElement();
	    
	    List expressions = root.getChildren("expression");
	    
	    Vector<Vector<Integer>> result = new Vector<Vector<Integer>>();
	    
	    for (Object el : expressions)
	    	result.add( extractSymbolIdsFromNode((Element)el) );
	    
	    return result;

	}
	
	public static Vector<Integer> extractSymbolIdsFromNode(Element e)
	{
		List symbols = e.getChildren("symbol");
	    
	    Vector<Integer> result = new Vector<Integer>();
	    
	    for (Object el : symbols)
	    	result.add( Integer.parseInt( ((Element)el).getAttributeValue("id") ) );
	    
	    return result;
	}

	/**
	 * Create an XML file containing the list of symbols for several expressions.
	 * @param ve	: list of expressions	
	 * @param file	: file to save
	 */
	public static void listsOfSymbolXML(Vector<Expression> ve, String file) {
		Element root = new Element("expressions");
		
		for (Expression e: ve)
			root.addContent(listOfSymbols(e.getSymbols()));
		
		Document doc = new Document(root);
		try {
		    XMLOutputter outputter = new XMLOutputter();
		    Format f = outputter.getFormat();
		    f.setIndent("  "); // use two space indent
		    f.setLineSeparator("\r\n"); 
		    outputter.setFormat(f);
		    outputter.output(doc, new FileWriter(new File(FileManager.get().getWorkspace()+file)));
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
		
	}
}
