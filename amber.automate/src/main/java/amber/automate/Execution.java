package amber.automate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import amber.input.Input;

/**
 * Describes the execution of automation. Holds information about the current states of the execution.
 * @author Hicks48
 *
 */
public class Execution {
	
	/**
	 * Automate execution runs on.
	 */
	protected final Automate automate;
	
	/**
	 * Current states of the execution.
	 */
	protected final Set<State> currentStates;
	
	protected final List<Set<State>> encounteredEndStates;
	
	/**
	 * Configuration for the execution.
	 */
	protected ExecutionConfiguration configuration;
	
	/**
	 * Create new execution for given automate with given configuration.
	 * @param automate Automate execution is run on.
	 * @param configuration Configuration for execution.
	 */
	public Execution(final Automate automate, final ExecutionConfiguration configuration) {
		this.automate = automate;
		
		this.currentStates = new HashSet<State>();
		this.encounteredEndStates = new ArrayList<Set<State>>();
		
		this.configuration = configuration;
		
		this.reset();
	}
	
	/**
	 * Returns current states of execution.
	 * @return Current states of execution.
	 */
	public Set<State> getCurrentStates() {
		return this.currentStates;
	}
	
	public List<Set<State>> getEncounteredEndStates() {
		return this.encounteredEndStates;
	}
	
	public Automate getAutomate() {
		return this.automate;
	}
	
	public ExecutionConfiguration getConfiguration() {
		return this.configuration;
	}
	
	public boolean start() {
		// Add current states and find epsilon transitions
		this.currentStates.add(this.automate.getStartState());
		this.automate.getTransitionTable().getEpsilonTranitions(this.currentStates);
		
		// Look if end states are found and call on entry callback for each
		final Set<String> endStateNames = this.automate.getEndStateNames();
		final Set<State> endStates = new HashSet<State>();
		for (final State currentState : this.currentStates) {
			
			if (currentState.getOnEntryCallback() != null) {
				currentState.getOnEntryCallback().run();
			}
			
			if (endStateNames.contains(currentState.getName())) {
				endStates.add(currentState);
			}
		}
		
		// Add end states if found
		if (!endStates.isEmpty()) {
			this.encounteredEndStates.add(endStates);
		}
		
		return !this.isAtEnd();
	}
	
	/**
	 * Resets execution process.
	 */
	public void reset() {
		this.currentStates.clear();
	}
	
	/**
	 * Updates execution with given inputs in order they are given.
	 * @param inputs Inputs used to update execution.
	 */
	public List<Boolean> update(final Input... inputs) {
		return this.update(Arrays.asList(inputs));
	}
	
	/**
	 * Updates execution with given inputs in order they are given.
	 * @param inputs Inputs used to update execution.
	 */
	public List<Boolean> update(final Collection<Input> inputs) {
		final List<Boolean> inputAccepted = new ArrayList<Boolean>();
		
		for (Input input : inputs) {
			inputAccepted.add(this.update(input));
		}
		
		return inputAccepted;
	}
	
	/**
	 * Updates execution using given input.
	 * @param input Input which is used to update execution.
	 */
	public boolean update(final Input input) {
		
		// Check that has been started
		if (this.currentStates.isEmpty()) {
			throw new IllegalStateException("Current states is empty. This is most likely because start method wasn't called before first update.");
		}
		
		final Set<State> nextCurrentStates = new HashSet<State>();
		final Set<State> endStates = new HashSet<State>();
		
		for (final State currentState : this.currentStates) {
			
			// Get states to transit
			final Set<State> statesToTransit = this.automate.getTransitionTable().getTransitionWithEpsilon(currentState, input);
			
			// Call on entry callback for each states which is to become current state
			// Do checks to find end states
			for (final State transitState : statesToTransit) {
				
				if (transitState.getOnEntryCallback() != null) {
					transitState.getOnEntryCallback().run();
				}
				
				if (this.automate.getEndStateNames().contains(transitState.getName())) {
					endStates.add(transitState);
				}
			}
			
			// If no states where to transit are found stay in current state
			// or go to error state depending on configuration
			if (statesToTransit.isEmpty()) {
				
				if (this.configuration.allowToStayInStateOnUpdate) {
					// Call on stay callback
					if (currentState.getOnStayCallback() != null) {
						currentState.getOnStayCallback().run();
					}
					
					// Add to next current state
					nextCurrentStates.add(currentState);
					
					continue;
				}
				
				else {
					// Call on exit callback
					if (currentState.getOnExitCallback() != null) {
						currentState.getOnExitCallback().run();
					}
					
					final State errorState = this.automate.getErrorState();
					if (errorState.getOnEntryCallback() != null) {
						errorState.getOnEntryCallback().run();
					}
					
					nextCurrentStates.add(errorState);
				}
			}
			
			// If has one transition then can transit
			else if (statesToTransit.size() == 1) {
				// Call on exit callback
				if (currentState.getOnExitCallback() != null) {
					currentState.getOnExitCallback().run();
				}
				
				nextCurrentStates.add(statesToTransit.iterator().next());
			}
			
			// If has many possible transitions transit to all if is nondeterministic
			// otherwise go to error state
			else {
				// Call on exit callback
				if (currentState.getOnExitCallback() != null) {
					currentState.getOnExitCallback().run();
				}
				
				if (this.configuration.allowToTransitToMultipleStates) {
					nextCurrentStates.addAll(statesToTransit);
				}
				
				else {
					final State errorState = this.automate.getErrorState();
					if (errorState.getOnEntryCallback() != null) {
						errorState.getOnEntryCallback().run();
					}
					
					nextCurrentStates.add(errorState);
				}
			}
		}
		
		// Update current states
		this.currentStates.clear();
		this.currentStates.addAll(nextCurrentStates);
		
		// Update encountered end states
		if (!endStates.isEmpty()) {
			this.encounteredEndStates.add(endStates);
		}
		
		return !this.isAtEnd();
	}
	
	public boolean isAtEnd() {
		// If only error state is left execution is done
		if (this.currentStates.size() == 1 && this.currentStates.contains(this.automate.getErrorState())) {
			return true;
		}
		
		// Otherwise is determined if end state was found
		if (this.configuration.exitWhenEndStateIsEncountered) {
			return !this.encounteredEndStates.isEmpty();
		} 
		
		else {
			// If end states are treated as normal states then return false if all states are not in error state
			return false;
		}
	}
}
