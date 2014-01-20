/**
 * 
 */
package generator;

import model.Plane;

/**
 * @author Adrien
 * 
 */
public class DataGenerator {

	/**
	 * First minute of the day.
	 */
	public static final int LOWEST_TIME = 0;
	/**
	 * Last minute of the day.
	 */
	public static final int HIGHEST_TIME = 1080;
	/**
	 * Number of minutes while planes can land.
	 */
	public static final int LANDING_WINDOW_END = 1050;
	
	public static final int MIN_PARKING_TIME = 30;
	public static final int MAX_PARKING_TIME = 270;
	
	public static final int EASY = 10;
	public static final int MEDIUM = 11;
	public static final int HARD = 12;

	public Plane[] generateLinear(int numberOfFlights, int weightDifficulty) {
		Plane[] planes = new Plane[numberOfFlights];
		int window = LANDING_WINDOW_END / numberOfFlights;
		for (int i = 0; i < LANDING_WINDOW_END; i ++) {
			switch (weightDifficulty) {
			case EASY:
				planes[i] = new Plane(1,i*window,i*window + MAX_PARKING_TIME);
				break;

			default:
				planes[i] = new Plane(1,i*window,i*window + MAX_PARKING_TIME);
				break;
			}
		}
		return planes;
	}
}
