
attribute vec3 vertex;
attribute vec3 coordTexture3D;
varying vec2 vcoordTexture2D;
attribute vec3 normal;
uniform float echelle;
varying  float z;
varying  float zp;
varying float cf;

 uniform float tpx;
uniform float tpy;
 uniform float tpz;



uniform float deltat;
varying vec2 habTexCoord;


varying vec3 normals;
varying vec4 position;
varying vec4 projection;

void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*echelle, 1.0);
  projection=gl_Position;
  z =coordTexture3D.z+tpz+deltat/2;
  zp=z-deltat*normal.z;
  normals = normalize(gl_NormalMatrix * normal);
  position =gl_ModelViewMatrix * vec4(vertex*echelle,1.0);
  habTexCoord=vertex.xy;
  vec3  v = vec3(position);    

  vcoordTexture2D =vec2(coordTexture3D.x+tpx,coordTexture3D.y+tpy);

  
}