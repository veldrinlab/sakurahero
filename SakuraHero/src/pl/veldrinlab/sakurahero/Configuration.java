package pl.veldrinlab.sakurahero;

public class Configuration{ 
	
	// Application Options
	
	public int width;
	public int height;
	public boolean useGL20;
	public boolean fullScreenEnabled;
	public String windowTitle;
	public String resourcePath;
	
	// Game Options
	
	public boolean soundOn;
	public boolean musicOn;

	//TODO mo¿e spróbowaæ to jednak wrzuciæ do pliku? - jakiœ JSON w main by wystarczy³
	private Configuration() {
		width = 800;
		height = 480;
		useGL20 = true;
		fullScreenEnabled = false;
		windowTitle = "Sakura Hero - WIP";
		resourcePath = "resources.json";
		musicOn = soundOn = true;
	}

	private static class ConfigurationHolder { 
		private static final Configuration instance = new Configuration();
	}

	public static Configuration getInstance() {
		return ConfigurationHolder.instance;
	}
}
