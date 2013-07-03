package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter {

	public static void main(String[] args) {
	
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();	
		config.addIcon("icon.png", FileType.Internal);
		config.useGL20 = true;
		new LwjglApplication(new SakuraHero(), config);		
	}
}
