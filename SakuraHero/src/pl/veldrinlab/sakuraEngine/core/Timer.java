package pl.veldrinlab.sakuraEngine.core;

/**
 * Class represents Sakura Engine Timer which is used to control game time loop. Stor defined time step, update methods and simple profiling tools.
 * @author Szymon Jab³oñski
 *
 */
public class Timer {

	public final static float TIME_STEP = 1.0f/60.0f;
	
	private float timeAccumulator;
	private float framesAccumulator;
	private int framesCounter;
	
	/**
	 * Class constructor.
	 */
	public Timer() {
	}
	
	/**
	 * Method is used to update timer.
	 * @param deltaTime is time step.
	 */
	public void updateTimer(final float deltaTime) {	
		timeAccumulator += deltaTime;
		framesAccumulator += deltaTime;
		framesCounter++;
		
		if(framesAccumulator > 1.0f) {
			framesCounter = 0;
			framesAccumulator = 0.0f;
		}
	}
	
	/**
	 * Method is used to check if another update by time step can be done.
	 * @return true if there is enough time in accumulator.
	 */
	public boolean checkTimerAccumulator() {
		return timeAccumulator > TIME_STEP;
	}
	
	/**
	 * Method is used to eat time step from time accumulator.
	 */
	public void eatAccumulatorTime() {
		timeAccumulator -= TIME_STEP;
	}
	
	/**
	 * Accessor to current FPS data
	 * @return current fps.
	 */
	public float getFramePerSecond() {
		return framesCounter;
	}
	
	/**
	 * Method is used to reset timer.
	 */
	public void reset() {
		timeAccumulator = 0.0f;
	}	
}
