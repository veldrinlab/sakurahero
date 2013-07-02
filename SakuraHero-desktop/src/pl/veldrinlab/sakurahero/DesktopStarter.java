package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter {

	public static void main(String[] args) {

		//TODO config from json file???
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Configuration.getInstance().width;
		config.height = Configuration.getInstance().height;
		config.title = Configuration.getInstance().windowTitle;
		config.useGL20 = Configuration.getInstance().useGL20;
		config.fullscreen = Configuration.getInstance().fullScreenEnabled;	
				
		//TODO extend Configuration
		config.addIcon("icon.png", FileType.Internal);
		new LwjglApplication(new SakuraHero(), config);		
	}
}
