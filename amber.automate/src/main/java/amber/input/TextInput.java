package amber.input;

public class TextInput implements Input {
	
	private static final long serialVersionUID = 1L;
	
	protected String text;
	
	public TextInput(final TextInputJsonObject inputJson) {
		this(inputJson.getText());
	}
	
	public TextInput(final String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	@Override
	public int hashCode() {
		return this.text.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		
		return this.text.equals(((TextInput)obj).text);
	}

	public Object toJsonObject() {
		final TextInputJsonObject jsonObject = new TextInputJsonObject();
		jsonObject.setText(this.text);
		return jsonObject;
	}

	public Class<?> getJsonObjectClass() {
		return TextInputJsonObject.class;
	}
	
	protected class TextInputJsonObject {
		
		protected String text;
		
		public String getText() {
			return this.text;
		}
		
		public void setText(final String text) {
			this.text = text;
		}
	}
}
