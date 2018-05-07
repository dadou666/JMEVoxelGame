
uniform vec4 color;
varying vec3 vnormal;
varying vec4 position;


void main(void) {


	gl_FragData[0]		= color;
	gl_FragData[1]		= vec4(position.xyz,0);
	gl_FragData[2]		= vec4(vnormal,1.0);
	gl_FragData[3] = vec4(0.0,0.0,0.0,0.0);
	

	
	
	}