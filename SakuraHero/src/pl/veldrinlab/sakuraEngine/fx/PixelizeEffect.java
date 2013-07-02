package pl.veldrinlab.sakuraEngine.fx;

import pl.veldrinlab.sakuraEngine.core.SpriteActor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Class represents pixelize fade effect which is fragment shader operations. It can be used to achieve also retro 8-bit sprites.
 * @author Szymon Jab³oñski
 *
 */
public class PixelizeEffect extends PostEffect {

	private float power;
	private float dx;
	private float dy;
	private float timer;
	private boolean finished;
	private boolean pixelizeFadeIn;

	/**
	 * Class constructor. Initailize Render Target and full screen quad representaion.
	 * @param shader
	 */
	public PixelizeEffect(final ShaderProgram shader) {
		super(shader);
		renderTarget = new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		actor = new SpriteActor(renderTarget.getColorBufferTexture());
		stage.addActor(actor);
		actor.getSprite().flip(false,true);
		power = 20.0f;
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
	 * Method is used to update effect. Calculate state and set shader uniforms.
	 * @param deltaTime is time step.
	 */
	@Override
	public void updateEffect(final float deltaTime) {	
		if(pixelizeFadeIn) {			
			timer += deltaTime;
			if(timer > 1.0f) {
				timer = 1.0f;
				finished = true;
			}
		}
		else {
			timer -= deltaTime;
			if(timer < 0.0f)
				finished = true;
		}
		
		dx = (power - (timer*(power-1.0f)))*(1.0f/Gdx.graphics.getWidth());
		dy = (power - (timer*(power-1.0f)))*(1.0f/Gdx.graphics.getHeight());
			
		shader.begin();
		shader.setUniformf("time", timer);
		shader.setUniformf("dx", dy);
		shader.setUniformf("dy", dx);
		shader.end();
	}

	/**
	 * Method is used to reset effect state.
	 * @param fadeIn is fade type. If true perform "in" effect, "out" otherwise.
	 */
	public void reset(final boolean fadeIn) {
		if(fadeIn)
			timer = 0.0f;
		else
			timer = 1.0f;
		
		finished = false;
		pixelizeFadeIn = fadeIn;
	}

	/**
	 * Method is used to check if effects is finihes.
	 * @return true if finished, false otherwise.
	 */
	public boolean isFinished() {
		return finished;
	}
}
