package pl.veldrinlab.sakuraEngine.utils;

/**
 * Class represents ShaderProgram resources descriptor. Each shader is defined by name, path to vertex program and path to fragment program.
 * @author Szymon Jab³oñski
 *
 */
public class ShaderDescriptor {
	public String name;
	public String vertPath;
	public String fragPath;
	
	/**
	 * Class default constructor.
	 */
	public ShaderDescriptor() {
		
	}
	
	/**
	 * Class constructor with initialize parameters.
	 * @param n is shader program name.
	 * @param vert is path to vertex program file.
	 * @param frag is path to fragment program file.
	 */
	public ShaderDescriptor(final String n, final String vert, final String frag) {
		name = n;
		vertPath = vert;
		fragPath = frag;
	}
}
