package view;

import static spark.Spark.*;
import spark.*;

public class MainFrame {

	   public static void main(String[] args) {
	      
	      get(new Route("/hello") {
			
			@Override
			public Object handle(Request request, Response response) {
				return "Hello World!";
			}
	      });

	   }	
	
}
