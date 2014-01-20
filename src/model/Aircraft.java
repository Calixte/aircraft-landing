package model;
import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.exception.ContradictionException;
import solver.search.strategy.IntStrategyFactory;
import solver.search.strategy.decision.Decision;
import solver.search.strategy.strategy.AbstractStrategy;
import solver.search.strategy.strategy.Assignment;
import solver.search.strategy.strategy.StrategiesSequencer;
import solver.variables.IntVar;
import solver.variables.Task;
import solver.variables.Variable;
import solver.variables.VariableFactory;
import util.tools.ArrayUtils;


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
	protected IntVar<?>[][] plane_weight;
	
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
		
		s.set(new StrategiesSequencer(
				IntStrategyFactory.random(ArrayUtils.flatten(plane_weight), System.currentTimeMillis()),
				IntStrategyFactory.random(parking, System.currentTimeMillis()),
				IntStrategyFactory.random(landing, System.currentTimeMillis()),
				IntStrategyFactory.random(take_off, System.currentTimeMillis())
				));
	}
	
	public void solve() {
		solver=new Solver();
		model(solver);
		solver.findSolution();
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
}
