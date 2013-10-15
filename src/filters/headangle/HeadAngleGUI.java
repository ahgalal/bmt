/**
 * 
 */
package filters.headangle;

import java.awt.Cursor;
import java.awt.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import ui.PluggedGUI;
import utils.PManager;
import utils.PManager.ProgramState;
import utils.video.ImageManipulator.RGB;

/**
 * @author Creative
 */
public class HeadAngleGUI extends PluggedGUI<HeadAngleFilter> {

	private Composite	cmpstHeadAngle;
	private ExpandItem	xpndtmHeadAngle;

	public HeadAngleGUI(final HeadAngleFilter owner) {
		super(owner);
	}

	@Override
	public void deInitialize() {
		disposeWidget(cmpstHeadAngle);
		disposeWidget(xpndtmHeadAngle);
	}

	@Override
	public void initialize(final Shell shell, final ExpandBar expandBar,
			final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
		xpndtmHeadAngle = new ExpandItem(expandBar, SWT.NONE);
		xpndtmHeadAngle.setExpanded(true);
		xpndtmHeadAngle.setText("Trace points");
		cmpstHeadAngle = new Composite(expandBar, SWT.NONE);
		xpndtmHeadAngle.setControl(cmpstHeadAngle);

		cmpstHeadAngle.setLayout(new GridLayout(2, false));

		final Label lblEar = new Label(cmpstHeadAngle, SWT.NONE);
		lblEar.setText("Ear1");

		final Composite cmpstEar1 = new Composite(cmpstHeadAngle, SWT.BORDER);
		final GridData gd_cmpstEar1 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_cmpstEar1.heightHint = 25;
		cmpstEar1.setLayoutData(gd_cmpstEar1);
		cmpstEar1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(final MouseEvent e) {
				// change cursor to color picker
				PManager.mainGUI.setMainScreenCursor(Cursor.CROSSHAIR_CURSOR);

				final java.awt.event.MouseAdapter listener = new java.awt.event.MouseAdapter() {
					@Override
					public void mouseClicked(final java.awt.event.MouseEvent e) {
						PManager.mainGUI.removeMouseListenerOnMainFrame(this);

						final Point point = e.getPoint();
						final RGB pixel = PManager.mainGUI
								.getMainScreenPixelRGB(point);
						Display.getDefault().syncExec(new Runnable() {

							@Override
							public void run() {
								cmpstEar1.setBackground(new Color(Display
										.getDefault(), pixel.getR(), pixel
										.getG(), pixel.getB()));
								PManager.mainGUI.setMainScreenCursor(Cursor
										.getDefaultCursor().getType());
							}
						});
					}
				};
				PManager.mainGUI.addMouseListenerOnMainFrame(listener);
			}
		});

		final Label lblEar_1 = new Label(cmpstHeadAngle, SWT.NONE);
		lblEar_1.setText("Ear2");

		final Composite cmpstEar2 = new Composite(cmpstHeadAngle, SWT.BORDER);
		final GridData gd_cmpstEar2 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_cmpstEar2.heightHint = 25;
		cmpstEar2.setLayoutData(gd_cmpstEar2);

		final Label lblBody = new Label(cmpstHeadAngle, SWT.NONE);
		lblBody.setText("Body1");

		final Composite cmpstBody1 = new Composite(cmpstHeadAngle, SWT.BORDER);
		final GridData gd_cmpstBody1 = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_cmpstBody1.heightHint = 25;
		cmpstBody1.setLayoutData(gd_cmpstBody1);

		final Label lblBody_1 = new Label(cmpstHeadAngle, SWT.NONE);
		lblBody_1.setText("Body2");

		final Composite cmpstBody2 = new Composite(cmpstHeadAngle, SWT.BORDER);
		final GridData gd_cmpstBody2 = new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1);
		gd_cmpstBody2.heightHint = 25;
		cmpstBody2.setLayoutData(gd_cmpstBody2);

		xpndtmHeadAngle.setHeight(xpndtmHeadAngle.getControl().computeSize(
				SWT.DEFAULT, SWT.DEFAULT).y + 10);
	}

	@Override
	public void stateGeneralChangeHandler(final ProgramState state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateStreamChangeHandler(final ProgramState state) {
		// TODO Auto-generated method stub

	}

}
