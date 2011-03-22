package modules;

import model.business.Experiment;
import model.business.Group;
import model.business.If_Grp2GUI;
import model.business.Rat;
import utils.saveengines.ExcelEngine;
import utils.saveengines.TextEngine;
import utils.video.processors.Data;

public class ExperimentModule extends Module {

	private int curr_rat_number;
	private String curr_grp_name;
	private Experiment exp;
	private String exp_file_name;

	private TextEngine text_engine;
	private ExcelEngine excel_engine;

	public ExperimentModule(String name, ModuleConfigs config) {
		super(name, config);
		exp=new Experiment();
		text_engine=new TextEngine();
		excel_engine=new ExcelEngine();

		initialize();
	}

	@Override
	public void deInitialize() {
		saveRatInfo();
	}

	@Override
	public void process() {

	}

	@Override
	public void updateGUICargoData() {
		gui_cargo.setDataByIndex(0, exp.getName());
		gui_cargo.setDataByIndex(1, curr_grp_name);
		gui_cargo.setDataByIndex(2, Integer.toString(curr_rat_number));

	}

	@Override
	public void updateFileCargoData() {
		file_cargo.setDataByIndex(0, Integer.toString(curr_rat_number));
		file_cargo.setDataByIndex(1, curr_grp_name);
	}

	@Override
	public void updateConfigs(ModuleConfigs config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDataObject(Data data) {
		// TODO Auto-generated method stub

	}

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

	public void saveExpInfo(String name, String user,  String date, String notes){
		exp.setExperimentInfo(name, user, date, notes);
	}

	public void saveGrpInfo(int grp_id,String name, String rats_numbering, String notes){
		Group tmp_grp=exp.getGroupByID(grp_id);
		if(tmp_grp==null)
		{
			Group gp = new Group(grp_id,name, rats_numbering, notes);
			exp.addGroup(gp);
		}
		else //group is already existing ... edit it..
		{
			tmp_grp.setName(name);
			tmp_grp.setNotes(notes);
			tmp_grp.setRats_numbering(rats_numbering);
		}
	}

	private int getIndexByStringValue(String[] arr,String val)
	{
		for(int i=0;i<arr.length;i++)
			if(arr[i].equals(val))
				return i;
		return -1;
	}

	public void saveRatInfo(){
		if(exp.getMeasurementsList()==null)
			exp.setMeasurementsList(ModulesManager.getDefault().getCodeNames());
		String[] measurements_list=exp.getMeasurementsList();
		String[] data	= ModulesManager.getDefault().getFileData();
		String[] code_names = ModulesManager.getDefault().getCodeNames();
		boolean override_rat = false;
		Rat rat_tmp =exp.getGroupByName(curr_grp_name).getRatByNumber(curr_rat_number);
		if(rat_tmp==null)
			rat_tmp = new Rat(measurements_list);
		else
			override_rat=true;

		for(int i=0;i<measurements_list.length;i++)
		{
			rat_tmp.setValueByMeasurementName(
					measurements_list[i],
					data[getIndexByStringValue(
							code_names,
							measurements_list[i])]
			);
		}

		if(!override_rat)
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

	public int validateRatAndGroup(int rat_num,String grp_name)
	{
		Group tmp_grp=exp.getGroupByName(grp_name);
		if(tmp_grp!=null){	//Group exists
			if(tmp_grp.getRatByNumber(rat_num)==null)
			{
				return 0;
			}
			return 1;
		}else
			return -1;
	}

	public int setCurrentRatAndGroup(int rat_num,String grp_name)
	{
		Group tmp_grp=exp.getGroupByName(grp_name);
		if(tmp_grp!=null){	//Group exists
			curr_rat_number=rat_num;
			curr_grp_name = grp_name;
			return 0;
		}else
			return -1;
	}

	public void unloadExperiment()
	{
		exp.clearExperimentData();
		excel_engine.reset();
	}

	@Override
	public void initialize() {
		gui_cargo=new Cargo(
				new String[] {
						"Experiment's Name",
						"Group's Name",
						"Rat Number"
				}
		);

		file_cargo = new Cargo(
				new String[]{
						"Number",
						"Group"
				}
		);		
	}



}
