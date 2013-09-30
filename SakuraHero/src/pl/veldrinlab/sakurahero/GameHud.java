package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

public class GameHud {

	public SakuraHero game;
	
	private SceneEntity pauseButton;

	private Label hit;
	private int hitAmount;
	private float hitAccumulator;
	private final float HIT_DURATION = 2.0f;
	private float hitAlpha;

	private Label combo;
	private int comboAmount;
	private float comboAlpha;

	private SceneEntity katanaLevelBar;
	private SceneEntity katanaLevelBackground;
	private Label katanaLevelInfo;
	private int katanaLevel;
	private float katanaExp;

	private Label points;
	private int pointAmount;

	private Label time;
	private float survivedTime;
	
	public GameHud(final SakuraHero game) {
		this.game = game;
		
		pauseButton = new SceneEntity(Renderer.sceneAtlas.createSprite("pauseButton"),"Pause");

		hit = new Label(String.valueOf(hitAmount) + " Hit!", Renderer.specialFont);
		combo = new Label(String.valueOf(comboAmount) + "  Combo!",Renderer.specialFont);
		points = new Label("Points: " + String.valueOf(pointAmount), Renderer.smallFont);
		time = new Label("Time ", Renderer.smallFont);
		
		katanaLevelBackground = new SceneEntity(Renderer.sceneAtlas.createSprite("katanaLevelBar"),"");
		katanaLevelBar = new SceneEntity(Renderer.sceneAtlas.createSprite("katanaLevelBar"),"");
		katanaLevelInfo = new Label("Level " + String.valueOf(katanaLevel),Renderer.smallFont);

		points.setTouchable(Touchable.disabled);
		hit.setTouchable(Touchable.disabled);
		combo.setTouchable(Touchable.disabled);
		time.setTouchable(Touchable.disabled);
		katanaLevelInfo.setTouchable(Touchable.disabled);
	}

	public void resetState() {
		pointAmount = 0;
		survivedTime = 0.0f;
		katanaLevel = 0;

		hitAmount = 0;
		hitAccumulator = 0.0f;
		hitAlpha = 0.0f;

		comboAmount = 0;
		comboAlpha = 0.0f;

		hit.setColor(1.0f, 1.0f, 1.0f, hitAlpha);
		combo.setColor(1.0f,1.0f,1.0f,comboAlpha);
	}

	public void initialize() {
		resetState();

		katanaLevelBackground.updateEntityState(Configuration.getWidth()-katanaLevelBackground.width, Configuration.getHeight() - katanaLevelBackground.height);
		katanaLevelBar.updateEntityState(Configuration.getWidth()-katanaLevelBar.width, Configuration.getHeight() - katanaLevelBar.height);

		katanaLevelInfo.setX(katanaLevelBackground.position.x+katanaLevelBackground.width*0.5f-katanaLevelInfo.getTextBounds().width*0.5f);
		katanaLevelInfo.setY(katanaLevelBackground.position.y-katanaLevelBackground.height*0.5f);

		points.setX((Configuration.getWidth()-points.getTextBounds().width)*0.025f);	
		points.setY(Configuration.getHeight()*0.95f - points.getTextBounds().height);

		time.setX((Configuration.getWidth()-time.getTextBounds().width)*0.025f);	
		time.setY(Configuration.getHeight()*0.95f - time.getTextBounds().height);
		
		pauseButton.updateEntityState(Configuration.getWidth()*0.98f-pauseButton.width, 0.0f);

		katanaLevelBar.sprite.setSize(katanaExp*228+64,62);
		katanaLevelBar.sprite.setRegion(katanaLevelBar.sprite.getRegionX(),katanaLevelBar.sprite.getRegionY(), (int)katanaExp*228+64, 62);

		katanaLevelBackground.setEntityAlpha(0.5f);
		katanaLevelBar.setEntityAlpha(1.0f);
	}

	public void initializeNormalHUD() {
		initializeBase();
		Renderer.hudStage.addActor(points);
	}

	public void initializeTrainingHUD() {
		initializeBase();
	}

	public void initializeSurvivalHUD() {
		initializeBase();
		Renderer.hudStage.addActor(time);
	}

	public void updateNormalHud(final int enemyHitAmount, final float deltaTime) {
		updateHud(enemyHitAmount,deltaTime);
	}
	
	public void updateSurvivalHud(final int enemyHitAmount, final float deltaTime) {
		updateHud(enemyHitAmount,deltaTime);
		
		survivedTime += deltaTime;
		
		int hours = (int)survivedTime / 3600;
		int minutes = ((int)survivedTime / 60) % 60;
		int seconds = (int)survivedTime % 60;
		
		time.setText("Time " + hours/10+(hours-(hours/10)*10)+":"+minutes/10+(minutes-(minutes/10)*10)+":"+seconds/10+(seconds-(seconds/10*10)));
	}
	
	public void updateTrainingHud(final int enemyHitAmount, final float deltaTime) {
		updateHud(enemyHitAmount,deltaTime);
	}
	
	private void initializeBase() {
		Renderer.hudStage.addActor(pauseButton);
		Renderer.hudStage.addActor(katanaLevelBackground);
		Renderer.hudStage.addActor(katanaLevelInfo);
		Renderer.hudStage.addActor(katanaLevelBar);
		Renderer.hudStage.addActor(hit);
		Renderer.hudStage.addActor(combo);
	}
	
	private void updateHud(final int enemyHitAmount, final float deltaTime) {
		katanaExp -= deltaTime/100;
		katanaExp = MathUtils.clamp(katanaExp, 0.0f, 1.0f);
		
		comboAmount +=  enemyHitAmount;
		pointAmount += 10* enemyHitAmount;
		katanaExp += 0.1f/(katanaLevel+1)* enemyHitAmount;

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
				pointAmount += comboAmount*100;
				katanaExp += 0.5f/(katanaLevel+1);
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
			katanaExp += 0.5f/(katanaLevel+1);
			combo.setText(String.valueOf(comboAmount) + " Combo!"); 
			combo.setColor(1.0f, 1.0f, 1.0f, comboAlpha);
			combo.setPosition(0.0f, hit.getTextBounds().height);
		}

		if(katanaExp > 1.0f) {
			katanaExp = 0.0f;
			katanaLevel++;
			katanaLevelInfo.setText("Level " + katanaLevel);
			long id = game.resources.getSoundEffect("levelUp").play();
			game.resources.getSoundEffect("levelUp").setVolume(id, game.options.soundVolume);
		}

		katanaLevelBar.sprite.setSize(katanaExp*228+64,62);
		katanaLevelBar.sprite.setRegion(katanaLevelBar.sprite.getRegionX(),katanaLevelBar.sprite.getRegionY(), (int)(katanaExp*228)+64, 62);

		points.setText("Points: " + String.valueOf(pointAmount));
	}
	
	public int getPointAmount() {
		return pointAmount;
	}
	
	public float getSurvivedTime() {
		return survivedTime;
	}
}
