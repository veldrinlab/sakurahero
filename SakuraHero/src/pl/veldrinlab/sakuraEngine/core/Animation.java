package pl.veldrinlab.sakuraEngine.core;

public class Animation {
	
	public SceneEntity entity;
	public int frameAmount;
	public int currentFrame;
	public float frameDuration;
	public float timeAccumulator;
	
	public Animation(final int amount, final float duration, final SceneEntity entity) {
		this.entity = entity;
		
		frameAmount = amount;
		currentFrame = 0;
		timeAccumulator = 0.0f;
		frameDuration = duration;	
	}
	
	public void initializeAnimation() {
		currentFrame = 0;
		entity.setVerticalRegion(entity.width*(frameAmount-1));
	}
	
	public void updateAnimation(final float deltaTime) {		
		timeAccumulator += deltaTime;

		if(timeAccumulator > frameDuration) {
			currentFrame = (currentFrame+1) % frameAmount;
			entity.setVerticalRegion(entity.width*currentFrame);
			timeAccumulator = 0.0f;
		}
	}
	
	public void setDefinedFrame(final int frame) {
		entity.setVerticalRegion(entity.width*frame);
	}
	
	public boolean animationCycleFinished() {
		return (currentFrame == frameAmount-1);
	}
}
