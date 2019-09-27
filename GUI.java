
/*
 * Date: April 4 2019
 * Responsible for the presentation of most of the user interface (input portion)
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class GUI extends JFrame implements ActionListener {
	/*
	 * The frame is divided up into three main sections: top, middle and bottom Top
	 * has introduction on the left and two buttons on the right Middle will be the
	 * largest and will contain the input textArea (with scroll-bar) Bottom will
	 * contain the translate button on the right
	 */
	private JPanel top;
	private JPanel topRight;
	private JPanel mid;
	private JPanel bot;

	private JLabel intro;
	private JButton help;
	private JButton upload;
	private JButton go;

	private JTextArea inputArea;

	private JScrollPane scrollBar;

	private final JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

	public GUI() {
		top = new JPanel();
		topRight = new JPanel();
		mid = new JPanel();
		bot = new JPanel();

		intro = new JLabel("Upload a text file or enter your code below");

		help = new JButton("Help");
		upload = new JButton("Upload File");
		go = new JButton("Translate");

		help.addActionListener(this);

		upload.addActionListener(this);

		go.addActionListener(this);

		inputArea = new JTextArea(20, 20);
		inputArea.setText("Enter text here");
		inputArea.setBounds(0, 0, 40, 40);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		GridLayout topRightLayout = new GridLayout(1, 2);
		topRightLayout.setHgap(5);
		topRight.setLayout(topRightLayout);
		topRight.add(upload);
		topRight.add(help);

		GridLayout topLayout = new GridLayout(1, 3);
		topLayout.setHgap(5);
		top.setLayout(topLayout);

		top.add(intro);
		top.add(new JLabel(""));
		top.add(topRight);

		mid.setLayout(new GridLayout(1, 1));
		mid.add(inputArea);

		scrollBar = new JScrollPane(inputArea);
		mid.add(scrollBar);

		bot = new JPanel(new GridLayout(1, 3));

		bot.add(new JLabel(""));
		bot.add(new JLabel(""));

		bot.add(go);
		c.add(top, BorderLayout.NORTH);
		c.add(mid, BorderLayout.CENTER);
		c.add(bot, BorderLayout.SOUTH);

		setTitle("Java to Psuedocode Translator");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/*
	 * Performs an action based on the button pressed
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		// Allow user to choose a file to upload
		if (event.getSource() == upload) {
			int rValue = chooser.showOpenDialog(null);

			if (rValue == JFileChooser.APPROVE_OPTION) {
				File newFile = chooser.getSelectedFile();
				InputReader.clear();
				InputReader.readFile(newFile);
			} else {
				Error.warnError("An issue occurred during file selection");
			}
		}
		// Show the information pop-up
		else if (event.getSource() == help) {
			Help.inform();
		}
		// Translate the code from the text area
		else if (event.getSource() == go) {
			InputReader.clear();
			String javInput = inputArea.getText();
			InputReader.inputList(javInput);
			InputReader.readArea();
		}
	}

}