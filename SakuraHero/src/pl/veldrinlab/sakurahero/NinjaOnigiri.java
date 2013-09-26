package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import pl.veldrinlab.sakuraEngine.core.Animation;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

public class NinjaOnigiri extends SceneEntity {

	public boolean collisionOccurred;
	public float deathAccum;
	
	public SceneEntity explosion;
	
	private Animation explosionAnimation;	
	private Animation entityAnimation;
	
	
	private float t;
	public Vector2 collisionPos = new Vector2();
	public float angle;
	private float[] angleOptions = new float[4];

	private float alphaAccumulator;
	private boolean fadeIn;

	
	public NinjaOnigiri(final Sprite enemySprite, final Sprite explosionSprite) {
		super(enemySprite,128,128);
		
		explosion = new SceneEntity(explosionSprite,128,128);
		
		explosionAnimation = new Animation(15,0.020f,explosion);
		entityAnimation = new Animation(1,0.020f,this);
	
		angleOptions[0] = -60.0f;
		angleOptions[1] = 60.0f;
		angleOptions[2] = 120.0f;
		angleOptions[3] = -120.0f;
		
	}
	
	public void init() {
		
		float x = MathUtils.random(Configuration.getWidth()*0.2f,Configuration.getWidth()*0.8f);
		float y = MathUtils.random(Configuration.getHeight()*0.2f,Configuration.getHeight()*0.8f);
	
		alphaAccumulator = 0.0f;
		rotation = 0.0f;
		rotationVelocity = 5.0f;
		
		setEntityAlpha(0.0f);
		updateEntityState(x,y);
		collisionCircle.set(x+sprite.getWidth()*0.5f, y+sprite.getHeight()*0.5f, 64.0f);

		// 
		deathAccum = 0.0f;
		t = 0.0f;
		collisionOccurred = false;
		fadeIn = true;
	
		entityAnimation.initializeAnimation();
		explosionAnimation.initializeAnimation();
	}
	
	public	void update(final float deltaTime) {

		// stan pocz¹tkowy
		if(!collisionOccurred && fadeIn) {
			
			alphaAccumulator += deltaTime;
			final float alpha = MathUtils.clamp(alphaAccumulator, 0.0f, 1.0f);
			setEntityAlpha(alpha);
			
			if(alphaAccumulator > 1.4999f)
				fadeIn = false;			
		}
		else if(!collisionOccurred && !fadeIn) {
			
			alphaAccumulator -= deltaTime;
			final float alpha = MathUtils.clamp(alphaAccumulator, 0.0f, 1.0f);
			setEntityAlpha(alpha);
			
			if(alpha < 0.00001f)
				init();	
		}
		else if(collisionOccurred && deathAccum < 0.5f) {
			deathAccum += deltaTime;
			alphaAccumulator -= deltaTime;
			t += deltaTime;
			
			final float g = 100.0f;		
			final float v0 = 300.0f;
		
			float x = collisionPos.x + v0*t*MathUtils.cosDeg(angle);
			float y = collisionPos.y + v0*t*MathUtils.sinDeg(angle) - (g*t*t*0.5f);

			rotation -= deltaTime* 90.0f*rotationVelocity;
			updateEntityState(x, y);
			explosion.updateEntityState(x, y);
		}
		else if(collisionOccurred && deathAccum > 0.5f && !explosionAnimation.animationCycleFinished()) {
			
			alphaAccumulator -= deltaTime;
			final float alpha = MathUtils.clamp(alphaAccumulator, 0.0f, 1.0f);
			setEntityAlpha(alpha);
			
			explosionAnimation.updateAnimation(deltaTime);

		}
		else if(collisionOccurred && explosionAnimation.animationCycleFinished())
			init();
	}
	
	public boolean hit() {
		if(alphaAccumulator > 1.0f) {
			collisionOccurred = true;
			collisionPos.set(getX(), getY());
			angle = angleOptions[MathUtils.random(0, 3)];
			entityAnimation.setDefinedFrame(1);
			return true;
		}
		return false;
	}
}
