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
public class SpriteActor extends Actor {
	
	//TODO torchê du¿o tych danych siê robi - pozycja sprite, actora, jeszcze w³asny vector tutaj jest
	
	
	protected Sprite sprite;
	
	public Vector3 velocity;
	public float rotation;
	public float rotationVelocity;
	public Circle collisionCircle;
	
	//
	public Vector2 position;
	
	/**
	 * Class constructor, creates spirte.
	 * @param splashTexture is sprite texture.
	 */
	public SpriteActor(final Texture splashTexture) {	
		sprite = new Sprite(splashTexture);	
		position = new Vector2();
		velocity = new Vector3();
		collisionCircle = new Circle();
	}

	/**
	 * Class constructor, creates spirte with defined name.
	 * @param splashTexture is sprite texture.
	 * @param name is Actor name;
	 */
	public SpriteActor(final Texture splashTexture, final String name) {
		sprite = new Sprite(splashTexture);
		
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
	
	/**
	 * Accessor to actor Sprite data.
	 * @return sprite.
	 */
	public Sprite getSprite() {
		return sprite;
	}
}
