package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class SakuraTreeDescriptor implements Serializable {
	
	public Array<SakuraLeafDescriptor> leaves;
	
	public SakuraTreeDescriptor () {
		leaves = new Array<SakuraLeafDescriptor>();
	}
	
	@Override
	public void write(final Json json) {
		json.writeValue("leaves",leaves);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(final Json json, final JsonValue jsonData) {
		leaves = json.readValue("leaves", Array.class,jsonData);
	}
}
