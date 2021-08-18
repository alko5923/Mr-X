 package mrX_maven_players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import mrX_maven_game.Board;
import mrX_maven_game.Move;
import mrX_maven_game.Station;
import mrX_maven_strategies.Tuple;
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
	public void findPossibleStationsAfterTicket(int step, Board board, String ticketUsed, List<Detective> detectives) {
		if (step == 3 || step == 8 || step == 13 || step == 18 || step == 24) {
			return;
		}
		List<Integer> oldPossibleStations = getPossibleStations();
		TreeNodeMrX<Station> possibleStationsNode = getPossibleStationsNode();
		for (Integer station : oldPossibleStations) {
			Station oldStation = board.getStations().get(station-1);
			List<Integer> newStations = new ArrayList<Integer>();
			if (ticketUsed.equals("Taxi") && oldStation.isOccupied()==false) {
				newStations = oldStation.getTaxiNeighbours();
			}
			else if (ticketUsed.equals("Bus") && oldStation.isOccupied()==false && (oldStation.getStationType().equals("Bus") || oldStation.getStationType().equals("Tube"))) {
				newStations = oldStation.getBusNeighbours();
			}
			else if (ticketUsed.equals("Tube") && oldStation.isOccupied()==false && oldStation.getStationType().equals("Tube")) {
				newStations = oldStation.getTubeNeighbours();
			}
			//Add new nodes
			for (Integer st : newStations) {
				Station newSt = board.getStations().get(st-1);
				if (newSt.isOccupied()==false) {
					TreeNodeMrX<Station> node = possibleStationsNode.findTreeNode(oldStation);
					node.addChild(newSt);
				}
			}
		}
		//Extract the possible stations from the tree
		Set<Integer> newPossibleStationsSet = new HashSet<Integer>();
		for (TreeNodeMrX<Station> node : getPossibleStationsNode()) {
			if (step > 3 && step < 9) {
				if (node.getLevel()==(step-3)) {
					newPossibleStationsSet.add(node.getStation().getNameInt());
				}
			}
			else if (step > 8 && step < 13) {
				if (node.getLevel()==(step-8)) {
					newPossibleStationsSet.add(node.getStation().getNameInt());
				}
			}
			else if (step > 13 && step < 18) {
				if (node.getLevel()==(step-13)) {
					newPossibleStationsSet.add(node.getStation().getNameInt());
				}
			}
			else if (step > 18 && step < 24) {
				if (node.getLevel()==(step-18)) {
					newPossibleStationsSet.add(node.getStation().getNameInt());
				}
			}
			
		}
		
		//Extract from set to list
		List<Integer> newPossibleStations = new ArrayList<Integer>();
		for (Integer st : newPossibleStationsSet) {
			newPossibleStations.add(st);
		}
		
		if (newPossibleStations.size() > 5) {
			List<Integer> newPossibleStationsFiltered = new ArrayList<Integer>();
			
			//Sort the stations according to distance from current station
			List<Tuple> stationsWithDistances = new ArrayList<Tuple>();
			for (Integer stat : newPossibleStations) {
				int dist = calculateDistanceFromDetectives(stat, detectives, board);
				Tuple tuple = new Tuple(stat, dist);
				stationsWithDistances.add(tuple);	
			}
			stationsWithDistances.sort((t1, t2) -> t1.getDistance().compareTo(t2.getDistance()));
			
			//Take the five stations that are furthest from detectives
			List<Tuple> bestFive = stationsWithDistances.subList(stationsWithDistances.size()-5, stationsWithDistances.size());
			
			//Extract the stations from the list of tuples
			for (Tuple t : bestFive) {
				int station = t.getStation();
				newPossibleStationsFiltered.add(station);
			}
			
			setPossibleStations(newPossibleStationsFiltered);
			return;
		}
		
		setPossibleStations(newPossibleStations);
	}
	
	public int calculateDistanceFromDetectives(Integer station, List<Detective> detectives, Board board) {
		int distance = 0;
		for (Detective det : detectives) {
			distance += board.returnShortestDistance(station, det.getCurrentPosition());
		}
		return distance;
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
	public void reveal(int station, Board board) {
		addToReveals(station);
		setCurrentStation(station);
		List<Integer> possibleStations = new ArrayList<Integer>();
		possibleStations.add(station);
		setPossibleStations(possibleStations);
		Station newStation = board.getStations().get(station-1);
		TreeNodeMrX<Station> newNode = new TreeNodeMrX<Station>(newStation);
		setPossibleStationsNode(newNode);
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
