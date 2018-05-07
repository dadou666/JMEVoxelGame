
varying vec2 vcoordTexture2D;

varying float fogFactor;


uniform	float fogDensity;
uniform vec3 fogColor;
varying vec4 position;


uniform sampler2D skybox;




void main(void) {
	vec4 cl=texture2D(skybox, vcoordTexture2D);
	gl_FragData[0]		=vec4((cl.xyz),0.0);
		gl_FragData[1]		=vec4(0,length(position.xyz),0,1.0);
	gl_FragDepth=1.0f;
	
	}