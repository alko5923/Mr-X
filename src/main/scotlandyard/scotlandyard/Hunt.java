package scotlandyard;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import com.rits.cloning.Cloner;
import mrX_maven_game.Move;
import mrX_maven_game.Station;
import mrX_maven_players.Detective;
import mrX_maven_strategies.CoordinatePlayers;
import mrX_maven_utilities.TreeNode;

//TODO: 
//Step through the debugger up to full depth and check if everything is being calculated correctly
//Ran out of java heap space on step 12 last time (with 3 detectives and depth of 1): where can we save space and time?
//Check if you can make it work on the branch bestDetMoves3!


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
		
		CoordinatePlayers coordinator = CoordinatePlayers.initializeCoordinator();
			
		for(int step = 1; step < steps+1; step++) {
			System.out.println(" ***** MOVE NR. " + (step) + " *****\n");	
			playOneRound(step, coordinator);	
		}
	}
	
	
	public static void playOneRound(int step, CoordinatePlayers coordinator) {
		
		if (step == 1) {
			//Initialize possible stations for Mr.X
			coordinator.getMrX().setPossibleStations(coordinator.getMrX().getPossibleStartingStationsMrX());
			
			//Move Mr. X
			coordinator.getMrX().moveMrX();
			
			//Find the nearest tube stations for all detectives
			List<Station> closestTubeStations = coordinator.generateClosestTubeStationsAllDetectives();
			
			//Set first destination for every detective
			coordinator.setFirstDestinationDetectives(closestTubeStations);
			
			//Find and set the first move for all detectives
			coordinator.findFirstAndSecondMoves(step);
			
			//Extract and set the best combo
			List<Move> bestCombo = coordinator.extractAndSetBestCombo();
			
			//Execute the combo
			coordinator.executeCombo(bestCombo);
			
			//Handle new tickets for Mr. X
			coordinator.getMrX().handleTickets(coordinator.getDetectives());
			
			//Find all possible detective move combos
			coordinator.generatePrunedDetectiveCombos();
			
			//Print out game state
			System.out.println(coordinator);
			
			//Give instructions to the user
			for (Detective det : coordinator.getDetectives()) {
				instructionsToUSer(det);
			}
			
		} else if (step == 2) {
			//Move Mr. X
			coordinator.getMrX().moveMrX();
			
			//Find and set the second move for all detectives
			coordinator.findFirstAndSecondMoves(step);
			
			//Extract and set the best combo
			List<Move> bestCombo = coordinator.extractAndSetBestCombo();
			
			//Execute the combo
			coordinator.executeCombo(bestCombo);
			
			//Handle new tickets for Mr. X
			coordinator.getMrX().handleTickets(coordinator.getDetectives());
			
			//Reset the attributes used for the first 2 moves
			for (Detective det : coordinator.getDetectives()) {
				det.setClosestTubeStations(null);
				det.setFirstDestination(null);
			}
			
			//Find all possible detective move combos
			coordinator.generatePrunedDetectiveCombos();
			
			//Print out game state
			System.out.println(coordinator);
			
			//Give instructions to the user
			for (Detective det : coordinator.getDetectives()) {
				instructionsToUSer(det);
			}
			
		} else {
			//Move Mr.
			String ticketUsed = coordinator.getMrX().moveMrX();
			
			//Find all possible Mr. X stations
			coordinator.getMrX().findPossibleStationsAfterTicket(ticketUsed, coordinator.getStations());
			
			//Reveal Mr. X on defined steps
			if (step == 3 || step == 8 || step == 13 || step == 18 || step == 24) {
				System.out.println("\nMr. X, reveal yourself!\n");
				System.out.println("Where is Mr. X at the moment?\n");
				mrXLocation = sc.nextInt();
				coordinator.getMrX().reveal(mrXLocation);
			}
			
			//Find all possible Mr. X moves
			coordinator.getMrX().findPossibleMoves(coordinator.getStations());
			
			//Find all possible detective move combos
			coordinator.generatePrunedDetectiveCombos();
			
			//Perform tree search
			TreeNode<CoordinatePlayers> rootNode = treeSearch(coordinator);
			
			//Execute best moves
			executeBestMoves(rootNode);	
			
			//Handle new tickets for Mr. X
			coordinator.getMrX().handleTickets(coordinator.getDetectives());
			
			//Print out game state
			System.out.println(coordinator);
			
			//Give instructions to the user
			for (Detective det : coordinator.getDetectives()) {
				instructionsToUSer(det);
			}
			
		}
	}
	
	// ****************************************************************************************************************
	// *************************************** STATIC METHODS *********************************************************
	// ****************************************************************************************************************
	
	/**
	 * Check if Mr. X has no moves left.
	 * @return	true for no moves left, false otherwise.
	 */
	public static boolean noMovesLeftCheck(CoordinatePlayers coordinator) {
		
		if (coordinator.getMrX().getPossibleMoves().size() == 0) {		
			return true;
		}
		return false;
	}
	
	/**
	 * The minimax algorithm that runs through the tree of possible game states.
	 * @param depth
	 * @param isMaximizing
	 */
	public static double miniMax(int depth, boolean isMaximizing, TreeNode<CoordinatePlayers> node, Cloner cloner, List<Move> combo) {
		//Check if Mr. X has no moves left and return negative infinity if that is the case  
		double bestScore;
		//TODO: find possible Mr. X moves?
		boolean result = noMovesLeftCheck(node.getData());
		if (result == true) {
			System.out.println("MR. X IS NO MORE!!!");
			//bestScore = Double.NEGATIVE_INFINITY;
			bestScore = -10000;
			//node.setBestCombo(combo);
			return bestScore;
		}
		
		//Once you reach a certain depth, evaluate the game state and propagate the score 
		if(depth == 1) {
			double miniMaxEvaluation = evaluateGameState(node);
			node.setNodeEvaluation(miniMaxEvaluation);
			return miniMaxEvaluation;
		}
		
		//Mr. X is the maximizing player
		if (isMaximizing) {
			bestScore = Double.NEGATIVE_INFINITY;
			CoordinatePlayers coordinator = node.getData();
			//Loop through all possible Mr. X moves and make every one of them; 
			//Call minimax with minimizing player 
			for (int i = 0; i < coordinator.getMrX().getPossibleMoves().size(); i++) {
				CoordinatePlayers clonedState = node.getDeepCloneOfRepresentedState();
				Move move = clonedState.getMrX().getPossibleMoves().get(i);
				clonedState.getMrX().simulateMove(move, clonedState.getStations());
				//clonedState.getCoordinator().getMrX().findPossibleStationsAfterTicket(clonedState.getCoordinator().getBoard(), move.getTicket());
				clonedState.generatePrunedDetectiveCombos();
				TreeNode<CoordinatePlayers> newChild = new TreeNode<CoordinatePlayers>(clonedState, cloner);
				double score = miniMax(depth+1, false, newChild, cloner, combo);
				node.addChild(newChild);
				bestScore = Math.max(score, bestScore);
				node.setNodeEvaluation(bestScore);
			}
		} else {
			bestScore = Double.POSITIVE_INFINITY;
			//Loop through all move combos and make every one of them
			//After executing a combo, call minimax with maximizing player
			List<List<Move>> allPossibleDetectiveCombos = node.getData().getAllPossibleMoveCombosDetectives();
			for (int i = 0; i < allPossibleDetectiveCombos.size(); i++) {
				CoordinatePlayers clonedState = node.getDeepCloneOfRepresentedState();
				clonedState.executeCombo(clonedState.getAllPossibleMoveCombosDetectives().get(i));
				
				boolean check = checkIfMrXFound(node);
				if (check == true) {
					bestScore = Double.NEGATIVE_INFINITY;
				}
				//TODO: check the order of execution here! 
				clonedState.generatePrunedDetectiveCombos();
				TreeNode<CoordinatePlayers> newChild = new TreeNode<CoordinatePlayers>(clonedState, cloner);
				double score = miniMax(depth+1, true, newChild, cloner, combo);
				node.addChild(newChild);
			
				if (score < bestScore) {
					bestScore = score;
					//List<Move> bestCombo = node.getParent().getData().getCoordinator().getAllPossibleMoveCombosDetectives().get(i);
					node.setBestCombo(combo);
				}
				
				node.setNodeEvaluation(bestScore);
			}
		}
		return bestScore;
	}
	
	private static boolean checkIfMrXFound(TreeNode<CoordinatePlayers> node) {
		for (Detective det : node.getData().getDetectives()) {
			if(node.getData().getMrX().getCurrentStation() == det.getCurrentPosition()) {
				return true;
			}
		}
		return false;
		
	}
	
	/**Evaluates the game state, using average distance of detectives to simulated position of Mr. X.
	 * 
	 * @return	the evaluation of the game state. 
	 */
	public static double evaluateGameState(TreeNode<CoordinatePlayers> node) {
		CoordinatePlayers coordinator = node.getData();
		double evaluation = coordinator.calculateAverageDistanceDetectives();
		return evaluation;
	}
	
	private static TreeNode<CoordinatePlayers> treeSearch(CoordinatePlayers coordinator) {
		//Clone Hunter
		Cloner cloner = new Cloner();
		TreeNode<CoordinatePlayers> rootNode = new TreeNode<CoordinatePlayers>(coordinator, cloner);
		double bestScore = Double.POSITIVE_INFINITY;
		
		//Start making moves on the cloned state, thus simulating moves of Mr. X and detectives
		for (int i = 0; i < coordinator.getAllPossibleMoveCombosDetectives().size(); i++) {
			List<Move> moveCombo = coordinator.getAllPossibleMoveCombosDetectives().get(i);
			CoordinatePlayers clonedState = rootNode.getDeepCloneOfRepresentedState();
			clonedState.executeCombo(moveCombo);
			clonedState.generatePrunedDetectiveCombos();
			TreeNode<CoordinatePlayers> newNode = new TreeNode<CoordinatePlayers>(clonedState, cloner);
			rootNode.addChild(newNode);
			double score = miniMax(0, true, newNode, cloner, moveCombo);
		
			if (score < bestScore) {
				bestScore = score;
				rootNode.setBestCombo(moveCombo);
			}
			rootNode.setNodeEvaluation(bestScore);
		}
		
		setBestMoves(rootNode);
		
		return rootNode;
	}
	
	/**
	 * Add the best moves found in the tree search to all detectives. 
	 * @param rootNode
	 */
	private static void setBestMoves(TreeNode<CoordinatePlayers> rootNode) {
		for (int i = 0; i < rootNode.getBestCombo().size(); i++) {
			Detective det = rootNode.getData().getDetectives().get(i);
			Move move = rootNode.getBestCombo().get(i);
			det.getBestMoves().add(move);
		}
	}
	
	/**
	 * Executes the best moves found for all detectives. 
	 * @param rootNode
	 */
	private static void executeBestMoves(TreeNode<CoordinatePlayers> rootNode) {
		List<Move> bestCombo = rootNode.getBestCombo();
		
		for (int i = 0; i < rootNode.getBestCombo().size(); i++) {
			Detective det = rootNode.getData().getDetectives().get(i);
			Move move = bestCombo.get(i);
			rootNode.getData().moveDetective(det, move, rootNode.getData().getMrX());
		}
	}
	
	/**
	 * Gives instructions to the user on what move to make with which detective. 
	 */
	private static void instructionsToUSer(Detective det) {
		System.out.println("Move detective " + det.getName() + " : " + det.getBestMoves().get(det.getBestMoves().size()-1));
	}
	
}
