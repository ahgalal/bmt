package utils.saveengines;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import model.business.Experiment;
import model.business.Group;
import model.business.If_Grp2GUI;
import model.business.Rat;
import utils.PManager;

/**
 * Responsible for saving/loading Experiment's data to/from Text files.
 * @author Creative
 */
public class TextEngine {

	private PManager pm;

	public TextEngine()
	{
		pm=PManager.getDefault();
	}


	private FileOutputStream out;
	private PrintStream p;

	private FileInputStream fis = null;
	private BufferedInputStream bis = null;

	/**
	 * Loads Experiment's data from a Text file to an Experiment object.
	 * @param file_name file path to the text file containing the experiment's information
	 * @param exp Experiment object to load the information to
	 */
	public void readExpInfoFromTXTFile(String file_name,Experiment exp)
	{
		try {
			String tmp_line =null;
			String[] line_data;
			fis = new FileInputStream(file_name);
			bis = new BufferedInputStream(fis);

			BufferedReader buf_rdr = new BufferedReader(new InputStreamReader(bis));
			boolean group_rats_are_loaded=false;
			Group grp_tmp = null;
			while (buf_rdr.ready()) {
				if(!group_rats_are_loaded)
					tmp_line = buf_rdr.readLine();
				group_rats_are_loaded=false;
				if (tmp_line.equals(Cls_Constants.h_exp)){ //load exp. info
					String tmp_name= buf_rdr.readLine().substring(Cls_Constants.h_exp_name.length());
					String tmp_user= buf_rdr.readLine().substring(Cls_Constants.h_exp_user.length());
					String tmp_date= buf_rdr.readLine().substring(Cls_Constants.h_exp_date.length());
					String tmp_notes =buf_rdr.readLine().substring(Cls_Constants.h_exp_notes.length());
					exp.setExperimentInfo(tmp_name, tmp_user, tmp_date, tmp_notes);
				}	
				else if (tmp_line.equals(Cls_Constants.h_grp)){ //load grp. info
					int tmp_id= Integer.parseInt(buf_rdr.readLine().substring(Cls_Constants.h_grp_id.length()));
					String tmp_name= buf_rdr.readLine().substring(Cls_Constants.h_grp_name.length());
					//int tmp_no_rats = 
					Integer.parseInt(buf_rdr.readLine().substring(Cls_Constants.h_grp_no_rats.length()));
					String tmp_rats_numbers= buf_rdr.readLine().substring(Cls_Constants.h_grp_rats_numbers.length());
					String tmp_notes =buf_rdr.readLine().substring(Cls_Constants.h_grp_notes.length());
					grp_tmp= new Group(tmp_id,tmp_name,tmp_rats_numbers,tmp_notes);
					exp.addGroup(grp_tmp);
				}
				else if (tmp_line.equals(Cls_Constants.h_rat)){
					tmp_line=buf_rdr.readLine();
					line_data=readLineData(tmp_line);
					exp.setMeasurementsList(line_data);
					while(buf_rdr.ready())
					{
						if(!(tmp_line=buf_rdr.readLine()).substring(0, 1).equals("["))
						{
							line_data=readLineData(tmp_line);
							for(int i=0;i<line_data.length;i++)
								line_data[i]=line_data[i].trim();
							Rat tmp_rat=new Rat(exp.getMeasurementsList(),line_data);
							grp_tmp.addRat(tmp_rat);
						}
						else
							break;
					}
					group_rats_are_loaded=true;
				}
			}
			fis.close();
			bis.close();

			pm.frm_exp.fillForm(exp);
			If_Grp2GUI[] arr_grps = new If_Grp2GUI[exp.getNo_groups()];
			exp.getGroups().toArray(arr_grps);
			pm.frm_grps.loadDataToForm(arr_grps);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String[] readLineData(String line)
	{
		String[] res= line.split("\t");
		return res;
	}

	/**
	 * Saves the Experiment's information to a Text file.
	 * @param filename file path to save the experiment information to
	 * @param exp the Experiment to save its information
	 */
	public void writeExpInfoToTXTFile(String filename,Experiment exp)
	{
		try {
			out = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		p = new PrintStream(out);
		try {
			p.print(exp.expInfo2String());	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		p.close();

	}

}
