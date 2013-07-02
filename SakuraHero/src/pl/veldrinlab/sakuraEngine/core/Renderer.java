package pl.veldrinlab.sakuraEngine.core;

//TODO to powinno byæ w silniku zdecydowanie! - i konfiguracja odgórna podstawowych danych
import pl.veldrinlab.sakurahero.Configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Class represents Core element of SakuraEngine - pure static Renderer class. It is collection of default, global rendering data like camera, sprite batch, font
 * and matrices. It also store couple of usefull rendering methods which use OpenGL API.
 * @author Szymon Jab³oñski
 *
 */
public class Renderer {
	//TODO make Renderer3D and Renderer2D - pomyœleæ nad tym
	
	public static PerspectiveCamera camera = new PerspectiveCamera(60, Configuration.getInstance().width, Configuration.getInstance().height); 
	public static SpriteBatch defaultBatch = new SpriteBatch();
	public static Stage defaultStage = new Stage(Configuration.getInstance().width, Configuration.getInstance().height,false,defaultBatch);
	public static BitmapFont defaultFont = null;
	public static BitmapFont defaultFontSmall = null;
	public static ShaderProgram defaultShader = null;
	
	public static Vector3 axisY = new Vector3(0.0f, 1.0f, 0.0f);
	public static Vector3 axisX = new Vector3(1.0f, 0.0f, 0.0f);
	public static Vector3 axisZ = new Vector3(0.0f, 0.0f, 1.0f);
	
	public static Matrix4 modelViewProjectionMatrix = new Matrix4();
	public static Matrix4 modelViewMatrix = new Matrix4();
	
	public static void clearScreen() {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public static void clearScreen(final Color color) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glClearColor(color.r,color.g,color.b,color.a);
	}
	
	public static void initPerspectiveMode() {
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_BACK);
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public static void enterOrthoMode() {
		Gdx.gl20.glDisable(GL20.GL_TEXTURE_2D);
		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST); 
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
	}
	
	public static void leaveOrthoMode() {
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST); 
		Gdx.gl20.glCullFace(GL20.GL_BACK);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
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
	
	public static void renderEntity(final SceneEntity entity) {
		entity.shader.begin();
		modelViewProjectionMatrix.set(camera.projection).mul(camera.view).mul(entity.modelMatrix);
		modelViewMatrix.set(camera.view).mul(entity.modelMatrix);
		entity.shader.setUniformMatrix("u_mvpMatrix",modelViewProjectionMatrix);
		entity.shader.setUniformMatrix("u_mvMatrix",modelViewMatrix);
		entity.shader.setUniformMatrix("u_vMatrix",camera.view);
		entity.shader.setUniformf("scale", entity.scale.x,entity.scale.y,entity.scale.z);
		entity.shader.setUniformf("alpha", entity.material.alpha);
		entity.texture.bind();
		entity.mesh.render(entity.shader, GL20.GL_TRIANGLES);
		entity.shader.end();
	}
	
	public static void renderEntity(final SceneEntity entity, final Mesh subMesh) {
		entity.shader.begin();
		modelViewProjectionMatrix.set(camera.projection).mul(camera.view).mul(entity.modelMatrix);
		modelViewMatrix.set(camera.view).mul(entity.modelMatrix);
		entity.shader.setUniformMatrix("u_mvpMatrix",modelViewProjectionMatrix);
		entity.shader.setUniformMatrix("u_mvMatrix",modelViewMatrix);
		entity.shader.setUniformMatrix("u_vMatrix",camera.view);
		entity.shader.setUniformf("scale", entity.scale.x,entity.scale.y,entity.scale.z);
		entity.shader.setUniformf("alpha", entity.material.alpha);
		entity.texture.bind();
		subMesh.render(entity.shader, GL20.GL_TRIANGLES);
		entity.shader.end();
	}
	
	public static void renderEntity(final SceneEntity entity, final Camera cam) {			
		entity.shader.begin();
		modelViewProjectionMatrix.set(cam.projection).mul(cam.view).mul(entity.modelMatrix);
		modelViewMatrix.set(camera.view).mul(entity.modelMatrix);
		entity.shader.setUniformMatrix("u_mvpMatrix", modelViewProjectionMatrix);
		entity.shader.setUniformMatrix("u_mvMatrix",modelViewMatrix);
		entity.shader.setUniformMatrix("u_vMatrix",camera.view);	
		entity.shader.setUniformf("alpha", entity.material.alpha);
		entity.texture.bind();
		entity.mesh.render(entity.shader, GL20.GL_TRIANGLES);
		entity.shader.end();
	}
}
