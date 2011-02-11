package control;

import java.awt.Point;
import java.util.ArrayList;

import model.Zone.ZoneType;

/**
 * Maintains counters information, including (current zone, session's total time).
 * @author Creative
 */
public class StatsController {

	static StatsController default_controller;
	public boolean require_update_gui;
	private Point current_position;
	private Point old_position;
	private int current_zone_num;
	private ZonesController zone_controller;
	private ArrayList<Point> arr_path; // This array will hold the positions of the object through the whole experiment 
	private long session_start_time;
	private long session_end_time;
	private long central_start_tmp;
	private boolean central_flag;
	private float central_zone_time;
	private float scale;
	private long total_distance;
	private int updated_zone_number;
	private int hyst_value=50;
	private int all_entrance,central_entrance;
	private long central_zone_time_tmp;
	private int rearing_ctr;
	private boolean is_rearing;


	/**
	 * initialization of attributes
	 */
	public StatsController() {
		arr_path = new ArrayList<Point>();
		old_position=new Point();
		current_position=new Point();
		scale=10;
	}

	public static StatsController getDefault()
	{
		if(default_controller==null)
			default_controller=new StatsController();

		return default_controller;
	}

	/**
	 * initializes a new session (reset counters)
	 * and starts the timer for the new session
	 */
	public void startSession()
	{
		initializeSession();
		startSessionTime();
	}

	/**
	 * stops session timer
	 */
	public void endSession()
	{
		stopSessionTimer();
	}

	/**
	 * Uses information got from the VideoProcessor to calculate the new
	 * values of counters (current zone, total distance , total time ..)
	 * 
	 * @param pos Current position of the rat (x,y)
	 * @param is_rearing is the rat Rearing?
	 */
	public void updateStats(Point pos,boolean is_rearing)
	{
		if(current_position!=null)
		{
			old_position.x=current_position.x;
			old_position.y=current_position.y;
		}

		if(!this.is_rearing & is_rearing) //it is rearing
			rearing_ctr++;
		this.is_rearing=is_rearing;

		current_position.x = pos.x;
		current_position.y = pos.y;
		updated_zone_number=zone_controller.getZone(current_position.x, current_position.y);
		zoneHysteresis();
		addPointToPosArray(pos);
		updateTotalDistance();
		updateCentralZoneTime();

		require_update_gui=true;
	}

	/**
	 * Calculates the new zone number , based on the rat's position.
	 * Uses a hysteresis function.
	 * Example:
	 * when the rat moves from zone1 to zone2, it will pass on the boundary
	 * between the two zones before entering zone2.
	 * the rat is not considered to be in zone2 until it covers certain
	 * distance in zone2 away from the boundary.(hysteresis distance)
	 * 
	 * this strategy is used to prevent fluctuations in the total zone entrance
	 * counter, in case of the rat staying on the boundary for some time (its
	 * detected position fluctuates, so, the zone detection should have immunity
	 * against these fluctuations)
	 */
	private void zoneHysteresis()
	{
		if(current_zone_num!= updated_zone_number & updated_zone_number!=-1)
		{
			int zone_up_left = 0,
			zone_up_right = 0,
			zone_down_left = 0,
			zone_down_right = 0;
			try
			{
				zone_up_left = zone_controller.getZone(current_position.x-hyst_value/2,	current_position.y+hyst_value/2);
				zone_up_right = zone_controller.getZone(current_position.x+hyst_value/2,	current_position.y+hyst_value/2);
				zone_down_left = zone_controller.getZone(current_position.x-hyst_value/2,	current_position.y-hyst_value/2);
				zone_down_right = zone_controller.getZone(current_position.x+hyst_value/2,	current_position.y-hyst_value/2);
			}
			catch(Exception e)
			{
				System.err.print("Error fel index .. zoneHysteresis!");
			}
			if(zone_up_left !=current_zone_num & zone_up_right !=current_zone_num &zone_down_left !=current_zone_num & zone_down_right !=current_zone_num) //we are in a new zone :)
			{
				updateZoneCounters();
			}
		}
	}

	/**
	 * updates (all zones entrance and central zone entrance) counters
	 */
	private void updateZoneCounters()
	{
		current_zone_num = updated_zone_number;
		all_entrance++;
		if(zone_controller.getZoneByNumber(current_zone_num)!=null)
			if(zone_controller.getZoneByNumber(current_zone_num).getZone_type()==ZoneType.CENTRAL_ZONE)
				central_entrance++;
	}

	 
	/**
	 * Saves the path of the rat in the path array in form of points (x,y)
	 * @param pos Current rat position
	 */
	private void addPointToPosArray(Point pos) {
		// we need to add some tolerance due to noise 
		//path.add(new Point(pos.x,pos.y));
	}

	
	/**
	 * To Calculate the total distance covered by the rat
	 * through the experiment
	 */
	private void updateTotalDistance(){
		if(old_position!=null)
			total_distance+=current_position.distance(old_position)/scale;	
	}

	/**
	 * Resets all counters
	 */
	private void initializeSession()
	{
		require_update_gui=false;
		current_zone_num=-1;
		arr_path.clear(); // This array will hold the positions of the object through the whole experiment 
		session_start_time=0;
		session_end_time=0;
		central_start_tmp=0;
		central_flag=false;
		central_zone_time=0;
		total_distance=0;
		updated_zone_number=-1;
		hyst_value=50;
		all_entrance=0;
		central_entrance=0;
		central_zone_time_tmp=0;
		require_update_gui=true;
		rearing_ctr=0;
	}

	/**
	 * Saves session's start time
	 */
	private void startSessionTime (){
		session_start_time=System.currentTimeMillis();
	}

	/**
	 * Saves session's end time
	 */
	private void stopSessionTimer (){
		session_end_time=System.currentTimeMillis();
	}

	/**
	 * Updates "central zone time" counter , if the rat is in a central zone.
	 */
	private void updateCentralZoneTime ()
	{
		if(zone_controller.getNumberOfZones()!=-1)
		{
			if(current_zone_num!=-1 & zone_controller.getZoneByNumber(current_zone_num)!=null)
				if (zone_controller.getZoneByNumber(current_zone_num).getZone_type()== ZoneType.CENTRAL_ZONE & central_flag==false)
				{
					central_start_tmp = System.currentTimeMillis();
					central_flag=true;
				}
				else if(zone_controller.getZoneByNumber(current_zone_num).getZone_type()== ZoneType.CENTRAL_ZONE & central_flag==true)
					central_zone_time_tmp = ((System.currentTimeMillis() - central_start_tmp)/1000);
				else if (zone_controller.getZoneByNumber(current_zone_num).getZone_type()!= ZoneType.CENTRAL_ZONE & central_flag==true)
				{
					central_zone_time+=central_zone_time_tmp;
					central_flag=false;
				}
		}
	}

	public float getTotalSessionTime()
	{
		long totalTime = (session_end_time - session_start_time)/(160000);// total time in minutes
		return totalTime;
	}

	public int getAll_entrance() {
		return all_entrance;
	}

	public int getCentral_entrance() {
		return central_entrance;
	}

	public float getSessionTimeTillNow()
	{
		long time = (System.currentTimeMillis()- session_start_time)/(1000);// total time in seconds
		return time;
	}

	public int getCurrentZoneNumber() {
		return current_zone_num;
	}

	public long getTotalDistance()
	{
		return total_distance;
	}

	public float getCentralTime()
	{
		return central_zone_time;
	}


	//Scaling measurement function...takes (x1,y1) & (x2,y2) & resolution of the picture width & height
	/**
	 * Calculates the scale between real world and the cam image.
	 * using the distance between two points on the screen and the distance
	 * between them in real image.
	 * @param p1 First point of measurement
	 * @param p2 Second point of measurement
	 * @param real_distance distance entered by the user as real distance
	 */
	public void setScale (Point p1,Point p2,float real_distance)
	{
		double screen_distance = p1.distance(p2);
		//note: horizontal scale === vertical scale (the cam is perpendicular on the field)

		double cmResult = screen_distance/real_distance;
		scale= (float) cmResult;
	}

	public void setHysteresis(int h)
	{
		if(h!=-1)
			hyst_value=h;
	}

	/**
	 * Initializes the zone controller instance.
	 */
	public void init()
	{
		zone_controller=ZonesController.getDefault();
	}

	public int getRearingCtr() {
		return rearing_ctr;
	}
}
