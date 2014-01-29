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

	private static int identifier = 100;
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
				identifier = 100;
				DataGenerator generator = new DataGenerator();
				String type = request.queryParams("generatorType");
				int difficulty = Integer.parseInt(request
						.queryParams("generatorDifficulty"));
				int nbOfFlights = Integer.parseInt(request
						.queryParams("generatorNbPlanes"));
				planes = new Plane[nbOfFlights];
				switch (type) {
				case DataGenerator.LINEAR:
					planes = generator.generateLinear(nbOfFlights, difficulty);
					break;
				case DataGenerator.RANDOM:
					planes = generator.generateRandom(nbOfFlights, difficulty);
				}
				aircraft = new Aircraft(planes, new int[] { 6, 5, 3, 2, 7, 6,
						4, 1, 1 }, 1200);
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
				int runwayCapacity = aircraft.getRunwayCapacity(id);
				List<Plane> planes = aircraft.getPlaneForRunway(id);
				String data = "{\"graphs\":[{\"Timeline\" : [[\"Position\",\"Flight\", \"landing\", \"take off\"],";
				for (int i = 0; i < planes.size(); i++) {
					data += "[\"Runway n°" + (id + 1) + "\",\"BA" + identifier
							+ "\", " + planes.get(i).getLanding() + ", "
							+ planes.get(i).getTakeoff() + "],";
					identifier++;
				}
				data = data.substring(0, data.length() - 1);
				data += ",[\"Runway n°" + (id + 1)
						+ "\",\"Fermeture\",1080,1080]]},";
				data += "{\"LineChart\" : [[\"Time\",\"Weight\", \"Limit\"],";

				for (int i = 0; i < DataGenerator.HIGHEST_TIME; i++) {
					int weight = 0;
					for (int j = 0; j < planes.size(); j++) {
						Plane p = planes.get(j);
						if (p.getLanding() < i && p.getTakeoff() > i) {
							weight += p.getWeight();
						}
					}
					data += "[" + i + ", " + weight + ", " + runwayCapacity
							+ "],";
				}
				data = data.substring(0, data.length() - 1);
				data += "], \"vAxisHeight\":" + aircraft.getMaxRunwayCapacity()
						+ "}]}";
				return data;
			}
		});
	}
}
