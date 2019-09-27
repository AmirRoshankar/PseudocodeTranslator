/*
 * Date: April 4 2019
 * A child class of Line (an output line)
 */

public class OutputLine extends Line {
	/*
	 * Constructs an OutputLine object with the given parameter as its code
	 */
	public OutputLine(String code) {
		super(code);
	}

	/*
	 * Returns the translated pseudocode for this OutputLine object
	 */
	public String translate() {
		String output = "Output (";
		output = output + new Line(code).translate() + ")";
		return output;
	}

}
