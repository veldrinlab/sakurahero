package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.Language;
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

	private SceneEntity background;

	private SceneEntity logoAng;
	private SceneEntity katana;
	private SceneEntity shine;

	//TODO maybe change to only one later - when we will have TextureAtlas for all GUI elements. One SceneEntity - status
	//TODO SceneEntity is very bad name. We had it in Cows and Expendables but it is really but I think.
	private SceneEntity loading;
	private SceneEntity tapToContinue;

	private boolean fadeState;
	private boolean loadingState;
	private boolean katanaState;
	private boolean shineState;
	private boolean readyToGo;

	private float fadeInAlpha;
	private float blinking;

	// katana animation

	private Vector2 direction;
	private float timeDistance;
	private float velocity;

	// shine animation
	private float shineTime;


	//
	private FallingLeavesEffect fallingSakura;

	public LoadingScreen(final SakuraHero game) {
		this.game = game;

		background = new SceneEntity(game.resources.getTexture("menuBackground"));
				
		katana = new SceneEntity(game.resources.getTexture("katana"));
		shine = new SceneEntity(game.resources.getTexture("shine"));

		shine.getSprite().setColor(1.0f, 1.0f, 1.0f, 0.0f);
		shine.getSprite().setSize(Configuration.getWidth(), Configuration.getHeight());

		// katana
		direction = new Vector2(-1.0f,-1.0f);
		timeDistance = 0.75f;
		velocity = 750.0f;

		//shine
		shineTime = 1.0f;

		inputDetector = new GestureDetector(this);
	}

	@Override
	public void processInput() {

	}

	@Override
	public void processLogic(final float deltaTime) {

		fallingSakura.updateEffect(deltaTime);
		/*
		 * STATE data flow
		 * 
		 * Fade in with loading
		 * Loading 
		 * Finish loading - change to tap to start
		 * Perform katana animation
		 * Start blinking and up screen ready to finish flag
		 * 
		 */


		if(fadeState) {
			fadeInAlpha += deltaTime*1.0f;
			fadeInAlpha = MathUtils.clamp(fadeInAlpha, 0.0f, 1.0f);

			background.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
			loading.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
			logoAng.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);

			if(fadeInAlpha > 0.99f) {
				fadeState = false;
				loadingState = true;
				
				game.resources.loadResources(Configuration.getResourcePath());
			}
		}
		else if(loadingState) {
			//load
			if(game.resources.updateLoading()) {

				//if loading completed
				loading.getSprite().setColor(1.0f,1.0f,1.0f,0.0f);
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
			shine.getSprite().setColor(1.0f, 1.0f, 1.0f, shineTime);
			
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
			tapToContinue.getSprite().setColor(1.0f, 1.0f, 1.0f, (float) ((Math.sin(blinking)+1.0f)/2.0f));
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

		//TU NIE LOGIKA, to ogarniaæ kwestie zawartoœci sceny tylko i wy³¹cznie, ew. init jak coœ siê powtarza
		// jednorazowa logika show() w konstruktorze
		
		//		Renderer.enterOrthoMode();
		//		if(Configuration.getInstance().musicOn) {
		//			menuMusic.play();
		//			menuMusic.setVolume(0.1f);
		//		}
		//
		
		//TODO mo¿e jakoœ inaczej? choæ show jest raz wiêc jeden chuj
		
		if(game.options.language == Language.ENGLISH) {
			loading = new SceneEntity(game.resources.getTexture("loadingAng"));
			tapToContinue = new SceneEntity(game.resources.getTexture("tapToAng"));
			logoAng = new SceneEntity(game.resources.getTexture("logoAng"));
		}
		else {
			loading = new SceneEntity(game.resources.getTexture("loadingJap"));
			tapToContinue = new SceneEntity(game.resources.getTexture("tapToJap"));
			logoAng = new SceneEntity(game.resources.getTexture("logoJap"));
		}
		
		fallingSakura = game.fallingSakura;
		
		Renderer.backgroundStage.addActor(background);
		
		Renderer.hudStage.addActor(katana);
		Renderer.hudStage.addActor(logoAng);
		Renderer.hudStage.addActor(loading);
		Renderer.hudStage.addActor(tapToContinue);
		Renderer.hudStage.addActor(shine);

		fadeState = true;

		fallingSakura.setLeavesAlpha(0.0f);
		
		background.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
		logoAng.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
		loading.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
		tapToContinue.getSprite().setColor(1.0f,1.0f,1.0f,fadeInAlpha);
		Gdx.input.setInputProcessor(inputDetector);
		
//		
		initializeInterface();
		
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

		logoAng.getSprite().setX((Configuration.getWidth()-logoAng.getSprite().getWidth())*0.5f);	
		logoAng.getSprite().setY(Configuration.getHeight()*0.80f - logoAng.getSprite().getHeight());

		katana.getSprite().setX((Configuration.getWidth()-katana.getSprite().getWidth())*0.5f);	
		katana.getSprite().setY(Configuration.getHeight()*1.00f - katana.getSprite().getHeight());

		//TODO smarter
		katana.getSprite().setPosition(325.0f, 480.0f-15.0f-katana.getSprite().getHeight());

		loading.getSprite().setX((Configuration.getWidth()-loading.getSprite().getWidth())*0.5f);
		loading.getSprite().setY(Configuration.getHeight()*0.25f -loading.getSprite().getHeight());

		tapToContinue.getSprite().setX((Configuration.getWidth()-tapToContinue.getSprite().getWidth())*0.5f);
		tapToContinue.getSprite().setY(Configuration.getHeight()*0.25f -tapToContinue.getSprite().getHeight());

		//ruch katany

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
