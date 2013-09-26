package pl.veldrinlab.sakurahero.screens;

import pl.veldrinlab.sakurahero.Configuration;
import pl.veldrinlab.sakurahero.FallingLeavesEffect;
import pl.veldrinlab.sakurahero.FixedList;
import pl.veldrinlab.sakurahero.NinjaOnigiri;
import pl.veldrinlab.sakurahero.OniOnigiri;
import pl.veldrinlab.sakurahero.SakuraHero;
import pl.veldrinlab.sakurahero.KatanaSwing;
import pl.veldrinlab.sakurahero.SakuraLeafDescriptor;
import pl.veldrinlab.sakurahero.SakuraTree;
import pl.veldrinlab.sakurahero.SakuraTreeDescriptor;
import pl.veldrinlab.sakurahero.SamuraiOnigiri;
import pl.veldrinlab.sakuraEngine.core.GameScreen;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureDetector;
import pl.veldrinlab.sakuraEngine.utils.MultitouchGestureListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Json;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SurvivalScreen extends GameScreen implements MultitouchGestureListener, InputProcessor {

	public PauseScreen pauseScreen;
	public GameOverScreen gameOverScreen;
	private SakuraHero game;
	private MultitouchGestureDetector inputDetector;
	private InputMultiplexer inputMultiplexer;

	// w³aœciwy kod stanu
	private SceneEntity pauseButton;
	private SceneEntity background;

	// level Editor

	//
	public SakuraTree tree;

	float leafAccum = 1.0f;


	// katana swing
	private KatanaSwing katana;
	private FixedList<Vector2> input;
	float katanaTime;
	Vector2 lastPoint = new Vector2();



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

	// system katana level

	private SceneEntity katanaLevelBar;
	private SceneEntity katanaLevelBackground;
	private Label katanaLevelInfo;
	private int katanaLevel;
	private float katanaExp;

	private FallingLeavesEffect fallingSakura;

	private Label time;
	private float survivedTime;
	
	public SurvivalScreen(final SakuraHero game) {
		this.game = game;

		pauseButton = new SceneEntity(Renderer.sceneAtlas.createSprite("pauseButton"),"Pause");
		inputDetector = new MultitouchGestureDetector(this);

		//enemy

		enemy = new SamuraiOnigiri(Renderer.sceneAtlas.createSprite("onigiriSamurai"),Renderer.sceneAtlas.createSprite("explosion"));
		enemy.init();

		enemy2 = new NinjaOnigiri(Renderer.sceneAtlas.createSprite("onigiriNinja"),Renderer.sceneAtlas.createSprite("explosion"));
		enemy2.init();

		enemy3 = new OniOnigiri(Renderer.sceneAtlas.createSprite("onigiriOni"),Renderer.sceneAtlas.createSprite("explosion"));
		enemy3.init();

		//TODO katana
		katana = new KatanaSwing();
		katana.texture = new Texture(Gdx.files.internal("swingTexture.png"));
		input = new FixedList<Vector2>(100,Vector2.class);

		stateMessage = new Label("", Renderer.standardFont);

		//
		background = new SceneEntity(Renderer.sceneAtlas.createSprite("natsuBackground"));


		katanaLevelBackground = new SceneEntity(Renderer.sceneAtlas.createSprite("katanaLevelBar"));
		katanaLevelBar = new SceneEntity(Renderer.sceneAtlas.createSprite("katanaLevelBar"));
		katanaLevel = 0;
		katanaLevelInfo = new Label("Level " + String.valueOf(katanaLevel),Renderer.smallFont);


		// msg flow 

		//TODO hit i combo czcionka inna i kolor odpowiedni! Ten Mario Bros font bedzie dobry
		hit = new Label(String.valueOf(hitAmount) + " Hit!", Renderer.specialFont);
		hit.setTouchable(Touchable.disabled);
		combo = new Label(String.valueOf(comboAmount) + "  Combo!",Renderer.specialFont);
		combo.setTouchable(Touchable.disabled);


		tree = new SakuraTree(Renderer.sceneAtlas.createSprite("tree"),Renderer.sceneAtlas.createSprite("sakuraFlower"));
		
		//
		time = new Label("Time ", Renderer.smallFont);
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

		if(Gdx.input.isKeyPressed(Keys.SPACE))
			game.setScreen(gameOverScreen);

		if(Gdx.input.isKeyPressed(Keys.ENTER))
			game.setScreen(pauseScreen);

		// level editor

		if(Gdx.input.isKeyPressed(Keys.S)) {
			Json json = new Json();		
			FileHandle file = Gdx.files.local("levelSurvival.json");		
			String jsonData = json.toJson(tree.leaves);
			file.writeString(jsonData, false);
		}

		if(Gdx.input.isKeyPressed(Keys.L)) {

			Json json = new Json();		
			FileHandle file = Gdx.files.local("levelSurvival.json");

			String jsonData = file.readString();

			try {
				tree.leaves = json.fromJson(SakuraTreeDescriptor.class, jsonData);			
			} catch(Exception e ) {

				Gdx.app.log("SakuraHero ","Level file loading exception");
				e.printStackTrace();
			}

			tree.init();
		}
	}

	@Override
	public void processLogic(final float deltaTime) {

		leafAccum -= deltaTime*0.25f;
		leafAccum = MathUtils.clamp(leafAccum, 0.0f, 1.0f);

		fallingSakura.updateEffect(deltaTime);
		fallingSakura.setLeavesAlpha(leafAccum);


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
						katanaExp += 0.1f; //TODO z levelu na level coraz trudniej - jakiœ geometryczny wspó³czynnik
						//Gdx.app.log("collision"," occurred");
					}
					else if(!enemy2.collisionOccurred && enemy2.collisionCircle.contains(input.get(i).x, input.get(i).y)) {
						if(enemy2.hit()) { //TODO lepiej
							enemyHitAmount++;
							comboAmount++;
							katanaExp += 0.1f;
							Gdx.app.log("collision"," occurred");
						}
					}
					else if(!enemy3.collisionOccurred && enemy3.collisionCircle.contains(input.get(i).x, input.get(i).y)) {
						enemy3.hit();
						enemyHitAmount++;
						comboAmount++;
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
			katanaLevelBar.getSprite().setRegion(katanaLevelBar.getSprite().getRegionX(),katanaLevelBar.getSprite().getRegionY(), (int)(katanaExp*228)+64, 62);

			katana.update(input);

			katanaTime += deltaTime;

			//mo¿e sterowanie czasem nie jest wcale takie g³upie
			if(input.size > 2 && katanaTime > Timer.TIME_STEP*20) {
				input.pop();
				input.pop();
				katanaTime = 0.0f;
			}

		}

		
		
				// survival time update
		survivedTime += deltaTime;
		
		int hours = (int)survivedTime / 3600;
		int minutes = ((int)survivedTime / 60) % 60;
		int seconds = (int)survivedTime % 60;
		
		time.setText("Time " + hours/10+(hours-(hours/10)*10)+":"+minutes/10+(minutes-(minutes/10)*10)+":"+seconds/10+(seconds-(seconds/10*10)));

	}

	@Override
	public void processRendering() { 
		Renderer.clearScreen();
		Renderer.backgroundStage.draw();
		tree.render();
		Renderer.sceneStage.draw();
		katana.draw(Renderer.sceneStage.getCamera());
		Renderer.hudStage.draw();

		fallingSakura.renderEffect();
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

		fallingSakura = new FallingLeavesEffect(3);
		fallingSakura.setFallingBoundary(250-32.0f, 150.0f, 250+32.0f, 150+32.0f);
		fallingSakura.initializeEffect();



		//		if(Configuration.getInstance().musicOn) {
		//			gameMusic.play();
		//			gameMusic.setLooping(true);
		//		}

		//	Gdx.input.setInputProcessor(inputDetector);

		Renderer.backgroundStage.addActor(background);

		//TODO to jakoœ po³¹czyæ ze sob¹
		Renderer.sceneStage.addActor(enemy.shadow3);
		Renderer.sceneStage.addActor(enemy.shadow2);
		Renderer.sceneStage.addActor(enemy.shadow);

		Renderer.sceneStage.addActor(enemy);
		Renderer.sceneStage.addActor(enemy.explosion);
		Renderer.sceneStage.addActor(enemy2);
		Renderer.sceneStage.addActor(enemy2.explosion);
		Renderer.sceneStage.addActor(enemy3);
		Renderer.sceneStage.addActor(enemy3.explosion);


		//TODO hud stage
		Renderer.hudStage.addActor(pauseButton);
		Renderer.hudStage.addActor(stateMessage);
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

		//	Renderer.hudStage.addActor(points);

		Renderer.hudStage.addActor(katanaLevelBackground);
		Renderer.hudStage.addActor(katanaLevelInfo);
		Renderer.hudStage.addActor(katanaLevelBar);

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

		Renderer.hudStage.addActor(hit);
		Renderer.hudStage.addActor(combo);

		//katana level system

		//292 czyli 100 % miecza daje 228 pikseli

		katanaLevelBar.getSprite().setSize(katanaExp*228+64,62);
		katanaLevelBar.getSprite().setRegion(katanaLevelBar.getSprite().getRegionX(),katanaLevelBar.getSprite().getRegionY(), (int)katanaExp*228+64, 62);

		katanaLevelBackground.getSprite().setColor(1.0f, 1.0f, 1.0f, 0.5f);
		katanaLevelBar.getSprite().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		time.setTouchable(Touchable.disabled);
		Renderer.hudStage.addActor(time);
		
		
		time.setX((Configuration.getWidth()-time.getTextBounds().width)*0.025f);	
		time.setY(Configuration.getHeight()*0.95f - time.getTextBounds().height);
		
		survivedTime = 0.0f;

	}

	@Override
	public void dispose() {

	}

	@Override
	public void hide() {
		Renderer.backgroundStage.clear();
		Renderer.sceneStage.clear();
		Renderer.hudStage.clear();
	}

	@Override
	public void pause() {

	}

	@Override
	public boolean tap(float x, float y, int count, int pointer) {
		Vector2 stageCoords = Vector2.Zero;
		Renderer.hudStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
		Actor actor = Renderer.hudStage.hit(stageCoords.x, stageCoords.y, true);

		//test
		fallingSakura.setFallingBoundary(stageCoords.x-32.0f, stageCoords.y, stageCoords.x+32.0f, stageCoords.y);
		fallingSakura.initializeEffect();
		leafAccum = 1.0f;

		if(actor == null)
			return false;

		if(actor.getName().equals("Pause")) {
			pauseScreen.getFrameBuffer().begin();	
			processRendering();
			pauseScreen.getFrameBuffer().end();

			//	if(Configuration.getInstance().musicOn)
			//	gameMusic.pause();

			Gdx.app.log("test", "test");
			game.setScreen(pauseScreen);
		}

		return true;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		// Level Editor screen

		//Gdx.app.log("Level","editor");

//		Vector2 stageCoords = Vector2.Zero;
//		tree.sakuraTreeStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
//		float rotation = MathUtils.random(0.0f, 360.0f);
//
//		//		// to nie tutaj jebnac
//		SceneEntity flower = new SceneEntity(Renderer.sceneAtlas.createSprite("sakuraFlower"));
//		flower.getSprite().setPosition(stageCoords.x-flower.getSprite().getWidth()*0.5f, stageCoords.y-flower.getSprite().getHeight()*0.5f);
//		flower.getSprite().setRotation(rotation);
//
//		tree.sakuraTreeStage.addActor(flower);
//
//		tree.leaves.leaves.add(new SakuraLeafDescriptor(stageCoords.x, stageCoords.y, rotation));

		return true;
	}

	@Override
	public boolean touchUp(float x, float y, int pointer) {

		Gdx.app.log("test","up");
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

		Gdx.app.log("test","down");
		Vector2 stageCoords = new Vector2();
		//cos innego
		tree.sakuraTreeStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

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
			Renderer.sceneStage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

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
