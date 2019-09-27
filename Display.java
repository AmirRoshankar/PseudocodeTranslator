
/*
 * Date: April 4 2019
 * Responsible for creating and displaying the translated pseudocode
 */

import java.awt.*;
import javax.swing.*;

public class Display extends JFrame {
	private JPanel top;
	private JPanel mid;

	private JLabel intro;

	private JTextArea inputArea;

	private JScrollPane scrollBar;

	/*
	 * Constructs a new Display object that contains the pseudocode given by the
	 * parameters
	 */
	public Display(String pseudocode) {
		top = new JPanel();
		mid = new JPanel();

		intro = new JLabel("Your Pseudocode:");

		inputArea = new JTextArea(20, 20);
		inputArea.setText(pseudocode);
		inputArea.setBounds(0, 0, 40, 40);
		inputArea.setEditable(false);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		GridLayout topLayout = new GridLayout(1, 3);
		top.setLayout(topLayout);

		top.add(intro);

		mid.setLayout(new GridLayout(1, 1));
		mid.add(inputArea);

		scrollBar = new JScrollPane(inputArea);
		mid.add(scrollBar);

		c.add(top, BorderLayout.NORTH);
		c.add(mid, BorderLayout.CENTER);

		setTitle("Java to Psuedocode Translator");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/*
	 * Calls on the Display constructor with the given pseudocode parameter Makes
	 * the new frame visible
	 */
	public static void showCode(String pseudocode) {
		if (pseudocode != null && !pseudocode.equals("")) {
			Display display = new Display(pseudocode);
			display.setSize(900, 700);
			display.setVisible(true);
		}
	}

}