attribute vec3 vertex;
attribute vec2 coordTexture2D;
attribute vec3 normal;
varying vec3 vNormal;
varying vec2 vcoordTexture2D;
varying float cf;
varying float fogFactor;
varying vec4 position;

void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex, 1.0);
   position =gl_ModelViewMatrix * vec4(vertex,1.0);
vcoordTexture2D= coordTexture2D;

}