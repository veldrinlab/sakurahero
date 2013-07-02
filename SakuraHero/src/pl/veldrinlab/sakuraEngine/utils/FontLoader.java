package pl.veldrinlab.sakuraEngine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

/**
 * Class represents AssetLoader for Bitmapfont instances. The font data is loaded asynchronously. The font
 * is then created on the rendering thread, synchronously.
 * @author Szymon Jab³oñski
 *
 */
public class FontLoader extends AsynchronousAssetLoader<BitmapFont, FontParameter> {

	private BitmapFont font;
	
	/**
	 * Class constructor. Intialize file loader.
	 * @param resolver
	 */
	public FontLoader(final FileHandleResolver resolver) {
		super(resolver);
	}

	/**
	 * Method is used to perform asynchronous resource loading part. Nothing can we do.
	 * @param	manager is asset manager.
	 * @param	fileName is resource path.
	 * @param	parameter is resource parameters.
	 */
	@Override
	public void loadAsync(final AssetManager manager, final String fileName, final FontParameter parameter) {
	
	}
	
	/**
	 * Method is used to perform synchronus resource loading part. Create font object.
	 * @param	manager is asset manager.
	 * @param	fileName is resource path.
	 * @param	parameter is resource parameters.
	 */
	@Override
	public BitmapFont loadSync(final AssetManager manager, final String fileName, final FontParameter parameter) {
		String fntPath = fileName.substring(0, fileName.indexOf("+"));
		String imagePath = fileName.substring(fileName.indexOf("+")+1, fileName.length());	
	
		font = new BitmapFont(Gdx.files.internal(fntPath),Gdx.files.internal(imagePath),false);
		return font;
	}

	/**
	 * Method is used to get resource dependencies.
	 * @param fileName is resource name.
	 * @param parameter is resource parameters.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(final String filename,final FontParameter parameter) {
		return null;
	}
}