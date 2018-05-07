uniform float echelle;
uniform vec3 dim;
varying vec3 vV;

varying vec4 position;
uniform sampler3D texture;
varying mat3 rotationInverseV;
varying vec3 dep;
varying vec3 normalV;
int testVertex(vec3 v) {
	if (v.x <0.0) {
		return 0;
	}
	
	
	if (v.y < 0.0) {
		return 0;
	}
	if (v.z < 0.0) {
		return 0;
	}
	if (v.x > 1.0) {
		return 0;
	}
	if (v.y > 1.0) {
		return 0;
	}
	if (v.z > 1.0 ) {
		return 0;
	}
	return 1;
	
}


vec4 getColor(vec3 dep,vec3 limit) {
vec3 u=vV/limit;
	vec4 empty = vec4(0,0,0,0);
	if (dep == vec3(0,0,0))  {
	return vec4(0,1,0,1);
	}
	u+=dep;
while (true) {
	vec3 tx= u;
	if (testVertex(tx)==0) {
		return empty;
	}

	vec4 tmp = texture3D(texture,tx);

	if (tmp.xyz != empty.xyz) {
		return tmp;
	}
	u+=dep;
}
return empty;	


}







void main(void) {
	if (dot(normalV,position.xyz) < 0) {
	discard;
	} else {

	
	float m = max(abs(dep.x),max(abs(dep.z),abs(dep.y)));
	dep=echelle*dep/1.5*m;
	vec3 limit = echelle*dim;
	
	vec4 empty = vec4(0,0,0,0);
	vec4 color = getColor(dep/limit,limit);
	if (color.xyz == empty.xyz ) {
		discard;
	} else {
		gl_FragData[0]		= color;
		gl_FragData[1]		= vec4(position.xyz,0);
		gl_FragData[2]		= vec4(-normalize(position.xyz),0);
	}
	
	}
	


	

	
	
	}