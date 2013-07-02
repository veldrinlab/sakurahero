package pl.veldrinlab.sakurahero;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.screens.CreditsScreen;
import pl.veldrinlab.sakurahero.screens.GameOverScreen;
import pl.veldrinlab.sakurahero.screens.MenuScreen;
import pl.veldrinlab.sakurahero.screens.PauseScreen;
import pl.veldrinlab.sakurahero.screens.PlayScreen;
import pl.veldrinlab.sakurahero.screens.SettingsScreen;
import pl.veldrinlab.sakurahero.screens.SplashScreen;
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
public class SakuraHero extends Game {

	public AsyncResourceManager resources;
	
	private Timer timer;
	private SplashScreen teamSplashScreen;
	private SplashScreen engineSplashScreen;
	private MenuScreen menuScreen;
	private SettingsScreen settingsScreen;
	private CreditsScreen creditsScreen;
	private PlayScreen playScreen;
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
		timer = new Timer();
		resources = new AsyncResourceManager();
		
		initializeEngine();
	//	loadHighScore();
		initializeGame();
	}

	private void initializeEngine() {
		resources.loadResources(Configuration.getInstance().resourcePath);
		resources.finishLoading();
		Renderer.defaultFont = resources.getFont("defaultFont");	
	//	Renderer.defaultShader = resources.getShader("defaultMesh");
	}
		
	public void initializeGame() {
		FadeEffectParameters fadeEffect = new FadeEffectParameters();
		
		fadeEffect.fadeInTime = 1.0f;
		fadeEffect.stayTime = 2.0f;
		fadeEffect.fadeOutTime = 1.0f;
		fadeEffect.skippable = true;
		fadeEffect.skippableWhileFadingIn = true;
		fadeEffect.textureName = "engineLogo";
		
		//
		playScreen = new PlayScreen(this);
		menuScreen = new MenuScreen(this);
		engineSplashScreen = new SplashScreen(this,fadeEffect,playScreen);
		
		fadeEffect.textureName = "teamLogo";		
		teamSplashScreen = new SplashScreen(this,fadeEffect,engineSplashScreen);
		
		creditsScreen = new CreditsScreen(this);
		settingsScreen = new SettingsScreen(this);
		pauseScreen = new PauseScreen(this);
		//playScreen = new PlayScreen(this);
		gameOverScreen = new GameOverScreen(this);

		buildGameStateGraph();
		
		setScreen(teamSplashScreen);
	}
	
	public void buildGameStateGraph() {
		menuScreen.playScreen = playScreen;
		menuScreen.informationScreen = creditsScreen;
		menuScreen.settingsScreen = settingsScreen;
		
		creditsScreen.menuScreen = menuScreen;
		settingsScreen.menuScreen = menuScreen;
		
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
}


//package pl.veldrinlab.sakurahero;
//
//import com.badlogic.gdx.ApplicationListener;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.Input.Keys;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL10;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
//import com.badlogic.gdx.graphics.glutils.ShaderProgram;
//import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.utils.Array;
//
//public class SakuraHero implements ApplicationListener, InputProcessor {
//	private OrthographicCamera camera;
//
//	// swing cout
//	ImmediateModeRenderer20 renderer;
//	Matrix4 projMatrix = new Matrix4();
//	Stage stage;
//
//	Vector2 pos = new Vector2();
//	int swingPointMax = 8;
//
//	Array<Vector2> swing;
//	float accumulator = 0.5f;
//
//	
//	ShaderProgram shader;
//
//	// smoke test
//
//	SpriteBatch batch;
//	Sprite sprite;
//	int currentFrame = 0;
//	int frameAmount = 45;
//	float TIME = 0.1f;
//	float frameTime = TIME;
//
//	Sprite background;
//
//	// sakura tree
//	
//	Sprite tree;
//	
//	
//	// sakura
//	
//	Array<Sprite> leaves;
//	Array<Float> leaveBalance;
//	float sakuraAccumulator;
//	
//	
//	// test enemey
//	Sprite enemy;
//	int enemyCurrentFrame = 0;
//	int enemyFrameAmount = 8;
//	float ENEMY_TIME = 0.05f;
//	float enemyFrameTime = ENEMY_TIME;
//	
//	
//	float elipseA = 250.0f;
//	float elipseB = 150.0f;
//	float movementAccumulator = 0.0f;
//	
//	
//	
//	// ninja
//	float v = 100.0f;
//	
//	@Override
//	public void create() {		
//
//		// enemy
//		enemy = new Sprite(new Texture(Gdx.files.internal("enemy.png")),enemyCurrentFrame*0, 0, 256, 256);
//		enemy.setPosition(400-128, 240-128);
//		
//		
//		//
//		renderer = new ImmediateModeRenderer20(false,true,0);
//		swing = new Array<Vector2>();
//
//		shader = new ShaderProgram(Gdx.files.internal("default2.vert"),Gdx.files.internal("default.frag"));
//
//		// leaves
//		leaves = new Array<Sprite>();
//		leaveBalance = new Array<Float>();
//		
//		for(int i = 0; i < 20; i++) {
//			Sprite leaf = new Sprite(new Texture(Gdx.files.internal("sakuraLeaf.png")));
//			leaf.setPosition(MathUtils.random(0.0f, 800.0f), MathUtils.random(0.0f, 480.0f));
//			leaves.add(leaf);
//			leaveBalance.add(MathUtils.random(0.0f, 1.0f));
//		}
//		
//
//		renderer.setShader(shader);
//		//
//		stage = new Stage(800.0f,480.0f,false);
//		batch = new SpriteBatch();
//
//	//	batch.setShader(shader);
//	
//		sprite = new Sprite( new Texture(Gdx.files.internal("test2a.png")), currentFrame*0, 0, 256, 256);
//		sprite.setColor(Color.BLACK);
//		background = new Sprite(new Texture(Gdx.files.internal("test.png")));
//		
//		tree = new Sprite(new Texture(Gdx.files.internal("test3.png")));
//		
//		tree.setPosition(400-tree.getWidth()*0.5f, -100.0f);
//		tree.setScale(0.65f);
//		
//		
//
//		
//		Gdx.input.setInputProcessor(this);
//	}
//
//	@Override
//	public void dispose() {
//		batch.dispose();
//	}
//
//	@Override
//	public void render() {		
//
//		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
//			Gdx.app.exit();
//		
//		float deltaTime = 1.0f/60.0f;
//
//		if(swing.size > 0) {
//			accumulator -= deltaTime;
//
//			if(accumulator < 0.0f) {
//				accumulator = 0.5f;
//				swing.clear();
//			}
//		}
//
//		// enemy animation
//		enemyFrameTime -= deltaTime;
//		
//		if(enemyFrameTime < 0.0f) {
//			enemyCurrentFrame = (enemyCurrentFrame+1)%enemyFrameAmount;
//			enemy.setRegion(enemyCurrentFrame*256, 0,256, 256);
//			enemyFrameTime = ENEMY_TIME;
//		}
//		
//		movementAccumulator += deltaTime*2.0f;
//		
//		enemy.setX((float) (400-128+elipseA*Math.cos(movementAccumulator)));
//		enemy.setY((float) (240-128+elipseB*Math.sin(movementAccumulator)));
//	
//		Gdx.app.log("frame ", String.valueOf(enemyCurrentFrame));
//		
//		//animation
//		frameTime -= deltaTime;
//
//		if(frameTime < 0.0f) {
//			
//			// dla ca³oœci
//			
//			/*
//			 * 0 1 2 3 4 5 6 
//			 * 7 8 9 10 11 12 13 
//			 * 14 15 16 ...
//			 * 
//			 * 
//			 * 
//			 */
//			
//			int column = currentFrame % 7;
//			int row = (currentFrame / 7);
//			
//			
//			currentFrame  = (currentFrame+1)%45;
//			sprite.setRegion(column*256, row*256, 256,256);
//			frameTime = TIME;
//		}
//
//		Gdx.gl.glClearColor(1, 1, 1, 1);
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//
//		//	batch.setProjectionMatrix(camera.combined);
//
//		// render
//
//		batch.begin();
//		
//		
//	//	v -= deltaTime*5.0f;
//		
//	//	shader.setUniformf("v", v);
//	
//		
//		background.draw(batch);
//		
////		enemy.draw(batch);
//		
//
////		sprite.draw(batch);
//		//tree.draw(batch);
//		batch.end();
//
//		batch.begin();
//		
//		sakuraAccumulator += deltaTime;
//		
//		for(int i = 0; i < leaves.size; ++i) {
//			float currentX = leaves.get(i).getX();
//			float currentY = leaves.get(i).getY();
//			
//			
//			currentX += Math.sin(leaveBalance.get(i)+sakuraAccumulator)*0.5f;
//			currentY -= 50.0f * deltaTime;
//			
//			leaves.get(i).setX(currentX);
//			leaves.get(i).setY(currentY);
//			
////			leaves.get(i).setRotation(leaves.get(i).getRotation()+deltaTime*50.0f);
//			
//			leaves.get(i).setRotation((float) (-Math.abs(Math.sin(leaveBalance.get(i)+sakuraAccumulator))*30.0f));
//			
//			
//			if(currentY < -leaves.get(i).getHeight())
//				leaves.get(i).setPosition(MathUtils.random(0.0f, 800.0f), 480.0f);
//			
//			
//			leaves.get(i).draw(batch);
//			
//		}
//		
//		batch.end();
//		
//	//	Gdx.gl20.glLineWidth(15);
//		
//
////		if(swing.size > 1) {
////
////			for(int i = 0; i < swing.size-1; ++i) {
////				renderer.begin(projMatrix, GL20.GL_LINES);
////				renderer.vertex(swing.get(i).x, swing.get(i).y, 0);
////				renderer.vertex(swing.get(i+1).x, swing.get(i+1).y, 0);
////				renderer.end();
////			}
////		}
//	}
//
//	@Override
//	public void resize(int width, int height) {
//	}
//
//	@Override
//	public void pause() {
//	}
//
//	@Override
//	public void resume() {
//	}
//
//	@Override
//	public boolean keyDown(int keycode) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean keyUp(int keycode) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean keyTyped(char character) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		// TODO Auto-generated method stub
//
//
//		//
//		if(swing.size < swingPointMax) {
//			Vector2 point = new Vector2();
//			stage.screenToStageCoordinates(point.set(screenX, screenY));
//
//			swing.add(point);
//
//		}
//
//		//		Actor actor = hudStage.hit(stageCoords.x, stageCoords.y, true);
//		Gdx.app.log("touch down", " test");
//		return false;
//	}
//
//	@Override
//	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean touchDragged(int screenX, int screenY, int pointer) {
//		//	
//		if(swing.size < swingPointMax) {
//			Vector2 point = new Vector2();
//			stage.screenToStageCoordinates(point.set(screenX, screenY));
//
//			swing.add(point);
//
//		}
//		Gdx.app.log("touch dragged", " test");
//		return false;
//	}
//
//	@Override
//	public boolean mouseMoved(int screenX, int screenY) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean scrolled(int amount) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//}
