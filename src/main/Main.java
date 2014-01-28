package main;

import static spark.Spark.get;

import java.io.File;
import java.util.List;

import spark.Request;
import spark.Response;
import spark.Route;
import view.MainFrame;
import generator.DataGenerator;
import model.Aircraft;
import model.Plane;

public class Main {

	private static Plane[] planes;
	private static Aircraft aircraft;

	public static void main(String[] args) {

		get(new Route("/") {

			@Override
			public Object handle(Request request, Response response) {
				String page = MainFrame.HEADER;
				page += MainFrame.parseFile(new File(MainFrame.INDEX_ROUTE));
				response.type("text/html");
				return page;
			}
		});

		get(new Route("/generate") {

			@Override
			public Object handle(Request request, Response response) {
				DataGenerator generator = new DataGenerator();
				String type = request.queryParams("generatorType");
				int difficulty = Integer.parseInt(request
						.queryParams("generatorDifficulty"));
				int nbOfFlights = Integer.parseInt(request
						.queryParams("generatorNbPlanes"));
				System.out.println("Params : " + type + ", " + difficulty
						+ ", " + nbOfFlights);
				planes = new Plane[nbOfFlights];
				switch (type) {
				case DataGenerator.LINEAR:
					System.out.println("Through here");
					planes = generator.generateLinear(nbOfFlights, difficulty);
					break;
				case DataGenerator.RANDOM:
					planes = generator.generateRandom(nbOfFlights, difficulty);
				}
				aircraft = new Aircraft(planes, new int[] { 6, 5, 3, 3, 3, 4,
						2, 1, 1 }, 1200);
				aircraft.solve();
				aircraft.updatePlaneArray();
				int nbOfRunways = aircraft.getNbOfRunways();

				String page = MainFrame.HEADER;
				page += MainFrame.parseFile(new File(MainFrame.INDEX_ROUTE));
				for (int i = 0; i < nbOfRunways; i++) {
					page += MainFrame.createRunway(i + 1);
				}
				page += "</body>";
				response.type("text/html");

				return page;
			}
		});

		get(new Route(MainFrame.GRAPH_SCRIPT_ROUTE) {

			@Override
			public Object handle(Request request, Response response) {
				String answer = MainFrame.parseFile(new File("js/graph.js"));
				response.type("text/javascript");
				return answer;
			}
		});

		get(new Route(MainFrame.STYLESHEET_ROUTE) {

			@Override
			public Object handle(Request request, Response response) {
				String stylesheet = MainFrame.parseFile(new File(
						MainFrame.STYLESHEET_ROUTE));
				response.type("text/css");
				return stylesheet;
			}
		});
		get(new Route(MainFrame.BOOTSTRAP_ROUTE) {

			@Override
			public Object handle(Request request, Response response) {
				String stylesheet = MainFrame.parseFile(new File(
						MainFrame.BOOTSTRAP_ROUTE));
				response.type("text/css");
				return stylesheet;
			}
		});

		get(new Route("/graph/:id") {

			@Override
			public Object handle(Request request, Response response) {
				int id = Integer.parseInt(request.params(":id"));
				List<Plane> planes = aircraft.getPlaneForRunway(id);
				String data = "{\"data\" : [[\"Position\",\"Flight\", \"landing\", \"take off\"],";
				for (int i = 0; i < planes.size(); i++) {
					data += "[\"Runway n°" + (id+1) + "\",\"AF" + (i + id) + "\", "
							+ planes.get(i).getLanding() + ", "
							+ planes.get(i).getTakeoff() + "],";
				}
				data = data.substring(0, data.length() - 1);
				data += ",[\"Runway n°" + (id+1) + "\",\"Fermeture\",1080,1080]]}";
				System.out.println("Runway n°" + id);
				System.out.println("Number of planes : " + planes.size());
				System.out.println(data);
				return data;
			}
		});
	}
}
