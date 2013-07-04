package pl.veldrinlab.sakurahero;

//TODO refactor
public class Configuration { 
	
	// Application Options
	
	public ConfigurationDescriptor descriptor;
	
	// Game Options
	
	public boolean soundOn;
	public boolean musicOn;

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
	}

	private static class ConfigurationHolder { 
		private static final Configuration instance = new Configuration();
	}

	public static Configuration getInstance() {
		return ConfigurationHolder.instance;
	}
	
	
	//TODO API for important data
}
