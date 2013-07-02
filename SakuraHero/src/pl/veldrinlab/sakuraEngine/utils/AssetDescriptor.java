package pl.veldrinlab.sakuraEngine.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Class represents SakuraEngine assets descriptors collection. Store data of all loaded descriptors which will be then used
 * by asset loaders to load such assets like textures, sounds etc.
 * @author Szymon Jab³oñski
 *
 */
public class AssetDescriptor implements Serializable {
	
	public Array<ResourceDescriptor> resourceDescriptors;
	public Array<ShaderDescriptor> shaderDescriptors;
	public Array<FontDescriptor> fontDescriptors;

	/**
	 * Class default constructor. Create descriptor arrays.
	 */
	public AssetDescriptor() {
		resourceDescriptors = new Array<ResourceDescriptor>();
		shaderDescriptors = new Array<ShaderDescriptor>();
		fontDescriptors = new Array<FontDescriptor>();	
	}
	
	/**
	 * Method is used to write descriptor data to Json file.
	 * @param	json is json reader/writer object.
	 */
	@Override
	public void write(final Json json) {
		json.writeValue("resources",resourceDescriptors);
		json.writeValue("shaders",shaderDescriptors);
		json.writeValue("fonts",fontDescriptors);
	}

	/**
	 * Method is used to read descriptor data from Json file.
	 * @param	json is json reader/writer object.
	 * @param	jsonData is load map of data.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void read(final Json json, final JsonValue jsonData) {
		// TODO Auto-generated method stub
		resourceDescriptors = json.readValue("resources",Array.class,jsonData);
		shaderDescriptors = json.readValue("shaders",Array.class,jsonData);
		fontDescriptors = json.readValue("fonts",Array.class,jsonData);	
	}
}
