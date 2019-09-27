
/*
 * Date: April 4 2019
 * Responsible for reading the raw Java code from a file or from the text area
 * Calls on other methods to translate and display the translated code
 */

import java.io.*;
import java.util.*;

public class InputReader {
	private static String temp;
	private static String output = "{";
	private static ArrayList<String> inputList = new ArrayList<String>();

	/*
	 * Clears all variables and objects that stored information about a round of
	 * translation
	 */
	public static void clear() {
		temp = "";
		output = "{";
		if (inputList != null)
			inputList.clear();
		Organizer.reset();
	}

	/*
	 * Initializes an ArrayList of strings that holds each line of the input
	 */
	public static void inputList(String input) {
		if (input == null || input.equals("")) {
			return;
		}
		if (input.indexOf("\n") != -1) {
			inputList.add(input.substring(0, input.indexOf("\n")));
			inputList(input.substring(input.indexOf("\n") + 1));
		} else {
			inputList.add(input);
		}

	}

	/*
	 * Takes the array list of input code Filters out single line comments and tabs
	 * Calls on other methods to translate and display the pseudocode
	 * 
	 */
	public static void readArea() {
		if (inputList.size() == 0) {
			Error.warnError("No code was detected");
		}

		for (int lineCount = 0; lineCount < inputList.size(); lineCount = lineCount + 1) {
			temp = inputList.get(lineCount);
			temp = temp.replaceAll("\t", "");
			if (temp != null && !temp.equals("") && BracketCheck.findRealString(temp, "//") >= 0) {
				temp = temp.substring(0, BracketCheck.findRealString(temp, "//"));
			}
			output = output + temp;
		}
		if (output.equals("{")) {
			Error.warnError("No code was detected");
		} else {
			output = output + "}";
			if (!BracketCheck.check(output)) {
				Error.warnError("Looks like your brackets and quotes don't match");
				output = null;
			} else {
				Organizer.setInAndQuotes(output);
				Node newN = Organizer.read(Organizer.getInput());
				Node.setRoot(newN);
				Display.showCode(Node.translateAll(Node.getRoot()));
			}
		}
	}

	/*
	 * Reads a file line by line Filters out single line comments and tabs Calls on
	 * other methods to translate and display the pseudocode
	 * 
	 */
	public static void readFile(File newFile) {
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(newFile.getPath()));
			temp = fileReader.readLine();
			while (temp != null) {
				temp = temp.replaceAll("\t", "");
				if (temp != null && !temp.equals("") && BracketCheck.findRealString(temp, "//") >= 0) {
					temp = temp.substring(0, BracketCheck.findRealString(temp, "//"));
				}
				output = output + temp;
				temp = fileReader.readLine();
			}
			fileReader.close();
		} catch (Exception exception) {
			Error.warnError("An error occured with reading the file");
			return;
		}
		if (output.equals("{")) {
			Error.warnError("The selected file is empty");
		} else {
			output = output + "}";
			System.out.println(output);
			if (!BracketCheck.check(output)) {
				Error.warnError("Looks like your brackets and quotes don't match");
				output = null;
			} else {
				Organizer.setInAndQuotes(output);
				Node newN = Organizer.read(Organizer.getInput());
				Display.showCode(Node.translateAll(newN));
			}
		}
	}
}