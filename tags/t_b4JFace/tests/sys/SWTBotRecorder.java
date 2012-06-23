package sys;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import utils.KeyboardUtils;

public class SWTBotRecorder {
	private class SWTListener implements Listener {
		private final String	botName			= "bot.";
		private Text			currentText;
		private String			oldTextString	= "";

		@Override
		public void handleEvent(final Event event) {
			String widgetType = event.widget.getClass().toString();
			widgetType = widgetType.substring(widgetType.lastIndexOf(".") + 1)
					.toLowerCase();
			String widgetText = "";
			if (!(event.widget instanceof Text))
				if (currentText != null) {
					generatedCode += botName + "text(\"" + oldTextString
							+ "\").setText(\"" + currentText.getText()
							+ "\");\n";
					currentText = null;
					oldTextString = "";
				}

			if ((event.widget instanceof Button)
					&& (event.type == SWT.MouseDown)) {
				final Button btn = (Button) event.widget;
				widgetText = btn.getText();
				generatedCode += botName + widgetType + "(\"" + widgetText
						+ "\").click();\n" + "index of widget: " + event.index;
			} else if ((event.widget instanceof MenuItem)
					&& (event.type == SWT.Selection)) {
				final MenuItem mnutm = (MenuItem) event.widget;
				final Menu mnuParent = mnutm.getParent();

				generatedCode += botName + "menu(\""
						+ mnuParent.getParentItem().getText() + "\").menu(\""
						+ mnutm.getText() + "\").click();\n";
			} else if ((event.widget instanceof Text)
					&& (event.type == SWT.KeyDown)) {
				final Text txt = (Text) event.widget;
				if (currentText == null) {
					currentText = txt;
					oldTextString = currentText.getText();
				}
				if (currentText != txt) {
					generatedCode += botName + "text(\"" + oldTextString
							+ "\").setText(\"" + currentText.getText()
							+ "\");\n";
					currentText = txt;
					oldTextString = currentText.getText();
				}
			} else if (event.type == SWT.FocusIn)
				System.out.println("focus" + event.keyCode);
			System.out.println(generatedCode);

			/*
			 * new Thread(new Runnable() {
			 * @Override public void run() { while (true) {
			 * Display.getDefault().asyncExec(new Runnable() {
			 * @Override public void run() {
			 * System.out.println(Display.getDefault()); } }); try {
			 * Thread.sleep(1000); } catch (final InterruptedException e) {
			 * e.printStackTrace(); } } } }).start();
			 */
		}
	}

	private String	generatedCode	= "";

	public SWTBotRecorder() {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				final SWTListener listener = new SWTListener();
				Display.getDefault().addFilter(SWT.MouseDown, listener);
				Display.getDefault().addFilter(SWT.Selection, listener);
				Display.getDefault().addFilter(SWT.KeyDown, listener);
				Display.getDefault().addFilter(SWT.Activate, listener);
				Display.getDefault().addFilter(SWT.Close, listener);

			}
		});
	}

	public void handleSaveDialog(final String path) {
		KeyboardUtils.typeText(path, true);
	}

}
