package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import pl.veldrinlab.sakuraEngine.core.SpriteActor;

//to jest generalnie z³a nazwa, zmienic i zrobic cos z tego - SpriteActor
public class SamuraiOnigiri extends SpriteActor {

	//TODO animacja itp. - moze basowa klasa samego Enemy i jakies roznice w funkcji aktualizaujacej - cos w stylu wzorca Strategii

	// tor ruchu opracowac

	// explozja po³¹czona z bytem
	// stan ataku, stan œmierci i stan ekspolozji

	private Vector2 moveDirection;

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
	private float alpha;
	
	public SamuraiOnigiri(final Texture enemyTexture, final Texture explosionTexture) {
		super(enemyTexture);

		moveDirection = new Vector2();
		// TODO Auto-generated constructor stub

		//TODO remove magic strings, use with/height 
		explosion = new SpriteActor(explosionTexture);
		explosion.getSprite().setSize(128.0f, 128.0f);
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

		// set target - samrua leaf
		moveDirection.set(400.0f, 280.0f);
		moveDirection.sub(x, y);
		moveDirection = moveDirection.nor();

		// pozycja trzymana w aktorze TODO zobaczy co oferuje jeszcze sam aktor, dane tam trzymac!!!
		setPosition(x, y);

		getSprite().setColor(1.0f,1.0f,1.0f,1.0f);
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
		alpha = 1.0f;
	}

	public	void update(final float deltaTime) {

		// stan pocz¹tkowy
		if(!collisionOccurred) {
			float x = getX();
			float y = getY();
			float velocity = 5.0f;


			x += moveDirection.x * velocity;
			y += moveDirection.y * velocity;

			setPosition(x,y);
			getSprite().setPosition(x,y);

			// update circle
			collisionCircle.set(x+getSprite().getWidth()*0.5f, y+getSprite().getHeight()*0.5f, 64.0f);

			position.set(x, y);
			if(position.dst(400.0f, 280.0f) > 1000)
				init();
		}
		else if(collisionOccurred && deathAccum < 0.5f) {
			deathAccum += deltaTime;
			alpha -= deltaTime;
			t+= deltaTime;
			
			final float g = 100.0f;		
			final float v0 = 300.0f;
		
//			float deltaX = v0*deltaTime*MathUtils.cosDeg(10.0f);
//			float deltaY = v0*deltaTime*MathUtils.sinDeg(10.0f) - (g*deltaTime*deltaTime*0.5f);
//			
//			
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
			
			alpha -= deltaTime;
			alpha = MathUtils.clamp(alpha, 0.0f, 1.0f);
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
		// losowanie 60, 120, -60, -120
		
		getSprite().setTexture(new Texture(Gdx.files.internal("onigiriSamuraiDead.png")));
		collisionOccurred = true;
		collisionPos.set(getX(), getY());
		angle = angleOptions[MathUtils.random(0, 3)];
	}
}
