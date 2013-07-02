package pl.veldrinlab.sakuraEngine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

/**
 * Class represents AssetLoader for ShaderProgram instances. The shader program data is loaded asynchronously. The shader
 * is then created on the rendering thread, synchronously.
 * @author Szymon Jab³oñski
 *
 */
public class ShaderLoader extends AsynchronousAssetLoader<ShaderProgram, ShaderParameter> {

	private ShaderProgram shader;
	private String vertProgram;
	private String fragProgram;
	
	/**
	 * Class constructor. Intialize file loader.
	 * @param resolver
	 */
	public ShaderLoader(final FileHandleResolver resolver) {
		super(resolver);
	}
	
	/**
	 * Method is used to perform asynchronous resource loading part. Load shader text data.
	 * @param	manager is asset manager.
	 * @param	fileName is resource path.
	 * @param	parameter is resource parameters.
	 */
	@Override
	public void loadAsync(final AssetManager manager,final String fileName,final ShaderParameter parameter) {
		
		String vertPath = fileName.substring(0, fileName.indexOf("+"));
		String fragPath = fileName.substring(fileName.indexOf("+")+1, fileName.length());
		
		FileHandle vertFile = Gdx.files.internal(vertPath);
		FileHandle fragFile = Gdx.files.internal(fragPath);

		if(vertFile.exists() && fragFile.exists()) {
			
			vertProgram = vertFile.readString();
			fragProgram = fragFile.readString();
		}
	}
		
	/**
	 * Method is used to perform synchronus resource loading part. Create shader: compile, link and create shader program.
	 * @param	manager is asset manager.
	 * @param	fileName is resource path.
	 * @param	parameter is resource parameters.
	 */
	@Override
	public ShaderProgram loadSync(final AssetManager manager,final String fileName,final ShaderParameter parameter) {
		
		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(vertProgram, fragProgram);
		return shader;
	}

	/**
	 * Method is used to get resource dependencies.
	 * @param fileName is resource name.
	 * @param parameter is resource parameters.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(final String fileName,final ShaderParameter parameter) {
		return null;
	}
}
