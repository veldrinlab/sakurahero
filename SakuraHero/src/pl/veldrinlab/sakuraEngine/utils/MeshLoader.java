package pl.veldrinlab.sakuraEngine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.loaders.wavefront.ObjLoader;
import com.badlogic.gdx.utils.Array;

/**
 * Class represents AssetLoader for Mesh instances. The mesh data buffers is loaded asynchronously. The mesh vertex buffers
 * is then created on the rendering renering thread, synchronously.
 * @author Szymon Jab³oñski
 *
 */
public class MeshLoader extends AsynchronousAssetLoader<Mesh, MeshParameter> {

	private Mesh mesh;
	
	/**
	 * Class constructor. Intialize file loader.
	 * @param resolver
	 */
	public MeshLoader(final FileHandleResolver resolver) {
		super(resolver);
	}

	/**
	 * Method is used to perform asynchronous resource loading part. Nothing can we do.
	 * @param	manager is asset manager.
	 * @param	fileName is resource path.
	 * @param	parameter is resource parameters.
	 */
	@Override
	public void loadAsync(final AssetManager manager,final String fileName, final MeshParameter parameter) {
		
	}

	/**
	 * Method is used to perform synchronus resource loading part. Load mesh data to vertex buffers.
	 * @param	manager is asset manager.
	 * @param	fileName is resource path.
	 * @param	parameter is resource parameters.
	 */
	@Override
	public Mesh loadSync(final AssetManager manager, final String fileName, final MeshParameter parameter) {
		
		ObjLoader loader = new ObjLoader();		
		mesh = loader.loadObj(Gdx.files.internal(fileName),true).getSubMeshes()[0].getMesh();
		
		//	m = ObjLoader.loadObj(Gdx.files.internal(fileName),true);
		//mesh = ObjLoader.loadObj(Gdx.files.internal(fileName),true).getSubMeshes()[0].getMesh();
		return mesh;
	}

	/**
	 * Method is used to get resource dependencies.
	 * @param fileName is resource name.
	 * @param parameter is resource parameters.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(final String filename, final MeshParameter parameter) {
		return null;
	}
}