package view;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import spark.Request;
import spark.Response;
import spark.Route;

public class MainFrame {

	private static final String HEADER = "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<title>Aircraft Landing</title>"
			+ "<meta charset=\"utf-8\" />"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/layout.css\">"
			+ "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
			+ "<script type=\"text/javascript\" src=\"js/graph.js\"></script>"
			+ "</head>";

	public MainFrame() {

	}

	public static void main(String[] args) {

		get(new Route("/hello") {

			@Override
			public Object handle(Request request, Response response) {

				return HEADER + "<body>" + "<h1>Hello World!</h1>"
						+ "<div class='runway' id='runway_1'></div>"
						+ "<div class='runway' id='runway_2'></div>"
						+ "</body>";
			}
		});

		get(new Route("/js/graph.js") {

			@Override
			public Object handle(Request request, Response response) {
				return sendFile(new File("js/graph.js"), response);
			}
		});

		get(new Route("/css/layout.css") {

			@Override
			public Object handle(Request request, Response response) {
				return "h1 {color:red;}";
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

	public static String sendFile(File f, Response response) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.type("text/javascript");
		return result;
	}

}
