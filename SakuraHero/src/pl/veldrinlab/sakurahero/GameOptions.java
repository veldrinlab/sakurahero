package pl.veldrinlab.sakurahero;

public class GameOptions {
	
	public Language language;
	public SakuraGameMode mode;
	public String worldName;
	
	//TODO change to volume
	public boolean soundOn;
	public boolean musicOn;
	
	public GameOptions() {
		language = Language.ENGLISH;
		mode = SakuraGameMode.NORMAL;
		worldName = "natsuBackground";
		musicOn = soundOn = true;
	}
}
