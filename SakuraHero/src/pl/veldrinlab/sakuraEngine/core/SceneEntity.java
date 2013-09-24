package pl.veldrinlab.sakuraEngine.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Class represents adapter for libgdx Actor with Sprite data. It can be stored in Stage for proper resolution scaling.
 * @author Szymon Jab³oñski
 *
 */
public class SceneEntity extends Actor {
	
	//TODO torchê du¿o tych danych siê robi - pozycja sprite, actora, jeszcze w³asny vector tutaj jest
	
	
	protected Sprite sprite;
	
	public Vector3 velocity;
	public float rotation;
	public float rotationVelocity;
	public Circle collisionCircle;
	
	// zbêdne?
	public Vector2 position;
	
	/**
	 * Class constructor, creates spirte.
	 * @param texture is sprite texture.
	 */
	public SceneEntity(final Texture texture) {	
		sprite = new Sprite(texture);	
		position = new Vector2();
		velocity = new Vector3();
		collisionCircle = new Circle();
	}
	
	/**
	 * Class constructor, creates SceneEntity from TextureAtlas.
	 * @param sprite is reference to Sprite created from TextureAtlas.
	 */
	public SceneEntity(final Sprite sprite) {	
		this.sprite = sprite;
		position = new Vector2();
		velocity = new Vector3();
		collisionCircle = new Circle();
	}

	/**
	 * Class constructor, creates Sprite with defined name.
	 * @param texture is sprite texture.
	 * @param name is Actor name;
	 */
	public SceneEntity(final Texture texture, final String name) {
		sprite = new Sprite(texture);
		
		setName(name);
		setTouchable(Touchable.enabled);
		setWidth(sprite.getWidth());
		setHeight(sprite.getHeight());
		setBounds(0, 0, getWidth(), getHeight());
	}
	
	/**
	 * Class constructor, creates SceneEntity with defined name.
	 * @param texture is sprite texture.
	 * @param name is Actor name;
	 */
	public SceneEntity(final Sprite sprite, final String name) {
		this.sprite = sprite;
		
		setName(name);
		setTouchable(Touchable.enabled);
		setWidth(sprite.getWidth());
		setHeight(sprite.getHeight());
		setBounds(0, 0, getWidth(), getHeight());
	}
	
	/**
	 * Method is used to draw sprite.
	 * @param	batch is sprite batch.
	 * @param	timeDelta is not used.(and never wiil be)
	 */
	@Override
	public void draw(final SpriteBatch batch, final float timeDelta) {
		sprite.draw(batch);
	}
	
	
	public void changeEntitySprite(final Sprite sprite) {
		this.sprite = sprite;
	}
	
	//TODO paczkê metod zamiast tego
	
	/**
	 * Accessor to actor Sprite data.
	 * @return sprite.
	 */
	public Sprite getSprite() {
		return sprite;
	}
}
