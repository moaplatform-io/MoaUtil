package com.moaplanet.util.property;

import java.util.Map;
import java.util.Properties;

public interface PropertyStrategy {
	public Map<String, String> makePropertyMap(Properties properties);
}
