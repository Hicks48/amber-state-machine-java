package amber.storage;

import amber.automate.ExecutionConfiguration;

public class ExecutionConfigurationJsonFormat extends JsonFormat {

	public static ExecutionConfigurationJsonFormat executionConfigurationToJsonFormat(final ExecutionConfiguration executionConfiguration) {
		final ExecutionConfigurationJsonFormat executionConfigurationJsonFormat = new ExecutionConfigurationJsonFormat();
		executionConfigurationJsonFormat.setAllow_to_stay_in_state_on_update(executionConfiguration.isAllowToStayInStateOnUpdate());
		executionConfigurationJsonFormat.setAllow_to_transit_to_multiple_states(executionConfiguration.isAllowToTransitToMultipleStates());
		executionConfigurationJsonFormat.setExit_when_end_state_is_encountered(executionConfiguration.isExitWhenEndStateIsEncountered());
		return executionConfigurationJsonFormat;
	}
	
	public static ExecutionConfiguration loadExecutionConfiguration(final ExecutionConfigurationJsonFormat configurationJsonFormat) {
		final ExecutionConfiguration configuration = new ExecutionConfiguration();
		configuration.setAllowToStayInStateOnUpdate(configurationJsonFormat.getAllow_to_stay_in_state_on_update());
		configuration.setAllowToTransitToMultipleStates(configurationJsonFormat.getAllow_to_transit_to_multiple_states());
		configuration.setExitWhenEndStateIsEncountered(configurationJsonFormat.getExit_when_end_state_is_encountered());
		return configuration;
	}
	
	protected Boolean allow_to_transit_to_multiple_states;
	
	protected Boolean allow_to_stay_in_state_on_update;
	
	protected Boolean exit_when_end_state_is_encountered;

	public Boolean getAllow_to_transit_to_multiple_states() {
		return allow_to_transit_to_multiple_states;
	}

	public void setAllow_to_transit_to_multiple_states(final Boolean allow_to_transit_to_multiple_states) {
		this.allow_to_transit_to_multiple_states = allow_to_transit_to_multiple_states;
	}

	public Boolean getAllow_to_stay_in_state_on_update() {
		return allow_to_stay_in_state_on_update;
	}

	public void setAllow_to_stay_in_state_on_update(final Boolean allow_to_stay_in_state_on_update) {
		this.allow_to_stay_in_state_on_update = allow_to_stay_in_state_on_update;
	}

	public Boolean getExit_when_end_state_is_encountered() {
		return exit_when_end_state_is_encountered;
	}

	public void setExit_when_end_state_is_encountered(final Boolean exit_when_end_state_is_encountered) {
		this.exit_when_end_state_is_encountered = exit_when_end_state_is_encountered;
	}
}
