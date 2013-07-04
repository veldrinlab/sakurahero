package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class HighScoreDescriptor implements Serializable {

	public int highScore;
	
	@Override
	public void write(Json json) {
		json.writeValue("highScore",highScore);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		highScore = json.readValue("highScore", Integer.class,jsonData);	
	}
}
