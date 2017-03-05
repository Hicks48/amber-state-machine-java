package amber.inputs;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import amber.automate.Automate;
import amber.automate.AutomateBuilder;
import amber.automate.Execution;
import amber.automate.ExecutionConfiguration;
import amber.automate.State;
import amber.input.TextInput;

public class TextInputTest {

	private AutomateBuilder automateBuilder;
	private Automate automate;
	
	private Execution exe;
	
	private State start;
	private State error;
	
	private State a;
	private State b1;
	private State b2;
	
	@Before
	public void setup() {
		this.automateBuilder = AutomateBuilder.createAutomateBuilder();
		this.start = State.createState("start");
		this.error = State.createState("error");
		this.a = State.createState("A");
		this.b1 = State.createState("B1");
		this.b2 = State.createState("B2");
	
		this.automateBuilder
		.addState(this.start)
		.addState(this.error)
		.addState(this.a)
		.addState(this.b1)
		.addState(this.b2)
		
		.setStartState(start)
		.setErrorState(this.error)
		
		.addTransition(start, new TextInput("1"), a)
		.addTransition(a, new TextInput("2"), b1)
		.addTransition(a, new TextInput("2"), b2);
		
		this.automate = this.automateBuilder.build();
	
		this.exe = new Execution(this.automate, ExecutionConfiguration.getDefaultConfiguration());
	}
	
	@Test
	public void shouldMoveToStateWithTextTransition() {
		this.exe.start();
		
		this.exe.update(new TextInput("1"));
		assertEquals(new HashSet<State>(Arrays.asList(this.a)), this.exe.getCurrentStates());
	}
	
	@Test
	public void shouldMoveToMultipleStateWhenTransitionHasMulipleSourceInputs() {
		this.exe.start();
		
		this.exe.update(new TextInput("1"), new TextInput("2"));
		assertEquals(new HashSet<State>(Arrays.asList(this.b1, this.b2)), this.exe.getCurrentStates());
	}
	
	@Test
	public void shoulMoveToErrorStateWithUndefinedInputIfNotAllowedToStayInSameState() {
		this.exe.start();
		
		this.exe.update(new TextInput("undefined"));
		assertEquals(new HashSet<State>(Arrays.asList(this.error)), this.exe.getCurrentStates());
	}
	
	@Test
	public void shoulStayInStateWithUndefinedInputIfAllowedToStayInSameState() {
		final ExecutionConfiguration conf = new ExecutionConfiguration();
		conf.setAllowToStayInStateOnUpdate(true);
		
		final Execution customeExe = new Execution(this.automate, conf);
		customeExe.start();
		
		customeExe.update(new TextInput("undefined"));
		assertEquals(new HashSet<State>(Arrays.asList(this.start)), customeExe.getCurrentStates());
	}
}
