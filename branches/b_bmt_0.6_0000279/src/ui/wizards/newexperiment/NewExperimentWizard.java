package ui.wizards.newexperiment;

import modules.experiment.Exp2GUI;
import modules.experiment.Grp2GUI;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;

import control.ui.CtrlNewExperimentWizard;

public class NewExperimentWizard extends Wizard {
	private final CtrlNewExperimentWizard	controller;
	private final ExpBasicInfoPage			expBasicInfoPage;
	private final GrpsBasicInfoPage			grpBasicInfoPage;

	public NewExperimentWizard(final CtrlNewExperimentWizard controller) {
		this.controller = controller;
		setWindowTitle("Experiment information");
		expBasicInfoPage = new ExpBasicInfoPage("expInfo");
		expBasicInfoPage.setTitle("Experiment information");
		expBasicInfoPage
				.setDescription("Information describing the Experiment.");
		addPage(expBasicInfoPage);
		grpBasicInfoPage = new GrpsBasicInfoPage("grpInfo");
		grpBasicInfoPage.setTitle("Groups' information");
		grpBasicInfoPage
				.setDescription("Information describing rat groups in the experiment.");
		addPage(grpBasicInfoPage);

		final ImageDescriptor defaultPageImage = new ImageDescriptor() {
			@Override
			public ImageData getImageData() {
				ImageData img;
				try{
					img=new ImageData("images/wizards/sa_samplecube48.gif");
				}catch(Exception e){
					img=new ImageData(this.getClass().getClassLoader().getResourceAsStream("images/wizards/sa_samplecube48.gif"));
				}
				return img;
			}
		};
		setDefaultPageImageDescriptor(defaultPageImage);
	}

	public void loadInfoToGUI(final Exp2GUI expInfo, final Grp2GUI[] grpsInfo) {
		expBasicInfoPage.loadData(new String[] { expInfo.getName(),
				expInfo.getUser(), expInfo.getNotes(), expInfo.getDate(),expInfo.getType() });

		for (final Grp2GUI grp : grpsInfo) {
			grpBasicInfoPage.addNewTab(grp.getId(), grp.getName(),
					Integer.toString(grp.getNoRats()), grp.getRatsNumbering(),
					grp.getNotes());
		}
	}

	@Override
	public boolean performFinish() {

		// set experiment's info
		controller
				.setExpVars(new String[] { expBasicInfoPage.getExpName(),
						expBasicInfoPage.getUserName(),
						expBasicInfoPage.getExpDate(),
						expBasicInfoPage.getExpNotes(),
						expBasicInfoPage.getExpType() });

		// set groups' info
		controller.setGroups(grpBasicInfoPage.getGroups().toArray(
				new Grp2GUI[0]));

		// save experiment
		controller.saveAction((Composite) expBasicInfoPage.getControl());

		this.dispose();
		return true;
	}

}
