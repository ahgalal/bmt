/**
 * 
 */
package help;

import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

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
