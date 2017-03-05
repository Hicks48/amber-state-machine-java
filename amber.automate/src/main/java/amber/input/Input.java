package amber.input;

import java.io.Serializable;

/**
 * Interface for all classes which are used as an input in an automate.
 * Custom inputs are allowed. They must implement valid equals and hashCode methods.
 * To be able to save in JSON format toTextFormat and constructor which takes InputJsonFormat as an argument must also be implemented.
 * @author Hicks48
 */
public interface Input extends Serializable {
	
	/**
	 * Returns text which is stored inside InputJsonFormat and then provided as an constructor argument.
	 * @return Text which is stored inside InputJsonFormat and then provided as an constructor argument.
	 */
	public Object toJsonObject();
	
	public Class<?> getJsonObjectClass();
}
