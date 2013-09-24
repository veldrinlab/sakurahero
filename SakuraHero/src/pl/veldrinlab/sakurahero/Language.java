package pl.veldrinlab.sakurahero;

public enum Language {
	
	ENGLISH("englishAtlas"),
	JAPANESE("japaneseAtlas");
	
	private String textureAtlas;
	
	private Language(final String atlas) {
		this.textureAtlas = atlas;
	}
	
	public String getTextureAtlas() {
		return textureAtlas;
	}
}
