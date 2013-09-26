package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import pl.veldrinlab.sakuraEngine.core.Animation;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

//to jest generalnie z³a nazwa, zmienic i zrobic cos z tego - SceneEntity
public class SamuraiOnigiri extends SceneEntity {

	//TODO animacja itp. - moze basowa klasa samego Enemy i jakies roznice w funkcji aktualizaujacej - cos w stylu wzorca Strategii

	//fake motion blur
	public SceneEntity shadow;
	public SceneEntity shadow2;
	public SceneEntity shadow3;
	
	// tor ruchu opracowac

	// explozja po³¹czona z bytem
	// stan ataku, stan œmierci i stan ekspolozji

	private Vector2 moveDirection;

	public boolean collisionOccurred;
	public float deathAccum;

	public SceneEntity explosion;
	private Animation explosionAnimation;
	private Animation entityAnimation;

	private float t;
	public Vector2 collisionPos = new Vector2();
	public float angle;
	private float[] angleOptions = new float[4];
	private float alpha;

	public SamuraiOnigiri(final Sprite enemySprite, final Sprite explosionSprite) {
		super(enemySprite,128,128);

		moveDirection = new Vector2();

		explosion = new SceneEntity(explosionSprite,128,128);
		
		explosionAnimation = new Animation(15,0.020f,explosion);
		entityAnimation = new Animation(9,0.05f,this);
				
		angleOptions[0] = -60.0f;
		angleOptions[1] = 60.0f;
		angleOptions[2] = 120.0f;
		angleOptions[3] = -120.0f;

	
		shadow = new SceneEntity(Renderer.sceneAtlas.createSprite("onigiriSamurai"),128,128);	
		shadow2 = new SceneEntity(Renderer.sceneAtlas.createSprite("onigiriSamurai"),128,128);
		shadow3 = new SceneEntity(Renderer.sceneAtlas.createSprite("onigiriSamurai"),128,128);
	}

	public void init() {

		boolean areaSelection = MathUtils.randomBoolean();
		float x = MathUtils.random(-Configuration.getWidth()*0.25f,Configuration.getWidth()*1.25f);
		float y = 0.0f;

		if(areaSelection)
			y = MathUtils.random(-Configuration.getHeight()*0.5f,0.0f);
		else
			y = MathUtils.random(Configuration.getHeight(),Configuration.getHeight()*1.5f);

		// set target - samrua leaf
		moveDirection.set(400.0f, 280.0f);
		moveDirection.sub(x, y);
		moveDirection = moveDirection.nor();

		rotation = 0.0f;
		rotationVelocity = 5.0f;
		
		setEntityAlpha(1.0f);
		updateEntityState(x,y);
		
		//TODO to te¿ dodaæ do stanu?
		collisionCircle.set(x+sprite.getWidth()*0.5f, y+sprite.getHeight()*0.5f, 64.0f);

		// shadow
		shadow.updateEntityState(x,y);
		shadow2.updateEntityState(x,y);
		shadow3.updateEntityState(x,y);
		
		shadow.setEntityAlpha(0.75f);
		shadow2.setEntityAlpha(0.5f);	
		shadow3.setEntityAlpha(0.25f);
	
		// 
		deathAccum = 0.0f;
		t = 0.0f;
		collisionOccurred = false;
		alpha = 1.0f;

		explosionAnimation.initializeAnimation();
		entityAnimation.initializeAnimation();
	}

	public	void update(final float deltaTime) {

		// stan pocz¹tkowy
		if(!collisionOccurred) {
			
			entityAnimation.updateAnimation(deltaTime);
			
			float x = getX();
			float y = getY();
			float velocity = 5.0f;
			
			float shadowX = x - moveDirection.x * velocity*5;
			float shadowY = y - moveDirection.y * velocity*5;
			
			float shadowX2 = x - moveDirection.x * velocity*10;
			float shadowY2 = y - moveDirection.y * velocity*10;
			
			float shadowX3 = x - moveDirection.x * velocity*15;
			float shadowY3 = y - moveDirection.y * velocity*15;
			
			x += moveDirection.x * velocity;
			y += moveDirection.y * velocity;

			updateEntityState(x, y);
			
			shadow.updateEntityState(shadowX, shadowY);
			shadow2.updateEntityState(shadowX2, shadowY2);
			shadow3.updateEntityState(shadowX3, shadowY3);
			
			// update circle
			collisionCircle.set(x+sprite.getWidth()*0.5f, y+sprite.getHeight()*0.5f, 64.0f);

			Vector2 temp = Vector2.Zero;
			temp.set(x, y);
			if(temp.dst(400.0f, 280.0f) > 1000)
				init();
		}
		else if(collisionOccurred && deathAccum < 0.5f) {
			
			
			deathAccum += deltaTime;
			alpha -= deltaTime;
			t+= deltaTime;

			final float g = 100.0f;		
			final float v0 = 300.0f;
		
			float x = collisionPos.x + v0*t*MathUtils.cosDeg(angle);
			float y = collisionPos.y + v0*t*MathUtils.sinDeg(angle) - (g*t*t*0.5f);

			rotation -= deltaTime* 90.0f*rotationVelocity;

			entityAnimation.setDefinedFrame(8);

			updateEntityState(x, y);
			explosion.updateEntityState(x, y);
			
		}
		else if(collisionOccurred && deathAccum > 0.5f && !explosionAnimation.animationCycleFinished()) {

			alpha -= deltaTime;
			alpha = MathUtils.clamp(alpha, 0.0f, 1.0f);
			setEntityAlpha(alpha);
			
			explosionAnimation.updateAnimation(deltaTime);
		}
		else if(collisionOccurred && explosionAnimation.animationCycleFinished())
			init();
	}

	public void hit() {
		// losowanie 60, 120, -60, -120


		collisionOccurred = true;
		collisionPos.set(getX(), getY());
		angle = angleOptions[MathUtils.random(0, 3)];
		
		shadow.setEntityAlpha(0.0f);
		shadow2.setEntityAlpha(0.0f);
		shadow3.setEntityAlpha(0.0f);
	}
}
