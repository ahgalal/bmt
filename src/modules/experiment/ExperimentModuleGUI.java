/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package modules.experiment;

import ui.PluggedGUI;

/**
 * GUI class for the Experiment Module.
 * 
 * @author Creative
 */
public abstract class ExperimentModuleGUI extends PluggedGUI<ExperimentModule> {
    public ExperimentModuleGUI(final ExperimentModule owner) {
	super(owner);
    }

    /**
     * Initializes/shows the GUI components.
     * 
     * @param menuBar
     *            menubar of MainGUI
     * @param shell
     *            parent shell (MainGUI's shell)
     */
    /*
     * public ExperimentModuleGUI( Shell shell) {}
     */

    /*
     * CoolItem coolItem = new CoolItem(coolBar, SWT.NONE);
     * 
     * Composite composite = new Composite(coolBar, SWT.NONE);
     * coolItem.setControl(composite); composite.setLayout(new
     * FillLayout(SWT.HORIZONTAL));
     * 
     * Button btnNewExp = new Button(composite, SWT.NONE);
     * btnNewExp.setText("New Experiment"); composite.setBounds(0, 0, 40, 32);
     * coolItem.setSize(composite.getSize().x, composite.getSize().y);
     */

}
