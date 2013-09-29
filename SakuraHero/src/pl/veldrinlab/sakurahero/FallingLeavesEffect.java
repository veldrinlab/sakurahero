package pl.veldrinlab.sakurahero;

import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class FallingLeavesEffect {

	private SpriteBatch effectBatch;
	private Stage effectStage;
	
	private Array<SceneEntity> leaves;
	private float sakuraAccumulator;
	
	private Rectangle boundary;
	private float fallingVelocity;
	
	private boolean continuousEffect;
	
	public FallingLeavesEffect(final float amount, final boolean continuousEffect) {
		this.continuousEffect = continuousEffect;
		
		effectBatch = new SpriteBatch();
		effectStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,effectBatch);
		
		leaves = new Array<SceneEntity>();
		boundary = new Rectangle(0.0f,0.0f,Configuration.getWidth(),Configuration.getHeight());
		
		fallingVelocity = 50.0f;
		
		for(int i = 0; i < amount; i++) {
			SceneEntity leaf = new SceneEntity(Renderer.introAtlas.createSprite("sakuraLeaf"),32,32);
			leaves.add(leaf);
		}
	}
	
	public void initializeEffect() {
		for(SceneEntity a : leaves) {
			a.updateEntityState(MathUtils.random(boundary.x, boundary.width), MathUtils.random(boundary.y, boundary.height));
			a.rotationVelocity = MathUtils.random(0.0f, 1.0f);
			effectStage.addActor(a);
		}
	}
	
	public void setLeavesAlpha(final float alpha) {
		
		for(SceneEntity actor : leaves)
			actor.setEntityAlpha(alpha);
	}
	
	public void setFallingBoundary(final float x, final float y, final float width, final float height) {
		boundary.set(x, y, width, height);
	}
	
	public void updateEffect(final float deltaTime) {
		
		sakuraAccumulator += deltaTime;
		
		for(int i = 0; i < leaves.size; ++i) {
			float currentX = leaves.get(i).position.x;
			float currentY = leaves.get(i).position.y;
				
			currentX += Math.sin(leaves.get(i).rotationVelocity+sakuraAccumulator)*0.5f;
			currentY -= fallingVelocity * deltaTime;
			
			leaves.get(i).rotation = (float) (-Math.abs(Math.sin(leaves.get(i).rotationVelocity+sakuraAccumulator))*30.0f);
			leaves.get(i).updateEntityState(currentX, currentY);

			if(continuousEffect && currentY < -leaves.get(i).height)
				leaves.get(i).updateEntityState(MathUtils.random(boundary.x,boundary.width), boundary.height);
		}
	}
	
	public void renderEffect() {
		effectStage.draw();
	}
}
