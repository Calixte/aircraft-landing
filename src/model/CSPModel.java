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


public class CSPModel {
	
	protected int closing_time;
	protected int nb_of_planes;
	protected int nb_of_runways;
	protected int[] runway_max_capacity;
	protected Plane[] planes;

	protected IntVar<?>[] parking;
	protected IntVar<?>[] landing;
	protected IntVar<?>[] take_off;
	protected Task[] plane;
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
	}
	
	public void model(Solver s){
		this.plane = new Task[this.nb_of_planes];
		
		this.landing = new IntVar<?>[this.nb_of_planes];
		this.parking = new IntVar<?>[this.nb_of_planes];
		this.take_off = new IntVar<?>[this.nb_of_planes];
		
		for(int i=0;i<this.nb_of_planes;i++){
			landing[i] = VariableFactory.enumerated("landing"+i, planes[i].getLanding(), planes[i].getTakeoff(), s);
			take_off[i] = VariableFactory.enumerated("takeoff"+i, planes[i].getLanding(), planes[i].getTakeoff(), s);
			parking[i] = VariableFactory.enumerated("parking"+i, new int[]{30}, s);
			plane[i] = VariableFactory.task(landing[i], parking[i], take_off[i]);
		}
				
		this.plane_weight = new IntVar[nb_of_planes][nb_of_runways];
		for(int i = 0 ; i < nb_of_runways ; i++) {
			for(int j = 0 ; j < nb_of_planes ; j++) {
				this.plane_weight[j][i] = VariableFactory.enumerated("pw"+i, new int[]{0, planes[j].getWeight()}, s);
			}
		}
		
		for(int i = 0 ; i < nb_of_planes ; i++) {
			s.post(IntConstraintFactory.count(planes[i].getWeight(), this.plane_weight[i], VariableFactory.fixed(1, s)));
		}
		
		for(int i = 0 ; i < this.nb_of_runways ; i++) {
			s.post(IntConstraintFactory.cumulative(plane, ArrayUtils.transpose(plane_weight)[i], VariableFactory.fixed(runway_max_capacity[i], s)));
		}
		
		for(int i = 0 ; i < this.nb_of_planes ; i++) {
			for(int j = 0 ; j < nb_of_planes ; j++) {
				s.post(LogicalConstraintFactory.ifThenElse(
						IntConstraintFactory.arithm(landing[i], "<=", landing[j]), 
						IntConstraintFactory.arithm(take_off[i], "<=", take_off[j]),
						IntConstraintFactory.arithm(take_off[i], ">=", take_off[j])));
			}
		}
		
		s.set(new StrategiesSequencer(
				IntStrategyFactory.inputOrder_InDomainMin(landing),
				IntStrategyFactory.inputOrder_InDomainMin(parking),
				IntStrategyFactory.inputOrder_InDomainMin(take_off),
				IntStrategyFactory.random(ArrayUtils.flatten(plane_weight), System.currentTimeMillis())
				));
	}
	
	public void solve() {
		solver=new Solver();
		model(solver);
		SMF.limitTime(solver, 10000);
		solver.findSolution();
		//this.prettyOut();
	}
	
	public void prettyOut() {
		System.out.println(solver.getMeasures());
		for(int i = 0 ; i < nb_of_planes ; i++) {
			System.out.print(landing[i].getLB() + "\t" + take_off[i].getLB() + "\t" + planes[i].getWeight());
			for(int j = 0 ; j < plane_weight[i].length ; j++) {
				if(plane_weight[i][j].getLB() != 0) {
					System.out.print("\t" + j);
				}
			}
			System.out.println();
		}
	}
	
	public void updatePlaneArray() {
		for (int i = 0; i < nb_of_planes; i++) {
			this.planes[i].setLanding(landing[i].getLB());
			this.planes[i].setTakeoff(take_off[i].getLB());
		}
	}
	
	public List<Plane> getPlaneForRunway(int id) {
		List<Plane> planes = new ArrayList<>();
		for (int i=0; i<nb_of_planes; i++) {
			if (plane_weight[i][id].getLB() != 0) {
				planes.add(this.planes[i]);
			}
		}
		return planes;
	}
	
	public int getRunwayCapacity(int id) {
		return runway_max_capacity[id];
	}
	
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
	
	public long getNbSolutions() {
		return solver.getMeasures().getSolutionCount();
	}
}
