package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import pl.veldrinlab.sakuraEngine.core.Animation;
import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

public class SamuraiOnigiri extends Onigiri {

	private Vector2 moveDirection;	

	private SceneEntity shadow;
	private SceneEntity shadow2;
	private SceneEntity shadow3;

	public SamuraiOnigiri(final Sprite enemySprite, final Sprite explosionSprite) {
		super(enemySprite,explosionSprite,128,128);

		moveDirection = new Vector2();
		entityAnimation = new Animation(8,0.020f,this);

		shadow = new SceneEntity(Renderer.sceneAtlas.createSprite("onigiriSamurai"),128,128);	
		shadow2 = new SceneEntity(Renderer.sceneAtlas.createSprite("onigiriSamurai"),128,128);
		shadow3 = new SceneEntity(Renderer.sceneAtlas.createSprite("onigiriSamurai"),128,128);
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

		velocity.set(2.5f,2.5f);
		rotationVelocity = 5.0f;
		rotation = 0.0f;
		updateEntityState(x,y);
		setEntityAlpha(1.0f);

		timeAccumulator = 0.0f;
		collisionOccurred = false;

		explosionAnimation.initializeAnimation();
		entityAnimation.initializeAnimation();

		shadow.updateEntityState(x,y);
		shadow2.updateEntityState(x,y);
		shadow3.updateEntityState(x,y);

		shadow.setEntityAlpha(0.75f);
		shadow2.setEntityAlpha(0.5f);	
		shadow3.setEntityAlpha(0.25f);	

		explosionStarted = false;
		attackSoundFinished = false;
	}

	@Override
	public void setupRendering(final Stage stage) {
		stage.addActor(shadow3);
		stage.addActor(shadow2);
		stage.addActor(shadow);
		stage.addActor(this);
		stage.addActor(explosion);	
	}

	public	void update(final float deltaTime) {

		if(!collisionOccurred) {
			velocity.x += deltaTime*2;
			velocity.y += deltaTime*2;

			entityAnimation.updateAnimation(deltaTime);

			float shadowX = position.x  - moveDirection.x * velocity.x*2.5f;
			float shadowY = position.y - moveDirection.y * velocity.y*2.5f;

			float shadowX2 = position.x  - moveDirection.x * velocity.x*5.0f;
			float shadowY2 = position.y - moveDirection.y * velocity.y*5.0f;

			float shadowX3 =position.x  - moveDirection.x * velocity.x*7.5f;
			float shadowY3 = position.y - moveDirection.y * velocity.y*7.5f;

			position.x += moveDirection.x * velocity.x;
			position.y += moveDirection.y * velocity.y;

			updateEntityState(position.x, position.y);

			shadow.updateEntityState(shadowX, shadowY);
			shadow2.updateEntityState(shadowX2, shadowY2);
			shadow3.updateEntityState(shadowX3, shadowY3);

			updateEntityState(position.x, position.y);

			Vector2 temp = Vector2.Zero;
			temp.set(position.x, position.y);
			if(temp.dst(Configuration.getWidth()*0.5f, Configuration.getHeight()*0.5f) > respawnDistance)
				initialize(sakuraLeaves);

			if(temp.dst(Configuration.getWidth()*0.5f, Configuration.getHeight()*0.5f) < 200 && attack && !attackSoundFinished) {
				if(MathUtils.randomBoolean()) {
					long id = attackSound1.play();
					attackSound1.setVolume(id, options.soundVolume);
				}
				else {
					long id = attackSound2.play();
					attackSound2.setVolume(id, options.soundVolume);
				}

				attackSoundFinished = true;
			}
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
	public void collisionResponse() {
		collisionOccurred = true;
		attack = false;
		collisionPosition.set(getX(), getY());
		blowAngle = blowAngles.get(MathUtils.random(0, blowAngles.size-1));	
		entityAnimation.setDefinedFrame(8);
		shadow.setEntityAlpha(0.0f);
		shadow2.setEntityAlpha(0.0f);
		shadow3.setEntityAlpha(0.0f);

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
