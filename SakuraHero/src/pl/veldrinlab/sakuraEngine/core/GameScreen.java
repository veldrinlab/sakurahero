package pl.veldrinlab.sakuraEngine.core;

import com.badlogic.gdx.Screen;

/**
 * Class represents abstact game scren - whole Android application is state machine of screens. Screen can be Menu, Splash, Level view etc.
 * @author Szymon Jab³oñski
 *
 */
public abstract class GameScreen implements Screen {
	
	public abstract void processInput();
	public abstract void processLogic(final float deltaTime);
	public abstract void processRendering();
}
