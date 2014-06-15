MERStructure
============

Description
-----------

This work was submitted for the completion of the MSc in Computer Science of Oxford University. 
The MSc is a one-year program divided in three parts. 
Courses take place in the first two terms of eight weeks. 
The last part is a research project, carried out for five months. 
I chose the topic and had the privilege to work under Dr. Vasile Palade supervision.

The goal is to recognize simple layouts of mathematical expressions without the symbols
identities. The experiments carried out were on very simple data, and a lot should be improved.

For more details, I suggest to read my dissertation, available on [my website](http://www.tbluche.com/msc.php)

*Abstract of the thesis*

> Unlike texts, printed mathematical expressions have a two-dimensional nature, and their recognition involves 
> the recognition of the structure of the formula and of the symbols contained in it. In most research, 
> the structure recognition uses the identity of symbols. Moreover, a few previous works use the structure in the 
> symbol recognition, or machine learning techniques in the structure recognition part. There is a lot of mathematical 
> symbols, they can be very similar, and new symbols are being invented by scientists. The arrangements of symbols, 
> however, evolve less. In this dissertation, we investigate the possibility of recognizing the structure of the mathematical 
> expression without the symbols' identity. We also perform a prior classification of the symbols using the structure only. 
> Moreover, we use very few knowledge about mathematical expression syntax. This approach has, to the best of our knowledge,
>  never been tried. We built a system using machine learning techniques, such as neural networks and fuzzy logic. 
> A binary image representing a mathematical expression is segmented, and the recognition is performed using the symbols' 
> bounding boxes. An iterative algorithm using a multi-classifier system recognizes the structure and classifies the symbols. 
> The results proved that the context of a symbol, when known and used, can help classify the symbol. 
> The structure recognition using a non-recursive algorithm with very few backtracking yielded good results. 
> This project proved that the symbols' identity is not essential for the structure analysis. Moreover, the structure recognition 
> provides some useful information for the symbol classification. This classification is assumed to help the symbol recognition 
> in a future work. Finally, the machine learning approach produced a flexible system, able to adapt to unknown symbols and writing 
> styles, and to return confidence values for the recognition, rather than a crisp interpretation.


Also, since I did not have releasing the code in mind at the time, I only know it works on my 
Windows laptop from back then. Installation instruction have yet to come then.


License
-------

   * GPLv3


Dependencies
------------

There are several dependencies I remember of, but I don't remember the versions needed.
If you find things which do not work with the latest versions, please report the
issue. Anyhow, I wrote the code in mid-2010, I guess with the latest versions
of the tools listed below at the time. 

If you find dependencies I did't mention, please tell me so I can add it :)

   * JDOM
   * Weka (ML toolkit)
   * jLatexMath


Limitations
-----------

**Image processing**

   * Images should be binary
   * The program considers each connected component as a symbols, which is a problems for things like `i`, `=`, and so on

**Recognition**
 
   * The method classifies the symbols in general classes, but no symbol identity recognition is performed
   * There are also missing classes and relationships
   * There is still many things to do to try different methods for classification, and the indentity recognition should be added

**Experiments**

   * They were carried out on simple syntetic data
   * Improvements of the application scope are necessary to evaluate the method on realistic data / datasets



Contribute!
-----------

I encourage anyone interested by the code to fork it and correct the bugs.
This work was done during my MSc in 2010 and I moved to other research topics,
leaving me little time to maintain the code.


