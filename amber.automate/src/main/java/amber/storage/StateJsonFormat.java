package amber.storage;

import amber.automate.State;

public class StateJsonFormat extends JsonFormat {
	
	public static StateJsonFormat stateToJsonFormat(final State state) {
		String callbackName = "none";
		
		if (state.getOnEntryCallback() != null) {
			callbackName = state.getOnEntryCallback().getName();
		}
		
		return new StateJsonFormat(state.getName(), callbackName);
	}
	
	protected final String name;
	protected final String callback_name;
	
	public StateJsonFormat(final String name, final String callback_name) {
		this.name = name;
		this.callback_name = callback_name;
	}

	public String getName() {
		return name;
	}

	public String getCallback_name() {
		return callback_name;
	}
}
