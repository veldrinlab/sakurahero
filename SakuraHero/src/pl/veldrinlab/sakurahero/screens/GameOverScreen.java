package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;
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

public class GameOverScreen extends GameScreen implements GestureListener {
	
	public PlayScreen playScreen;
	public MenuScreen menuScreen;
	
	private SakuraHero game;
	private GestureDetector inputDetector;
	
	private SpriteActor background;

	private Label score;
	private Label tryAgain;
	private Label backToMenu;
	
	public GameOverScreen(final SakuraHero game) {
		this.game = game;
		
//		background = new SpriteActor(game.resources.getTexture("background"));
//	
//		LabelStyle style = new LabelStyle(Renderer.defaultFont,Color.WHITE);
//		
//		score = new Label("Score ",style);
//		tryAgain= new Label("Try again!",style);
//		backToMenu= new Label("Back to menu",style);
//		
//		inputDetector = new GestureDetector(this);
//		initializeInterface();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {		
//		if(Configuration.getInstance().musicOn) {
//			game.resources.getMusic("gameOverMusic").play();
//			game.resources.getMusic("gameOverMusic").setLooping(true);
//		}
//		
//		Renderer.enterOrthoMode();
//		
//		Renderer.defaultStage.clear();
//		Renderer.defaultStage.addActor(background);
//		Renderer.defaultStage.addActor(score);
//		Renderer.defaultStage.addActor(tryAgain);
//		Renderer.defaultStage.addActor(backToMenu);
//		
//		
//
//		score.setX((Configuration.getInstance().width-score.getTextBounds().width)*0.5f);	
//		
//		Gdx.input.setInputProcessor(inputDetector);
	}
		
	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		Renderer.defaultStage.clear();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
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
		
//		Renderer.enterOrthoMode();
//		Renderer.defaultStage.clear();
//		Renderer.defaultStage.addActor(background);
//		Renderer.defaultStage.addActor(score);
//		Renderer.defaultStage.addActor(tryAgain);
//		Renderer.defaultStage.addActor(backToMenu);
//		Renderer.defaultStage.draw();
	}
	
	@Override
	public void render(final float deltaTime) {
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
		Renderer.defaultStage.setViewport(Configuration.getInstance().descriptor.width, Configuration.getInstance().descriptor.height, false);	
	}

	@Override
	public void pause() {
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
//		score.setTouchable(Touchable.disabled);
//		tryAgain.setName("Again");
//		backToMenu.setName("Menu");
//
//		score.setX((Configuration.getInstance().width-score.getTextBounds().width)*0.5f);	
//		score.setY(Configuration.getInstance().height*0.70f - score.getTextBounds().height);
//
//		tryAgain.setX((Configuration.getInstance().width-tryAgain.getTextBounds().width)*0.5f);	
//		tryAgain.setY(Configuration.getInstance().height*0.60f - tryAgain.getTextBounds().height);
//
//		backToMenu.setX((Configuration.getInstance().width-backToMenu.getTextBounds().width)*0.5f);	
//		backToMenu.setY(Configuration.getInstance().height*0.50f - backToMenu.getTextBounds().height);
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
		Renderer.defaultStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.defaultStage.hit(stageCoords.x, stageCoords.y, true);
			
		if(actor == null)
			return false;

//		if(actor.getName().equals("Again")) {
//			if(Configuration.getInstance().soundOn)
//				game.resources.getSoundEffect("selection").play();
//			
//			if(Configuration.getInstance().musicOn)
//				game.resources.getMusic("gameOverMusic").stop();
//	
//			game.setScreen(playScreen);
//		}
//		else if(actor.getName().equals("Menu")) {
//			if(Configuration.getInstance().soundOn)
//				game.resources.getSoundEffect("selection").play();
//			
//			if(Configuration.getInstance().musicOn)
//				game.resources.getMusic("gameOverMusic").stop();
//			game.setScreen(menuScreen);
//		}
//		
		

		return true;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
