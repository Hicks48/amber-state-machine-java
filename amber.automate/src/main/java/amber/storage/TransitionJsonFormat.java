package amber.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import amber.automate.State;
import amber.common.Pair;
import amber.input.Input;

public class TransitionJsonFormat extends JsonFormat {

	public static TransitionJsonFormat transitionToJson(final Pair<State, Input> stateInputPair, final Set<State> targetStates) {
		final List<String> transitionStates = new ArrayList<String>();
		for (final State targetState : targetStates) {
			transitionStates.add(targetState.getName());
		}
		
		return new TransitionJsonFormat(stateInputPair.getFirst().getName(), InputJsonFormat.inputToJsonFormat(stateInputPair.getSecond()), transitionStates);
	}
	
	protected final String state_name;
	protected final InputJsonFormat input;
	
	protected final List<String> transition_states;

	public TransitionJsonFormat(final String state_name, final InputJsonFormat input, final List<String> transition_states) {
		this.state_name = state_name;
		this.input = input;
		this.transition_states = transition_states;
	}

	public String getState_name() {
		return state_name;
	}

	public InputJsonFormat getInput() {
		return input;
	}

	public List<String> getTransition_states() {
		return transition_states;
	}
}
