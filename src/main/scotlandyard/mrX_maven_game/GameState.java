package mrX_maven_game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.rits.cloning.Cloner;

import mrX_maven_players.Detective;
import mrX_maven_players.MrX;
import mrX_maven_utilities.TreeNode;

import java.util.*;


/**This class represents the main logic for hunting Mr. X.
 * 
 * @author aljaz
 *
 */

public class GameState {
	
	private Scanner sc = new Scanner(System.in);
	private int numberOfDetectives;
	private int numberOfPolice;
	private List<Station> stationsList = new ArrayList<Station>();
	private MrX mrX = new MrX(stationsList, 4, 3, 3);
	private List<Integer> possibleStartingStationsDetectives = 
			Arrays.asList(13, 26, 29, 34, 50, 53, 91, 94, 103, 112, 117, 123, 138, 141, 155, 174);
	private List<Integer> possibleStartingStationsDetectivesDemo = Arrays.asList(2, 4);
	private List<Detective> listDetectives = new ArrayList<Detective>();
	private List<Integer> possibleStartingStationsMrX = Arrays.asList(35, 45, 51, 71, 78, 104, 106, 127, 132, 146, 166, 170, 172);
	private List<Integer> possibleStartingStationsMrXdemo = Arrays.asList(1, 3, 5);
	private List<Integer> possibleMrXstations = new ArrayList<Integer>();
	private List<Move> possibleMrXmoves = new ArrayList<Move>();
	private List<List<Move>> allPossibleDetectiveMoves = new ArrayList<List<Move>>();
	private List<List<Move>> allPossibleMoveCombosDetectives = new ArrayList<List<Move>>();
	private List<Move> bestDetectiveMoves = new ArrayList<Move>();
	private Move winningMove = null;
	
	
	public GameState() {
		
	}
	
	/**A constructor for the clone of the Hunter object. 
	 * 
	 * @param original
	 */
	public GameState(GameState original) {
		this.mrX = original.mrX;
		this.numberOfDetectives = original.numberOfDetectives;
		this.numberOfPolice = original.numberOfPolice;
		this.stationsList = original.stationsList;
		this.listDetectives = original.listDetectives;
		this.possibleStartingStationsDetectives = original.possibleStartingStationsDetectives;
		this.possibleStartingStationsDetectivesDemo = original.possibleStartingStationsDetectivesDemo;
		this.possibleStartingStationsMrX = original.possibleStartingStationsMrX;
		this.possibleStartingStationsMrXdemo = original.possibleStartingStationsMrXdemo;
		this.possibleMrXstations = original.possibleMrXstations;
		this.possibleMrXmoves = original.possibleMrXmoves;
		this.allPossibleDetectiveMoves = original.allPossibleDetectiveMoves;
		this.allPossibleMoveCombosDetectives = original.allPossibleMoveCombosDetectives;
		this.bestDetectiveMoves = original.bestDetectiveMoves;
	}
	
	
	/**Get an array list of stations.
	 * 
	 * @return	an array list of stations.
	 */
	public List<Station> getStations() {
		return stationsList;
	}
	
	/**Get a specific station.
	 * 
	 * @param loc	the station to be returned.
	 * @return		the station.
	 */
	public Station getStation(int station) {
		return (stationsList.get(station));
	}
	
	/**Load the demo stations from the JSON file.
	 *
	 *@return	ArrayList of stations
	 * @throws 	FileNotFoundException 
	 */
	public List<Station> loadStationsDemo() throws FileNotFoundException {
		
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader("src/main/resources/demoStations.json"));
		List<Station> stations = gson.fromJson(reader, new TypeToken<ArrayList<Station>>(){}.getType());
		
		//stations.remove(0);
		this.stationsList = stations;
		evaluateStations(stations);
		return stations;
	}
	
	/**Load the stations from the JSON file.
	 *
	 *@return	ArrayList of stations
	 * @throws 	FileNotFoundException 
	 */
	public List<Station> loadStations() throws FileNotFoundException {
		
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader("src/main/resources/stations.json"));
		List<Station> stations = gson.fromJson(reader, new TypeToken<ArrayList<Station>>(){}.getType());
		
		stations.remove(0);
		this.stationsList = stations;
		evaluateStations(stations);
		return stations;
	}
	
	//TODO: change the heuristic function. Replace it with a function that tries to minimize the distance to Mr. X.
	/**A simple evaluation formula for a station, based on number and type of connections from it.
	 * 
	 * @param stations
	 */
	public void evaluateStations(List<Station> stations) {
		
		for (int i = 0; i < stations.size(); i++) {
			Station station = stations.get(i);
			int value = station.getNumberTaxiConnections()*10 + station.getNumberBusConnections()*15 + station.getNumberTubeConnections()*20;
			station.setValue(value);
		}
	}
	
	/**Print all the stations on the map. 
	 * @throws FileNotFoundException 
	 * 
	 */
	public void printStations(List<Station> stations) throws FileNotFoundException {
		System.out.println("Here is the current map we are working with: \n");
		for(int i = 0; i < stations.size(); i++) {
			Station station = stations.get(i);
			System.out.println(station);
		}
	}
	
	/**Set the detectives list.
	 * 
	 * @param listDetectives
	 */
	public void setDetectives(List<Detective> listDetectives) {
		this.listDetectives = listDetectives;
	}
	
	/**Set the stations list.
	 * 
	 * @param stationsList
	 */
	public void setStations(List<Station> stationsList) {
		this.stationsList = stationsList;
	}
	
//	//TODO: remove map and just set the stations directly in a list of stations in Hunter?
//	public void setStations(List<Stations> listStations) {
//		
//	}
	

	/**Get an instance of Mr. X.
	 * 
	 * @return		an instance of Mr. X. 
	 */
	public MrX getMrX() {
		return mrX;
	}

	
	/**Get the number of detectives playing.
	 * 
	 * @return		number of detectives
	 */
	public int getNrOfDetectives() {
		return numberOfDetectives;
	}
	
	/**Get the list of detectives playing.
	 * 
	 * @return		list of detectives playing. 
	 */
	public List<Detective> getListDetectives() {
		return listDetectives;
	}
	
	/** Get the list of possible starting stations for detectives.
	 * 
	 * @return	list of possible starting stations of detectives.
	 */
	public List<Integer> getPossibleStartingStationsDetectives() {
		return possibleStartingStationsDetectives;
	}
	
	/** Get the list of possible starting stations for detectives for the dmeo.
	 * 
	 * @return	list of possible starting stations of detectives for the demo. 
	 */
	public List<Integer> getPossibleStartingStationsDetectivesDemo() {
		return possibleStartingStationsDetectivesDemo;
	}

	
	/** Get the starting stations of Mr. X.
	 * 
	 * @return	the starting stations of Mr. X. 
	 */
	public List<Integer> getPossibleStartingStationsMrX() {
		return possibleStartingStationsMrX;
	}
	
	/** Get the starting stations of Mr. X for the demo.
	 * 
	 * @return	the starting stations of Mr. X for the demo. 
	 */
	public List<Integer> getPossibleStartingStationsMrXdemo() {
		return possibleStartingStationsMrXdemo;
	}
	
	/** Get the list of all possible Mr. X stations. 
	 * 
	 * @return	the list of new possible Mr. X stations.
	 */
	public List<Integer> getPossibleMrXstations() {
		return possibleMrXstations;
	}
	
	public void setNewPossibleMrXstations(List<Integer> possibleMrXstations) {
		this.possibleMrXstations = possibleMrXstations;
	}
	
	/** Get the list of possible Mr. X moves. 
	 * 
	 * @return	the list of possible Mr. X moves.
	 */
	public List<Move> getPossibleMrXmoves() {
		return possibleMrXmoves;
	}
	
	/**Get all possible combinations of detective moves.
	 * 
	 * @return	all possible combinations of detective moves. 
	 */
	public List<List<Move>> getAllPossibleDetectiveMoves() {
		return this.allPossibleDetectiveMoves;
	}
	
	/**Get all possible move combos for detectives.
	 * 
	 * @return	all possible move combos for detectives. 
	 */
	public List<List<Move>> getAllPossibleMoveCombosDetectives() {
		return this.allPossibleMoveCombosDetectives;
	}
	
	/**Get the best detective moves.
	 * 
	 * @return	the best detective moves. 
	 */
	public List<Move> getBestDetectiveMoves() {
		return this.bestDetectiveMoves;
	}
	
	/**Set the best detective moves.
	 * 
	 * @param bestDetMoves
	 */
	public void setBestDetMoves(List<Move> bestDetMoves) {
		this.bestDetectiveMoves = bestDetMoves;
	}
	
	/**Set up the detectives, including their number and names.
	 * 
	 * @return		the number of detectives playing. 
	 */
	public int setupDetectives(List<Station> stations) {
		
		while(true) {
			System.out.println("Please enter the number of detectives: ");
			int numberOfDetectives = sc.nextInt();
				if(numberOfDetectives > 5) {
					System.out.println("The max number of detectives is 5!");
					continue;
				}
				else if(numberOfDetectives < 2) {
					System.out.println("The min number of detectives is 2!");
					continue;
				}
				else {
					for (int number = 1; number < numberOfDetectives+1; number+=1) {
						System.out.println("Name of detective nr. " + number + ": ");
						String nameDetective = sc.next();
						
						System.out.println("Please give start location of detective " + nameDetective + ":");
						while(true) {
							int startPos = sc.nextInt();
							if(possibleStartingStationsDetectives.contains(startPos)) {
								Detective detective = createDetective(number, nameDetective, startPos, 10, 8, 4, stationsList);
								detective.setCurrentPosition(startPos);
								break;
							}
							System.out.println("That is not a valid starting position!");
						}
					}
				}
				this.numberOfDetectives = numberOfDetectives;
				return numberOfDetectives;
		}
	}
	
	
	/**Set up the detectives for the demo, including their number and names.
	 * 
	 * @return		the number of detectives playing. 
	 */
	public int setupDetectivesDemo(List<Station> stations) {
		
		while(true) {
			System.out.println("Please enter the number of detectives: ");
			int numberOfDetectives = sc.nextInt();
				if(numberOfDetectives > 5) {
					System.out.println("The max number of detectives is 5!");
					continue;
				}
				else if(numberOfDetectives < 2) {
					System.out.println("The min number of detectives is 2!");
					continue;
				}
				else {
					for (int number = 1; number < numberOfDetectives+1; number+=1) {
						System.out.println("Name of detective nr. " + number + ": ");
						String nameDetective = sc.next();
						
						System.out.println("Please give start location of detective " + nameDetective + ":");
						while(true) {
							int startPos = sc.nextInt();
							if(possibleStartingStationsDetectivesDemo.contains(startPos)) {
								Detective detective = createDetective(number, nameDetective, startPos, 10, 8, 4, stationsList);
								detective.setCurrentPosition(startPos);
								break;
							}
							System.out.println("That is not a valid starting position!");
						}
					}
				}
				this.numberOfDetectives = numberOfDetectives;
				return numberOfDetectives;
		}
	}

	
	//TODO(low): add check for setting a detective on non-existing station or already occupied station
	/**Create a detective.
	 * 
	 * @param number			integer representing the detective.
	 * @param name				name of the detective.
	 * @param startPosition		the start position of the detective. 
	 * @param taxiTickets		the number of taxi tickets the detective gets. 
	 * @param busTickets		the number of bus tickets the detective gets.
	 * @param tubeTickets		the number of tube tickets the detective gets.
	 * @return					the created detective. 
	 */
	public Detective createDetective(int number, String name, int startPosition, int taxiTickets, int busTickets, int tubeTickets, List<Station> stationsList) {
		Detective newDetective = new Detective(number, name, startPosition, taxiTickets, busTickets, tubeTickets, stationsList);
		listDetectives.add(newDetective);
		getStation(startPosition-1).occupyStation();
		return newDetective;
	}
	
	
	// TODO: fix this and remember to occupy start locations of police officers too! 
	/**Set up the police officers.
	 * 
	 * @param numberOfDetectives	the number of detectives playing. 
	 * @return						the number of police officers assigned. 
	 */
	public int setupPolice(int numberOfDetectives) {
		if (numberOfDetectives == 2) {
			numberOfPolice = 2;
			System.out.println("You get 2 police officers as backup help.");
			return numberOfPolice;
		}
		else if (numberOfDetectives == 3) {
			numberOfPolice = 1;
			System.out.println("You get 1 police officer as backup help.");
			return numberOfPolice;
		}
		else {
			numberOfPolice = 0;
			System.out.println("You get no police officers as backup help.");
			return numberOfPolice;
		}
	}
	
	
//	// TODO(low): refactor this into one function (setupPolice()) and 
//	// add check for setting a police officer on non-existing station or already occupied station 
//	public ArrayList<Integer> setupStartPosPolice(int numberOfPolice) {
//		for(int i = 1; i < numberOfPolice+1; i+=1) {
//			System.out.println("Start location of police officer nr. " + i);
//			int policeStartPosition = sc.nextInt();
//			startPosPolice.add(policeStartPosition);
//			System.out.println("Start location of police officer nr. " + i + ": " + policeStartPosition);
//		}
//		return startPosPolice;
//	}
	
	/**Set up the full game.
	 * 
	 */
	public void setupFullGame() throws FileNotFoundException {
		
		List<Station> stations = loadStations();
		setupDetectives(stations);
		for (int i = 0; i < listDetectives.size(); i++) {
			Detective detective = listDetectives.get(i);
			detective.findPossibleMovesDetective(stations);
		}
		//mrX.setStations(stations);
		printStations(stations);
		
	}
	
	/**Set up the demo game, for development purposes.
	 * 
	 * @throws EmptyMapException
	 */
	public void setupDemo() throws FileNotFoundException {
		
		List<Station> stations = loadStationsDemo();
		setupDetectivesDemo(stations);
		for (int i = 0; i < listDetectives.size(); i++) {
			Detective detective = listDetectives.get(i);
			detective.findPossibleMovesDetective(stations);
		}
		//mrX.setStations(stations);
		printStations(stations);
	}

	
	
	
	/**Check if Mr. X has no moves left.
	 * If that is the case, GAME OVER.
	 * @return	true for no moves left, false otherwise.
	 */
	public boolean noMovesLeftCheck() {
			if (possibleMrXmoves.size() == 0) {
				return true;
			}
			return false;
	}
	
	/**Initialize all possible stations where Mr. X could be located at the start of the game. 
	 * 
	 */
	public void initialMrXPossibleStations() {
		
		this.possibleMrXstations = possibleStartingStationsMrX;
	}
	
	
	/**Initialize all possible stations where Mr. X could be located at the start of the game. 
	 * 
	 */
	public void initialMrXPossibleStationsDemo() {
		
		this.possibleMrXstations = possibleStartingStationsMrXdemo;
	}
	
	/**Find all possible stations where Mr. X could be located after using a certain type of ticket.
	 * 
	 * @param ticketUsed
	 */
	public void findMrXPossibleStations(String ticketUsed, int step) {
		//loop through the list of possible Mr. X stations
		//every station that can be reached with the ticketUsed and that is not occupied, 
		//add it to the list of new possible Mr. X stations.
		
		if (step == 3 || step == 8 || step == 13 || step == 18 || step == 24) {
			return;
		}
		
		else {
			List<Integer> possibleStations = new ArrayList<Integer>();
			if (ticketUsed.equals("Taxi")) {
				for (int i = 0; i < possibleMrXstations.size(); i++) {
					Station startStation = getStation(possibleMrXstations.get(i)-1);
					for (int j = 0; j < startStation.getNumberTaxiConnections(); j++) {
						Station destinationStation = getStation(startStation.getTaxiNeighbours().get(j)-1);
						if (destinationStation.isOccupied()==false && possibleStations.contains(destinationStation.getNameInt())==false) {
							possibleStations.add(destinationStation.getNameInt());
						}
					}
				}
			}
				if (ticketUsed.equals("Bus")) {
					for (int i = 0; i < possibleMrXstations.size(); i++) {
						Station startStation = getStation(possibleMrXstations.get(i)-1);
						for (int j = 0; j < startStation.getNumberBusConnections(); j++) {
							Station destinationStation = getStation(startStation.getBusNeighbours().get(j)-1);
							if (destinationStation.isOccupied()==false) {
								possibleStations.add(destinationStation.getNameInt());
							}
						}
					}
				}
				if (ticketUsed.equals("Tube")) {
					for (int i = 0; i < possibleMrXstations.size(); i++) {
						Station startStation = getStation(possibleMrXstations.get(i)-1);
						for (int j = 0; j < startStation.getNumberTubeConnections(); j++) {
							Station destinationStation = getStation(startStation.getTubeNeighbours().get(j)-1);
							if (destinationStation.isOccupied()==false) {
								possibleStations.add(destinationStation.getNameInt());
							}
						}
					}
				}
				
				possibleStations.sort(null);
				this.possibleMrXstations = possibleStations;
		}
			
	}
	
	//TODO: or remove all moves that would lead to a distance of 1 between Mr. X and any detective? 
	
	public List<Move> pruneMrXMoves(List<Move> possibleMoves, List<List<Integer>> parsedDistances) {
		
		List<Move> prunedMoves = possibleMoves;
		
		//Find the nearest detective
		//Remove all moves that do not increase the distance to him 
		
//		int nearestDistance = 1000; 
//		List<Integer> distances = new ArrayList<Integer>();
//		
//		for (Detective det : listDetectives) {
//			
//			int distance = 0;
//			List<Integer> distancesFromDet = parsedDistances.get(det.getCurrentPosition()-1);
//			List<Integer> distancesFromMrX = parsedDistances.get(mrX.getSimulatedCurrentStation()-1);
//			
//			if (det.getCurrentPosition() < mrX.getSimulatedCurrentStation()) {
//				distance = distancesFromDet.get(mrX.getSimulatedCurrentStation()-det.getCurrentPosition()-1);
//				
//			}
//			
//			else {
//				distance = distancesFromMrX.get(det.getCurrentPosition()-mrX.getSimulatedCurrentStation()-1);
//			}
//			
//			if (distance < nearestDistance) {
//				nearestDistance = distance;
//				
//			}
//		}
		
		List<Integer> distancesFromMrX = new ArrayList<Integer>();
		
		if (mrX.getSimulatedCurrentStation() != 199) {
			distancesFromMrX = parsedDistances.get(mrX.getSimulatedCurrentStation()-1);
		}
		
		List<Move> toRemove = new ArrayList<Move>();
		int distance = 0;
		
		for (Move move : possibleMoves) {
			Station destination = move.getDestinationStation();
			List<Integer> distancesFromDestination = parsedDistances.get(destination.getNameInt()-1);
			
			if (destination.getNameInt() < mrX.getSimulatedCurrentStation()) {
				distance = distancesFromDestination.get(mrX.getSimulatedCurrentStation()-destination.getNameInt()-1);
			}
			else {
				distance = distancesFromMrX.get(destination.getNameInt()-mrX.getSimulatedCurrentStation()-1);
			}
			
			if (distance == 1) {
				toRemove.add(move);
			}
			
		}
		
		prunedMoves.removeAll(toRemove);
		
		return prunedMoves;
		
		
	}
	
	/**Find all possible Mr. X moves from the simulated current station.
	 * 
	 * @param simulatedStation
	 */
	public void findMrXPossibleMoves(Station station, int step, List<List<Integer>> parsedDistances) {
		
		List<Move> possibleMoves = new ArrayList<Move>();
		
		for(int j = 0; j < station.getNumberTaxiConnections(); j++) {
			if (mrX.getAvailableTaxi()>0) {
				Station destinationStation = getStation(station.getTaxiNeighbours().get(j)-1);
				if (destinationStation.isOccupied()==false) {
					Move possibleMove = new Move(station, destinationStation, "Taxi");
					possibleMoves.add(possibleMove);
				}
			}
		}
		for(int j = 0; j < station.getNumberBusConnections(); j++) {
			if (mrX.getAvailableBus()>0) {
				Station destinationStation = getStation(station.getBusNeighbours().get(j)-1);
				if (destinationStation.isOccupied()==false) {
					Move possibleMove = new Move(station, destinationStation, "Bus");
					possibleMoves.add(possibleMove);
				}
			}
		}
		for(int j = 0; j < station.getNumberTubeConnections(); j++) {
			if (mrX.getAvailableBus()>0) {
				Station destinationStation = getStation(station.getTubeNeighbours().get(j)-1);
				if (destinationStation.isOccupied()==false) {
					Move possibleMove = new Move(station, destinationStation, "Tube");
					possibleMoves.add(possibleMove);
				}
			}
		}
		
//		if (step > 2) {
//			List<Move> prunedMoves = pruneMrXMoves(possibleMoves, parsedDistances);
//			this.possibleMrXmoves = prunedMoves;
//		}
		
		this.possibleMrXmoves = possibleMoves;
		
	}
	
	/**Find all possible Mr. X moves. 
	 * 
	 */
	public void findMrXPossibleMoves(int step, List<List<Integer>> parsedDistances) {
		
		//loop through all possible stations
		//check all possible moves from them and add them to the list of possible moves
		List<Move> possibleMoves = new ArrayList<Move>();
		
			for(int i = 0; i < possibleMrXstations.size(); i++) {
				
				Station startStation = getStation(possibleMrXstations.get(i)-1);
				
				//a move gets added if Mr. X has the right ticket and if the destination station is not occupied
					for(int j = 0; j < startStation.getNumberTaxiConnections(); j++) {
							if (mrX.getAvailableTaxi()>0) {
								Station destinationStation = getStation(startStation.getTaxiNeighbours().get(j)-1);
								if (destinationStation.isOccupied()==false) {
									Move possibleMove = new Move(startStation, destinationStation, "Taxi");
									possibleMoves.add(possibleMove);
								}
							}
						}
					for(int j = 0; j < startStation.getNumberBusConnections(); j++) {
							if (mrX.getAvailableBus()>0) {
								Station destinationStation = getStation(startStation.getBusNeighbours().get(j)-1);
								if (destinationStation.isOccupied()==false) {
									Move possibleMove = new Move(startStation, destinationStation, "Bus");
									possibleMoves.add(possibleMove);
								}
							}
						}
					for(int j = 0; j < startStation.getNumberTubeConnections(); j++) {
							if (mrX.getAvailableBus()>0) {
								Station destinationStation = getStation(startStation.getTubeNeighbours().get(j)-1);
								if (destinationStation.isOccupied()==false) {
									Move possibleMove = new Move(startStation, destinationStation, "Tube");
									possibleMoves.add(possibleMove);
								}
							}
						}
					}
			
//			if (step > 2) {
//				List<Move> prunedMoves = pruneMrXMoves(possibleMoves, parsedDistances);
//				this.possibleMrXmoves = prunedMoves;
//			}
			
			this.possibleMrXmoves = possibleMoves;
		
			}
	
	
	
	/**Move a detective. 
	 * 
	 * @param detective
	 * @param move
	 */
	public boolean moveDetective(Detective detective, Move move, List<Station> stationsList) {
		
		Station startStation = move.getStartStation();
		Station destinationStation = move.getDestinationStation();
	
		if (destinationStation.isOccupied()==false) {
			startStation.unoccupyStation();
			destinationStation.occupyStation();
			handleStatisticsDetective(move, detective.getNumber()-1);	
			detective.setCurrentPosition(destinationStation.getNameInt());
			detective.findPossibleMovesDetective(stationsList);
			possibleMrXstations.remove(Integer.valueOf(destinationStation.getNameInt()));
			
			return true;
		}
		
		return false;
		
	}
	
	
	/**Move Mr. X.
	 * 
	 * @return		the type of ticket that Mr. X used. 
	 */
	public String moveMrX(int step, List<List<Integer>> parsedDistances) {
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
		if (ticketUsed.equals("Taxi") && mrX.getAvailableTaxi() > 0 || ticketUsed.equals("Bus") && 
				mrX.getAvailableBus() > 0 || ticketUsed.equals("Tube") && mrX.getAvailableTube() > 0) {
			mrX.addToUsedTickets(ticketUsed);
			removeMrXticket(ticketUsed);
		}
		else {
			System.out.println("Not enough tickets of this type available!");
			//TODO throw an exception? 
		}
		
//		if (step > 2) {
//			findMrXPossibleStations(ticketUsed);
//			findMrXPossibleMoves(step, parsedDistances);
//		}
		
		
		return ticketUsed;
	}
	
	//TODO: consider and develop this! 
	/**Simulate Mr. X making a move.
	 * 
	 * @param move
	 */
	public void simulateMrXmove(Move move, int step, List<List<Integer>> parsedDistances) {
		mrX.setSimulatedCurrentStation(move.getDestinationStation().getNameInt());
		if (move.getTicket().equals("Taxi") && mrX.getAvailableTaxi() > 0 || move.getTicket().equals("Bus") && 
				mrX.getAvailableBus() > 0 || move.getTicket().equals("Tube") && mrX.getAvailableTube() > 0) {
			mrX.addToUsedTickets(move.getTicket());
			removeMrXticket(move.getTicket());
		}
		//findMrXPossibleStations(move.getTicket());
		findMrXPossibleMoves(stationsList.get(mrX.getSimulatedCurrentStation()-1), step, parsedDistances);
		
	}
	
	/**Remove the ticket Mr. X used from his available tickets.
	 * 
	 * @param ticket	the ticket that Mr. X has used. 
	 */
	public void removeMrXticket(String ticket) {
		if (ticket.equals("Taxi")) {
			mrX.removeXTaxiTicket();
		}
		else if (ticket.equals("Bus")) {
			mrX.removeXBusTicket();
		}
		else if (ticket.equals("Tube")) {
			mrX.removeXTubeTicket();
		}
	}
	
	 public int returnShortestDistance(int position1, int position2, List<List<Integer>> parsedDistances) {
		 
		 if (position1 == position2)
	            return 0;
		 
		 else
	            return shortestDistanceBetweenDifferent(position1, position2, parsedDistances);
	    }
	 
	 public int shortestDistanceBetweenDifferent(int position1, int position2, List<List<Integer>> parsedDistances) {
	        
		 int index1, index2;
		 
		 if (position1 < position2) {
	            index1 = position1 - 1;
	            index2 = (position2 - position1) - 1;
	        }
		 
		 else {
	            index1 = position2 - 1;
	            index2 = (position1 - position2) - 1;
	        }
		 
		 return parsedDistances.get(index1).get(index2);
	 }
	
	
//	/**Return the shortest distance between two stations. 
//	 * 
//	 * @param start
//	 * @param destination
//	 * @return
//	 */
//	public int returnShortestDistance(int start, int destination, List<List<Integer>> parsedDistances) {
//		
//		int distance = 0;
//		
//		if (start == destination) {
//			return 0;
//		}
//		
//		else {
//			
//			List<Integer> distancesFromStart = new ArrayList<Integer>();
//			
//			if (start != 199) {
//				distancesFromStart = parsedDistances.get(start-1);
//				List<Integer> distancesFromDestination = parsedDistances.get(destination-1);
//			}
//			
//			
//			if (start < destination) {	
//				distance = distancesFromStart.get(destination-start-1);
//			}
//			else {
//				distance = distancesFromDestination.get(start-destination-1);
//			}
//			
//			return distance;
//			
//		}
//		
//		
//	}
	
	
	/**Cleans up the possible moves for every detective. 
	 * 
	 */
	public void coordinatePossibleDetectiveMoves(int step, List<List<Integer>> parsedDistances) {
		/**Loop through all detectives, except for the last one 
		 * For every detective, loop through all possible moves
		 * If any of the moves' destinations stations equals any of the detectives' current stations,
		 * remove the move.
		 * This is a design choice to make the algorithm easier: a following detective will not be able to move
		 * to a station that was occupied by a previous detective. 
		 */
		
		List<Integer> detectivesCurrentStations = new ArrayList<Integer>();
		
		//Remove the moves that increase the distance between current detective station and any of Mr. X possible stations  
		
		if (step > 2) {
			
			//TODO: calculate average distance of detectives 
			//int averageDist = calculateAverageDistanceDetectives();
			//TODO: in case average distance is greater than a certain number, occupy as many of the closest tube stations as possible 
			//if (averageDistance > ??) {
				
			//}
			
			
			for (Detective det : listDetectives) {
				
				List<Move> toRemove = new ArrayList<Move>();
				
				for (Move move : det.getPossibleMovesCurrentStation()) {
					
					int currentDistance = 0;
					int newDistance = 0;
					
					for (int station : possibleMrXstations) {
						
						currentDistance = returnShortestDistance(det.getCurrentPosition(), station, parsedDistances);
						newDistance = returnShortestDistance(move.getDestinationStation().getNameInt(), station, parsedDistances);
						
						if(newDistance > currentDistance) {
							toRemove.add(move);
						}
						
					}
					
				}
				
				List<Move> cleanedUpMoves = det.getPossibleMovesCurrentStation();
				
				cleanedUpMoves.removeAll(toRemove);
				
				det.setPossibleMovesCurrentStation(cleanedUpMoves);
				
			
			}
			
					
		}
		
				
				
		for (int i = 0; i < numberOfDetectives; i++) {
			Detective det = listDetectives.get(i);
			int current = det.getCurrentPosition();
			detectivesCurrentStations.add(current);
		}
		
		
		for (int i = 0; i < numberOfDetectives-1; i++) {
			
			Detective det = listDetectives.get(i);
			
			for (int j = 0; j < det.getPossibleMovesCurrentStation().size(); j++) {
				
				int destination = det.getPossibleMovesCurrentStation().get(j).getDestinationStation().getNameInt();
				if (detectivesCurrentStations.contains(destination)) {
					det.getPossibleMovesCurrentStation().remove(j);
				}
			}
		}
		
		List<List<Move>> allMoves = new ArrayList<List<Move>>();
		
		//Save all possible moves in a list of lists, allPossibleDetectiveMoves
		for (int i = 0; i < numberOfDetectives; i++) {
			Detective detective = listDetectives.get(i);
			List<Move> possibleMoves = detective.getPossibleMovesCurrentStation();
			allMoves.add(possibleMoves);
		}
		
		
		
		this.allPossibleDetectiveMoves = allMoves;
	}
	
	
	/**Generate all possible move combinations for detectives. 
	 * 
	 * 
	 */
	public final void generateAllPossibleMoveCombosDetectives(List<List<Move>> lists, List<List<Integer>> parsedDistances, int step) {
		
		//Use guava to create a cartesian product of all possible combos
		List<List<Move>> allCombos = new ArrayList<List<Move>>();
		allCombos = Lists.cartesianProduct(lists);
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
		
		//TODO: filter all possible combos with the following criteria: if the move does not shorten the total 
		//distance from the current positions of detectives to the simulated current position of Mr. X then 
		//remove the move 
		
		//D1 at 7, D2 at 2
		//Mr. X simulated current position = 5
		
		//One check for if station nr. is lower, another for if it is higher
		//Sum the distances
		
		//Check distances from destination station of every move to Mr. X simulated current position 
		//Sum the distances
		
		//If this sum is higher than the previous sum, remove the combo 
		

		if (step > 2) {
			
			List<Integer> distancesFromMrXStation = new ArrayList<Integer>();
			
			if (mrX.getSimulatedCurrentStation() != 199) {
				distancesFromMrXStation = parsedDistances.get(mrX.getSimulatedCurrentStation()-1);
			}
			
			List<List<Move>> toRemove = new ArrayList<List<Move>>();
			
			int oldDistances = 0; 
			
			for (Detective det : listDetectives) {
				
				List<Integer> distancesFromCurrentDetStation = new ArrayList<Integer>();
				
				if(det.getCurrentPosition() != 199) {
					distancesFromCurrentDetStation = parsedDistances.get(det.getCurrentPosition()-1);
				}
				
				int oldDistance = 0; 
				
				if (det.getCurrentPosition() < mrX.getSimulatedCurrentStation()) {	
					oldDistance = distancesFromCurrentDetStation.get(mrX.getSimulatedCurrentStation()-det.getCurrentPosition()-1);
					oldDistances += oldDistance;
				}
				else {
					oldDistance = distancesFromMrXStation.get(det.getCurrentPosition()-mrX.getSimulatedCurrentStation()-1);
					oldDistances += oldDistance;
				}
			}
			
			
			for (List<Move> combo : allCombosCleanedUp) {
				

				int newDistances = 0;
				
				for (Move move : combo) {
					
					List<Integer> distancesFromMoveDestination = new ArrayList<Integer>();
					
					if (move.getDestinationStation().getNameInt() != 199) {
						distancesFromMoveDestination = parsedDistances.get(move.getDestinationStation().getNameInt()-1);
					}
					
					int newDist = 0;
					
					if(mrX.getSimulatedCurrentStation()-move.getDestinationStation().getNameInt() == 0 || move.getDestinationStation().getNameInt()-mrX.getSimulatedCurrentStation() == 0) {
						setWinningMove(move);
						//System.out.println("WINNING MOVE = " + move);
					}
					
					else {
						if (move.getDestinationStation().getNameInt() < mrX.getSimulatedCurrentStation()) {
							newDist = distancesFromMoveDestination.get(mrX.getSimulatedCurrentStation()-move.getDestinationStation().getNameInt()-1);
							newDistances += newDist; 
						}
						else {
							newDist = distancesFromMrXStation.get(move.getDestinationStation().getNameInt()-mrX.getSimulatedCurrentStation()-1);
							newDistances += newDist;
						}
					}
					
				}
				
				if(newDistances > oldDistances) {
					toRemove.add(combo);
				}
			}
			
			allCombosCleanedUp.removeAll(toRemove);
		}
		
		
		
		this.allPossibleMoveCombosDetectives = allCombosCleanedUp;
		
	}
	
	
	
	/**The minimax algorithm that runs through the tree of possible game states.
	 * 
	 * @param depth
	 * @param isMaximizing
	 */
	public double miniMax(int depth, boolean isMaximizing, TreeNode<GameState> startNode, Cloner cloner, List<List<Integer>> parsedDistances, int step, List<Station> stations) {
		
		//Check if Mr. X has no moves left and return negative infinity if that is the case  
		double bestScore;
		
		
		boolean result = startNode.getData().noMovesLeftCheck();
		if (result == true) {
			System.out.println("MR. X IS NO MORE!!!");
			//bestScore = Double.NEGATIVE_INFINITY;
			bestScore = -10000;
			return bestScore;
		}
		
//		//TODO: check this! 
		if (winningMove != null) {
			bestScore = Double.NEGATIVE_INFINITY;
			return bestScore;
		}
		
		//Once you reach a depth of two, evaluate the game state and propagate the score 
		if(depth == 2) {
			double miniMaxEvaluation = startNode.getData().evaluateGameState(startNode, parsedDistances);
			//System.out.println("Game state evaluation = " + miniMaxEvaluation);
			startNode.setNodeEvaluation(miniMaxEvaluation);
			return miniMaxEvaluation;
			
		}
		
		//Mr. X is the maximizing player
		if (isMaximizing) {
			bestScore = Double.NEGATIVE_INFINITY;
			
			//Loop through all possible Mr. X moves and make every one of them; 
			//Call minimax with minimizing player 
			for (int i = 0; i < startNode.getData().getPossibleMrXmoves().size(); i++) {
				GameState clonedState = startNode.getDeepCloneOfRepresentedState();
				Move move = clonedState.getPossibleMrXmoves().get(i);
				clonedState.simulateMrXmove(move, step, parsedDistances);
				clonedState.findMrXPossibleStations(move.getTicket(), step);
				//clonedState.findMrXPossibleMoves();
				clonedState.coordinatePossibleDetectiveMoves(step, parsedDistances);
				clonedState.generateAllPossibleMoveCombosDetectives(allPossibleDetectiveMoves, parsedDistances, step);
				
				TreeNode<GameState> newChild = new TreeNode<GameState>(clonedState, cloner);
				
				double score = clonedState.miniMax(depth+1, false, newChild, cloner, parsedDistances, step, stations);
				
				startNode.addChild(newChild);
				
				bestScore = Math.max(score, bestScore);
				startNode.setNodeEvaluation(bestScore);
			}
		}
		
		else {
			bestScore = Double.POSITIVE_INFINITY;
			//Loop through all possible move combos and make every one of them
			//Call minimax with maximizing player
			for (int i = 0; i < allPossibleMoveCombosDetectives.size(); i++) {
				GameState clonedState = startNode.getDeepCloneOfRepresentedState();
				for (int j = 0; j < allPossibleMoveCombosDetectives.get(i).size(); j++) {
					
					Detective det = clonedState.getListDetectives().get(j);
					Move move = clonedState.getAllPossibleMoveCombosDetectives().get(i).get(j);
					if(clonedState.getMrX().getSimulatedCurrentStation() == move.getDestinationStation().getNameInt()) {
						bestScore = Double.NEGATIVE_INFINITY;
						startNode.setBestCombo(startNode.getData().getAllPossibleMoveCombosDetectives().get(i));
						return bestScore;
					}
					clonedState.moveDetective(det, move, stations);
					//clonedState.findMrXPossibleStations(move.getTicket());
					clonedState.findMrXPossibleMoves(clonedState.getStations().get((clonedState.getMrX().getSimulatedCurrentStation()-1)), step, parsedDistances);
				}
				
				clonedState.coordinatePossibleDetectiveMoves(step, parsedDistances);
				clonedState.generateAllPossibleMoveCombosDetectives(clonedState.getAllPossibleDetectiveMoves(), parsedDistances, step);
				
				TreeNode<GameState> newChild = new TreeNode<GameState>(clonedState, cloner);
				//startNode.addChild(newChild);
				
				double score = clonedState.miniMax(depth+1, true, newChild, cloner, parsedDistances, step, stations);
				
				startNode.addChild(newChild);
				
				//bestScore = Math.min(score, bestScore);
				if (score < bestScore) {
					bestScore = score;
					List<Move> bestCombo = startNode.getParent().getData().getAllPossibleMoveCombosDetectives().get(i);
					//List<Move> bestCombo = newChild.getData().getAllPossibleMoveCombosDetectives().get(i);
					startNode.setBestCombo(bestCombo);
				}
				
				startNode.setNodeEvaluation(bestScore);
			}
			
		}
		return bestScore;
	}
	
	public double calculateAverageDistanceDetectives(TreeNode<GameState> startNode, List<List<Integer>> parsedDistances) {
		
		double averageDistance = 0;
		int nrDetectives = startNode.getData().getListDetectives().size();
		
		for (Detective det : startNode.getData().getListDetectives()) {
			int dist = returnShortestDistance(det.getCurrentPosition(), startNode.getData().getMrX().getSimulatedCurrentStation(), parsedDistances);
			averageDistance += dist;
		}
		
		return (averageDistance / nrDetectives);
	}
	
	
	//TODO: include distances of detectives to possible Mr. X stations 
	/**Evaluates a game state.
	 * 
	 * @return	the evaluation of the game state. 
	 */
	public double evaluateGameState(TreeNode<GameState> startNode, List<List<Integer>> parsedDistances) {
		//PLUS:
		//Each possible location Mr. X is located at = +10
		//The values of all those stations
		//TODO: The increase in average distance
		
		//MINUS:
		//Each possible Mr. X location reachable in 1 move = -10 
		//Values of those stations
		//TODO: The decrease in average distance
		
		
		
		GameState state = startNode.getData();
		double evaluation = state.getPossibleMrXstations().size() * 10;
		if (noMovesLeftCheck()) {
			evaluation = -10000;
			return evaluation; 
		}
		for (int i = 0; i < state.getPossibleMrXstations().size(); i++) {
			Station station = state.getStation(state.getPossibleMrXstations().get(i)-1);
			evaluation += station.getValue();
		}
		for (int i = 0; i < state.getListDetectives().size(); i++) {
			Detective detective = state.getListDetectives().get(i);
			Station startStation = state.getStation(detective.getCurrentPosition()-1);
			//check all the neighbours of the detective's current station
			//if the list of Mr. X possible stations contains a neighbour and if the detective has the right type of ticket, then increment counter
			for (int j = 0; j < startStation.getNumberTaxiConnections(); j++) {
				Station destinationStation = state.getStation(startStation.getTaxiNeighbours().get(j)-1);
				if (state.getPossibleMrXstations().contains(destinationStation.getNameInt()) && detective.getTaxiTicketsAvailable()>0) {
					int value = destinationStation.getValue();
					evaluation -= (10 + value);
				}
			}
			for (int j = 0; j < startStation.getNumberBusConnections(); j++) {
				Station destinationStation = state.getStation(startStation.getBusNeighbours().get(j)-1);
				if (state.getPossibleMrXstations().contains(destinationStation.getNameInt()) && detective.getBusTicketsAvailable()>0) {
					int value = destinationStation.getValue();
					evaluation -= (10 + value);
				}
			}
			for (int j = 0; j < startStation.getNumberTubeConnections(); j++) {
				Station destinationStation = state.getStation(startStation.getTubeNeighbours().get(j)-1);
				if (state.getPossibleMrXstations().contains(destinationStation.getNameInt()) && detective.getTubeTicketsAvailable()>0) {
					int value = destinationStation.getValue();
					evaluation -= (10 + value);
				}
			}
		}
		
		//double averageDistancePrevious = calculateAverageDistanceDetectives(startNode.getParent(), parsedDistances);
		//double averageDistanceNew = calculateAverageDistanceDetectives(startNode, parsedDistances);
		
		//evaluation += (averageDistancePrevious - averageDistanceNew);
		
		
		return evaluation; 
	}
	
	/**Handle tickets and travel list for given detective.
	 * 
	 * @param bestMove			best move as calculated by the calculateBestMove method.
	 * @param detectiveIndex	given index of detective. 
	 */
	public void handleStatisticsDetective(Move bestMove, int detectiveIndex) {
		
		Detective detective = listDetectives.get(detectiveIndex);
		
		if(bestMove.getTicket().equals("Taxi")) {
			detective.removeDetectiveTaxiTicket();
			mrX.addTaxiTicket();
		}
		else if(bestMove.getTicket().equals("Bus")) {
			detective.removeDetectiveBusTicket();
			mrX.addBusTicket();
		}
		else if(bestMove.getTicket().equals("Tube")) {
			detective.removeDetectiveTubeTicket();
			mrX.addTubeTicket();
		}
		
		detective.getMoveList().add(bestMove);
	}
	
	
	/**Change the occupation status of both the location from which the detective has moved,
	 * as well as the location to which the detective has moved. 
	 * @param currentPosition
	 * @param newPosition
	 */
	public void occupyAndUnoccupyRelevantLocation(Move bestMove) {
		Station currentLocation = bestMove.getStartStation();
		Station newLocation = bestMove.getDestinationStation();
		currentLocation.unoccupyStation();
		newLocation.occupyStation();
	}
	
	
	/**Add the location where Mr. X reveals himself to the list of reveal locations.
	 * 
	 * @param mrXLocation						number of the location where Mr. X. reveals himself.  
	 * @throws LocationNotFoundException		
	 */
	public void reveal(int mrXLocation) {
		mrX.addToReveals(mrXLocation);
	}
	
	
	/**Print out the state of affairs to the console, containing information about all the 
	 * detectives, as well as the map of locations. 
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---------------------------------\n");
		sb.append("THE CURRENT STATE OF AFFAIRS\n");
		sb.append("---------------------------------\n");
		// print all detectives
		for (int i = 0; i < numberOfDetectives; i+=1) {
			sb.append(listDetectives.get(i).toString());
		}
		sb.append(mrX.toString());
		sb.append("All possible Mr. X stations = " + possibleMrXstations + "\n");
		sb.append("All possible Mr. X moves = " + possibleMrXmoves + "\n");
		sb.append("---------------------------------\n");
		sb.append("*** DETECTIVE MOVES ***\n");
		sb.append("---------------------------------\n");
		sb.append("All possible detective moves = " + allPossibleDetectiveMoves + "\n");
		sb.append("All possible combinations of detective moves = " + allPossibleMoveCombosDetectives + "\n");
		sb.append("The best detective moves = " + bestDetectiveMoves + "\n");
		
		return sb.toString();
	}

	public Move getWinningMove() {
		return winningMove;
	}

	public void setWinningMove(Move winningMove) {
		this.winningMove = winningMove;
	}
	
}
