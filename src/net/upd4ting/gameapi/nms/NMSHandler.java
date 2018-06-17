package net.upd4ting.gameapi.nms;

import java.util.HashMap;
import java.util.Map;

import net.upd4ting.gameapi.GameAPI;

/**
 * Class that is handling nms module!
 * @author Upd4ting
 *
 */
public class NMSHandler {
	
	private static String NMS_VERSION = "";
	private static final Map<String, ModuleNMS<?>> modules = new HashMap<>();
	private static final Map<String, Object> caches = new HashMap<>();
	
	/**
	 * Initializing the class
	 */
	public static void init() {
	    String packageName = GameAPI.getInstance().getServer().getClass().getPackage().getName();
	    NMS_VERSION =  packageName.substring(packageName.lastIndexOf('.') + 1);
	}
	
	/**
	 * Register an NMS module
	 * @param mainPath The path to module
	 * @param name Name of the module
	 * @param typeClass The type of class
	 */
	public static <T> void registerModule(String mainPath, String name, T typeClass) {
		modules.put(name, new ModuleNMS<T>(mainPath));
	}
	
	/**
	 * Get an NMS module by his name
	 * @param name Name of the NMS module we want
	 * @return The module or @null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getModule(String name) {
		try {
			if (caches.containsKey(name))
				return (T) caches.get(name);
			else if (modules.containsKey(name)) {
				ModuleNMS<T> module = (ModuleNMS<T>) modules.get(name);
				String mainPath = module.getMainPath();
				T object = (T) Class.forName(mainPath + "." + NMS_VERSION + "." + name).newInstance();
				caches.put(name, object);
				return object;
			} else
				return null;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get nms version of the server
	 * @return The NMS version.
	 */
	public static String getNmsVersion() {
		return NMS_VERSION;
	}
	
	/**
	 * Get if we have to use basic sound or not
	 * @return The value
	 */
	public static Boolean isBasicSound() {
		return NMS_VERSION.startsWith("v1_8") || NMS_VERSION.startsWith("v1_7");
	}
}
