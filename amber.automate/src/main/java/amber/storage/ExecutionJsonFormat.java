package amber.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import amber.automate.Execution;
import amber.automate.ExecutionBuilder;
import amber.automate.State;

public class ExecutionJsonFormat extends JsonFormat {

	public static ExecutionJsonFormat executionToJsonFormat(final Execution execution) {
		final ExecutionJsonFormat executionJsonFormat = new ExecutionJsonFormat();
		
		// Automate
		executionJsonFormat.setAutomate(AutomateJsonFormat.automateToJsonFormat(execution.getAutomate()));
		
		// Current states
		final List<StateJsonFormat> currentStatesJsonFormat = new ArrayList<StateJsonFormat>();
		for (final State currentState : execution.getCurrentStates()) {
			currentStatesJsonFormat.add(StateJsonFormat.stateToJsonFormat(currentState));
		}
		
		executionJsonFormat.setCurrent_states(currentStatesJsonFormat);
		
		// Encountered end states
		final List<List<StateJsonFormat>> encounteredEndStates = new ArrayList<List<StateJsonFormat>>();
		for (final Set<State> endStatesEncounteredAtTime : execution.getEncounteredEndStates()) {
			
			final List<StateJsonFormat> endStatesEncounteredAtTimeJsonFormat = new ArrayList<StateJsonFormat>();
			for (final State endState : endStatesEncounteredAtTime) {
				endStatesEncounteredAtTimeJsonFormat.add(StateJsonFormat.stateToJsonFormat(endState));
			}
			
			encounteredEndStates.add(endStatesEncounteredAtTimeJsonFormat);
		}
		
		executionJsonFormat.setEncountered_end_states(encounteredEndStates);
		
		// Configuration
		executionJsonFormat.setConfiguration(ExecutionConfigurationJsonFormat.executionConfigurationToJsonFormat(execution.getConfiguration()));
		
		return executionJsonFormat;
	}
	
	public static Execution loadExecution(final ExecutionJsonFormat executionJsonFormat) {
		final ExecutionBuilder builder = ExecutionBuilder.createExecutionBuilder();
		
		// Automate
		builder.setAutomate(AutomateJsonFormat.loadAutomate(executionJsonFormat.getAutomate()));
		
		// Configuration
		builder.setConfiguration(ExecutionConfigurationJsonFormat.loadExecutionConfiguration(executionJsonFormat.getConfiguration()));
		
		return builder.build();
	}
	
	protected List<StateJsonFormat> current_states;
	
	protected List<List<StateJsonFormat>> encountered_end_states;
	
	protected ExecutionConfigurationJsonFormat configuration;
	
	protected AutomateJsonFormat automate;

	public List<StateJsonFormat> getCurrent_states() {
		return current_states;
	}

	public void setCurrent_states(List<StateJsonFormat> current_states) {
		this.current_states = current_states;
	}

	public List<List<StateJsonFormat>> getEncountered_end_states() {
		return encountered_end_states;
	}

	public void setEncountered_end_states(List<List<StateJsonFormat>> encountered_end_states) {
		this.encountered_end_states = encountered_end_states;
	}

	public ExecutionConfigurationJsonFormat getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ExecutionConfigurationJsonFormat configuration) {
		this.configuration = configuration;
	}

	public AutomateJsonFormat getAutomate() {
		return automate;
	}

	public void setAutomate(AutomateJsonFormat automate) {
		this.automate = automate;
	}
}
