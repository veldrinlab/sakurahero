package pl.veldrinlab.sakurahero;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class GameResult implements Serializable {
	public int highScore;
	public float timeRecord;
	
	public int score;
	public float time;
	
	public GameResult() {
		score = highScore = 0;
		time = timeRecord = 0.0f;
	}

	@Override
	public void write(final Json json) {
		json.writeValue("highScore",highScore);
		json.writeValue("timeRecord",timeRecord);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		highScore = json.readValue("highScore", Integer.class,jsonData);
		timeRecord= json.readValue("timeRecord", Float.class,jsonData);	
	}
}
