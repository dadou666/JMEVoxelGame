attribute vec3 vertex;
uniform float dim;

void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(dim*vertex, 1.0);
  
}