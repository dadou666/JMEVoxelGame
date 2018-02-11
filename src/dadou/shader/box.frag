uniform float size;

uniform vec4 color;
uniform float dx;
uniform float dy;
uniform float dz;
varying vec3 vvertexD; 
varying vec3 vnormal;
varying float cf;
void main(void) {
float h=0.05;

	
	float adx=abs(dx);
	float ady=abs(dy);
	float adz=abs(dz);
	float fx= abs(vvertexD.x);
	float fy= abs(vvertexD.y);
	float fz= abs(vvertexD.z);
	

	
	if ( ((fx >=h && fx <=adx-h) || abs(vnormal.x)>0) && ((fy >=h && fy <=ady-h) || abs(vnormal.y)>0) && ((fz >=h && fz <=adz-h) || abs(vnormal.z) > 0 )){
	 
	discard;
	} else {
    vec4 u=color;//*0.8*cf;
    
	gl_FragColor =vec4(vnormal.x*vnormal.x,vnormal.y*vnormal.y,vnormal.z*vnormal.z,1.0); 
	
	}

	
	
	}