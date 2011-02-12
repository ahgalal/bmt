package control;

import model.business.Experiment;
import model.business.Group;
import model.business.If_Grp2GUI;
import model.business.Rat;
import utils.PManager;
import utils.saveengines.ExcelEngine;
import utils.saveengines.TextEngine;

public class InfoController {

	private TextEngine text_engine;
	private ExcelEngine excel_engine;
	private PManager pm;
	static InfoController default_controller;
	private StatsController stats_controller;
	private Experiment exp;

	private int curr_rat_number;
	private String curr_grp_name;
	private String exp_file_name;

	public void setExpFileName(String f_name)
	{
		exp_file_name=f_name;
	}

	public boolean isExperimentPresent()
	{
		return (exp!=null);
	}

	public boolean isThereAnyGroups()
	{
		return (exp.getNo_groups()!=0);
	}

	public InfoController(){
		text_engine=new TextEngine();
		excel_engine=new ExcelEngine();
		stats_controller = StatsController.getDefault();
		exp=new Experiment();
		pm=PManager.getDefault();
	}

	public static InfoController getDefault(){
		if(default_controller==null)
			default_controller=new InfoController();

		return default_controller;
	}

	public void saveExpInfo(String name, String user,  String date, String notes){
		exp.setExperimentInfo(name, user, date, notes);
	}

	public void saveGrpInfo(int grp_id,String name, int no_rats, String rats_numbering, String notes){
		Group tmp_grp=exp.getGroupByID(grp_id);
		if(tmp_grp==null)
		{
			Group gp = new Group(grp_id,name, no_rats, rats_numbering, notes);
			exp.addGroup(gp);
		}
		else //group is already existing ... edit it..
		{
			tmp_grp.setName(name);
			tmp_grp.setNo_rats(no_rats);
			tmp_grp.setNotes(notes);
			tmp_grp.setRats_numbering(rats_numbering);
		}
	}

	public void saveRatInfo(){
		Rat rat_tmp = new Rat(curr_rat_number);
		rat_tmp.setAll_entrance(stats_controller.getAll_entrance());
		rat_tmp.setCentralEntrance(stats_controller.getCentral_entrance());
		rat_tmp.setTotal_distance(stats_controller.getTotalDistance());
		rat_tmp.setRearing_ctr(stats_controller.getRearingCtr());
		rat_tmp.setCentralTime(stats_controller.getCentralTime());
		rat_tmp.setSessionTime((long)stats_controller.getSessionTimeTillNow());
		exp.getGroupByName(curr_grp_name).addRat(rat_tmp);
		writeToTXTFile(exp_file_name);
	}

	public void writeToTXTFile(String FilePath){
		text_engine.writeExpInfoToTXTFile(FilePath, exp);
	}

	public void loadInfoFromTXTFile(String file_name){
		text_engine.readExpInfoFromTXTFile(file_name, exp);
	}

	public void writeToExcelFile(String FilePath)
	{
		excel_engine.writeExpInfoToExcelFile(FilePath, exp);
	}

	public String[] getGroupsNames()
	{
		String[] res=new String[exp.getGroups().size()];
		int i=0;
		for(If_Grp2GUI grp: exp.getGroups())
		{
			res[i]=grp.getName();
			i++;
		}
		return res;
	}
	public int setCurrentRatAndGroup(int rat_num,String grp_name,boolean ovrrd)
	{
		Group tmp_grp=exp.getGroupByName(grp_name);
		if(tmp_grp!=null){	//Group exists
			if(tmp_grp.getRatByNumber(rat_num)==null | ovrrd==true)
			{
				curr_rat_number=rat_num;
				curr_grp_name = grp_name;
				return 0;
			}
			return 1;
		}else
			return -1;
	}

	@SuppressWarnings("static-access")
	public void unloadExperiment()
	{
		exp.clearExperimentData();
		pm.frm_exp.clearForm();
		pm.frm_grps.clearForm();
		pm.main_gui.clearForm();
		excel_engine.reset();
	}
}