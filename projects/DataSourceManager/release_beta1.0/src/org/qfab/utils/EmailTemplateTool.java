package org.qfab.utils;
import java.util.Iterator;
import java.util.Map;


public class EmailTemplateTool {
	
	/**
	 * Replaces a simple marker with the supplied value.
	 * Markers must be upper-case and must appear in
	 * templates embraced by three hash signs, e.g. ###MARKER###
	 * @param template - text template
	 * @param marker - marker name, e.g Marker
	 * @param value - value to be replaced
	 * @return
	 */
	public static String replace(String template, String marker, String value) {
		if (value == null) value = "";
		return template.replaceAll("###"+marker.toUpperCase()+"###", value);
	}
	
	/**
	 * Replaces multiple simple tag markers.
	 * Simple tag markers must be upper-case. The must appear in
	 * templates embraced by three hash signs, e.g. ###MARKER###
	 * @param template - text template
	 * @param markers - markers map
	 * @return - replaced template text
	 */
	public static String replace(String template, Map markers) {
		if (template == null) return null;
		
		// Iterate while there are changes
		// Necessary due to unpredictable iterating order
		while (true) {
			String pre = template;
			Iterator i = markers.keySet().iterator();
			while (i.hasNext()) {
				String marker = (String)i.next();
				Object value  = markers.get(marker);
				if (value == null) value = "";
				template = template.replaceAll("###"+marker.toUpperCase()+"###", value.toString());
			}
			if (pre.equals(template)) break;
		}
		return template;
	}
	

}
