package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakurahero.SakuraLeafDescriptor;
import pl.veldrinlab.sakurahero.SakuraTree;
import pl.veldrinlab.sakurahero.SakuraTreeDescriptor;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureDetector;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;

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

		pauseButton = new SceneEntity(Renderer.sceneAtlas.createSprite("pauseButton"),"Pause");
		inputDetector = new MultitouchGestureDetector(this);

		background = new SceneEntity(Renderer.sceneAtlas.createSprite("natsuBackground"));

		tree = new SakuraTree(Renderer.sceneAtlas.createSprite("tree"),Renderer.sceneAtlas.createSprite("sakuraFlower"));
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


		if(Gdx.input.isKeyPressed(Keys.S)) {
			Json json = new Json();		
			FileHandle file = Gdx.files.local("level.json");		
			String jsonData = json.toJson(tree.leaves);
			file.writeString(jsonData, false);
		}

		if(Gdx.input.isKeyPressed(Keys.L)) {

			Json json = new Json();		
			FileHandle file = Gdx.files.local("level.json");

			String jsonData = file.readString();

			try {
				tree.leaves = json.fromJson(SakuraTreeDescriptor.class, jsonData);			
			} catch(Exception e ) {

				Gdx.app.log("SakuraHero ","Level file loading exception");
				e.printStackTrace();
			}

			tree.init();
		}
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

		pauseButton.getSprite().setX(Configuration.getWidth()*0.98f-pauseButton.getSprite().getWidth());
	
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
		tree.sakuraTreeStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		float rotation = MathUtils.random(0.0f, 360.0f);

		SceneEntity flower = new SceneEntity(Renderer.sceneAtlas.createSprite("sakuraFlower"));
		flower.getSprite().setPosition(stageCoords.x-flower.getSprite().getWidth()*0.5f, stageCoords.y-flower.getSprite().getHeight()*0.5f);
		flower.getSprite().setRotation(rotation);

		tree.sakuraTreeStage.addActor(flower);
		tree.leaves.leaves.add(new SakuraLeafDescriptor(stageCoords.x, stageCoords.y, rotation));

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
