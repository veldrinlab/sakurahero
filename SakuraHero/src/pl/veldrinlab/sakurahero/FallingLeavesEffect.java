package pl.veldrinlab.sakurahero;

import pl.veldrinlab.sakuraEngine.core.SpriteActor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class FallingLeavesEffect {

	private SpriteBatch effectBatch;
	private Stage effectStage;
	
	private Array<SpriteActor> leaves;
	private float sakuraAccumulator;
	
	private Rectangle boundary;
	private float fallingVelocity;
	
	public FallingLeavesEffect(final float amount, final Texture texture) {
	
		effectBatch = new SpriteBatch();
		effectStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,effectBatch);
		
		leaves = new Array<SpriteActor>();
		boundary = new Rectangle(0.0f,0.0f,Configuration.getWidth(),Configuration.getHeight());
		
		fallingVelocity = 50.0f;
	
		for(int i = 0; i < amount; i++) {
			SpriteActor leaf = new SpriteActor(texture);
			leaves.add(leaf);
		}
	}
	
	public void initializeEffect() {
		for(SpriteActor a : leaves) {
			a.getSprite().setPosition(MathUtils.random(boundary.x, boundary.width), MathUtils.random(boundary.y, boundary.height));
			a.rotationVelocity = MathUtils.random(0.0f, 1.0f);
			effectStage.addActor(a);
		}
	}
	
	public void setLeavesAlpha(final float alpha) {
		
		for(SpriteActor actor : leaves)
			actor.getSprite().setColor(1.0f,1.0f, 1.0f, alpha);
	}
	
	public void setFallingBoundary(final float x, final float y, final float width, final float height) {
		boundary.set(x, y, width, height);
	}
	
	public void updateEffect(final float deltaTime) {
		
		sakuraAccumulator += deltaTime;
		
		for(int i = 0; i < leaves.size; ++i) {
			float currentX = leaves.get(i).getSprite().getX();
			float currentY = leaves.get(i).getSprite().getY();
				
			currentX += Math.sin(leaves.get(i).rotationVelocity+sakuraAccumulator)*0.5f;
			currentY -= fallingVelocity * deltaTime;
			
			leaves.get(i).getSprite().setX(currentX);
			leaves.get(i).getSprite().setY(currentY);
			
			leaves.get(i).getSprite().setRotation((float) (-Math.abs(Math.sin(leaves.get(i).rotationVelocity+sakuraAccumulator))*30.0f));
			
			if(currentY < -leaves.get(i).getSprite().getHeight())
				leaves.get(i).getSprite().setPosition(MathUtils.random(boundary.x,boundary.width), boundary.height);

//			leaves.get(i).getSprite().setPosition(MathUtils.random(250.0f, 600.0f), 150.0f);
		}
	}
	
	public void renderEffect() {
		effectStage.draw();
	}
}
