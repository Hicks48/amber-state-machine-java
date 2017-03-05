package amber.automate;

/**
 * This class presents configuration which can be used with execution class to 
 * configure execution process.
 * 
 * @author Hicks48
 *
 */
public class ExecutionConfiguration {
	
	/**
	 * Returns default configuration.
	 * 
	 * Default configuration:
	 * Allow to stay in state on update = false,
	 * Exit when end state is encountered = false.
	 * 
	 * @return Default configuration.
	 */
	public static ExecutionConfiguration getDefaultConfiguration() {
		return new ExecutionConfiguration();
	}
	
	protected boolean allowToTransitToMultipleStates = true;
	
	/**
	 * Determines if current state is allowed to stay in same state (meaning not to transit) on update.
	 * If this situation occurs and this is set to false. Then execution moves to error state.
	 */
	protected boolean allowToStayInStateOnUpdate = false;
	
	/**
	 * Determines if execution is ended immediately when one of the end states is encountered.
	 */
	protected boolean exitWhenEndStateIsEncountered = false;
	
	public boolean isAllowToTransitToMultipleStates() {
		return allowToTransitToMultipleStates;
	}

	public boolean isAllowToStayInStateOnUpdate() {
		return allowToStayInStateOnUpdate;
	}

	public boolean isExitWhenEndStateIsEncountered() {
		return exitWhenEndStateIsEncountered;
	}

	public void setAllowToTransitToMultipleStates(boolean allowToTransitToMultipleStates) {
		this.allowToTransitToMultipleStates = allowToTransitToMultipleStates;
	}

	public void setAllowToStayInStateOnUpdate(boolean allowToStayInStateOnUpdate) {
		this.allowToStayInStateOnUpdate = allowToStayInStateOnUpdate;
	}

	public void setExitWhenEndStateIsEncountered(boolean exitWhenEndStateIsEncountered) {
		this.exitWhenEndStateIsEncountered = exitWhenEndStateIsEncountered;
	}
}
