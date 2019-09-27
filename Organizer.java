
/*
 * Date: April 4 2019
 * Responsible for splitting up and organizing the input code
 * Makes all Lines and Nodes and connects them 
 */

import java.util.*;

public class Organizer {
	// A 2-Dimensional array of possible line starters and their type
	private static String[][] lineStarter = { { "if", "param" }, { "else if", "param" }, { "else", "noParam" },
			{ "for", "param" }, { "while", "param" }, { "System.out.print", "noParam" }, { "{", "noParam" },
			{ "/*", "comment" } };

	private static String input;
	private static ArrayList<String> quotes;
	private static int qCount = 0;

	/*
	 * Returns the value of qCount
	 */
	public static int getQCount() {
		return qCount;
	}

	/*
	 * Increments qCount by 1 and returns it
	 */
	public static int addQCount() {
		return qCount++;
	}

	/*
	 * Initializes the input then searches for, removes and sequentially stores all
	 * quotes
	 */
	public static void setInAndQuotes(String source) {
		input = source;
		quotes = filterQuotesAndComments();
	}

	/*
	 * Returns the input code
	 */
	public static String getInput() {
		return input;
	}

	/*
	 * Returns the ArrayList of quotes
	 */
	public static ArrayList<String> getQuotes() {
		return quotes;
	}

	/*
	 * Reads the given code and returns a tree of Nodes that know their Line objects
	 * and their inner and outer Nodes
	 */
	public static Node read(String code) {
		if (code == null)
			return null;
		String[] type = identify(code.trim());
		Line newLine;
		Node newNode = null;
		String param = "";
		String body = "";
		int endParam = 0;
		int endBody = 0;
		int startBody = 0;

		// If the first element needs parameters
		if (type[1].equals("param")) {
			if (code.substring(code.indexOf(type[0]) + type[0].length()).trim().startsWith("(")) {
				// Find the parameters
				endParam = BracketCheck.closePlace(code.substring(code.indexOf(type[0]) + type[0].length()))
						+ type[0].length();
				param = code.substring(code.indexOf("(") + 1, endParam);
				if (code.substring(endParam + 1).trim().startsWith("{")) {
					startBody = code.substring(endParam + 1).indexOf("{") + endParam + 1;
					endBody = BracketCheck.closePlace(code.substring(endParam + 1).trim()) + startBody;
					body = code.substring(startBody + 1, endBody);
				} else if (code.substring(endParam + 1).indexOf(";") == 0) {
					body = "";
					endBody = endParam + 1;
				} else if (code.substring(endParam + 1).indexOf(";") > 0) {
					startBody = endParam;
					endBody = code.substring(endParam + 1).indexOf(";") + endParam + 1;
					body = code.substring(startBody + 1, endBody);
				}
			}

			newLine = new Block(type[0] + " ", param);
			if (endBody < code.length() - 1) {
				newNode = new Node(newLine, read(body), read(code.substring(endBody + 1)));
			} else {
				newNode = new Node(newLine, read(body), null);
			}
			return newNode;
		}

		// If the first parameter doesn't need parameters
		else if (type[1].equals("noParam")) {
			// If it's a plain {} block
			if (type[0].equals("{")) {
				newLine = new Block(type[0] + " ", "");
				startBody = code.indexOf("{");
				endBody = BracketCheck.closePlace(code.substring(code.indexOf("{")));

				body = code.substring(startBody + 1, endBody);
				if (endBody < code.length() - 1) {
					newNode = new Node(newLine, read(body), read(code.substring(endBody + 1)));
				} else {
					newNode = new Node(newLine, read(body), null);
				}
				return newNode;
			}

			// If it's an else block
			else if (type[0].equals("else")) {
				if (code.substring(type[0].length()).trim().startsWith("{")) {
					startBody = type[0].length();
					endBody = BracketCheck.closePlace(code.substring(startBody).trim()) + startBody;
					body = code.substring(startBody + 1, endBody);
				} else if (code.substring(type[0].length()).indexOf(";") == 0) {
					body = "";
					endBody = type[0].length() + 1;
				} else if (code.substring(type[0].length()).indexOf(";") > 0) {
					startBody = type[0].length();
					endBody = code.indexOf(";");
					body = code.substring(startBody, endBody);
				}
				newLine = new Block(type[0] + " ", "");
				if (endBody < code.length() - 1) {
					newNode = new Node(newLine, read(body), read(code.substring(endBody + 1)));
				} else {
					newNode = new Node(newLine, read(body), null);
				}
				return newNode;
			}

			// If it's an output line
			else if (type[0].startsWith("System.out.print") && code.contains("(") && code.contains(")")
					&& BracketCheck.closePlace(code.substring(code.indexOf("("))) >= 0) {
				param = code.substring(code.indexOf("(") + 1,
						BracketCheck.closePlace(code.substring(code.indexOf("("))) + code.indexOf("("));
				newLine = new OutputLine(param);
				endParam = BracketCheck.findRealString(code, ";");
				if (endParam < code.length() - 1 && endParam >= 0) {
					newNode = new Node(newLine, null, read(code.substring(endParam + 1)));
				} else if (endParam == code.length() - 1) {
					newNode = new Node(newLine, null, null);
				} else {
					Error.warnError("Looks like you're missing a semicolon in an output line");
					return null;
				}
			}

			// If it's a regular Line
			else {
				endParam = BracketCheck.findRealString(code, ";");
				if (endParam > -1) {
					param = code.substring(0, endParam + 1);
				} else {
					param = code;
				}

				newLine = new Line(param);
				if (endParam < code.length() - 1 && endParam >= 0) {
					newNode = new Node(newLine, null, read(code.substring(endParam + 1)));
				} else {
					newNode = new Node(newLine, null, null);
				}
			}
			return newNode;
		}

		// Ignore multi-line comments
		else if (type[1].equals("comment")) {
			endBody = BracketCheck.closePlace(code) + 2;
			if (endBody < code.length()) {
				return read(code.substring(endBody));
			}
		}
		return null;
	}

	/*
	 * Returns the array of two strings that identify the type of the first element
	 * of the parameter code
	 */
	public static String[] identify(String code) {
		String[] output = { "", "noParam" };
		if (code == null) {
			return output;
		}
		int cPlaceMax = Math.min(code.length(), 17);
		for (int cPlace = 0; cPlace < cPlaceMax; cPlace++) {
			for (int kPlace = 0; kPlace < lineStarter.length; kPlace++) {
				if (code.substring(0, cPlace).equals(lineStarter[kPlace][0])) {
					if (cPlace < code.length() - 3 && code.substring(0, cPlace + 3).equals("else if")) {
						output[0] = "else if";
						output[1] = "param";
						return output;
					}
					output = lineStarter[kPlace];
				}
			}
		}
		return output;

	}

	/*
	 * Filters out all quotes and unnecessary spaces from the code Stores quotes
	 * sequentially in an ArrayList
	 */
	public static ArrayList<String> filterQuotesAndComments() {

		ArrayList<String> output = new ArrayList<String>();
		for (int charCount = 0; charCount < input.length(); charCount = charCount + 1) {
			if (input.charAt(charCount) == '\"' || input.charAt(charCount) == '\'') {
				output.add(input.substring(charCount,
						1 + charCount + BracketCheck.closePlace(input.substring(charCount))));
				input = input.substring(0, charCount + 1)
						+ input.substring(charCount + BracketCheck.closePlace(input.substring(charCount)));
				charCount = charCount + 1;
			}
		}

		input = input + " ";
		String[] spaceSplit = input.split(" ");
		input = "";
		for (int wordCount = 0; wordCount < spaceSplit.length; wordCount = wordCount + 1) {
			if (endsInKey(spaceSplit[wordCount]) && !endsInLineStarter(spaceSplit[wordCount])) {
				input = input + spaceSplit[wordCount] + " ";
			}

			else if (Line.isKey(spaceSplit[wordCount]) && !isLineStarter(spaceSplit[wordCount])) {
				input = input + " " + spaceSplit[wordCount] + " ";
			} else if (wordCount > 0 && input.length() > 0 && input.charAt(input.length() - 1) != ' '
					&& spaceSplit[wordCount - 1].endsWith("else") && spaceSplit[wordCount].startsWith("if")) {
				input = input + " " + spaceSplit[wordCount];
			} else if (wordCount > 0 && !spaceSplit[wordCount].equals("") && (endsInClass(spaceSplit[wordCount - 1])
					|| (isOnlyLetters(spaceSplit[wordCount]) && Line.isClassName(spaceSplit[wordCount - 1])))) {
				input = input + " " + spaceSplit[wordCount];
			} else if (!spaceSplit[wordCount].equals("")) {
				input = input + spaceSplit[wordCount];
			}
		}
		return output;
	}

	/*
	 * Returns true if the parameter is only letters
	 */
	public static boolean isOnlyLetters(String word) {
		for (int charCount = 0; charCount < word.length(); charCount = charCount + 1) {
			if (!(word.charAt(charCount) >= 'a' && word.charAt(charCount) <= 'z'
					|| word.charAt(charCount) >= 'A' && word.charAt(charCount) <= 'Z'
					|| word.charAt(charCount) >= '0' && word.charAt(charCount) <= '9')) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Resets the quotes and input fields
	 */
	public static void reset() {
		if (quotes != null)
			quotes.clear();
		input = "";
		qCount = 0;
	}

	/*
	 * Returns true if the last word of the parameter is a potential class name
	 * (starts with capital letter)
	 */
	public static boolean endsInClass(String word) {
		int endLine = Math.max(Math.max(word.lastIndexOf(";"), word.lastIndexOf("}")), word.lastIndexOf("{"));
		return Line.isClassName(word.substring(endLine + 1));
	}

	/*
	 * Returns true if the last word of the parameter is a Java keyword
	 */
	public static boolean endsInKey(String word) {
		String temp = "";
		for (int charCount = word.length() - 1; charCount >= 0; charCount = charCount - 1) {
			temp = word.charAt(charCount) + temp;
			if (Line.isKey(temp)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Returns true if the last word of the parameter is a line starter
	 */
	public static boolean endsInLineStarter(String word) {
		String temp = "";
		for (int charCount = word.length() - 1; charCount >= 0; charCount = charCount - 1) {
			temp = word.charAt(charCount) + temp;
			if (isLineStarter(temp)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Returns true if the parameter is a line starter
	 */
	public static boolean isLineStarter(String word) {
		for (int count = 0; count < lineStarter.length; count = count + 1) {
			if (word.equals(lineStarter[count][0])) {
				return true;
			}
		}
		return false;
	}

}