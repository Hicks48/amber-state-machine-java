package amber.inputs;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import amber.automate.Automate;
import amber.automate.AutomateBuilder;
import amber.automate.Execution;
import amber.automate.ExecutionConfiguration;
import amber.automate.State;
import amber.common.Callback;
import amber.input.EpsilonInput;
import amber.input.TextInput;

public class EpsilonInputTest {

	private AutomateBuilder builder;
	private State errorState;
	
	public static Map<String, Boolean> callbackResults;
	
	@Before
	public void setup() {
		this.builder = AutomateBuilder.createAutomateBuilder();
		this.errorState = State.createState("error", new Callback("error") {

			@Override
			public void run() {
				callbackResults.put("error", true);
			}
		});
		
		callbackResults = new HashMap<String, Boolean>();
	}
	
	@Test
	public void shouldMoveFromStartStateToStatesWithEpsilonTransitionsAtStart() {
		final State start = State.createState("start");
		final State a = State.createState("A");
		final State b = State.createState("B");
		final State c = State.createState("C");
		
		this.builder
		.addState(start)
		.addState(this.errorState)
		.addState(a)
		.addState(b)
		.addState(c)
		.addTransition(start, EpsilonInput.getEpsilonInput(), a, b, c)
		.setStartState(start)
		.setErrorState(this.errorState);
		
		final Automate automate = this.builder.build();
		final Execution exe = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		exe.start();
		
		final List<State> expected = Arrays.asList(start, a, b, c);
		final Set<State> actual = exe.getCurrentStates();
		assertTrue("Expected current states to be: " + expected + " but was " + actual, actual.containsAll(expected) && actual.size() == expected.size());
	}
	
	@Test
	public void shouldMoveOnTransitionToStatesWithEpsilonTransition() {
		final State start = State.createState("start");
		final State a = State.createState("A");
		final State b = State.createState("B");
		final State c = State.createState("C");
		final State d = State.createState("D");
		
		this.builder
		.addState(start)
		.addState(this.errorState)
		.addState(a)
		.addState(b)
		.addState(c)
		.addState(d)
		
		.setStartState(start)
		.setErrorState(this.errorState)
		
		.addTransition(start, new TextInput("1"), a)
		.addTransition(a, new TextInput("2"), d)
		.addTransition(a, EpsilonInput.getEpsilonInput(), b, c);
		
		final Automate automate = this.builder.build();
		final Execution exe = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		exe.start();
		
		exe.update(new TextInput("1"));
		
		final List<State> expected = Arrays.asList(a, b, c);
		final Set<State> actual = exe.getCurrentStates();
		assertTrue("Expected current states to be: " + expected + " but was " + actual, actual.containsAll(expected) && actual.size() == expected.size());
	}
	
	@Test
	public void shouldCallCallbackOnEpsilonTransitionAtStart() {
		final State start = State.createState("start", new Callback("start") {

			@Override
			public void run() {
				callbackResults.put("start", true);
			}
		});
		
		final State a = State.createState("A", new Callback("A") {

			@Override
			public void run() {
				callbackResults.put("A", true);
			}
		});
		
		this.builder
		.addState(start)
		.addState(this.errorState)
		.addState(a)
		.addTransition(start, EpsilonInput.getEpsilonInput(), a)
		.setStartState(start)
		.setErrorState(this.errorState);
		
		final Automate automate = this.builder.build();
		final Execution exe = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		exe.start();
		
		assertTrue("All calbacks weren't called", callbackResults.get("A") != null && callbackResults.get("start") != null);
		assertTrue("All callback didn't return correct values", callbackResults.get("A") && callbackResults.get("start"));
	}
	
	@Test
	public void shouldCallCallbackOnTransitionToStatesWithEpsilonTransition() {
		final State start = State.createState("start", new Callback("start") {

			@Override
			public void run() {
				callbackResults.put("start", true);
			}
		});
		
		final State a = State.createState("A", new Callback("A") {

			@Override
			public void run() {
				callbackResults.put("A", true);
			}
		});
		
		final State b = State.createState("B", new Callback("B") {

			@Override
			public void run() {
				callbackResults.put("B", true);
			}
		});
		
		this.builder
		.addState(start)
		.addState(this.errorState)
		.addState(a)
		.addState(b)
		
		.setStartState(start)
		.setErrorState(this.errorState)
		
		.addTransition(start, new TextInput("1"), a)
		.addTransition(a, EpsilonInput.getEpsilonInput(), b);
		
		final Automate automate = this.builder.build();
		final Execution exe = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		exe.start();
		
		assertTrue("All calbacks weren't called correctly", callbackResults.get("B") == null && callbackResults.get("A") == null && callbackResults.get("start") != null);
		assertTrue("All callback didn't return correct values", callbackResults.get("start"));
		
		exe.update(new TextInput("1"));
		
		assertTrue("All calbacks weren't called correctly", callbackResults.get("B") != null && callbackResults.get("A") != null && callbackResults.get("start") != null);
		assertTrue("All callback didn't return correct values", callbackResults.get("B") && callbackResults.get("A"));
	}
}
