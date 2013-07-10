package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.Language;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;

import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class CreditsScreen extends GameScreen implements GestureListener  {
	
	public MenuScreen menuScreen;
	
	private SakuraHero game;
	private GestureDetector inputDetector;
	
	private FallingLeavesEffect fallingSakura;
	private SpriteBatch stateBatch;
	private Stage stateStage;
	
	private SpriteActor background;
	private SpriteActor credits;
	private SpriteActor back;
	
	public CreditsScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;
		
		stateBatch = new SpriteBatch();
		stateStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,stateBatch);
		
		
		background = new SpriteActor(game.resources.getTexture("menuBackground"));  
		
		
//		background = new SpriteActor(game.resources.getTexture("background"));   
//		
//    	LabelStyle style = new LabelStyle(Renderer.defaultFont,Color.WHITE);
//    	
//    	authors = new Label("Authors",style);
//    	jablonski = new Label("Szymon Jablonski - code",style);
//    	zubrzycki = new Label("Mariusz Zubrzycki - 3D graphics",style);
//    	lachowicz = new Label("Ewa Lachowicz - 2D graphics",style);
//    	filip = new Label("Piotr Maletka - audio",style);
//    	
//    	backToMenu = new Label("Back to menu",style);
//
    	inputDetector = new GestureDetector(this);    	
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		Renderer.defaultStage.clear();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
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
	public void resize(final int width, final int height) {
		Renderer.defaultStage.setViewport(Configuration.getWidth(), Configuration.getHeight(), false);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void show() {
		
		if(Configuration.getInstance().getSelectedLanguage() == Language.ENGLISH) {
			credits = new SpriteActor(game.resources.getTexture("creditsBig"));
			back = new SpriteActor(game.resources.getTexture("back"),"Back");	
		}
		else {
			credits = new SpriteActor(game.resources.getTexture("creditsBigJap"));
			back = new SpriteActor(game.resources.getTexture("backJap"),"Back");	
		}
		
    	initializeInterface();
    	
		Renderer.defaultStage.clear();
		stateStage.clear();
    	Renderer.defaultStage.addActor(background);
    	stateStage.addActor(credits);
    	stateStage.addActor(back);
    	
//    	Renderer.defaultStage.addActor(background);
//    	Renderer.defaultStage.addActor(authors);
//    	Renderer.defaultStage.addActor(jablonski);
//    	Renderer.defaultStage.addActor(zubrzycki);
//    	Renderer.defaultStage.addActor(lachowicz);
//    	Renderer.defaultStage.addActor(filip);
//    	Renderer.defaultStage.addActor(backToMenu);
    	Gdx.input.setInputProcessor(inputDetector); 	
	}

	@Override
	public void processInput() {
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
		stateStage.draw();
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
		
		credits.getSprite().setX((Configuration.getWidth()-credits.getSprite().getWidth())*0.5f);	
		credits.getSprite().setY(Configuration.getHeight()*0.90f - credits.getSprite().getHeight());
		
//		backToMenu.setName("Back");
//		
//		authors.setTouchable(Touchable.disabled);
//		jablonski.setTouchable(Touchable.disabled);
//		zubrzycki.setTouchable(Touchable.disabled);
//		lachowicz.setTouchable(Touchable.disabled);
//		filip.setTouchable(Touchable.disabled);
//		
//		authors.setX((Configuration.getInstance().width-authors.getTextBounds().width)*0.5f);	
//		authors.setY(Configuration.getInstance().height*0.90f - authors.getTextBounds().height);
//		jablonski.setX((Configuration.getInstance().width-jablonski.getTextBounds().width)*0.5f);	
//		jablonski.setY(Configuration.getInstance().height*0.75f - jablonski.getTextBounds().height);
//		zubrzycki.setX((Configuration.getInstance().width-zubrzycki.getTextBounds().width)*0.5f);	
//		zubrzycki.setY(Configuration.getInstance().height*0.65f - zubrzycki.getTextBounds().height);
//		lachowicz.setX((Configuration.getInstance().width-lachowicz.getTextBounds().width)*0.5f);	
//		lachowicz.setY(Configuration.getInstance().height*0.55f - lachowicz.getTextBounds().height);
//		filip.setX((Configuration.getInstance().width-filip.getTextBounds().width)*0.5f);	
//		filip.setY(Configuration.getInstance().height*0.45f - filip.getTextBounds().height);
//		backToMenu.setX((Configuration.getInstance().width-backToMenu.getTextBounds().width)*0.5f);	
//		backToMenu.setY(Configuration.getInstance().height*0.30f - backToMenu.getTextBounds().height);
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
	public boolean tap(float x, float y, int arg2, int arg3) {
		Vector2 stageCoords = Vector2.Zero;
		stateStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = stateStage.hit(stageCoords.x, stageCoords.y, true);
		
		if(actor == null)
			return false;
	
		if(actor.getName().equals("Back")) {
			
//			if(Configuration.getInstance().soundOn)
//				game.resources.getSoundEffect("selection").play();
			game.setScreen(menuScreen);
		}
		
		return true;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
