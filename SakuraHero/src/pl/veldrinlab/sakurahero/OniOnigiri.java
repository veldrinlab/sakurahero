package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import pl.veldrinlab.sakuraEngine.core.Animation;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

public class OniOnigiri extends SceneEntity {

	public boolean collisionOccurred;
	public float deathAccum;

	private Vector2 moveDirection;
		
	public SceneEntity explosion;
	private Animation explosionAnimation;
	private Animation entityAnimation;
	
	private float t;
	public Vector2 collisionPos = new Vector2();
	public float angle;
	private float[] angleOptions = new float[4];
	private float alpha;
	
	public OniOnigiri(final Sprite enemySprite, final Sprite explosionSprite) {
		super(enemySprite,128,128);

		moveDirection = new Vector2();
		
		explosion = new SceneEntity(explosionSprite,128,128);
		
		explosionAnimation = new Animation(15,0.020f,explosion);
		entityAnimation = new Animation(12,0.020f,this);
		
		angleOptions[0] = -60.0f;
		angleOptions[1] = 60.0f;
		angleOptions[2] = 120.0f;
		angleOptions[3] = -120.0f;
	}

	public void init() {

		boolean areaSelection = MathUtils.randomBoolean();
		float x = MathUtils.random(-Configuration.getWidth()*0.25f,Configuration.getWidth()*1.25f);
		float y = 0.0f;

		if(areaSelection)
			y = MathUtils.random(-Configuration.getHeight()*0.5f,0.0f);
		else
			y = MathUtils.random(Configuration.getHeight(),Configuration.getHeight()*1.5f);

		// set target - sakura leaf (some %)
		moveDirection.set(300.0f, 380.0f);
		moveDirection.sub(x, y);
		moveDirection = moveDirection.nor();

		
		// pozycja trzymana w aktorze TODO zobaczy co oferuje jeszcze sam aktor, dane tam trzymac!!!
		setPosition(x, y);

		sprite.setColor(1.0f,1.0f,1.0f,1.0f);
		sprite.setPosition(x,y);
		collisionCircle.set(x+sprite.getWidth()*0.5f, y+sprite.getHeight()*0.5f, 64.0f);
		
		// 
		deathAccum = 0.0f;
		t = 0.0f;
		explosionAnimation.initializeAnimation();
		
		
		collisionOccurred = false;
		sprite.setRotation(0.0f);
		
		
		rotationVelocity = 5.0f;
		rotation = 0.0f;
		
		entityAnimation.initializeAnimation();
	}
	
	
	public void update(final float deltaTime) {

		// stan pocz¹tkowy
		if(!collisionOccurred) {

			entityAnimation.updateAnimation(deltaTime);
			
			
			float x = getX();
			float y = getY();
			float velocity = 5.0f;

			x += moveDirection.x * velocity;
			y += moveDirection.y * velocity;

			updateEntityState(x, y);

			
			// update circle - method
			collisionCircle.set(x+sprite.getWidth()*0.5f, y+sprite.getHeight()*0.5f, 64.0f);

			Vector2 temp = Vector2.Zero;
			temp.set(x, y);
			if(temp.dst(400.0f, 280.0f) > 1000)
				init();
		}
		else if(collisionOccurred && deathAccum < 0.5f) {
			deathAccum += deltaTime;
			alpha -= deltaTime;
			t += deltaTime;

			final float g = 100.0f;		
			final float v0 = 300.0f;

			float x = collisionPos.x + v0*t*MathUtils.cosDeg(angle);
			float y = collisionPos.y + v0*t*MathUtils.sinDeg(angle) - (g*t*t*0.5f);

			entityAnimation.setDefinedFrame(12);
			
			rotation -= deltaTime* 90.0f*rotationVelocity;

			updateEntityState(x, y);
			explosion.updateEntityState(x, y);
		}
		else if(collisionOccurred && deathAccum > 0.5f && !explosionAnimation.animationCycleFinished()) {

			alpha -= deltaTime;
			alpha = MathUtils.clamp(alpha, 0.0f, 1.0f);
			sprite.setColor(1.0f, 1.0f, 1.0f, alpha);
			
			explosionAnimation.updateAnimation(deltaTime);
		}
		else if(collisionOccurred && explosionAnimation.animationCycleFinished())
			init();
	}

	public void hit() {
		collisionOccurred = true;
		collisionPos.set(getX(), getY());
		angle = angleOptions[MathUtils.random(0, 3)];
	}

}
