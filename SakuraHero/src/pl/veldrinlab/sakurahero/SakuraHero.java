package pl.veldrinlab.sakurahero;

import pl.veldrinlab.sakurahero.screens.CreditsScreen;
import pl.veldrinlab.sakurahero.screens.GameOverScreen;
import pl.veldrinlab.sakurahero.screens.LanguageSelectionScreen;
import pl.veldrinlab.sakurahero.screens.LoadingScreen;
import pl.veldrinlab.sakurahero.screens.MenuScreen;
import pl.veldrinlab.sakurahero.screens.ModeSelectionScreen;
import pl.veldrinlab.sakurahero.screens.PauseScreen;
import pl.veldrinlab.sakurahero.screens.PlayScreen;
import pl.veldrinlab.sakurahero.screens.OptionsScreen;
import pl.veldrinlab.sakurahero.screens.SplashScreen;
import pl.veldrinlab.sakurahero.screens.SurvivalScreen;
import pl.veldrinlab.sakurahero.screens.TrainingScreen;
import pl.veldrinlab.sakurahero.screens.WorldSelectionScreen;
import pl.veldrinlab.sakuraEngine.core.AsyncResourceManager;
import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.fx.FadeEffectParameters;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class SakuraHero extends Game {

	public AsyncResourceManager resources;
	public FallingLeavesEffect fallingSakura;
	public GameOptions options;
	public GameResult results;
	
	public Music menuMusic;
	public Music playMusic;
	public Music gameOverMusic;
	
	private Timer timer;
	
	private SplashScreen teamSplashScreen;
	private SplashScreen engineSplashScreen;
	private LanguageSelectionScreen languageSelectionScreen;
	private LoadingScreen loadingScreen;
	
	private MenuScreen menuScreen;
	private OptionsScreen optionsScreen;
	private CreditsScreen creditsScreen;
	
	private ModeSelectionScreen modeSelectionScreen;
	private WorldSelectionScreen worldSelectionScreen;
	private PlayScreen playScreen;	
	private TrainingScreen trainingScreen;
	private SurvivalScreen survivalScreen;
	
	private PauseScreen pauseScreen;
	private GameOverScreen gameOverScreen;
	
	//private LevelEditorScreen levelEditorScreen;
	
	/**
	 * Class construtor, nothing to do (it is great!).
	 */
	public SakuraHero() {
		
	}
	
	/**
	 * Method is used to create game data. It it executed by Activity. Initialize game data and setup first screen - spin that shit!
	 */
	@Override
	public void create() {		
		timer = new Timer();
		resources = new AsyncResourceManager();
		options = new GameOptions();
		results = new GameResult();
	
		initializeEngine();
		loadHighScore();
	
		initalizeIntro();
	//	initializeLoading();
	//	initializeGame();

		
//		setScreen(trainingScreen);
	//setScreen(playScreen);
//	setScreen(loadingScreen);
//		setScreen(menuScreen);
	//	setScreen(optionsScreen);
		//setScreen(survivalScreen);
	}

	private void initializeEngine() {
		resources.loadResources(Configuration.getInitResourcePath());
		resources.finishLoading();
		
		//
	//	resources.loadResources(Configuration.getResourcePath());
	//	resources.finishLoading();
		
		Renderer.introAtlas = resources.getTextureAtlas("introAtlas");
		Renderer.smallFont = new LabelStyle(resources.getFont("smallFont"),Color.WHITE);
		Renderer.standardFont = new LabelStyle(resources.getFont("standardFont"),Color.WHITE);
		Renderer.specialFont = new LabelStyle(resources.getFont("specialFont"),Color.YELLOW);
		
		fallingSakura = new FallingLeavesEffect(20,true);
		fallingSakura.initializeEffect();
		fallingSakura.setLeavesAlpha(1.0f);
	}
	
	private void initalizeIntro() {
		FadeEffectParameters fadeEffect = new FadeEffectParameters();
		
		fadeEffect.fadeInTime = 1.0f;
		fadeEffect.stayTime = 2.0f;
		fadeEffect.fadeOutTime = 1.0f;
		fadeEffect.skippable = fadeEffect.skippableWhileFadingIn = true;
		
		languageSelectionScreen = new LanguageSelectionScreen(this,fadeEffect);
		
		fadeEffect.textureName = "engineLogo";
		engineSplashScreen = new SplashScreen(this,fadeEffect,languageSelectionScreen);
		
		fadeEffect.textureName = "teamLogo";		
		teamSplashScreen = new SplashScreen(this,fadeEffect,engineSplashScreen);		
		
		setScreen(teamSplashScreen);
	}
	
	public void initializeLoading() {
		Renderer.guiAtlas = resources.getTextureAtlas(options.language.getTextureAtlas());
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}
		
	public void initializeGame() {
		Renderer.sceneAtlas = resources.getTextureAtlas("sceneAtlas");

		menuScreen = new MenuScreen(this);
		optionsScreen = new OptionsScreen(this);	
		creditsScreen = new CreditsScreen(this);
		pauseScreen = new PauseScreen(this);
		gameOverScreen = new GameOverScreen(this);

		modeSelectionScreen = new ModeSelectionScreen(this);
		worldSelectionScreen = new WorldSelectionScreen(this);
		
		playScreen = new PlayScreen(this);
		trainingScreen = new TrainingScreen(this);
		survivalScreen = new SurvivalScreen(this);
		//levelEditorScreen = new LevelEditorScreen(this);
		
		buildGameStateGraph();
		
		setScreen(menuScreen);
	}
	
	public void buildGameStateGraph() {
		menuScreen.modeSelectionScreen = modeSelectionScreen;
		menuScreen.creditsnScreen= creditsScreen;
		menuScreen.optionsScreen = optionsScreen;
		
		creditsScreen.menuScreen = menuScreen;
		optionsScreen.menuScreen = menuScreen;
		
		modeSelectionScreen.menuScreen = menuScreen;
		modeSelectionScreen.worldSelectionScreen = worldSelectionScreen;
		modeSelectionScreen.survivalScreen = survivalScreen;
		modeSelectionScreen.trainingScreen = trainingScreen;
		
		worldSelectionScreen.modeSelectionScreen = modeSelectionScreen;
		worldSelectionScreen.playScreen = playScreen;
		worldSelectionScreen.survivalScreen = survivalScreen;
		
		playScreen.pauseScreen = pauseScreen;
		playScreen.gameOverScreen = gameOverScreen;
		
		survivalScreen.pauseScreen = pauseScreen;
		survivalScreen.gameOverScreen = gameOverScreen;
		
		trainingScreen.pauseScreen = pauseScreen;
		
		pauseScreen.menuScreen = menuScreen;
		gameOverScreen.menuScreen = menuScreen;		
	}
	
	public void loadHighScore() {
		Json json = new Json();
		FileHandle file = Gdx.files.local("results.json");
	
		if(file.exists()) {
			String jsonData = file.readString();
			jsonData = Base64Coder.decodeString(jsonData);

			try {
				results = json.fromJson(GameResult.class, jsonData);			
			} catch(Exception e ) {
			
				Gdx.app.log("SakuraHero ","results file results.json loading exception");
				e.printStackTrace();
			}
		}
		else {
			createHighscoreFile();
			loadHighScore();
		}
	}
	
	public void saveHighScore() {
		Json json = new Json();		
		FileHandle file = Gdx.files.local("results.json");

		if(file.exists()) {
			String jsonData = json.toJson(results);
			jsonData = Base64Coder.encodeString(jsonData);
			file.writeString(jsonData, false);
		}
		else {
			createHighscoreFile();
			Gdx.app.log("SakuraHero ","results file results.json opening error detected! exception");
		}
	}
	
	private void createHighscoreFile() {	
		Json json = new Json();		
		FileHandle file = Gdx.files.local("results.json");
		String jsonData = json.toJson(results);
		
		jsonData = Base64Coder.encodeString(jsonData);
		file.writeString(jsonData, false);
	}

	public void initializeAudio() {
		menuMusic = resources.getMusic("menuMusic");
		playMusic = resources.getMusic("playMusic");
		gameOverMusic = resources.getMusic("gameOverMusic");
		
		menuMusic.setLooping(true);
		playMusic.setLooping(true);
		gameOverMusic.setLooping(true);
		
		menuMusic.setVolume(options.musicVolume);
		playMusic.setVolume(options.musicVolume);
		gameOverMusic.setVolume(options.musicVolume);
	}
	
	public Timer getTimer() {
		return timer;
	}
}

