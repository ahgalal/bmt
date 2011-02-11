package utils.saveengines;

import utils.ExcelWrapper;
import model.business.Experiment;
import model.business.Group;
import model.business.Rat;


/**
 * Responsible for saving Experiment's data to an Excel file.
 * @author Creative
 */
public class ExcelEngine {

	ExcelWrapper excel_wrapper;			//Excel wrapper utility for creating sheets/cells

	public ExcelEngine()
	{
		excel_wrapper=new ExcelWrapper();
	}

	/**
	 * Saves the Experiment's information to an Excel file.
	 * @param filename file path to save the experiment information to
	 * @param exp the Experiment to save its information
	 */
	public void writeExpInfoToExcelFile(String filename,Experiment exp)
	{
		try {
			excel_wrapper.fillRow(-1, new String[] {Cls_Constants.h_exp_name,exp.getName()});
			excel_wrapper.fillRow(-1, new String[] {Cls_Constants.h_exp_user,exp.getUser()});
			excel_wrapper.fillRow(-1, new String[] {Cls_Constants.h_exp_date,exp.getDate()});
			excel_wrapper.fillRow(-1, new String[] {Cls_Constants.h_exp_notes,exp.getNotes()});

			excel_wrapper.fillRow(-1, new String[] {""});

			for (Group grp_tmp:exp.getGroups()){
				excel_wrapper.fillRow(-1, new Object[] {Cls_Constants.h_grp_id,(Integer)(grp_tmp.getId())});
				excel_wrapper.fillRow(-1, new Object[] {Cls_Constants.h_grp_name,grp_tmp.getName()});
				excel_wrapper.fillRow(-1, new Object[] {Cls_Constants.h_grp_no_rats,(Integer)(grp_tmp.getNo_rats())});
				excel_wrapper.fillRow(-1, new Object[] {Cls_Constants.h_grp_rats_numbers,grp_tmp.getRats_numbering()});
				excel_wrapper.fillRow(-1, new Object[] {Cls_Constants.h_grp_notes,grp_tmp.getNotes()});

				excel_wrapper.fillRow(-1, new Object[] {""});

				excel_wrapper.fillRow(-1,
						new Object[] {"Number","Group","Central Zone Entrance",
						"Central Time","Session Time","Rearing counter",
						"All Zones Entrance","Distance"});
				for(Rat rat_tmp:grp_tmp.getAllRats())
				{
					excel_wrapper.fillRow(-1,
							new Object[] {
							(Integer)(rat_tmp.getNumber()),
							grp_tmp.getName(),
							(Integer)(int)((rat_tmp.getCentralEntrance())),
							(Integer)(int)(rat_tmp.getCentralTime()),
							(Integer)(int)(rat_tmp.getSessionTime()),
							(Integer)(int)(rat_tmp.getRearing_ctr()),
							(Integer)(rat_tmp.getAll_entrance()),
							(Integer)(int)(rat_tmp.getTotal_distance())});
				}
			}
			excel_wrapper.saveToFile(filename);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 

	}

	/**
	 * Resets the Excel wrapper
	 */
	public void reset() {
		excel_wrapper.reset();		
	}

}
