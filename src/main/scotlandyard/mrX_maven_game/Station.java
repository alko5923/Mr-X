package mrX_maven_game;

import java.util.ArrayList;

public class Station {
	private int name;
	private String stationType;
	private boolean blackStation;
	private ArrayList<Integer> neighboursTaxi;
	private ArrayList<Integer> neighboursBus;
	private ArrayList<Integer> neighboursTube;
	private int value;
	private boolean occupied;
	
	/**
	 * The class constructor.
	 * @param name
	 * @param stationType
	 * @param blackStation
	 * @param neighboursTaxi
	 * @param neighboursBus
	 * @param neighboursTube
	 */
	public Station(int name, String stationType, boolean blackStation, ArrayList<Integer> neighboursTaxi, ArrayList<Integer> neighboursBus, ArrayList<Integer> neighboursTube) {
		this.name = name;
		this.stationType = stationType;
		this.neighboursTaxi = neighboursTaxi;
		this.neighboursBus = neighboursBus;
		this.neighboursTube = neighboursTube;
	}
	
	public int getNameInt() {
		return this.name;
	}
	
	public String getStationType() {
		return stationType;
	}
	
	public boolean isBlackStation() {
		return blackStation;
	}
	
	public ArrayList<Integer> getTaxiNeighbours() {
		return neighboursTaxi;
	}
	
	public int getNumberTaxiConnections() {
		return neighboursTaxi.size();
	}

	public ArrayList<Integer> getBusNeighbours() {
		return neighboursBus;
	}

	public int getNumberBusConnections() {
		return neighboursBus.size();
	}
	
	public ArrayList<Integer> getTubeNeighbours() {
		return neighboursTube;
	}

	public int getNumberTubeConnections() {
		return this.neighboursTube.size();
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

	public boolean isOccupied() {
		return this.occupied;
	}
	
	public void occupyStation() {
		this.occupied = true;
	}
	
	public void unoccupyStation() {
		this.occupied = false;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Station nr. = " + name + "\n");
		sb.append("Station type = " + stationType + "\n");
		sb.append("Black station = " + blackStation + "\n");
		sb.append("Station value = " + value + "\n");
		sb.append("Station occupied = " + occupied + "\n");
		
		return sb.toString();
	}
}
