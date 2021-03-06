
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
attribute vec3 vertex1;
attribute vec3 vertex2;
attribute vec3 vertex3;

varying vec4 vvertex1;
varying vec4 vvertex2;
varying vec4 vvertex3;

varying vec4 projection;
 varying vec2 habTexCoord;
void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*echelle, 1.0);
  projection = gl_Position;
    vvertex1=gl_ModelViewProjectionMatrix*vec4(vertex1*echelle, 1.0);
  vvertex2=gl_ModelViewProjectionMatrix*vec4(vertex2*echelle, 1.0);
  vvertex3=gl_ModelViewProjectionMatrix*vec4(vertex3*echelle, 1.0);
  
  x =coordTexture3D.x+tpx+deltat/2;
  xp= x-deltat*normal.x;
  habTexCoord = vertex.yz;


   
  vcoordTexture2D = vec2(coordTexture3D.y+tpy,coordTexture3D.z+tpz);

  

  
  
}