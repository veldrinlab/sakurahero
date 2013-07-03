package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;
import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

//TODO wszêdzie spadaj¹ce kwiaty wiœni - nawet je¿eli kosztem wielu wartsw renderingu - docelowo d¹¿ymy do jednej
//TODO Stage per stan??
public class MenuScreen extends GameScreen implements GestureListener {

	public PlayScreen playScreen;
	public CreditsScreen creditsnScreen;
	public OptionsScreen optionsScreen;
	
	private SakuraHero game;
	private GestureDetector inputDetector;

	private SpriteActor background;
	

	
	
	
	private SpriteActor menu;
	private SpriteActor play;
	private SpriteActor options;
	private SpriteActor credits;
	private SpriteActor exit;

	private Music menuMusic;

	public MenuScreen(final SakuraHero game) {
		this.game = game;

		background = new SpriteActor(game.resources.getTexture("menuBackground"));
		
		
		
		
		menu = new SpriteActor(game.resources.getTexture("menu"));
		play = new SpriteActor(game.resources.getTexture("play"),"Play");
		options = new SpriteActor(game.resources.getTexture("options"),"Options");
		credits = new SpriteActor(game.resources.getTexture("credits"),"Credits");
		exit = new SpriteActor(game.resources.getTexture("exit"),"Exit");

	
		inputDetector = new GestureDetector(this);
//		
		initializeInterface();
	}

	@Override
	public void processInput() {
	}

	@Override
	public void processLogic(final float deltaTime) {

	}

	@Override
	public void processRendering() {	
		Renderer.clearScreen();
		Renderer.defaultStage.draw();
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
		//TODO to jest chyba i tak zbêdne??/
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
		//	 TODO Auto-generated method stub
	}

	@Override
	public void show() {	
		
//		Renderer.enterOrthoMode();
//		if(Configuration.getInstance().musicOn) {
//			menuMusic.play();
//			menuMusic.setVolume(0.1f);
//		}
//
		Renderer.defaultStage.addActor(background);
		
		
		Renderer.defaultStage.addActor(menu);
		Renderer.defaultStage.addActor(play);
		Renderer.defaultStage.addActor(options);
		Renderer.defaultStage.addActor(credits);
		Renderer.defaultStage.addActor(exit);

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
		

		
		// boundsy
		
		menu.getSprite().setX((Configuration.getInstance().descriptor.width-menu.getSprite().getWidth())*0.5f);	
		menu.getSprite().setY(Configuration.getInstance().descriptor.height*0.90f - menu.getSprite().getHeight());
		
		play.getSprite().setX((Configuration.getInstance().descriptor.width-play.getSprite().getWidth())*0.5f);	
		play.getSprite().setY(Configuration.getInstance().descriptor.height*0.65f - play.getSprite().getHeight());
		options.getSprite().setX((Configuration.getInstance().descriptor.width-options.getSprite().getWidth())*0.5f);	
		options.getSprite().setY(Configuration.getInstance().descriptor.height*0.50f - options.getSprite().getHeight());
		credits.getSprite().setX((Configuration.getInstance().descriptor.width-credits.getSprite().getWidth())*0.5f);	
		credits.getSprite().setY(Configuration.getInstance().descriptor.height*0.35f - credits.getSprite().getHeight());
		exit.getSprite().setX((Configuration.getInstance().descriptor.width-exit.getSprite().getWidth())*0.5f);	
		exit.getSprite().setY(Configuration.getInstance().descriptor.height*0.20f - exit.getSprite().getHeight());		
		
		//TODO refactor this getSprite shit
		//TODO maybe some update method 
		
		play.setBounds(play.getSprite().getX(), play.getSprite().getY(), play.getSprite().getWidth(), play.getSprite().getHeight());
		options.setBounds(options.getSprite().getX(), options.getSprite().getY(), options.getSprite().getWidth(), options.getSprite().getHeight());
		credits.setBounds(credits.getSprite().getX(), credits.getSprite().getY(), credits.getSprite().getWidth(), credits.getSprite().getHeight());
		exit.setBounds(exit.getSprite().getX(), exit.getSprite().getY(), exit.getSprite().getWidth(), exit.getSprite().getHeight());
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
		Vector2 stageCoords = Vector2.Zero;
		Renderer.defaultStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.defaultStage.hit(stageCoords.x, stageCoords.y, true);

		if(actor == null)
			return false;

		Gdx.app.log("name", actor.getName());
		
		if(actor.getName().equals("Credits")) {
	//		if(Configuration.getInstance().soundOn)
	//			game.resources.getSoundEffect("selection").play();
			game.setScreen(creditsnScreen);
			return true;
		}
		else if(actor.getName().equals("Options")) {
		//	if(Configuration.getInstance().soundOn)
		//		game.resources.getSoundEffect("selection").play();
			game.setScreen(optionsScreen);
			return true;
		}
		else if(actor.getName().equals("Play")) {
		//	if(Configuration.getInstance().soundOn)
		//		game.resources.getSoundEffect("selection").play();
			
			game.setScreen(playScreen);
		//	if(Configuration.getInstance().musicOn)
		//		menuMusic.stop();
		//		
			dispose();
			return true;			
		}
		else if(actor.getName().equals("Exit")) {
		//	if(Configuration.getInstance().soundOn)
		//		game.resources.getSoundEffect("selection").play();
			Gdx.app.exit();
		}

		return false;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
