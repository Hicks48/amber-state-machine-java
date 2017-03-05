package amber.automate;

import java.util.HashSet;
import java.util.Set;

/**
 * This class presents automate structure. It contains the states and transitions between those states.
 * 
 * Execution class is used to describe execution of automate.
 * 
 * Automate can be saved into JSON format using the Storage class.
 * 
 * @author Hicks48
 */
public class Automate {
	
	/**
	 * Start state is the state which is first current state in the execution process.
	 */
	protected State startState;
	
	/**
	 * Error state is a state in which execution proceeds when there is no defined transition with given input.
	 */
	protected State errorState;
	
	/**
	 * End states of the execution.
	 */
	protected final Set<String> endStateNames;
	
	/**
	 * Transition table holds the information about the transitions between the states and the actual states them selves.
	 */
	protected TransitionTable transitionTable;
	
	/**
	 * Create a new Automate object by providing start state, error state and transition table.
	 * Consider using AutomateBuilder class to initiate Automate object before using this.
	 * @param startState Start state is the state which is first current state in the execution process.
	 * @param errorState Error state is a state in which execution proceeds when there is no defined transition with given input.
	 * @param transitionTable Transition table holds the information about the transitions between the states and the actual states them selves.
	 */
	public Automate(final State startState, final State errorState, final Set<String> endStates, final TransitionTable transitionTable) {		
		this.setStartState(startState);
		this.setErrorState(errorState);
		this.endStateNames = endStates;
		
		this.transitionTable = transitionTable;
	}
	
	public Set<String> getEndStateNames() {
		return this.endStateNames;
	}
	
	public Set<State> getEndStates() {
		// Collect all states
		final Set<State> allStates = new HashSet<State>();
		allStates.addAll(this.transitionTable.getSourceStates());
		allStates.addAll(this.transitionTable.getTargetStates());
		
		// Collect all end states
		final Set<State> endStates = new HashSet<State>();
		for (final State state : allStates) {
			
			if (this.endStateNames.contains(state.getName())) {
				endStates.add(state);
			}
		}
		
		return endStates;
	}
	
	/**
	 * Returns transition table of this automate.
	 * @return Transition table of this automate.
	 */
	public TransitionTable getTransitionTable() {
		return this.transitionTable;
	}
	
	/**
	 * Returns error state of this automate.
	 * @return Error state of this automate.
	 */
	public State getErrorState() {
		return this.errorState;
	}
	
	/**
	 * Set error state for this automate. Throws exception if given error state is null.
	 * @param errorState Error state for this automate.
	 */
	public void setErrorState(final State errorState) {	
		
		if (errorState == null) {
			throw new IllegalArgumentException("Can't set null error state.");
		}
		
		this.errorState = errorState;
	}
	
	/**
	 * Returns start state of this automate.
	 * @return Start state of this automate.
	 */
	public State getStartState() {
		return this.startState;
	}
	
	/**
	 * Set start state for this automate. Throws exception if given start state is null.
	 * @param startState Start state for this automate.
	 */
	public void setStartState(final State startState) {
		
		if (startState == null) {
			throw new IllegalArgumentException("Can't set null start state.");
		}
		
		this.startState = startState;
	}
}
