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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;


//TODO potrzebny jakiœ GUIItem - tekst lub grafika któr¹ mo¿na nacisn¹æ -> czyli musi to byæ Actor

public class CreditsScreen extends GameScreen implements GestureListener  {

	public MenuScreen menuScreen;

	private SakuraHero game;
	private GestureDetector inputDetector;

	private FallingLeavesEffect fallingSakura;

	private SceneEntity background;
	private SceneEntity credits;
	private SceneEntity back;

	private Label codeDesignAnimation;
	private Label jablonski;
	private Label graphics;
	private Label harasimiuk;
	private Label music;
	private Label macleod;

	public CreditsScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;

		background = new SceneEntity(game.resources.getTexture("menuBackground"));  

		LabelStyle style = new LabelStyle(game.resources.getFont("defaultFont"),Color.WHITE);
		LabelStyle styleSmall = new LabelStyle(game.resources.getFont("smallFont"),Color.WHITE);

		codeDesignAnimation = new Label("Code, design, animation",styleSmall);
		jablonski = new Label("Szymon Jablonski",style);
		graphics = new Label("Graphics",styleSmall);
		harasimiuk = new Label("Malgorzata Harasimiuk", style);
		music = new Label("Music",styleSmall);
		macleod = new Label("Kevin Macleod", style);

		inputDetector = new GestureDetector(this);   
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
			credits = new SceneEntity(game.resources.getTexture("creditsBig"));
			back = new SceneEntity(game.resources.getTexture("back"),"Back");	
		}
		else {
			credits = new SceneEntity(game.resources.getTexture("creditsBigJap"));
			back = new SceneEntity(game.resources.getTexture("backJap"),"Back");	
		}

		initializeInterface();

		Renderer.backgroundStage.addActor(background);
		Renderer.hudStage.addActor(credits);
		Renderer.hudStage.addActor(back);

		Renderer.hudStage.addActor(codeDesignAnimation);
		Renderer.hudStage.addActor(jablonski);
		Renderer.hudStage.addActor(graphics);
		Renderer.hudStage.addActor(harasimiuk);
		Renderer.hudStage.addActor(music);
		Renderer.hudStage.addActor(macleod);

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
		codeDesignAnimation.setTouchable(Touchable.disabled);
		jablonski.setTouchable(Touchable.disabled);
		graphics.setTouchable(Touchable.disabled);
		harasimiuk.setTouchable(Touchable.disabled);
		music.setTouchable(Touchable.disabled);
		macleod.setTouchable(Touchable.disabled);

		credits.getSprite().setX((Configuration.getWidth()-credits.getSprite().getWidth())*0.5f);	
		credits.getSprite().setY(Configuration.getHeight()*0.90f - credits.getSprite().getHeight());

		codeDesignAnimation.setX((Configuration.getWidth()-codeDesignAnimation.getTextBounds().width)*0.5f);	
		codeDesignAnimation.setY(Configuration.getHeight()*0.70f - codeDesignAnimation.getTextBounds().height);

		jablonski.setX((Configuration.getWidth()-jablonski.getTextBounds().width)*0.5f);	
		jablonski.setY(Configuration.getHeight()*0.60f - jablonski.getTextBounds().height);

		graphics.setX((Configuration.getWidth()-graphics.getTextBounds().width)*0.5f);	
		graphics.setY(Configuration.getHeight()*0.50f - graphics.getTextBounds().height);

		harasimiuk.setX((Configuration.getWidth()-harasimiuk.getTextBounds().width)*0.5f);	
		harasimiuk.setY(Configuration.getHeight()*0.40f - harasimiuk.getTextBounds().height);

		music.setX((Configuration.getWidth()-music.getTextBounds().width)*0.5f);	
		music.setY(Configuration.getHeight()*0.30f - music.getTextBounds().height);

		macleod.setX((Configuration.getWidth()-macleod.getTextBounds().width)*0.5f);	
		macleod.setY(Configuration.getHeight()*0.20f - macleod.getTextBounds().height);
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
