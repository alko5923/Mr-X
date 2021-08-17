package mrX_maven_testing;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import mrX_maven_game.Board;
import mrX_maven_game.GameState;
import mrX_maven_game.Station;
import mrX_maven_strategies.CoordinatePlayers;
import mrX_maven_utilities.TreeNodeMrX;

public class TestStations {
	
	/**
	 * Set up the full game.
	 */
	public static GameState setupFullGame() throws FileNotFoundException {
		
		Board board = Board.initializeBoard();
		CoordinatePlayers coordinator = CoordinatePlayers.initializeCoordinator(board);
		GameState gameState = new GameState(coordinator);
		return gameState;
	}
	
	@Test
	public void Test1() {
		System.out.println("Hello, stations!");
	}
	
	//TODO: aim to finish this test by occupying station 111 with a detective and then examining the tree
	@Test
	public <T> void Test2() throws FileNotFoundException {
		Board board = Board.initializeBoard();
		Station rootStation = board.getStations().get(123);
		board.getStations().get(110).occupyStation();
		TreeNodeMrX<Station> rootNode = new TreeNodeMrX<Station>(rootStation);
		List<Integer> busNeighbours = rootStation.getBusNeighbours();
		for (Integer station : busNeighbours) {
			Station newStation = board.getStations().get(station-1);
			rootNode.addChild(newStation);
		}
		
		List<Station> stationsFound = new ArrayList<Station>();
		for (TreeNodeMrX<Station> station : rootNode) {
			if (station.getLevel()==1 && station.getStation().isOccupied()==false && station.getStation().getStationType().equals("Tube")) {
				stationsFound.add(station.getStation());
			}
		}
		
		for (Station st : stationsFound) {
			for (Integer s : st.getTubeNeighbours()) {
				Station newStation = board.getStations().get(s-1);
				if (newStation.isOccupied()==false) {
					rootNode.findTreeNode(st).addChild(newStation);
				}
			}
		}
		for (TreeNodeMrX<Station> node : rootNode) {
			if(node.getLevel()==2) {
				System.out.println(node + "node level = " + node.getLevel());
			}
		}
		assertEquals(rootNode.getLevel(), 0);
	}
	
}
