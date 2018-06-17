package net.upd4ting.gameapi.nms;

/**
 * This class is here to handle every nms module
 * @author Upd4ting
 */
public class ModuleNMS<T> {
	
	private final String mainPath;
	
	public ModuleNMS(String mainPath) {
		this.mainPath = mainPath;
	}

	public String getMainPath() {
		return mainPath;
	}
}
