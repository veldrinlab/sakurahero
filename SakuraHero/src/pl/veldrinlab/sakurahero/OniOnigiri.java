package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import pl.veldrinlab.sakuraEngine.core.Animation;
import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

public class OniOnigiri extends Onigiri {

	private Vector2 moveDirection;

	public OniOnigiri(final Sprite enemySprite, final Sprite explosionSprite) {
		super(enemySprite,explosionSprite,128,128);

		moveDirection = new Vector2();
		entityAnimation = new Animation(12,0.020f,this);
	}


	@Override
	public void initialize(final Array<SakuraLeaf> sakuraLeaves) {
		this.sakuraLeaves = sakuraLeaves;
		
		float x = MathUtils.random(-Configuration.getWidth()*0.25f,-width) + Configuration.getWidth()*1.25f*MathUtils.random(0, 1);		
		float y = MathUtils.random(-Configuration.getHeight()*0.5f, -height) + Configuration.getHeight()*1.5f*MathUtils.random(0, 1);

		if(sakuraLeaves != null && MathUtils.random(0.0f,1.0f) > 0.2f) {	
			int leafTargetId = MathUtils.random(0, sakuraLeaves.size-1);
			SceneEntity target = sakuraLeaves.get(leafTargetId);
			moveDirection.set(target.position.x,target.position.y);
			attack = true;
		}
		else
			moveDirection.set(400.0f, 240.0f);
		
		moveDirection.sub(x, y);
		moveDirection = moveDirection.nor();

		velocity.set(5.0f,5.0f);
		rotationVelocity = 5.0f;
		rotation = 0.0f;
		updateEntityState(x,y);
		setEntityAlpha(1.0f);

		timeAccumulator = 0.0f;
		collisionOccurred = false;

		explosionAnimation.initializeAnimation();
		entityAnimation.initializeAnimation();
	}

	@Override
	public void update(final float deltaTime) {

		if(!collisionOccurred) {

			entityAnimation.updateAnimation(deltaTime);

			position.x += moveDirection.x * velocity.x;
			position.y += moveDirection.y * velocity.y;

			updateEntityState(position.x, position.y);

			Vector2 temp = Vector2.Zero;
			temp.set(position.x, position.y);
			if(temp.dst(Configuration.getWidth()*0.5f, Configuration.getHeight()*0.5f) > respawnDistance)
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
			else {
				setEntityAlpha(MathUtils.clamp(1.0f-timeAccumulator, 0.0f, 1.0f));

				explosionAnimation.updateAnimation(deltaTime);
				if(explosionAnimation.animationCycleFinished())
					initialize(sakuraLeaves);			
			}
		}
	}

	@Override
	public void setupRendering(final Stage stage) {
		stage.addActor(this);
		stage.addActor(explosion);	
	}

	@Override
	public void collisionResponse() {
		collisionOccurred = true;
		attack = false;
		collisionPosition.set(getX(), getY());
		blowAngle = blowAngles.get(MathUtils.random(0, blowAngles.size-1));	
		entityAnimation.setDefinedFrame(12);

	}
}
