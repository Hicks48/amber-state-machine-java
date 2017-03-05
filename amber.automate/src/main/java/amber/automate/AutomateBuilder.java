package amber.automate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import amber.common.Pair;
import amber.input.Input;

/**
 * Use this class to build automates. This class provides easy and clean way for building automates in code.
 * 
 * @author Hicks48
 *
 */
public class AutomateBuilder {
	
	/**
	 * Create automate builder which can be used to build automate object.
	 * @return New AutomateBuilder object.
	 */
	public static AutomateBuilder createAutomateBuilder() {
		return new AutomateBuilder();
	}
	
	/**
	 * Base of transition table for automate being created.
	 */
	protected Map<Pair<State, Input>, Set<State>> transitionTable;
	
	/**
	 * Map from state names to actual states.
	 */
	protected Map<String, State> states;
	
	/**
	 * Start state for automate being created.
	 */
	protected State startState;
	
	/**
	 * Error state for automate being created.
	 */
	protected State errorState;
	
	protected Set<String> endStateNames;
	
	/**
	 * Creates new automate builder object.
	 */
	protected AutomateBuilder() {
		this.transitionTable = new HashMap<Pair<State, Input>, Set<State>>();
		this.states = new HashMap<String, State>();
		this.endStateNames = new HashSet<String>();
	}
	
	/**
	 * Set start state for automate being created. State with given name must first be created with method addState.
	 * Throws exception if state with given name has not been created.
	 * @param name Name of state to be set as start state.
	 * @return Automate builder object so that calls can be chained.
	 */
	public AutomateBuilder setStartState(final String name) {
		
		if (!this.states.containsKey(name)) {
			throw new IllegalArgumentException("No state named " + name + " found. Use addState first.");
		}
		
		this.startState = this.states.get(name);
		
		return this;
	}
	
	/**
	 * Set start state for automate being created. Given state must first be added using addState method.
	 * Throws exception if state with the same name as given state has not been created.
	 * @param startState Start state for automate being created.
	 * @return Automate builder object so that calls can be chained.
	 */
	public AutomateBuilder setStartState(final State startState) {
		
		if (!this.states.containsKey(startState.getName())) {
			throw new IllegalArgumentException("No state named " + startState.getName() + " found. Use addState first.");
		}
		
		this.startState = startState;
		
		return this;
	}
	
	/**
	 * Set error state for automate being created. State with given name must first be created with method addState.
	 * Throws exception if state with given name has not been created.
	 * @param name Name of the state to be set as error state .
	 * @return Automate builder object so that calls can be chained.
	 */
	public AutomateBuilder setErrorState(final String name) {
		
		if (!this.states.containsKey(name)) {
			throw new IllegalArgumentException("No state named " + name + " found. Use addState first.");
		}
		
		this.errorState = this.states.get(name);
		
		return this;
	}
	
	/**
	 * Set error state for automate being created. State with the same name as given state must first be created with method addState.
	 * Throws exception if state with given name has not been created.
	 * @param errorState State to be set as error state.
	 * @return Automate builder object so that calls can be chained.
	 */
	public AutomateBuilder setErrorState(final State errorState) {
		
		if (!this.states.containsKey(errorState.getName())) {
			throw new IllegalArgumentException("No state named " + errorState.getName() + " found. Use addState first.");
		}
		
		this.errorState = errorState;
		
		return this;
	}
	
	/**
	 * Adds state to automate. State with given name is created. 
	 * Throws exception if automate already has a state with the same name as given name.
	 * @param name Name for the state to be added.
	 * @return Automate builder object so that calls can be chained.
	 */
	public AutomateBuilder addState(final String name) {
		
		if (this.states.containsKey(name)) {
			throw new IllegalArgumentException("Already has a state named " + name + ". Can't have two states with same name.");
		}
		
		this.states.put(name, State.createState(name));
		return this;
	}
	
	/**
	 * Adds given state to the automate.
	 * Throws exception if automate already has a state with the same name as given state.
	 * @param state State to be added to the automate.
	 * @return Automate builder object so that calls can be chained.
	 */
	public AutomateBuilder addState(final State state) {
		
		if (this.states.containsKey(state.getName())) {
			throw new IllegalArgumentException("Already has a state named " + state.getName() + ". Can't have to states with same name.");
		}
		
		this.states.put(state.getName(), state);
		return this;
	}
	
	public AutomateBuilder addEndState(final State endState) {
		
		if (!this.states.containsKey(endState.getName())) {
			throw new IllegalArgumentException("No state named " + endState.getName() + " found. Use addState first.");
		}
		
		this.endStateNames.add(endState.getName());
		
		return this;
	}
	
	public AutomateBuilder addEndState(final String name) {
		
		if (!this.states.containsKey(name)) {
			throw new IllegalArgumentException("No state named " + name + " found. Use addState first.");
		}
		
		this.endStateNames.add(name);
		
		return this;
	}
	
	/**
	 * Adds transition from state with source state name with given input to all target states.
	 * All states used must be first added with addState method.
	 * Throws exception if source state or some of the target stated are not found.
	 * @param sourceStateName Name of source state (source of transition).
	 * @param input Input which triggers transition.
	 * @param targetStateNames Names of all target states (names of states where transition leads to).
	 * @return Automate builder object so that calls can be chained.
	 */
	public AutomateBuilder addTransition(final String sourceStateName, final Input input, final String... targetStateNames) {
		return this.addTransition(sourceStateName, input, Arrays.asList(targetStateNames));
	}
	
	/**
	 * Adds transition from state with source state name with given input to all target states.
	 * All states used must be first added with addState method.
	 * Throws exception if source state or some of the target stated are not found.
	 * @param sourceStateName Name of source state (source of transition).
	 * @param input Input which triggers transition.
	 * @param targetStateNames Names of all target states (names of states where transition leads to).
	 * @return Automate builder object so that calls can be chained.
	 */
	public AutomateBuilder addTransition(final String sourceStateName, final Input input, final List<String> targetStateNames) {
		
		if (!this.states.containsKey(sourceStateName)) {
			throw new IllegalArgumentException("No state named " + sourceStateName + " found. Use addState first.");
		}
		
		final Pair<State, Input> stateInputPair = new Pair<State, Input>(this.states.get(sourceStateName), input);
		
		final Set<State> transitionStates = new HashSet<State>();
		for (final String targetStateName : targetStateNames) {
			
			if (!this.states.containsKey(targetStateName)) {
				throw new IllegalArgumentException("No state named " + sourceStateName + " found. Use addState first.");
			}
			
			transitionStates.add(this.states.get(targetStateName));
		}
		
		// Update transition table
		if (!this.transitionTable.containsKey(stateInputPair)) {
			this.transitionTable.put(stateInputPair, transitionStates);
		}
		
		else {
			final Set<State> originalTargetStates = this.transitionTable.get(stateInputPair);
			originalTargetStates.addAll(transitionStates);
			this.transitionTable.put(stateInputPair, originalTargetStates);
		}
		
		return this;
	}
	
	/**
	 * Adds transition from state with source state name with given input to all target states.
	 * All states used must be first added with addState method.
	 * Throws exception if source state or some of the target stated are not found.
	 * @param sourceState Source state (source of transition).
	 * @param input Input which triggers transition.
	 * @param targetStates All target states (States where transition leads to).
	 * @return Automate builder object so that calls can be chained.
	 */
	public AutomateBuilder addTransition(final State sourceState, final Input input, final State... targetStates) {
		
		// Check source state
		if (!this.states.containsKey(sourceState.getName())) {
			throw new IllegalArgumentException("No state named " + sourceState.getName() + " found. Use addState first.");
		}
		
		// Create source state input pair
		final Pair<State, Input> stateInputPair = new Pair<State, Input>(this.states.get(sourceState.getName()), input);
		
		// Check transition states
		final Set<State> transitionStates = new HashSet<State>();
		for (final State targetState : targetStates) {
			
			if (!this.states.containsKey(targetState.getName())) {
				throw new IllegalArgumentException("No state named " + targetState.getName() + " found. Use addState first.");
			}
			
			transitionStates.add(targetState);
		}
		
		// Update transition table
		if (!this.transitionTable.containsKey(stateInputPair)) {
			this.transitionTable.put(stateInputPair, transitionStates);
		}
		
		else {
			final Set<State> originalTargetStates = this.transitionTable.get(stateInputPair);
			originalTargetStates.addAll(transitionStates);
			this.transitionTable.put(stateInputPair, originalTargetStates);
		}
		
		return this;
	}
	
	/**
	 * Resets this automate builder.
	 * Removes all data of the automate which was being built.
	 */
	public void clear() {
		this.transitionTable.clear();
		this.states.clear();
		this.startState = null;
		this.errorState = null;
	}
	
	/**
	 * Builds automate with data which was set.
	 * If no custom automate configuration was set uses default configuration.
	 * Checks that both start and error states have been set. Throws exception id either one is missing.
	 * @return New automate object.
	 */
	public Automate build() {
		
		if (this.startState == null) {
			throw new IllegalStateException("Start state has not been set.");
		}
		
		if (this.errorState == null) {
			throw new IllegalStateException("Error state has not been set.");
		}
		
		return new Automate(this.startState, this.errorState, this.endStateNames, new TransitionTable(this.transitionTable));
	}
}
