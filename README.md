# CogTool+


CogTool+ is an open source software inspired by and developed based on CogTool (https://github.com/cogtool/cogtool). 

This open source software is a research outcome of an EPSRC project COMMANDO-HUMANS (http://www.commando-humans.net)

### Examples included in this repository:
---------------------------------------

Two example projects (‘pin_study’ and ‘undercover_study’)  are included in this repository. They are located in the ‘example’ directory.  

1. Brief description of the ‘pin_study’ project is presented below:

	**design** directory contains the following files: 
	
		-> model_pin.xml (Descriptive meta model to describe high level UI and interaction)
		-> mixed_models.xml (Mixed model to define mixed probability model)
		-> simulation.xml (Simulation model to define simulation parameters)
		-> analyse.xml (Model to define visualisation parameters) 

	**js** directory contains the following files:
	
		-> pinSMU.csv (Algorithmic model to work with descriptive meta model)
		
	**output** directory: all intermediate data and results generated during the modelling and simulation process are saved here.

	**configure.xml**: to define simulation configuration

2. Brief description of the ‘undercover_study’ project is presented below:

	**design** directory contains the following files: 
    
		-> simulation.xml (Simulation model to define simulation parameters)
        -> analyse.xml (Model to define visualisation parameters)     
		-> model_CLR_Confirm.xml
		-> model_CLR_Only.xml
		-> model_LR_Confirm.xml
		-> model_LR_Only.xml
		-> model_RL_Confirm.xml
		-> model_RL_Only.xml 
		N.B. The above models are descriptive models based on different 
		visual search behaviors reported in the paper [1].
		
	**js** directory contains the following files:
        -> undercover.js (Algorithmic model to define a number of JavaScript functions to deal with dynamic change of user interface/interaction)

	**output** directory: all intermediate data and results generated during the modelling and simulation process are saved here.

	**configure.xml**: to define simulation configuration

### Runing the example
---------------------------------------

1. Locate CogToolPlusMain.java from cogtool_plus_open_source-master.src.cogtoolplus
2. Update the below three variables and run this main java application.
	
		String conf_file = "YOUR_OWN_PATH/example/undercover_study/configure.xml";
		String prj_file = "mixed_models.xml";
		String vis_file = "analyse.xml";

#### N.B.
---------------------------------------

* Make sure to add jar files included in the ‘third party lib’ directory to the project as External JARs.
* CogTool+ is developed and tested using Eclipse version eclipse-java-mars-2-win32
* More details can be found in the paper 'CogTool+: Modeling human performance at large scale' [2]
* User-manual is work-in-progress
#### Reference
---------------------------------------

#### 


[1] Yuan H., Li S., Rusconi P., and Aljaffan N., "[When Eye-tracking Meets Cognitive Modeling: Applications to Cyber Security Systems](https://epubs.surrey.ac.uk/813689/1/eye-tracking-meets.pdf)," in Human Aspects of Information Security, Privacy and Trust: 5th International Conference, HAS 2017, Held as Part of HCI International 2017, Vancouver, BC, Canada, July 9-14, 2017, Proceedings, Lecture Notes in Computer Science, vol. 10292, pp. 251-264, 2017 

[2] Yuan H., Li S., and Rusconi P. 2021. CogTool+: Modeling Human Performance at Large Scale. ACM Trans. Comput.-Hum. Interact. 28, 2, Article 7 (April 2021), 38 pages. DOI:https://doi.org/10.1145/3447534
