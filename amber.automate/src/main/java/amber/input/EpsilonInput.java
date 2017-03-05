package amber.input;

public class EpsilonInput implements Input {
	
	private static final long serialVersionUID = 1L;
	
	protected static final EpsilonInput epsilon = new EpsilonInput();
	
	public static EpsilonInput getEpsilonInput() {
		return EpsilonInput.epsilon;
	}
	
	
	protected static final String id = "epsilon";
	
	@Override
	public boolean equals(Object obj) {
		
		if (this.getClass() == obj.getClass()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	public EpsilonInputJsonObject toJsonObject() {
		final EpsilonInputJsonObject jsonObject = new EpsilonInputJsonObject();
		jsonObject.setEpsilon_id(id);
		return jsonObject;
	}

	public Class<?> getJsonObjectClass() {
		return EpsilonInputJsonObject.class;
	}
	
	protected class EpsilonInputJsonObject {
		
		protected String epsilon_id = EpsilonInput.id;
		
		protected String getEpsilon_id() {
			return this.epsilon_id;
		}
		
		protected void setEpsilon_id(final String epsilon_id) {
			this.epsilon_id = epsilon_id;
		}
	}
}
