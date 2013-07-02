package pl.veldrinlab.sakuraEngine.fx;

import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SpriteActor;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Class represents abstract post-process effect. It extends special effect by adding render target.
 * @author Szymon Jab³oñski
 *
 */
public abstract class PostEffect extends SpecialEffect {

	protected FrameBuffer renderTarget;
	protected Stage stage;
	protected SpriteActor actor;
	
	/**
	 * Class constructor. Initailize Special Effect batch.
	 * @param shader
	 */
	public PostEffect(final ShaderProgram shader) {
		super(shader);
	}
	
	/**
	 * Method is used to start rendering to render target.
	 */
	public void startOffScreenRendering() {
		Renderer.clearScreen();
		renderTarget.begin();
		Renderer.clearScreen();
	}
	
	/**
	 * Method is used to finish rendering to render target.
	 */
	public void finishOffScreenRendering() {
		renderTarget.end();
	}
	
	/**
	 * Method is used to dispos allocated memeory.
	 */
	public void dispose() {
		renderTarget.dispose();
	}
	
	public abstract void renderEffect();
}

