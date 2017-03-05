package amber.inputs;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

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

public class CallbackTest {

	private AutomateBuilder builder;
	private static Map<String, String> callRegister;
	
	@Before
	public void setup() {
		this.builder = AutomateBuilder.createAutomateBuilder();
		callRegister = new HashMap<String, String>();
	}
	
	@Test
	public void shouldCallCallbackOnEntryAtStart() {
		final Automate automate = this.buildAutomate();
		final Execution exe = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		exe.start();
		
		assertTrue("Callback not called correctly", callRegister.get("start on entry") != null && callRegister.get("start on entry").equals("entry"));
	}
	
	@Test
	public void shouldCallCallbackOnEntryAtEntry() {
		final Automate automate = this.buildAutomate();
		final Execution exe = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		exe.start();
		
		exe.update(new TextInput("1"));
		assertTrue("Callback not called correctly", callRegister.get("a on entry") != null && callRegister.get("a on entry").equals("entry"));
	}
	
	@Test
	public void shouldCallOnEntryOnEpsilonTransition() {
		final Automate automate = this.buildAutomate();
		final Execution exe = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		exe.start();
		
		exe.update(new TextInput("1"));
		exe.update(new TextInput("2"));
		assertTrue("Callback not called correctly", callRegister.get("b on entry") != null && callRegister.get("b on entry").equals("entry"));
		assertTrue("Callback not called correctly", callRegister.get("c on entry") != null && callRegister.get("c on entry").equals("entry"));
	}
	
	@Test
	public void shouldCallOnEnryForErrorState() {
		final Automate automate = this.buildAutomate();
		final Execution exe = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		exe.start();
		
		exe.update(new TextInput("invalid"));
		assertTrue("Callback not called correctly", callRegister.get("error on entry") != null && callRegister.get("error on entry").equals("entry"));
	}
	
	@Test
	public void shouldCallOnExitForState() {
		final Automate automate = this.buildAutomate();
		final Execution exe = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		exe.start();
		
		exe.update(new TextInput("1"));
		assertTrue("Callback not called correctly", callRegister.get("start on exit") != null && callRegister.get("start on exit").equals("exit"));
	}
	
	@Test
	public void shouldCallOnExitForStateOnError() {
		final Automate automate = this.buildAutomate();
		final Execution exe = new Execution(automate, ExecutionConfiguration.getDefaultConfiguration());
		exe.start();
		
		exe.update(new TextInput("invalid"));
		assertTrue("Callback not called correctly", callRegister.get("start on exit") != null && callRegister.get("start on exit").equals("exit"));
	}
	
	@Test
	public void shouldCallOnStaysOnState() {
		final Automate automate = this.buildAutomate();
		
		final ExecutionConfiguration conf = new ExecutionConfiguration();
		conf.setAllowToStayInStateOnUpdate(true);
		
		final Execution exe = new Execution(automate, conf);
		exe.start();
		exe.update(new TextInput("1"));
		exe.update(new TextInput("stay here"));
		
		assertTrue("Callback not called correctly", callRegister.get("a on stay") != null && callRegister.get("a on stay").equals("stay"));
		assertTrue("Exit called when only stay was expected callback", callRegister.get("a on exit") == null);
	}
	
	private Automate buildAutomate() {
		return this.builder
		.addState(State.createState("start", this.createCallback("start on entry", "entry"), this.createCallback("start on exit", "exit")))
		.addState(State.createState("error", this.createCallback("error on entry", "entry"), this.createCallback("error on exit", "exit"), this.createCallback("error on stay", "stay")))
		.addState(State.createState("a", this.createCallback("a on entry", "entry"), this.createCallback("a on exit", "exit"), this.createCallback("a on stay", "stay")))
		.addState(State.createState("b", this.createCallback("b on entry", "entry"), this.createCallback("b on exit", "exit"), this.createCallback("b on stay", "stay")))
		.addState(State.createState("c", this.createCallback("c on entry", "entry"), this.createCallback("c on exit", "exit"), this.createCallback("c on stay", "stay")))
		
		.addTransition("start", new TextInput("1"), "a")
		.addTransition("a", new TextInput("2"), "b")
		.addTransition("b", EpsilonInput.getEpsilonInput(), "c")
		
		.setStartState("start")
		.setErrorState("error")
		.build();
	}
	
	private Callback createCallback(final String name, final String msg) {
		return new Callback(name) {

			@Override
			public void run() {
				callRegister.put(super.name, msg);
			}
		};
	}
}
