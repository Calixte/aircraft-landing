package performances;

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;

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
    private final int nbRuns = 100;
    private int[][][] statsBacktracks;
    private int[][][] statsTime;

    @BeforeClass
    public void setUpBeforeClass(){
	statsTime = new int[3][5][nbRuns];
	statsBacktracks=new int[3][5][nbRuns];
    }
    
    /**Generates the csv 
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
	
    }

    @Test
    public void mainTest(){
	for(int i=0;i<3;i++){
	    for(int j=0;j<5;j++){
		for(int k=0;k<nbRuns;k++){
		    test(i,j);
		}
	    }
	}
    }
    
    public void test(int difficulty ,int nbOfFlights) {
	DataGenerator generator = new DataGenerator();
	Plane[] planes = new Plane[nbOfFlights];
	planes = generator.generateRandom(nbOfFlights, difficulty);
	CSPModel cSPModel = new CSPModel(planes, new int[] { 6, 5, 3, 3, 3, 4,2, 1, 1 }, 1200,100);
	// fail("Not yet implemented");
    }
    
    private static void generateCsvFile(int[][][] tab)
    {
 	try
 	{
 	    FileWriter writer = new FileWriter("target\\output"+System.currentTimeMillis()+".csv");
 	    
 	    for(int i=0;i<3;i++){
 		for(int j=0;j<5;j++){
         	    writer.append("Difficulty ");
         	    writer.append(',');
         	    writer.append("Age");
         	    writer.append('\n');
            
 		}
 	    }
 	    //generate whatever data you want
  
 	    writer.flush();
 	    writer.close();
 	}
 	catch(IOException e)
 	{
 	     e.printStackTrace();
 	} 
     }
}
