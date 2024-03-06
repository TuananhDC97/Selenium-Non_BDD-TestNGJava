package com.nashtech.automation.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Employee {
	private Map<String,String> attributeMaps;
	
	public Map<String, String> getAttributeMaps() {
		return attributeMaps;
	}

	public Employee(List<String> keys, List<String> values) {
		attributeMaps = new HashMap<>();
		for (int i = 0; i < keys.size(); i++) {
			attributeMaps.put(keys.get(i), values.get(i));
		}	
	}
	
	public Employee(String[] keys, String[] values) {
		attributeMaps = new HashMap<>();
		for (int i = 0; i < keys.length; i++) {
			attributeMaps.put(keys[i], values[i]);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Employee))
			return false;
		Employee other = (Employee) obj;
		Map<String,String> otherAttributeMaps = other.attributeMaps;
		for ( Entry<String, String> entry : otherAttributeMaps.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (!attributeMaps.containsKey(key)) 
				return false;
			if (!(attributeMaps.get(key).equals(value)))
				return false;
		}
		return true;
	}
}
