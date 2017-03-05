package amber.storage;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import amber.automate.Automate;
import amber.automate.AutomateBuilder;
import amber.automate.Execution;
import amber.automate.State;
import amber.common.Callback;
import amber.input.TextInput;
import amber.automate.ExecutionConfiguration;

public class JsonSaveFormatTest {

	@Test
	public void shouldSavePairRecognizingAutomateThenLoadAndRunCorrectly() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Automate automate = AutomateBuilder.createAutomateBuilder()
				.addState("error")
				
				.addState(State.createState("pair", new Callback("pair") {
					@Override
					public void run() {
						System.out.println("in state pair");
					}
				}))
				
				.addState(State.createState("nonPair", new Callback("nonPair") {
					@Override
					public void run() {
						System.out.println("in state non pair");
					}
				}))
					
				.setErrorState("error")
				.setStartState("pair")
				
				.addTransition("pair", new TextInput("1"), "nonPair")
				.addTransition("pair", new TextInput("0"), "pair")
				.addTransition("nonPair", new TextInput("1"), "pair")
				.addTransition("nonPair", new TextInput("0"), "nonPair")
				
				.build();
		
		final String jsonFormat = Storage.automateToJson(automate);
		System.out.println(jsonFormat);
		
		final Automate loadedAutomate = Storage.loadAutomate(jsonFormat);
		assertEquals(loadedAutomate.getStartState().getName(), "pair");
		
		final Execution execution = new Execution(loadedAutomate, ExecutionConfiguration.getDefaultConfiguration());
		execution.start();
		execution.update(new TextInput("1"), new TextInput("0"), new TextInput("0"));
		assertEquals(execution.getCurrentStates().iterator().next().getName(), "nonPair");
	}
}
