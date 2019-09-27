
/*
 * Date: April 4 2019
 * This class is used for all checks and processes involving 
 * awareness of brackets, quotes and multiline comments 
 */

import java.util.*;

public class BracketCheck {
	/*
	 * Check if all brackets, quotes and multiline comments are closed Returns true
	 * if there are no errors
	 */
	public static boolean check(String input) {
		// Add brackets - "(){}[]" - in order to the stack
		Stack<Character> bStack = new Stack<Character>();
		int type = -1;

		// Iterate through input characters, push openers and pop them when respective
		// closers arise
		for (int charPlace = 0; charPlace < input.length(); charPlace = charPlace + 1) {
			type = BType(input.charAt(charPlace));
			if (!bStack.isEmpty() && (bStack.peek() == '\'' || bStack.peek() == '\"')) {
				if (bStack.peek() == input.charAt(charPlace)) {
					bStack.pop();
				}
			}

			else if (!bStack.isEmpty() && bStack.peek() == '/') {
				if (charPlace < input.length() - 1 && input.charAt(charPlace) == '*'
						&& input.charAt(charPlace + 1) == '/') {
					bStack.pop();
				}
			}

			else if (charPlace < input.length() - 1 && input.charAt(charPlace) == '/'
					&& input.charAt(charPlace + 1) == '*') {
				bStack.push(input.charAt(charPlace));
			} else if (type >= 0) {
				if (type > 5) {
					bStack.push(input.charAt(charPlace));
				} else if (type % 2 == 0) {
					bStack.push(input.charAt(charPlace));
				} else if (charPlace > 0 && type % 2 == 1) {
					if (!bStack.isEmpty() && type != 0 && type == BType(bStack.peek()) + 1) {
						bStack.pop();
					} else {
						return false;
					}
				}
			}

		}

		return bStack.isEmpty();
	}

	/*
	 * Returns the index of the first character where all brackets in the input
	 * string are closed Returns -1 if such ann index does not index
	 */
	public static int closePlace(String input) {
		input = input.trim();
		Stack<Character> bStack = new Stack<Character>();
		int type = -1;

		for (int charPlace = 0; charPlace < input.length(); charPlace = charPlace + 1) {
			type = BType(input.charAt(charPlace));
			if (!bStack.isEmpty() && (bStack.peek() == '\'' || bStack.peek() == '\"')) {
				if (bStack.peek() == input.charAt(charPlace)) {
					bStack.pop();
				}
			} else if (!bStack.isEmpty() && bStack.peek() == '/') {
				if (charPlace < input.length() - 1 && input.charAt(charPlace) == '*'
						&& input.charAt(charPlace + 1) == '/') {
					bStack.pop();
				}
			} else if (charPlace < input.length() - 1 && input.charAt(charPlace) == '/'
					&& input.charAt(charPlace + 1) == '*') {
				bStack.push(input.charAt(charPlace));
			}

			else if (type >= 0) {
				if (type > 5) {
					bStack.push(input.charAt(charPlace));
				} else if (type % 2 == 0) {
					bStack.push(input.charAt(charPlace));
				} else if (charPlace > 0 && type % 2 == 1) {
					if (!bStack.isEmpty() && type != 0 && type == BType(bStack.peek()) + 1) {
						bStack.pop();
					}
				}
			}
			if (bStack.isEmpty())
				return charPlace;
		}
		return -1;
	}

	/*
	 * Returns the index of the string str in the string input that isn't contained
	 * in a quote or comment Returns -1 if str is not found under these conditions
	 */
	public static int findRealString(String input, String str) {
		input = input.trim();
		Stack<Character> bStack = new Stack<Character>();
		int type = -1;

		for (int charPlace = 0; charPlace < input.length(); charPlace = charPlace + 1) {
			type = BType(input.charAt(charPlace));
			if (!bStack.isEmpty() && (bStack.peek() == '\'' || bStack.peek() == '\"')) {
				if (bStack.peek() == input.charAt(charPlace)) {
					bStack.pop();
				}
			} else if (!bStack.isEmpty() && bStack.peek() == '/') {
				if (charPlace < input.length() - 1 && input.charAt(charPlace) == '*'
						&& input.charAt(charPlace + 1) == '/') {
					bStack.pop();
				}
			}

			else if (charPlace < input.length() - 1 && input.charAt(charPlace) == '/'
					&& input.charAt(charPlace + 1) == '*') {
				bStack.push(input.charAt(charPlace));
			} else if (type > 5) {
				bStack.push(input.charAt(charPlace));
			}
			if (charPlace < input.length() - str.length() + 1
					&& (input.substring(charPlace, charPlace + str.length())).equals(str) && bStack.isEmpty()) {
				return charPlace;
			}
		}
		return -1;
	}

	/*
	 * Returns an integer indicating what kind of bracket the character input is
	 * Even returns below 6 are starters Odd returns below 6 are closers Above 5 is
	 * ' and " respectively
	 */
	public static int BType(char ch) {
		return ("(){}[]\'\"").indexOf(ch);
	}

}