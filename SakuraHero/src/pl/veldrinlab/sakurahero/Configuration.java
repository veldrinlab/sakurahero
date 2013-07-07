package pl.veldrinlab.sakurahero;

//TODO refactor
public class Configuration { 
	
	// Application Options
	
	public ConfigurationDescriptor descriptor;
	
	// Game Options
	
	private Language language;
	// state
	private boolean soundOn;
	private boolean musicOn;

	private Configuration() {
		descriptor = new ConfigurationDescriptor();
		
		descriptor.width = 800;
		descriptor.height = 480;
		descriptor.useGL20 = true;
		descriptor.fullScreenEnabled = false;
		descriptor.windowTitle = "Sakura Hero - WIP";
		descriptor.resourcePath = "resources.json";
		descriptor.initResourcePath = "initResources.json";
		
		//
		musicOn = soundOn = true;
		language = Language.ENGLISH;
	}

	private static class ConfigurationHolder { 
		private static final Configuration instance = new Configuration();
	}

	// EVIL!
	public static Configuration getInstance() {
		return ConfigurationHolder.instance;
	}
	
	//TODO API for important data
	
	public static int getWidth() {
		return ConfigurationHolder.instance.descriptor.width;
	}
	
	public static int getHeight() {
		return ConfigurationHolder.instance.descriptor.height;
	}
	
	public static boolean isFullscreenEnabled() {
		return ConfigurationHolder.instance.descriptor.fullScreenEnabled;
	}
	
	public static String getWindowTitle() {
		return ConfigurationHolder.instance.descriptor.windowTitle;
	}
	
	public static String getResourcePath() {
		return ConfigurationHolder.instance.descriptor.resourcePath;
	}
	
	public static String getInitResourcePath() {
		return ConfigurationHolder.instance.descriptor.initResourcePath;
	}
	
	public Language getSelectedLanguage() {
		return language;
	}

	public void setLanguage(final Language language) {
		this.language = language;
	}	
}
