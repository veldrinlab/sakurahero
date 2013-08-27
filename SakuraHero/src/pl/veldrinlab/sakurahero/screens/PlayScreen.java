package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.FixedList;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakurahero.KatanaSwing;
import pl.veldrinlab.sakurahero.SakuraLeafDescriptor;
import pl.veldrinlab.sakurahero.SakuraTree;
import pl.veldrinlab.sakurahero.SakuraTreeDescriptor;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureDetector;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

//
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
	private InputMultiplexer inputMultiplexer;

	// na razie chuj tam z optymalizacj¹ tego - jakas scena z tego 
	
	private SpriteBatch backgroundBatch;
	private Stage backgroundStage;
	
	private SpriteBatch sceneBatch;
	private Stage sceneStage;
	
	//TODO hud class
	private SpriteBatch hudBatch;
	private Stage hudStage;
	
	// w³aœciwy kod
	private SpriteActor pauseButton;
	private SpriteActor background;

	// level Editor

	//
	public SakuraTree tree;

	//TODO To nie do koñca to bêdzie - chyba ¿e jakiœ nowy effekt/wersja, chodzi o spadanie 4 p³atków/kwiat
	//tez element klasy drzewa
	private FallingLeavesEffect fallingSakura;
	
	// enemy
	private SpriteActor enemy;

	//TODO try to use Libgdx Animation class or write something own
	private SpriteActor explosion;
	private float animationAccumulator;
	private int frameAmount = 15;
	private int currentFrame = 0;
	private float FRAME_TIME = 0.020f;

	boolean collisionOccured;

	// katana swing
	KatanaSwing katana;
	FixedList<Vector2> input;

	float katanaTime;
	Vector2 lastPoint = new Vector2();



	public PlayScreen(final SakuraHero game) {
		this.game = game;

		pauseButton = new SpriteActor(game.resources.getTexture("pauseButton"),"Pause");
		background = new SpriteActor(new Texture(Gdx.files.internal("test.png")));
		inputDetector = new MultitouchGestureDetector(this);



		// level Editor

		tree = new SakuraTree(game.resources.getTexture("tree"),game.resources.getTexture("sakuraFlower"));


		//enemey
		enemy = new SpriteActor(game.resources.getTexture("onigiriSamurai"));

		enemy.getSprite().setPosition(350.0f,200.0f);
		enemy.collisionCircle.set(350.0f, 200.0f, 64.0f);

		explosion = new SpriteActor(game.resources.getTexture("explosion"));
		explosion.getSprite().setPosition(350.0f,200.0f);
		explosion.getSprite().setSize(128.0f, 128.0f);
		explosion.getSprite().setRegion(128.0f*frameAmount, 0, 128, 128);

		//TODO katana
		katana = new KatanaSwing();
		katana.texture = new Texture(Gdx.files.internal("swingTexture.png"));
		input = new FixedList<Vector2>(100,Vector2.class);

		
		
		//
		backgroundBatch = new SpriteBatch();
		sceneBatch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		backgroundStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,backgroundBatch);
		sceneStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,sceneBatch);
		hudStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,hudBatch);
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

		if(Gdx.input.isKeyPressed(Keys.ENTER))
			game.setScreen(pauseScreen);

		// level editor

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

		fallingSakura.updateEffect(deltaTime);


		// collisiom detection
		if(!collisionOccured && input.size > 0) {

			explosion.getSprite().setPosition(enemy.getSprite().getX(), enemy.getSprite().getY());

			for(int i = 0; i < input.size; ++i) {

				if(enemy.collisionCircle.contains(input.get(i).x, input.get(i).y)) {
					enemy.getSprite().setColor(1.0f, 1.0f, 1.0f, 0.0f);
					collisionOccured = true;
					break;
				}

			}
		}

		// update explosion animation
		if(collisionOccured) {
			animationAccumulator += deltaTime;

			if(animationAccumulator > FRAME_TIME) {
				currentFrame = (currentFrame+1) % frameAmount;
				explosion.getSprite().setRegion(currentFrame*128, 0, 128, 128);
				animationAccumulator = 0.0f;

				if(currentFrame == frameAmount-1)
					collisionOccured = !collisionOccured;
			}
		}


		if(enemy.getSprite().getColor().a == 0.0f) {
			enemy.getSprite().setColor(1.0f,1.0f,1.0f,1.0f);
			enemy.getSprite().setX(MathUtils.random(128.0f,600.0f));
			enemy.getSprite().setY(MathUtils.random(128.0f,350.0f));

			enemy.collisionCircle.set(enemy.getSprite().getX()+enemy.getSprite().getWidth()*0.5f, enemy.getSprite().getY()+enemy.getSprite().getHeight()*0.5f, 64.0f);

		}

		katana.update(input);

		katanaTime += deltaTime;

		//mo¿e sterowanie czasem nie jest wcale takie g³upie
		if(input.size > 0 && katanaTime > 0.02f) {
			input.pop();
			katanaTime = 0.0f;
		}
	}

	@Override
	public void processRendering() { 
		Renderer.clearScreen();
		backgroundStage.draw();
		tree.render();
		sceneStage.draw();
		katana.draw(tree.sakuraTreeStage.getCamera());
		hudStage.draw();

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

	
		backgroundStage.addActor(background);
		
		//todo to jakoœ po³¹czyæ ze sob¹
		sceneStage.addActor(enemy);
		sceneStage.addActor(explosion);
		
		

		//TODO hud stage
		hudStage.addActor(pauseButton);

		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(inputDetector);
		inputMultiplexer.addProcessor(this);

		Gdx.input.setInputProcessor(inputMultiplexer);
		//		Gdx.input.setInputProcessor(inputDetector);
		//		Gdx.input.setInputProcessor(this);

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
		hudStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = hudStage.hit(stageCoords.x, stageCoords.y, true);

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
		// Level Editor screen

		//Gdx.app.log("Level","editor");

//		Vector2 stageCoords = Vector2.Zero;
//		tree.sakuraTreeStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
//		float rotation = MathUtils.random(0.0f, 360.0f);
//
//		//		// to nie tutaj jebnac
//				SpriteActor flower = new SpriteActor(game.resources.getTexture("sakuraFlower"));
//				flower.getSprite().setPosition(stageCoords.x-flower.getSprite().getWidth()*0.5f, stageCoords.y-flower.getSprite().getHeight()*0.5f);
//				flower.getSprite().setRotation(rotation);
//				
//				tree.sakuraTreeStage.addActor(flower);
//
//		tree.leaves.leaves.add(new SakuraLeafDescriptor(stageCoords.x, stageCoords.y, rotation));
		
		
		//jakas kolekcja ich




		return true;
	}

	@Override
	public boolean touchUp(float x, float y, int pointer) {

		Gdx.app.log("test","up");
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

		Gdx.app.log("test","down");
		Vector2 stageCoords = new Vector2();
		//cos innego
		tree.sakuraTreeStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

		input.insert(stageCoords);
		lastPoint.set(stageCoords.x, stageCoords.y);

		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub

		input.clear();

		return false;
	}

	public static float distSq(Vector2 p1, Vector2 p2) {
		float dx = p1.x - p2.x, dy = p1.y - p2.y;
		return dx * dx + dy * dy;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		// jakis maksymalny rozmiar swinga

		final float maxLength = 10.0f;

		float swingLength = 0.0f;

		for(int i = 0; i < input.size-1; ++i)
			swingLength += input.get(i).dst(input.get(i+1));

		Gdx.app.log("distance", String.valueOf(swingLength));

		// TODO wci¹¿ nie da siê ³adnego okrêgu narysowaæ i s¹ artefakty

		if(swingLength < 500 && input.size < 50) {

			Vector2 stageCoords = new Vector2();
			//cos innego
			tree.sakuraTreeStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
