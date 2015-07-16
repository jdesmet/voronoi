/*
 * Created on Oct 14, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package desmet.yo.voronoi.common;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jd3714
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Config {
	private Properties props;
	private String configName;
	
	static public final String DEFAULT = "default";
	static public final String DEFAULTPFX = DEFAULT+".";

	/**
	 * @throws IOException
	 * 
	 */
	public Config(String resource) throws IOException {
		super();
		setConfigName(DEFAULT);
		props = new Properties();
        props.load(Config.class.getResourceAsStream(resource));
	}
	
	public String getProperty(String property) 
	throws ConfigError 
	{
		String prop = null;
	    if (prop == null) prop = props.getProperty(property); // override confignames
	    if (prop == null) prop = props.getProperty(configName+"."+property);
	    if (prop == null) prop = props.getProperty(DEFAULTPFX+property); // default fallback
	    if (prop == null) throw new ConfigError("Could not find "+configName+"."+property);
	    
	    Pattern pat;
	    Matcher mat;
	
	    // Finding String replacements, gets quoted with single
	    // quotes or gets the NULL value without quotes in case
	    // variable is null.
	    pat = Pattern.compile("\\$\\{([a-zA-Z0-9_\\.]+)\\}");
	    mat = pat.matcher(prop);
	    while (mat.find()) {
	      String varValue = getProperty(mat.group(1));
	      prop = prop.replaceAll("\\$\\{"+mat.group(1).replaceAll("\\.","\\\\\\.")+"\\}",varValue==null?"":varValue);
	    }
	    
	    return prop;
	}

	public double getPropertyDouble(String property) 
	throws ConfigError 
	{
	    String prop = getProperty(property);
	    double propd;
	    try {
	        propd = Double.parseDouble(prop);
	    } catch (NumberFormatException x) {
	        throw new ConfigError("Not a double format: "+configName+"."+property,x);
	    }
	    return propd;
	}

	public int getPropertyInt(String property) 
	throws ConfigError 
	{
	    String prop = getProperty(property);
	    int propi;
	    try {
	        propi = Integer.parseInt(prop);
	    } catch (NumberFormatException x) {
	        throw new ConfigError("Not an int format: "+configName+"."+property,x);
	    }
	    return propi;
	}

//	protected String [] getStrings(String name)
//	throws ConfigError
//	{
//	    String stringlist = props.getProperty(name);
//	    if (stringlist == null) throw new ConfigError("Could not find "+name);
//	    String [] strings = stringlist.split(",");
//	    return strings;
//	}

	public String [] getStrings(String name) 
	throws ConfigError
	{
	    String stringlist = getProperty(name);
	    String [] strings = stringlist.split(",");
	    return strings;
	}

	public static class ConfigError extends Exception {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 400084591928063862L;

		/** Creates a new instance of LinkBudgetError */
	    public ConfigError() {
	    }
	
	    public ConfigError(String message) {
	        super(message);
	    }
	
	    public ConfigError(String message,Throwable cause) {
	        super(message, cause);
	    }
	
	    public ConfigError(Throwable cause) {
	        super(cause);
	    }
	}
	/**
	 * @return Returns the configName.
	 */
	public String getConfigName() {
		return configName;
	}
	/**
	 * @param configName The configName to set.
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public java.awt.Color getColor(String property) 
	throws ConfigError 
	{
	    String colorName = getProperty(property);
	    if (colorName == null) return java.awt.Color.black;
	    if (colorName.equalsIgnoreCase("black"))
	        return java.awt.Color.black;
	    else if (colorName.equalsIgnoreCase("blue"))
	        return java.awt.Color.blue;
	    else if (colorName.equalsIgnoreCase("cyan"))
	        return java.awt.Color.cyan;
	    else if (colorName.equalsIgnoreCase("darkgray"))
	        return java.awt.Color.darkGray;
	    else if (colorName.equalsIgnoreCase("darkgrey"))
	        return java.awt.Color.darkGray;
	    else if (colorName.equalsIgnoreCase("gray"))
	        return java.awt.Color.gray;
	    else if (colorName.equalsIgnoreCase("grey"))
	        return java.awt.Color.gray;
	    else if (colorName.equalsIgnoreCase("green"))
	        return java.awt.Color.green;
	    else if (colorName.equalsIgnoreCase("lightgray"))
	        return java.awt.Color.lightGray;
	    else if (colorName.equalsIgnoreCase("lightgrey"))
	        return java.awt.Color.lightGray;
	    else if (colorName.equalsIgnoreCase("magenta"))
	        return java.awt.Color.magenta;
	    else if (colorName.equalsIgnoreCase("orange"))
	        return java.awt.Color.orange;
	    else if (colorName.equalsIgnoreCase("pink"))
	        return java.awt.Color.pink;
	    else if (colorName.equalsIgnoreCase("red"))
	        return java.awt.Color.red;
	    else if (colorName.equalsIgnoreCase("yellow"))
	        return java.awt.Color.yellow;
	    else {
	        try { return java.awt.Color.decode(colorName); }
	        catch (NumberFormatException x) {}
	    }
	    return java.awt.Color.black;
	}
}
