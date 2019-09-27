/*
 * Date: April 4 2019
 * A template for Line objects that know some raw source code 
 * and how to translate it
 */

public class Line {
	// A 2-Dimensional array of non-letter symbols and their translations
	private static String[][] nonLetter = { { ".", "." }, { ";", "" }, { ",", ", " }, { "(", " (" }, { ")", ") " },
			{ "<=", " <= " }, { ">=", " >= " }, { "&&", " AND " }, { "==", " = " }, { "||", " OR " },
			{ "!=", " NOT= " }, { ">", " > " }, { "<", " < " }, { "=", " = " }, { "!", "NOT" }, { "%", " mod " },
			{ "/", " div " }, { "+", " + " }, { "-", " - " }, { "*", " * " } };

	private static String[] keyword = { "abstract", "continue", "for", "new", "switch", "assert", "default", "goto",
			"package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements",
			"protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return",
			"transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void",
			"class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while", "true",
			"false", "null" };

	protected String code;

	/*
	 * Constructs a new Line object a given string of code
	 */
	public Line(String code) {
		this.code = code;
	}

	/*
	 * Returns the raw code
	 */
	public String getCode() {
		return code;
	}

	/*
	 * Returns whether this is a Block object (child of Line)
	 */
	public boolean isBlock() {
		return false;
	}

	/*
	 * Returns a string translation of the raw code
	 */
	public String translate() {
		String output = "";
		String tempWord = "";

		for (int charCount = 0; charCount < code.length(); charCount = charCount + 1) {
			/*
			 * If the character is a symbol, concatenate tempWord with output Concatenate
			 * the symbol with output
			 */
			if (charCount < code.length() - 1 && code.charAt(charCount) == '(') {
				output = output + tempWord;
				tempWord = "";
				output = output + '(';
			} else if (charCount < code.length() - 1 && isOpp(code.substring(charCount, charCount + 2))) {
				output = addTempWord(output, tempWord);
				tempWord = "";
				output = output + getNewOpp(code.substring(charCount, charCount + 2));
				charCount = charCount + 1;
			} else if (isOpp("" + code.charAt(charCount))) {
				output = addTempWord(output, tempWord);
				tempWord = "";
				output = output + getNewOpp("" + code.charAt(charCount));
			} else if (code.charAt(charCount) == ' ') {
				output = addTempWord(output, tempWord);
				tempWord = "";
				output = output + " ";
			}
			// If not a special symbol, concatenate with tempWord
			else {
				tempWord = tempWord + code.charAt(charCount);
			}
		}
		// Concatenate output and tempWord at the end
		if (isKey(tempWord)) {
			output = output + tempWord;
		} else {
			output = output + tempWord.toUpperCase();
		}
		return output;
	}

	/*
	 * Returns true if the given string is a class name (starts with capital letter)
	 */
	public static boolean isClassName(String word) {
		return (word != null && word.length() > 0 && word.charAt(0) >= 'A' && word.charAt(0) <= 'Z');
	}

	/*
	 * Returns true if the given string is a Java keyword
	 */
	public static boolean isKey(String word) {
		for (int count = 0; count < keyword.length; count = count + 1) {
			if (word.equals(keyword[count])) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Returns true if the given string is a non-letter symbol
	 */
	public static boolean isOpp(String word) {
		for (int count = 0; count < nonLetter.length; count = count + 1) {
			if (word.equals(nonLetter[count][0])) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Returns the translated version of the given non-letter Returns an empty
	 * string if the parameter is not a non-letter
	 */
	public static String getNewOpp(String word) {
		for (int count = 0; count < nonLetter.length; count = count + 1) {
			if (word.equals(nonLetter[count][0])) {
				return nonLetter[count][1];
			}
		}
		return "";
	}

	/*
	 * Returns false since else is a block, not a line
	 */
	public boolean isElse() {
		return false;
	}

	/*
	 * Returns an empty string since lines don't have closing lines
	 */
	public String getCloser() {
		return "";
	}

	/*
	 * Concatenates string tempWord onto string output and changes its
	 * capitalization based on what kind of word or symbol it is
	 */
	public static String addTempWord(String output, String tempWord) {
		if (tempWord.equals("\"")) {
			return output;
		}
		if (isKey(tempWord) || !output.isEmpty() && output.charAt(output.length() - 1) == '.'
				|| isClassName(tempWord)) {
			output = output + tempWord;
		} else {
			output = output + tempWord.toUpperCase();
		}
		return output;
	}

}