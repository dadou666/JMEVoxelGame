uniform float size;
varying vec2 vcoordTexture2D;
uniform vec4 color;
varying float fogFactor;
varying float cf;
varying vec3 vNormal;

varying vec4 position;
uniform	float fogDensity;
uniform vec3 fogColor;
vec3 brouillard(vec3 color) {
  

  	
  
     	 float LOG2 = 1.442695;
	float l = length(position.xyz);
	float fogFactor = exp2( -fogDensity * 
					   fogDensity * 
					   l * 
					   l * 
					   LOG2 );
	fogFactor = clamp(fogFactor, 0.0, 1.0);
    return  mix(fogColor,color, fogFactor );  
}



void main(void) {
float h=0.01;

	float fx= vcoordTexture2D.x*size;
	float fy= vcoordTexture2D.y*size;
	float dx = abs(int(fx)-fx);
	float dy = abs(int(fy)-fy);
	vec4 cl;
	if ( dx >=h && dx <=1-h && dy >=h && dy <=1-h){ 
cl=color;
	
	} else {
	cl= color*0.8;
	}
	gl_FragData[0]		= vec4(brouillard(cl.xyz),1.0);
	gl_FragDepth=1.0f;
	
	}