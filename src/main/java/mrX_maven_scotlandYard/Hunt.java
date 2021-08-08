package mrX_maven_scotlandYard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.rits.cloning.Cloner;

import mrX_maven_game.GameState;
import mrX_maven_game.Move;
import mrX_maven_game.Station;
import mrX_maven_players.Detective;
import mrX_maven_utilities.DistancesFileParser;
import mrX_maven_utilities.TreeNode;


/**This class represents the main overview logic of the program.
 * 
 * @author aljaz
 *
 */
public class Hunt {
	
	private static Scanner sc = new Scanner(System.in);
	private static int moves = 24;
	private static int mrXLocation;
	
	
	public static Move findSecondMove(int nextNeighbour, Station destination, List<Integer> distancesFromNextNeighbour, List<Integer> distancesFromDestination, int distance, Station neighbourStation, GameState hunter) {
		Move secondMove = null;
		//If distance is 1 then we can just save the second move straight to the destination
		if (nextNeighbour < destination.getNameInt()) {
			int distToDest = distancesFromNextNeighbour.get(destination.getNameInt()-nextNeighbour-1);
			if (distToDest < distance) {
				distance = distance - 1;
				secondMove = new Move (neighbourStation, hunter.getStation(nextNeighbour-1), "Taxi");
			}
		}
		else {
			int distanceToDest = distancesFromDestination.get(nextNeighbour-destination.getNameInt());
			if (distanceToDest < distance) {
				distance = distance - 1;
				secondMove = new Move (neighbourStation, hunter.getStation(nextNeighbour-1), "Taxi");
			}
		}
		return secondMove;
	}
	
	public static List<Move> findMoves(int neighbour, Station destination, List<Integer> distancesFromNeighbour, List<Integer> distancesFromDestination, int distance, GameState hunter, Station startingStation, List<List<Integer>> parsedDistances) {
		List<Move> movesFound = new ArrayList<Move>();
		if (neighbour < destination.getNameInt()) {
			int distanceToDest = distancesFromNeighbour.get(destination.getNameInt()-neighbour-1);
			if (distanceToDest < distance) {
				distance = distance - 1;
				Move firstMove = new Move(startingStation, hunter.getStation(neighbour-1), "Taxi");
				movesFound.add(firstMove);
				Station neighbourStation = hunter.getStation(neighbour-1);
				Move secondMove = null;
				
				if (distance == 1) {
					
					for (int i = 0; i < neighbourStation.getNumberTaxiConnections(); i++) {
						Station finalStation = hunter.getStation(neighbourStation.getTaxiNeighbours().get(i)-1);
						if (finalStation.getNameInt() == destination.getNameInt()) {
							secondMove = new Move(neighbourStation, finalStation, "Taxi");
							movesFound.add(secondMove);
						}
					}
					if (secondMove == null) {
						for (int i = 0; i < neighbourStation.getNumberBusConnections(); i++) {
							Station finalStation = hunter.getStation(neighbourStation.getBusNeighbours().get(i)-1);
							if (finalStation.getNameInt() == destination.getNameInt()) {
								secondMove = new Move(neighbourStation, finalStation, "Bus");
								movesFound.add(secondMove);
							}
						}
						if (secondMove == null) {
							for (int i = 0; i < neighbourStation.getNumberTubeConnections(); i++) {
								Station finalStation = hunter.getStation(neighbourStation.getTubeNeighbours().get(i)-1);
								if (finalStation.getNameInt() == destination.getNameInt()) {
									secondMove = new Move(neighbourStation, finalStation, "Tube");
									movesFound.add(secondMove);
								}
							}
						}
					}
				}
				
				else {
					for (int m = 0; m < neighbourStation.getNumberTaxiConnections(); m ++) {
						int nextNeighbour = neighbourStation.getTaxiNeighbours().get(m);
						List<Integer> distancesFromNextNeighbour = parsedDistances.get(nextNeighbour-1);
						secondMove = findSecondMove(nextNeighbour, destination, distancesFromNextNeighbour, distancesFromDestination, distance, neighbourStation, hunter);
						if (secondMove != null && movesFound.size() < 2) {
							movesFound.add(secondMove);
						}
					}
					if (movesFound.size() < 2) {
						for (int m = 0; m < neighbourStation.getNumberBusConnections(); m ++) {
							int nextNeighbour = neighbourStation.getBusNeighbours().get(m);
							List<Integer> distancesFromNextNeighbour = parsedDistances.get(nextNeighbour-1);
							secondMove = findSecondMove(nextNeighbour, destination, distancesFromNextNeighbour, distancesFromDestination, distance, neighbourStation, hunter);
							if (secondMove != null && movesFound.size() < 2) {
								movesFound.add(secondMove);
							}
						}
						for (int m = 0; m < neighbourStation.getNumberTubeConnections(); m ++) {
							int nextNeighbour = neighbourStation.getTubeNeighbours().get(m);
							List<Integer> distancesFromNextNeighbour = parsedDistances.get(nextNeighbour-1);
							secondMove = findSecondMove(nextNeighbour, destination, distancesFromNextNeighbour, distancesFromDestination, distance, neighbourStation, hunter);
							if (secondMove != null && movesFound.size() < 2) {
								movesFound.add(secondMove);
							}
						}		
					}
				}
			}
		}
		
		else {
			int distToDest = distancesFromDestination.get(neighbour-destination.getNameInt()-1);
			if (distToDest < distance) {
				distance = distance - 1;
				Move firstMove = new Move(startingStation, hunter.getStation(neighbour-1), "Taxi");
				movesFound.add(firstMove);
				Station neighbourStation = hunter.getStation(neighbour-1);
				Move secondMove = null;
				
				if (distance == 1) {
					
					for (int i = 0; i < neighbourStation.getNumberTaxiConnections(); i++) {
						Station finalStation = hunter.getStation(neighbourStation.getTaxiNeighbours().get(i)-1);
						if (finalStation.getNameInt() == destination.getNameInt()) {
							secondMove = new Move(neighbourStation, finalStation, "Taxi");
							movesFound.add(secondMove);
						}
					}
					if (secondMove == null) {
						for (int i = 0; i < neighbourStation.getNumberBusConnections(); i++) {
							Station finalStation = hunter.getStation(neighbourStation.getBusNeighbours().get(i)-1);
							if (finalStation.getNameInt() == destination.getNameInt()) {
								secondMove = new Move(neighbourStation, finalStation, "Bus");
								movesFound.add(secondMove);
							}
						}
						if (secondMove == null) {
							for (int i = 0; i < neighbourStation.getNumberTubeConnections(); i++) {
								Station finalStation = hunter.getStation(neighbourStation.getTubeNeighbours().get(i)-1);
								if (finalStation.getNameInt() == destination.getNameInt()) {
									secondMove = new Move(neighbourStation, finalStation, "Tube");
									movesFound.add(secondMove);
								}
							}
						}
					}
				}
				
				else {
					for (int m = 0; m < neighbourStation.getNumberTaxiConnections(); m ++) {
						int nextNeighbour = neighbourStation.getTaxiNeighbours().get(m);
						List<Integer> distancesFromNextNeighbour = parsedDistances.get(nextNeighbour-1);
						secondMove = findSecondMove(nextNeighbour, destination, distancesFromNextNeighbour, distancesFromDestination, distance, neighbourStation, hunter);
						if (secondMove != null && movesFound.size() < 2) {
							movesFound.add(secondMove);
						}
					}
					for (int m = 0; m < neighbourStation.getNumberBusConnections(); m ++) {
						int nextNeighbour = neighbourStation.getBusNeighbours().get(m);
						List<Integer> distancesFromNextNeighbour = parsedDistances.get(nextNeighbour-1);
						secondMove = findSecondMove(nextNeighbour, destination, distancesFromNextNeighbour, distancesFromDestination, distance, neighbourStation, hunter);
						if (secondMove != null && movesFound.size() < 2) {
							movesFound.add(secondMove);
						}
					}
					for (int m = 0; m < neighbourStation.getNumberTubeConnections(); m ++) {
						int nextNeighbour = neighbourStation.getTubeNeighbours().get(m);
						List<Integer> distancesFromNextNeighbour = parsedDistances.get(nextNeighbour-1);
						secondMove = findSecondMove(nextNeighbour, destination, distancesFromNextNeighbour, distancesFromDestination, distance, neighbourStation, hunter);
						if (secondMove != null && movesFound.size() < 2) {
							movesFound.add(secondMove);
						}
					}
				}
				
			}
		}
		return movesFound;
	}
	
	//TODO: make sure they are not aiming for the same tube station
	
	public static void findPath(int distance, Station startingStation, List<List<Integer>> parsedDistances, Station destination, GameState hunter, Detective det) {
		List<Move> moves = new ArrayList<Move>();
		
		for (int k = 0; k < startingStation.getNumberTaxiConnections(); k++) {
			int neighbour = startingStation.getTaxiNeighbours().get(k);
			List<Integer> distancesFromNeighbour = parsedDistances.get(neighbour-1);
			List<Integer> distancesFromDestination = parsedDistances.get(destination.getNameInt()-1);
			moves = findMoves(neighbour, destination, distancesFromNeighbour, distancesFromDestination, distance, hunter, startingStation, parsedDistances);
			if (det.getPath() == null && moves.size()==2) {
				det.setPath(moves);
			}
		}
		
		for (int k = 0; k < startingStation.getNumberBusConnections(); k++) {
			int neighbour = startingStation.getBusNeighbours().get(k);
			List<Integer> distancesFromNeighbour = parsedDistances.get(neighbour-1);
			List<Integer> distancesFromDestination = parsedDistances.get(destination.getNameInt()-1);
			moves = findMoves(neighbour, destination, distancesFromNeighbour, distancesFromDestination, distance, hunter, startingStation, parsedDistances);
			if (det.getPath() == null && moves.size()==2) {
				det.setPath(moves);
			}
		}
		
		for (int k = 0; k < startingStation.getNumberTubeConnections(); k++) {
			int neighbour = startingStation.getTubeNeighbours().get(k);
			List<Integer> distancesFromNeighbour = parsedDistances.get(neighbour-1);
			List<Integer> distancesFromDestination = parsedDistances.get(destination.getNameInt()-1);
			moves = findMoves(neighbour, destination, distancesFromNeighbour, distancesFromDestination, distance, hunter, startingStation, parsedDistances);
			if (det.getPath() == null && moves.size()==2) {
				det.setPath(moves);
			}
		}

	}
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
		
		GameState hunter = new GameState();
		
		System.out.println("Welcome to The Hunt for Mr X, the codebreaker for the game Scotland Yard!\n");
		
		DistancesFileParser parser = new DistancesFileParser("src/main/resources/seekers_distances.xml");
		List<List<Integer>> parsedDistances = parser.getParsedData();
		

		
		hunter.setupFullGame();
		hunter.initialMrXPossibleStations();
		
		
		for(int step = 1; step < moves+1; step++) {
			
			System.out.println(" ***** MOVE NR. " + (step) + " *****\n");
						
			String ticketUsed = hunter.moveMrX(step, parsedDistances);
			
			
			
			
			//TODO: make variations of algorithm for every step 
			//FIND THE NEAREST TUBE STATION FOR EVERY DETECTIVE AND COORDINATE THEM MOVING THERE OVER
			//2 MOVES
			
			if (step == 1) {
				//MAKE THE FIRST MOVE
				
				hunter.coordinatePossibleDetectiveMoves(step, parsedDistances);
				hunter.generateAllPossibleMoveCombosDetectives(hunter.getAllPossibleDetectiveMoves(), parsedDistances, step);
				
				
				for (int i = 0; i < hunter.getNrOfDetectives(); i++) {
					
					List<Station> perfectDistance = new ArrayList<Station>();
					List<Station> lessPerfectDistance = new ArrayList<Station>();
					List<Station> perfectTubeStations = new ArrayList<Station>();
					List<Station> lessPerfectTubeStations = new ArrayList<Station>();
					List<Move> path = new ArrayList<Move>();
					
					Detective det = hunter.getListDetectives().get(i);
					Station startingStation = hunter.getStation(det.getStartPosition()-1);
					if (startingStation.getStationType().equals("Tube")) {
						//Move a station away and then back
						Station neighbourTaxi = hunter.getStation(startingStation.getTaxiNeighbours().get(0)-1);
						Move firstMove = new Move(startingStation, neighbourTaxi, "Taxi");
						Move secondMove = new Move(neighbourTaxi, startingStation, "Taxi");
						path.add(firstMove);
						path.add(secondMove);
						det.setPath(path);
					}
					
					else {
						//Find all the stations that are at a distance of 2 or 3 
						//Save them in 2 separate lists
						//The problem here is we only look at tube stations that have a higher number than the starting station
						List<Integer> distancesFromStartingStation = parsedDistances.get(startingStation.getNameInt()-1);
						//System.out.println("Distances from starting station " + startingStation.getNameInt() + " = " + distancesFromStartingStation);
						for (int j = 0; j < distancesFromStartingStation.size(); j++) {
							int distance = distancesFromStartingStation.get(j);
							if (distance == 2) {
								perfectDistance.add(hunter.getStation(startingStation.getNameInt()+j));
							}
							if (distance == 3) {
								lessPerfectDistance.add(hunter.getStation(startingStation.getNameInt()+j));
							}
						}
						
						//Loop through the 2 lists and save the tube stations in 2 separate lists
						for (int k = 0; k < perfectDistance.size(); k++) {
							Station station = perfectDistance.get(k);
							if (station.getStationType().equals("Tube")) {
								perfectTubeStations.add(station);
							}
						}
						
						for (int m = 0; m < lessPerfectDistance.size(); m++) {
							Station station = lessPerfectDistance.get(m);
							if (station.getStationType().equals("Tube")) {
								lessPerfectTubeStations.add(station);
							}
						}
						
						//Find the distances to the starting station from tube stations with lower number 
						List<Station> tubeStations = new ArrayList<Station>();
						//List<Integer> distancesFromTubeStations = new ArrayList<Integer>();
						for (int t = 0; t < startingStation.getNameInt(); t++) {
							Station station = hunter.getStation(t);
							if (station.getStationType().equals("Tube")) {
								tubeStations.add(station);
							}
						}
						
						for (int l = 0; l < tubeStations.size(); l++) {
							List<Integer> distances = parsedDistances.get(tubeStations.get(l).getNameInt()-1);
							int dist = distances.get(startingStation.getNameInt()-tubeStations.get(l).getNameInt()-1);
							if(dist == 2) {
								perfectTubeStations.add(tubeStations.get(l));
							}
							if(dist == 3) {
								lessPerfectTubeStations.add(tubeStations.get(l));
							}
						}
					}
					
					det.setPerfectTubeStations(perfectTubeStations);
					det.setLessPerfectTubeStations(lessPerfectTubeStations);
					//System.out.println(det);
					
					//Choose a tube station to move to - ideally one from perfect tube stations with the highest value that is not occupied
					
					if (det.getPath() != null) {
						continue;
					}
					
					
					if (det.getPerfectTubeStations().size() > 0) {
						
						//Find the station with the highest value
						int maxValue = 0;
						Station destination = null;
						
						for (int j = 0; j < det.getPerfectTubeStations().size(); j++) {
							
							Station station = det.getPerfectTubeStations().get(j);
							//maxValue = (maxValue < station.getValue()) ? station.getValue() : maxValue;
							if (maxValue < station.getValue()) {
								maxValue = station.getValue();
								destination = station;
							}
						}
						
						//System.out.println("Destination tube station = " + destination);
						
						int distance = 2;
						findPath(distance, startingStation, parsedDistances, destination, hunter, det);
						
					}
					
					else {
						//Find the station with the highest value
						int maxValue = 0;
						Station destination = null;
						
						for (int j = 0; j < det.getLessPerfectTubeStations().size(); j++) {	
							Station station = det.getLessPerfectTubeStations().get(j);
							//maxValue = (maxValue < station.getValue()) ? station.getValue() : maxValue; 
							if (maxValue < station.getValue()) {
								maxValue = station.getValue();
								destination = station;
							}
						}
						
						//System.out.println("Destination tube station = " + destination);
						
						//Find the path to the chosen tube station and save it
						//Loop through all neighbouring stations to start station
						//Check their distance to destination
						//If it is shorter than 3 add the move to the path
						//When adding the move, do the same for the new destination
						int distance = 3;
						findPath(distance, startingStation, parsedDistances, destination, hunter, det);
						
					}
					
				}
				
				//TODO: make sure the first two and the second two stations 
				//on the paths of detectives are not the same
				List<List<Move>> listPaths = new ArrayList<List<Move>>();
				
				for (Detective detective : hunter.getListDetectives()) {
					listPaths.add(detective.getPath());
				}
				
				Set<Station> checkMoves = new HashSet<Station>();
				
				for (int i = 0; i < listPaths.size(); i++) {
					for (int j = 0; j < 2; j++) {
						Move move = listPaths.get(i).get(j);
						Station dest = move.getDestinationStation();
						checkMoves.add(dest);
					}
				}
				
				if (checkMoves.size() == listPaths.size()*2) {
					//System.out.println("ALL GOOD, MAKE THE MOVES!");
				}
				else {
					//System.out.println("ALERT, DUPLICATE DESTINATIONS!");
				}
				
				//Make the first move on the path
				for (Detective detective : hunter.getListDetectives()) {
					boolean checkIfMoveMade = hunter.moveDetective(detective, detective.getPath().get(0), hunter.getStations());
					if(checkIfMoveMade) {
						System.out.println("Please move detective " + detective.getName() + " " + detective.getPath().get(0));
					}
					else {
						detective.setPath(null);
						for (Move firstMove : detective.getPossibleMovesCurrentStation()) {
							boolean tryMove = hunter.moveDetective(detective, firstMove, hunter.getStations());
							if (tryMove == true) {
								System.out.println("Please move detective " + detective.getName() + " " + firstMove);
								//TODO: find second move
								List<Move> path = new ArrayList<Move>();
								path.add(firstMove);
								detective.setPath(path);
								Station currentStation = hunter.getStation(detective.getCurrentPosition()-1);
								for (int neighbour : currentStation.getTaxiNeighbours()) {
									Station dest = hunter.getStation(neighbour-1);
									Move secondMove = new Move(currentStation, dest, "Taxi");
									path.add(secondMove);
									detective.setPath(path);
									break;
								}
								break;
							}
						}
					}
				}
				//System.out.println(hunter.getListDetectives());
				
				continue;
				
			}
			
			if (step == 2) {
				//MAKE THE SECOND MOVE
				for (Detective det : hunter.getListDetectives()) {
					hunter.moveDetective(det, det.getPath().get(1), hunter.getStations());
					System.out.println("Please move detective " + det.getName() + " " + det.getPath().get(1));
					
				}
				continue;
			}
			
			//Reset the paths and other attributes used for the first 2 moves
			for (Detective det : hunter.getListDetectives()) {
				det.setLessPerfectTubeStations(null);
				det.setPerfectTubeStations(null);
				det.setPath(null);
			}
			
			
			// TODO: CHECK THIS 
			if (step == 3 || step == 8 || step == 13 || step == 18 || step == 24) {
				
				System.out.println("\nMr. X, reveal yourself!\n");
				System.out.println("Where is Mr. X at the moment?\n");
				mrXLocation = sc.nextInt();
				hunter.reveal(mrXLocation);
				List<Integer> possibleStations = new ArrayList<Integer>();
				possibleStations.add(mrXLocation);
				hunter.setNewPossibleMrXstations(possibleStations);
				hunter.getMrX().setSimulatedCurrentStation(mrXLocation);
//				hunter.findMrXPossibleMoves(step, parsedDistances);
//				hunter.coordinatePossibleDetectiveMoves();
//				hunter.generateAllPossibleMoveCombosDetectives(hunter.getAllPossibleDetectiveMoves(), parsedDistances, step);
				
				if (hunter.getWinningMove() != null) {
					System.out.println("WINNING MOVE = " + hunter.getWinningMove());
				}
				
				//BEGIN THE SEARCH IN EARNEST
				
			}
			
			hunter.coordinatePossibleDetectiveMoves(step, parsedDistances);
			hunter.generateAllPossibleMoveCombosDetectives(hunter.getAllPossibleDetectiveMoves(), parsedDistances, step);
			hunter.findMrXPossibleStations(ticketUsed, step);
			hunter.findMrXPossibleMoves(step, parsedDistances);
			
			//System.out.println(hunter);
			
			
			if (hunter.getAllPossibleMoveCombosDetectives().size()==1) {
				List<Move> bestMoves = new ArrayList<Move>();
				for (int i = 0; i < hunter.getAllPossibleMoveCombosDetectives().get(0).size(); i++) {
					Detective det = hunter.getListDetectives().get(i);
					Move move = hunter.getAllPossibleMoveCombosDetectives().get(0).get(i);
					bestMoves.add(move);
					hunter.setBestDetMoves(bestMoves);
					String detName = det.getName();
					hunter.moveDetective(det, move, hunter.getStations());
					System.out.println("Detective " + detName + " : " + move);
				}
				
//				if (step != 3 || step != 8 || step != 13 || step != 18 || step != 24) {
//					hunter.findMrXPossibleStations(ticketUsed, step);
//					hunter.findMrXPossibleMoves(step, parsedDistances);
//					hunter.coordinatePossibleDetectiveMoves();
//					hunter.generateAllPossibleMoveCombosDetectives(hunter.getAllPossibleDetectiveMoves(), parsedDistances, step);
//				}
				
				if(hunter.noMovesLeftCheck()) {
					break;
				}

			}
			
			else {
				//Clone Hunter
				Cloner cloner = new Cloner();
				TreeNode<GameState> rootNode = new TreeNode<GameState>(hunter, cloner);
				double bestScore = Double.NEGATIVE_INFINITY;
				
				
				//System.out.println("POSSIBLE MR. X MOVES = " + hunter.getPossibleMrXmoves().size());
				
				for (int i = 0; i < hunter.getPossibleMrXmoves().size(); i++) {
					Move move = hunter.getPossibleMrXmoves().get(i);
					//System.out.println("THE SIMULATED MOVE nr. " + i + " = " + move);
					GameState clonedState = rootNode.getDeepCloneOfRepresentedState();
					clonedState.simulateMrXmove(move, step, parsedDistances);
					TreeNode<GameState> startNode = new TreeNode<GameState>(clonedState, cloner);
					rootNode.addChild(startNode);
					double score = clonedState.miniMax(0, false, startNode, cloner, parsedDistances, step, hunter.getStations());
					//bestScore = Math.max(score, bestScore);
					if (score >= bestScore) {
						bestScore = score;
						rootNode.setBestCombo(startNode.getBestCombo());
					}
					
					rootNode.setNodeEvaluation(bestScore);
				}
				
				List<Move> bestCom = rootNode.getBestCombo();
				
				
				hunter.setBestDetMoves(bestCom);
				
				for (int i = 0; i < hunter.getBestDetectiveMoves().size(); i++) {
					Detective det = hunter.getListDetectives().get(i);
					Move move = hunter.getBestDetectiveMoves().get(i);
					String detName = det.getName();
					System.out.println("Detective " + detName + " : " + move);
					hunter.moveDetective(det, move, hunter.getStations());
				}
			
			
			//System.out.println("ORIGINAL HUNTER \n= " + hunter + "\n");
			
			}

		}
	}
}
