package amber.storage;

import java.util.Arrays;

import com.google.gson.Gson;

import amber.input.Input;

public class InputJsonFormat extends JsonFormat {

	protected static final Gson JSON = new Gson();
	
	public static InputJsonFormat inputToJsonFormat(final Input input) {
		final String className = input.getClass().getCanonicalName();
		final Class<?> inputJsonObjectClass = input.getJsonObjectClass();
		
		// Check that input class is suitable to be stored in JSON format
		try {
			final Class<?> inputClass = Class.forName(className);
			
			// Check that implements input
			if (!Arrays.asList(inputClass.getInterfaces()).contains(Input.class)) {
				throw new IllegalArgumentException("Given input class doesnt implement input interface");
			}
			
			// Check that has the required constructor
			inputClass.getConstructor(inputJsonObjectClass);
			
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Invalid input class name: " + className + ". Class not found.");
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Given input class needs to implement constructor which takes" + inputJsonObjectClass.getCanonicalName() + " as an argument.");
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		
		// Create input JSON format
		final InputJsonFormat jsonFormat = new InputJsonFormat();
		jsonFormat.setJson_object(JSON.toJson(input.toJsonObject()));
		jsonFormat.setInput_class_name(className);
		
		// Check if is nested class
		if (inputJsonObjectClass.getEnclosingClass() != null) {
			// If is nested class replace last path separator with $
			final String jsonObjectClassName = inputJsonObjectClass.getCanonicalName();
			final int pathFormTopToNestedClassIndex = jsonObjectClassName.lastIndexOf('.');
			jsonFormat.setInput_json_class_name(jsonObjectClassName.substring(0, pathFormTopToNestedClassIndex) + "$" + jsonObjectClassName.substring(pathFormTopToNestedClassIndex + 1));
		}
		
		else {
			jsonFormat.setInput_json_class_name(inputJsonObjectClass.getCanonicalName());
		}
		
		return jsonFormat;
	}
	
	public static Input loadInput(final InputJsonFormat inputJsonFormat) {
		Input input;
		
		try {
			final Class<?> inputClass = Class.forName(inputJsonFormat.getInput_class_name());
			final Class<?> inputJsonObjectClass = Class.forName(inputJsonFormat.getInput_json_class_name());
			
			final Object inputJsonObject = JSON.fromJson(inputJsonFormat.getJson_object(), inputJsonObjectClass);
			input = (Input)inputClass.getConstructor(inputJsonObjectClass).newInstance(inputJsonObjectClass.cast(inputJsonObject));
		} catch (Exception e) {
			throw new IllegalArgumentException("Couldn't create input class: " + inputJsonFormat.getInput_class_name());
		}
		
		return input;
	}
	
	protected String json_object;
	protected String input_class_name;
	protected String input_json_class_name;
	
	public String getJson_object() {
		return this.json_object;
	}
	
	public void setJson_object(final String jsonObject) {
		this.json_object = jsonObject;
	}
	
	public String getInput_class_name() {
		return this.input_class_name;
	}
	
	public void setInput_class_name(final String className) {
		this.input_class_name = className;
	}

	public String getInput_json_class_name() {
		return this.input_json_class_name;
	}
	
	public void setInput_json_class_name(final String input_json_class_name) {
		this.input_json_class_name = input_json_class_name;
	}
	
	/*protected final String input_text_format;
	protected final String class_name;
	
	public InputJsonFormat(final String input_text_format, final String class_name) {
		this.input_text_format = input_text_format;
		this.class_name = class_name;
	}

	public String getInput_text_format() {
		return input_text_format;
	}

	public String getClass_name() {
		return class_name;
	}
	
	@Override
	public String toString() {
		return "class_name: " + this.class_name + ", input_text_format: " + this.input_text_format;
	}*/
}
