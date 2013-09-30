package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import pl.veldrinlab.sakuraEngine.core.Animation;
import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;
import pl.veldrinlab.sakuraEngine.utils.Stack;

public class NinjaOnigiri extends Onigiri {

	private float alphaAccumulator;
	private float fadeState;
	private boolean targetApproved;
	
	public NinjaOnigiri(final Sprite enemySprite, final Sprite explosionSprite) {
		super(enemySprite,explosionSprite,128,128);
			
		entityAnimation = new Animation(1,0.0f,this);	
	}
	
	@Override
	public void initialize(final Array<SakuraLeaf> sakuraLeaves) {
		this.sakuraLeaves = sakuraLeaves;
		
		float x = MathUtils.random(Configuration.getWidth()*0.2f,Configuration.getWidth()*0.8f);
		float y = MathUtils.random(Configuration.getHeight()*0.2f,Configuration.getHeight()*0.8f);
		
		targetApproved = attack = false;
		
		if(sakuraLeaves != null && MathUtils.random(0.0f,1.0f) > 0.75f) {	
			int leafTargetId = MathUtils.random(0, sakuraLeaves.size-1);
			SceneEntity target = sakuraLeaves.get(leafTargetId);
			x = target.position.x + target.width*0.5f - width*0.5f;
			y = target.position.y + target.height*0.5f - height*0.5f;
			targetApproved = true;
		}
		
		rotation = 0.0f;
		rotationVelocity = 5.0f;
		
		setEntityAlpha(0.0f);
		updateEntityState(x,y);
	
		alphaAccumulator = 0.0f;
		timeAccumulator = 0.0f;
		collisionOccurred = false;
		fadeState = 1.0f;

		explosionAnimation.initializeAnimation();
		entityAnimation.initializeAnimation();
		
		explosionStarted = false;
	}
	
	@Override
	public void setupRendering(final Stage stage) {
		stage.addActor(this);
		stage.addActor(explosion);		
	}
	
	@Override
	public	void update(final float deltaTime) {

		if(!collisionOccurred) {
			alphaAccumulator += fadeState*deltaTime;
			final float alpha = MathUtils.clamp(alphaAccumulator, 0.0f, 1.0f);
			setEntityAlpha(alpha);
			
			attack = targetApproved && (alphaAccumulator > 1.0f);
				
			if(alphaAccumulator > 1.4999f)
				fadeState = -1.0f;	
			
			if(alpha < 0.00001f)
				initialize(sakuraLeaves);
		}
		else {
			float x = collisionPosition.x + V0*timeAccumulator*MathUtils.cosDeg(blowAngle);
			float y = collisionPosition.y + V0*timeAccumulator*MathUtils.sinDeg(blowAngle) - (G*timeAccumulator*timeAccumulator*0.5f);

			timeAccumulator += deltaTime;
			rotation -= deltaTime* 90.0f*rotationVelocity;

			if(timeAccumulator < 0.5f) {
				updateEntityState(x, y);
				explosion.updateEntityState(x, y);
			}
			else if(!explosionStarted && timeAccumulator > 0.5f) {
				explosionStarted = true;
				long id = explosionSound.play();
				explosionSound.setVolume(id, options.soundVolume);
			}
			else if(explosionStarted) {
				setEntityAlpha(MathUtils.clamp(1.0f-timeAccumulator, 0.0f, 1.0f));
				
				explosionAnimation.updateAnimation(deltaTime);
				if(explosionAnimation.animationCycleFinished())
					initialize(sakuraLeaves);			
			}
		}
	}
	
	@Override
	public int collisionDetection(final Stack<Vector2> katanaInput) {
		
		if(alphaAccumulator < 1.0f)
			return 0;
		
		int result = 0;
		
		for(int i = 0; i < katanaInput.size; ++i)
			if(!collisionOccurred && collisionCircle.contains(katanaInput.get(i).x, katanaInput.get(i).y)) {
				collisionResponse();
				result = 1;
				break;
			}
		
		return result;
	}
	
	@Override
	public void collisionResponse() {
		collisionOccurred = true;
		attack = false;
		collisionPosition.set(getX(), getY());
		blowAngle = blowAngles.get(MathUtils.random(0, blowAngles.size-1));	
		entityAnimation.setDefinedFrame(1);
			
		if(MathUtils.random() > 0.9f) {
			if(MathUtils.randomBoolean()) {
				long id = deathSound1.play();
				deathSound1.setVolume(id, options.soundVolume);
			}
			else {
				long id = deathSound2.play();
				deathSound2.setVolume(id, options.soundVolume);
			}
		}
	}
}
