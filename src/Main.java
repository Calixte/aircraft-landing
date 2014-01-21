import generator.DataGenerator;
import model.Aircraft;
import model.Plane;

public class Main {
	public static void main(String[] args) {
		Plane[] planes = new DataGenerator().generateRandom(100, DataGenerator.HARD);
		new Aircraft(planes, new int[]{6, 5, 3, 3, 3, 4, 2, 1, 1}, 1200).solve();
	}
}
