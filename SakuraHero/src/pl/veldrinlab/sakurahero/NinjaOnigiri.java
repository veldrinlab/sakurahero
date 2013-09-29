package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.veldrinlab.sakuraEngine.core.Animation;
import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.utils.Stack;

public class NinjaOnigiri extends Onigiri {

	private float alphaAccumulator;
	private float fadeState;
	
	public NinjaOnigiri(final Sprite enemySprite, final Sprite explosionSprite) {
		super(enemySprite,explosionSprite,128,128);
			
		entityAnimation = new Animation(1,0.020f,this);	
	}
	
	@Override
	public void initialize() {
		
		float x = MathUtils.random(Configuration.getWidth()*0.2f,Configuration.getWidth()*0.8f);
		float y = MathUtils.random(Configuration.getHeight()*0.2f,Configuration.getHeight()*0.8f);
	
		velocity.set(5.0f,5.0f);
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
			
			if(alphaAccumulator > 1.4999f)
				fadeState = -1.0f;	
			
			if(alpha < 0.00001f)
				initialize();
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
			else {
				setEntityAlpha(MathUtils.clamp(1.0f-timeAccumulator, 0.0f, 1.0f));

				explosionAnimation.updateAnimation(deltaTime);
				if(explosionAnimation.animationCycleFinished())
					initialize();			
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
		collisionPosition.set(getX(), getY());
		blowAngle = blowAngles.get(MathUtils.random(0, blowAngles.size-1));	
		entityAnimation.setDefinedFrame(1);
	}
}
