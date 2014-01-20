package view;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import spark.*;

public class MainFrame {
	
	private static final String HEADER  = "<!DOCTYPE html>"
	   		+ "<html>" +
	   			"<head>" +
	   				"<title>Easyvirt</title>" +
	   				"<meta charset=\"utf-8\" />" +
	   				"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/layout.css\">" +
	   				"<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>" +	
	   				"<script type=\"text/javascript\" src=\"js/graph.js\"></script>" +
	   			"</head>";

	   public static void main(String[] args) {
		  
	      get(new Route("/hello") {
			
			@Override
			public Object handle(Request request, Response response) {
				
				return HEADER + "<body>"
						+ "<h1>Hello World!</h1>"
						+ "<div class='chart' id='\"+id_chart+\"'></div>"
						+ "</body>";
			}
	      });
	      
	      get(new Route("/js/graph.js") {
			
			@Override
			public Object handle(Request request, Response response) {
				System.out.println("received request");
				String result = "";
				try {
					BufferedReader br = new BufferedReader(new FileReader(new File("js/graph.js")));
					String line = br.readLine();
					while (line != null) {
						System.out.println("reading");
						result += "\n" + line;
						line = br.readLine();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("sending");
				response.type("text/javascript");
				return result;
			}
	      });
	      
	      get(new Route("css/layout.css") {
			
			@Override
			public Object handle(Request request, Response response) {
				// TODO Auto-generated method stub
				return "h1 {color:red;}";
			}
		});

	   }	
	
}
