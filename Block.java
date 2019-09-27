/*
 * Date: April 4 2019
 * A child of Line class
 * A template for blocks that know their header, type, and closing line
 * Are translated differently than regular Line objects
 */

public class Block extends Line {
	private static String[] integerTypes = { "int", "long", "short", "byte" };

	private String type;
	private String closer;

	/*
	 * Constructs a new Block object given type and code strings
	 */
	public Block(String type, String code) {
		super(code);
		this.type = type;

		// Determine the closing line of the block based off of it's type
		if (type.contains("else") || type.contains("if")) {
			closer = "\nend if";
		} else if (type.contains("while") || type.contains("for")) {
			closer = "\nend loop";
		} else {
			closer = "\n";
		}
	}

	/*
	 * Returns true (for all blocks)
	 */
	public boolean isBlock() {
		return true;
	}

	/*
	 * Returns true for else and else if blocks
	 */
	public boolean isElse() {
		return type.startsWith("else");
	}

	/*
	 * Returns the closing string of the block
	 */
	public String getCloser() {
		return closer;
	}

	/*
	 * Returns the translated header of a block
	 */
	public String translate() {
		String output = "";
		if (type.contains("if")) {
			output = type + new Line(code).translate() + " then";
		} else if (type.contains("else")) {
			output = type + new Line(code).translate();
		} else if (type.contains("while")) {
			output = "loop while " + new Line(code).translate();
		} else if (type.contains("for")) {
			if (getVar() == null || getLow() == null || getHigh() == null) {
				return null;
			}
			output = "loop " + getVar() + " from " + getLow() + " to " + getHigh();
		}
		return output;

	}

	/*
	 * Returns the counter variable of a for-loop
	 */
	public String getVar() {
		String type = "";

		// Error if no parameters
		if (code.equals("")) {
			Error.warnError("Looks like your for-loop had no parameters");
			return null;
		}
		code = code.trim();
		int cPlaceMax = Math.min(code.length(), 5);

		// Find the datatype of the counter
		for (int cPlace = 0; cPlace < cPlaceMax; cPlace++) {
			for (int kPlace = 0; kPlace < integerTypes.length; kPlace++) {
				if (code.substring(0, cPlace).equals(integerTypes[kPlace])) {
					type = integerTypes[kPlace];
				}
			}
		}

		// Error if data type is not long, int, short or byte
		if (type == "") {
			Error.warnError("Looks like a for-loop had invalid parameters\n"
					+ "This program can only translate for-loops with integer counters that increment by 1");
			return null;
		}
		return new Line(code.substring(type.length(), code.indexOf("=")).trim().toUpperCase()).translate();

	}

	/*
	 * Returns the lower-bound of the for-loop
	 */
	public String getLow() {
		if (code.indexOf("=") == -1 || code.indexOf(";") == -1 || code.indexOf(";") < code.indexOf("=")) {
			Error.warnError("Looks like a for loop had invalid parameters\n"
					+ "This program can only translate for-loops with integer counters that increment by 1");
			return null;
		}
		return new Line(code.substring(code.indexOf("=") + 1, code.indexOf(";"))).translate();
	}

	/*
	 * Returns the upper-bound of the for-loop Upper-bound is inclusive for
	 * pseudocode Method accounts for the difference between < and <= operators
	 */
	public String getHigh() {
		int valueStart = 0;
		boolean inclusive = false;
		if (code.contains("<=")) {
			valueStart = code.indexOf("<=") + 1;
			inclusive = true;
		} else if (code.contains("<")) {
			valueStart = code.indexOf("<");
		} else {
			Error.warnError("Looks like a for loop had invalid parameters\n"
					+ "This program can only translate for-loops with integer counters that increment by 1");
			return null;
		}

		if (code.substring(code.indexOf(";") + 1).indexOf(";") == -1) {
			Error.warnError("Looks like a for loop had invalid parameters\n"
					+ "This program can only translate for-loops with integer counters that increment by 1");
			return null;
		}
		if (inclusive) {
			return new Line(code.substring(valueStart + 1,
					code.substring(code.indexOf(";") + 1).indexOf(";") + code.indexOf(";") + 1)).translate();
		} else {
			return new Line(code.substring(valueStart + 1,
					code.substring(code.indexOf(";") + 1).indexOf(";") + code.indexOf(";") + 1) + " - 1").translate();
		}
	}
}