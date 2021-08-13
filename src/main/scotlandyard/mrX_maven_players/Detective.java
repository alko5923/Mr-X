package mrX_maven_players;

import java.util.*;

import mrX_maven_game.Board;
import mrX_maven_game.Move;
import mrX_maven_game.Station;

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
	private List<Move> possibleMovesCurrentStation = new ArrayList<Move>();
	private int taxiTicketsAvailable;
	private int busTicketsAvailable;
	private int tubeTicketsAvailable;
	private List<Station> closestTubeStations;
	private Station firstDestination;
	private List<Move> bestMoves;
	private static final int CUTOFF_PRUNE = 3;
	
	/**
	 * The class constructor.
	 * @param number
	 * @param name
	 * @param startPosition
	 * @param taxiTickets
	 * @param busTickets
	 * @param tubeTickets
	 */
	public Detective(int number, String name, int startPosition, int taxiTickets, int busTickets, int tubeTickets) {
		this.number = number;
		this.name = name;
		this.startPosition = startPosition;
		this.currentPosition = startPosition;
		this.taxiTicketsAvailable = taxiTickets;
		this.busTicketsAvailable = busTickets;
		this.tubeTicketsAvailable = tubeTickets;	
	}
	
	/**
	 * If the distance from detective to Mr. X is larger than a certain cutoff, 
	 * and if move destination to current position of Mr. X is larger than the distance 
	 * from detective's current position to Mr. X, return true. 
	 * @param move
	 * @param mrX
	 * @return
	 */
	public boolean checkIfPruneMove(Board board, Move move, MrX mrX) {
		int distMoveDestToMrX = board.returnShortestDistance(move.getDestinationStation().getNameInt(), mrX.getCurrentStation());
		int distDetToMrX = board.returnShortestDistance(currentPosition, mrX.getCurrentStation());
		boolean check = false;
		if (distDetToMrX > CUTOFF_PRUNE && distMoveDestToMrX > distDetToMrX) {
			check = true;
		}
		return check;
	}

	
	/**
	 * Find all possible moves for detective from his current position and prune them
	 * if the distance of the detective to the current position of Mr. X is above a certain cutoff. 
	 * @param board
	 */
	public void findAndPrunePossibleMovesDetective(Board board, MrX mrX) {
		List<Move> possibleMoves = new ArrayList<Move>();
		Station startStation = board.getStations().get(currentPosition-1);
			
		for (int j = 0; j < startStation.getNumberTaxiConnections(); j++) {
			Station destinationStation = board.getStations().get(startStation.getTaxiNeighbours().get(j)-1);
			if (destinationStation.isOccupied()==false) {
				Move move = new Move(startStation, destinationStation, "Taxi");
				if (mrX.getCurrentStation() != 0) {
					boolean check = checkIfPruneMove(board, move, mrX);
					if (check == true) {
						continue;
					}
				}
				possibleMoves.add(move);
			}
		}
		for (int j = 0; j < startStation.getNumberBusConnections(); j++) {
			Station destinationStation = board.getStations().get(startStation.getBusNeighbours().get(j)-1);
			if (destinationStation.isOccupied()==false) {
				Move move = new Move(startStation, destinationStation, "Bus");
				possibleMoves.add(move);
			}
		}
		for (int j = 0; j < startStation.getNumberTubeConnections(); j++) {
			Station destinationStation = board.getStations().get(startStation.getTubeNeighbours().get(j)-1);
			if (destinationStation.isOccupied()==false) {
				Move move = new Move(startStation, destinationStation, "Tube");
				possibleMoves.add(move);
			}
		}
			
		this.possibleMovesCurrentStation = possibleMoves;
			
	}
	
	/**
	 * Check if the move makes the detective move towards given destination. 
	 * @param board
	 * @param move
	 * @param destination
	 * @return
	 */
	public boolean checkIfDetectiveMovesTowardsDestination(Board board, Move move, Station destination) {
		int distanceToDest = board.returnShortestDistance(currentPosition, destination.getNameInt());
		int distanceFromMoveDestToDest = board.returnShortestDistance(move.getDestinationStation().getNameInt(), destination.getNameInt());
		if (distanceFromMoveDestToDest < distanceToDest) {
			return true;
		}
		return false;
	}
	
	
	/** 
	 * Move the detective and remove the newly occupied station 
	 * from the list of possible Mr. X stations. 
	 * @param detective
	 * @param move
	 */
	public boolean moveDetective(Board board, Move move, MrX mrX) {
		Station destinationStation = move.getDestinationStation();
		List<Integer> possibleStationsMrX = mrX.getPossibleStations();
		List<Integer> stationsToRemove = new ArrayList<Integer>();
		
		if (destinationStation.isOccupied() == false) {
			board.occupyAndUnoccupyRelevantStation(move);
			handleStatisticsDetective(move);	
			setCurrentPosition(destinationStation.getNameInt());
			stationsToRemove.add(destinationStation.getNameInt());
			return true;
		}
		possibleStationsMrX.removeAll(stationsToRemove);
		mrX.setPossibleStations(possibleStationsMrX);
		
		
		return false;
	}
	
	/**
	 * Handle tickets and travel list for given detective.
	 * @param bestMove			best move as calculated by the calculateBestMove method.
	 * @param detectiveIndex	given index of detective. 
	 */
	public void handleStatisticsDetective(Move bestMove) {
		if(bestMove.getTicket().equals("Taxi")) {
			removeDetectiveTaxiTicket();
		}
		else if(bestMove.getTicket().equals("Bus")) {
			removeDetectiveBusTicket();
		}
		else if(bestMove.getTicket().equals("Tube")) {
			removeDetectiveTubeTicket();
		}
	}
	
	/** 
	 * Decrease the amount of available taxi tickets by 1.
	 */
	public void removeDetectiveTaxiTicket() {
		taxiTicketsAvailable -= 1;
	}
	
	/** 
	 * Decrease the amount of available bus tickets by 1.
	 */
	public void removeDetectiveBusTicket() {
		busTicketsAvailable -= 1;
	}
	
	/** 
	 * Decrease the amount of available tube tickets by 1.
	 */
	public void removeDetectiveTubeTicket() {
		tubeTicketsAvailable -= 1;
	}
	
	public int getNumber() {
		return number;
	}
	
	public String getName() {
		return name;
	}
	
	public int getStartPosition() {
		return startPosition;
	}
	
	public int getCurrentPosition() {
		return currentPosition;
	}
	
	public void setCurrentPosition(int currentPos) {
		this.currentPosition = currentPos;
	}
	
	public List<Move> getPossibleMovesCurrentStation() {
		return possibleMovesCurrentStation;
	}
	
	public void setPossibleMovesCurrentStation(List<Move> possibleMovesCurrentStation) {
		this.possibleMovesCurrentStation = possibleMovesCurrentStation;
	}
	
	public int getTaxiTicketsAvailable() {
		return taxiTicketsAvailable;
	}
	
	public int getBusTicketsAvailable() {
		return busTicketsAvailable;
	}
	
	public int getTubeTicketsAvailable() {
		return tubeTicketsAvailable;
	}
	
	public List<Station> getClosestTubeStations() {
		return closestTubeStations;
	}

	public void setClosestTubeStations(List<Station> closestTubeStations) {
		this.closestTubeStations = closestTubeStations;
	}
	
	public Station getFirstDestination() {
		return firstDestination;
	}

	public void setFirstDestination(Station firstDestination) {
		this.firstDestination = firstDestination;
	}

	public List<Move> getBestMoves() {
		return bestMoves;
	}

	public void setBestMoves(List<Move> bestMoves) {
		this.bestMoves = bestMoves;
	}
	
public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---------------------------------\n");
		sb.append("DETECTIVE nr. " + number + " = " + name + "\n");
		sb.append("---------------------------------\n");
		sb.append("Start position: " + startPosition + "\n");
		sb.append("Current position: " + currentPosition + "\n");
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
		sb.append("\nFirst destination = ");
		sb.append(firstDestination);
		sb.append("\nBest moves = ");
		sb.append(bestMoves);
		sb.append("\n---------------------------------\n");
		return sb.toString();
	}
}
