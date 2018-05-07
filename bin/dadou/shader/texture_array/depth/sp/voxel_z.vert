
attribute vec3 vertex;
attribute vec3 coordTexture3D;
varying vec2 vcoordTexture2D;
attribute vec3 normal;

attribute vec3 vertex1;
attribute vec3 vertex2;
attribute vec3 vertex3;

varying vec4 vvertex1;
varying vec4 vvertex2;
varying vec4 vvertex3;


uniform float echelle;
varying  float z;
varying  float zp;
varying float cf;

 uniform float tpx;
uniform float tpy;
 uniform float tpz;



uniform float deltat;
varying vec2 habTexCoord;


varying vec3 vnormal;
varying vec4 position;
varying vec4 projection;

void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*echelle, 1.0);
  projection=gl_Position;
        vvertex1=gl_ModelViewProjectionMatrix*vec4(vertex1*echelle, 1.0);
  vvertex2=gl_ModelViewProjectionMatrix*vec4(vertex2*echelle, 1.0);
  vvertex3=gl_ModelViewProjectionMatrix*vec4(vertex3*echelle, 1.0);
  z =coordTexture3D.z+tpz+deltat/2;
  zp=z-deltat*normal.z;
 
  habTexCoord=vertex.xy;
   

  vcoordTexture2D =vec2(coordTexture3D.x+tpx,coordTexture3D.y+tpy);

  
}