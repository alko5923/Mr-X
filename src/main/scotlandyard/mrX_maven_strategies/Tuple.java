package mrX_maven_strategies;

public class Tuple {
	
	private Integer station;
	private Integer distance;
	
	public Tuple (Integer station, Integer distance) {
		
		setStation(station);
		setDistance(distance);
		
	}

	public Integer getStation() {
		return station;
	}

	public void setStation(int station) {
		this.station = station;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("Station = " + station + "\n");
		sb.append("Distance to station = " + distance);
		return sb.toString();
		
	}
	
}
