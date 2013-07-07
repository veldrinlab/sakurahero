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

	static public Matrix4 matCam = new Matrix4();

	public PauseScreen pauseScreen;
	public GameOverScreen gameOverScreen;

	private SakuraHero game;
	
	//
	private MultitouchGestureDetector inputDetector;

	private OrthographicCamera camera;

	// swing cout
	ImmediateModeRenderer20 renderer;
	Matrix4 projMatrix = new Matrix4();
	Stage stage;

	Vector2 pos = new Vector2();
	int swingPointMax = 8;

	Array<Vector2> swing;
	float accumulator = 0.5f;

	ShaderProgram shader;

	// smoke test

	SpriteBatch batch;

	FallingLeavesEffect fallingSakura;
	
	// w³aœciwy kod
	private SpriteActor pauseButton;
	private SpriteActor background;
	
	public PlayScreen(final SakuraHero game) {
		this.game = game;

		pauseButton = new SpriteActor(game.resources.getTexture("pauseButton"),"Pause");
		background = new SpriteActor(new Texture(Gdx.files.internal("test.png")));
		inputDetector = new MultitouchGestureDetector(this);


		renderer = new ImmediateModeRenderer20(false,true,0);
		swing = new Array<Vector2>();

		shader = new ShaderProgram(Gdx.files.internal("default2.vert"),Gdx.files.internal("default.frag"));


//		renderer.setShader(shader);
//		//
//		stage = new Stage(800.0f,480.0f,false);
//		batch = new SpriteBatch();

	//	batch.setShader(shader);
	
//		sprite = new Sprite( new Texture(Gdx.files.internal("test2a.png")), currentFrame*0, 0, 256, 256);
//		sprite.setColor(Color.BLACK);
//		background = new Sprite(new Texture(Gdx.files.internal("test.png")));
		
//		tree = new Sprite(new Texture(Gdx.files.internal("test3.png")));
//		
//		tree.setPosition(400-tree.getWidth()*0.5f, -100.0f);
//		tree.setScale(0.65f);
		
		

		//TDODO input same type?
	//	Gdx.input.setInputProcessor(this);
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
		
		fallingSakura.updateEffect(deltaTime);
	}

	@Override
	public void processRendering() { 
		Renderer.clearScreen();
		Renderer.defaultStage.draw();
		
		fallingSakura.renderEffect();
	
		
//		float deltaTime = 1.0f/60.0f;
//
//		if(swing.size > 0) {
//			accumulator -= deltaTime;
//
//			if(accumulator < 0.0f) {
//				accumulator = 0.5f;
//				swing.clear();
//			}
//		
		
//		batch.begin();
//		
//		sakuraAccumulator += deltaTime;
//		
//		for(int i = 0; i < leaves.size; ++i) {
//			float currentX = leaves.get(i).getX();
//			float currentY = leaves.get(i).getY();
//			
//			
//			currentX += Math.sin(leaveBalance.get(i)+sakuraAccumulator)*0.5f;
//			currentY -= 50.0f * deltaTime;
//			
//			leaves.get(i).setX(currentX);
//			leaves.get(i).setY(currentY);
//			
////			leaves.get(i).setRotation(leaves.get(i).getRotation()+deltaTime*50.0f);
//			
//			leaves.get(i).setRotation((float) (-Math.abs(Math.sin(leaveBalance.get(i)+sakuraAccumulator))*30.0f));
//			
//			
//			if(currentY < -leaves.get(i).getHeight())
//				leaves.get(i).setPosition(MathUtils.random(0.0f, 800.0f), 480.0f);
//			
//			
//			leaves.get(i).draw(batch);
//			
//		}
//		
//		batch.end();
//		
//		
//		
//		
//		Gdx.gl20.glLineWidth(15);
//		
//
//		if(swing.size > 1) {
//
//			for(int i = 0; i < swing.size-1; ++i) {
//				renderer.begin(projMatrix, GL20.GL_LINES);
//				renderer.vertex(swing.get(i).x, swing.get(i).y, 0);
//				renderer.vertex(swing.get(i+1).x, swing.get(i+1).y, 0);
//				renderer.end();
//			}
//		}
	
		
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
		
		fallingSakura = game.fallingSakura;
		
		Gdx.app.log("play", "screen show");
		Renderer.defaultStage.clear();	
		Renderer.defaultStage.addActor(background);
		Renderer.defaultStage.addActor(pauseButton);
		
//		Gdx.input.setInputProcessor(this);
		
		Gdx.input.setInputProcessor(inputDetector);
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
			Vector2 point = new Vector2();
			stage.screenToStageCoordinates(point.set(screenX, screenY));

			swing.add(point);

		}

		//		Actor actor = hudStage.hit(stageCoords.x, stageCoords.y, true);
		Gdx.app.log("touch down", " test");
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
			Vector2 point = new Vector2();
			stage.screenToStageCoordinates(point.set(screenX, screenY));

			swing.add(point);

		}
		Gdx.app.log("touch dragged", " test");
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
