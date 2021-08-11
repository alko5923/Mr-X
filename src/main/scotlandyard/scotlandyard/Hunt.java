package scotlandyard;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import com.rits.cloning.Cloner;

import mrX_maven_game.Board;
import mrX_maven_game.GameState;
import mrX_maven_game.Move;
import mrX_maven_game.Station;
import mrX_maven_players.Detective;
import mrX_maven_strategies.CoordinatePlayers;
import mrX_maven_utilities.TreeNode;


/**
 * This class represents the main overview logic of the program.
 * @author aljaz
 *
 */
public class Hunt {
	
	private static Scanner sc = new Scanner(System.in);
	private static int steps = 24;
	private static int mrXLocation;
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
		
		System.out.println("Welcome to The Hunt for Mr X, the codebreaker for the game Scotland Yard!\n");
		
		GameState gameState = setupFullGame();
			
		for(int step = 1; step < steps+1; step++) {
			
			System.out.println(" ***** MOVE NR. " + (step) + " *****\n");	
			playOneRound(step, gameState);	
		}
	}
	
	/**
	 * Set up the full game.
	 */
	public static GameState setupFullGame() throws FileNotFoundException {
		
		Board board = Board.initializeBoard();
		CoordinatePlayers coordinator = CoordinatePlayers.initializeCoordinator(board);
		GameState gameState = new GameState(coordinator);
		return gameState;
	}
	
	public static void playOneRound(int step, GameState gameState) {
		
		if (step == 1) {
			//Move Mr. X
			gameState.getCoordinator().getMrX().moveMrX();
			
			//Find the nearest tube stations for all detectives
			List<Station> closestTubeStations = gameState.getCoordinator().generateClosestTubeStationsAllDetectives();
			
			//Set first destination for every detective
			gameState.getCoordinator().setFirstDestinationDetectives(closestTubeStations);
			
			//Find and set the first move for all detectives
			gameState.getCoordinator().findFirstAndSecondMoves(step);
			
			//Give instructions to the user
			for (Detective det : gameState.getCoordinator().getDetectives()) {
				instructionsToUSer(det);
			}
			
			//Execute the first move for all detectives
			for (Detective det : gameState.getCoordinator().getDetectives()) {
				det.moveDetective(det.getBestMoves().get(det.getBestMoves().size()-1), gameState.getCoordinator().getBoard());
			}
			
			//Handle new tickets for Mr. X
			gameState.getCoordinator().getMrX().handleTickets(gameState.getCoordinator().getDetectives());
			
			
		} else if (step == 2) {
			//Move Mr. X
			gameState.getCoordinator().getMrX().moveMrX();
			
			//TODO: fix second move for starting station 13
			//Find and set the second move for all detectives
			gameState.getCoordinator().findFirstAndSecondMoves(step);
			
			//Give instructions to the user
			for (Detective det : gameState.getCoordinator().getDetectives()) {
				instructionsToUSer(det);
			}
			
			//Execute the second move for all detectives
			for (Detective det : gameState.getCoordinator().getDetectives()) {
				det.moveDetective(det.getBestMoves().get(det.getBestMoves().size()-1), gameState.getCoordinator().getBoard());
			}
			
			//Reset the attributes used for the first 2 moves
			for (Detective det : gameState.getCoordinator().getDetectives()) {
				det.setClosestTubeStations(null);
				det.setFirstDestination(null);
			}
			
		} else {
			//Move Mr. X
			String ticketUsed = gameState.getCoordinator().getMrX().moveMrX();
			
			//TODO: Mr. X stations and moves are empty!
			//Find all possible Mr. X stations
			gameState.getCoordinator().getMrX().findPossibleStationsAfterTicket(gameState.getCoordinator().getBoard(), ticketUsed);
			
			//Reveal Mr. X on defined steps
			if (step == 3 || step == 8 || step == 13 || step == 18 || step == 24) {
				System.out.println("\nMr. X, reveal yourself!\n");
				System.out.println("Where is Mr. X at the moment?\n");
				mrXLocation = sc.nextInt();
				gameState.getCoordinator().getMrX().reveal(mrXLocation);
			}
			
			//Find all possible Mr. X moves
			gameState.getCoordinator().getMrX().findPossibleMoves(gameState.getCoordinator().getBoard());
			
			//Find all possible detective move combos
			gameState.getCoordinator().generateAllPossibleMoveCombosDetectives();
			
			//Perform tree search
			TreeNode<GameState> rootNode = treeSearch(gameState);
			
			//Give instructions to the user
			for (Detective det : gameState.getCoordinator().getDetectives()) {
				instructionsToUSer(det);
			}
			
			//Execute best moves
			executeBestMoves(rootNode);	
		}
	}
	
	private static TreeNode<GameState> treeSearch(GameState gameState) {
		
		//Clone Hunter
		Cloner cloner = new Cloner();
		TreeNode<GameState> rootNode = new TreeNode<GameState>(gameState, cloner);
		double bestScore = Double.NEGATIVE_INFINITY;
		
		//Start making moves on the cloned state, thus simulating moves of Mr. X and detectives
		for (int i = 0; i < gameState.getCoordinator().getMrX().getPossibleMoves().size(); i++) {
			Move move = gameState.getCoordinator().getMrX().getPossibleMoves().get(i);
			GameState clonedState = rootNode.getDeepCloneOfRepresentedState();
			clonedState.getCoordinator().getMrX().simulateMove(move, gameState.getCoordinator().getBoard());
			TreeNode<GameState> newNode = new TreeNode<GameState>(clonedState, cloner);
			rootNode.addChild(newNode);
			double score = clonedState.miniMax(0, false, newNode, cloner);
		
			if (score >= bestScore) {
				bestScore = score;
				rootNode.setBestCombo(newNode.getBestCombo());
			}
			rootNode.setNodeEvaluation(bestScore);
		}
		
		return rootNode;
	}
	
	/**
	 * Executes the best moves found for all detectives. 
	 * @param rootNode
	 */
	private static void executeBestMoves(TreeNode<GameState> rootNode) {
		
		List<Move> bestCombo = rootNode.getBestCombo();
		
		for (int i = 0; i < rootNode.getBestCombo().size(); i++) {
			Detective det = rootNode.getData().getCoordinator().getDetectives().get(i);
			Move move = bestCombo.get(i);
			det.moveDetective(move, rootNode.getData().getCoordinator().getBoard());
		}
	}
	
	/**
	 * Gives instructions to the user on what move to make with which detective. 
	 */
	private static void instructionsToUSer(Detective det) {
		System.out.println("Move detective " + det.getName() + " : " + det.getBestMoves().get(det.getBestMoves().size()-1));
	}
	
}
