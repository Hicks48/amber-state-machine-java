package amber.common;

import java.util.HashMap;
import java.util.Map;

public class CallbackMapper {
	
	protected static CallbackMapper callbackMapper;
	
	public static CallbackMapper get() {
		
		if (callbackMapper == null) {
			callbackMapper = new CallbackMapper();
		}
		
		return callbackMapper;
	}
	
	
	protected final Map<String, Callback> callbackMap;
	
	protected CallbackMapper() {
		this.callbackMap = new HashMap<String, Callback>();
	}
	
	public void addCallback(final Callback callback) {
		
		if (this.callbackMap.containsKey(callback.getName())) {
			throw new IllegalStateException("Callback with same name is already in the callback map");
		}
		
		this.callbackMap.put(callback.getName(), callback);
	}
	
	public Callback getCallBackForName(final String name) {
		return this.callbackMap.get(name);
	}
}
