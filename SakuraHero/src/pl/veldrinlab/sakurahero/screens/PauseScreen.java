package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;
import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class PauseScreen extends GameScreen implements GestureListener  {
	
	public PlayScreen playScreen;
	public MenuScreen menuScreen;
	
	private SakuraHero game;
	private GestureDetector inputDetector;

	private FrameBuffer renderTarget;
	private Stage pauseStage;
	private SpriteBatch pauseBatch;
	private SpriteActor pauseBackground;
	
	private Label pause;
	private Label resume;
	private Label menu;
	private Label exit;
	private float alpha;
	
	public PauseScreen(final SakuraHero game) {
		this.game = game;
		
//		LabelStyle style = new LabelStyle(Renderer.defaultFont,Color.WHITE);
//		
//		pauseBatch = new SpriteBatch();
//		renderTarget = new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
//		pauseStage = new Stage(Configuration.getInstance().width, Configuration.getInstance().height, false, pauseBatch);
//		pauseBackground = new SpriteActor(renderTarget.getColorBufferTexture());
//		pauseBackground.getSprite().flip(false,true);
//		
//		pause = new Label("Pause",style);
//		resume = new Label("Resume",style);
//		menu = new Label("Back to menu",style);
//		exit = new Label("Exit",style);
//		
//		inputDetector = new GestureDetector(this);
//		
//		alpha = 0.4f;
//		initializeInterface();
	}
	
	public FrameBuffer getFrameBuffer() {
		return renderTarget;
	}
	
	@Override
	public void dispose() {
		pauseBatch.dispose();
		pauseStage.dispose();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		pauseStage.clear();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
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
 	//	Renderer.defaultStage.setViewport(Configuration.getInstance().width, Configuration.getInstance().height, false);		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {	
//		pauseStage.addActor(pauseBackground);
//		pauseStage.addActor(pause);
//		pauseStage.addActor(resume);
//		pauseStage.addActor(menu);
//		pauseStage.addActor(exit);
//		
//		Gdx.input.setInputProcessor(inputDetector);
	}

	@Override
	public void processInput() {
	}

	@Override
	public void processLogic(final float deltaTime) {
		
	}

	@Override
	public void processRendering() {
//		Renderer.clearScreen();
//		Renderer.enterOrthoMode();
//		pauseBackground.getSprite().setTexture(renderTarget.getColorBufferTexture());
//		pauseBackground.getSprite().setColor(1.0f,1.0f,1.0f,alpha);
//		pauseStage.draw();
//		Renderer.leaveOrthoMode();	
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
//		pause.setTouchable(Touchable.disabled);
//		
//		resume.setName("Resume");
//		menu.setName("Menu");
//		exit.setName("Exit");
//		
//		pause.setX((Configuration.getInstance().width-pause.getTextBounds().width)*0.5f);	
//		pause.setY(Configuration.getInstance().height*0.75f - pause.getTextBounds().height);
//		
//		resume.setX((Configuration.getInstance().width-resume.getTextBounds().width)*0.5f);	
//		resume.setY(Configuration.getInstance().height*0.50f - resume.getTextBounds().height);
//		menu.setX((Configuration.getInstance().width-menu.getTextBounds().width)*0.5f);	
//		menu.setY(Configuration.getInstance().height*0.40f - menu.getTextBounds().height);
//		exit.setX((Configuration.getInstance().width-exit.getTextBounds().width)*0.5f);	
//		exit.setY(Configuration.getInstance().height*0.30f - exit.getTextBounds().height);
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
		pauseStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = pauseStage.hit(stageCoords.x, stageCoords.y, true);

		if(actor == null)
			return false;

//		if(actor.getName().equals("Resume")) {
//			if(Configuration.getInstance().soundOn)
//				game.resources.getSoundEffect("selection").play();
//		
//			game.setScreen(playScreen);
//		}
//		else if(actor.getName().equals("Menu")) {
//			if(Configuration.getInstance().soundOn)
//				game.resources.getSoundEffect("selection").play();
//			
//			if(Configuration.getInstance().musicOn)
//				game.resources.getMusic("gameOverMusic").stop();
//			game.setScreen(menuScreen);
//		}
//		else if(actor.getName().equals("Exit")) {
//			if(Configuration.getInstance().soundOn)
//				game.resources.getSoundEffect("selection").play();
//		
//			Gdx.app.exit();
//		}
		return true;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
