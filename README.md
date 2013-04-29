Welcome to the NCI EVS Value Set Editor Project!
==============================

The NCI EVS Value Set Editor is used to create and maintain CTS 2 value set and pick list definitions for
loading into the LexEVS terminology server, which resolves the definitions against referenced terminologies. 
This initial prototype release is used internally to generate all value sets published by EVS, but will need
further development to reach a broader user community.  

The key capabilities of EVS Value Set Editor include the following:
* Create a new value set (i.e., LexEVS value set definition) from scratch.
*	Clone (i.e., make a copy) of a value set based on an existing one available on the server.
*	Create and maintain metadata of a value set.
*	Construct component value sets. A component value set may be composed of a collection of concepts in a particular terminology, or a subset of concepts in a terminology sharing the same property value according to a user specified matching algorithm, or a subset of concepts in a terminology sharing the same relationship with a focus concept according to a user specified matching algorithm, or the entire set of concepts in a given terminology, or an existing value set on the server
*	Define a value set by an expression using value set components as the operands and set union, set interception, and set difference as binary operators.
*	Resolve the value set definition constructed above.
*	Export the value set definition to a file in LexGrid XML format.  Such files can be reviewed and published through the terminology server


NCI EVS Value Set Editor is developed in Java and requires Apache Ant for building.

NCI EVS Value Set Editor is distributed under the BSD 3-Clause License.
Please see the NOTICE and LICENSE files for details.

You will find more details about NCI EVS Value Set Editor in the following links:

 * [Code Repository] (https://github.com/NCIP/nci-value-set-editor)
 * [Issue Tracker] (https://tracker.nci.nih.gov/browse/NCIVSA) 
 
Please join us in further developing and improving NCI EVS Value Set Editor.
