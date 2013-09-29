package pl.veldrinlab.sakurahero;

public class GameOptions {
	
	public Language language;
	public SakuraGameMode mode;
	public String worldName;
	public boolean soundOn;
	public boolean musicOn;

	public GameOptions() {
		language = Language.ENGLISH;
		mode = SakuraGameMode.NORMAL;
		worldName = "natsuBackground";
		musicOn = soundOn = true;
	}
}
