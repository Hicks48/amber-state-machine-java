package amber.common;

public class Pair<FirstType, SecondType> {
	
	protected FirstType first;
	protected SecondType second;

	public Pair(final FirstType first, final SecondType second) {
		this.first = first;
		this.second = second;
	}
	
	public FirstType getFirst() {
		return this.first;
	}
	
	public SecondType getSecond() {
		return this.second;
	}

	public void setFirst(FirstType first) {
		this.first = first;
	}

	public void setSecond(SecondType second) {
		this.second = second;
	}
	
	@Override
	public String toString() {
		return "first:" + this.first.toString() + " second:" + this.second.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
			
		if (getClass() != obj.getClass()) {
			return false;
		}
			
		Pair<?, ?> other = (Pair<?, ?>) obj;
		return this.first.equals(other.first) && this.second.equals(other.second);
	}
}
