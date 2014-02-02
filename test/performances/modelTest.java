package performances;

import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;
import generator.DataGenerator;
import model.CSPModel;
import model.Plane;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Sabrine DJEDIDI
 *
 */
public class modelTest {
    private static final int nbRuns = 2;
    private static FileWriter writer;
    private static long[][] stats;
    private static final int[] nbFlights={20,50,100,200,350};
    private static final int[] difficulty={DataGenerator.EASY,DataGenerator.MID,DataGenerator.HARD};
    
    @BeforeClass
    public static void setUpBeforeClass(){
	stats=new long[nbRuns][3];
	try {
	    writer = new FileWriter("target\\output"+System.currentTimeMillis()+".csv");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
	writer.flush();
	writer.close();
    }

   @Test
    public void mainTest(){
	for(int i=0;i<3;i++){
	    for(int j=0;j<5;j++){
		for(int k=0;k<nbRuns;k++){
		    test(difficulty[i],nbFlights[j],k);
		}
		addToCsvFile(stats, difficulty[i],nbFlights[j]);
	    }
	}
    }
    
    public void test(int difficulty ,int nbOfFlights,int run) {
	DataGenerator generator = new DataGenerator();
	Plane[] planes = new Plane[nbOfFlights];
	planes = generator.generateRandom(nbOfFlights, difficulty);
	CSPModel cSPModel = new CSPModel(planes, new int[] { 6, 5, 3, 3, 3, 4,2, 1, 1 }, 1200,100);
	stats[run][0]=cSPModel.getMeasures().getSolutionCount();
	stats[run][1]=cSPModel.getMeasures().getBackTrackCount();
	stats[run][2]=(long) cSPModel.getMeasures().getTimeCount();
    }
    
    public void addToCsvFile(long[][] tab,int difficulty ,int nbOfFlights)
    {
 	try
 	{
 	    for(int i=0;i<3;i++){
 		for(int j=0;j<5;j++){
         	    writer.append("Generator : Random"+','+"Difficulty : "+difficulty+','+"Nb Planes : "+nbOfFlights);
         	    writer.append('\n');
         	    writer.append("Resolved"+','+"Backtracks"+','+"Time");
        	    writer.append('\n');
        	    for(int k=0;k<nbRuns;k++){
        		writer.append(""+tab[k][0]+','+tab[k][1]+','+tab[k][2]);
             	    	writer.append('\n');
        	    }            
 		}
 	    }
 	}
 	catch(IOException e)
 	{
 	     e.printStackTrace();
 	} 
     }
}
