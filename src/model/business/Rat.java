package model.business;

/**
 * @author ShaQ
 * Handles all rats and their info
 */
public class Rat {

	private int number;
	private int all_entrance;//,central;
	private long session_time;
	private long central_entrance;
	private float central_zone_time;
	private long total_distance;
	private int rearing_ctr;
	
	public long getTotal_distance() {
		return total_distance;
	}

	public void setTotal_distance(long totalDistance) {
		total_distance = totalDistance;
	}

	public Rat(){
		
	}
	
	public Rat(int number){
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getAll_entrance() {
		return all_entrance;
	}

	public void setAll_entrance(int allEntrance) {
		all_entrance = allEntrance;
	}

	public long getSessionTime() {
		return session_time;
	}

	public void setSessionTime(long sessionEndTime) {
		session_time = sessionEndTime;
	}

	public long getCentralEntrance() {
		return central_entrance;
	}

	public void setCentralEntrance(long centralStartTmp) {
		central_entrance = centralStartTmp;
	}

	public float getCentralTime() {
		return central_zone_time;
	}

	public void setCentralTime(float centralZoneTimeTmp) {
		central_zone_time = centralZoneTimeTmp;
	}

	public void setRearing_ctr(int rearing_ctr) {
		this.rearing_ctr = rearing_ctr;
	}

	public int getRearing_ctr() {
		return rearing_ctr;
	}

}
