package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.fx.FadeEffectParameters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Class represents Splash screen. Show game/engine/team logo. It is part of Intro state.
 * @author Szymon Jab³oñski
 *
 */
public class LanguageSelectionScreen extends GameScreen implements GestureListener {

	private GameScreen nextScreen;
	private GestureDetector inputDetector;
	
	private SakuraHero game;
	private SpriteActor splash;
	private FadeEffectParameters fade;
	
	private float effectTime;
	private float elapsedTime;
	
	// nowe w stosunku do 
	
	private SpriteActor selection;
	private SpriteActor english;
	private boolean languageSelected;
	
	
	private boolean fadeInState;
	private boolean selectState;
	private boolean fadeOutState;
		
    public LanguageSelectionScreen(final SakuraHero game, final FadeEffectParameters fadeParams, final GameScreen nextScreen) {
    	this.game = game;
    	this.fade = fadeParams;
    	this.nextScreen = nextScreen;
    	splash = new SpriteActor(game.resources.getTexture(fadeParams.textureName));
    	
    	selection = new SpriteActor(game.resources.getTexture("selectLanguage"));
    	english = new SpriteActor(game.resources.getTexture("english"),"English");
    	
    	inputDetector = new GestureDetector(this);    
    }
    
	@Override
	public void processInput() {		

	}

	@Override
	public void processLogic(final float deltaTime) {

		if(fadeInState) {
			elapsedTime += deltaTime;
			elapsedTime = MathUtils.clamp(elapsedTime, 0.0f, fade.fadeInTime);
			
			splash.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
			selection.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
			english.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
			
			if(elapsedTime > fade.fadeInTime-0.001f) {
				fadeInState = false;
				selectState = true;
			}
		}
		else if(selectState) {
			splash.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
			selection.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
			english.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
		}
		else if(fadeOutState) {
			elapsedTime += deltaTime;
			elapsedTime = MathUtils.clamp(elapsedTime, 0.0f, fade.fadeOutTime);
			
			splash.getSprite().setColor(1.0f, 1.0f, 1.0f, fade.fadeOutTime-elapsedTime);
			selection.getSprite().setColor(1.0f, 1.0f, 1.0f, fade.fadeOutTime-elapsedTime);
			english.getSprite().setColor(1.0f, 1.0f, 1.0f, fade.fadeOutTime-elapsedTime);
			
			if(elapsedTime > fade.fadeOutTime-0.001f)
				game.setScreen(nextScreen);
		}
	}

	@Override
	public void processRendering() {
		Renderer.clearScreen(Color.BLACK);
		Renderer.defaultStage.draw();	
	}
    
	@Override
	public void render(final float deltaTime) {
//		if(deltaTime > 0.5f)
//			return;
//		
		processInput();
		game.getTimer().updateTimer(deltaTime);
		while(game.getTimer().checkTimerAccumulator()) {
			processLogic(Timer.TIME_STEP);
			game.getTimer().eatAccumulatorTime();
		}
		processRendering();
	}

	@Override
	public void resize(int width, int height) {
		Renderer.defaultStage.setViewport(Configuration.getInstance().descriptor.width, Configuration.getInstance().descriptor.height, false);
	}
	
	@Override
	public void hide() {
		Renderer.defaultStage.clear();
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
	
		fadeInState = true;
		
		splash.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
		selection.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
		english.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
			
	//	Renderer.defaultStage.addActor(splash);
		Renderer.defaultStage.addActor(selection);
		Renderer.defaultStage.addActor(english);
		
		selection.getSprite().setX((Configuration.getInstance().descriptor.width-selection.getSprite().getWidth())*0.5f);	
		selection.getSprite().setY(Configuration.getInstance().descriptor.height*0.90f - selection.getSprite().getHeight());
		
		english.getSprite().setX((Configuration.getInstance().descriptor.width-english.getSprite().getWidth())*0.20f);	
		english.getSprite().setY(Configuration.getInstance().descriptor.height*0.40f - english.getSprite().getHeight());
		
		english.setBounds(english.getSprite().getX(), english.getSprite().getY(), english.getSprite().getWidth(), english.getSprite().getHeight());
		
    	Gdx.input.setInputProcessor(inputDetector);
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		Vector2 stageCoords = Vector2.Zero;
		Renderer.defaultStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.defaultStage.hit(stageCoords.x, stageCoords.y, true);
		
		if(actor == null)
			return false;

		if(selectState && actor.getName().equals("English")) {
			selectState = false;
			fadeOutState = true;
			elapsedTime = 0.0f;
		}
			
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}
