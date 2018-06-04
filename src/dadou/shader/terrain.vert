attribute vec3 vertex;
attribute vec3 normal;

uniform float hMax;
varying vec3 vnormal;
varying vec4 position;
uniform float a;
uniform float m;
uniform  vec3 color;
varying  vec3 vcolor;


void main(void) {
	vec4 v=vec4(vertex.x,vertex.y-m,vertex.z,1.0);


   gl_Position =  gl_ModelViewProjectionMatrix*v;

  vnormal = normalize(gl_NormalMatrix * normal);
  position =gl_ModelViewMatrix * v;
  float h = (( vertex.y/hMax )-1.0) *a+1.0;
  vcolor = h*color;
}
