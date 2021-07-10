package mrX_maven_run;


/**Represent a move by one of the players on the map. 
 * 
 * @author aljaz
 *
 */
public class Move {
	private Station startStation;
	private Station destinationStation;
	private String ticket;
	
	public Move(Station startStation, Station destinationStation, String ticket) {
		this.startStation = startStation;
		this.destinationStation = destinationStation;
		this.ticket = ticket;
	}

	/**Get the start station of the move. 
	 * 
	 * @return	the start station
	 */
	public Station getStartStation() {
		return startStation;
	}

	/**Set the start station of the move.
	 * 
	 * @param startStation
	 */
	public void setStartStation(Station startStation) {
		this.startStation = startStation;
	}

	/**Get the destination station of the move.
	 * 
	 * @return	the destination station
	 */
	public Station getDestinationStation() {
		return destinationStation;
	}
	
	/**Set the destination station of the move. 
	 * 
	 * @param destinationStation
	 */
	public void setDestinationStation(Station destinationStation) {
		this.destinationStation = destinationStation;
	}

	/**Get the type of ticket used in the move. 
	 * 
	 * @return	type of ticket
	 */
	public String getTicket() {
		return ticket;
	}

	/**Set the type of ticket used in the move. 
	 * 
	 * @param type of ticket 
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	/**Print the station and its attributes to the console.
	 * 
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Move from " + startStation.getNameInt() + " to " + destinationStation.getNameInt() + " using " + ticket + "\n");
		
		return sb.toString();
	}
	
}
