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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

//TODO wszêdzie spadaj¹ce kwiaty wiœni - nawet je¿eli kosztem wielu wartsw renderingu - docelowo d¹¿ymy do jednej
//TODO Stage per stan??
public class MenuScreen extends GameScreen implements GestureListener {

	public ModeSelectionScreen modeSelectionScreen;
	public CreditsScreen creditsnScreen;
	public OptionsScreen optionsScreen;

	private SakuraHero game;
	private GestureDetector inputDetector;

	private SpriteActor background;

	private FallingLeavesEffect fallingSakura;
	private SpriteBatch stateBatch;
	private Stage stateStage;

	//TODO pomyœleæ nad rendererm globalnym. Tylko po co? Mo¿e nie w Engine, a jakiœ custom na potrzeby Danego stanu - jeden na backgroud
	//jeden na hud i jeden na scene. Reszte realizowaæ poprzez dodatki
	//Jakis Renderer2D czy cos,
	
	private SpriteBatch backgroundBatch;
	private Stage backgroundStage;

	private SpriteActor menu;
	private SpriteActor play;
	private SpriteActor options;
	private SpriteActor credits;
	private SpriteActor exit;

	private Music menuMusic;

	public MenuScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;

		background = new SpriteActor(game.resources.getTexture("menuBackground"));

		stateBatch = new SpriteBatch();
		stateStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,stateBatch);

		backgroundBatch = new SpriteBatch();
		backgroundStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,backgroundBatch);



		inputDetector = new GestureDetector(this);
		//		

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
		backgroundStage.draw();
		fallingSakura.renderEffect();
		stateStage.draw();
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
	}

	@Override
	public void hide() {

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

		if(Configuration.getInstance().getSelectedLanguage() == Language.ENGLISH) {
			menu = new SpriteActor(game.resources.getTexture("menu"));
			play = new SpriteActor(game.resources.getTexture("play"),"Play");
			options = new SpriteActor(game.resources.getTexture("options"),"Options");
			credits = new SpriteActor(game.resources.getTexture("credits"),"Credits");
			exit = new SpriteActor(game.resources.getTexture("exit"),"Exit");
		}
		else {
			menu = new SpriteActor(game.resources.getTexture("menu"));
			play = new SpriteActor(game.resources.getTexture("play"),"Play");
			options = new SpriteActor(game.resources.getTexture("options"),"Options");
			credits = new SpriteActor(game.resources.getTexture("credits"),"Credits");
			exit = new SpriteActor(game.resources.getTexture("exit"),"Exit");
		}

		initializeInterface();

		stateStage.clear();
		backgroundStage.addActor(background);


		stateStage.addActor(menu);
		stateStage.addActor(play);
		stateStage.addActor(options);
		stateStage.addActor(credits);
		stateStage.addActor(exit);

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
		menu.getSprite().setX((Configuration.getWidth()-menu.getSprite().getWidth())*0.5f);	
		menu.getSprite().setY(Configuration.getHeight()*0.90f - menu.getSprite().getHeight());

		play.getSprite().setX((Configuration.getWidth()-play.getSprite().getWidth())*0.5f);	
		play.getSprite().setY(Configuration.getHeight()*0.65f - play.getSprite().getHeight());
		options.getSprite().setX((Configuration.getWidth()-options.getSprite().getWidth())*0.5f);	
		options.getSprite().setY(Configuration.getHeight()*0.50f - options.getSprite().getHeight());
		credits.getSprite().setX((Configuration.getWidth()-credits.getSprite().getWidth())*0.5f);	
		credits.getSprite().setY(Configuration.getHeight()*0.35f - credits.getSprite().getHeight());
		exit.getSprite().setX((Configuration.getWidth()-exit.getSprite().getWidth())*0.5f);	
		exit.getSprite().setY(Configuration.getHeight()*0.20f - exit.getSprite().getHeight());		

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
		stateStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = stateStage.hit(stageCoords.x, stageCoords.y, true);

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

			game.setScreen(modeSelectionScreen);
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
