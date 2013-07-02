package pl.veldrinlab.sakuraEngine.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Class represents simple fade effect which is using image alpha controled by time accumulator. Effect is performed by shader.
 * @author Szymon Jab³oñski
 *
 */
public class FadeEffect extends SpecialEffect {
	
	private float effectTime;
	private float fadeInTime;
	private float stayTime;
	private float fadeOutTime;
	private float elapsedTime;
	private boolean skippable;
	private boolean skippableWhileFadingIn;
	private boolean locked;
	private boolean finished;
	
	/**
	 * Class constructor. Initailize rendering batch and default parameters
	 * @param params is fade parameters object.
	 * @param shader is effect shader
	 */
	public FadeEffect(final FadeEffectParameters params,final ShaderProgram shader) {
		super(shader);
		fadeInTime = params.fadeInTime;
		stayTime = params.stayTime;
		fadeOutTime = params.fadeOutTime;
		skippable = params.skippable;
		skippableWhileFadingIn = params.skippableWhileFadingIn;
		locked = params.locked;
		effectTime = fadeInTime + stayTime + fadeOutTime;
		finished = false;
		elapsedTime = 0.0f;
	}
		
	/**
	 * Method is used to render effect.
	 * @param texture is sprite image with fade effect
	 */
	public void renderEffect(final Texture texture) {
		batch.draw(texture,0,0);
	}
	
	/**
	 * Method is used to update effect. Calculate state and set shader uniforms.
	 * @param deltaTime is time step.
	 */
	@Override
	public void updateEffect(final float deltaTime) {
		elapsedTime += deltaTime;
		
		shader.begin();
			
		if(elapsedTime <= fadeInTime)
			shader.setUniformf("time", elapsedTime);
		else if (elapsedTime <= fadeInTime + stayTime)
			shader.setUniformf("time",1.0f);
		
		if(!locked && elapsedTime > fadeInTime + stayTime)
			shader.setUniformf("time",effectTime-elapsedTime);
	
		shader.end();
	
		if(locked && !Gdx.input.justTouched())
			return;
		else if(skippable && skippableWhileFadingIn && Gdx.input.justTouched() ||
		   skippable && elapsedTime >= fadeInTime && Gdx.input.justTouched() || 
		   elapsedTime > effectTime) {
			finished = true;
		}
	}
	
	/**
	 * Method is used to check if effects is finihes.
	 * @return true if finished, false otherwise.
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * Accessor to effect sprite batch.
	 * @return	effect sprite bacth
	 */
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
	
	/**
	 * Method is used to set effect batch.
	 * @param batch is sprite batch for special effect.
	 */
	public void setBatch(final SpriteBatch batch) {
		this.batch = batch;
	}
}
