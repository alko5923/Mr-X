package mrX_maven_game;

import java.util.ArrayList;

public class Station {
	private int name;
	private String stationType;
	private boolean blackStation;
	private ArrayList<Integer> neighboursTaxi;
	private ArrayList<Integer> neighboursBus;
	private ArrayList<Integer> neighboursTube;
	private int value;
	private boolean occupied;
	
	public Station(int name, String stationType, boolean blackStation, ArrayList<Integer> neighboursTaxi, ArrayList<Integer> neighboursBus, ArrayList<Integer> neighboursTube) {
		this.name = name;
		this.stationType = stationType;
		this.neighboursTaxi = neighboursTaxi;
		this.neighboursBus = neighboursBus;
		this.neighboursTube = neighboursTube;
	}
	
	
	/**Get the name of the station.
	 * 
	 */
	public int getNameInt() {
		return this.name;
	}
	
	/**Get the type of the station.
	 * 
	 * @return	station type
	 */
	public String getStationType() {
		return stationType;
	}
	
	/**Return true if the station is a black station, false otherwise. 
	 * 
	 * @return	a boolean indicating if the station is a black station or not. 
	 */
	public boolean isBlackStation() {
		return blackStation;
	}
	
	/**Return the taxi neighbours of this station.
	 * 
	 * @return	station's taxi neighbours
	 */
	public ArrayList<Integer> getTaxiNeighbours() {
		return neighboursTaxi;
	}
	
	/**Get the number of taxi connections from this station.
	 * 
	 * @return	Number of taxi connections from this station.
	 */
	public int getNumberTaxiConnections() {
		return neighboursTaxi.size();
	}

	
	/**Return the bus neighbours of this station.
	 * 
	 * @return	station's bus neighbours
	 */
	public ArrayList<Integer> getBusNeighbours() {
		return neighboursBus;
	}

	/**Get the number of bus connections from this location.
	 * 
	 * @return	Number of bus connections from this location.
	 */
	public int getNumberBusConnections() {
		return neighboursBus.size();
	}
	
	/**Return the tube neighbours of this station.
	 * 
	 * @return	station's tube neighbours
	 */
	public ArrayList<Integer> getTubeNeighbours() {
		return neighboursTube;
	}

	/**Get the number of tube connections from this station.
	 * 
	 * @return	Number of tube connections from this station.
	 */
	public int getNumberTubeConnections() {
		return this.neighboursTube.size();
	}
	
	/**Get the value of the station, as determined by the evaluation function. 
	 * 
	 * @return	The value of the station. 
	 */
	public int getValue() {
		return value;
	}
	
	/**Set the value of the station, as determined by the evaluation function.
	 * 
	 */
	public void setValue(int value) {
		this.value = value;
	}

	
	/**Check if the station is occupied.
	 *  
	 * @return a boolean, true if station is occupied, false otherwise
	 */
	public boolean isOccupied() {
		return this.occupied;
	}
	
	/**Make the station occupied.
	 *  
	 */
	public void occupyStation() {
		this.occupied = true;
	}
	
	/**Make the station unoccupied.
	 *  
	 */
	public void unoccupyStation() {
		this.occupied = false;
	}
	
	
	/**Print the station and its attributes to the console.
	 * 
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Station nr. = " + name + "\n");
		sb.append("Station type = " + stationType + "\n");
		sb.append("Black station = " + blackStation + "\n");
		sb.append("Taxi neighbours = " + neighboursTaxi + "\n");
		sb.append("Bus neighbours = " + neighboursBus + "\n");
		sb.append("Tube neighbours = " + neighboursTube + "\n");
		sb.append("Station value = " + value + "\n");
		sb.append("Station occupied = " + occupied + "\n");
		
		return sb.toString();
	}
	
	
	
}
