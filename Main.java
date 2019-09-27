/*
 * Date: April 4 2019
 * Responsible for starting the program by initiating the first gui fram
 */

public class Main {
	private static GUI gui;

	public static void main(String args[]) {
		gui = new GUI();
		gui.setSize(880, 300);
		gui.setVisible(true);
	}
}
