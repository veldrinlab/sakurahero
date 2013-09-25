package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class LoadingScreen extends GameScreen implements GestureListener {

	private SakuraHero game;
	private GestureDetector inputDetector;
	private FallingLeavesEffect fallingSakura;
	
	private SceneEntity background;
	private SceneEntity logo;
	private SceneEntity loadingStatus;
	private SceneEntity katana;
	private SceneEntity shineEffect;

	private boolean fadeState;
	private boolean loadingState;
	private boolean katanaState;
	private boolean shineState;
	private boolean readyToGo;

	private float fadeInAlpha;
	private float blinking;
	private Vector2 direction;
	private float timeDistance;
	private float velocity;
	private float shineTime;

	public LoadingScreen(final SakuraHero game) {
		this.game = game;
		this.fallingSakura = game.fallingSakura;
		
		background = new SceneEntity(Renderer.introAtlas.createSprite("menuBackground"));
				
		logo = new SceneEntity(Renderer.guiAtlas.createSprite("logo"));
		loadingStatus = new SceneEntity(Renderer.guiAtlas.createSprite("loading"));
			
		katana = new SceneEntity(Renderer.introAtlas.createSprite("katana"));
		shineEffect = new SceneEntity(Renderer.introAtlas.createSprite("shine"));

		shineEffect.getSprite().setColor(1.0f, 1.0f, 1.0f, 0.0f);
		shineEffect.getSprite().setSize(Configuration.getWidth(), Configuration.getHeight());

		direction = new Vector2(-1.0f,-1.0f);
		timeDistance = 0.75f;
		velocity = 750.0f;
		shineTime = 1.0f;
		fadeState = true;
		
		inputDetector = new GestureDetector(this);
	
		initializeInterface();
	}

	@Override
	public void processInput() {

	}

	@Override
	public void processLogic(final float deltaTime) {

		fallingSakura.updateEffect(deltaTime);

		if(fadeState) {
			fadeInAlpha += deltaTime*1.0f;
			fadeInAlpha = MathUtils.clamp(fadeInAlpha, 0.0f, 1.0f);

			background.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
			logo.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
			loadingStatus.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
		
			if(fadeInAlpha > 0.99f) {
				fadeState = false;
				loadingState = true;
				
				game.resources.loadResources(Configuration.getResourcePath());
			}
		}
		else if(loadingState) {
			if(game.resources.updateLoading()) {	
				loadingStatus.changeEntitySprite(Renderer.guiAtlas.createSprite("tapToContinue"));
				loadingStatus.getSprite().setX((Configuration.getWidth()-loadingStatus.getSprite().getWidth())*0.5f);
				loadingStatus.getSprite().setY(Configuration.getHeight()*0.25f -loadingStatus.getSprite().getHeight());
				loadingStatus.getSprite().setColor(1.0f, 1.0f, 1.0f, 0.0f);
				loadingState = false;
				katanaState = true;
			}
		}
		else if(katanaState) {
			timeDistance -= deltaTime;

			float x = katana.getSprite().getX();
			float y = katana.getSprite().getY();

			x += direction.x*velocity*deltaTime;
			y += direction.y*velocity*deltaTime;

			katana.getSprite().setPosition(x, y);
		
			if(timeDistance < 0.0f) {
				katanaState = false;
				shineState = true;	
			}

		}
		else if(shineState) {
			//TODO sword slash sound
			shineEffect.getSprite().setColor(1.0f, 1.0f, 1.0f, shineTime);
			
			shineTime -= deltaTime;
			
			fallingSakura.setLeavesAlpha(1.0f-shineTime);
			
			if(shineTime < 0.0f) {
				fallingSakura.setLeavesAlpha(1.0f);
				shineState = false;
				readyToGo = true;				
			}
		}
		else if(readyToGo) {
			blinking += deltaTime*5.0f;
			loadingStatus.getSprite().setColor(1.0f, 1.0f, 1.0f, (float) ((Math.sin(blinking)+1.0f)/2.0f));
		}			
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
	public void hide() {
		Renderer.backgroundStage.clear();
		Renderer.hudStage.clear();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		//	 TODO Auto-generated method stub
	}

	@Override
	public void show() {	
		
		//		if(Configuration.getInstance().musicOn) {
		//			menuMusic.play();
		//			menuMusic.setVolume(0.1f);
		//		}
		//
		
		fallingSakura.setLeavesAlpha(0.0f);
		
		Renderer.backgroundStage.addActor(background);
		
		Renderer.hudStage.addActor(katana);
		Renderer.hudStage.addActor(logo);
		Renderer.hudStage.addActor(loadingStatus);
		Renderer.hudStage.addActor(shineEffect);

		Gdx.input.setInputProcessor(inputDetector);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
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
		
		background.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
		logo.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
		loadingStatus.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
				
		logo.getSprite().setX((Configuration.getWidth()-logo.getSprite().getWidth())*0.5f);	
		logo.getSprite().setY(Configuration.getHeight()*0.80f - logo.getSprite().getHeight());

		loadingStatus.getSprite().setX((Configuration.getWidth()-loadingStatus.getSprite().getWidth())*0.5f);
		loadingStatus.getSprite().setY(Configuration.getHeight()*0.25f -loadingStatus.getSprite().getHeight());

		katana.getSprite().setPosition(325.0f, 480.0f-15.0f-katana.getSprite().getHeight());

		float x = katana.getSprite().getX();
		float y = katana.getSprite().getY();

		x += -direction.x*velocity*timeDistance;
		y += -direction.y*velocity*timeDistance;

		katana.getSprite().setPosition(x, y);
	}

	@Override
	public boolean fling(float arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float arg0, float arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float arg0, float arg1, int arg2, int arg3) {	
		if(readyToGo)
			game.initializeGame();

		return false;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
