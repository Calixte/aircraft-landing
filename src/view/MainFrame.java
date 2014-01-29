package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainFrame {

	public static final String TITLE = "<title>CSPModel landing</title>";
	public static final String INDEX_ROUTE = "index.html";
	public static final String STYLESHEET_ROUTE = "css/stylesheet.css";
	public static final String BOOTSTRAP_ROUTE = "css/bootstrap.min.css";
	public static final String GRAPH_SCRIPT_ROUTE = "/js/graph.js";

	public static final String HEADER = "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ TITLE
			+ "<meta charset=\"utf-8\" />"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href="
			+ STYLESHEET_ROUTE
			+ ">"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href="
			+ BOOTSTRAP_ROUTE
			+ ">"
			+ "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
			+ "<script type=\"text/javascript\" src=" + GRAPH_SCRIPT_ROUTE
			+ "></script>" + "</head>";

	public MainFrame() {

	}

	public static String createRunway(int id) {
		String div = "<div class='runway'> "
				+ "<div class='runwayWeight' id='runway_1'></div>"
				+ "<div class='runwayGraph' id='runway_1'></div>"
				+ "</div>";
		return div;
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
