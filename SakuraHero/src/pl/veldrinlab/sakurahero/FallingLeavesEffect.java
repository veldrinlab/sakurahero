package pl.veldrinlab.sakurahero;

import pl.veldrinlab.sakuraEngine.core.SpriteActor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class FallingLeavesEffect {

	private SpriteBatch effectBatch;
	private Stage effectStage;
	
	private Array<SpriteActor> leaves;
	private float sakuraAccumulator;
	
	public FallingLeavesEffect(final float amount, final Texture texture) {
	
		effectBatch = new SpriteBatch();
		effectStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,effectBatch);
		
		leaves = new Array<SpriteActor>();
	
		for(int i = 0; i < amount; i++) {
			SpriteActor leaf = new SpriteActor(texture);
			leaf.getSprite().setPosition(MathUtils.random(0.0f, Configuration.getWidth()), MathUtils.random(0.0f, Configuration.getHeight()));
			leaf.rotationVelocity = MathUtils.random(0.0f, 1.0f);
			leaves.add(leaf);
			effectStage.addActor(leaf);
		}
	}
	
	public void setLeavesAlpha(final float alpha) {
		
		for(SpriteActor actor : leaves)
			actor.getSprite().setColor(1.0f,1.0f, 1.0f, alpha);
	}
	
	public void updateEffect(final float deltaTime) {
		
		sakuraAccumulator += deltaTime;
		
		for(int i = 0; i < leaves.size; ++i) {
			float currentX = leaves.get(i).getSprite().getX();
			float currentY = leaves.get(i).getSprite().getY();
				
			currentX += Math.sin(leaves.get(i).rotationVelocity+sakuraAccumulator)*0.5f;
			currentY -= 50.0f * deltaTime;
			
			leaves.get(i).getSprite().setX(currentX);
			leaves.get(i).getSprite().setY(currentY);
			

			leaves.get(i).getSprite().setRotation((float) (-Math.abs(Math.sin(leaves.get(i).rotationVelocity+sakuraAccumulator))*30.0f));
			
			if(currentY < -leaves.get(i).getSprite().getHeight())
				leaves.get(i).getSprite().setPosition(MathUtils.random(0.0f, 800.0f), 480.0f);

			
		}
	}
	
	public void renderEffect() {
		effectStage.draw();
	}
}
