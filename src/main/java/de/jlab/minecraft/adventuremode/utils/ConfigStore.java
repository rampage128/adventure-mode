package de.jlab.minecraft.mods.adventuremode.utils;

import java.util.HashMap;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public abstract class ConfigStore {

	private Configuration config = null;
	private HashMap<String, Object> configmap = new HashMap<String, Object>();
	
	public ConfigStore(Configuration config) {
		this.config = config;
		this.readConfig();
	}
	
	public abstract void readConfig();
	public abstract String getDefaultCategory();
	
	protected void readItemId(String name, int defaultValue, String description) {
		Property property = this.config.getItem(name, defaultValue, description);
		this.configmap.put(Configuration.CATEGORY_ITEM + "." + name, property.getInt(defaultValue));
	}
	
	protected void readProperty(String category, String name, Object defaultValue, String description) {				
		Property property = null;
		
		if (defaultValue instanceof Integer) {
			property = this.config.get(category, name, (Integer)defaultValue);
	        this.configmap.put(category + "." + name, property.getInt((Integer)defaultValue));
		} else if (defaultValue instanceof Boolean) {
			property = this.config.get(category, name, (Boolean)defaultValue);
	        this.configmap.put(category + "." + name, property.getBoolean((Boolean)defaultValue));
		} else if (defaultValue instanceof Double) {
			property = this.config.get(category, name, (Double)defaultValue);
	        this.configmap.put(category + "." + name, property.getDouble((Double)defaultValue));
		} else if (defaultValue instanceof String) {
			property = this.config.get(category, name, (String)defaultValue);
	        this.configmap.put(category + "." + name, property.getString());
		} else if (defaultValue instanceof int[]) {
			property = this.config.get(category, name, (int[])defaultValue);
	        this.configmap.put(category + "." + name, property.getIntList());
		} else if (defaultValue instanceof String[]) {
			property = this.config.get(category, name, (String[])defaultValue);
	        this.configmap.put(category + "." + name, property.getStringList());
		} else if (defaultValue instanceof double[]) {    
	        property = this.config.get(category, name, (double[])defaultValue);
	        this.configmap.put(category + "." + name, property.getDoubleList());
		} else {
			throw new UnsupportedOperationException("Cannot read property \"" + category + "." + name + "\": Type " + defaultValue.getClass().getName() + " is not supported!");
		}
		
		property.comment = description;
	}
	
	public Object getProperty(String name) {
		return this.getProperty(this.getDefaultCategory(), name);
	}
	
	public Object getProperty(String category, String name) {
		return this.configmap.get(category + "." + name);
	}
	
}