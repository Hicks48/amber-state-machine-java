package amber.common;

public abstract class Callback {
		
	protected final String name;
	
	public Callback(final String name) {
		this.name = name;
	}
	
	public abstract void run();
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) {
			return false;
		}
		
		else if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		Callback other = (Callback)obj;
		
		return this.getName().equals(other.getName());
	}
}
