package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.FixedList;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakurahero.KatanaSwing;
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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PlayScreen extends GameScreen implements MultitouchGestureListener, InputProcessor {

	public PauseScreen pauseScreen;
	public GameOverScreen gameOverScreen;
	private SakuraHero game;
	private MultitouchGestureDetector inputDetector;
	private InputMultiplexer inputMultiplexer;

	// w³aœciwy kod
	private SceneEntity pauseButton;
	private SceneEntity background;

	// level Editor

	//
	public SakuraTree tree;

	//TODO To nie do koñca to bêdzie - chyba ¿e jakiœ nowy effekt/wersja, chodzi o spadanie 4 p³atków/kwiat
	//tez element klasy drzewa
	private FallingLeavesEffect fallingSakura;
	
	// enemy
	private SceneEntity enemy;

	//TODO try to use Libgdx Animation class or write something own
	private SceneEntity explosion;
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

	float leafAccum = 1.0f;


	public PlayScreen(final SakuraHero game) {
		this.game = game;

		pauseButton = new SceneEntity(Renderer.sceneAtlas.createSprite("pauseButton"),"Pause");
	//	background = new SceneEntity(Renderer.sceneAtlas.createSprite("natsuBackground"));
		inputDetector = new MultitouchGestureDetector(this);



		// level Editor

		tree = new SakuraTree(Renderer.sceneAtlas.createSprite("tree"),Renderer.sceneAtlas.createSprite("sakuraFlower"));


		//enemey
		enemy = new SceneEntity(Renderer.sceneAtlas.createSprite("onigiriSamurai"));

		enemy.getSprite().setPosition(350.0f,200.0f);
		enemy.collisionCircle.set(350.0f, 200.0f, 64.0f);

		explosion = new SceneEntity(Renderer.sceneAtlas.createSprite("explosion"));
		explosion.getSprite().setPosition(350.0f,200.0f);
		explosion.getSprite().setSize(128.0f, 128.0f);
		explosion.getSprite().setRegion(128.0f*frameAmount, 0, 128, 128);

		//TODO katana
		katana = new KatanaSwing();
		katana.texture = new Texture(Gdx.files.internal("swingTexture.png"));
		input = new FixedList<Vector2>(100,Vector2.class);
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

		leafAccum -= deltaTime*0.25f;
		leafAccum = MathUtils.clamp(leafAccum, 0.0f, 1.0f);

		fallingSakura.updateEffect(deltaTime);
		fallingSakura.setLeavesAlpha(leafAccum);
		
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
		Renderer.backgroundStage.draw();
		tree.render();
		Renderer.sceneStage.draw();
		katana.draw(tree.sakuraTreeStage.getCamera());
		Renderer.hudStage.draw();
		
		fallingSakura.renderEffect();
	}

	@Override
	public void resize(final int width, final int height) {

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

		fallingSakura = new FallingLeavesEffect(3);
		fallingSakura.setFallingBoundary(250-32.0f, 150.0f, 250+32.0f, 150+32.0f);
		fallingSakura.initializeEffect();


		//TODO naprawic troche
		background = new SceneEntity(Renderer.sceneAtlas.createSprite(game.options.worldName));
		Renderer.backgroundStage.addActor(background);
		
		//todo to jakoœ po³¹czyæ ze sob¹
		Renderer.sceneStage.addActor(enemy);
		Renderer.sceneStage.addActor(explosion);
		
		

		//TODO hud stage
		Renderer.hudStage.addActor(pauseButton);

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

		//test
		fallingSakura.setFallingBoundary(stageCoords.x-32.0f, stageCoords.y, stageCoords.x+32.0f, stageCoords.y);
		fallingSakura.initializeEffect();
		leafAccum = 1.0f;
		
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
//				SceneEntity flower = new SceneEntity(game.resources.getTexture("sakuraFlower"));
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
