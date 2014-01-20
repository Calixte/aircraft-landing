package model;

public class Plane{
		
		private int weight;
		private int takeoff;
		private int landing;
				
		/**
		 * Create a plane
		 * @param w Weight of the plane
		 * @param landing Beginning of the landing window
		 * @param takeoff End of the takeoff window
		 */
		public Plane(int w, int landing, int takeoff){
			this.weight=w;
			this.takeoff = takeoff;
			this.landing = landing;
		}
		
		/**
		 * 
		 * @return the weight of the plane
		 */
		public int getWeight() {
			 return weight;
		}
		
		/**
		 * 
		 * @return the end of the takeoff window
		 */
		public int getTakeoff() {
			return takeoff;
		}

		/**
		 * 
		 * @return the beginning of the landing window
		 */
		public int getLanding() {
			return landing;
		}
		
		/**
		 * 
		 * @return the maximum duration of the stay
		 */
		public int getDuration() {
			return takeoff-landing;
		}
		
		public static int[] weights(Plane[] planes) {
			int[] weights = new int[planes.length];
			for(int i = 0 ; i < planes.length ; i++) {
				weights[i] = planes[i].getWeight();
			}
			return weights;
		}
	}