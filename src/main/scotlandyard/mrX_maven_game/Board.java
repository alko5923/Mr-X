package mrX_maven_game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.rits.cloning.Cloner;

import mrX_maven_utilities.DistancesFileParser;

public class Board {
	private final Cloner cloner;
	private static final String DISTANCES_FILE_NAME = "src/main/resources/seekers_distances.xml";
	private List<List<Integer>> distances = new ArrayList<List<Integer>>();
	private List<Station> stations = new ArrayList<Station>();
	
	/**
	 * Reads the json file with stations data into a list of stations and evaluates them, thus initializing the board.
	 * @return The initialized board. 
	 * @throws FileNotFoundException
	 */
	public static Board initializeBoard(Cloner cloner) throws FileNotFoundException {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader("src/main/resources/stations.json"));
		List<Station> stations = gson.fromJson(reader, new TypeToken<ArrayList<Station>>(){}.getType());
		stations.remove(0);
		evaluateStations(stations);
		
		DistancesFileParser parser = new DistancesFileParser(DISTANCES_FILE_NAME);
		List<List<Integer>> distances = parser.getParsedData();
		return new Board(stations, distances, cloner);
	}
	
	/**
	 * The class constructor
	 * @param stations
	 * @param distances
	 */
	private Board (List<Station> stations, List<List<Integer>> distances, Cloner cloner) {
		this.setStations(stations);
		this.setDistances(distances);
		this.cloner = cloner;
	}
	
	public List<Station> getDeepCloneOfStations() {
        return cloner.deepClone(stations);
    }
	
	
	/**
	* A simple evaluation method for the stations, based on the number and the type of connections from them.
	* @param stations
	*/
	public static void evaluateStations(List<Station> stations) {
			
		for (int i = 0; i < stations.size(); i++) {
			Station station = stations.get(i);
			int value = station.getNumberTaxiConnections()*10 + station.getNumberBusConnections()*20 + station.getNumberTubeConnections()*40;
			station.setValue(value);
		}
	}
	
	/**
	 * Calculates the shortest distance between two stations, and returns 0 if the stations are the same station.
	 * @param position1
	 * @param position2
	 * @return The shortest distance between two stations, and 0 if the stations are the same station.
	 */
	public int returnShortestDistance(int position1, int position2) {
		if (position1 == position2) {	
		    return 0;
		} else { 
			 return shortestDistanceBetweenDifferent(position1, position2);
		}
	}
	
	/**
	 * Calculates the shortest distance between two different stations.
	 * @param position1
	 * @param position2
	 * @return The distance between two different stations. 
	 */
	public int shortestDistanceBetweenDifferent(int position1, int position2) {
		int index1, index2;
		if (position1 < position2) {
			index1 = position1 - 1;
	        index2 = (position2 - position1) - 1;
		} else {
			index1 = position2 - 1;
	        index2 = (position1 - position2) - 1;
		}
		return distances.get(index1).get(index2);
	}
	
	/**
	 * Changes the occupation status of both the station from which the detective has moved,
	 * as well as the station to which the detective has moved. 
	 * @param currentPosition
	 * @param newPosition
	 */
	public void occupyAndUnoccupyRelevantStation(Move bestMove) {
		Station currentLocation = bestMove.getStartStation();
		Station newLocation = bestMove.getDestinationStation();
		currentLocation.unoccupyStation();
		newLocation.occupyStation();
	}

	public List<List<Integer>> getDistances() {
		return distances;
	}

	public void setDistances(List<List<Integer>> distances) {
		this.distances = distances;
	}

	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}
}
