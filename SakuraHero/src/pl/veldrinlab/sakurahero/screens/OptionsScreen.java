package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

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

	public OptionsScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;

		background = new SceneEntity(Renderer.introAtlas.createSprite("menuBackground")); 
		options = new SceneEntity(Renderer.guiAtlas.createSprite("optionsBig"));
		back = new SceneEntity(Renderer.guiAtlas.createSprite("back"),"Back");

		currentHighScore = new Label("High Score "+ 0,Renderer.smallFont);
		currentSurvivalTime = new Label("Time record "+ 0 + " sec",Renderer.smallFont);
		resetHighScore = new Label("Tap to reset High Scores...",Renderer.smallFont);	
		music = new Label("Music",Renderer.smallFont);
		musicState = new Label("On",Renderer.smallFont);
		sound = new Label("Sound",Renderer.smallFont);
		soundState = new Label("On",Renderer.smallFont);

		inputDetector = new GestureDetector(this);    	
		initializeInterface();
	}

	@Override
	public void dispose() {}

	@Override
	public void hide() {
		Renderer.backgroundStage.clear();
		Renderer.hudStage.clear();
	}

	@Override
	public void pause() {}

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
	public void resize(final int width, final int height) {}

	@Override
	public void resume() {}

	@Override
	public void show() {
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

		currentHighScore.setText("High Score "+ game.results.highScore);
		currentHighScore.setX((Configuration.getWidth()-currentHighScore.getTextBounds().width)*0.5f);	

		currentSurvivalTime.setText("Time record "+ game.results.timeRecord);
		currentSurvivalTime.setX((Configuration.getWidth()-currentSurvivalTime.getTextBounds().width)*0.5f);

		Gdx.input.setInputProcessor(inputDetector); 	
	}

	@Override
	public void processInput() {}

	@Override
	public void processLogic(final float deltaTime) {
		fallingSakura.updateEffect(deltaTime);
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
		options.alignCenter(0.90f);

		resetHighScore.setName("Reset");
		musicState.setName("Music");
		soundState.setName("Sound");

		currentHighScore.setTouchable(Touchable.disabled);
		currentSurvivalTime.setTouchable(Touchable.disabled);
		music.setTouchable(Touchable.disabled);
		sound.setTouchable(Touchable.disabled);

		currentHighScore.setX((Configuration.getWidth()-currentHighScore.getTextBounds().width)*0.5f);	
		currentHighScore.setY(Configuration.getHeight()*0.70f - currentHighScore.getTextBounds().height);

		currentSurvivalTime.setX((Configuration.getWidth()-currentSurvivalTime.getTextBounds().width)*0.5f);	
		currentSurvivalTime.setY(Configuration.getHeight()*0.55f - currentSurvivalTime.getTextBounds().height);

		resetHighScore.setX((Configuration.getWidth()-resetHighScore.getTextBounds().width)*0.5f);	
		resetHighScore.setY(Configuration.getHeight()*0.40f - resetHighScore.getTextBounds().height);

		music.setX((Configuration.getWidth()-music.getTextBounds().width*2.0f)*0.5f);	
		music.setY(Configuration.getHeight()*0.30f - music.getTextBounds().height);

		musicState.setX(music.getX()+music.getTextBounds().width*1.5f);
		musicState.setY(music.getY());

		sound.setX((Configuration.getWidth()-sound.getTextBounds().width*2.0f)*0.5f);	
		sound.setY(Configuration.getHeight()*0.20f - sound.getTextBounds().height);

		soundState.setX(music.getX()+music.getTextBounds().width*1.5f);
		soundState.setY(sound.getY());

		back.updateBounds();
	}

	@Override
	public boolean fling(float arg0, float arg1, int arg2) {
		return false;
	}

	@Override
	public boolean longPress(float arg0, float arg1) {
		return false;
	}

	@Override
	public boolean pan(float arg0, float arg1, float arg2, float arg3) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int arg2, int arg3) {
		Vector2 stageCoords = Vector2.Zero;
		Renderer.hudStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.hudStage.hit(stageCoords.x, stageCoords.y, true);

		if(actor == null)
			return false;

		if(actor.getName().equals("Music")) {

			if(game.options.musicVolume == 1.0f) {
				game.options.musicVolume = 0.0f;
				musicState.setText("Off");
			}
			else {
				game.options.musicVolume = 1.0f;
				musicState.setText("On");
			}

			game.menuMusic.setVolume(game.options.musicVolume);
			game.playMusic.setVolume(game.options.musicVolume);
			game.gameOverMusic.setVolume(game.options.musicVolume);
			
			musicState.setX(music.getX()+music.getTextBounds().width*1.5f);
		}
		else if(actor.getName().equals("Sound")) {
			if(game.options.soundVolume == 1.0f) {
				game.options.soundVolume = 0.0f;
				soundState.setText("Off");
			}
			else {
				game.options.soundVolume = 1.0f;
				soundState.setText("On");
			}
				
			soundState.setX(music.getX()+music.getTextBounds().width*1.5f);
		}
		else if(actor.getName().equals("Reset")) {
			resetHighScore.setName("Confirm");
			resetHighScore.setText("Confirm score reset!");
			resetHighScore.setX((Configuration.getWidth()-resetHighScore.getTextBounds().width)*0.5f);	
		}
		else if(actor.getName().equals("Confirm")) {
			resetHighScore.setName("Reset");
			resetHighScore.setText("Tap to reset High Scores...");
			resetHighScore.setX((Configuration.getWidth()-resetHighScore.getTextBounds().width)*0.5f);	

			game.results.highScore = 0;
			game.results.timeRecord = 0.0f;
			game.saveHighScore();
			
			currentHighScore.setText("High Score "+ game.results.highScore);
			currentHighScore.setX((Configuration.getWidth()-currentHighScore.getTextBounds().width)*0.5f);	

			currentSurvivalTime.setText("Time record "+ game.results.timeRecord);
			currentSurvivalTime.setX((Configuration.getWidth()-currentSurvivalTime.getTextBounds().width)*0.5f);
		}

		if(actor.getName().equals("Back"))
			game.setScreen(menuScreen);
		return true;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		return false;
	}
}
