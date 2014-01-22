package view;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.chainsaw.Main;

import spark.Request;
import spark.Response;
import spark.Route;

public class MainFrame {
	
	public static final String TITLE = "<title>Aircraft landing</title>";
	public static final String STYLESHEET_ROUTE = "/css/stylesheet.css";
	public static final String GRAPH_SCRIPT_ROUTE = "/js/graph.js";

	private static final String HEADER = "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ TITLE
			+ "<meta charset=\"utf-8\" />"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=" + STYLESHEET_ROUTE + ">"
			+ "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
			+ "<script type=\"text/javascript\" src=" + GRAPH_SCRIPT_ROUTE + "></script>"
			+ "</head>";

	public MainFrame() {

	}
	
	public String createRunway(int number) {
		String div = "<div class='runway'> "
				+ "<h2>Runway n°" + number + "</h2>"
				+ "<div class='runway' id='runway_1'></div>"
				+ "</div>";
		return div;
	}

	public static void main(String[] args) {

		get(new Route("/hello") {

			@Override
			public Object handle(Request request, Response response) {

				return HEADER + "<body>" + "<h1>Aircraft landing project</h1>"
						+ "<div class='runway' id='runway_1'></div>"
						+ "<div class='runway' id='runway_2'></div>"
						+ "</body>";
			}
		});

		get(new Route(MainFrame.GRAPH_SCRIPT_ROUTE) {

			@Override
			public Object handle(Request request, Response response) {
				String answer = parseFile(new File("js/graph.js"));
				response.type("text/javascript");
				return answer;
			}
		});

		get(new Route(MainFrame.STYLESHEET_ROUTE) {

			@Override
			public Object handle(Request request, Response response) {
				String stylesheet = parseFile(new File("css/stylesheet.css"));
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
						+ "[\"2\",\"BA123\", 20, 60]"
						+ "]}";
			}
		});

	}

	public static String parseFile(File f) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			while (line != null) {
				result += "\n" + line;
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
