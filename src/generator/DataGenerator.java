/**
 * 
 */
package generator;

import java.util.Random;

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
	public static final int MID = 11;
	public static final int HARD = 12;

	public Plane[] generateLinear(int numberOfFlights, int difficulty) {
		Plane[] planes = new Plane[numberOfFlights];
		int window = LANDING_WINDOW_END / numberOfFlights;
		Random random = new Random();
		for (int i = 0; i < numberOfFlights; i++) {
			switch (difficulty) {
			case EASY:
				planes[i] = new Plane(Plane.LIGHT, i * window, Math.min(i
						* window + MAX_PARKING_TIME, HIGHEST_TIME));
				break;
			case MID:
				int weight = random.nextInt(2) + 1;
				planes[i] = new Plane(weight, i * window, Math.min(i * window
						+ MAX_PARKING_TIME, HIGHEST_TIME));
				break;
			case HARD:
				weight = random.nextInt(3) + 1;
				planes[i] = new Plane(weight, i * window, Math.min(i * window
						+ MAX_PARKING_TIME, HIGHEST_TIME));
				break;

			default:
				planes[i] = new Plane(1, i * window, Math.min(i * window
						+ MAX_PARKING_TIME, HIGHEST_TIME));
				break;
			}
		}
		return planes;
	}

	public Plane[] generateRandom(int numberOfFlights, int difficulty) {
		Plane[] planes = new Plane[numberOfFlights];
		Random random = new Random();
		for (int i = 0; i < numberOfFlights; i++) {
			switch (difficulty) {
			case EASY:
				int landing = random.nextInt(LANDING_WINDOW_END + 1);
				planes[i] = new Plane(Plane.LIGHT, landing, Math.min(landing
						+ MAX_PARKING_TIME, HIGHEST_TIME));
				break;

			default:
				landing = random.nextInt(LANDING_WINDOW_END + 1);
				planes[i] = new Plane(Plane.LIGHT, landing, Math.min(landing
						+ MAX_PARKING_TIME, HIGHEST_TIME));
				break;
			}
		}

		return planes;
	}

	public static void main(String[] args) {
		DataGenerator generator = new DataGenerator();
		Plane[] planes = generator.generateRandom(5, EASY);
		for (Plane plane : planes) {
			System.out.println(plane);
		}
	}
}
