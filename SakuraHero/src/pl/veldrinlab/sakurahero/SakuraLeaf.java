package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import pl.veldrinlab.sakuraEngine.core.SceneEntity;

public class SakuraLeaf extends SceneEntity{

	private FallingLeavesEffect fallingLeaves;

	private float leafAlpha;
	private boolean dead;
	private boolean collisionOccurred;

	public SakuraLeaf(final Sprite sprite, final int width, final int height) {
		super(sprite, width, height);

		leafAlpha = 1.0f;
		dead = false;
		collisionOccurred = false;
		
		fallingLeaves = new FallingLeavesEffect(3,false);
		fallingLeaves.setFallingBoundary(position.x - width, position.y, position.x+width, position.y+height);
	}

	@Override
	public void updateEntityState(final float x, final float y) {
		super.updateEntityState(x, y);
		
		fallingLeaves.setFallingBoundary(position.x - width, position.y, position.x+width, position.y+height);
	}
	
	public void update(final float deltaTime) {
		if(collisionOccurred) {
			leafAlpha -= deltaTime*0.25f;
			leafAlpha = MathUtils.clamp(leafAlpha, 0.0f, 1.0f);

			fallingLeaves.updateEffect(deltaTime);
			fallingLeaves.setLeavesAlpha(leafAlpha);
			
			if(leafAlpha < 0.00001f)
				dead = true;
		}
	}

	public void render() {
		fallingLeaves.renderEffect();
	}

	public void collisionResponse() {
		collisionOccurred = true;
		setEntityAlpha(0.0f);
		fallingLeaves.initializeEffect();
	}

	public void collisionDetection(final Array<Onigiri> onigiriArmy) {	

		for(Onigiri o : onigiriArmy)
			if(!collisionOccurred && o.isAttacking() && collisionCircle.contains(o.position.x+o.width*0.5f,o.position.y+o.height*0.5f)) {
				o.finishAttack();
				collisionResponse();
				return;
			}
	}
	
	public boolean isDead() {
		return dead;
	}
}
