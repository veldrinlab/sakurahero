package pl.veldrinlab.sakuraEngine.utils;

/**
 * Class represents SakuraEngine resource descriptor. Each simple resource like Texture, Music and Sound is defined by type enum, name and path to resource file.
 * @author Szymon Jab³oñski
 *
 */
public class ResourceDescriptor {
	public ResourceType type;
	public String name;
	public String path;
	
	/**
	 * Class default constructor.
	 */
	public ResourceDescriptor() {
		
	}
	
	/**
	 * Class constructor with initialize parameters.
	 * @param t is resource type.
	 * @param n is resource name.
	 * @param p is path to resource file.
	 */
	public ResourceDescriptor(final ResourceType t, final String n, final String p) {
		
		type = t;
		name = n;
		path = p;
	}
}
