package pl.veldrinlab.sakuraEngine.core;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Class represents adapter for libgdx Actor with Sprite data. It can be stored in Stage for proper resolution scaling.
 * @author Szymon Jab³oñski
 *
 */
public class SceneEntity extends Actor {
	
	public Sprite sprite;
	
	public Vector2 velocity;
	public Vector2 position;
	public float rotation;
	public float rotationVelocity;
	public Circle collisionCircle;

	public int width;
	public int height;
	public int originX;
	public int originY;
	

	/**
	 * Class constructor, creates spirte.
	 * @param texture is sprite texture.
	 */
	public SceneEntity(final Texture texture) {	
		sprite = new Sprite(texture);	
		velocity = new Vector2();
		position = new Vector2();
		collisionCircle = new Circle();
	}
	
	/**
	 * Class constructor, creates SceneEntity from TextureAtlas.
	 * @param sprite is reference to Sprite created from TextureAtlas.
	 */
	public SceneEntity(final Sprite sprite) {	
		this.sprite = sprite;
		this.width = (int) sprite.getWidth();
		this.height = (int) sprite.getHeight();
		
		velocity = new Vector2();
		position = new Vector2();
		collisionCircle = new Circle();
			
		setWidth(sprite.getWidth());
		setHeight(sprite.getHeight());
		sprite.setSize(width, height);
	}
	
	/**
	 * Class constructor, creates SceneEntity from TextureAtlas.
	 * @param sprite is reference to Sprite created from TextureAtlas.
	 * @param width is sprite width.
	 * @param height is sprite height.
	 */
	public SceneEntity(final Sprite sprite,final int width, final int height) {	
		this.sprite = sprite;
		this.width = width;
		this.height = height;
	
		velocity = new Vector2();
		position = new Vector2();
		collisionCircle = new Circle();
		
		originX = sprite.getRegionX();
		originY = sprite.getRegionY();
		
		setVerticalRegion(0);
		sprite.setSize(width, height);
		sprite.setOrigin(width/2, height/2);		
	}

	/**
	 * Class constructor, creates Sprite with defined name.
	 * @param texture is sprite texture.
	 * @param name is Actor name;
	 */
	public SceneEntity(final Texture texture, final String name) {
		sprite = new Sprite(texture);
		velocity = new Vector2();
		position = new Vector2();
		collisionCircle = new Circle();
		
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
		this.width = (int) sprite.getWidth();
		this.height = (int) sprite.getHeight();
		
		velocity = new Vector2();
		position = new Vector2();
		collisionCircle = new Circle();
		
		originX = sprite.getRegionX();
		originY = sprite.getRegionY();
		
		setName(name);
		setTouchable(Touchable.enabled);
		setWidth(sprite.getWidth());
		setHeight(sprite.getHeight());
		setBounds(sprite.getRegionX(), sprite.getRegionY(), getWidth(), getHeight());
	}
	
	/**
	 * Class constructor, creates SceneEntity with defined name.
	 * @param texture is sprite texture.
	 * @param name is Actor name;
	 */
	public SceneEntity(final Sprite sprite, final String name, final int width, final int height) {
		this.sprite = sprite;
		this.width = width;
		this.height = height;
		
		velocity = new Vector2();
		position = new Vector2();
		collisionCircle = new Circle();
		
		originX = sprite.getRegionX();
		originY = sprite.getRegionY();
	
		sprite.setSize(width, height);
		
		setName(name);
		setTouchable(Touchable.enabled);
		setWidth(sprite.getWidth());
		setHeight(sprite.getHeight());
		setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
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
		
	public void updateEntityState(final float x, final float y) {
		position.set(x, y);
		setPosition(x, y);
		sprite.setPosition(x, y);
		sprite.setRotation(rotation);
		collisionCircle.set(x+width*0.5f, y+height*0.5f, width*0.5f);
	}
	
	public void setEntityAlpha(final float alpha) {
		sprite.setColor(1.0f, 1.0f, 1.0f, alpha);
	}
	
	public void changeEntitySprite(final Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void alignRelative(final float x, final float y) {	
		sprite.setX((Configuration.getWidth()-sprite.getWidth())*x);	
		sprite.setY(Configuration.getHeight()*y - sprite.getHeight());
		
		position.x = sprite.getX();
		position.y = sprite.getY();
	}
	
	public void alignCenter(final float y) {
		alignRelative(0.5f,y);
	}
	
	public void updateBounds() {
		setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
	}
	
	public void setVerticalRegion(int regionX) {
		sprite.setRegion(originX+regionX, originY, width,height);	
	}
}
