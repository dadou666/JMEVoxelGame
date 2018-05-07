attribute vec3 vertex;
attribute vec3 normal;

varying vec3 vnormal;
varying vec4 position;

void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex, 1.0);

vnormal = normalize(gl_NormalMatrix * normal);
 position =gl_ModelViewMatrix * vec4(vertex,1.0);
}