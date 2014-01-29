package model;

import solver.Solver;
import solver.variables.IntVar;
import solver.variables.VariableFactory;

public class Plane{
	
	public static final int LIGHT = 1;
	public static final int MEDIUM = 2;
	public static final int HEAVY = 3;
	
	private int weight;
	private int takeoff;
	private int landing;
			
	/**
	 * Create a stay
	 * @param w Weight of the stay
	 * @param landing Beginning of the landing window
	 * @param takeoff End of the takeoff window
	 */
	public Plane(int w, int landing, int takeoff){
		this.weight=w;
		this.takeoff = takeoff;
		this.landing = landing;
	}
	
	/**
	 * 
	 * @return the weight of the stay
	 */
	public int getWeight() {
		 return weight;
	}
	
	/**
	 * 
	 * @return the end of the takeoff window
	 */
	public int getTakeoff() {
		return takeoff;
	}

	/**
	 * 
	 * @return the beginning of the landing window
	 */
	public int getLanding() {
		return landing;
	}
	
	/**
	 * 
	 * @return the maximum duration of the stay
	 */
	public int getDuration() {
		return takeoff-landing;
	}
	
	/**
	 * @param takeoff the takeoff to set
	 */
	public void setTakeoff(int takeoff) {
		this.takeoff = takeoff;
	}

	/**
	 * @param landing the landing to set
	 */
	public void setLanding(int landing) {
		this.landing = landing;
	}

	public static IntVar<?>[] weights(Plane[] planes, Solver s) {
		IntVar<?>[] weights = new IntVar[planes.length];
		for(int i = 0 ; i < planes.length ; i++) {
			weights[i] = VariableFactory.fixed(planes[i].getWeight(), s);
		}
		return weights;
	}
	
	@Override
	public String toString() {
		return "Plane[weight : " + weight + ", landing : " + landing + ", takeoff : " + takeoff + "]";
	}
}
