import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.variables.IntVar;
import solver.variables.Task;
import solver.variables.VariableFactory;


public class Aircraft {
	
	protected int opening_time; 
	protected int closing_time;
	protected int nb_of_planes;
	protected int nb_of_runways;
	protected int[] runway_max_capacity;
	protected Plane[] planes;

	protected IntVar<?>[] parking;
	protected IntVar<?>[] landing;
	protected IntVar<?>[] take_off;
	protected Task[] plane;
	protected IntVar<?>[][] runway_usage;
	
	protected Solver solver;
	
	/**
	 * 
	 * @param nb_of_planes number of planes
	 * @param runway_capacity array of capacities of all runways
	 * @param opening_time when the airport opens
	 * @param closing_time when the airport closes
	 */
	public Aircraft(Plane[] planes, int[] runway_capacity, int opening_time, int closing_time){
		this.opening_time=opening_time;
		this.closing_time=closing_time;
		this.runway_max_capacity = runway_capacity;
		this.planes = planes;
		
		this.nb_of_runways=runway_capacity.length;
		this.nb_of_planes=planes.length;
	}
	
	public void model(Solver s){
		this.plane = new Task[this.nb_of_planes];
		
		this.landing = new IntVar<?>[this.nb_of_planes];
		this.parking = new IntVar<?>[this.nb_of_planes];
		this.take_off = new IntVar<?>[this.nb_of_planes];
		
		this.runway_usage= new IntVar<?>[this.nb_of_runways][];

		for(int i=0;i<this.nb_of_planes;i++){
			landing[i] = VariableFactory.enumerated("landing"+i, planes[i].getLanding(), planes[i].getTakeoff(), s);
			take_off[i] = VariableFactory.enumerated("takeoff"+i, planes[i].getLanding(), planes[i].getTakeoff(), s);
			parking[i] = VariableFactory.enumerated("parking"+i, 0, planes[i].getDuration(), s);
			plane[i]=VariableFactory.task(landing[i], parking[i], take_off[i]);
		}
		for(int i = 0 ; i < this.nb_of_runways;i++){
			this.runway_usage[i]=VariableFactory.enumeratedArray("runway_usage"+i, this.closing_time-this.opening_time, 0, runway_max_capacity[i], s);
			s.post(IntConstraintFactory.cumulative(plane, Plane.weights(planes), runway_max_capacity));
		}
	}
	
	public void solve() {
		solver=new Solver();
		model(solver);
		solver.findSolution();
	}
}
