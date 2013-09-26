package pl.veldrinlab.sakurahero;

public class GameOptions {
	
	public Language language;
	public SakuraHeroMode mode;
	public String worldName;
	public boolean soundOn;
	public boolean musicOn;

	public GameOptions() {
		language = Language.ENGLISH;
		mode = SakuraHeroMode.NORMAL;
		worldName = "natsu";
		musicOn = soundOn = true;
	}
}
