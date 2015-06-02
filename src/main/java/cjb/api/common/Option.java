package cjb.api.common;

import net.minecraft.client.resources.I18n;
import cjb.api.CJBAPI;


public class Option {   
	private final String mod;
    private final Object defaultValue;
    private final String description;
    private final String propertyname;
    private final Boolean isPacket;
    public Boolean enabled;
    private String[] returnValue = null;
    
    public Option(String mod, String s, Object obj, String s1, Boolean isPacket) {
    	this.mod = mod;
    	this.description = s;
        this.defaultValue = obj;
        this.propertyname = s1;
        this.isPacket = isPacket;
        this.enabled = true;
        this.returnValue = new String[]{"cjb.options.off", "cjb.options.on"};
    }
    
    public Option(String mod, String s, Object obj, String s1, Boolean isPacket, Boolean enabled) {
    	this(mod, s, obj, s1, isPacket);
        this.enabled = enabled;
    }

    public Option(String mod, String s, Object obj, String s1, Boolean isPacket, String [] ss) {
    	this(mod, s, obj, s1, isPacket);
        this.returnValue = ss;
    }
  
    public Object getDefaultValue()
    {
    	if (defaultValue instanceof String) {
    		return defaultValue;
    	}
    	
    	if (defaultValue instanceof Boolean) {
    		if ((Boolean) defaultValue)
    			return 1;
    		else 
    			return 0;
    	} else if (defaultValue instanceof Integer) {
    		return defaultValue;
    	}
    	
        return 0;
    }
    
    public String getDescription() {
    	if (this.description.isEmpty()) {
    		return I18n.format("cjb.options." + propertyname);
    	}
    	return this.description;
    }

    public String getEnumString()
    {
    	if (!this.enabled) {
    		 return "\u00a72\u00a7l" + this.description;
    	}
    	
        return getDescription() + ": " + getValue();
    }
    
    public int getInt() {
    	return CJBAPI.getInt(mod, propertyname, (Integer) getDefaultValue());
    }
    
    public int getMaxValue() {
    	if (returnValue == null)
    		return 1;
    	
    	return returnValue.length;
    }
    
    public String getMod() {
    	return mod;
    }
    
    public String getPropertyName() {
    	return propertyname;
    }
    
    public String getString() {
    	return CJBAPI.getString(mod, propertyname, (String) getDefaultValue());
    }
    
    public String getValue() {
    	if (returnValue == null) {
    		return I18n.format(Integer.toString(CJBAPI.getInt(mod, propertyname, (Integer) getDefaultValue())));
    	}
    	
	    return I18n.format(returnValue[CJBAPI.getInt(mod, propertyname, (Integer) getDefaultValue())]);
    }
    
    public void increase() {
    	CJBAPI.updateInt(mod, propertyname, returnValue.length);
    	CJBAPI.configDirty = true;
    }
    
    public Boolean isPacket() {
    	return this.isPacket;
    }
    
    public boolean isTrue() {
    	return CJBAPI.getInt(mod, propertyname, (Integer) getDefaultValue()) == 1;
    }
    
    public void setInt(int i) {
    	CJBAPI.setInt(mod, propertyname, i);
    	CJBAPI.configDirty = true;
    }
    
    public void setString(String s) {
    	CJBAPI.setString(mod, propertyname, s);
    	CJBAPI.configDirty = true;
    }
}
