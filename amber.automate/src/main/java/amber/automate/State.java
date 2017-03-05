package amber.automate;

import amber.common.Callback;

/**
 * Presents single state in an automate.
 * @author Hicks48
 *
 */
public class State {
	
	/**
	 * Creates a new state with given name. Automate can only contain one state with the same name.
	 * @param name Name for the state.
	 * @return State which was created.
	 */
	public static State createState(final String name) {
		return new State(name, null, null, null);
	}
	
	/**
	 * Creates a new state with given name and callback. Automate can only contain one state with the same name.
	 * @param name Name for the state.
	 * @param onEntry Callback for state being created.
	 * @return State which was created.
	 */
	public static State createState(final String name, final Callback onEntry) {
		return new State(name, onEntry, null, null);
	}
	
	public static State createState(final String name, final Callback onEntry, final Callback onExit) {
		return new State(name, onEntry, onExit, null);
	}
	
	public static State createState(final String name, final Callback onEntry, final Callback onExit, final Callback onStay) {
		return new State(name, onEntry, onExit, onStay);
	}
	
	/**
	 * Name for the state. Works as an id for the state inside an automate. 
	 * Many states can have same name but all states in an automate have unique name.
	 */
	protected final String name;
	
	/**
	 * Callback for the state which is called when state becomes current state in execution.
	 */
	protected Callback onEntry;
	
	/**
	 * Callback for the state which is called when state exits in current execution.
	 */
	protected Callback onExit;
	
	/**
	 * Callback for the state which is called when state stays as current state. Requires that allow to stay in same state is on in execution.
	 */
	protected Callback onStay;
	
	/**
	 * Creates new state object with given name and callback.
	 * @param name Name for the state.
	 * @param onEntry Callback for the state.
	 */
	protected State(final String name, final Callback onEntry, final Callback onExit, final Callback onStay) {
		this.name = name;
		this.onEntry = onEntry;
		this.onExit = onExit;
		this.onStay = onStay;
	}
	
	/**
	 * Returns callback of this state.
	 * @return Callback for this state.
	 */
	public Callback getOnEntryCallback() {
		return this.onEntry;
	}
	
	/**
	 * Set callback for this state.
	 * @param callback Callback for this state.
	 */
	public void setOnEntryCallback(final Callback callback) {
		this.onEntry = callback;
	}
	
	public Callback getOnExitCallback() {
		return this.onExit;
	}
	
	public void setOnExitCallback(final Callback callback) {
		this.onExit = callback;
	}
	
	public Callback getOnStayCallback() {
		return this.onStay;
	}
	
	public void setOnStayCallback(final Callback callback) {
		this.onStay = callback;
	}
	
	/**
	 * Get name of this state.
	 * @return Name of this state.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Constructs string presentation for state which is form: "State: name".
	 */
	@Override
	public String toString() {
		return "State:" + this.name;
	}
	
	/**
	 * Hash code is generated using the name attribute.
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	/**
	 * Objects are being compared using the name attribute.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) {
			return false;
		}
		
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		State other = (State)obj;
		
		return other.name.equals(this.name);
	}
}
