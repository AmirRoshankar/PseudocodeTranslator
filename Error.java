
/*
 * Date: April 4 2019
 * Responsible for displaying error messages
 */

import javax.swing.*;

public class Error {
	/*
	 * Displays a pop-up with the given errorMessage parameter
	 */
	public static void warnError(String errorMessage) {
		JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Uh Oh!", JOptionPane.ERROR_MESSAGE);
	}

}
