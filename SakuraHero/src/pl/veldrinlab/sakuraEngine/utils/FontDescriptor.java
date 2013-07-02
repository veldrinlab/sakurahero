package pl.veldrinlab.sakuraEngine.utils;

/**
 * Class represents Bitmapfont resource descriptor. Each Font  is defined by name, path to *fnt file and path to image file.
 * @author Szymon Jab³oñski
 *
 */
public class FontDescriptor {
	public String name;
	public String fntPath;
	public String imagePath;

	/**
	 * Class default constructor.
	 */
	public FontDescriptor() {
		
	}
	
	/**
	 * Class constructor with initialize parameters.
	 * @param n is name of font.
	 * @param fnt is path to fnt file which defines font structure.
	 * @param image is path image file which represents font chars.
	 */
	public FontDescriptor(final String n, final String fnt, final String image) {
		name = n;
		fntPath = fnt;
		imagePath = image;
	}
}
