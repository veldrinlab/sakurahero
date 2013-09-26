package pl.veldrinlab.sakuraEngine.fx;

import pl.veldrinlab.sakuraEngine.core.SceneEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Class represents Radial Blur post-process effect. It can be used to simulate speed blur.
 * @author Szymon Jab³oñski
 *
 */
public class RadialBlurEffect extends PostEffect {
	
	private float sampleDist;;
	private float sampleStrength;
	private int multiplicator;
	
	/**
	 * Class constructor. Initailize Render Target and full screen quad representaion.
	 * @param shader
	 */
	public RadialBlurEffect(final ShaderProgram shader) {
		super(shader);
		renderTarget = new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		actor = new SceneEntity(renderTarget.getColorBufferTexture());
		stage.addActor(actor);
		actor.sprite.flip(false,true);
		
		sampleDist = 1.0f;
		sampleStrength = 2.2f;
	}

	/**
	 * Method is used to render effect.
	 */
	@Override
	public void renderEffect() {
		actor.sprite.setTexture(renderTarget.getColorBufferTexture());
		stage.draw();
	}

	/**
	 * Method is used to update effect. Calculate state and set shader uniforms.
	 * @param deltaTime is time step.
	 */
	@Override
	public void updateEffect(final float deltaTime) {
		shader.begin();
		shader.setUniformf("sampleDist", sampleDist);
		shader.setUniformf("sampleStrength", sampleStrength*multiplicator*0.25f);
		shader.end();
	}
	
	/**
	 * Method is used to set effect multiplicator which is used to control effect by time.
	 * @param multiplicator
	 */
	public void setMultiplicator(final int multiplicator) {
		this.multiplicator = multiplicator;
	}
}
