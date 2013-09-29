package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Onigiri;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakurahero.SakuraTree;
import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureDetector;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class LevelEditorScreen extends GameScreen implements MultitouchGestureListener {

	public PauseScreen pauseScreen;
	public GameOverScreen gameOverScreen;
	private SakuraHero game;
	private MultitouchGestureDetector inputDetector;

	private SceneEntity pauseButton;
	private SceneEntity background;

	public SakuraTree tree;

	public LevelEditorScreen(final SakuraHero game) {
		this.game = game;

		pauseButton = new SceneEntity(Renderer.sceneAtlas.createSprite("pauseButton"),"Pause",64,64);
		inputDetector = new MultitouchGestureDetector(this);

		background = new SceneEntity(Renderer.sceneAtlas.createSprite("natsuBackground"));

		tree = new SakuraTree(Renderer.sceneAtlas.createSprite("tree"),new Array<Onigiri>());
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
	public void processInput() {	
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();

		if(Gdx.input.isKeyPressed(Keys.S))
			tree.saveSakuraTree("level.json");
		
		if(Gdx.input.isKeyPressed(Keys.L))
			tree.saveSakuraTree("level.json");
	}

	@Override
	public void processLogic(final float deltaTime) {

	}

	@Override
	public void processRendering() { 
		Renderer.clearScreen();
		Renderer.backgroundStage.draw();
		tree.render();
		Renderer.sceneStage.draw();
		Renderer.hudStage.draw();
	}

	@Override
	public void resize(final int width, final int height) {

	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {	
		Renderer.backgroundStage.addActor(background);
		Renderer.hudStage.addActor(pauseButton);

		pauseButton.updateEntityState(Configuration.getWidth()*0.98f-pauseButton.width, 0.0f);
		//		pauseButton.getSprite().setX(Configuration.getWidth()*0.98f-pauseButton.getSprite().getWidth());

		Gdx.input.setInputProcessor(inputDetector);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void hide() {
		Renderer.backgroundStage.clear();
		Renderer.sceneStage.clear();
		Renderer.hudStage.clear();
	}

	@Override
	public void pause() {

	}

	@Override
	public boolean tap(float x, float y, int count, int pointer) {
		Vector2 stageCoords = Vector2.Zero;
		Renderer.hudStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.hudStage.hit(stageCoords.x, stageCoords.y, true);

		if(actor == null)
			return false;

		if(actor.getName().equals("Pause")) {
			pauseScreen.getFrameBuffer().begin();	
			processRendering();
			pauseScreen.getFrameBuffer().end();
			game.setScreen(pauseScreen);
		}

		return true;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		Vector2 stageCoords = Vector2.Zero;
		Renderer.sceneStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

		SceneEntity flower = new SceneEntity(Renderer.sceneAtlas.createSprite("sakuraFlower"),32,32);

		flower.rotation = MathUtils.random(0.0f, 360.0f);
		flower.updateEntityState(stageCoords.x-flower.width*0.5f, stageCoords.y-flower.height*0.5f);

		tree.addSakuraLeaf(flower);


		return true;
	}

	@Override
	public boolean touchUp(float x, float y, int pointer) {
		return true;
	}

	@Override
	public boolean longPress(float x, float y, int pointer) {
		return false;
	}

	@Override
	public boolean fling(float startX, float startY, float endX, float endY, float velocityX, float velocityY, int pointer) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY, int pointer) {
		return false;
	}
}
