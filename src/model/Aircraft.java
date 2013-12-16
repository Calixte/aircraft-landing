package model;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.variables.IntVar;
import solver.variables.Task;
import solver.variables.VariableFactory;


public class Aircraft {
	
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
	protected IntVar<?>[][] runway_usage_t;
	protected IntVar<?>[] which_runway;
	
	protected Solver solver;
	
	/**
	 * 
	 * @param nb_of_planes number of planes
	 * @param runway_capacity array of capacities of all runways
	 * @param closing_time when the airport closes
	 */
	public Aircraft(Plane[] planes, int[] runway_capacity, int closing_time){
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
		
		for(int i=0;i<this.nb_of_planes;i++){
			landing[i] = VariableFactory.enumerated("landing"+i, planes[i].getLanding(), planes[i].getTakeoff(), s);
			take_off[i] = VariableFactory.enumerated("takeoff"+i, planes[i].getLanding(), planes[i].getTakeoff(), s);
			parking[i] = VariableFactory.enumerated("parking"+i, 0, planes[i].getDuration(), s);
			plane[i]=VariableFactory.task(landing[i], parking[i], take_off[i]);
		}
		
		this.which_runway = VariableFactory.enumeratedArray("which_runway", nb_of_planes, 1, nb_of_runways, s);
        this.runway_usage= new IntVar<?>[this.nb_of_runways][];

		for(int i = 0 ; i < this.nb_of_runways;i++){
			this.runway_usage[i]=VariableFactory.enumeratedArray("runway_usage"+i, this.closing_time, 0, runway_max_capacity[i], s);
		}
		runway_usage_t = new IntVar<?>[this.closing_time][nb_of_runways];
		for(int i = 0 ; i < this.nb_of_planes ; i++) {
			for(int j = 0 ; j < this.closing_time ; j++) {
				runway_usage_t[j][i] = runway_usage[i][j];
			}
		}
		for(int i = 0 ; i < closing_time ; i++) {
			s.post(IntConstraintFactory.bin_packing(which_runway, Plane.weights(planes), runway_usage_t[i], 0));
		}

	}
	
	public void solve() {
		solver=new Solver();
		model(solver);
		solver.findSolution();
	}
}
