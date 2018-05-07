
attribute vec3 vertex;
attribute vec3 coordTexture3D;
varying vec2 vcoordTexture2D;
attribute vec3 normal;
uniform float echelle;
varying  float u;
varying  float up;
varying float cf;
uniform vec3 deltat;
 uniform vec3 tp;


uniform mat3 textureMat;
uniform mat3 textureMatInv;

varying float deltatv;
varying vec2 habTexCoord;
varying vec3 vcoordTexture3D;
varying vec3 vcoordTexture3DP;

varying vec3 vnormal;
varying vec3 normalCube;
varying vec4 position;
varying vec4 projection;

void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*echelle, 1.0);
  projection=gl_Position;
  vec3 tmpVertex = textureMat*vertex;
  vec3 tmpCoordTexture3D =textureMat*coordTexture3D;
  vec3 tmpTp=textureMat*tp;
  vec3 tmpDeltat =textureMat*deltat;
  vec3 tmpNormal=textureMat*normal;
  normalCube=normal;
  habTexCoord = tmpVertex.xy;
  deltatv=tmpDeltat.z;
  u =tmpCoordTexture3D.z+tmpTp.z+tmpDeltat.z/2;
  up= u-tmpDeltat.z*tmpNormal.z;
   

 vcoordTexture3D = textureMatInv*vec3(tmpCoordTexture3D.x+tmpTp.x,tmpCoordTexture3D.y+tmpTp.y,u);
 vcoordTexture3DP = textureMatInv*vec3(tmpCoordTexture3D.x+tmpTp.x,tmpCoordTexture3D.y+tmpTp.y,up);

  
}