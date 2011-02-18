package model.business;

import java.util.ArrayList;


/**
 * @author ShaQ
 * Handles all groups and their info
 */
public class Group implements If_Grp2GUI {
	
	private String name;
	private int id;
	private int no_rats;
	private String rats_numbering;
	private String notes;
	private ArrayList<Rat> arr_rats;
	
	public Group(){
		
	}
	
	public Group(int id,String name, int no_rats, String rats_numbering, String notes){
		this.name = name;
		this.no_rats  = no_rats;
		this.rats_numbering = rats_numbering;
		this.notes = notes;
		this.id=id;
		arr_rats = new ArrayList<Rat>();
	}
	
	/**
	 * @param num the number of the rat to return its instance 
	 * @return instance of the rat having the given number 
	 * This function loops on all the rats in the group and checks the num 
	 * of each rat if it is the required rat we return a reference to that group
	 * else return null
	 */
	public Rat getRatByNumber(int num)
	{
		for(Rat r_tmp:arr_rats)
		{
			if(r_tmp.getNumber()==num)
				return r_tmp;
		}
		return null;
	}
	
	/**
	 * Adds new rat to a group 
	 * @param new_rat reference to a new rat object 
	 */
	public void addRat(Rat new_rat)
	{
		arr_rats.add(new_rat);
	}
	
	public ArrayList<Rat> getAllRats()
	{
		return arr_rats;
	}
	
	/*public void saveData(PrintStream p){
		try { // Create a new file output stream // connected to "myfile.txt"
			p.println("Group Info :");
			p.println("Group Name" + '\t' + "Number of Rats" + '\t' + "Rats Numbering" + '\t' + "Notes");
			p.println(name + '\t' + no_rats + '\t' + rats_numbering + '\t' + notes);
			p.println("Group Name : " + name);
			p.println("Number of Rats : " + no_rats);
			p.println("Rats Numbering : " + rats_numbering);
			p.println("Notes : " + notes);
			p.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}*/
	
	
	/* (non-Javadoc)
	 * @see model.If_Grp2GUI#getName()
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see model.If_Grp2GUI#getNo_rats()
	 */
	public int getNo_rats() {
		return no_rats;
	}
	
	public void setNo_rats(int noRats) {
		no_rats = noRats;
	}
	
	/* (non-Javadoc)
	 * @see model.If_Grp2GUI#getRats_numbering()
	 */
	public String getRats_numbering() {
		return rats_numbering;
	}
	
	public void setRats_numbering(String ratsNumbering) {
		rats_numbering = ratsNumbering;
	}
	
	/* (non-Javadoc)
	 * @see model.If_Grp2GUI#getNotes()
	 */
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setId(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see model.If_Grp2GUI#getId()
	 */
	public int getId() {
		return id;
	}
		
}
