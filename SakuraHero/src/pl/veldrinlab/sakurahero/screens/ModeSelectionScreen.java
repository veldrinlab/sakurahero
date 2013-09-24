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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class ModeSelectionScreen extends GameScreen implements GestureListener  {

	public MenuScreen menuScreen;
	public WorldSelectionScreen worldSelectionScreen;
	public TrainingScreen trainingScreen;
	public SurvivalScreen survivalScreen;
	
	private SakuraHero game;	
	private GestureDetector inputDetector;

	private FallingLeavesEffect fallingSakura;
	
	private SceneEntity background;
	private SceneEntity modeSelection;
	private SceneEntity back;

	private SceneEntity normalEmblem;
	private SceneEntity survivalEmblem;
	private SceneEntity trainingEmblem;
	
	private Label normal;
	private Label survival;
	private Label training;

	public ModeSelectionScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;

		background = new SceneEntity(game.resources.getTexture("menuBackground"));  
		
		LabelStyle style = new LabelStyle(game.resources.getFont("defaultFont"),Color.WHITE);

		normal = new Label("Normal",style);	
		survival = new Label("Survival",style);
		training = new Label("Training",style);

		inputDetector = new GestureDetector(this);    
	
		normalEmblem = new SceneEntity(game.resources.getTexture("onigiriSamurai"));
		survivalEmblem = new SceneEntity(game.resources.getTexture("onigiriOni"));
		trainingEmblem = new SceneEntity(game.resources.getTexture("onigiriNinja"));
		
		normalEmblem.getSprite().setRegion(0, 0, 128, 128);
		survivalEmblem.getSprite().setRegion(0, 0, 128, 128);
		trainingEmblem.getSprite().setRegion(0, 0, 128, 128);
		
		normalEmblem.getSprite().setSize(128, 128);
		survivalEmblem.getSprite().setSize(128, 128);
		trainingEmblem.getSprite().setSize(128, 128);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
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

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void show() {

		if(game.options.language == Language.ENGLISH) {
			modeSelection = new SceneEntity(game.resources.getTexture("modeSelection"));
			back = new SceneEntity(game.resources.getTexture("back"),"Back");

		}
		else {
			modeSelection = new SceneEntity(game.resources.getTexture("modeSelectionJap"));
			back = new SceneEntity(game.resources.getTexture("backJap"),"Back");
		}

		initializeInterface();

		Renderer.backgroundStage.addActor(background);
		Renderer.hudStage.addActor(modeSelection);
		Renderer.hudStage.addActor(back);

		Renderer.hudStage.addActor(normal);
		Renderer.hudStage.addActor(survival);
		Renderer.hudStage.addActor(training);
		
		Renderer.hudStage.addActor(normalEmblem);
		Renderer.hudStage.addActor(survivalEmblem);
		Renderer.hudStage.addActor(trainingEmblem);
		
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
		Renderer.backgroundStage.draw();
		fallingSakura.renderEffect();
		Renderer.hudStage.draw();
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
		normal.setY(Configuration.getHeight()*0.55f - normal.getTextBounds().height);
		
		survival.setX(Configuration.getWidth()*0.1f);
		survival.setY(Configuration.getHeight()*0.25f - survival.getTextBounds().height);
		
		training.setX(Configuration.getWidth()*0.9f - training.getTextBounds().width);
		training.setY(Configuration.getHeight()*0.25f - training.getTextBounds().height);
		
		normalEmblem.getSprite().setX(normal.getX()+normal.getTextBounds().width*0.5f-normalEmblem.getSprite().getWidth()*0.5f);
		normalEmblem.getSprite().setY(normal.getY()+normal.getTextBounds().height);
		
		survivalEmblem.getSprite().setX(survival.getX()+survival.getTextBounds().width*0.5f-survivalEmblem.getSprite().getWidth()*0.5f);
		survivalEmblem.getSprite().setY(survival.getY()+survival.getTextBounds().height);
		
		trainingEmblem.getSprite().setX(training.getX()+training.getTextBounds().width*0.5f-trainingEmblem.getSprite().getWidth()*0.5f);
		trainingEmblem.getSprite().setY(training.getY()+training.getTextBounds().height);
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
		Renderer.hudStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.hudStage.hit(stageCoords.x, stageCoords.y, true);

		if(actor == null)
			return false;
		
		if(actor.getName().equals("Normal")) {
			game.setScreen(worldSelectionScreen);
		}
		else if(actor.getName().equals("Survival")) {
			//TODO gdzie� info o odpowiednim pliku levelu
			game.setScreen(worldSelectionScreen);
		}
		else if(actor.getName().equals("Training")) {
			game.setScreen(trainingScreen);
		}
		else if(actor.getName().equals("Back")) {
			game.setScreen(menuScreen);
		}
		
		//TODO 
		//			if(Configuration.getInstance().soundOn)
		//				game.resources.getSoundEffect("selection").play();
	
		return true;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
