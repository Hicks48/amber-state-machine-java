package amber.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import amber.automate.Automate;
import amber.automate.AutomateBuilder;
import amber.automate.State;
import amber.automate.TransitionTable;
import amber.common.CallbackMapper;
import amber.common.Pair;
import amber.input.Input;

public class AutomateJsonFormat extends JsonFormat {
	
	public static AutomateJsonFormat automateToJsonFormat(final Automate automate) {
		// States and callbacks
		final Set<State> states = new HashSet<State>();
		states.addAll(automate.getTransitionTable().getSourceStates());
		states.addAll(automate.getTransitionTable().getTargetStates());
		
		final List<StateJsonFormat> statesJsonFormat = new ArrayList<StateJsonFormat>();
		for (final State state : states) {
			statesJsonFormat.add(StateJsonFormat.stateToJsonFormat(state));
		}
		
		// Transitions
		final List<TransitionJsonFormat> transitionsJsonFormat = new ArrayList<TransitionJsonFormat>();
		
		final TransitionTable transitionTable = automate.getTransitionTable();
		for (final State sourceState : transitionTable.getSourceStates()) {
			
			final Map<Pair<State, Input>, Set<State>> transitionsForState = transitionTable.getAllTransitionsForState(sourceState);
			for (final Pair<State, Input> stateInputPair : transitionsForState.keySet()) {
				transitionsJsonFormat.add(TransitionJsonFormat.transitionToJson(stateInputPair, transitionsForState.get(stateInputPair)));
			}
		}
		
		// Start state name
		final String startStateName = automate.getStartState().getName();
		
		// Error state
		final StateJsonFormat errorStateJsonFormat = StateJsonFormat.stateToJsonFormat(automate.getErrorState());
		
		return new AutomateJsonFormat(statesJsonFormat, transitionsJsonFormat, startStateName, errorStateJsonFormat);
	}
	
	public static Automate loadAutomate(final AutomateJsonFormat automateJsonFormat) {
		final AutomateBuilder automateBuilder = AutomateBuilder.createAutomateBuilder();
		
		// States and callbacks
		final CallbackMapper callbackMapper = CallbackMapper.get();
		for (final StateJsonFormat stateJson : automateJsonFormat.getStates()) {
			automateBuilder.addState(State.createState(stateJson.getName(), callbackMapper.getCallBackForName(stateJson.getCallback_name())));
		}
		
		// Transitions
		for (final TransitionJsonFormat transitionJson : automateJsonFormat.getTransitions()) {
			// Construct input
			//Input input;
			
			//final InputJsonFormat inputJson = transitionJson.getInput();
			
			/*try {
				final Class<?> inputClass = Class.forName(inputJson.getClass_name());
				input = (Input)inputClass.getConstructor(InputJsonFormat.class).newInstance(inputJson);
			} catch (Exception e) {
				throw new IllegalArgumentException("Couldn't create input class: " + inputJson.toString());
			}*/
			
			// Add transition
			automateBuilder.addTransition(transitionJson.getState_name(), InputJsonFormat.loadInput(transitionJson.getInput()), transitionJson.getTransition_states());
		}
		
		// Start state
		automateBuilder.setStartState(automateJsonFormat.getStart_state());
		
		// Error state
		final StateJsonFormat errorStateFormat = automateJsonFormat.getError_state();
		automateBuilder.addState(State.createState(errorStateFormat.getName(), callbackMapper.getCallBackForName(errorStateFormat.getCallback_name())));
		automateBuilder.setErrorState(errorStateFormat.getName());
		
		return automateBuilder.build();
	}
	
	protected final List<StateJsonFormat> states;
	protected final List<TransitionJsonFormat> transitions;
	
	protected final String start_state;
	protected final StateJsonFormat error_state;
	
	public AutomateJsonFormat(final List<StateJsonFormat> states, final List<TransitionJsonFormat> transitions,
			final String start_state, final StateJsonFormat error_state) {
		this.states = states;
		this.transitions = transitions;
		this.start_state = start_state;
		this.error_state = error_state;
	}

	public List<StateJsonFormat> getStates() {
		return states;
	}
	
	public List<TransitionJsonFormat> getTransitions() {
		return transitions;
	}

	public String getStart_state() {
		return start_state;
	}

	public StateJsonFormat getError_state() {
		return error_state;
	}
}
