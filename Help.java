
/*
 * Date: April 4 2019
 * Responsible for informing the user about the program
 */
import javax.swing.*;

public class Help {
	/*
	 * Displays a pop-up with information about the program
	 */
	public static void inform() {
		String message = "Welcome to the Java to Pseudocode translator!\n"
				+ "Please select a file containing Java code, or input code directly and press \"Translate\".\n"
				+ "Make sure that your input is free of any syntax errors, otherwise, the pseudocode may not generate properly.\n"
				+ "Enjoy!";
		JOptionPane.showMessageDialog(new JFrame(), message, "Help", JOptionPane.INFORMATION_MESSAGE);
	}

}
