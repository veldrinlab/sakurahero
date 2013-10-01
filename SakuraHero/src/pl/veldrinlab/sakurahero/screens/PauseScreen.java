package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class PauseScreen extends GameScreen implements GestureListener  {

	public GameScreen backScreen;
	public MenuScreen menuScreen;

	private SakuraHero game;
	private GestureDetector inputDetector;

	private FrameBuffer renderTarget;
	private Stage pauseStage;
	private SpriteBatch pauseBatch;
	private SceneEntity pauseBackground;

	private SceneEntity pause;
	private SceneEntity resume;
	private SceneEntity menu;
	private SceneEntity exit;

	private float alpha;

	public PauseScreen(final SakuraHero game) {
		this.game = game;

		pauseBatch = new SpriteBatch();
		renderTarget = new FrameBuffer(Pixmap.Format.RGBA8888,Configuration.getWidth(), Configuration.getHeight(),true);
		pauseStage = new Stage(Configuration.getWidth(), Configuration.getHeight(), false, pauseBatch);
		pauseBackground = new SceneEntity(renderTarget.getColorBufferTexture());
		pauseBackground.sprite.flip(false,true);

		pause = new SceneEntity(Renderer.guiAtlas.createSprite("pause"));
		resume = new SceneEntity(Renderer.guiAtlas.createSprite("resume"),"Resume");
		menu = new SceneEntity(Renderer.guiAtlas.createSprite("menuSmall"),"Menu");
		exit = new SceneEntity(Renderer.guiAtlas.createSprite("exit"),"Exit");

		inputDetector = new GestureDetector(this);

		alpha = 0.4f;
		initializeInterface();
	}

	public FrameBuffer getFrameBuffer() {
		return renderTarget;
	}

	@Override
	public void dispose() {
		pauseBatch.dispose();
		pauseStage.dispose();
	}

	@Override
	public void hide() {
		pauseStage.clear();
	}

	@Override
	public void pause() {}

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
	public void resume() {}

	@Override
	public void show() {	
		pauseStage.clear();
		pauseStage.addActor(pauseBackground);
		pauseStage.addActor(pause);
		pauseStage.addActor(resume);
		pauseStage.addActor(menu);
		pauseStage.addActor(exit);

		Gdx.input.setInputProcessor(inputDetector);
	}

	@Override
	public void processInput() {}

	@Override
	public void processLogic(final float deltaTime) {}

	@Override
	public void processRendering() {
		Renderer.clearScreen();
		pauseBackground.sprite.setTexture(renderTarget.getColorBufferTexture());
		pauseBackground.setEntityAlpha(alpha);
		pauseStage.draw();
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
		pause.alignCenter(0.9f);		
		resume.alignCenter(0.65f);
		menu.alignCenter(0.50f);
		exit.alignCenter(0.35f);
		
		resume.updateBounds();
		menu.updateBounds();
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
		pauseStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = pauseStage.hit(stageCoords.x, stageCoords.y, true);

		if(actor == null)
			return false;

		if(actor.getName().equals("Resume")) {
			game.playMusic.play();
			game.setScreen(backScreen);
		}
		else if(actor.getName().equals("Menu")) {
			game.playMusic.stop();
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
