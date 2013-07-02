package pl.veldrinlab.sakuraEngine.fx;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Class represents abstract particle emiter. It is base clsss of all 
 * @author Szymon Jab³oñski
 *
 */
public abstract class ParticleEmiter {
	protected Array<Particle> particles;
	protected int particleAmount;
	protected float effectTimer;
	protected Vector3 origin;
	
	/**
	 * Class constructor. Create memory for particle data,
	 * @param particleAmount
	 * @param origin
	 */
	public ParticleEmiter(final int particleAmount, final Vector3 origin) {
		this.particleAmount = particleAmount;
		this.origin = origin;
		particles = new Array<Particle>();
	}
	
	/**
	 * Method is used to set particle ffecy time.
	 * @param time is particle ffect time in seconds. 
	 */
	public void setEffectTime(final float time) {
		effectTimer = time;
	}
	
	public abstract void updateEmiter(final float deltaTime);
	public abstract void renderEmiter();
}
