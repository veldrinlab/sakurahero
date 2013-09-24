package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.fx.FadeEffectParameters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

//TODO pytanie czy nie zrobic jakiejs abstrakcji gry je¿eli chodzo o stany
//wszêdzie wpierdalam ju¿ pointer do konkternej klasy gry

/**
 * Class represents Splash screen. Show game/engine/team logo. It is part of Intro state.
 * @author Szymon Jab³oñski
 *
 */
public class SplashScreen extends GameScreen {

	private GameScreen nextScreen;
	
	private SakuraHero game;
	private SceneEntity splash;
	private FadeEffectParameters fade;
	
	private float effectTime;
	private float elapsedTime;

    public SplashScreen(final SakuraHero game, final FadeEffectParameters fadeParams, final GameScreen nextScreen) {
    	this.game = game;
    	this.fade = fadeParams;
    	this.nextScreen = nextScreen;
    	splash = new SceneEntity(game.resources.getTexture(fadeParams.textureName));
    }
    
	@Override
	public void processInput() {		

	}

	@Override
	public void processLogic(final float deltaTime) {
		
		if((fade.skippable && fade.skippableWhileFadingIn && Gdx.input.justTouched()) || elapsedTime > effectTime) {
			game.setScreen(nextScreen);
			dispose();
		}
		
		elapsedTime += deltaTime;
		
		if(elapsedTime < fade.fadeInTime)
			splash.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
		else if (elapsedTime < fade.fadeInTime + fade.stayTime)
			splash.getSprite().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		else if(elapsedTime < fade.fadeInTime + fade.stayTime + fade.fadeOutTime)
			splash.getSprite().setColor(1.0f, 1.0f, 1.0f, effectTime-elapsedTime);
	}

	@Override
	public void processRendering() {
		Renderer.clearScreen(Color.BLACK);
		Renderer.backgroundStage.draw();
	}
    
	@Override
	public void render(final float deltaTime) {
		
		if(deltaTime > 0.5f)
			return;
		
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
	}
	
	@Override
	public void hide() {
		Renderer.backgroundStage.clear();
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
		effectTime = fade.fadeInTime + fade.stayTime + fade.fadeOutTime;
		elapsedTime = 0.0f;
		splash.getSprite().setColor(1.0f, 1.0f, 1.0f, elapsedTime);
		Renderer.backgroundStage.addActor(splash);
	}

	@Override
	public void dispose() {

	}
}
