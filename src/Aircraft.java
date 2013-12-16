import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import solver.Solver;
import solver.variables.IntVar;
import solver.variables.Task;
import solver.variables.VariableFactory;


public class Aircraft {

	protected IntVar[] parking;
	protected IntVar[] landing;
	protected IntVar[] take_off;
	protected Task[] plane;
	protected IntVar[] runway_capacity;
	protected IntVar[] plane_weight;
	protected IntVar open_airport;
	protected int parking_min_time;
	protected int parking_max_time;
	protected int open_hours; 
	protected int close_hours;
	protected int nb_of_planes;
	protected int nb_of_runways;
	protected Solver solver;
	
	public Aircraft(int planes, int runways, int[] rw_cap, int min_time, int max_time, int open, int close){
		solver=new Solver();
		this.open_airport=VariableFactory.enumerated("opening_hours", open, close, solver);
		this.nb_of_planes=planes;
		this.open_hours=open;
		this.close_hours=close;
		this.parking_min_time=min_time;
		this.parking_max_time=max_time;
		this.plane=new Task[this.nb_of_planes];
		this.landing=new IntVar[this.nb_of_planes];
		this.parking=new IntVar[this.nb_of_planes];
		this.take_off=new IntVar[this.nb_of_planes];
		this.nb_of_runways=runways;
		this.runway_capacity=VariableFactory.enumeratedArray("runway_capacity", this.nb_of_runways, rw_cap, solver);
	}
	
	public void model(){
		for(int i=0;i<this.nb_of_planes;i++){
			landing[i]=VariableFactory.enumerated("landing"+i, open_hours, close_hours,solver);
			parking[i]=VariableFactory.enumerated("parking"+i, parking_min_time, parking_max_time,solver);
			take_off[i]=VariableFactory.enumerated("take_off"+i, open_hours, close_hours,solver);
			plane[i]=VariableFactory.task(landing[i], parking[i], take_off[i]);
			plane_weight[i]=VariableFactory.fixed("plane_weight"+i, Plane.[i], s);
		}
	}

	public enum Plane{
		SMALL(1),MEDIUM(2),LARGE(3);
		
		private int weight;
		private static final Map<Integer,Plane> lookup = new HashMap<Integer,Plane>();
		
		static {
	        for(Plane p : EnumSet.allOf(Plane.class))
	            lookup.put(p.getWeight(), p);
	    }
		
		private Plane(int w){
			this.weight=w;
		}
		
		public int getWeight() {
			 return weight;
		}
		
		public static Plane getPlane(int weight) { 
		        return lookup.get(weight); 
		}
	}
	
}
