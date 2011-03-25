/*
 * package control; import java.awt.Point; import java.util.ArrayList;
 *//**
 * Maintains counters information, including (current zone, session's total
 * time).
 * 
 * @author Creative
 */
/*
 * public class StatsController { static StatsController default_controller;
 * public boolean require_update_gui; private Point current_position;
 *//**
 * initialization of attributes
 */
/*
 * public StatsController() { current_position=new Point(); } public static
 * StatsController getDefault() { if(default_controller==null)
 * default_controller=new StatsController(); return default_controller; }
 *//**
 * Uses information got from the VideoProcessor to calculate the new values
 * of counters (current zone, total distance , total time ..)
 * 
 * @param pos
 *            Current position of the rat (x,y)
 * @param is_rearing
 *            is the rat Rearing?
 */
/*
 * public void updateStats(Point pos,boolean is_rearing) { current_position.x =
 * pos.x; current_position.y = pos.y; require_update_gui=true; }
 *//**
 * Resets all counters
 */
/*
 * private void initializeSession() { require_update_gui=false; // This array
 * will hold the positions of the object through the whole experiment
 * require_update_gui=true; } }
 */