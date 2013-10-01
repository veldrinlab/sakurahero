package pl.veldrinlab.sakurahero;

public class GameOptions {
	
	public Language language;
	public SakuraGameMode mode;
	public String worldName;
	public float soundVolume;
	public float musicVolume;
	
	public GameOptions() {
		language = Language.ENGLISH;
		mode = SakuraGameMode.NORMAL;
		worldName = "natsuBackground";
		musicVolume = 0.5f;
		soundVolume = 1.0f;
	}
}
