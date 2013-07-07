package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;

import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class OptionsScreen extends GameScreen implements GestureListener  {

	public MenuScreen menuScreen;
	
	private SakuraHero game;	
	private GestureDetector inputDetector;

	private FallingLeavesEffect fallingSakura;
	private SpriteBatch stateBatch;
	private Stage stateStage;
	
	private SpriteActor background;
	private SpriteActor options;
	private SpriteActor back;
	

//	private Label settings;
//	private Label currentHighScore;
//	private Label resetHighScore;
//	private Label music;
//	private Label musicState;
//	private Label sound;
//	private Label soundState;
//	private Label backToMenu;
	
	public OptionsScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;
		
		stateBatch = new SpriteBatch();
		stateStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,stateBatch);
		
		
		background = new SpriteActor(game.resources.getTexture("menuBackground"));  
		options = new SpriteActor(game.resources.getTexture("optionsBig"));
		back = new SpriteActor(game.resources.getTexture("back"),"Back");
		
		stateBatch = new SpriteBatch();
		stateStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,stateBatch);
		//options.getSprite().scale(1.0f);
//		
//    	LabelStyle style = new LabelStyle(Renderer.defaultFont,Color.WHITE);
//    	
//    	settings = new Label("Settings",style);
//    //	currentHighScore = new Label("High Score "+ Configuration.getInstance().highscoreDescriptor.highScore,style);
//    	resetHighScore = new Label("Tap to reset High Score!",style);	
//    	music = new Label("Music",style);
//    	musicState = new Label("On",style);
//    	sound = new Label("Sound",style);
//    	soundState = new Label("On",style);
//    	backToMenu = new Label("Back to menu",style);
//
    	inputDetector = new GestureDetector(this);    	
    	initializeInterface();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		Renderer.defaultStage.clear();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float deltaTime) {
		processInput();
		game.getTimer().updateTimer(deltaTime);
		while(game.getTimer().checkTimerAccumulator()) {
			processLogic(Timer.TIME_STEP);
			game.getTimer().eatAccumulatorTime();
		}
		processRendering();	
	}

	@Override
	public void resize(final int width, final int height) {
	//	Renderer.defaultStage.setViewport(Configuration.getInstance().width, Configuration.getInstance().height, false);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void show() {
		
		Renderer.defaultStage.clear();
    	Renderer.defaultStage.addActor(background);
    	
    	stateStage.addActor(options);
    	stateStage.addActor(back);
    	
//    	Renderer.defaultStage.addActor(settings);
//    	Renderer.defaultStage.addActor(currentHighScore);
//    	Renderer.defaultStage.addActor(resetHighScore);
//    	Renderer.defaultStage.addActor(music);
//    	Renderer.defaultStage.addActor(musicState);
//    	Renderer.defaultStage.addActor(sound);
//    	Renderer.defaultStage.addActor(soundState);
//    	
//    	Renderer.defaultStage.addActor(backToMenu);
//    	
//	//	currentHighScore.setText("High Score "+ Configuration.getInstance().highscoreDescriptor.highScore);
//		currentHighScore.setX((Configuration.getInstance().width-currentHighScore.getTextBounds().width)*0.5f);	
//
    	Gdx.input.setInputProcessor(inputDetector); 	
	}

	@Override
	public void processInput() {
	}

	@Override
	public void processLogic(final float deltaTime) {
		fallingSakura.updateEffect(deltaTime);
	}

	@Override
	public void processRendering() {
		Renderer.clearScreen();
		Renderer.defaultStage.draw();
		fallingSakura.renderEffect();
		stateStage.draw();
	}

	@Override
	public boolean pinch(Vector2 arg0, Vector2 arg1, Vector2 arg2, Vector2 arg3) {
		return false;
	}


	@Override
	public boolean zoom(float arg0, float arg1) {
		return false;
	}
	
	private void initializeInterface() {
		
		
		options.getSprite().setX((Configuration.getWidth()-options.getSprite().getWidth())*0.5f);	
		options.getSprite().setY(Configuration.getHeight()*0.90f - options.getSprite().getHeight());
		
	
		
		
//		backToMenu.setName("Back");
//		resetHighScore.setName("Reset");
//		musicState.setName("Music");
//		soundState.setName("Sound");
//		
//		settings.setTouchable(Touchable.disabled);
//	//	currentHighScore.setTouchable(Touchable.disabled);
//		music.setTouchable(Touchable.disabled);
//		sound.setTouchable(Touchable.disabled);
//		
//		settings.setX((Configuration.getInstance().width-settings.getTextBounds().width)*0.5f);	
//		settings.setY(Configuration.getInstance().height*0.90f - settings.getTextBounds().height);
//		
//	//	currentHighScore.setX((Configuration.getInstance().width-currentHighScore.getTextBounds().width)*0.5f);	
//	//	currentHighScore.setY(Configuration.getInstance().height*0.75f - currentHighScore.getTextBounds().height);
//		
//		resetHighScore.setX((Configuration.getInstance().width-resetHighScore.getTextBounds().width)*0.5f);	
//		resetHighScore.setY(Configuration.getInstance().height*0.65f - resetHighScore.getTextBounds().height);
//		
//		music.setX((Configuration.getInstance().width-music.getTextBounds().width*2.0f)*0.5f);	
//		music.setY(Configuration.getInstance().height*0.50f - music.getTextBounds().height);
//		
//		musicState.setX(music.getX()+music.getTextBounds().width*1.5f);
//		musicState.setY(music.getY());
//		
//		sound.setX((Configuration.getInstance().width-sound.getTextBounds().width*2.0f)*0.5f);	
//		sound.setY(Configuration.getInstance().height*0.40f - sound.getTextBounds().height);
//
//		soundState.setX(music.getX()+music.getTextBounds().width*1.5f);
//		soundState.setY(sound.getY());
//		
//		backToMenu.setX((Configuration.getInstance().width-backToMenu.getTextBounds().width)*0.5f);	
//		backToMenu.setY(Configuration.getInstance().height*0.30f - backToMenu.getTextBounds().height);
	}

	@Override
	public boolean fling(float arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float arg0, float arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int arg2, int arg3) {
		Vector2 stageCoords = Vector2.Zero;
		stateStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = stateStage.hit(stageCoords.x, stageCoords.y, true);
		
		if(actor == null)
			return false;
//		
//		if(actor.getName().equals("Music")) {
//			Configuration.getInstance().musicOn = !Configuration.getInstance().musicOn;
//			
//			if(Configuration.getInstance().musicOn) {
//				musicState.setText("On");
//				game.resources.getMusic("menuMusic").play();
//			}
//			else {
//				musicState.setText("Off");
//				game.resources.getMusic("menuMusic").stop();
//			}
//			
//			musicState.setX(music.getX()+music.getTextBounds().width*1.5f);
//		}
//		else if(actor.getName().equals("Sound")) {
//			Configuration.getInstance().soundOn = !Configuration.getInstance().soundOn;
//			
//			if(Configuration.getInstance().soundOn)
//				soundState.setText("On");
//			else
//				soundState.setText("Off");
//			
//			soundState.setX(music.getX()+music.getTextBounds().width*1.5f);
//		}
//		else if(actor.getName().equals("Reset")) {
////			if(Configuration.getInstance().soundOn)
////				game.resources.getSoundEffect("selection").play();
////			Configuration.getInstance().highscoreDescriptor.highScore = 0;
////			currentHighScore.setText("High Score "+ Configuration.getInstance().highscoreDescriptor.highScore);
////			currentHighScore.setX((Configuration.getInstance().width-currentHighScore.getTextBounds().width)*0.5f);	
//		}
//		
		if(actor.getName().equals("Back")) {
			
//			if(Configuration.getInstance().soundOn)
//				game.resources.getSoundEffect("selection").play();
			game.setScreen(menuScreen);
		}
		return true;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
