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
	public static void main(String[] args) {
		Plane[] planes = new DataGenerator().generateRandom(100, DataGenerator.HARD);
		new Aircraft(planes, new int[]{6, 5, 3, 3, 3, 4, 2, 1, 1}, 1200).solve();
		
		get(new Route("/") {

			@Override
			public Object handle(Request request, Response response) {
				String page = MainFrame.HEADER;
				page += MainFrame.parseFile(new File(MainFrame.INDEX_ROUTE));
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
