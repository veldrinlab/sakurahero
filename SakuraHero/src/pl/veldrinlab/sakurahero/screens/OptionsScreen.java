package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.Language;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class OptionsScreen extends GameScreen implements GestureListener  {

	public MenuScreen menuScreen;

	private SakuraHero game;	
	private GestureDetector inputDetector;

	private FallingLeavesEffect fallingSakura;

	private SceneEntity background;
	private SceneEntity options;
	private SceneEntity back;

	private Label currentHighScore;
	private Label currentSurvivalTime;
	private Label resetHighScore;
	private Label music;
	private Label musicState;
	private Label sound;
	private Label soundState;

	private float blinking;

	public OptionsScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;

		background = new SceneEntity(game.resources.getTexture("menuBackground"));  
		options = new SceneEntity(game.resources.getTexture("optionsBig"));
		back = new SceneEntity(game.resources.getTexture("back"),"Back");

		LabelStyle style = new LabelStyle(game.resources.getFont("defaultFont"),Color.WHITE);

		currentHighScore = new Label("High Score "+ 0,style);
		currentSurvivalTime = new Label("Survival Time "+ 0 + " sec",style);
		resetHighScore = new Label("Tap to reset High Score!",style);	
		music = new Label("Music",style);
		musicState = new Label("On",style);
		sound = new Label("Sound",style);
		soundState = new Label("On",style);

		inputDetector = new GestureDetector(this);    	

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		Renderer.backgroundStage.clear();
		Renderer.hudStage.clear();
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

		if(game.options.language == Language.ENGLISH) {
			options = new SceneEntity(game.resources.getTexture("optionsBig"));
			back = new SceneEntity(game.resources.getTexture("back"),"Back");

		}
		else {
			options = new SceneEntity(game.resources.getTexture("optionsBigJap"));
			back = new SceneEntity(game.resources.getTexture("back"),"Back");
		}

		initializeInterface();

		Renderer.backgroundStage.addActor(background);
		Renderer.hudStage.addActor(options);
		Renderer.hudStage.addActor(back);

		Renderer.hudStage.addActor(currentHighScore);
		Renderer.hudStage.addActor(currentSurvivalTime);
		Renderer.hudStage.addActor(resetHighScore);
		Renderer.hudStage.addActor(music);
		Renderer.hudStage.addActor(musicState);
		Renderer.hudStage.addActor(sound);
		Renderer.hudStage.addActor(soundState);
		
	    	
		currentHighScore.setText("High Score "+ 0);
		currentHighScore.setX((Configuration.getWidth()-currentHighScore.getTextBounds().width)*0.5f);	
		
		Gdx.input.setInputProcessor(inputDetector); 	
	}

	@Override
	public void processInput() {
	}

	@Override
	public void processLogic(final float deltaTime) {
		fallingSakura.updateEffect(deltaTime);
		
		blinking += deltaTime*5.0f;
		
		float alpha = (float) ((Math.sin(blinking)+1.0f)/2.0f);
		
		musicState.setColor(1.0f, 1.0f, 1.0f, alpha);
		soundState.setColor(1.0f, 1.0f, 1.0f, alpha);
		resetHighScore.setColor(1.0f, 1.0f, 1.0f, alpha);
		//backToMenu.setColor(1.0f, 1.0f, 1.0f, alpha);
	}

	@Override
	public void processRendering() {
		Renderer.clearScreen();
		Renderer.backgroundStage.draw();
		fallingSakura.renderEffect();
		Renderer.hudStage.draw();
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
				resetHighScore.setName("Reset");
				musicState.setName("Music");
				soundState.setName("Sound");
		//		
		//		settings.setTouchable(Touchable.disabled);
				currentHighScore.setTouchable(Touchable.disabled);
				currentSurvivalTime.setTouchable(Touchable.disabled);
				music.setTouchable(Touchable.disabled);
				sound.setTouchable(Touchable.disabled);
		//		
		//		settings.setX((Configuration.getInstance().width-settings.getTextBounds().width)*0.5f);	
		//		settings.setY(Configuration.getInstance().height*0.90f - settings.getTextBounds().height);
		//		
		currentHighScore.setX((Configuration.getWidth()-currentHighScore.getTextBounds().width)*0.5f);	
		currentHighScore.setY(Configuration.getHeight()*0.70f - currentHighScore.getTextBounds().height);
		
		currentSurvivalTime.setX((Configuration.getWidth()-currentSurvivalTime.getTextBounds().width)*0.5f);	
		currentSurvivalTime.setY(Configuration.getHeight()*0.55f - currentSurvivalTime.getTextBounds().height);
		//		
				resetHighScore.setX((Configuration.getWidth()-resetHighScore.getTextBounds().width)*0.5f);	
		
				resetHighScore.setY(Configuration.getHeight()*0.40f - resetHighScore.getTextBounds().height);
		//		
				music.setX((Configuration.getWidth()-music.getTextBounds().width*2.0f)*0.5f);	
				music.setY(Configuration.getHeight()*0.30f - music.getTextBounds().height);
				
				musicState.setX(music.getX()+music.getTextBounds().width*1.5f);
				musicState.setY(music.getY());
				
				sound.setX((Configuration.getWidth()-sound.getTextBounds().width*2.0f)*0.5f);	
				sound.setY(Configuration.getHeight()*0.20f - sound.getTextBounds().height);
		
				soundState.setX(music.getX()+music.getTextBounds().width*1.5f);
				soundState.setY(sound.getY());
				
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
		Renderer.hudStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.hudStage.hit(stageCoords.x, stageCoords.y, true);

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
