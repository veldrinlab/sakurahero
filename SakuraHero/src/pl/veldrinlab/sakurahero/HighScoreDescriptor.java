package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class HighScoreDescriptor implements Serializable {

	public int highScore;
	public float survivalTime;

	@Override
	public void write(Json json) {
		json.writeValue("highScore",highScore);
		json.writeValue("survivalTime",survivalTime);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		highScore = json.readValue("highScore", Integer.class,jsonData);
		survivalTime= json.readValue("survivalTime", Float.class,jsonData);	
	}
}
