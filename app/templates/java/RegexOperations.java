package <%=packageName%>.utils.Operations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexOperations 
{
	String text;
	String regex;
	int group;
	
	//	Default Constructor
	public RegexOperations()
	{
		this.group = 0;
		this.text = "";
		this.regex = "";
	}
	
	//	Parameterized Constructor
	RegexOperations(String text, String regex, int group)
	{
		this.group = group;
		this.text = text;
		this.regex = regex;
	}
	
	/*	A function that returns a matched text and group required in a given text.
	 * 	Input Parameters:	
	 * 	Text: The text in which you want a pattern to be matched.
	 * 	Regex:	The regular expression that you want to use to match text
	 * 	Group: 	The group number that needs to be matched.
	 */
	public static String getValue(String text, String regex, int group) 
	{
		try
		{
			//	Compile the pattern.
			Pattern r = Pattern.compile(regex);
	
			// Checks if text matches the regular expression.
			Matcher m = r.matcher(text);
			
			// If the text matches the regular expression, tag the word as a date.
			if(m.find())
			{
				return m.group(group);
			}
		}
		
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		return null;
	}
}
