package mrX_maven_game;

import com.rits.cloning.Cloner;
import mrX_maven_players.Detective;
import mrX_maven_strategies.CoordinatePlayers;
import mrX_maven_utilities.TreeNode;

import java.util.*;


/**
 * This class represents the current game state.
 * @author aljaz
 *
 */

public class GameState {
	
	private CoordinatePlayers coordinator;
	private List<Move> bestDetectiveMoves = new ArrayList<Move>();
	private Move winningMove = null;
	
	/**
	 * The class constructor. 
	 * @param coordinator
	 */
	public GameState(CoordinatePlayers coordinator) {
		this.setCoordinator(coordinator);
	}
	
	/**
	 * Check if Mr. X has no moves left.
	 * @return	true for no moves left, false otherwise.
	 */
	public boolean noMovesLeftCheck() {
		
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
	public double miniMax(int depth, boolean isMaximizing, TreeNode<GameState> node, Cloner cloner) {
		//Check if Mr. X has no moves left and return negative infinity if that is the case  
		double bestScore;
		boolean result = node.getData().noMovesLeftCheck();
		if (result == true) {
			System.out.println("MR. X IS NO MORE!!!");
			//bestScore = Double.NEGATIVE_INFINITY;
			bestScore = -10000;
			return bestScore;
		}
		
		//Once you reach a depth of two, evaluate the game state and propagate the score 
		if(depth == 2) {
			double miniMaxEvaluation = node.getData().evaluateGameState(node);
			node.setNodeEvaluation(miniMaxEvaluation);
			return miniMaxEvaluation;
		}
		
		//Mr. X is the maximizing player
		if (isMaximizing) {
			bestScore = Double.NEGATIVE_INFINITY;
			GameState gameState = node.getData();
			//Loop through all possible Mr. X moves and make every one of them; 
			//Call minimax with minimizing player 
			for (int i = 0; i < gameState.getCoordinator().getMrX().getPossibleMoves().size(); i++) {
				GameState clonedState = node.getDeepCloneOfRepresentedState();
				Move move = clonedState.getCoordinator().getMrX().getPossibleMoves().get(i);
				clonedState.getCoordinator().getMrX().simulateMove(move, coordinator.getBoard());
				clonedState.getCoordinator().getMrX().findPossibleStationsAfterTicket(clonedState.getCoordinator().getBoard(), move.getTicket());
				clonedState.getCoordinator().generateAllPossibleMoveCombosDetectives();
				TreeNode<GameState> newChild = new TreeNode<GameState>(clonedState, cloner);
				double score = clonedState.miniMax(depth+1, false, newChild, cloner);
				node.addChild(newChild);
				bestScore = Math.max(score, bestScore);
				node.setNodeEvaluation(bestScore);
			}
		} else {
			bestScore = Double.POSITIVE_INFINITY;
			//Loop through all possible move combos and make every one of them
			//Call minimax with maximizing player
			List<List<Move>> allPossibleDetectiveCombos = coordinator.getAllPossibleMoveCombosDetectives();
			for (int i = 0; i < allPossibleDetectiveCombos.size(); i++) {
				GameState clonedState = node.getDeepCloneOfRepresentedState();
				List<List<Move>> allPossibleDetectiveCombosClonedState = clonedState.getCoordinator().getAllPossibleMoveCombosDetectives();
				
				for (int j = 0; j < allPossibleDetectiveCombosClonedState.get(i).size(); j++) {
					Detective det = clonedState.getCoordinator().getDetectives().get(j);
					Move move = allPossibleDetectiveCombosClonedState.get(i).get(j);
					
					if(clonedState.getCoordinator().getMrX().getCurrentStation() == move.getDestinationStation().getNameInt()) {
						bestScore = Double.NEGATIVE_INFINITY;
						node.setBestCombo(node.getData().getCoordinator().getAllPossibleMoveCombosDetectives().get(i));
						return bestScore;
					}
						
					det.moveDetective(move, clonedState.getCoordinator().getBoard());
					clonedState.getCoordinator().getMrX().findPossibleMoves(clonedState.getCoordinator().getBoard());
				}
				
				clonedState.getCoordinator().generateAllPossibleMoveCombosDetectives();
				TreeNode<GameState> newChild = new TreeNode<GameState>(clonedState, cloner);
				double score = clonedState.miniMax(depth+1, true, newChild, cloner);
				node.addChild(newChild);
			
				if (score < bestScore) {
					bestScore = score;
					List<Move> bestCombo = node.getParent().getData().getCoordinator().getAllPossibleMoveCombosDetectives().get(i);
					node.setBestCombo(bestCombo);
				}
				node.setNodeEvaluation(bestScore);
			}
		}
		return bestScore;
	}
	
	
	/**Evaluates a game state, using average distance of detectives to simulated position of Mr. X.
	 * 
	 * @return	the evaluation of the game state. 
	 */
	
	public double evaluateGameState(TreeNode<GameState> node) {
		GameState gameState = node.getData();
		double evaluation = gameState.getCoordinator().calculateAverageDistanceDetectives();
		return evaluation;
	}
	
	public List<Move> getBestDetectiveMoves() {	
		return this.bestDetectiveMoves;
	}
		
	public void setBestDetMoves(List<Move> bestDetMoves) {
		this.bestDetectiveMoves = bestDetMoves;	
	}
	
	public Move getWinningMove() {	
		return winningMove;
	}
	
	public void setWinningMove(Move winningMove) {	
		this.winningMove = winningMove;
	}
	
	public CoordinatePlayers getCoordinator() {	
		return coordinator;
	}
	
	public void setCoordinator(CoordinatePlayers coordinator) {	
		this.coordinator = coordinator;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---------------------------------\n");
		sb.append("THE CURRENT GAME STATE\n");
		sb.append("---------------------------------\n");
		sb.append(coordinator.toString());
		sb.append("---------------------------------\n");
		sb.append("The best detective moves = " + bestDetectiveMoves + "\n");
		return sb.toString();
	}
	
}
