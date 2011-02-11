package model.business;

import java.util.ArrayList;


/**
 * @author ShaQ
 * Handles all the experiments info
 */
public class Experiment implements If_Exp2GUI {

	private ArrayList<model.business.Group> groups;
	private String name;
	private String user;
	private String date;
	private String notes;
	//I deleted no_groups .... data duplication with groups.size()


	/**
	 * Clears the experiment info(data)
	 */
	public void clearExperimentData()
	{
		for(Group g:groups)
			g.getAllRats().clear();
		groups.clear();
	}

	/**
	 * @param id the id of the group to return its instance
	 * @return instance of the group having the given id
	 */
	public Group getGroupByID(int id)
	{
		for(Group tmp_g:groups)
		{
			if(tmp_g.getId()==id)
				return tmp_g;
		}
		return null;
	}
	
	public Experiment(){
		groups=new ArrayList<Group>();
	}
	
	/**
	 * Sets the info of an experiment
	 * @param name the name of the experiment
	 * @param user the user name who makes the experiment  
	 * @param date2 the date of the experiment
	 * @param notes any additional notes about the experiment
	 */
	public void setExperimentInfo(String name, String user,  String date2, String notes){
		this.name = name;
		this.user = user;
		this.date = date2;
		this.notes = notes;
	}

	/*public void saveData(PrintStream p){ 
		try { // Create a new file output stream // connected to "myfile.txt" 
			p.println("Experiment Info :");
			p.println("Experiment Name" + '\t' + "User Name" + '\t' + "Number Of Groups" + '\t' + "Date"
					 + '\t' + "Notes");
			p.println(name + '\t' + user + '\t' + no_groups + '\t' + date + '\t' + notes);
			p.println("Experiment Name : " + name);
			p.println("User Name : " + user);
			p.println("Number Of Groups : " + no_groups);
			p.println("Date : " + date);
			p.println("Notes : " + notes);
			p.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}*/

	/**
	 * Adds a new group to the experiment groups
	 * @param g the reference to a group to add to the experiment
	 */
	public void addGroup(Group g){
		groups.add(g);
	}

/*	public void addNewGroup(String name, int no_rats, String rats_numbering, String notes){
		model.Group g = new model.Group(name,no_rats,rats_numbering,notes);
		groups.add(g);
	}*/

	/* (non-Javadoc)
	 * @see model.If_Exp2GUI#getName()
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see model.If_Exp2GUI#getUser()
	 */
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	/* (non-Javadoc)
	 * @see model.If_Exp2GUI#getDate()
	 */
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	/* (non-Javadoc)
	 * @see model.If_Exp2GUI#getNotes()
	 */
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public ArrayList<model.business.Group> getGroups() {
		return groups;
	}

	public Group getGroupByName(String name)
	{
		for(Group tmp_g:groups)
		{
			if(tmp_g.getName().equals(name))
				return tmp_g;
		}
		return null;
	}

	public void setGroups(ArrayList<model.business.Group> groups) {
		this.groups = groups;
	}

	/**
	 * @return the number of groups in the experiment
	 */
	public int getNo_groups() {
		return groups.size();
	}


}


