package amber.automate;

import static org.junit.Assert.*;

import org.junit.Test;

import amber.common.Callback;
import amber.input.TextInput;

public class AutomateTest {

	@Test
	public void shouldCorrectlyBuildPairRegognizingAutomate() {
		Automate automate = AutomateBuilder.createAutomateBuilder()
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
			
			
		final Execution execution = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		
		execution.start();
		execution.update(new TextInput("0"), new TextInput("1"), new TextInput("0") , new TextInput("0"), new TextInput("1"));
		
		assertTrue(execution.getCurrentStates().size() == 1);
		assertEquals(execution.getCurrentStates().iterator().next().getName(), "pair");
	}
}
