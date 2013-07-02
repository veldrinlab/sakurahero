package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "SakuraHero - WIP";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 480;
		
		cfg.addIcon("icon.png", FileType.Internal);
		new LwjglApplication(new SakuraHero(), cfg);
	}
}
