package main;
import static spark.Spark.get;

import java.io.File;

import spark.Request;
import spark.Response;
import spark.Route;
import view.MainFrame;
import generator.DataGenerator;
import model.Aircraft;
import model.Plane;

public class Main {
	
	private static Plane[] planes;
	
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
				int difficulty = Integer.parseInt(request.queryParams("generatorDifficulty"));
				int nbOfFlights = Integer.parseInt(request.queryParams("generatorNbPlanes"));
				System.out.println("Params : " + type + ", " + difficulty + ", " + nbOfFlights);
				planes = new Plane[nbOfFlights];
				switch(type) {
				case DataGenerator.LINEAR:
					System.out.println("Through here");
					planes = generator.generateLinear(nbOfFlights, difficulty);
					break;
				case DataGenerator.RANDOM:
					planes = generator.generateRandom(nbOfFlights, difficulty);
				}
				Aircraft aircraft = new Aircraft(planes, new int[]{6, 5, 3, 3, 3, 4, 2, 1, 1}, 1200);
				aircraft.solve();
				aircraft.updatePlaneArray();
				int nbOfRunways = aircraft.getNbOfRunways();
				System.out.println("Number of runways : " + nbOfRunways);
				
				String page = MainFrame.HEADER;
				page += MainFrame.parseFile(new File(MainFrame.INDEX_ROUTE));
				for (int i=0; i<nbOfRunways; i++) {
					page += MainFrame.createRunway(i);
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
				String stylesheet = MainFrame.parseFile(new File(MainFrame.STYLESHEET_ROUTE));
				response.type("text/css");
				return stylesheet;
			}
		});
		get(new Route(MainFrame.BOOTSTRAP_ROUTE) {

			@Override
			public Object handle(Request request, Response response) {
				String stylesheet = MainFrame.parseFile(new File(MainFrame.BOOTSTRAP_ROUTE));
				response.type("text/css");
				return stylesheet;
			}
		});

		get(new Route("/graph/:id") {

			@Override
			public Object handle(Request request, Response response) {
				return "{\"data\":["
						+ "[\"Position\",\"Flight\", \"landing\", \"take off\"],"
						+ "[\"1\",\"BA123\", 0, 35 ],"
						+ "[\"2\",\"BA123\", 20, 60]" + "]}";
			}
		});
	}
}
