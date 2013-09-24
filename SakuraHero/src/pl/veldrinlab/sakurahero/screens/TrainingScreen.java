package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.FixedList;
import pl.veldrinlab.sakurahero.NinjaOnigiri;
import pl.veldrinlab.sakurahero.OniOnigiri;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakurahero.KatanaSwing;
import pl.veldrinlab.sakurahero.SamuraiOnigiri;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureDetector;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

//
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;

public class TrainingScreen extends GameScreen implements MultitouchGestureListener, InputProcessor {

	public PauseScreen pauseScreen;

	private SakuraHero game;
	private MultitouchGestureDetector inputDetector;
	private InputMultiplexer inputMultiplexer;

	// w³aœciwy kod stanu

	private SpriteBatch sceneBatch;
	private Stage sceneStage;

	//TODO hud class
	private SpriteBatch hudBatch;
	private Stage hudStage;
	private SceneEntity pauseButton;


	// katana swing
	private KatanaSwing katana;
	private FixedList<Vector2> input;
	float katanaTime;
	Vector2 lastPoint = new Vector2();

	//
	private SceneEntity background;
	private SpriteBatch backgroundBatch;
	private Stage backgroundStage;

	private SamuraiOnigiri enemy;
	private NinjaOnigiri enemy2;
	private OniOnigiri enemy3;

	// test

	private float slashTimer;




	// state logic flow

	private float flowAccumulator;
	private Label stateMessage;


	// msq flow control - mo¿e jakaœ dodatkowa struktura do tego TODO? 

	// system hit
	private Label hit;
	private int hitAmount;
	private float hitAccumulator;
	private final float HIT_DURATION = 2.0f;
	private float hitAlpha;

	// system combo
	private Label combo;
	private int comboAmount;
	private float comboAlpha;
	
	// system points
	private int pointAmount;
	private Label points;

	// system katana level

	private SceneEntity katanaLevelBar;
	private SceneEntity katanaLevelBackground;
	private Label katanaLevelInfo;
	private int katanaLevel;
	private float katanaExp;
	

	public TrainingScreen(final SakuraHero game) {
		this.game = game;

		pauseButton = new SceneEntity(game.resources.getTexture("pauseButton"),"Pause");
		inputDetector = new MultitouchGestureDetector(this);

		//enemy

		enemy = new SamuraiOnigiri(game.resources.getTexture("onigiriSamurai"),game.resources.getTexture("explosion"));
		enemy.init();

		enemy2 = new NinjaOnigiri(game.resources.getTexture("onigiriNinja"),game.resources.getTexture("explosion"));
		enemy2.init();

		enemy3 = new OniOnigiri(game.resources.getTexture("onigiriOni"),game.resources.getTexture("explosion"));
		enemy3.init();

		//TODO katana
		katana = new KatanaSwing();
		katana.texture = new Texture(Gdx.files.internal("swingTexture.png"));
		input = new FixedList<Vector2>(100,Vector2.class);


		//
		sceneBatch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		backgroundBatch = new SpriteBatch();
		sceneStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,sceneBatch);
		hudStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,hudBatch);
		backgroundStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,backgroundBatch);


		//
		LabelStyle style = new LabelStyle(game.resources.getFont("defaultFont"),Color.WHITE);
		LabelStyle styleSmall = new LabelStyle(game.resources.getFont("smallFont"),Color.WHITE);
		stateMessage = new Label("", style);


		//
		background = new SceneEntity(game.resources.getTexture("dojo"));


		pointAmount = 0;
		points = new Label("Points: " + String.valueOf(pointAmount), styleSmall);
		points.setTouchable(Touchable.disabled);


		katanaLevelBackground = new SceneEntity(game.resources.getTexture("katanaLevelBar"));
		katanaLevelBar = new SceneEntity(game.resources.getTexture("katanaLevelBar"));
		katanaLevel = 0;
		katanaLevelInfo = new Label("Level " + String.valueOf(katanaLevel),styleSmall);


		// msg flow 

		//TODO hit i combo czcionka inna i kolor odpowiedni! Ten Mario Bros font bedzie dobry
		hit = new Label(String.valueOf(hitAmount) + " Hit!", style);
		hit.setTouchable(Touchable.disabled);
		combo = new Label(String.valueOf(comboAmount) + "  Combo!",style);
		combo.setTouchable(Touchable.disabled);
		
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
	public void processInput() {	
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();

		if(Gdx.input.isKeyPressed(Keys.ENTER))
			game.setScreen(pauseScreen);
	}

	@Override
	public void processLogic(final float deltaTime) {
		//TODO jakiœ fajniejszy pomys³ na konkretne stany??

		//for tests
		flowAccumulator += deltaTime*10.75f;

		if(flowAccumulator < 1.0f) {
			stateMessage.setColor(1.0f,1.0f,1.0f,flowAccumulator);
		}
		else if(flowAccumulator > 1.0f && flowAccumulator < 2.0f) {
			stateMessage.setColor(1.0f,1.0f,1.0f,2.0f-flowAccumulator);
		}
		else if(flowAccumulator > 2.0f && flowAccumulator < 3.0f) {
			stateMessage.setText("It is training time");
			stateMessage.setX((Configuration.getWidth()-stateMessage.getTextBounds().width)*0.5f);	
			stateMessage.setColor(1.0f, 1.0f, 1.0f, flowAccumulator-2.0f);
		}
		else if(flowAccumulator > 3.0f && flowAccumulator < 4.0f) {
			stateMessage.setColor(1.0f, 1.0f, 1.0f, 4.0f-flowAccumulator);
		}
		else if(flowAccumulator > 4.0f && flowAccumulator < 4.5f) {
			stateMessage.setText("Ready...");
			stateMessage.setX((Configuration.getWidth()-stateMessage.getTextBounds().width)*0.5f);	
			stateMessage.setColor(1.0f, 1.0f, 1.0f, flowAccumulator - 2.5f);
		}
		else if(flowAccumulator > 4.5f && flowAccumulator < 5.0f) {
			stateMessage.setText("Fight!!!");
			stateMessage.setX((Configuration.getWidth()-stateMessage.getTextBounds().width)*0.5f);	
			stateMessage.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
		else { // typical training state
			//
			stateMessage.setText("");

			enemy.update(deltaTime);
			enemy2.update(deltaTime);
			enemy3.update(deltaTime);

			// collisiom detection


			// liczba zabitych per klatka

			int enemyHitAmount = 0;

			if(input.size > 3) {

				//TODO update explosion inside
				enemy.explosion.getSprite().setPosition(enemy.getSprite().getX(), enemy.getSprite().getY());
				enemy2.explosion.getSprite().setPosition(enemy2.getSprite().getX(), enemy2.getSprite().getY());

				for(int i = 0; i < input.size; ++i) {

					if(!enemy.collisionOccurred && enemy.collisionCircle.contains(input.get(i).x, input.get(i).y)) {
						enemy.hit();
						enemyHitAmount++;
						comboAmount++;
						pointAmount += 10;
						katanaExp += 0.1f; //TODO z levelu na level coraz trudniej - jakiœ geometryczny wspó³czynnik
						//Gdx.app.log("collision"," occurred");
					}
					else if(!enemy2.collisionOccurred && enemy2.collisionCircle.contains(input.get(i).x, input.get(i).y)) {
						if(enemy2.hit()) { //TODO lepiej
							enemyHitAmount++;
							comboAmount++;
							pointAmount += 10;
							katanaExp += 0.1f;
							Gdx.app.log("collision"," occurred");
						}
					}
					else if(!enemy3.collisionOccurred && enemy3.collisionCircle.contains(input.get(i).x, input.get(i).y)) {
						enemy3.hit();
						enemyHitAmount++;
						comboAmount++;
						pointAmount += 10;
						katanaExp += 0.1f;
						//Gdx.app.log("collision"," occurred");
					}
				}
			}


			if(hitAmount > 0) {
				// bylo juz cos zabite

				if(enemyHitAmount > 0) {
					// zabiliœmy znowy
					hitAmount += enemyHitAmount;
					hitAlpha = 1.0f;

					hit.setText(String.valueOf(hitAmount) + " Hit!");
					hit.setColor(1.0f, 1.0f, 1.0f, hitAlpha);
				}
				else {
					//nic nie zabilismy
					hitAccumulator += deltaTime;
					hitAlpha -= deltaTime*0.5f;	

					hit.setColor(1.0f, 1.0f, 1.0f, hitAlpha);

					if(hitAccumulator > HIT_DURATION) {
						hitAmount = 0;
						hitAccumulator = 0.0f;
						hitAlpha = 0.0f;
					}

					hit.setColor(1.0f, 1.0f, 1.0f, hitAlpha);
				}


			}
			else if(hitAmount == 0 && enemyHitAmount > 0)  {
				// jezeli zaczynamy zabijac 
				hitAccumulator += deltaTime;
				hitAlpha = 1.0f;

				hitAmount += enemyHitAmount;

				hit.setText(String.valueOf(hitAmount) + " Hit!"); 
				//TODO pozycja odpowiednia
				hit.setColor(1.0f, 1.0f, 1.0f, hitAlpha);
			}

			
			if(comboAmount > 0) {
				
				if(enemyHitAmount > 1) {
					comboAlpha = 1.0f;
					comboAmount = enemyHitAmount;
					
					// bonus za Combo
					pointAmount += comboAmount*100;
					
					//TODO katana exp boost
					
					//TODO pozycja odpowiednia
					combo.setText(String.valueOf(comboAmount) + " Combo!"); 
					combo.setColor(1.0f, 1.0f, 1.0f, comboAlpha);
					combo.setPosition(0.0f, hit.getTextBounds().height); 
				}
				else {
					//by³o ju¿ combo
					comboAlpha -= deltaTime;
					
					combo.setColor(1.0f, 1.0f, 1.0f, comboAlpha);
					
					if(comboAlpha < 0.00001f)
						comboAmount = 0;					
				}
			}
			else if(comboAmount == 0 && enemyHitAmount > 1) {
				comboAlpha = 1.0f;
				comboAmount = enemyHitAmount;
				pointAmount += comboAmount*100;
				
				//TODO katana exp boost
				
				//TODO pozycja odpowiednia
				combo.setText(String.valueOf(comboAmount) + " Combo!"); 
				combo.setColor(1.0f, 1.0f, 1.0f, comboAlpha);
				combo.setPosition(0.0f, hit.getTextBounds().height);
			}

			
			// katana system
			
			if(katanaExp > 1.0f) {
				katanaExp = 0.0f;
				katanaLevel++;
				katanaLevelInfo.setText("Level " + katanaLevel);
			}
			katanaLevelBar.getSprite().setSize(katanaExp*228+64,62);
			katanaLevelBar.getSprite().setRegion(0,0, (int)(katanaExp*228)+64, 62);
			
			katana.update(input);

			katanaTime += deltaTime;

			//mo¿e sterowanie czasem nie jest wcale takie g³upie
			if(input.size > 2 && katanaTime > Timer.TIME_STEP*20) {
				input.pop();
				input.pop();
				katanaTime = 0.0f;
			}

		}
		
		points.setText("Points: " + String.valueOf(pointAmount));
	}

	@Override
	public void processRendering() { 
		Renderer.clearScreen(Color.LIGHT_GRAY);
		backgroundStage.draw();
		sceneStage.draw();
		katana.draw(sceneStage.getCamera());
		hudStage.draw();
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
		//		if(Configuration.getInstance().musicOn) {
		//			gameMusic.play();
		//			gameMusic.setLooping(true);
		//		}

		//	Gdx.input.setInputProcessor(inputDetector);



		backgroundStage.addActor(background);

		//TODO to jakoœ po³¹czyæ ze sob¹
		sceneStage.addActor(enemy.shadow3);
		sceneStage.addActor(enemy.shadow2);
		sceneStage.addActor(enemy.shadow);

		sceneStage.addActor(enemy);
		sceneStage.addActor(enemy.explosion);
		sceneStage.addActor(enemy2);
		sceneStage.addActor(enemy2.explosion);
		sceneStage.addActor(enemy3);
		sceneStage.addActor(enemy3.explosion);




		//TODO hud stage
		hudStage.addActor(pauseButton);
		hudStage.addActor(stateMessage);
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(inputDetector);
		inputMultiplexer.addProcessor(this);

		Gdx.input.setInputProcessor(inputMultiplexer);
		//		Gdx.input.setInputProcessor(inputDetector);
		//		Gdx.input.setInputProcessor(this);


		// logic
		flowAccumulator = 0.0f;
		stateMessage.setTouchable(Touchable.disabled);
		stateMessage.setText("Wellcome to Dojo!");
		stateMessage.setX((Configuration.getWidth()-stateMessage.getTextBounds().width)*0.5f);	
		stateMessage.setY(Configuration.getHeight()*0.65f - stateMessage.getTextBounds().height);
		stateMessage.setColor(1.0f, 1.0f, 1.0f, flowAccumulator);

		hudStage.addActor(points);

		hudStage.addActor(katanaLevelBackground);
		hudStage.addActor(katanaLevelInfo);
		hudStage.addActor(katanaLevelBar);

		points.setX((Configuration.getWidth()-points.getTextBounds().width)*0.025f);	
		points.setY(Configuration.getHeight()*0.95f - points.getTextBounds().height);

		katanaLevelBackground.getSprite().setY(Configuration.getHeight() - katanaLevelBackground.getSprite().getHeight());
		katanaLevelBackground.getSprite().setX(Configuration.getWidth()-katanaLevelBackground.getSprite().getWidth());

		katanaLevelBar.getSprite().setY(Configuration.getHeight() - katanaLevelBar.getSprite().getHeight());
		katanaLevelBar.getSprite().setX(Configuration.getWidth()-katanaLevelBar.getSprite().getWidth());

		katanaLevelInfo.setX(katanaLevelBackground.getSprite().getX()+katanaLevelBackground.getSprite().getWidth()*0.5f-katanaLevelInfo.getTextBounds().width*0.5f);
		katanaLevelInfo.setY(katanaLevelBackground.getSprite().getY()-katanaLevelBackground.getSprite().getHeight()*0.5f);




		// pause button hud
		pauseButton.getSprite().setX(Configuration.getWidth()*0.98f-pauseButton.getSprite().getWidth());


		//hit/combo system
		hitAmount = 0;
		hitAccumulator = 0.0f;
		hitAlpha = 0.0f;

		hit.setColor(1.0f, 1.0f, 1.0f, hitAlpha);

		comboAmount = 0;
		comboAlpha = 0.0f;
		
		combo.setColor(1.0f,1.0f,1.0f,comboAlpha);
		
		hudStage.addActor(hit);
		hudStage.addActor(combo);
		
		//katana level system
		
		//292 czyli 100 % miecza daje 228 pikseli

		katanaLevelBar.getSprite().setSize(katanaExp*228+64,62);
		katanaLevelBar.getSprite().setRegion(0,0, (int)katanaExp*228+64, 62);

		katanaLevelBackground.getSprite().setColor(1.0f, 1.0f, 1.0f, 0.5f);
		katanaLevelBar.getSprite().setColor(1.0f, 1.0f, 1.0f, 1.0f);

		
	}

	@Override
	public void dispose() {

	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {

	}

	@Override
	public boolean tap(float x, float y, int count, int pointer) {
		Vector2 stageCoords = Vector2.Zero;
		hudStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = hudStage.hit(stageCoords.x, stageCoords.y, true);

		if(actor == null)
			return false;

		if(actor.getName().equals("Pause")) {
			pauseScreen.getFrameBuffer().begin();	
			processRendering();
			pauseScreen.getFrameBuffer().end();

			//	if(Configuration.getInstance().musicOn)
			//	gameMusic.pause();

			//	Gdx.app.log("test", "test");
			game.setScreen(pauseScreen);
		}

		return true;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		return true;
	}

	@Override
	public boolean touchUp(float x, float y, int pointer) {
		return true;
	}

	@Override
	public boolean longPress(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float startX, float startY, float endX, float endY, float velocityX, float velocityY, int pointer) {
		//velocity X -> dodatnie to w prawo
		// > 1500

		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 stageCoords = new Vector2();
		sceneStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

		input.insert(stageCoords);
		lastPoint.set(stageCoords.x, stageCoords.y);

		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub

		input.clear();
		slashTimer = 0.0f;
		return false;
	}

	public static float distSq(Vector2 p1, Vector2 p2) {
		float dx = p1.x - p2.x, dy = p1.y - p2.y;
		return dx * dx + dy * dy;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//	Gdx.app.log("touch", " dragged");
		// jakis maksymalny rozmiar swinga

		slashTimer += Timer.TIME_STEP;

		if(slashTimer > 0.2f)
			return true;

		final float maxLength = 10.0f;

		float swingLength = 0.0f;

		for(int i = 0; i < input.size-1; ++i)
			swingLength += input.get(i).dst(input.get(i+1));

		//	Gdx.app.log("distance", String.valueOf(swingLength));

		// TODO wci¹¿ nie da siê ³adnego okrêgu narysowaæ i s¹ artefakty

		if(input.size < 100) {

			Vector2 stageCoords = new Vector2();
			//cos innego
			sceneStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

			// this is dst2 in Vector class
			float lenSq = distSq(stageCoords,lastPoint);

			float minDistanceSq = 25.0f;

			if (lenSq >= minDistanceSq) {
				input.insert(stageCoords);
				lastPoint.set(stageCoords.x, stageCoords.y);
			}
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
