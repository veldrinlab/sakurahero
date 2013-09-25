package pl.veldrinlab.sakuraEngine.core;

import pl.veldrinlab.sakurahero.Configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Renderer {
	
	public static SpriteBatch backgroundBatch = new SpriteBatch();
	public static Stage backgroundStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,backgroundBatch);
	
	public static SpriteBatch sceneBatch = new SpriteBatch();
	public static Stage sceneStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,sceneBatch);
	
	public static SpriteBatch hudBatch = new SpriteBatch();
	public static Stage hudStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,hudBatch);
		
	public static TextureAtlas introAtlas;
	public static TextureAtlas guiAtlas;
	public static TextureAtlas sceneAtlas;
	public static TextureAtlas hudAtlas;
	
	public static LabelStyle smallFont;
	public static LabelStyle standardFont;
	public static LabelStyle specialFont;
	
	public static void clearScreen() {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public static void clearScreen(final Color color) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glClearColor(color.r,color.g,color.b,color.a);
	}
	
	public static void enableBlending() {
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);	
	}
	
	public static void enableAlphaBlending() {
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);	
	}
	
	public static void disableBlending() {
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
}
