
/*
 * Date: April 4 2019
 * A template for for Nodes in a tree that connect all the Line objects
 * Nodes know a Line, the following line within them (if they are a Block),
 * and the following line after them
 */
import java.util.*;

public class Node {
	// The root is always a {} block holding everything
	private static Node root = new Node();
	private Node nextIn;
	private Node nextOut;
	private Line line;

	/*
	 * Constructs a new Node with the given line, nextIn and nextOut parameters as
	 * fields
	 */
	public Node(Line line, Node nextIn, Node nextOut) {
		this.nextIn = nextIn;
		this.nextOut = nextOut;
		this.line = line;
	}

	/*
	 * Constructs a Node linked only to null
	 */
	public Node() {
		nextIn = null;
		nextOut = null;
		line = null;
	}

	/*
	 * Sets the given Node as the inner element of the root
	 */
	public static void setRoot(Node newRoot) {
		root = newRoot;
	}

	/*
	 * Returns the root Node
	 */
	public static Node getRoot() {
		return root;
	}

	/*
	 * Returns a string translation that contains all the pseudocode in order
	 */
	public static String translateAll(Node localRoot) {
		String output = "";
		ArrayList<Node> ins = new ArrayList<Node>();

		// Error if trying to translate a null Node
		if (localRoot == null) {
			Error.warnError("An unexpected error occurred. Please make sure your input has correct Java syntax");
			return null;
		}

		// Translate line if no inner or outer nodes
		else if (localRoot.nextIn == null && localRoot.nextOut == null) {
			output = localRoot.line.translate();
		}

		/*
		 * Create an ArrayList of all the inner nodes of the given node Add their
		 * translations to the output, preceded by a new line and an indent
		 */
		else {
			output = output + localRoot.line.translate();

			Node currentIn = localRoot.nextIn;
			if (currentIn != null && currentIn.line != null && currentIn.line.translate() != null
					&& !(currentIn.line.translate()).equals("")) {
				ins.add(currentIn);
				while (currentIn.nextOut != null) {
					currentIn = currentIn.nextOut;
					ins.add(currentIn);
				}
				for (int inCount = 0; inCount < ins.size(); inCount = inCount + 1) {
					if (!translateAll(ins.get(inCount)).equals("")) {
						output = output + "\n" + "\t" + indent(translateAll(ins.get(inCount)));
					}
				}
			}
		}

		// Add back all quotes removed during the organization of the code
		if (root.equals(localRoot)) {
			output = addQuotes(output);
		}

		// If it's a block that needs to be closed, close it
		if (localRoot.line.isBlock() && (localRoot.nextOut == null || !localRoot.nextOut.line.isElse())) {
			output = (output) + (localRoot.line.getCloser());
		}

		return (output);
	}

	/*
	 * Inserts all the quotes removed and saved in the organization process back to
	 * the pseudocode Returns the code with the quotes
	 */
	public static String addQuotes(String input) {
		if (input.indexOf("\'\'") > -1
				&& (input.indexOf("\"\"") > input.indexOf("\'\'") || input.indexOf("\"\"") == -1)) {
			return input.substring(0, input.indexOf("\'\'")) + Organizer.getQuotes().get(Organizer.addQCount())
					+ addQuotes(input.substring(input.indexOf("\'\'") + 2));
		} else if (input.indexOf("\"\"") > -1
				&& (input.indexOf("\'\'") > input.indexOf("\"\"") || input.indexOf("\'\'") == -1)) {
			return input.substring(0, input.indexOf("\"\"")) + Organizer.getQuotes().get(Organizer.addQCount())
					+ addQuotes(input.substring(input.indexOf("\"\"") + 2));
		} else {
			return input;
		}
	}

	/*
	 * Returns a string of the code where every line is indented
	 */
	public static String indent(String line) {
		int lineBreak = line.indexOf("\n");
		if (lineBreak < 0 && line != null) {
			return line;
		} else {
			return line.substring(0, lineBreak + 1) + "\t" + indent(line.substring(lineBreak + 1));
		}
	}

}