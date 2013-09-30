package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.SakuraGameMode;
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

public class GameOverScreen extends GameScreen implements GestureListener {

	public GameScreen gameScreen;
	public MenuScreen menuScreen;

	private SakuraHero game;
	private GestureDetector inputDetector;

	private FallingLeavesEffect fallingSakura;

	private SceneEntity background;
	private SceneEntity gameOver;
	private SceneEntity tryAgain;
	private SceneEntity backToMenu;
	private SceneEntity exit;

	private Label record;
	private Label score;

	public GameOverScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;

		background = new SceneEntity(Renderer.introAtlas.createSprite("menuBackground"));
		gameOver = new SceneEntity(Renderer.guiAtlas.createSprite("gameOver"));
		tryAgain = new SceneEntity(Renderer.guiAtlas.createSprite("tryAgain"),"Try Again");
		backToMenu = new SceneEntity(Renderer.guiAtlas.createSprite("menuSmall"),"Menu");
		exit = new SceneEntity(Renderer.guiAtlas.createSprite("exit"),"Exit");

		record = new Label("Its is new record !!!!!!!!!",Renderer.standardFont);
		score = new Label("",Renderer.standardFont);
		
		inputDetector = new GestureDetector(this);
		initializeInterface();
	}

	@Override
	public void dispose() {}

	@Override
	public void show() {		
		game.playMusic.stop();
		game.gameOverMusic.play();
		
		record.setColor(1.0f, 1.0f, 1.0f, 0.0f);
		
		if(game.options.mode == SakuraGameMode.NORMAL) {
			score.setText("Score is " + String.valueOf(game.results.score));

			if(game.results.score > game.results.highScore) {
				game.results.highScore = game.results.score;
				game.saveHighScore();
				record.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
		else {
			score.setText("Time " + String.valueOf(game.results.time + " sec"));
		
			if(game.results.time > game.results.timeRecord) {
				game.results.timeRecord = game.results.time;
				game.saveHighScore();
				record.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
		
		score.setX((Configuration.getWidth()-score.getTextBounds().width)*0.5f);	
		
		Renderer.backgroundStage.addActor(background);
		Renderer.hudStage.addActor(score);
		Renderer.hudStage.addActor(record);
		Renderer.hudStage.addActor(tryAgain);
		Renderer.hudStage.addActor(backToMenu);
		Renderer.hudStage.addActor(gameOver);
		Renderer.hudStage.addActor(exit);

		Gdx.input.setInputProcessor(inputDetector);
	}

	@Override
	public void hide() {
		Renderer.backgroundStage.clear();
		Renderer.hudStage.clear();
	}

	@Override
	public void resume() {}

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
	public void resize(final int width, final int height) {}

	@Override
	public void pause() {}

	@Override
	public boolean pinch(Vector2 arg0, Vector2 arg1, Vector2 arg2, Vector2 arg3) {
		return false;
	}

	@Override
	public boolean zoom(float arg0, float arg1) {
		return false;
	}

	private void initializeInterface() {		
		gameOver.alignCenter(0.9f);
		tryAgain.alignCenter(0.35f);
		backToMenu.alignRelative(0.1f, 0.20f);
		exit.alignRelative(0.9f, 0.20f);
		
		tryAgain.updateBounds();
		backToMenu.updateBounds();
		exit.updateBounds();
		
		score.setTouchable(Touchable.disabled);
		record.setTouchable(Touchable.disabled);
		
		record.setX((Configuration.getWidth()-record.getTextBounds().width)*0.5f);	
		record.setY(Configuration.getHeight()*0.65f - record.getTextBounds().height);
		
		score.setY(Configuration.getHeight()*0.45f - score.getTextBounds().height);
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

		if(actor.getName().equals("Try Again")) {
			game.gameOverMusic.stop();
			game.playMusic.play();
			
			if(game.options.mode == SakuraGameMode.NORMAL) {
				PlayScreen screen = (PlayScreen)gameScreen;
				screen.resetState();
				game.setScreen(screen);
			}
			else {
				SurvivalScreen screen = (SurvivalScreen)gameScreen;
				screen.resetState();
				game.setScreen(screen);
			}
		}
		else if(actor.getName().equals("Menu")) {
			game.gameOverMusic.stop();
			game.menuMusic.play();
			game.setScreen(menuScreen);
		}
		else if(actor.getName().equals("Exit"))
			Gdx.app.exit();
		return true;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		return false;
	}
}
