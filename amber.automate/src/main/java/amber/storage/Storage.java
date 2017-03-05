package amber.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import com.google.gson.Gson;

import amber.automate.Automate;
import amber.automate.Execution;


/**
 * This class can be used to save automates in JSON format.
 * @author Hicks48
 */
public class Storage {
	
	protected static final Gson JSON = new Gson();
	
	/**
	 * Saves given automate in JSON format and saves generated JSON in given file.
	 * Throws exceptions if given file doesn't exist or is not a file but a directory.
	 * @param automate Automate to be saved.
	 * @param saveFile File object which points to file where JSON is to be saved.
	 * @throws IOException Is thrown if invalid file object is given. 
	 */
	public static void saveAutomate(final Automate automate, final File saveFile) throws IOException {
		writeToFile(automateToJson(automate), saveFile);
	}
	
	public static void saveExecution(final Execution execution, final File saveFile) throws IOException {
		writeToFile(executionToJson(execution), saveFile);
	}
	
	private static void writeToFile(final String content, final File saveFile) throws IOException {
		if (!saveFile.exists()) {
			throw new IllegalArgumentException("Given file doesn't exist");
		}
		
		if (saveFile.isDirectory()) {
			throw new IllegalArgumentException("Given file is directory not a file");
		}
		
		final FileWriter writer = new FileWriter(saveFile);
		writer.write(content);
		writer.flush();
		writer.close();
	}
	
	public static Automate loadAutomate(final File loadFile) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final String json = readFileContents(loadFile);
		return loadAutomate(json);
	}
	
	public static Execution loadExecution(final File loadFile) throws IOException {
		final String json = readFileContents(loadFile);
		return loadExecution(json);
	}
	
	private static String readFileContents(final File file) throws IOException {
		if (!file.exists()) {
			throw new IllegalArgumentException("Given file doesn't exist");
		}
		
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Given file is directory not a file");
		}
		
		final StringBuilder contentBuilder = new StringBuilder();
		
		final Scanner reader = new Scanner(file);
		while (reader.hasNextLine()) {
			contentBuilder.append(reader.nextLine());
		}
		
		reader.close();
		return contentBuilder.toString();
	}
	
	public static Automate loadAutomate(final String json) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final AutomateJsonFormat automateJsonFormat = JSON.fromJson(json, AutomateJsonFormat.class);
		return AutomateJsonFormat.loadAutomate(automateJsonFormat);
	}
	
	public static Execution loadExecution(final String json) {
		final ExecutionJsonFormat executionJsonFormat = JSON.fromJson(json, ExecutionJsonFormat.class);
		return ExecutionJsonFormat.loadExecution(executionJsonFormat);
	}
	
	public static String executionToJson(final Execution execution) {
		return JSON.toJson(ExecutionJsonFormat.executionToJsonFormat(execution));
	}
	
	public static String automateToJson(final Automate automate) {
		return JSON.toJson(AutomateJsonFormat.automateToJsonFormat(automate));
	}
}
