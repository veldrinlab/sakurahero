package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameOverScreen extends GameScreen implements GestureListener {

	public PlayScreen playScreen;
	public MenuScreen menuScreen;

	private SakuraHero game;
	private GestureDetector inputDetector;

	private FallingLeavesEffect fallingSakura;

	private SceneEntity background;
	private SceneEntity gameOver;
	private SceneEntity tryAgain;
	private SceneEntity backToMenu;
	private SceneEntity exit;

	private Label score; // to bedzie ciekawie zrobic przy pomocy tekstur - chyba, ¿e do tego u¿yjê jednak fontów
	// w sumie fonty s¹ dobre wszêdzie - ale nie do wersji japoñskiej, wiêc opieram to na spritach - generyczna wersja

	public GameOverScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;

		background = new SceneEntity(Renderer.introAtlas.createSprite("menuBackground"));
		gameOver= new SceneEntity(Renderer.guiAtlas.createSprite("gameOver"));
		tryAgain = new SceneEntity(Renderer.guiAtlas.createSprite("tryAgain"),"Try Again");
		backToMenu = new SceneEntity(Renderer.guiAtlas.createSprite("menuSmall"),"Menu");
		exit = new SceneEntity(Renderer.guiAtlas.createSprite("exit"),"Exit");

		inputDetector = new GestureDetector(this);
		initializeInterface();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void show() {		
		//		if(Configuration.getInstance().musicOn) {
		//			game.resources.getMusic("gameOverMusic").play();
		//			game.resources.getMusic("gameOverMusic").setLooping(true);
		//		}
		//		



		Renderer.backgroundStage.addActor(background);

		//Renderer.defaultStage.addActor(score);
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
	public void resume() {
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
	public void resize(final int width, final int height) {
	}

	@Override
	public void pause() {
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
		gameOver.alignCenter(0.9f);
		tryAgain.alignCenter(0.45f);
		backToMenu.alignCenter(0.30f);
		exit.alignCenter(0.15f);
		
		tryAgain.updateBounds();
		backToMenu.updateBounds();
		exit.updateBounds();
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
			//			if(Configuration.getInstance().soundOn)
			//				game.resources.getSoundEffect("selection").play();
			//			
			//			if(Configuration.getInstance().musicOn)
			//				game.resources.getMusic("gameOverMusic").stop();

			game.setScreen(playScreen);
		}
		else if(actor.getName().equals("Menu")) {
			//			if(Configuration.getInstance().soundOn)
			//				game.resources.getSoundEffect("selection").play();
			//			
			//			if(Configuration.getInstance().musicOn)
			//				game.resources.getMusic("gameOverMusic").stop();
			game.setScreen(menuScreen);
		}
		else if(actor.getName().equals("Exit")) {
			//			if(Configuration.getInstance().soundOn)
			//				game.resources.getSoundEffect("selection").play();
			//			
			//			if(Configuration.getInstance().musicOn)
			//				game.resources.getMusic("gameOverMusic").stop();
			Gdx.app.exit();
		}
		return true;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		return false;
	}
}
