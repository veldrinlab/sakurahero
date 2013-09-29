package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.SakuraGameMode;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

import pl.veldrinlab.sakuraEngine.core.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class WorldSelectionScreen extends GameScreen implements GestureListener  {

	public ModeSelectionScreen modeSelectionScreen;
	public PlayScreen playScreen;
	public SurvivalScreen survivalScreen;
	
	private SakuraHero game;	
	private GestureDetector inputDetector;

	private FallingLeavesEffect fallingSakura;

	private SceneEntity background;
	private SceneEntity worldSelection;
	private SceneEntity natsu;
	private SceneEntity aki;
	private SceneEntity back;

	private SceneEntity natsuBackground;
	private SceneEntity akiBackground;

	public WorldSelectionScreen(final SakuraHero game) {
		this.game = game;
		fallingSakura = game.fallingSakura;

		background = new SceneEntity(Renderer.introAtlas.createSprite("menuBackground"));
		worldSelection = new SceneEntity(Renderer.guiAtlas.createSprite("worldSelection"));
		back = new SceneEntity(Renderer.guiAtlas.createSprite("back"),"Back");
		aki = new SceneEntity(Renderer.guiAtlas.createSprite("aki"));	
		natsu = new SceneEntity(Renderer.guiAtlas.createSprite("natsu"));

		natsuBackground = new SceneEntity(Renderer.sceneAtlas.createSprite("natsuBackground"),"natsuBackground",320,192);
		akiBackground = new SceneEntity(Renderer.sceneAtlas.createSprite("akiBackground"),"akiBackground",320,192);

		inputDetector = new GestureDetector(this);    
		
		initializeInterface();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void hide() {
		Renderer.backgroundStage.clear();
		Renderer.hudStage.clear();
	}

	@Override
	public void pause() {
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
	}

	@Override
	public void show() {
		Renderer.backgroundStage.addActor(background);

		Renderer.hudStage.addActor(worldSelection);
		Renderer.hudStage.addActor(back);

		Renderer.hudStage.addActor(natsuBackground);
		Renderer.hudStage.addActor(akiBackground);

		Renderer.hudStage.addActor(natsu);
		Renderer.hudStage.addActor(aki);

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
		worldSelection.alignCenter(0.90f);
		
		natsuBackground.updateEntityState(Configuration.getWidth()*0.05f, Configuration.getHeight()*0.70f - natsuBackground.height);
		akiBackground.updateEntityState(Configuration.getWidth()*0.95f - akiBackground.width,Configuration.getHeight()*0.70f - akiBackground.height);
		natsu.updateEntityState(natsuBackground.position.x+natsuBackground.width*0.5f-natsu.width*0.5f,natsuBackground.position.y-natsuBackground.height*0.40f);
		aki.updateEntityState(akiBackground.position.x+akiBackground.width*0.5f-aki.width*0.5f,akiBackground.position.y-akiBackground.height*0.40f);
		natsuBackground.updateBounds();
		akiBackground.updateBounds();	
		back.updateBounds();
	}

	@Override
	public boolean fling(float arg0, float arg1, int arg2) {
		return false;
	}

	@Override
	public boolean longPress(float arg0, float arg1) {
		return false;
	}

	@Override
	public boolean pan(float arg0, float arg1, float arg2, float arg3) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int arg2, int arg3) {
		Vector2 stageCoords = Vector2.Zero;
		Renderer.hudStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.hudStage.hit(stageCoords.x, stageCoords.y, true);

		if(actor == null)
			return false;

		if(actor.getName().equals("Back"))
			game.setScreen(modeSelectionScreen);
		else  {
			game.options.worldName = actor.getName();
			
			if(game.options.mode == SakuraGameMode.NORMAL) {
				playScreen.resetState();
				game.setScreen(playScreen);
			}
			else {
				survivalScreen.resetState();
				game.setScreen(survivalScreen);
			}
		}

		return true;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		return false;
	}
}
