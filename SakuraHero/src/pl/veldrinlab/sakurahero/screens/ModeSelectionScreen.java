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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class ModeSelectionScreen extends GameScreen implements GestureListener  {

	public MenuScreen menuScreen;

	private SakuraHero game;	
	private GestureDetector inputDetector;

	private FallingLeavesEffect fallingSakura;
	private SpriteBatch stateBatch;
	private Stage stateStage;

	private SpriteActor background;
	private SpriteActor modeSelection;
	private SpriteActor back;

	private Label normal;
	private Label survival;
	private Label training;

	public ModeSelectionScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;

		stateBatch = new SpriteBatch();
		stateStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,stateBatch);


		background = new SpriteActor(game.resources.getTexture("menuBackground"));  
		modeSelection = new SpriteActor(new Texture(Gdx.files.internal("modeSelection.png")));
		back = new SpriteActor(game.resources.getTexture("back"),"Back");

		stateBatch = new SpriteBatch();
		stateStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,stateBatch);

		LabelStyle style = new LabelStyle(game.resources.getFont("defaultFont"),Color.WHITE);

		normal = new Label("Normal",style);	
		survival = new Label("Survival",style);
		training = new Label("Training",style);

		inputDetector = new GestureDetector(this);    

		//TODO next button

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		stateStage.clear();
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
		//	Renderer.defaultStage.setViewport(Configuration.getInstance().width, Configuration.getInstance().height, false);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void show() {

		//		if(Configuration.getInstance().getSelectedLanguage() == Language.ENGLISH) {
		//			options = new SpriteActor(game.resources.getTexture("optionsBig"));
		//			back = new SpriteActor(game.resources.getTexture("back"),"Back");
		//
		//		}
		//		else {
		//			options = new SpriteActor(game.resources.getTexture("optionsBigJap"));
		//			back = new SpriteActor(game.resources.getTexture("back"),"Back");
		//		}

		initializeInterface();

		stateStage.addActor(background);
		stateStage.addActor(modeSelection);
		stateStage.addActor(back);

		stateStage.addActor(normal);
		stateStage.addActor(survival);
		stateStage.addActor(training);

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
		//TODO pytanie czy nie layer dodatkowy do backgrounda? tak dzia³a³o to z Rendererem?
		// w tym projekcie mo¿e ju¿ rozwin¹æ silnik o to
		Renderer.clearScreen();
		stateStage.draw();
		fallingSakura.renderEffect();
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

		modeSelection.getSprite().setX((Configuration.getWidth()-modeSelection.getSprite().getWidth())*0.5f);	
		modeSelection.getSprite().setY(Configuration.getHeight()*0.90f - modeSelection.getSprite().getHeight());

		normal.setName("Normal");
		survival.setName("Survival");
		training.setName("Training");
	
		normal.setX((Configuration.getWidth()-normal.getTextBounds().width)*0.5f);
		normal.setY(Configuration.getHeight()*0.60f - normal.getTextBounds().height);
		
		survival.setX(Configuration.getWidth()*0.1f);
		survival.setY(Configuration.getHeight()*0.30f - survival.getTextBounds().height);
		
		training.setX(Configuration.getWidth()*0.9f - training.getTextBounds().width);
		training.setY(Configuration.getHeight()*0.30f - training.getTextBounds().height);
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
		//		
		//		if(actor.getName().equals("Music")) {
		//			Configuration.getInstance().musicOn = !Configuration.getInstance().musicOn;
		//			
		//			if(Configuration.getInstance().musicOn) {
		//				musicState.setText("On");
		//				game.resources.getMusic("menuMusic").play();
		//			}
		//			else {
		//				musicState.setText("Off");
		//				game.resources.getMusic("menuMusic").stop();
		//			}
		//			
		//			musicState.setX(music.getX()+music.getTextBounds().width*1.5f);
		//		}
		//		else if(actor.getName().equals("Sound")) {
		//			Configuration.getInstance().soundOn = !Configuration.getInstance().soundOn;
		//			
		//			if(Configuration.getInstance().soundOn)
		//				soundState.setText("On");
		//			else
		//				soundState.setText("Off");
		//			
		//			soundState.setX(music.getX()+music.getTextBounds().width*1.5f);
		//		}
		//		else if(actor.getName().equals("Reset")) {
		////			if(Configuration.getInstance().soundOn)
		////				game.resources.getSoundEffect("selection").play();
		////			Configuration.getInstance().highscoreDescriptor.highScore = 0;
		////			currentHighScore.setText("High Score "+ Configuration.getInstance().highscoreDescriptor.highScore);
		////			currentHighScore.setX((Configuration.getInstance().width-currentHighScore.getTextBounds().width)*0.5f);	
		//		}
		//		
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
