package model;
import java.util.ArrayList;
import java.util.List;

import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.constraints.LogicalConstraintFactory;
import solver.search.loop.monitors.SMF;
import solver.search.strategy.IntStrategyFactory;
import solver.search.strategy.strategy.StrategiesSequencer;
import solver.variables.IntVar;
import solver.variables.Task;
import solver.variables.VariableFactory;
import util.tools.ArrayUtils;

/**
 * 
 * @author calixtebonsart
 *
 * This class builds the CSP model of the problem
 */
public class CSPModel {

	protected int closing_time;
	protected int nb_of_planes;
	protected int nb_of_runways;
	protected int[] runway_max_capacity;
	protected Plane[] planes;

	protected IntVar<?>[] parking;
	protected IntVar<?>[] landing;
	protected IntVar<?>[] take_off;
	protected Task[] stay;
	/**
	 * plane_weight[i][j] gives the weight of the plane i if i is on the runway j or 0 if not
	 */
	protected IntVar<?>[][] plane_weight;
	
	protected Solver solver;
	
	/**
	 * 
	 * @param nb_of_planes number of planes
	 * @param runway_capacity array of capacities of all runways
	 * @param closing_time when the airport closes
	 */
	public CSPModel(Plane[] planes, int[] runway_capacity, int closing_time){
		this.closing_time=closing_time;
		this.runway_max_capacity = runway_capacity;
		this.planes = planes;
		
		this.nb_of_runways=runway_capacity.length;
		this.nb_of_planes=planes.length;
		this.solve();
	}
	
	/**
	 * Build the model
	 * @param s solver
	 */
	private void model(Solver s){
		this.stay = new Task[this.nb_of_planes];
		this.landing = new IntVar<?>[this.nb_of_planes];
		this.parking = new IntVar<?>[this.nb_of_planes];
		this.take_off = new IntVar<?>[this.nb_of_planes];
		this.plane_weight = new IntVar[nb_of_planes][nb_of_runways];
		
		for(int i=0;i<this.nb_of_planes;i++){
			/**
			 * Ensure that a plane i stays parking[i] time between landing[i] and take_off[i]
			 */
			landing[i] = VariableFactory.enumerated("landing"+i, planes[i].getLanding(), planes[i].getTakeoff(), s);
			take_off[i] = VariableFactory.enumerated("takeoff"+i, planes[i].getLanding(), planes[i].getTakeoff(), s);
			parking[i] = VariableFactory.enumerated("parking"+i, new int[]{30}, s);
			stay[i] = VariableFactory.task(landing[i], parking[i], take_off[i]);
			
			/**
			 * Ensure plane_weight is either equal to 0 or the weight of the plane i
			 */
			for(int j = 0 ; j < nb_of_runways ; j++) {
				this.plane_weight[i][j] = VariableFactory.enumerated("pw"+j+i, new int[]{0, planes[i].getWeight()}, s);
			}
			
			/**
			 * Ensure that the plane i is only on one runway
			 */
			s.post(IntConstraintFactory.count(planes[i].getWeight(), this.plane_weight[i], VariableFactory.fixed(1, s)));
		}
		
		/**
		 * Ensure that a plane i takes off after a plane j if he landed after
		 */
		for(int i = 0 ; i < this.nb_of_planes ; i++) {
			for(int j = i+1 ; j < nb_of_planes ; j++) {
				s.post(LogicalConstraintFactory.ifThenElse(
						IntConstraintFactory.arithm(landing[i], "<=", landing[j]), 
						IntConstraintFactory.arithm(take_off[i], "<=", take_off[j]),
						IntConstraintFactory.arithm(take_off[i], ">=", take_off[j])));
			}
		}
		
		/**
		 * Ensure that the cumulated weights of all planes on runway i does not exceed its capacity
		 */
		for(int i = 0 ; i < this.nb_of_runways ; i++) {
			s.post(IntConstraintFactory.cumulative(stay, ArrayUtils.transpose(plane_weight)[i], VariableFactory.fixed(runway_max_capacity[i], s)));
		}
		
		/**
		 * Strategy
		 */
		s.set(new StrategiesSequencer(
				IntStrategyFactory.inputOrder_InDomainMin(landing),
				IntStrategyFactory.inputOrder_InDomainMin(parking),
				IntStrategyFactory.inputOrder_InDomainMin(take_off),
				IntStrategyFactory.random(ArrayUtils.flatten(plane_weight), System.currentTimeMillis())
				));
	}
	
	/**
	 * Instantiate and launch the solver
	 */
	private void solve() {
		solver=new Solver();
		model(solver);
		SMF.limitTime(solver, 10000);
		solver.findSolution();
	}

	/**
	 * Update the initial object with the solution
	 */
	public void updatePlaneArray() {
		for (int i = 0; i < nb_of_planes; i++) {
			this.planes[i].setLanding(landing[i].getLB());
			this.planes[i].setTakeoff(take_off[i].getLB());
		}
	}
	
	/**
	 * 
	 * @param id number of the runway
	 * @return a list of all planes on runway id
	 */
	public List<Plane> getPlaneForRunway(int id) {
		List<Plane> planes = new ArrayList<>();
		for (int i=0; i<nb_of_planes; i++) {
			if (plane_weight[i][id].getLB() != 0) {
				planes.add(this.planes[i]);
			}
		}
		return planes;
	}
	
	/**
	 * 
	 * @param id number of the runway
	 * @return the capacity of a runway id
	 */
	public int getRunwayCapacity(int id) {
		return runway_max_capacity[id];
	}
	
	/**
	 * 
	 * @return the number of runway
	 */
	public int getNbOfRunways() {
		return this.nb_of_runways;
	}
	
	public int getMaxRunwayCapacity() {
		int max = 0;
		for (int i = 0; i<this.nb_of_runways; i++) {
			if (runway_max_capacity[i] > max) {
				max = runway_max_capacity[i];
			}
		}
		return max;
	}
}
