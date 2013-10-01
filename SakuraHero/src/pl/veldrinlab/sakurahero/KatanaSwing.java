package pl.veldrinlab.sakurahero;

import pl.veldrinlab.sakuraEngine.core.Timer;
import pl.veldrinlab.sakuraEngine.utils.Stack;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class KatanaSwing {

	private ImmediateModeRenderer20 renderer;
	private Texture texture;
	
	private Array<Vector2> texcoord;
	private Array<Vector2> tristrip;
	private Vector2 perp;
	private Color color;
	private int batchSize;
	private float thickness;
	private float endcap;

	private float time;
	private Stack<Vector2> input;
	private Vector2 lastPoint;
	
	private int level;
	
	private float slashTimer;
	
	public KatanaSwing(final Texture katanaTexture) {
		this.texture = katanaTexture;
		
		renderer = new ImmediateModeRenderer20(false, true, 1);
		
		texcoord = new Array<Vector2>();
		tristrip = new Array<Vector2>();
		perp = new Vector2();
		color = new Color(Color.WHITE);
		
		input = new Stack<Vector2>(300,Vector2.class);
		lastPoint = new Vector2();
		
		thickness = 30f;
		endcap = 1.5f;
	}
	
	public void draw(final Camera cam) {
		if (tristrip.size <= 0)
			return;

		texture.bind();
		
		renderer.begin(cam.combined, GL20.GL_TRIANGLE_STRIP);
		for(int i = 0; i  <tristrip.size; ++i) {
			if(i == batchSize) {
				renderer.end();
				renderer.begin(cam.combined, GL20.GL_TRIANGLE_STRIP);
			}	
			
			Vector2 point = tristrip.get(i);
			Vector2 tc = texcoord.get(i);
			renderer.color(color.r, color.g, color.b, color.a);
			renderer.texCoord(tc.x, 0f);
			renderer.vertex(point.x, point.y, 0f);
		}
		renderer.end();
	}

	private int generate(Array<Vector2> input, int mult) {
		int c = tristrip.size;
		
		if (endcap <= 0)
			tristrip.add(input.get(0));
		else {
			Vector2 p = input.get(0);
			Vector2 p2 = input.get(1);
			perp.set(p).sub(p2).scl(endcap);
			tristrip.add(new Vector2(p.x+perp.x, p.y+perp.y));
		}
		
		texcoord.add(new Vector2(0f, 0f));
		
		for(int i = 1; i < input.size-1; ++i) {
			Vector2 p = input.get(i);
			Vector2 p2 = input.get(i+1);
			
			perp.set(p).sub(p2).nor();
			perp.set(-perp.y, perp.x);
			
			float thick = thickness * (1f-((i)/(float)(input.size)));
		
			perp.scl(thick/2f);
			perp.scl(mult);
			tristrip.add(new Vector2(p.x+perp.x, p.y+perp.y));
			texcoord.add(new Vector2(0f, 0f));
			tristrip.add(new Vector2(p.x, p.y));
			texcoord.add(new Vector2(1f, 0f));
		}
		
		if(endcap <= 0)
			tristrip.add(input.get(input.size-1));
		else {
			Vector2 p = input.get(input.size-2);
			Vector2 p2 = input.get(input.size-1);
			perp.set(p2).sub(p).scl(endcap);
			tristrip.add(new Vector2(p2.x+perp.x, p2.y+perp.y));
		}
		
		texcoord.add(new Vector2(0f, 0f));
		return tristrip.size-c;
	}
	
	public void update(final float deltaTime, final int katanaLevel) {
		level = katanaLevel;
		tristrip.clear();
		texcoord.clear();
		
		if(input.size < 2)
			return;
		
		batchSize = generate(input, 1);
		generate(input, -1);
		
		time += deltaTime;
		slashTimer += deltaTime;
				
		if(input.size > 2 && time > Timer.TIME_STEP*(level+2)) {
			input.pop();
			input.pop();
			time = 0.0f;
		}	
	}
	
	public void addPoint(final Vector2 point) {
		
		if(slashTimer > 0.2f + (level+1)*0.05f)
			return;
		
		final float minDistance = 5.0f;
		final float maxLength = 500.0f*(level+1);

		float distance = 0.0f;
		float swingLength = 0.0f;

		for(int i = 0; i < input.size-1; ++i)
			swingLength += input.get(i).dst(input.get(i+1));

		distance = point.dst(lastPoint.x, lastPoint.y);

		if(input.size < 300 && swingLength < maxLength &&  distance > minDistance) {
			input.insert(point);
			lastPoint.set(point.x, point.y);
		}
	}
	
	public void clear() {
		input.clear();
		slashTimer = 0.0f;
	}
	
	public boolean isReadyToCut() {
		return input.size > 3;
	}
	
	public Stack<Vector2> getInput() {
		return input;
	}
}
