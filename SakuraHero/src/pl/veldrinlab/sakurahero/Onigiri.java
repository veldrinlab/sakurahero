package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import pl.veldrinlab.sakuraEngine.core.Animation;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.utils.Stack;

public abstract class Onigiri extends SceneEntity {

	protected static final float G = 100.0f;
	protected static final float V0 = 300.0f;
	
	protected SceneEntity explosion;
	protected Animation explosionAnimation;
	protected Animation entityAnimation;
	protected Vector2 collisionPosition;
	protected Array<Float> blowAngles;

	protected float blowAngle;
	protected float timeAccumulator;
	protected float respawnDistance;

	protected boolean collisionOccurred;
	protected boolean active;
	protected boolean attack;
	
	protected Array<SakuraLeaf> sakuraLeaves;
	
	public GameOptions options;
	
	protected boolean explosionStarted;
	protected boolean attackSoundFinished;
	public Sound explosionSound;
	public Sound attackSound1;
	public Sound attackSound2;
	public Sound deathSound1;
	public Sound deathSound2;
	
	public Onigiri(final Sprite sprite, final Sprite explosionSprite, final int width, final int height) {
		super(sprite, width, height);

		explosion = new SceneEntity(explosionSprite,width,height);
		explosionAnimation = new Animation(15,0.020f,explosion);
		collisionPosition = new Vector2();
		
		blowAngles = new Array<Float>();
		
		for(int i = 0; i < 12; i++)
			blowAngles.add(30.0f*i);
		
		respawnDistance = 1000.0f;
	}

	public abstract void initialize(final Array<SakuraLeaf> sakuraLeaves);	
	public abstract void setupRendering(final Stage stage);	
	public abstract void update(final float deltaTime);
	public abstract void collisionResponse();
	
	public int collisionDetection(final Stack<Vector2> katanaInput) {	
		int result = 0;
		
		for(int i = 0; i < katanaInput.size; ++i)
			if(!collisionOccurred && collisionCircle.contains(katanaInput.get(i).x, katanaInput.get(i).y)) {
				collisionResponse();
				result = 1;
				break;
			}
		
		return result;
	}
	
	public void setActive(final boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void finishAttack() {
		attack = false;
	}
	public boolean isAttacking() {
		return attack;
	}
}
