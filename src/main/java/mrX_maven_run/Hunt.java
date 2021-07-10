package mrX_maven_run;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
		
		Hunter hunter = new Hunter();
		
		System.out.println("Welcome to The Hunt for Mr X, the codebreaker for the game Scotland Yard!\n");

		hunter.setupDemo();
		hunter.initialMrXPossibleStations();
		hunter.findMrXPossibleMoves();
		hunter.coordinatePossibleDetectiveMoves();
		hunter.generateAllPossibleMoveCombosDetectives(hunter.getAllPossibleDetectiveMoves());
		System.out.println(hunter);

		
		for(int step = 1; step < moves+1; step++) {
			
			System.out.println(" ***** Move nr. " + (step) + " *****\n");
						
			String ticketUsed = hunter.moveMrX();
			
			if (hunter.getAllPossibleMoveCombosDetectives().size()==1) {
				List<Move> bestMoves = new ArrayList<Move>();
				for (int i = 0; i < hunter.getAllPossibleMoveCombosDetectives().get(0).size(); i++) {
					Detective det = hunter.getListDetectives().get(i);
					Move move = hunter.getAllPossibleMoveCombosDetectives().get(0).get(i);
					bestMoves.add(move);
					hunter.setBestDetMoves(bestMoves);
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
			
			
			//Clone Hunter
			Cloner cloner = new Cloner();
			Hunter hunterCloneTest = cloner.deepClone(hunter);
			
			//Make a move on the HunterCloneTest
			Move move = hunterCloneTest.getListDetectives().get(0).getPossibleMovesCurrentStation().get(0);
			hunterCloneTest.moveDetective(hunterCloneTest.getListDetectives().get(0), move);
			
			hunter.findMrXPossibleStations(ticketUsed);
			hunter.findMrXPossibleMoves();
			hunter.coordinatePossibleDetectiveMoves();
			hunter.generateAllPossibleMoveCombosDetectives(hunter.getAllPossibleDetectiveMoves());
			
			hunterCloneTest.findMrXPossibleStations(ticketUsed);
			hunterCloneTest.findMrXPossibleMoves();
			hunterCloneTest.coordinatePossibleDetectiveMoves();
			hunterCloneTest.generateAllPossibleMoveCombosDetectives(hunterCloneTest.getAllPossibleDetectiveMoves());
			
			//Reset the hunterCloneTest
			hunterCloneTest = hunter;
			
//			//TODO: Run the minimax on the clone and save the best detective moves found
//			List<Move> bestDetMoves = hunterClone.bestDetectiveMoves(ticketUsed, step, clonedDetectives, clonedStations);
//			
//			hunter.setBestDetMoves(bestDetMoves);
//			
//			//Read the best detective moves found and execute them on the original Hunter
//			for (int i = 0; i < hunter.getNrOfDetectives(); i++) {
//				Detective detective = hunter.getListDetectives().get(i);
//				for (int j = 0; j < hunter.getBestDetectiveMoves().size(); j++) {
//					Move move = hunter.getBestDetectiveMoves().get(j);
//					hunter.moveDetective(detective, move);
//				}
//			}
			
			// TODO: CHECK THIS 
			if (step == 3 || step == 8 || step == 13 || step == 18 || step == 24) {
				System.out.println("\nMr. X, reveal yourself!\n");
				System.out.println("Where is Mr. X at the moment?\n");
				mrXLocation = sc.nextInt();
				hunter.reveal(mrXLocation);
			}
			
			System.out.println("ORIGINAL HUNTER \n= " + hunter + "\n");
			System.out.println("HUNTER CLONE = \n" + hunterCloneTest + "\n");
		}
			
	}
}
