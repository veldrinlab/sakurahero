package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Language;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.fx.FadeEffectParameters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Class represents LanguageSelection screen. It is used to select game language.
 * @author Szymon Jab³oñski
 *
 */
public class LanguageSelectionScreen extends GameScreen implements GestureListener {

	private SakuraHero game;
	private GestureDetector inputDetector;
	private FadeEffectParameters fade;

	private SceneEntity background;
	private SceneEntity selection;
	private SceneEntity english;
	private SceneEntity japanese;

	private boolean fadeInState;
	private boolean selectState;
	private boolean fadeOutState;

	private float elapsedTime;
	private float blinking;

	public LanguageSelectionScreen(final SakuraHero game, final FadeEffectParameters fadeParams) {
		this.game = game;
		this.fade = fadeParams;
		
		background = new SceneEntity(Renderer.introAtlas.createSprite("menuBackground"));
		selection = new SceneEntity(Renderer.introAtlas.createSprite("selectLanguage"));
		english = new SceneEntity(Renderer.introAtlas.createSprite("english"),"English");
		japanese = new SceneEntity(Renderer.introAtlas.createSprite("japanese"),"Japanese");
		blinking = 1.0f;
		inputDetector = new GestureDetector(this);    
		
		initializeInterface();
	}

	@Override
	public void processInput() {}

	@Override
	public void processLogic(final float deltaTime) {

		if(fadeInState) {
			elapsedTime += deltaTime;
			elapsedTime = MathUtils.clamp(elapsedTime, 0.0f, fade.fadeInTime);

			background.setEntityAlpha(elapsedTime);
			selection.setEntityAlpha(elapsedTime);
			english.setEntityAlpha(elapsedTime);
			japanese.setEntityAlpha(elapsedTime);

			if(elapsedTime > fade.fadeInTime-0.001f) {
				fadeInState = false;
				selectState = true;
			}
		}
		else if(selectState) {
			blinking += deltaTime*5.0f;

			background.setEntityAlpha(1.0f);
			selection.setEntityAlpha(1.0f);
			english.setEntityAlpha((float) ((Math.sin(blinking)+1.0f)/2.0f));
			japanese.setEntityAlpha((float) ((Math.sin(blinking)+1.0f)/2.0f));
		}
		else if(fadeOutState) {
			elapsedTime += deltaTime;
			elapsedTime = MathUtils.clamp(elapsedTime, 0.0f, fade.fadeOutTime);

			background.setEntityAlpha(fade.fadeOutTime-elapsedTime);
			selection.setEntityAlpha(fade.fadeOutTime-elapsedTime);

			if(game.options.language == Language.ENGLISH) {
				english.setEntityAlpha(fade.fadeOutTime-elapsedTime);
				japanese.setEntityAlpha(0.0f);
			}
			else {
				english.setEntityAlpha(0.0f);
				japanese.setEntityAlpha(fade.fadeOutTime-elapsedTime);
			}

			if(elapsedTime > fade.fadeOutTime-0.001f)
				game.initializeLoading();
		}
	}

	@Override
	public void processRendering() {
		Renderer.clearScreen();
		Renderer.backgroundStage.draw();
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
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
		Renderer.backgroundStage.clear();
		Renderer.hudStage.clear();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void show() {

		fadeInState = true;
		background.setEntityAlpha(0.0f);
		selection.setEntityAlpha(0.0f);
		english.setEntityAlpha(0.0f);
		japanese.setEntityAlpha(0.0f);

		Renderer.backgroundStage.addActor(background);
		Renderer.hudStage.addActor(selection);
		Renderer.hudStage.addActor(english);
		Renderer.hudStage.addActor(japanese);

		Gdx.input.setInputProcessor(inputDetector);
	}

	@Override
	public void dispose() {}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		Vector2 stageCoords = Vector2.Zero;
		Renderer.hudStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.hudStage.hit(stageCoords.x, stageCoords.y, true);

		if(actor == null)
			return false;

		if(selectState && (actor.getName().equals("English") || actor.getName().equals("Japanese"))) {
			selectState = false;
			fadeOutState = true;
			elapsedTime = 0.0f;

			if(actor.getName().equals("English"))
				game.options.language = Language.ENGLISH;
			else
				game.options.language = Language.JAPANESE;
		}

		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}
	
	private void initializeInterface()	{
		selection.alignCenter(0.90f);
		
		english.alignRelative(0.20f, 0.40f);
		japanese.alignRelative(0.80f, 0.40f);
		english.updateBounds();
		japanese.updateBounds();
	}
}
