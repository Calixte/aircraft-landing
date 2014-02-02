aircraft-landing
================
###Introduction
This project aims to create a model of the daily aircraft-landing procedure using Constraint Programming.

###Usage
To launch the GUI run the main method which is in src/main/Main.java  
To see the interface, open a browser and go to : http://localhost:4567/ 
After that just follow the instructions. 

Before generating data, you need to set four parameters :
* TYPE : the distribution of the weights of the airplanes "linéaire" for linear or "aléatoire" for random
* LEVEL OF DIFFICULTY :
	* EASY : all the planes are of weight 1 
	* MEDIUM : planes can be either of weight 1 or 2
	* HARD : planes can be either of weight 1, 2 or 3
* NUMBER OF AIRPLANES : from 10 to 350 
* COMPUTING TIMEOUT : time after which the algorithm should be stopped. 
					Avoids the algorithm running for too long when there is no solution, for instance.

Press : "Générer et Optimiser" to generate the data and launch the solving algorithm.

The output is displayed as 2 graphs per runway. 
* The first one displays the load (sum of the planes' weights at every moment). 
* On the second one you can see the time frames of each plane on the runway at hand.
