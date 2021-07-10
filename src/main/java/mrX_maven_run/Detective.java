package mrX_maven_run;

import java.util.*;

/**This class represents a detective.
 * 
 * @author aljaz
 *
 */
public class Detective {
	
	private int number;
	private String name;
	private int startPosition;
	private int currentPosition;
	private List<Station> stationsList = new ArrayList<Station>();
	private List<Move> possibleMovesCurrentStation = new ArrayList<Move>();
	private List<Move> moveList = new ArrayList<Move>();
	private int taxiTicketsAvailable;
	private int busTicketsAvailable;
	private int tubeTicketsAvailable;
	
	public Detective(int number, String name, int startPosition, int taxiTickets, int busTickets, int tubeTickets, List<Station> stationsList) {
		this.number = number;
		this.name = name;
		this.startPosition = startPosition;
		this.currentPosition = startPosition;
		this.stationsList = stationsList;
		this.taxiTicketsAvailable = taxiTickets;
		this.busTicketsAvailable = busTickets;
		this.tubeTicketsAvailable = tubeTickets;
		
	}
	
	/**A constructor for the Detective clone.
	 *  
	 * @param original
	 */
	
	public Detective(Detective original) {
		this.number = original.number;
		this.name = original.name;
		this.startPosition = original.startPosition;
		this.currentPosition = original.currentPosition;
		this.possibleMovesCurrentStation = original.possibleMovesCurrentStation;
		this.moveList = original.moveList;
		this.taxiTicketsAvailable = original.taxiTicketsAvailable;
		this.busTicketsAvailable = original.busTicketsAvailable;
		this.tubeTicketsAvailable = original.tubeTicketsAvailable;
		this.stationsList = original.stationsList;
	}
	
	
	/**Get the number of the detective.
	 * 
	 * @return		the number of the detective. 
	 */
	public int getNumber() {
		return number;
	}
	
	/**Get the name of the detective.
	 * 
	 * @return		the name of the detective.
	 */
	public String getName() {
		return name;
	}
		
	/**Get the start position of the detective.
	 * 
	 * @return		the start position of the detective. 
	 */
	public int getStartPosition() {
		return startPosition;
	}
	
	/**Get the current position of the detective.
	 * 
	 * @return		the number of the current location. 
	 */
	public int getCurrentPosition() {
		return currentPosition;
	}
	
	/**Set the current position of the detective.
	 * 
	 * @param currentPos	the current position.
	 */
	public void setCurrentPosition(int currentPos) {
		this.currentPosition = currentPos;
	}
	
	/**Get the list of possible moves from the current station.
	 * 
	 * @return	the list of possible moves from the current station.
	 */
	public List<Move> getPossibleMovesCurrentStation() {
		return possibleMovesCurrentStation;
	}
	
	/**Set the list of possible moves from current station. 
	 * 
	 * @param possibleMovesCurrentStation
	 */
	public void setPossibleMovesCurrentStation(List<Move> possibleMovesCurrentStation) {
		this.possibleMovesCurrentStation = possibleMovesCurrentStation;
	}
	
	/**Find all the possible moves the seeker can make from their current position.
	 * 
	 */
	public void findPossibleMovesDetective() {
		
		List<Move> possibleMoves = new ArrayList<Move>();
		Station startStation = stationsList.get(currentPosition-1);
			
		for (int j = 0; j < startStation.getNumberTaxiConnections(); j++) {
			Station destinationStation = stationsList.get(startStation.getTaxiNeighbours().get(j)-1);
			if (destinationStation.isOccupied()==false) {
				Move move = new Move(startStation, destinationStation, "Taxi");
				possibleMoves.add(move);
			}
		}
		for (int j = 0; j < startStation.getNumberBusConnections(); j++) {
			Station destinationStation = stationsList.get(startStation.getBusNeighbours().get(j)-1);
			if (destinationStation.isOccupied()==false) {
				Move move = new Move(startStation, destinationStation, "Bus");
				possibleMoves.add(move);
			}
		}
		for (int j = 0; j < startStation.getNumberTubeConnections(); j++) {
			Station destinationStation = stationsList.get(startStation.getTubeNeighbours().get(j)-1);
			if (destinationStation.isOccupied()==false) {
				Move move = new Move(startStation, destinationStation, "Tube");
				possibleMoves.add(move);
			}
		}
			
		this.possibleMovesCurrentStation = possibleMoves;
			
	}
	
	
	/**Get the list of moves.
	 * 
	 * @return	list of moves
	 */
	public List<Move> getMoveList() {
		return moveList;
	}
	
	/**Get the number of available taxi tickets.
	 * 
	 * @return	the number of available taxi tickets. 
	 */
	public int getTaxiTicketsAvailable() {
		return taxiTicketsAvailable;
	}
	
	/**Get the number of available bus tickets.
	 * 
	 * @return	the number of available bus tickets. 
	 */
	public int getBusTicketsAvailable() {
		return busTicketsAvailable;
	}
	
	/**Get the number of available tube tickets.
	 * 
	 * @return	the number of available tube tickets. 
	 */
	public int getTubeTicketsAvailable() {
		return tubeTicketsAvailable;
	}
	
	/**Decrease the amount of available taxi tickets by 1. 
	 * 
	 */
	public void removeDetectiveTaxiTicket() {
		taxiTicketsAvailable -= 1;
	}
	
	/**Decrease the amount of available bus tickets by 1. 
	 * 
	 */
	public void removeDetectiveBusTicket() {
		busTicketsAvailable -= 1;
	}
	
	/**Decrease the amount of available tube tickets by 1. 
	 * 
	 */
	public void removeDetectiveTubeTicket() {
		tubeTicketsAvailable -= 1;
	}
	
	
	/**Print the information about the detective to the console.
	 * 
	 */
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("---------------------------------\n");
		sb.append("DETECTIVE nr. " + number + " = " + name + "\n");
		sb.append("---------------------------------\n");
		sb.append("Start position: " + startPosition + "\n");
		sb.append("Current position: " + currentPosition + "\n");
		sb.append("Move list: [ ");
		for (int i = 0; i < moveList.size(); i+=1 ) {
			sb.append(moveList.get(i).toString());
			sb.append(", ");
		}
		sb.append(" ]\n");
		sb.append("Possible move list from current station: [ ");
		for (int i = 0; i < possibleMovesCurrentStation.size(); i+=1 ) {
			sb.append(possibleMovesCurrentStation.get(i).toString());
			sb.append(", ");
		}
		sb.append(" ]\n");
		sb.append("Taxi tickets available = "); 
		sb.append(taxiTicketsAvailable);
		sb.append("\nBus tickets available = "); 
		sb.append(busTicketsAvailable);
		sb.append("\nTube tickets available = "); 
		sb.append(tubeTicketsAvailable);
		sb.append("\n---------------------------------\n");
		return sb.toString();
	}

	
	
}
