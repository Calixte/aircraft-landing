import generator.DataGenerator;
import model.Aircraft;
import model.Plane;

public class Main {
	public static void main(String[] args) {
		Plane[] planes = new DataGenerator().generateLinear(20, DataGenerator.MID);
		new Aircraft(planes, new int[]{6, 5, 3, 2, 7, 6, 4, 1, 1}, 1200).solve();
	}
}
