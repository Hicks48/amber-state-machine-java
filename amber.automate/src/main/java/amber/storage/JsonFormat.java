package amber.storage;

import com.google.gson.Gson;

public abstract class JsonFormat {
	
	protected static final Gson JSON = new Gson();
	
	public String toJson() {
		return JSON.toJson(getClass());
	}
}
