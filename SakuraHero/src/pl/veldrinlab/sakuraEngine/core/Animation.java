package pl.veldrinlab.sakuraEngine.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Animation {
	
	//TODO w sceneEntity trzymac rozmiary!
	public SceneEntity entity;
	public int frameAmount;
	public int currentFrame;
	public float frameDuration;
	public float timeAccumulator;
	
	public Vector2 origin;
	
	public Animation(final int amount, final float duration, final SceneEntity entity) {
		this.entity = entity;
		
		origin = new Vector2(entity.getSprite().getRegionX(),entity.getSprite().getRegionY());
		
		currentFrame = 0;
		timeAccumulator = 0.0f;
		frameAmount = amount;
		frameDuration = duration;
		
	}
	
	public void initializeAnimation() {
		currentFrame = 0;
		entity.getSprite().setRegion((int)origin.x+128*(frameAmount-1), (int)origin.y, 128, 128);
	}
	
	public void updateAnimation(final float deltaTime) {	
		
		timeAccumulator += deltaTime;

		if(timeAccumulator > frameDuration) {
			currentFrame = (currentFrame+1) % frameAmount;
			entity.getSprite().setRegion((int)origin.x+128*currentFrame, (int)origin.y, 128, 128);
			timeAccumulator = 0.0f;
		}
	}
	
	public void setDefinedFrame(final int frame) {
		entity.getSprite().setRegion((int)origin.x+128*frame, (int)origin.y, 128, 128);
	}
	
	public boolean animationCycleFinished() {
		return (currentFrame == frameAmount-1);
	}
}
