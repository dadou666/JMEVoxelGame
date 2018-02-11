attribute vec3 vertex;
attribute vec3 normal;
uniform float echelle;
varying vec3 normalV;


varying vec4 position;
uniform mat3 rotationInverse;
varying mat3 rotationInverseV;
varying  vec3 vV;

varying vec3 dep;
void main(void) {
 vec4 p = vec4(vertex*echelle, 1.0);
 vV=p.xyz;
  gl_Position =  gl_ModelViewProjectionMatrix*p;
normalV = gl_NormalMatrix*normal;
 position =gl_ModelViewMatrix * p;
 dep=normalize(rotationInverse*position.xyz);


}