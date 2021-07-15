package mrX_maven_run;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import com.rits.cloning.Cloner;


/**This class represents the main overview logic of the program.
 * 
 * @author aljaz
 *
 */
public class Hunt {
	
	private static Scanner sc = new Scanner(System.in);
	private static int moves = 24;
	private static int mrXLocation;
	
	/**The minimax algorithm that runs through the tree of possible game states.
	 * 
	 * @param depth
	 * @param isMaximizing
	 */
	
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
		
		Hunter hunter = new Hunter();
		
		System.out.println("Welcome to The Hunt for Mr X, the codebreaker for the game Scotland Yard!\n");

		hunter.setupDemo();
		hunter.initialMrXPossibleStations();
		
		for(int step = 1; step < moves+1; step++) {
			
			System.out.println(" ***** MOVE NR. " + (step) + " *****\n");
						
			String ticketUsed = hunter.moveMrX();
			
			hunter.coordinatePossibleDetectiveMoves();
			hunter.generateAllPossibleMoveCombosDetectives(hunter.getAllPossibleDetectiveMoves());
			
			
			// TODO: CHECK THIS 
			if (step == 3 || step == 8 || step == 13 || step == 18 || step == 24) {
				System.out.println("\nMr. X, reveal yourself!\n");
				System.out.println("Where is Mr. X at the moment?\n");
				mrXLocation = sc.nextInt();
				hunter.reveal(mrXLocation);
			}
			
			if (hunter.getAllPossibleMoveCombosDetectives().size()==1) {
				List<Move> bestMoves = new ArrayList<Move>();
				for (int i = 0; i < hunter.getAllPossibleMoveCombosDetectives().get(0).size(); i++) {
					Detective det = hunter.getListDetectives().get(i);
					Move move = hunter.getAllPossibleMoveCombosDetectives().get(0).get(i);
					bestMoves.add(move);
					hunter.setBestDetMoves(bestMoves);
					String detName = det.getName();
					System.out.println("Detective " + detName + " : " + move);
					hunter.moveDetective(det, move);
				}
				
				hunter.findMrXPossibleStations(ticketUsed);
				hunter.findMrXPossibleMoves();
				hunter.coordinatePossibleDetectiveMoves();
				hunter.generateAllPossibleMoveCombosDetectives(hunter.getAllPossibleDetectiveMoves());
				
				if(hunter.noMovesLeftCheck()) {
					break;
				}

			}
			
			else {
				//Clone Hunter
				Cloner cloner = new Cloner();
				TreeNode<Hunter> rootNode = new TreeNode<Hunter>(hunter, cloner);
				double bestScore = Double.NEGATIVE_INFINITY;
				
				for (int i = 0; i < hunter.getPossibleMrXmoves().size(); i++) {
					Move move = hunter.getPossibleMrXmoves().get(i);
					//System.out.println("THE SIMULATED MOVE nr. " + i + " = " + move);
					Hunter clonedState = rootNode.getDeepCloneOfRepresentedState();
					clonedState.simulateMrXmove(move);
					TreeNode<Hunter> startNode = new TreeNode<Hunter>(clonedState, cloner);
					rootNode.addChild(startNode);
					double score = clonedState.miniMax(0, false, startNode, cloner);
					bestScore = Math.max(score, bestScore);
					rootNode.setNodeEvaluation(bestScore);
				}
				
				double bestScore1 = Double.NEGATIVE_INFINITY;
				
				List<Move> bestCom = new ArrayList<Move>();
				
				for (int i = 0; i < rootNode.getChildren().size(); i++) {
					double nodeEvaluation = rootNode.getChildren().get(i).getNodeEvaluation();
					if (nodeEvaluation > bestScore1) {
						bestScore1 = nodeEvaluation;
						bestCom = rootNode.getChildren().get(i).getBestCombo();
						System.out.println(bestCom);
					}
					
				}
				
				hunter.setBestDetMoves(bestCom);
				
				for (int i = 0; i < hunter.getBestDetectiveMoves().size(); i++) {
					Detective det = hunter.getListDetectives().get(i);
					Move move = hunter.getBestDetectiveMoves().get(i);
					String detName = det.getName();
					System.out.println("Detective " + detName + " : " + move);
					hunter.moveDetective(det, move);
				}
			
			
			System.out.println("ORIGINAL HUNTER \n= " + hunter + "\n");
			}
		}
	}
}
