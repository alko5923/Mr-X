package mrX_maven_game;

/**Represent a move by one of the players on the map. 
 * 
 * @author aljaz
 *
 */
public class Move {
	private Station startStation;
	private Station destinationStation;
	private String ticket;
	
	/**
	 * The class constructor.
	 * @param startStation
	 * @param destinationStation
	 * @param ticket
	 */
	public Move(Station startStation, Station destinationStation, String ticket) {
		this.startStation = startStation;
		this.destinationStation = destinationStation;
		this.ticket = ticket;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Move from " + startStation.getNameInt() + " to " + destinationStation.getNameInt() + " using " + ticket + "\n");
		return sb.toString();
	}
	
	public Station getStartStation() {
		return startStation;
	}

	public void setStartStation(Station startStation) {
		this.startStation = startStation;
	}

	public Station getDestinationStation() {
		return destinationStation;
	}
	
	public void setDestinationStation(Station destinationStation) {
		this.destinationStation = destinationStation;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
}
