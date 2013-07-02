#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform float v;

void main() {

	vec4 color = v_color * texture2D(u_texture, v_texCoords);
	
    gl_FragColor = color;
	
	int id = v_texCoords.y*10.0f*v;
	
	if(mod(id,2) == 0)
		gl_FragColor = vec4(0.0,0.0,0.0,1.0);
}
