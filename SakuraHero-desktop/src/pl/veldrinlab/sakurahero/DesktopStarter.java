package pl.veldrinlab.sakurahero;


import pl.veldrinlab.sakuraEngine.core.Configuration;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter {

	public static void main(String[] args) {
	
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();	
		config.width = Configuration.getWidth();
		config.height = Configuration.getHeight();
		config.title = Configuration.getWindowTitle();
		config.useGL20 = true;
		config.fullscreen = false;
		config.addIcon("sakuraFlower.png", FileType.Internal);
		
		new LwjglApplication(new SakuraHero(), config);		
	}
}
