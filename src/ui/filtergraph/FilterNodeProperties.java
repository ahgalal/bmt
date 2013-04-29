/**
 * 
 */
package ui.filtergraph;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ui.filtergraph.FilterGraph.Filter;
import filters.FiltersNamesRequirements.FilterTrigger;

/**
 * @author Creative
 */
public class FilterNodeProperties {
	private static FilterNodeProperties	self;

	public static FilterNodeProperties getDefault() {
		if (self == null)
			self = new FilterNodeProperties();
		return self;
	}

	private final Combo	combo;

	private final List	list;
	private final Shell	shell;
	private final Text	text;

	public FilterNodeProperties() {
		shell = new Shell(SWT.CLOSE | SWT.APPLICATION_MODAL);
		shell.setSize(284, 309);
		shell.addListener(SWT.Close, new Listener() {

			@Override
			public void handleEvent(final Event arg0) {
				hide();
				arg0.doit = false;
			}
		});
		shell.setLayout(new FormLayout());

		final Group grpFilterProperties = new Group(shell, SWT.NONE);
		final FormData fd_grpFilterProperties = new FormData();
		fd_grpFilterProperties.right = new FormAttachment(0, 274);
		fd_grpFilterProperties.top = new FormAttachment(0, 5);
		fd_grpFilterProperties.left = new FormAttachment(0, 5);
		grpFilterProperties.setLayoutData(fd_grpFilterProperties);
		grpFilterProperties.setLayout(new GridLayout(2, false));
		grpFilterProperties.setText("Filter Properties");

		final Label lblName = new Label(grpFilterProperties, SWT.NONE);
		lblName.setText("Name");

		text = new Text(grpFilterProperties, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		final Label lblId = new Label(grpFilterProperties, SWT.NONE);
		lblId.setText("ID");

		list = new List(grpFilterProperties, SWT.BORDER | SWT.V_SCROLL);
		final GridData gd_list = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_list.heightHint = 152;
		list.setLayoutData(gd_list);

		final Label lblTrigger = new Label(grpFilterProperties, SWT.NONE);
		lblTrigger.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblTrigger.setText("Trigger");

		combo = new Combo(grpFilterProperties, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		final Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				hide();
			}
		});
		final FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(0, 180);
		fd_btnCancel.top = new FormAttachment(0, 249);
		fd_btnCancel.left = new FormAttachment(0, 92);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");

		final Button btnOk = new Button(shell, SWT.NONE);
		final FormData fd_btnOk = new FormData();
		fd_btnOk.right = new FormAttachment(0, 274);
		fd_btnOk.top = new FormAttachment(0, 248);
		fd_btnOk.left = new FormAttachment(0, 186);
		btnOk.setLayoutData(fd_btnOk);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				String id = null;
				String name = null;
				String trigger = null;

				try {
					id = list.getSelection()[0];
					name = text.getText();
					trigger = combo.getText();
				} catch (final Exception e1) {
					// we will detect invalid values using the validateTextField
					// method
				}
				if (validateTextField("Filter ID", id)
						&& validateTextField("Filter Name", name)
						&& validateTextField("Trigger", trigger)) {

					final FilterTrigger filterTrigger = FilterTrigger
							.valueOf(trigger);
					final Filter filter = new Filter(name, id, filterTrigger);
					if (FilterGraph.getDefault().addUpdateNode(filter)) {
						// hide dialog
						hide();
					} else {
						final MessageBox box = new MessageBox(shell, SWT.ERROR);
						box.setMessage("Filter with the name \""
								+ name
								+ "\" already exists, please select another name");
						box.setText("Error");
						box.open();
					}
				}
			}

		});
		btnOk.setText("OK");
	}

	private void hide() {
		shell.setVisible(false);
	}

	public void loadFilter(final Filter filter) {
		text.setText(filter.getName());
		list.setSelection(new String[] { filter.getId() });
		combo.setText(filter.getTrigger().toString());
	}

	public void setFilterIDs(final String[] ids) {
		list.removeAll();
		for (final String id : ids) {
			list.add(id);
		}
	}

	public void show() {
		shell.open();
		text.setText("");

		// refresh filter triggers
		combo.removeAll();
		for (final FilterTrigger trigger : FilterTrigger.values()) {
			combo.add(trigger.toString());
		}
		combo.setText(combo.getItem(0));
	}

	private boolean validateTextField(final String fieldName, final String str) {
		if ((str != null) && !str.equals(""))
			return true;
		else {
			final MessageBox box = new MessageBox(shell, SWT.ERROR);
			box.setMessage("Invalid value for: " + fieldName);
			box.setText("Error");
			box.open();
		}
		return false;
	}
}
