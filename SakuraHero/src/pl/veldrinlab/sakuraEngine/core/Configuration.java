package pl.veldrinlab.sakuraEngine.core;

public class Configuration { 
	
	public int width;
	public int height;
	public boolean useGL20;
	public boolean fullScreenEnabled;
	public String windowTitle;
	public String initResourcePath;
	public String resourcePath;
	
	private Configuration() {
		width = 800;
		height = 480;
		useGL20 = true;
		fullScreenEnabled = false;
		windowTitle = "Sakura Hero";
		resourcePath = "resources.json";
		initResourcePath = "initResources.json";
	}

	private static class ConfigurationHolder { 
		private static final Configuration instance = new Configuration();
	}

	public static Configuration getInstance() {
		return ConfigurationHolder.instance;
	}
	
	public static int getWidth() {
		return getInstance().width;
	}
	
	public static int getHeight() {
		return getInstance().height;
	}
	
	public static boolean isFullscreenEnabled() {
		return getInstance().fullScreenEnabled;
	}
	
	public static String getWindowTitle() {
		return getInstance().windowTitle;
	}
	
	public static String getResourcePath() {
		return getInstance().resourcePath;
	}
	
	public static String getInitResourcePath() {
		return getInstance().initResourcePath;
	}
}
