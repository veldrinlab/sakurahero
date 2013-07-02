package pl.veldrinlab.sakuraEngine.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class TerrainChunk {
	public float[] heightMap;
	public short width;
	public short height;
	public float[] vertices;
	public short[] indices;
	public int vertexSize;

	public TerrainChunk(final int width, final int height, final int vertexSize) {
		this.heightMap = new float[(width + 1) * (height + 1)];
		this.width = (short)width;
		this.height = (short)height;
		this.vertices = new float[heightMap.length * vertexSize];
		this.indices = new short[width * height * 6];
		this.vertexSize = vertexSize;
	}

	public void buildTerrain(final String heightMapPath, final float xScale, final float yScale, final float zScale) {
		buildHeightmap(heightMapPath);
		buildIndices();
		buildVertices(xScale,yScale,zScale);
	}
	
	private void buildHeightmap(final String pathToHeightMap) {
		FileHandle handle = Gdx.files.internal(pathToHeightMap);
		Pixmap heightmapImage = new Pixmap(handle);
		Color color = new Color();
		int idh = 0;

		for (int x = 0; x < this.width + 1; x++) {
			for (int y = 0; y < this.height + 1; y++) {
				Color.rgba8888ToColor(color, heightmapImage.getPixel(x, y));
				this.heightMap[idh++] = (color.r+color.g+color.b)/3.0f;
			}
		}
	}

	private void buildVertices(final float xScale, final float yScale, final float zScale) {
		int heightPitch = height + 1;
		int widthPitch = width + 1;

		int idx = 0;
		int hIdx = 0;
		int inc = vertexSize - 3;
		
		for (int z = 0; z < heightPitch; z++) {
			for (int x = 0; x < widthPitch; x++) {
				vertices[idx++] = x * xScale;
				vertices[idx++] = heightMap[hIdx++] * yScale;
				vertices[idx++] = z * zScale;
				idx += inc;
			}
		}
	}

	private void buildIndices () {
		int idx = 0;
		short pitch = (short)(width + 1);
		short i1 = 0;
		short i2 = 1;
		short i3 = (short)(1 + pitch);
		short i4 = pitch;

		short row = 0;

		for (int z = 0; z < height; z++) {
			for (int x = 0; x < width; x++) {
				indices[idx++] = i1;
				indices[idx++] = i2;
				indices[idx++] = i3;

				indices[idx++] = i3;
				indices[idx++] = i4;
				indices[idx++] = i1;

				i1++;
				i2++;
				i3++;
				i4++;
			}

			row += pitch;
			i1 = row;
			i2 = (short)(row + 1);
			i3 = (short)(i2 + pitch);
			i4 = (short)(row + pitch);
		}
	}
}
