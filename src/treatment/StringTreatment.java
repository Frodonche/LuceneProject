package treatment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTreatment {

	/**
	 *
	 * @param chain
	 * @return The ID of the medoc
	 */
	public static String getID(String text) {
		Pattern pattern = Pattern.compile("DB[0-9]+");

		Matcher matcher = pattern.matcher(text);
		matcher.find();

		String toReturn = matcher.group(0);

		return toReturn;
	}

	/**
	 *
	 * @return The content of the field "fieldName" from the text "text"
	 */
	public static String getFieldContent(String fieldName, String text) {

		// Find the field
		Pattern pattern = Pattern.compile("# " + fieldName + ":\n.*");

		Matcher matcher = pattern.matcher(text);
		matcher.find();

		String toReturn = matcher.group(0);

		// Get rid of the field and only keep the content
		toReturn = toReturn.replaceFirst("# " + fieldName + ":\n", "");

		return toReturn;
	}
}
