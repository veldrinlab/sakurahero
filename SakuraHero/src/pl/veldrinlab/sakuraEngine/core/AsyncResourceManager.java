package pl.veldrinlab.sakuraEngine.core;

import java.util.HashMap;
import java.util.Map;

import pl.veldrinlab.sakuraEngine.utils.AssetDescriptor;
import pl.veldrinlab.sakuraEngine.utils.FontDescriptor;
import pl.veldrinlab.sakuraEngine.utils.FontLoader;
import pl.veldrinlab.sakuraEngine.utils.MeshLoader;
import pl.veldrinlab.sakuraEngine.utils.ResourceDescriptor;
import pl.veldrinlab.sakuraEngine.utils.ResourceType;
import pl.veldrinlab.sakuraEngine.utils.ShaderDescriptor;
import pl.veldrinlab.sakuraEngine.utils.ShaderLoader;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Class represents Sakura Engine Asynchronous Resource Manager. It is used to load, store and release game resource like meshes, textures, shaders, fonts and audio.
 * All resources is loaded from descriptors stores in json file. Resource is loaded asynchronous. It is good practice to load base resources on game start, then create
 * loading screen for rest files. ResourceManager can load resources anytime, loading can be blocked for finish. User can also define custom loaders for custom resources.
 * @author Szymon Jab³oñski
 *
 */
public class AsyncResourceManager {
	
	public static final String TAG = AsyncResourceManager.class.getSimpleName();
	
	private AssetManager assets;
	private Map<String,String> resourceDescriptors;
	
	/**
	 * Class constructor, creates resoruces arrays and initialize custom loaders.
	 */
	public AsyncResourceManager() {
		assets = new AssetManager();
		assets.setLoader(ShaderProgram.class, new ShaderLoader(new InternalFileHandleResolver()));
		assets.setLoader(Mesh.class, new MeshLoader(new InternalFileHandleResolver()));
		assets.setLoader(BitmapFont.class, new FontLoader(new InternalFileHandleResolver()));
		resourceDescriptors = new HashMap<String, String>();
	}

	/**
	 * Method is used udpate loading process.
	 * @return true if all resoruces in queue are loaded.
	 */
	public boolean updateLoading() {
		return assets.update();
	}
	
	/**
	 * Method is used to block loading until it is finished.
	 */
	public void finishLoading() {
		assets.finishLoading();
	}
	
	/**
	 * Method is used to start loading resources from json file.
	 * @param filePath is path to resources descriptor file.
	 */
	public void loadResources(final String filePath) {
		AssetDescriptor descriptor = getAssetDescriptor(filePath);
			
		if(descriptor.resourceDescriptors != null)
			for(ResourceDescriptor desc : descriptor.resourceDescriptors) {
				resourceDescriptors.put(desc.name, desc.path);

				if(desc.type == ResourceType.TEXTURE)
					assets.load(desc.path, Texture.class);
				else if(desc.type == ResourceType.SOUND)
					assets.load(desc.path, Sound.class);
				else if(desc.type == ResourceType.MUSIC)
					assets.load(desc.path, Music.class);
				else if(desc.type == ResourceType.MESH)
					assets.load(desc.path, Mesh.class);
				else
					Gdx.app.log(TAG, "Resource type " + desc.type.name() + " don't exist");
			}
		
		if(descriptor.shaderDescriptors != null)
			for(ShaderDescriptor desc : descriptor.shaderDescriptors) {
				String completePath = desc.vertPath+"+"+desc.fragPath;
				resourceDescriptors.put(desc.name, completePath);
				assets.load(completePath,ShaderProgram.class);
			}
		
		if(descriptor.fontDescriptors != null)
			for(FontDescriptor desc : descriptor.fontDescriptors) {
				String completePath = desc.fntPath+"+"+desc.imagePath;
				resourceDescriptors.put(desc.name, completePath);
				assets.load(completePath,BitmapFont.class);
			}
	}
	
	/**
	 * Method is used to release resources. Also use json file with descriptors.
	 * @param filePath is path to resources descriptor file.
	 */
	public void releaseResources(final String filePath) {
				
		AssetDescriptor descriptor = getAssetDescriptor(filePath);
		
		if(descriptor.resourceDescriptors != null)
			for(ResourceDescriptor desc : descriptor.resourceDescriptors) {
				
				if(desc.type == ResourceType.TEXTURE || desc.type == ResourceType.SOUND || desc.type == ResourceType.MUSIC || desc.type == ResourceType.MESH)
					assets.unload(desc.name); 
				else
					Gdx.app.log(TAG, "Resource type " + desc.type.name() + " don't exist");
			}
		
		if(descriptor.shaderDescriptors != null)
			for(ShaderDescriptor desc : descriptor.shaderDescriptors) {
				assets.unload(desc.name);
			}
		
		if(descriptor.fontDescriptors != null)
			for(FontDescriptor desc : descriptor.fontDescriptors) {
				assets.unload(desc.name);
			}
	}
		
	/**
	 * Accessor to loaded texture resource
	 * @param name is resource name id.
	 * @return
	 */
	public Texture getTexture(final String name) {
		
		return assets.get(resourceDescriptors.get(name), Texture.class);
	}

	/**
	 * Accessor to loaded mesh resource
	 * @param name is resource name id.
	 * @return
	 */
	public Mesh getMesh(final String name) {

		return assets.get(resourceDescriptors.get(name), Mesh.class);
	}
	
	/**
	 * Accessor to loaded shader program resource
	 * @param name is resource name id.
	 * @return
	 */
	public ShaderProgram getShader(final String name) {
		
		return assets.get(resourceDescriptors.get(name), ShaderProgram.class);
	}
	
	/**
	 * Accessor to loaded bitmap font resource
	 * @param name is resource name id.
	 * @return
	 */
	public BitmapFont getFont(final String name) {

		return assets.get(resourceDescriptors.get(name), BitmapFont.class);
	}

	/**
	 * Accessor to loaded sound effect resource
	 * @param name is resource name id.
	 * @return
	 */
	public Sound getSoundEffect(final String name) {
		
		return assets.get(resourceDescriptors.get(name), Sound.class);
	}
	
	/**
	 * Accessor to loaded music resource
	 * @param name is resource name id.
	 * @return
	 */
	public Music getMusic(final String name) {
		
		return assets.get(resourceDescriptors.get(name), Music.class);
	}
	
	/**
	 * Method is used to get asset descritpro which is stored in json file. After that, Manager can load each resources.
	 * @param filePath is path to asset descriptor file.
	 * @return loaded descriptor.
	 */
	private AssetDescriptor getAssetDescriptor(final String filePath) {
		
		AssetDescriptor descriptor = new AssetDescriptor();	
		Json json = new Json();
	
		FileHandle file = Gdx.files.internal(filePath);

		if(file.exists()) {

			String jsonData = file.readString();

			try {

				descriptor = json.fromJson(AssetDescriptor.class, jsonData);

			} catch(Exception e ) {
				
				Gdx.app.log(TAG, "Resource file " + filePath +" loading exception");	
				e.printStackTrace();
			}
		}
		else
			Gdx.app.log(TAG, "File " + filePath + " reading error detected!");
		
		return descriptor;
	}
}
