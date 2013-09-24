package pl.veldrinlab.sakuraEngine.fx;

import pl.veldrinlab.sakuraEngine.core.SceneEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Class represents Vibration post-process effect. It can be used to "shake" screen.
 * @author Szymon Jab³oñski
 *
 */
public class VibrationEffect extends PostEffect {

	private float vibrationX;
	private float vibrationY;
	private boolean finished;
	private float effectAccumulator;
	
	/**
	 * Class constructor. Initailize Render Target and full screen quad representaion.
	 * @param shader
	 */
	public VibrationEffect(final ShaderProgram shader) {
		super(shader);

		renderTarget = new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		actor = new SceneEntity(renderTarget.getColorBufferTexture());
		stage.addActor(actor);
		actor.getSprite().flip(false,true);
		finished = false;
		
		setVibrationX(0.0f);
		setVibrationY(0.0f);
	}

	/**
	 * Method is used to update effect. Calculate state and set shader uniforms.
	 * @param deltaTime is time step.
	 */
	@Override
	public void updateEffect(final float deltaTime) {	
		effectAccumulator -= deltaTime;
		if(effectAccumulator < 0.0f)
			finished = true;
		
		shader.begin();
		shader.setUniformf("vibrationX", MathUtils.random(-vibrationX, vibrationX));
		shader.setUniformf("vibrationY", MathUtils.random(-vibrationY, vibrationY));
		shader.end();
	}

	/**
	 * Method is used to render effect.
	 */
	@Override
	public void renderEffect() {
		actor.getSprite().setTexture(renderTarget.getColorBufferTexture());
		stage.draw();
	}

	/**
	 * Method is used to set vibration time
	 * @param count vibration time in seconds.
	 */
	public void setVibrationCount(final float count) {
		effectAccumulator = count;
		finished = false;
	}
	
	/**
	 * Accessor to current vibration X axis offset.
	 * @return vibration X axis offset.
	 */
	public float getVibrationX() {
		return vibrationX;
	}

	/**
	 * Method is used to set vibration X axis offset.
	 * @param vibrationX is vibration X axis offset.
	 */
	public void setVibrationX(final float vibrationX) {
		this.vibrationX = vibrationX;
	}

	/**
	 * Accessor to current vibration Y axis offset.
	 * @return vibration Y axis offset.
	 */
	public float getVibrationY() {
		return vibrationY;
	}

	/**
	 * Method is used to set vibration Y axis offset.
	 * @param vibrationX is vibration Y axis offset.
	 */
	public void setVibrationY(final float vibrationY) {
		this.vibrationY = vibrationY;
	}
	
	/**
	 * Method is used to check if effects is finihes.
	 * @return true if finished, false otherwise.
	 */
	public boolean isFinished() {
		return finished;
	}
}
