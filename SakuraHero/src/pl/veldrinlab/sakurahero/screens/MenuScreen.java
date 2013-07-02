package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;
import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class MenuScreen extends GameScreen implements GestureListener {

	public PlayScreen playScreen;
	public CreditsScreen informationScreen;
	public SettingsScreen settingsScreen;
	
	private SakuraHero game;
	private GestureDetector inputDetector;

	private SpriteActor background;
	
	private Label play;
	private Label settings;
	private Label credits;
	private Label exit;

	private Music menuMusic;

	public MenuScreen(final SakuraHero game) {
		this.game = game;

//		background = new SpriteActor(game.resources.getTexture("menu"));
//		
//		LabelStyle style = new LabelStyle(Renderer.defaultFont,Color.WHITE);
//		
//		play = new Label("Play",style);
//		settings = new Label("Options",style);
//		credits= new Label("Credits",style);
//		exit = new Label("Exit",style);
//		
//		menuMusic = game.resources.getMusic("menuMusic");
//		
//		inputDetector = new GestureDetector(this);
//		
//		initializeInterface();
	}

	@Override
	public void processInput() {
	}

	@Override
	public void processLogic(final float deltaTime) {

	}

	@Override
	public void processRendering() {	
		Renderer.clearScreen();
		Renderer.defaultStage.draw();
	}

	@Override
	public void render(final float deltaTime) {
		if(deltaTime > 0.5f)
			return;

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
		Renderer.defaultStage.setViewport(Configuration.getInstance().width, Configuration.getInstance().height, false);	
	}

	@Override
	public void hide() {
		Renderer.defaultStage.clear();
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		//	 TODO Auto-generated method stub
	}

	@Override
	public void show() {	
		
//		Renderer.enterOrthoMode();
//		if(Configuration.getInstance().musicOn) {
//			menuMusic.play();
//			menuMusic.setVolume(0.1f);
//		}
//
//		Renderer.defaultStage.addActor(background);
//		Renderer.defaultStage.addActor(play);
//		Renderer.defaultStage.addActor(settings);
//		Renderer.defaultStage.addActor(credits);
//		Renderer.defaultStage.addActor(exit);
//
//		Gdx.input.setInputProcessor(inputDetector);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
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
//		play.setName("Play");
//		settings.setName("Options");
//		credits.setName("Credits");
//		exit.setName("Exit");
//		
//		play.setX((Configuration.getInstance().width-play.getTextBounds().width)*0.5f);	
//		play.setY(Configuration.getInstance().height*0.60f - play.getTextBounds().height);
//		settings.setX((Configuration.getInstance().width-settings.getTextBounds().width)*0.5f);	
//		settings.setY(Configuration.getInstance().height*0.50f - settings.getTextBounds().height);
//		credits.setX((Configuration.getInstance().width-credits.getTextBounds().width)*0.5f);	
//		credits.setY(Configuration.getInstance().height*0.40f - credits.getTextBounds().height);
//		exit.setX((Configuration.getInstance().width-exit.getTextBounds().width)*0.5f);	
//		exit.setY(Configuration.getInstance().height*0.30f - exit.getTextBounds().height);		
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
	public boolean tap(float arg0, float arg1, int arg2, int arg3) {	
		Vector2 stageCoords = Vector2.Zero;
		Renderer.defaultStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.defaultStage.hit(stageCoords.x, stageCoords.y, true);

		if(actor == null)
			return false;

		if(actor.getName().equals("Credits")) {
			if(Configuration.getInstance().soundOn)
				game.resources.getSoundEffect("selection").play();
			game.setScreen(informationScreen);
			return true;
		}
		else if(actor.getName().equals("Options")) {
			if(Configuration.getInstance().soundOn)
				game.resources.getSoundEffect("selection").play();
			game.setScreen(settingsScreen);
			return true;
		}
		else if(actor.getName().equals("Play")) {
			if(Configuration.getInstance().soundOn)
				game.resources.getSoundEffect("selection").play();
			
			game.setScreen(playScreen);
			if(Configuration.getInstance().musicOn)
				menuMusic.stop();
				
			dispose();
			return true;			
		}
		else if(actor.getName().equals("Exit")) {
			if(Configuration.getInstance().soundOn)
				game.resources.getSoundEffect("selection").play();
			Gdx.app.exit();
		}

		return false;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
