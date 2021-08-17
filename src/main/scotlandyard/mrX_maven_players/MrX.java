 package mrX_maven_players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import mrX_maven_game.Board;
import mrX_maven_game.Move;
import mrX_maven_game.Station;
import mrX_maven_utilities.TreeNodeMrX;

/**
 * This class represents Mr. X, the villain of the game.
 * @author aljaz
 *
 */
public class MrX {
	private static Scanner sc = new Scanner(System.in);
	private List<Integer> possibleStartingStationsMrX = Arrays.asList(35, 45, 51, 71, 78, 104, 106, 127, 132, 146, 166, 170, 172);
	private List<Integer> possibleStations = new ArrayList<Integer>();
	private TreeNodeMrX<Station> possibleStationsNode;
	private List<Move> possibleMoves = new ArrayList<Move>();
	private int taxiTicketsAvailable; 
	private int busTicketsAvailable; 
	private int tubeTicketsAvailable;
	private List<String> ticketsUsed = new ArrayList<String>();
	private List<Integer> mrXReveals = new ArrayList<Integer>();
	private int currentStation;
	
	/**
	 * The class constructor. 
	 * @param taxiTicketsAvailable
	 * @param busTicketsAvailable
	 * @param tubeTicketsAvailable
	 */
	public MrX(int taxiTicketsAvailable, int busTicketsAvailable, int tubeTicketsAvailable) {
		this.taxiTicketsAvailable = taxiTicketsAvailable;
		this.busTicketsAvailable = busTicketsAvailable;
		this.tubeTicketsAvailable = tubeTicketsAvailable;
	}
	
	/**
	 * Move Mr. X and update the relevant statistics.
	 * @return the type of ticket that Mr. X used. 
	 */
	public String moveMrX() {
		System.out.println("\nOK, Mr. X moves ...\n");
		boolean waitingForMrX = true;
		while(waitingForMrX) {
			System.out.println("Press x when Mr. X has moved ...\n");
			String answer = sc.next();
			if(answer.equals("x")) {
				waitingForMrX = false;
				System.out.println("\nMr X has moved!\n");
			}
		}
		System.out.println("What ticket has Mr. X used? \n");
		String ticketUsed = sc.next();
		if (ticketUsed.equals("Taxi") && this.getAvailableTaxi() > 0 || ticketUsed.equals("Bus") && 
				this.getAvailableBus() > 0 || ticketUsed.equals("Tube") && this.getAvailableTube() > 0) {
			this.addToUsedTickets(ticketUsed);
			this.removeTicket(ticketUsed);
		} else {
			System.out.println("Not enough tickets of this type available!");
			//TODO throw an exception!!
		}
		return ticketUsed;
	}
	
	/**
	* Simulate Mr. X making a move, and update possible stations and moves list afterwards. 
	* @param move
	*/
	public void simulateMove(Move move, Board board) {
		setCurrentStation(move.getDestinationStation().getNameInt());
		if (move.getTicket().equals("Taxi") && getAvailableTaxi() > 0 || move.getTicket().equals("Bus") && 
			getAvailableBus() > 0 || move.getTicket().equals("Tube") && getAvailableTube() > 0) {
			addToUsedTickets(move.getTicket());
			removeTicket(move.getTicket());
		}
		//Set the list of possible stations to current station only
		List<Integer> possibleStations = new ArrayList<Integer>();
		possibleStations.add(getCurrentStation());
		setPossibleStations(possibleStations);
		findPossibleMoves(board);	
	}
	
	/**
	 * Find all possible stations where Mr. X could be located after using a certain type of ticket.
	 * @param ticketUsed
	 */
	public void findPossibleStationsAfterTicket(Board board, String ticketUsed) {
		//Loop through the list of possible Mr. X stations
		//every station that can be reached with the ticketUsed and that is not occupied, 
		//add it to the list of new possible Mr. X stations.
		List<Integer> possibleStations = new ArrayList<Integer>();
		if (ticketUsed.equals("Taxi")) {
			for (int i = 0; i < getPossibleStations().size(); i++) {
				Station startStation = board.getStations().get(getPossibleStations().get(i)-1);
				for (int j = 0; j < startStation.getNumberTaxiConnections(); j++) {	
					Station destinationStation = board.getStations().get(startStation.getTaxiNeighbours().get(j)-1);
					if (destinationStation.isOccupied()==false && possibleStations.contains(destinationStation.getNameInt())==false) {	
						possibleStations.add(destinationStation.getNameInt());	
					}
				}
			}
		} else if (ticketUsed.equals("Bus")) {
			for (int i = 0; i < getPossibleStations().size(); i++) {
				Station startStation = board.getStations().get(getPossibleStations().get(i)-1);
				for (int j = 0; j < startStation.getNumberBusConnections(); j++) {
					Station destinationStation = board.getStations().get(startStation.getBusNeighbours().get(j)-1);
					if (destinationStation.isOccupied()==false) {
						possibleStations.add(destinationStation.getNameInt());
					}
				}	
			}	
		} else if (ticketUsed.equals("Tube")) {
			for (int i = 0; i < getPossibleStations().size(); i++) {
				Station startStation = board.getStations().get(getPossibleStations().get(i)-1);
				for (int j = 0; j < startStation.getNumberTubeConnections(); j++) {
					Station destinationStation = board.getStations().get(startStation.getTubeNeighbours().get(j)-1);
					if (destinationStation.isOccupied()==false) {
						possibleStations.add(destinationStation.getNameInt());
					}
				}	
			}	
		}
		//TODO: throw an exception if ticket type does not match any of the keywords	
		possibleStations.sort(null);
		setPossibleStations(possibleStations);
	}
		
	/** 
	* Find all possible Mr. X moves from all his possible stations.
	* @param step
	* @param board
	*/
	public void findPossibleMoves (Board board) {
		//Loop through all possible stations
		//Check all possible moves from them and add them to the list of possible moves
		List<Move> possibleMoves = new ArrayList<Move>();
			for(int i = 0; i < getPossibleStations().size(); i++) {			
				Station startStation = board.getStations().get(getPossibleStations().get(i)-1);
				//A move gets added if Mr. X has the right ticket and if the destination station is not occupied
				for(int j = 0; j < startStation.getNumberTaxiConnections(); j++) {
						if (getAvailableTaxi() > 0) {
							Station destinationStation = board.getStations().get(startStation.getTaxiNeighbours().get(j)-1);
							if (destinationStation.isOccupied()==false) {
								Move possibleMove = new Move(startStation, destinationStation, "Taxi");
								possibleMoves.add(possibleMove);
							}
						}
				}
				for(int j = 0; j < startStation.getNumberBusConnections(); j++) {
						if (getAvailableBus() > 0) {
							Station destinationStation = board.getStations().get(startStation.getBusNeighbours().get(j)-1);
							if (destinationStation.isOccupied()==false) {
								Move possibleMove = new Move(startStation, destinationStation, "Bus");
								possibleMoves.add(possibleMove);
							}
						}
				}
				for(int j = 0; j < startStation.getNumberTubeConnections(); j++) {
						if (getAvailableBus() > 0) {
							Station destinationStation = board.getStations().get(startStation.getTubeNeighbours().get(j)-1);
							if (destinationStation.isOccupied()==false) {
								Move possibleMove = new Move(startStation, destinationStation, "Tube");
								possibleMoves.add(possibleMove);
							}
						}
					}
				}
			setPossibleMoves(possibleMoves);
	}
	
	/** 
	 * Sets the necessary attributes after Mr. X's location is revealed.
	 * @param station
	 */
	public void reveal(int station) {
		addToReveals(station);
		setCurrentStation(station);
		List<Integer> possibleStations = new ArrayList<Integer>();
		possibleStations.add(station);
		setPossibleStations(possibleStations);
	}
	
	/** 
	 * Add a station to the list of stations where Mr. X. has revealed himself.
	 * @param 	station of reveal
	 * @throws 	LocationNotFoundException
	 */
	public void addToReveals(int stationName) {
			mrXReveals.add(stationName);
	}
	
	/**
	 * Handle tickets for Mr. X after detectives have made their moves.
	 */
	public void handleTickets(List<Detective> detectives) {
		for (Detective det : detectives) {
			List<Move> bestMoves = det.getBestMoves();
			Move lastBestMove = bestMoves.get(bestMoves.size()-1);
			String ticketUsed = lastBestMove.getTicket();
			addTicket(ticketUsed);
		}
	}
	
	/** 
	 * Remove the ticket from the number of available tickets.
	 * @param ticketType 
	 */
	public void removeTicket(String ticketType) {
		if(ticketType.equals("Taxi")) {
			taxiTicketsAvailable -= 1;
		} else if (ticketType.equals("Bus")) {
			busTicketsAvailable -= 1;
		} else if (ticketType.equals("Tube")) {
			tubeTicketsAvailable -= 1;
		}
		
	}
	
	/**
	 * Add the ticket to the number of available tickets. 
	 * @param ticketType
	 */
	private void addTicket(String ticketType) {
		if(ticketType.equals("Taxi")) {
			taxiTicketsAvailable += 1;
		} else if (ticketType.equals("Bus")) {
			busTicketsAvailable += 1;
		} else if (ticketType.equals("Tube")) {
			tubeTicketsAvailable += 1;
		}
	}
	
	/**
	 * Add a ticket to the list of used tickets.
	 * @param 	the ticket used. 
	 */
	public void addToUsedTickets(String ticket) {
		ticketsUsed.add(ticket);
	}
	
	public int getAvailableTaxi() {
		return taxiTicketsAvailable;
	}

	public int getAvailableBus() {
		return busTicketsAvailable;
	}
	
	public int getAvailableTube() {
		return tubeTicketsAvailable;
	}
	
	public int getCurrentStation() {
		return currentStation;
	}
	
	public void setCurrentStation(int currentStation) {
		this.currentStation = currentStation;
	}
	
	public List<Integer> getPossibleStations() {
		return possibleStations;
	}
	
	public void setPossibleStations(List<Integer> possibleStations) {
		this.possibleStations = possibleStations;
	}
	
	public List<Integer> getPossibleStartingStationsMrX() {
		return possibleStartingStationsMrX;
	}


	public void setPossibleStartingStationsMrX(List<Integer> possibleStartingStationsMrX) {
		this.possibleStartingStationsMrX = possibleStartingStationsMrX;
	}


	public List<Move> getPossibleMoves() {
		return possibleMoves;
	}

	public void setPossibleMoves(List<Move> possibleMoves) {
		this.possibleMoves = possibleMoves;
	}
	
	public TreeNodeMrX<Station> getPossibleStationsNode() {
		return possibleStationsNode;
	}

	public void setPossibleStationsNode(TreeNodeMrX<Station> possibleStationsNode) {
		this.possibleStationsNode = possibleStationsNode;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---------------------------------\n");
		sb.append("     *** MR X INFO ***     \n");
		sb.append("---------------------------------\n");
		sb.append("Possible stations = " + possibleStations + "\n");
		sb.append("Possible moves = " + possibleMoves + "\n");
		sb.append("Taxi tickets available = "); 
		sb.append(String.valueOf(taxiTicketsAvailable));
		sb.append("\nBus tickets available = ");
		sb.append(String.valueOf(busTicketsAvailable));
		sb.append("\nTube tickets available = ");
		sb.append(String.valueOf(tubeTicketsAvailable));
		sb.append("\nMr. X used tickets: " + ticketsUsed + "\n"); 
		sb.append("Mr. X reveal list: " + mrXReveals + "\n"); 
		sb.append("Current station = " + currentStation + "\n");		
		return sb.toString();
	}

}
