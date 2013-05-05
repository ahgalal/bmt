/**
 * 
 */
package help;

import java.awt.Dimension;
import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;


/**
 * @author Creative
 */
public class HelpManager {

	private static HelpManager	self;
	JButton button;
	public static HelpManager getDefault() {
		if (self == null)
			self = new HelpManager();
		return self;
	}
	private HelpSet hs;
	private HelpBroker hb;
	CSH.DisplayHelpFromSource displayHelpFromSource;
	private HelpManager() {
		button = new JButton();
		
		final String helpHS = "BMT.hs";
		final ClassLoader cl = HelpManager.class.getClassLoader();
		try {
			final URL hsURL = HelpSet.findHelpSet(cl, helpHS);
			hs = new HelpSet(null, hsURL);
			hb = hs.createHelpBroker();
			Rectangle bounds = Display.getDefault().getPrimaryMonitor().getBounds();
			int width = (int) (bounds.width*0.9);
			int height = (int) (bounds.height*0.8);
			hb.setSize(new Dimension(width, height));
			displayHelpFromSource = new CSH.DisplayHelpFromSource( hb );
			button.addActionListener(displayHelpFromSource);	
			LookAndFeel lookAndFeel = new WindowsLookAndFeel();
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (final Exception ee) {
			System.out.println("HelpSet " + ee.getMessage());
			System.out.println("HelpSet " + helpHS + " not found");
			return;
		}
		button.setVisible(false);
	}

	public void openHelp() {
		button.doClick();
	}
}
