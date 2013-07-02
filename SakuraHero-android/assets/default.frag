#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec2 v_texCoords;

void main() {
	vec4 color = texture2D(u_texture, v_texCoords);	
	gl_FragColor = color;
	
	gl_FragColor = vec4(1.0,1.0,1.0,1.0);
}
