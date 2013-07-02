package pl.veldrinlab.sakuraEngine.fx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

/**
 * Class represents data of Particle. Particles are used in particle effects which will help to achieve special effects.
 * @author Szymon Jab³oñski
 *
 */
public class Particle {
	public Vector3 position;
	public Vector3 rotation;
	public Vector3 scale;
	public Vector3 velocity;
	public Color color;
	public float lifeTime;
	
	//
	public float angle;
	public float rotationVelocity;
	
	public Vector3 delta;
	
	/**
	 * CLass default constructor.
	 */
	public Particle() {
		position = new Vector3();
		rotation = new Vector3();
		scale = new Vector3();
		velocity = new Vector3();
		color = new Color();
		
		//
		delta = new Vector3();
	}
}
