uniform float size;
varying vec2 vcoordTexture2D;
uniform vec4 color;
varying float fogFactor;
varying float cf;
varying vec3 vNormal;
varying vec4 projection;
varying vec4 position;
void main(void) {
float h=0.01;

	float fx= vcoordTexture2D.x*size;
	float fy= vcoordTexture2D.y*size;
	float dx = abs(int(fx)-fx);
	float dy = abs(int(fy)-fy);
	vec4 cl;
	if ( dx >=h && dx <=1-h && dy >=h && dy <=1-h){ 
cl= mix(gl_Fog.color, color*cf, fogFactor );
	
	} else {
	cl= mix(gl_Fog.color, color*cf*0.8, fogFactor );
	}
	gl_FragData[0]	 = projection/projection.w;
	
	
	}