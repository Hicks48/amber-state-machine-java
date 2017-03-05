package amber.automate;

public class ExecutionBuilder {
	
	public static ExecutionBuilder createExecutionBuilder() {
		return new ExecutionBuilder();
	}
	
	protected ExecutionConfiguration configuration;
	
	protected Automate automate;
	
	protected ExecutionBuilder() {
		this.configuration = ExecutionConfiguration.getDefaultConfiguration();
		this.automate = null;
	}
	
	public ExecutionBuilder setAutomate(final Automate automate) {
		this.automate = automate;
		return this;
	}
	
	public ExecutionBuilder setConfiguration(final ExecutionConfiguration configuration) {
		this.configuration = configuration;
		return this;
	}
	
	public Execution build() {
		
		if (this.automate == null) {
			throw new IllegalStateException("Automate wasn't set for execution before trying to build it");
		}
		
		return new Execution(this.automate, this.configuration);
	}
}
