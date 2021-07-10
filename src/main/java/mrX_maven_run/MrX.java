package mrX_maven_run;

import java.util.ArrayList;
import java.util.List;

/**This class represents Mr. X, the villain of the game.
 * 
 * @author aljaz
 *
 */
public class MrX {
	private int taxiTicketsAvailable;
	private int busTicketsAvailable;
	private int tubeTicketsAvailable;
	private List<String> ticketsUsed = new ArrayList<String>();
	private List<Station> mrXReveals = new ArrayList<Station>();
	private List<Station> stationsList = new ArrayList<Station>();
	private int simulatedCurrentStation;
	
	public MrX(List<Station> stationsList, int taxiTicketsAvailable, int busTicketsAvailable, int tubeTicketsAvailable) {
		this.stationsList = stationsList;
		this.taxiTicketsAvailable = taxiTicketsAvailable;
		this.busTicketsAvailable = busTicketsAvailable;
		this.tubeTicketsAvailable = tubeTicketsAvailable;
	}
	
	
	/**Get the number of available taxi tickets. 
	 * 
	 * @return	number of available taxi tickets
	 */
	public int getAvailableTaxi() {
		return taxiTicketsAvailable;
	}

	/**Get the number of available bus tickets. 
	 * 
	 * @return	number of available bus tickets
	 */
	public int getAvailableBus() {
		return busTicketsAvailable;
	}
	
	/**Get the number of available tube tickets. 
	 * 
	 * @return	number of available tube tickets
	 */
	public int getAvailableTube() {
		return tubeTicketsAvailable;
	}
	
	/**Get the simulated current station.
	 * 
	 * @return	the simulated current station
	 */
	public int getSimulatedCurrentStation() {
		return simulatedCurrentStation;
	}
	
	/**Set the simulated current station.
	 * 
	 * @param simulatedCurrentStation
	 */
	public void setSimulatedCurrentStation(int simulatedCurrentStation) {
		this.simulatedCurrentStation = simulatedCurrentStation;
	}
	
	
	/**Add a station to the list of stations where Mr. X. has revealed himself. 
	 * 
	 * @param 	station of reveal
	 * @throws 	LocationNotFoundException
	 */
	public void addToReveals(int stationName) {
			Station station = stationsList.get(stationName-1);
			mrXReveals.add(station);
	}
	
	/**Remove a taxi ticket from the number of available taxi tickets. 
	 * 
	 */
	public void removeXTaxiTicket() {
		taxiTicketsAvailable -= 1;
	}
	
	/**Remove a bus ticket from the number of available bus tickets. 
	 * 
	 */
	public void removeXBusTicket() {
		busTicketsAvailable -= 1;
	}
	
	/**Remove a tube ticket from the number of available tube tickets. 
	 * 
	 */
	public void removeXTubeTicket() {
		tubeTicketsAvailable -= 1;
	}
	
	/**Add a taxi ticket to the number of available taxi tickets. 
	 * 
	 */
	public void addTaxiTicket() {
		taxiTicketsAvailable+=1;
	}
	
	/**Add a bus ticket to the number of available bus tickets. 
	 * 
	 */
	public void addBusTicket() {
		busTicketsAvailable+=1;
	}
	
	/**Add a tube ticket to the number of available tube tickets. 
	 * 
	 */
	public void addTubeTicket() {
		tubeTicketsAvailable+=1;
	}
	
	/**Add a ticket to the list of used tickets.
	 * 
	 * @param 	the ticket used. 
	 */
	public void addToUsedTickets(String ticket) {
		ticketsUsed.add(ticket);
	}
	
	
	/**Print information about Mr. X. to the console. 
	 * 
	 */
	public String toString() {
			
			StringBuilder sb = new StringBuilder();
			sb.append("---------------------------------\n");
			sb.append("     *** MR X INFO ***     \n");
			sb.append("---------------------------------\n");
			
			sb.append("Taxi tickets available = "); 
			sb.append(String.valueOf(taxiTicketsAvailable));
			sb.append("\nBus tickets available = ");
			sb.append(String.valueOf(busTicketsAvailable));
			sb.append("\nTube tickets available = ");
			sb.append(String.valueOf(tubeTicketsAvailable));
			
			sb.append("\nMr. X used tickets: [ "); 
			for (int i = 0; i < ticketsUsed.size(); i+=1 ) {
				sb.append(ticketsUsed.get(i));
				sb.append(", ");
			}
			sb.append(" ]\n");
			
			sb.append("Mr. X reveal list: [ "); 
			for (int i = 0; i < mrXReveals.size(); i+=1 ) {
				sb.append(mrXReveals.get(i).getNameInt());
				sb.append(", ");
			}
			sb.append(" ]\n");
			
			return sb.toString();
		}

}
