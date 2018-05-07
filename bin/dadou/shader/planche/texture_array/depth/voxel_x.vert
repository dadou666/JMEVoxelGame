
attribute vec3 vertex;
attribute vec3 coordTexture3D;
attribute vec3 normal;
varying vec2 vcoordTexture2D;
uniform float echelle;
uniform float deltat;
 varying float x;
 varying float xp;
 varying  float cf;

  uniform float tpx;
uniform float tpy;
 uniform float tpz;

varying vec3 normals;
varying vec4 position;
varying vec4 projection;
 varying vec2 habTexCoord;
void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*echelle, 1.0);
  projection = gl_Position;
  x =coordTexture3D.x+tpx+deltat/2;
  xp= x-deltat*normal.x;
  habTexCoord = vertex.yz;
normals = normalize(gl_NormalMatrix * normal);

 position =gl_ModelViewMatrix * vec4(vertex*echelle,1.0);
  vec3  v = vec3(position);     
  vcoordTexture2D = vec2(coordTexture3D.y+tpy,coordTexture3D.z+tpz);

  

  
  
}