package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

//TODO katana poziom spada z czasem
//TODO combo kilka klatek
//TODO exp boost z combo
public class GameHud {

	private SceneEntity pauseButton;
	
	private Label hit;
	private int hitAmount;
	private float hitAccumulator;
	private final float HIT_DURATION = 2.0f;
	private float hitAlpha;

	private Label combo;
	private int comboAmount;
	private float comboAlpha;
	private final int COMBO_DURATION = 5;
	private int comboFrameAccumulator;

	private Label points;
	private int pointAmount;

	private SceneEntity katanaLevelBar;
	private SceneEntity katanaLevelBackground;
	private Label katanaLevelInfo;
	private int katanaLevel;
	private float katanaExp;
	
	private Label stateMessage;
	//TODO jakis timer
	
	public GameHud() {
		
		pauseButton = new SceneEntity(Renderer.sceneAtlas.createSprite("pauseButton"),"Pause");
		
		hit = new Label(String.valueOf(hitAmount) + " Hit!", Renderer.specialFont);
		combo = new Label(String.valueOf(comboAmount) + "  Combo!",Renderer.specialFont);
		points = new Label("Points: " + String.valueOf(pointAmount), Renderer.smallFont);

		katanaLevelBackground = new SceneEntity(Renderer.sceneAtlas.createSprite("katanaLevelBar"));
		katanaLevelBar = new SceneEntity(Renderer.sceneAtlas.createSprite("katanaLevelBar"));
		katanaLevelInfo = new Label("Level " + String.valueOf(katanaLevel),Renderer.smallFont);
		
		stateMessage = new Label("Ready...!", Renderer.standardFont);
		
		points.setTouchable(Touchable.disabled);
		hit.setTouchable(Touchable.disabled);
		combo.setTouchable(Touchable.disabled);
		stateMessage.setTouchable(Touchable.disabled);
	}
	
	public void initialize() {
		pointAmount = 0;
		katanaLevel = 0;
		
		hitAmount = 0;
		hitAccumulator = 0.0f;
		hitAlpha = 0.0f;

		hit.setColor(1.0f, 1.0f, 1.0f, hitAlpha);

		comboAmount = 0;
		comboAlpha = 0.0f;
		comboFrameAccumulator = 0;

		combo.setColor(1.0f,1.0f,1.0f,comboAlpha);
		
		stateMessage.setX((Configuration.getWidth()-stateMessage.getTextBounds().width)*0.5f);	
		stateMessage.setY(Configuration.getHeight()*0.65f - stateMessage.getTextBounds().height);
		
		katanaLevelBackground.updateEntityState(Configuration.getWidth()-katanaLevelBackground.width, Configuration.getHeight() - katanaLevelBackground.height);
		katanaLevelBar.updateEntityState(Configuration.getWidth()-katanaLevelBar.width, Configuration.getHeight() - katanaLevelBar.height);

		katanaLevelInfo.setX(katanaLevelBackground.position.x+katanaLevelBackground.width*0.5f-katanaLevelInfo.getTextBounds().width*0.5f);
		katanaLevelInfo.setY(katanaLevelBackground.position.y-katanaLevelBackground.height*0.5f);

		points.setX((Configuration.getWidth()-points.getTextBounds().width)*0.025f);	
		points.setY(Configuration.getHeight()*0.95f - points.getTextBounds().height);
		
		pauseButton.updateEntityState(Configuration.getWidth()*0.98f-pauseButton.width, 0.0f);
	
		katanaLevelBar.sprite.setSize(katanaExp*228+64,62);
		katanaLevelBar.sprite.setRegion(katanaLevelBar.sprite.getRegionX(),katanaLevelBar.sprite.getRegionY(), (int)katanaExp*228+64, 62);

		katanaLevelBackground.setEntityAlpha(0.5f);
		katanaLevelBar.setEntityAlpha(1.0f);
	}
	
	public void initializeNormalHUD() {
		
		
		Renderer.hudStage.addActor(pauseButton);
		Renderer.hudStage.addActor(stateMessage);
		
		
		Renderer.hudStage.addActor(katanaLevelBackground);
		Renderer.hudStage.addActor(katanaLevelInfo);
		Renderer.hudStage.addActor(katanaLevelBar);
		
	
		Renderer.hudStage.addActor(points);

	}

	public void initializeTrainingHUD() {
		
		Renderer.hudStage.addActor(pauseButton);
		Renderer.hudStage.addActor(stateMessage);
		
		Renderer.hudStage.addActor(katanaLevelBackground);
		Renderer.hudStage.addActor(katanaLevelInfo);
		Renderer.hudStage.addActor(katanaLevelBar);
		
		
	}
	
	public void initializeSurvivalHUD() {
		
	}
	
	public void render() {
		Renderer.hudStage.draw();
	}
	
	public void updateHud(final int enemyHitAmount, final float deltaTime) {
		
		comboAmount +=  enemyHitAmount;
		pointAmount += 10* enemyHitAmount;
		katanaExp += 0.1f* enemyHitAmount;

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

			float x = MathUtils.random(Configuration.getWidth()*0.1f, Configuration.getWidth()*0.75f);
			float y = MathUtils.random(Configuration.getHeight()*0.1f, Configuration.getHeight()*0.75f);
			hit.setPosition(x, y);
			hit.setColor(1.0f, 1.0f, 1.0f, hitAlpha);
		}


		if(comboAmount > 0) {

			if(enemyHitAmount > 1) {
				comboAlpha = 1.0f;
				comboAmount = enemyHitAmount;

				// bonus za Combo
				pointAmount += comboAmount*100;

				//TODO katana exp boost

				combo.setText(String.valueOf(comboAmount) + " Combo!"); 
				combo.setColor(1.0f, 1.0f, 1.0f, comboAlpha);

				float x = MathUtils.random(Configuration.getWidth()*0.1f, Configuration.getWidth()*0.75f);
				float y = MathUtils.random(Configuration.getHeight()*0.1f, Configuration.getHeight()*0.75f);
				combo.setPosition(x, y);
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

			combo.setText(String.valueOf(comboAmount) + " Combo!"); 
			combo.setColor(1.0f, 1.0f, 1.0f, comboAlpha);
			combo.setPosition(0.0f, hit.getTextBounds().height);
		}
		
		if(katanaExp > 1.0f) {
			katanaExp = 0.0f;
			katanaLevel++;
			katanaLevelInfo.setText("Level " + katanaLevel);
		}
		katanaLevelBar.sprite.setSize(katanaExp*228+64,62);
		katanaLevelBar.sprite.setRegion(katanaLevelBar.sprite.getRegionX(),katanaLevelBar.sprite.getRegionY(), (int)(katanaExp*228)+64, 62);
		
		points.setText("Points: " + String.valueOf(pointAmount));
	}
}
