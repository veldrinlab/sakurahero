package pl.veldrinlab.sakurahero;

import pl.veldrinlab.sakurahero.Configuration;
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
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.fx.FadeEffectParameters;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

//TODO mo¿e pozostawiæ opcjê z High Score na przysz³oœæ?
//TODO change SettingsScreen to OptionsScreen

public class SakuraHero extends Game {

	public AsyncResourceManager resources;
	public FallingLeavesEffect fallingSakura; //TODO jakoœ inaczej?
	private Timer timer;

	private SplashScreen teamSplashScreen;
	private SplashScreen engineSplashScreen;
	private LanguageSelectionScreen languageSelectionScreen;
	private LoadingScreen loadingScreen;
	
	private MenuScreen menuScreen;
	private OptionsScreen optionsScreen;
	private CreditsScreen creditsScreen;
	
	// nowe, wybor trybu, jakos ograniczyc pamiec
	private PlayScreen playScreen;
	
	private TrainingScreen trainingScreen;
	private SurvivalScreen survivalScreen;
	
	private ModeSelectionScreen modeSelectionScreen;
	private WorldSelectionScreen worldSelectionScreen;
	
	private PauseScreen pauseScreen;
	private GameOverScreen gameOverScreen;
	
	
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
		// launch engine configuration
			
		// load config
		
//		Json json = new Json();
//		FileHandle file = Gdx.files.local("config.json");
//	
//		if(file.exists()) {
//			String jsonData = file.readString();
//			
//			try {
//				Configuration.getInstance().descriptor = json.fromJson(ConfigurationDescriptor.class, jsonData);			
//			} catch(Exception e ) {
//			
//				Gdx.app.log("Sakura Hero ","Config file loading exception");
//				e.printStackTrace();
//			}
//		}
//		

		Gdx.graphics.setDisplayMode(Configuration.getWidth(), Configuration.getHeight(), Configuration.isFullscreenEnabled());
		Gdx.graphics.setTitle(Configuration.getWindowTitle());
		
		
		// test
//		
//		Json json = new Json();		
//		FileHandle file = Gdx.files.local("config.json");
//
//		if(file.exists()) {
//			String jsonData = json.toJson(Configuration.getInstance());
//			file.writeString(jsonData, false);
//		}
		
		timer = new Timer();
		resources = new AsyncResourceManager();
		
	
		
		initializeEngine();
	//	loadHighScore();
	//	initializeGame();
		initalizeIntro();
	}

	private void initializeEngine() {
		resources.loadResources(Configuration.getInitResourcePath());
		resources.finishLoading();
		
		// test hack!
		resources.loadResources(Configuration.getResourcePath());
		resources.finishLoading();
		
		fallingSakura = new FallingLeavesEffect(20,resources.getTexture("sakuraLeaf"));
		fallingSakura.initializeEffect();
		
		// fonty pewnie kilka rodzajów
		// na pewno zwyk³y na points, fps i credits
		
		
		Renderer.defaultFont = resources.getFont("defaultFont");	
		
		// testable hack
		
		
		initalizeIntro();
		initializeGame();
		
		
		setScreen(teamSplashScreen);
		//setScreen(menuScreen);
		//setScreen(worldSelectionScreen);
	//	setScreen(playScreen);
	//	setScreen(creditsScreen);
	//	setScreen(optionsScreen);
		setScreen(trainingScreen);
//		setScreen(modeSelectionScreen);
///		setScreen(playScreen);
	//	Renderer.defaultShader = resources.getShader("defaultMesh");
	}
	
	private void initalizeIntro() {
		FadeEffectParameters fadeEffect = new FadeEffectParameters();
		
		fadeEffect.fadeInTime = 1.0f;
		fadeEffect.stayTime = 2.0f;
		fadeEffect.fadeOutTime = 1.0f;
		fadeEffect.skippable = fadeEffect.skippableWhileFadingIn = true;
		
		loadingScreen = new LoadingScreen(this);	
		languageSelectionScreen = new LanguageSelectionScreen(this,fadeEffect,loadingScreen);
		
		fadeEffect.textureName = "engineLogo";
		engineSplashScreen = new SplashScreen(this,fadeEffect,languageSelectionScreen);
		
		fadeEffect.textureName = "teamLogo";		
		teamSplashScreen = new SplashScreen(this,fadeEffect,engineSplashScreen);		
		
	//	setScreen(teamSplashScreen);
	}
		
	public void initializeGame() {
		
		menuScreen = new MenuScreen(this);
		optionsScreen = new OptionsScreen(this);	
		creditsScreen = new CreditsScreen(this);
		pauseScreen = new PauseScreen(this);
		gameOverScreen = new GameOverScreen(this);

		//
		modeSelectionScreen = new ModeSelectionScreen(this);
		worldSelectionScreen = new WorldSelectionScreen(this);
		
		
		playScreen = new PlayScreen(this);
		trainingScreen = new TrainingScreen(this);
		survivalScreen = new SurvivalScreen(this);
		
		
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
		
		//
		trainingScreen.pauseScreen = pauseScreen;
		survivalScreen.pauseScreen = pauseScreen;
		
		worldSelectionScreen.modeSelectionScreen = modeSelectionScreen;
		worldSelectionScreen.playScreen = playScreen;
		
		playScreen.pauseScreen = pauseScreen;
		playScreen.gameOverScreen = gameOverScreen;
		
		pauseScreen.playScreen = playScreen;
		pauseScreen.menuScreen = menuScreen;
		
		gameOverScreen.playScreen = playScreen;
		gameOverScreen.menuScreen = menuScreen;
		
		
	}
	
//	public void loadHighScore() {
//		Json json = new Json();
//		FileHandle file = Gdx.files.local(Configuration.getInstance().highscoreSavePath);
//	
//		if(file.exists()) {
//			String jsonData = file.readString();
//			jsonData = Base64Coder.decodeString(jsonData);
//
//			try {
//				Configuration.getInstance().highscoreDescriptor = json.fromJson(HighScoreDescriptor.class, jsonData);			
//			} catch(Exception e ) {
//			
//				Gdx.app.log("Expendable ","High Score file " + Configuration.getInstance().highscoreSavePath +" loading exception");
//				e.printStackTrace();
//			}
//		}
//		else {
//			createHighscoreFile();
//			loadHighScore();
//		}
//	}
	
//	public void saveHighScore() {
//		Json json = new Json();		
//		FileHandle file = Gdx.files.local(Configuration.getInstance().highscoreSavePath);
//
//		if(file.exists()) {
//			String jsonData = json.toJson(Configuration.getInstance().highscoreDescriptor);
//			jsonData = Base64Coder.encodeString(jsonData);
//			file.writeString(jsonData, false);
//		}
//		else {
//			createHighscoreFile();
//			Gdx.app.log("Expendable ", "File " + Configuration.getInstance().highscoreSavePath + " opening error detected!");
//		}
//	}
	
//	private void createHighscoreFile() {
//		HighScoreDescriptor desc = new HighScoreDescriptor();
//			
//		Json json = new Json();		
//		FileHandle file = Gdx.files.local(Configuration.getInstance().highscoreSavePath);
//		String jsonData = json.toJson(desc);
//		
//		jsonData = Base64Coder.encodeString(jsonData);
//		file.writeString(jsonData, false);
//	}

	public Timer getTimer() {
		return timer;
	}
	
	//TODO API for resources and sakura?
}

