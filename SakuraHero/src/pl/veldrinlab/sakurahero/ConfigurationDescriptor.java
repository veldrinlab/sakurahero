package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class ConfigurationDescriptor implements Serializable{

	public int width;
	public int height;
	public boolean useGL20;
	public boolean fullScreenEnabled;
	public String windowTitle;
	public String initResourcePath;
	public String resourcePath;
	
	@Override
	public void write(final Json json) {
		json.writeValue("width",width);
		json.writeValue("height",height);
		json.writeValue("useGL20",useGL20);
		json.writeValue("windowTitle",windowTitle);
		json.writeValue("fullScreenEnabled",fullScreenEnabled);
		json.writeValue("initResourcePath",initResourcePath);
		json.writeValue("resourcePath",resourcePath);		
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		width = json.readValue("width", Integer.class,jsonData);
		height = json.readValue("height", Integer.class,jsonData);
		useGL20 = json.readValue("useGL20", Boolean.class,jsonData);
		fullScreenEnabled = json.readValue("fullScreenEnabled", Boolean.class,jsonData);
		windowTitle = json.readValue("windowTitle", String.class,jsonData);
		resourcePath = json.readValue("resourcePath", String.class,jsonData);
		initResourcePath = json.readValue("initResourcePath", String.class,jsonData);
	}

}
