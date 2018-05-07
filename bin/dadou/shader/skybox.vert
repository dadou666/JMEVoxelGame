attribute vec3 vertex;
attribute vec2 coordTexture2D;
varying vec2 vcoordTexture2D;
varying float fogFactor;
varying vec4 position;
uniform float dim;

void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*dim, 1.0);
   position =gl_ModelViewMatrix * vec4(vertex*dim,1.0);
vcoordTexture2D= coordTexture2D;

}