package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;
import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class PauseScreen extends GameScreen implements GestureListener  {
	
	public PlayScreen playScreen;
	public MenuScreen menuScreen;
	
	private SakuraHero game;
	private GestureDetector inputDetector;

	private FrameBuffer renderTarget;
	private Stage pauseStage;
	private SpriteBatch pauseBatch;
	private SpriteActor pauseBackground;
	
	private SpriteActor pause;
	private SpriteActor resume;
	private SpriteActor menu;
	private SpriteActor exit;
	
	private float alpha;
	
	public PauseScreen(final SakuraHero game) {
		this.game = game;
		
		pauseBatch = new SpriteBatch();
		renderTarget = new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
		pauseStage = new Stage(Configuration.getWidth(), Configuration.getHeight(), false, pauseBatch);
		pauseBackground = new SpriteActor(renderTarget.getColorBufferTexture());
		pauseBackground.getSprite().flip(false,true);

		pause = new SpriteActor(game.resources.getTexture("pause"));
		resume = new SpriteActor(game.resources.getTexture("resume"),"Resume");
		menu = new SpriteActor(game.resources.getTexture("menuSmall"),"Menu");
		exit = new SpriteActor(game.resources.getTexture("exit"),"Exit");
		
		inputDetector = new GestureDetector(this);
		
		alpha = 0.4f;
		initializeInterface();
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
		pauseStage.addActor(pauseBackground);
		pauseStage.addActor(pause);
		pauseStage.addActor(resume);
		pauseStage.addActor(menu);
		pauseStage.addActor(exit);
		
		Gdx.input.setInputProcessor(inputDetector);
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
		pauseBackground.getSprite().setTexture(renderTarget.getColorBufferTexture());
		pauseBackground.getSprite().setColor(1.0f,1.0f,1.0f,alpha);
		pauseStage.draw();
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
		
		pause.getSprite().setX((Configuration.getWidth()-pause.getSprite().getWidth())*0.5f);	
		pause.getSprite().setY(Configuration.getHeight()*0.9f - pause.getSprite().getHeight());
		
		resume.getSprite().setX((Configuration.getWidth()-resume.getSprite().getWidth())*0.5f);	
		resume.getSprite().setY(Configuration.getHeight()*0.65f - resume.getSprite().getHeight());
		menu.getSprite().setX((Configuration.getWidth()-menu.getSprite().getWidth())*0.5f);	
		menu.getSprite().setY(Configuration.getHeight()*0.50f - menu.getSprite().getHeight());
		exit.getSprite().setX((Configuration.getWidth()-exit.getSprite().getWidth())*0.5f);	
		exit.getSprite().setY(Configuration.getHeight()*0.35f - exit.getSprite().getHeight());
				
		resume.setBounds(resume.getSprite().getX(), resume.getSprite().getY(), resume.getSprite().getWidth(), resume.getSprite().getHeight());
		menu.setBounds(menu.getSprite().getX(), menu.getSprite().getY(), menu.getSprite().getWidth(), menu.getSprite().getHeight());
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
	public boolean tap(float x, float y, int arg2, int arg3) {
		Vector2 stageCoords = Vector2.Zero;
		pauseStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = pauseStage.hit(stageCoords.x, stageCoords.y, true);

		Gdx.app.log("pause","input");
		
		if(actor == null)
			return false;

		if(actor.getName().equals("Resume")) {
//			if(Configuration.getInstance().soundOn)
//				game.resources.getSoundEffect("selection").play();
		
			
			Gdx.app.log("play", "screen request");
			game.setScreen(playScreen);
		}
		else if(actor.getName().equals("Menu")) {
//			if(Configuration.getInstance().soundOn)
//				game.resources.getSoundEffect("selection").play();
//			
//			if(Configuration.getInstance().musicOn)
//				game.resources.getMusic("gameOverMusic").stop();
			game.setScreen(menuScreen);
		}
		else if(actor.getName().equals("Exit")) {
//			if(Configuration.getInstance().soundOn)
//				game.resources.getSoundEffect("selection").play();
//		
			Gdx.app.exit();
		}
		return true;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
