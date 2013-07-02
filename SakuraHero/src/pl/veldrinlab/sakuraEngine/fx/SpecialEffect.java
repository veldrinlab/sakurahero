package pl.veldrinlab.sakuraEngine.fx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Class represets abstarct represenation of SakuraEngine spaecial effects. Store batch for effect and effects shader.
 * @author Szymon Jab³oñski
 *
 */
public abstract class SpecialEffect {
	
	protected ShaderProgram shader;
	protected SpriteBatch batch;
	
	/**
	 * Class constructor. Initialize special effect batch with defined shader
	 * @param shader is special effect shader program.
	 */
	public SpecialEffect(final ShaderProgram shader) {
		this.shader = shader;
		batch = new SpriteBatch();
		batch.setShader(shader);
	}
	
	/**
	 * Method is used to activate special effect rendering.
	 */
	public void activateEffect() {
		batch.begin();
	}
	
	/**
	 * Method is used to finish special effect rendering.
	 */
	public void deactivateEffect() {
		batch.end();
	}
	
	public abstract void updateEffect(final float deltaTime);
}
