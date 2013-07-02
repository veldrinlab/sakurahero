package pl.veldrinlab.sakuraEngine.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * Class represents SceneEntity material. Store texture data and light properties.
 * @author Szymon Jab³oñski
 *
 */
public class Material {
	public Texture texture;
	public Color ambient;
	public Color diffuse;
	public Color specular;
	public float shininess;
	public float alpha;
}
