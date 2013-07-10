package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureDetector;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

//
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class PlayScreen extends GameScreen implements MultitouchGestureListener, InputProcessor {

	public PauseScreen pauseScreen;
	public GameOverScreen gameOverScreen;
	private SakuraHero game;
	private MultitouchGestureDetector inputDetector;


	// swing cout



	// w³aœciwy kod
	private SpriteActor pauseButton;
	private SpriteActor background;

	// level Editor
	private SpriteActor tree;

	// ca³e drzewo do jakiejœ klasy wrzuciæ
	private SpriteBatch sakuraTreeBatch;
	private Stage sakuraTreeStage;

	private FallingLeavesEffect fallingSakura;


	// katana swing
	private SpriteBatch swingBatch;
	private Stage swingStage;
	private int swingPointMax = 100;
	private Array<SpriteActor> swing;
	private float accumulator = 0.0f;


	// enemy
	private SpriteActor enemy;


	//TODO try to use Libgdx Animation class
	private SpriteActor explosion;
	private float animationAccumulator;
	private int frameAmount = 15;
	private int currentFrame = 0;
	private float FRAME_TIME = 0.020f;


	public PlayScreen(final SakuraHero game) {
		this.game = game;

		pauseButton = new SpriteActor(game.resources.getTexture("pauseButton"),"Pause");
		background = new SpriteActor(new Texture(Gdx.files.internal("test.png")));
		inputDetector = new MultitouchGestureDetector(this);



		// level Editor

		tree = new SpriteActor(game.resources.getTexture("tree"));
		tree.getSprite().setY(-10.0f);

		sakuraTreeBatch = new SpriteBatch();
		sakuraTreeStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,sakuraTreeBatch);

		// katana swing
		swing = new Array<SpriteActor>();
		swingBatch = new SpriteBatch();
		swingStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,swingBatch);


		//enemey
		enemy = new SpriteActor(game.resources.getTexture("onigiriOni"));
		enemy.getSprite().setSize(150.0f, 150.0f);
		enemy.getSprite().setPosition(350.0f,200.0f);
		enemy.collisionCircle.set(350.0f, 200.0f, 50.0f);


		explosion = new SpriteActor(game.resources.getTexture("explosion"));
		explosion.getSprite().setSize(128.0f, 128.0f);
		explosion.getSprite().setRegion(0, 0, 128, 128);
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

		if(Gdx.input.isKeyPressed(Keys.SPACE))
			game.setScreen(gameOverScreen);
	}

	@Override
	public void processLogic(final float deltaTime) {

		// update explosion animation

		animationAccumulator += deltaTime;

		if(animationAccumulator > FRAME_TIME) {
			currentFrame = (currentFrame+1) % frameAmount;
			explosion.getSprite().setRegion(currentFrame*128, 0, 128, 128);
			animationAccumulator = 0.0f;
		}
		



		fallingSakura.updateEffect(deltaTime);

		if(swing.size > 0) {
			accumulator -= deltaTime;

			if(accumulator < 0.0f) {
				accumulator = 0.5f;
				swing.clear();
				swingStage.clear();
			}

			// collisiom detection
			if(swing.size > 3) {

				for(int i = 0; i < swing.size; ++i) {

					if(enemy.collisionCircle.contains(swing.get(i).getSprite().getX(), swing.get(i).getSprite().getY())) {
						Gdx.app.log("collison", "detected");
						break;
					}

				}
			}
		}
	}

	@Override
	public void processRendering() { 
		Renderer.clearScreen();
		Renderer.defaultStage.draw();

		//	fallingSakura.renderEffect();
		sakuraTreeStage.draw();
		if(swing.size > 1)
			swingStage.draw();
	}

	@Override
	public void resize(final int width, final int height) {
		Renderer.defaultStage.setViewport(Configuration.getWidth(), Configuration.getHeight(), false);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {	
		//		if(Configuration.getInstance().musicOn) {
		//			gameMusic.play();
		//			gameMusic.setLooping(true);
		//		}

		//	Gdx.input.setInputProcessor(inputDetector);

		fallingSakura = new FallingLeavesEffect(6,game.resources.getTexture("sakuraLeaf"));
		fallingSakura.setFallingBoundary(250.0f, 150.0f, 550.0f, 200.0f);
		fallingSakura.initializeEffect();

		Gdx.app.log("play", "screen show");
		Renderer.defaultStage.clear();	
		Renderer.defaultStage.addActor(background);
		Renderer.defaultStage.addActor(pauseButton);
		Renderer.defaultStage.addActor(enemy);
		Renderer.defaultStage.addActor(explosion);

		sakuraTreeStage.addActor(tree);

		Gdx.input.setInputProcessor(inputDetector);
		Gdx.input.setInputProcessor(this);

	}

	@Override
	public void dispose() {

	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {

	}

	@Override
	public boolean tap(float x, float y, int count, int pointer) {

		Vector2 stageCoords = Vector2.Zero;
		Renderer.defaultStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.defaultStage.hit(stageCoords.x, stageCoords.y, true);


		if(actor == null)
			return false;

		if(actor.getName().equals("Pause")) {
			pauseScreen.getFrameBuffer().begin();	
			processRendering();
			pauseScreen.getFrameBuffer().end();

			//	if(Configuration.getInstance().musicOn)
			//	gameMusic.pause();

			Gdx.app.log("test", "test");
			game.setScreen(pauseScreen);
		}

		return true;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		Gdx.app.log("touch","down");

		// Level Editor screen
		Vector2 stageCoords = Vector2.Zero;
		sakuraTreeStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

		SpriteActor flower = new SpriteActor(game.resources.getTexture("sakuraFlower"));


		flower.getSprite().setPosition(stageCoords.x-flower.getSprite().getWidth()*0.5f, stageCoords.y-flower.getSprite().getHeight()*0.5f);

		// moze rotate losowy?
		flower.getSprite().setRotation(MathUtils.random(0.0f, 360.0f));
		sakuraTreeStage.addActor(flower);

		//jakas kolekcja ich


		return true;
	}

	@Override
	public boolean touchUp(float x, float y, int pointer) {
		return true;
	}

	@Override
	public boolean longPress(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float startX, float startY, float endX, float endY, float velocityX, float velocityY, int pointer) {
		//velocity X -> dodatnie to w prawo
		// > 1500

		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub


		//
		if(swing.size < swingPointMax) {

			Vector2 stageCoords = Vector2.Zero;
			swingStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

			SpriteActor a = new SpriteActor(game.resources.getTexture("sakuraFlower"));
			a.getSprite().setPosition(stageCoords.x,stageCoords.y);
			swing.add(a);
			swingStage.addActor(a);
		}

		//		Actor actor = hudStage.hit(stageCoords.x, stageCoords.y, true);
		//	Gdx.app.log("touch down", " test");
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//	
		if(swing.size < swingPointMax) {
			Vector2 stageCoords = Vector2.Zero;
			swingStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

			SpriteActor a = new SpriteActor(game.resources.getTexture("sakuraFlower"));
			a.getSprite().setPosition(stageCoords.x,stageCoords.y);
			swing.add(a);
			swingStage.addActor(a);

		}
		//	Gdx.app.log("touch dragged", " test");
		return false;
	}


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
