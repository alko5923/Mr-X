package mrX_maven_strategies;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import com.google.common.collect.Lists;
import mrX_maven_game.Board;
import mrX_maven_game.Move;
import mrX_maven_game.Station;
import mrX_maven_players.Detective;
import mrX_maven_players.MrX;

public class CoordinatePlayers {
	
	private static Scanner sc = new Scanner(System.in);
	private Board board;
	private static List<Integer> possibleStartingStationsDetectives = Arrays.asList(13, 26, 29, 34, 50, 53, 91, 94, 103, 112, 117, 123, 138, 141, 155, 174);
	private List<Detective> detectives = new ArrayList<Detective>();
	private MrX mrX;
	private List<List<Move>> allPossibleMoveCombosDetectives = new ArrayList<List<Move>>();
	
	
	public static CoordinatePlayers initializeCoordinator(Board board) throws FileNotFoundException {
		List<Detective> detectives = setupDetectives(board);
		MrX mrX = new MrX(4, 3, 3);
		return new CoordinatePlayers(board, detectives, mrX);
	}
	
	/**
	 * Class constructor.
	 * @param board
	 * @throws FileNotFoundException
	 */
	public CoordinatePlayers(Board board, List<Detective> detectives, MrX mrX) {		
		this.setBoard(board);
		this.setDetectives(detectives);
		this.setMrX(mrX);
	}
		
	/** 
	 * Sets the first tube destination for all detectives.
	 * @param closestTubeStations
	 */
	public void setFirstDestinationDetectives(List<Station> closestTubeStations) {
		
		for (int i = 0; i < detectives.size(); i++) {
			Detective det = detectives.get(i);
			Station firstDestination = closestTubeStations.get(i);
			det.setFirstDestination(firstDestination);
		}
		
	}
	
	/**
	 * Sets up the detectives, including their number and names.
	 * @return	The list of detectives. 
	 */
	public static List<Detective> setupDetectives(Board board) {
		List<Detective> detectives = new ArrayList<Detective>();
		while(true) {
			System.out.println("Please enter the number of detectives: ");
			int numberOfDetectives = sc.nextInt();
				if(numberOfDetectives > 5) {
					System.out.println("The max number of detectives is 5!");
					continue;
				} else if(numberOfDetectives < 2) {
					System.out.println("The min number of detectives is 2!");
					continue;
				} else {
					for (int number = 1; number < numberOfDetectives+1; number+=1) {
						System.out.println("Name of detective nr. " + number + ": ");
						String nameDetective = sc.next();
						System.out.println("Please give start location of detective " + nameDetective + ":");
						while(true) {
							int startPos = sc.nextInt();
							if(possibleStartingStationsDetectives.contains(startPos)) {
								Detective detective = new Detective(number, nameDetective, startPos, 10, 8, 4);
								detective.setCurrentPosition(startPos);
								detectives.add(detective);
								board.getStations().get(startPos-1).occupyStation();
								break;
							}
							System.out.println("That is not a valid starting position!");
						}
					}
					return detectives;
				}
		}
	}
	
	/**
	 * Finds and sets the first two moves for all detectives.
	 */
	public void findFirstAndSecondMoves(int step) {
		
		Set<Integer> firstMoveDestinations = new HashSet<Integer>();
		
		for (Detective det : detectives) {
			List<Move> bestMoves = new ArrayList<Move>();
			if(det.getBestMoves()!=null) {
				bestMoves = det.getBestMoves();
			}
			det.findPossibleMovesDetective(board);
			for (Move move : det.getPossibleMovesCurrentStation()) {
				boolean checkIfMoveRightDirection = det.checkIfDetectiveMovesTowardsDestination(board, move, det.getFirstDestination());
				//In case the detective starts at a tube station, we first want to just move him away one taxi station, to any station, and then back. 
				if(step == 1 && board.getStations().get(det.getStartPosition()-1).getStationType().equals("Tube") && move.getTicket().equals("Taxi")) {
					bestMoves.add(move);
					det.setBestMoves(bestMoves);
					break;
				}
				if (checkIfMoveRightDirection == true) {
					boolean tryToAdd = firstMoveDestinations.add(move.getDestinationStation().getNameInt());
					if (tryToAdd == true) {
						bestMoves.add(move);
						det.setBestMoves(bestMoves);
						break;
					}
				}
			}
		}
	}
	
	
	/** 
	 * Generates and sets all possible move combinations for detectives, based on all possible moves
	 * every detective can make from its current position.
	 */
	public void generateAllPossibleMoveCombosDetectives() {
		findPossibleMovesAllDetectives();
		List<List<Move>> allPossibleDetectiveMoves = coordinatePossibleDetectiveMoves();
		//Use guava to create a cartesian product of all possible combos
		List<List<Move>> allCombos = new ArrayList<List<Move>>();
		allCombos = Lists.cartesianProduct(allPossibleDetectiveMoves);
		List<List<Move>> allCombosCleanedUp = new ArrayList<List<Move>>();
		
		//To clean up the cartesian product we loop through the list of lists
		for (int i = 0; i < allCombos.size(); i++) {	
			List<Station> destStations = new ArrayList<Station>();
		
			//We loop through each sublist and save the destination stations in a separate list
			for (int j = 0; j < allCombos.get(i).size(); j++) {
				
				Station destination = allCombos.get(i).get(j).getDestinationStation();
				destStations.add(destination);
			}
			
			//We now insert the destination stations in a set, using the add operation
			//The add operation only adds an item if it is not already present
			Set<Integer> stationsSet = new HashSet<Integer>();
			
			for (int k = 0; k < destStations.size(); k++) {
				stationsSet.add(destStations.get(k).getNameInt());
			}
			
			//We now compare the sizes of the list of destinations and the set
			//If the set is shorter, there have been duplicates in the list
			//In case the sizes are equal, we add the sublist to the cleaned up list of lists
			if (destStations.size() == stationsSet.size()) {
				List<Move> goodCombo = allCombos.get(i);
				allCombosCleanedUp.add(goodCombo);	
			}
		}
		this.setAllPossibleMoveCombosDetectives(allCombosCleanedUp);
	}
	
	
	/**
	 * Coordinates the possible moves for every detective. 
	 * @return	A list of lists of coordinated moves.
	 */
	private List<List<Move>> coordinatePossibleDetectiveMoves() {
		/**Loop through all detectives, except for the last one 
		 * For every detective, loop through all possible moves
		 * If any of the moves' destinations stations equals any of the detectives' current stations,
		 * remove the move.
		 * This is a design choice to make the algorithm easier: a following detective will not be able to move
		 * to a station that was occupied by a previous detective. 
		 */
		List<Integer> detectivesCurrentStations = new ArrayList<Integer>();  
				
		for (int i = 0; i < detectives.size(); i++) {
			Detective det = detectives.get(i);
			int current = det.getCurrentPosition();
			detectivesCurrentStations.add(current);
		}
		
		for (int i = 0; i < detectives.size()-1; i++) {
			Detective det = detectives.get(i);
			for (int j = 0; j < det.getPossibleMovesCurrentStation().size(); j++) {
				int destination = det.getPossibleMovesCurrentStation().get(j).getDestinationStation().getNameInt();
				if (detectivesCurrentStations.contains(destination)) {
					det.getPossibleMovesCurrentStation().remove(j);	
				}	
			}
		}
		
		List<List<Move>> allMoves = new ArrayList<List<Move>>();
		
		//Save all possible moves in a list of lists, allPossibleDetectiveMoves
		for (int i = 0; i < detectives.size(); i++) {
			Detective detective = detectives.get(i);
			List<Move> possibleMoves = detective.getPossibleMovesCurrentStation();
			allMoves.add(possibleMoves);	
		}
		return allMoves;
	}
	
	
	/** 
	 * Finds and sets the possible moves for all detectives.
	 */
	private void findPossibleMovesAllDetectives() {
		
		for (Detective det : detectives) {
			det.findPossibleMovesDetective(board);
		}
		
	}
	
	/**
	 * Generates coordinated closest tube stations for all detectives.
	 * @return
	 */
	public List<Station> generateClosestTubeStationsAllDetectives() {
		findAndSetClosestTubeStationsAllDetectives();
		List<Integer> coordinatedClosestStationNames = coordinateClosestTubeStations();
		List<Station> coordinatedClosestStations = new ArrayList<Station>();
		
		for (int stationName : coordinatedClosestStationNames) {	
			Station station = board.getStations().get(stationName-1);
			coordinatedClosestStations.add(station);
		}
		
		return coordinatedClosestStations;
		
		
	}
	
	/** 
	 * Find the closest coordinated tube stations from detectives' current positions.
	 * @return	A list of closest coordinated tube stations from detectives' current positions.
	 */
	private void findAndSetClosestTubeStationsAllDetectives() {
		
		List<Integer> tubeStations = Arrays.asList(1, 13, 46, 67, 74, 79, 89, 93, 111, 128, 140, 153, 163, 185);
		
		
		for (int i = 0; i < detectives.size(); i++) {
			List<Station> closestTubeStations = new ArrayList<Station>();
			Detective det = detectives.get(i);
			List<Tuple> stationsWithDistances = new ArrayList<Tuple>(); 
			
			for (int station : tubeStations) {	
				int distance = board.returnShortestDistance(station, det.getCurrentPosition());
				Tuple tuple = new Tuple(station, distance);
				stationsWithDistances.add(tuple);
			}
			//Sort the stations according to distance from current station
			stationsWithDistances.sort((t1, t2) -> t1.getDistance().compareTo(t2.getDistance()));
			
			//Extract the stations from the set
			for (Tuple t : stationsWithDistances) {
				int station = t.getStation();
				Station newStation = board.getStations().get(station-1);
				closestTubeStations.add(newStation);
			}
			
			det.setClosestTubeStations(closestTubeStations);
		}
		
	}
	
	
	/** 
	 * Coordinates the tube stations detectives are aiming for on the first 2 moves, making 
	 * sure they are all aiming for a different tube station.
	 * @return	A list of coordinated tube stations. 
	 */
	private List<Integer> coordinateClosestTubeStations() {
		
		List<Integer> coordinatedStations = new ArrayList<Integer>();
		Set<Integer> closestTubesSet = new HashSet<Integer>();
		
		for (Detective det : detectives) {
			boolean tryToAdd = false;
			for (int i = 0; i < det.getClosestTubeStations().size(); i++) {
				Station station = det.getClosestTubeStations().get(i);
				tryToAdd = closestTubesSet.add(station.getNameInt());
				if (tryToAdd == true) {
					coordinatedStations.add(station.getNameInt());
					break;
				}
			}
		}
		return coordinatedStations;
	}
	
	
	/**
	 * Calculates the average distance of all detectives to Mr. X.
	 * @return average distance of all detectives to Mr. X.
	 */
	public double calculateAverageDistanceDetectives() {
		
		double averageDistance = 0;
		int nrDetectives = detectives.size();
		
		for (Detective det : detectives) {
			int dist = board.returnShortestDistance(det.getCurrentPosition(), mrX.getCurrentStation());	
			averageDistance += dist;
		}
		return (averageDistance / nrDetectives);
	}
	
	public Board getBoard() {
		return board;	
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	public List<Detective> getDetectives() {
		return detectives;
	}
	
	public void setDetectives(List<Detective> detectives) {	
		this.detectives = detectives;
	}
	
	public MrX getMrX() {	
		return mrX;
	}
	
	public void setMrX(MrX mrX) {	
		this.mrX = mrX;
	}
	
	public List<List<Move>> getAllPossibleMoveCombosDetectives() {	
		return allPossibleMoveCombosDetectives;
	}
	
	public void setAllPossibleMoveCombosDetectives(List<List<Move>> allPossibleMoveCombosDetectives) {	
		this.allPossibleMoveCombosDetectives = allPossibleMoveCombosDetectives;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(detectives.toString());
		sb.append(mrX.toString());
		return sb.toString();
	}
	
}
