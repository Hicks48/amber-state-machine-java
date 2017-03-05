package amber.automate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import amber.common.Pair;
import amber.input.EpsilonInput;
import amber.input.Input;

/**
 * Presents the transition table of automate. Has information of all the states and the transitions between them.
 * Error state is not part of the transition table.
 * @author Hicks48
 */
public class TransitionTable {
	
	/**
	 * Transition table map.
	 */
	protected final Map<Pair<State, Input>, Set<State>> transitionTable;
	
	/**
	 * Creates new transition table. Before using this constructor consider using automate builder class.
	 * @param transitionTable Transition table map which is used as a transition table.
	 */
	public TransitionTable(final Map<Pair<State, Input>, Set<State>> transitionTable) {
		this.transitionTable = transitionTable;
	}
	
	/**
	 * Adds state to the transition table.
	 * @param state State to be added.
	 */
	public void addState(final State state) {
		final Pair<State, Input> transitionTableEntry = new Pair<State, Input>(state, null);
		this.transitionTable.put(transitionTableEntry, new HashSet<State>());
	}
	
	/**
	 * Returns transition table map which contains all transitions.
	 * @return Transition table map which contains all transitions.
	 */
	public Map<Pair<State, Input>, Set<State>> getAllTransitions() {
		return this.transitionTable;
	}
	
	/**
	 * Returns transition table map which holds all transitions which have given state as a source.
	 * @param sourceState Source state which transitions are retrieved.
	 * @return Transition table map which holds all transitions which have given state as a source.
	 */
	public Map<Pair<State, Input>, Set<State>> getAllTransitionsForState(final State sourceState) {
		final Map<Pair<State, Input>, Set<State>> transitionsForState = new HashMap<Pair<State, Input>, Set<State>>();
		
		for (final Pair<State, Input> transition : this.transitionTable.keySet()) {
			
			if (transition.getFirst().equals(sourceState)) {
				transitionsForState.put(transition, this.transitionTable.get(transition));
			}
		}
		
		return transitionsForState;
	}
	
	/**
	 * Returns transitions for given state input pair.
	 * State input pair holds the source state and input.
	 * @param stateInputPair State input pair holds the source state and input.
	 * @return Transition for given state input pair.
	 */
	public Set<State> getTransitionWithoutEpsilonTransitions(final Pair<State, Input> stateInputPair) {
		return this.transitionTable.get(stateInputPair);
	}
	
	/**
	 * Returns transitions for given state and input.
	 * @param state Source state of transition being retrieved.
	 * @param input Input of transition being retrieved.
	 * @return Transitions for given state and input.
	 */
	public Set<State> getTransition(final State state, final Input input) {
		return this.getTransitionWithoutEpsilonTransitions(new Pair<State, Input>(state, input));
	}
	
	public Set<State> getTransitionWithEpsilon(final State state, final Input input) {
		return this.getTransitionWithEpsilon(new Pair<State, Input>(state, input));
	}
	
	public Set<State> getTransitionWithEpsilon(final Pair<State, Input> stateInputPair) {
		// Get target states without epsilon transitions
		final Set<State> targetStates = this.transitionTable.get(stateInputPair);
		
		if (targetStates == null || targetStates.isEmpty()) {
			return new HashSet<State>();
		}
		
		return this.getEpsilonTranitions(targetStates);
	}
	
	public Set<State> getEpsilonTranitions(final Set<State> states) {
		// Do search to find all states which are connected via epsilon transitions
		final Set<State> searchedStates = new HashSet<State>();
		final Set<State> statesToSearch = new HashSet<State>();
		
		// Initialize states to search
		statesToSearch.addAll(states);
		
		while (!statesToSearch.isEmpty()) {
			// Select state to process
			final State currentState = statesToSearch.iterator().next();
			
			// Add current state to target states
			states.add(currentState);
			
			// Update book keeping sets
			statesToSearch.remove(currentState);
			searchedStates.add(currentState);
			
			// Find epsilon transitions
			final Set<State> epsilonTargetStates = this.transitionTable.get(new Pair<State, Input>(currentState, EpsilonInput.getEpsilonInput()));
			
			if (epsilonTargetStates == null) {
				continue;
			}
			
			for (final State epsilonTargetState : epsilonTargetStates) {
				
				// Add if not already checked
				if (!searchedStates.contains(epsilonTargetState)) {
					statesToSearch.add(epsilonTargetState);
				}
			}
		}
		
		return states;
	}
	
	/**
	 * Adds transition with given source state, input and target states.
	 * Throws exception if same transition is being added for the second time.
	 * @param stateInputPair Holds source state and input of transition being added.
	 * @param transitionStates States where added transition leads to.
	 */
	public void addTransition(final Pair<State, Input> stateInputPair, final Set<State> transitionStates) {
		if (this.transitionTable.containsKey(stateInputPair)) {
			throw new IllegalArgumentException("Given state input pair already exists in transition table");
		}
		
		this.transitionTable.put(stateInputPair, transitionStates);
	}
	
	/**
	 * Updates transition with given state input pair.
	 * Allows chancing target states of transition in code.
	 * Throws exception if given transition with given state input pair doesn't exist.
	 * @param stateInputPair State input pair of transition being updated.
	 * @param transitionStates New target states for transition.
	 */
	public void updateTransition(final Pair<State, Input> stateInputPair, final Set<State> transitionStates) {
		if (!this.transitionTable.containsKey(stateInputPair)) {
			throw new IllegalArgumentException("Given state input pair doesn't exists in transition table");
		}
		
		this.transitionTable.put(stateInputPair, transitionStates);
	}
	
	public void deleteTransitionsForStateInputPair(final Pair<State, Input> stateInputPair) {
		this.transitionTable.remove(stateInputPair);
	}
	
	/**
	 * Returns all inputs which are present in transition table.
	 * @return All inputs which are present in transition table.
	 */
	public Set<Input> getInputs() {
		final Set<Input> inputs = new HashSet<Input>();
		
		for (final Pair<State, Input> stateInputPair : this.transitionTable.keySet()) {
			inputs.add(stateInputPair.getSecond());
		}
		
		return inputs;
	}
	
	/**
	 * Returns all source states of this transition table.
	 * @return All source states of this transition table.
	 */
	public Set<State> getSourceStates() {
		final Set<State> states = new HashSet<State>();
		
		for (final Pair<State, Input> stateInputPair : this.transitionTable.keySet()) {
			states.add(stateInputPair.getFirst());
		}
		
		return states;
	}
	
	/**
	 * Returns all target states of this transition table.
	 * @return All target states of this transition table.
	 */
	public Set<State> getTargetStates() {
		final Set<State> states = new HashSet<State>();
		
		for (final Set<State> stateSet : this.transitionTable.values()) {
			states.addAll(stateSet);
		}
		
		return states;
	}
}
