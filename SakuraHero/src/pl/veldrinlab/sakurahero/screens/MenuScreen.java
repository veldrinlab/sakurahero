package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MenuScreen extends GameScreen implements GestureListener {

	public ModeSelectionScreen modeSelectionScreen;
	public CreditsScreen creditsnScreen;
	public OptionsScreen optionsScreen;

	private SakuraHero game;
	private GestureDetector inputDetector;
	private FallingLeavesEffect fallingSakura;
	
	private SceneEntity background;	
	private SceneEntity menu;
	private SceneEntity play;
	private SceneEntity options;
	private SceneEntity credits;
	private SceneEntity exit;

	private Music menuMusic;

	public MenuScreen(final SakuraHero game) {
		this.game = game;
		this.fallingSakura = game.fallingSakura;

		background = new SceneEntity(Renderer.introAtlas.createSprite("menuBackground"));
		menu = new SceneEntity(Renderer.guiAtlas.createSprite("menu"));
		play = new SceneEntity(Renderer.guiAtlas.createSprite("play"),"Play");
		options = new SceneEntity(Renderer.guiAtlas.createSprite("options"),"Options");
		credits = new SceneEntity(Renderer.guiAtlas.createSprite("credits"),"Credits");
		exit = new SceneEntity(Renderer.guiAtlas.createSprite("exit"),"Exit");
		
		inputDetector = new GestureDetector(this);
		
		initializeInterface();
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
		Renderer.backgroundStage.clear();
		Renderer.hudStage.clear();
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

		//		if(Configuration.getInstance().musicOn) {
		//			menuMusic.play();
		//			menuMusic.setVolume(0.1f);
		//		}
		//		

		Renderer.backgroundStage.addActor(background);

		Renderer.hudStage.addActor(menu);
		Renderer.hudStage.addActor(play);
		Renderer.hudStage.addActor(options);
		Renderer.hudStage.addActor(credits);
		Renderer.hudStage.addActor(exit);

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
		menu.alignCenter(0.90f);
		
		play.alignCenter(0.65f);
		options.alignCenter(0.50f);
		credits.alignCenter(0.35f);
		exit.alignCenter(0.20f);
		
		play.updateBounds();
		options.updateBounds();
		credits.updateBounds();
		exit.updateBounds();
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
		Renderer.hudStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.hudStage.hit(stageCoords.x, stageCoords.y, true);

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
