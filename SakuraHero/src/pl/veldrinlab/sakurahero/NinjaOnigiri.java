package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import pl.veldrinlab.sakuraEngine.core.SpriteActor;

public class NinjaOnigiri extends SpriteActor {

	public boolean collisionOccurred;
	public float deathAccum;
	
	//TODO try to use Libgdx Animation class or write something own
	public SpriteActor explosion;
	private float animationAccumulator;
	private int frameAmount = 15;
	private int currentFrame = 0;
	private float FRAME_TIME = 0.020f;

	private float t;
	public Vector2 collisionPos = new Vector2();
	public float angle;
	private float[] angleOptions = new float[4];
	private float alphaAccumulator;
	
	//
	private boolean fadeIn;
	
	public NinjaOnigiri(Texture enemyTexture, final Texture explosionTexture) {
		super(enemyTexture);
		
		explosion = new SpriteActor(explosionTexture);
		explosion.getSprite().setSize(128.0f, 128.0f);
		angleOptions[0] = -60.0f;
		angleOptions[1] = 60.0f;
		angleOptions[2] = 120.0f;
		angleOptions[3] = -120.0f;
	}
	
	public void init() {
		
		float x = MathUtils.random(Configuration.getWidth()*0.2f,Configuration.getWidth()*0.8f);
		float y = MathUtils.random(Configuration.getHeight()*0.2f,Configuration.getHeight()*0.8f);
	
		alphaAccumulator = 0.0f;
		
		setPosition(x, y);
		getSprite().setColor(1.0f,1.0f,1.0f,0.0f);
		getSprite().setPosition(x,y);
		collisionCircle.set(x+getSprite().getWidth()*0.5f, y+getSprite().getHeight()*0.5f, 64.0f);

		// 
		deathAccum = 0.0f;
		t = 0.0f;
		explosion.getSprite().setRegion(128.0f*frameAmount, 0, 128, 128);
		collisionOccurred = false;
		getSprite().setRotation(0.0f);
		explosion.getSprite().setRegion(128.0f*(frameAmount-1), 0, 128, 128);
		currentFrame = 0;
		
		//
		fadeIn = true;
	}
	
	public	void update(final float deltaTime) {

		// stan pocz¹tkowy
		if(!collisionOccurred && fadeIn) {
			
			alphaAccumulator += deltaTime;
			final float alpha = MathUtils.clamp(alphaAccumulator, 0.0f, 1.0f);
			getSprite().setColor(1.0f,1.0f,1.0f,alpha);
			
			if(alphaAccumulator > 1.4999f)
				fadeIn = false;			
		}
		else if(!collisionOccurred && !fadeIn) {
			
			alphaAccumulator -= deltaTime;
			final float alpha = MathUtils.clamp(alphaAccumulator, 0.0f, 1.0f);
			getSprite().setColor(1.0f,1.0f,1.0f,alpha);
			
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

			float rotation = getSprite().getRotation();
			float rotationVelocity = 5.0f;
			rotation -= deltaTime* 90.0f*rotationVelocity;

			getSprite().setRotation(rotation);
			setPosition(x,y);
			getSprite().setPosition(x,y);
			explosion.getSprite().setPosition(x, y);
		}
		else if(collisionOccurred && deathAccum > 0.5f && (currentFrame != frameAmount-1)) {
			
			alphaAccumulator -= deltaTime;
			final float alpha = MathUtils.clamp(alphaAccumulator, 0.0f, 1.0f);
			getSprite().setColor(1.0f, 1.0f, 1.0f, alpha);
			// update explosion animation
			animationAccumulator += deltaTime;

			if(animationAccumulator > FRAME_TIME) {
				currentFrame = (currentFrame+1) % frameAmount;
				explosion.getSprite().setRegion(currentFrame*128, 0, 128, 128);
				animationAccumulator = 0.0f;
			}
		}
		else if(collisionOccurred && (currentFrame == frameAmount-1))
			init();
	}
	
	public void hit() {
		if(alphaAccumulator > 1.0f) {
			collisionOccurred = true;
			collisionPos.set(getX(), getY());
			angle = angleOptions[MathUtils.random(0, 3)];
		}
	}

}
