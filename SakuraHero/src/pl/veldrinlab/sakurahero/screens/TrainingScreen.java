package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.GameHud;
import pl.veldrinlab.sakurahero.Onigiri;
import pl.veldrinlab.sakurahero.NinjaOnigiri;
import pl.veldrinlab.sakurahero.OniOnigiri;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakurahero.KatanaSwing;
import pl.veldrinlab.sakurahero.SamuraiOnigiri;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureDetector;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureListener;
import pl.veldrinlab.sakuraEngine.utils.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TrainingScreen extends GameScreen implements MultitouchGestureListener, InputProcessor {

	public PauseScreen pauseScreen;

	private SakuraHero game;
	private MultitouchGestureDetector inputDetector;
	private InputMultiplexer inputMultiplexer;

	private SceneEntity background;
	private Array<Onigiri> onigiriArmy;
	
	//TODO katana swing �adne z wp�ywem na level, d�ugo�� itp.
	private KatanaSwing katana;
	private Stack<Vector2> input;
	float katanaTime;
	Vector2 lastPoint = new Vector2();
	private float slashTimer;

	private GameHud gameHud;

	public TrainingScreen(final SakuraHero game) {
		this.game = game;

		background = new SceneEntity(Renderer.sceneAtlas.createSprite("dojoBackground"));
		onigiriArmy = new Array<Onigiri>();
		
		for(int i = 0; i < 5; ++i) {
			onigiriArmy.add(new SamuraiOnigiri(Renderer.sceneAtlas.createSprite("onigiriSamurai"),Renderer.sceneAtlas.createSprite("explosion")));
			onigiriArmy.add(new NinjaOnigiri(Renderer.sceneAtlas.createSprite("onigiriNinja"),Renderer.sceneAtlas.createSprite("explosion")));
			onigiriArmy.add(new OniOnigiri(Renderer.sceneAtlas.createSprite("onigiriOni"),Renderer.sceneAtlas.createSprite("explosion")));
		}
		
		katana = new KatanaSwing(game.resources.getTexture("katanaSwing"));
		input = new Stack<Vector2>(100,Vector2.class);

		gameHud = new GameHud(game);
		gameHud.initialize();
		
		inputDetector = new MultitouchGestureDetector(this);
		inputMultiplexer = new InputMultiplexer();
	}
	
	public void resetState() {
		gameHud.resetState();
		
		for(Onigiri o : onigiriArmy) {
			o.initialize(null);
			o.explosionSound = game.resources.getSoundEffect("explosion");
			o.attackSound1 = game.resources.getSoundEffect("attack1");
			o.attackSound2 = game.resources.getSoundEffect("attack2");
			o.deathSound1 = game.resources.getSoundEffect("death1");
			o.deathSound2 = game.resources.getSoundEffect("death2");	
			o.options = game.options;
		}
		
		onigiriArmy.get(0).setActive(true);
		onigiriArmy.get(1).setActive(true);
		onigiriArmy.get(2).setActive(true);
		onigiriArmy.get(3).setActive(true);
		onigiriArmy.get(4).setActive(true);
		onigiriArmy.get(5).setActive(true);
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

		if(Gdx.input.isKeyPressed(Keys.ENTER))
			game.setScreen(pauseScreen);
	}

	@Override
	public void processLogic(final float deltaTime) {
		
		int enemyHitAmount = 0;

		for(Onigiri o : onigiriArmy)
			if(o.isActive()) {
				o.update(deltaTime);

				if(input.size > 3)
					enemyHitAmount += o.collisionDetection(input);
			}
		
		gameHud.updateTrainingHud(enemyHitAmount, deltaTime);
		
		

		katana.update(input);

		katanaTime += deltaTime;

		//mo�e sterowanie czasem nie jest wcale takie g�upie
		if(input.size > 2 && katanaTime > Timer.TIME_STEP*2) {
			input.pop();
			input.pop();
			katanaTime = 0.0f;
		}	
	}

	@Override
	public void processRendering() { 
		Renderer.clearScreen();
		Renderer.backgroundStage.draw();
		Renderer.sceneStage.draw();
		katana.draw(Renderer.sceneStage.getCamera());
		Renderer.hudStage.draw();
	}

	@Override
	public void resize(final int width, final int height) {}

	@Override
	public void resume() {}

	@Override
	public void show() {			
		Renderer.backgroundStage.addActor(background);

		for(Onigiri o : onigiriArmy)
			if(o.isActive())
				o.setupRendering(Renderer.sceneStage);
		
		gameHud.initializeTrainingHUD();
			
		inputMultiplexer.clear();
		inputMultiplexer.addProcessor(inputDetector);
		inputMultiplexer.addProcessor(this);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void dispose() {}

	@Override
	public void hide() {
		Renderer.backgroundStage.clear();
		Renderer.sceneStage.clear();
		Renderer.hudStage.clear();
	}

	@Override
	public void pause() {}

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
			pauseScreen.backScreen = this;
			
			game.playMusic.pause();
			game.setScreen(pauseScreen);
		}

		return true;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
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

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		input.clear();
		slashTimer = 0.0f;
		
		String id = "sword"+ MathUtils.random(1,13);
		long i = game.resources.getSoundEffect(id).play();
		game.resources.getSoundEffect(id).setVolume(i, game.options.soundVolume);
		
		return false;
	}

	public static float distSq(Vector2 p1, Vector2 p2) {
		float dx = p1.x - p2.x, dy = p1.y - p2.y;
		return dx * dx + dy * dy;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		slashTimer += Timer.TIME_STEP;

		if(slashTimer > 0.2f)
			return true;

		final float maxLength = 10.0f;

		float swingLength = 0.0f;

		for(int i = 0; i < input.size-1; ++i)
			swingLength += input.get(i).dst(input.get(i+1));

		//	Gdx.app.log("distance", String.valueOf(swingLength));

		// TODO wci�� nie da si� �adnego okr�gu narysowa� i s� artefakty

		if(input.size < 100) {

			Vector2 stageCoords = new Vector2();
			//cos innego
			Renderer.sceneStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

			// this is dst2 in Vector class
			float lenSq = distSq(stageCoords,lastPoint);

			float minDistanceSq = 25.0f;

			if (lenSq >= minDistanceSq) {
				input.insert(stageCoords);
				lastPoint.set(stageCoords.x, stageCoords.y);
			}
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
