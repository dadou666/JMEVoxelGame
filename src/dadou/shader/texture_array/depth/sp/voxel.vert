
attribute vec3 vertex;
attribute vec3 coordTexture3D;
attribute vec3 normal;

uniform float echelle;
uniform vec3 deltat;
uniform vec3 tp;
uniform mat3 textureMat;
uniform mat3 textureMatInv;
uniform mat4 transformationMat;

varying vec2 a_vcoordTexture2D;
varying vec3 a_normalCube;
varying float a_u;
varying float a_up;
varying float a_deltatv;
varying vec2 a_habTexCoord;
varying vec3 a_vcoordTexture3D;
varying vec3 a_vcoordTexture3DP;
varying vec3 a_vnormal;
varying vec4 a_position;
varying vec4 a_projection;
varying vec4 sommet;

void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*echelle, 1.0);
  a_projection=gl_Position;
  sommet=transformationMat*vec4(vertex*echelle, 1.0);
  vec3 tmpVertex = textureMat*vertex;
  vec3 tmpCoordTexture3D =textureMat*coordTexture3D;
  vec3 tmpTp=textureMat*tp;
  vec3 tmpDeltat =textureMat*deltat;
  vec3 tmpNormal=textureMat*normal;
  a_normalCube = normal;
  a_habTexCoord = tmpVertex.xy;
  a_deltatv=tmpDeltat.z;
  a_u =tmpCoordTexture3D.z+tmpTp.z+tmpDeltat.z/2;
  a_up= a_u-tmpDeltat.z*tmpNormal.z;
   

 	a_vcoordTexture3D = textureMatInv*vec3(tmpCoordTexture3D.x+tmpTp.x,tmpCoordTexture3D.y+tmpTp.y,a_u);
 a_vcoordTexture3DP = textureMatInv*vec3(tmpCoordTexture3D.x+tmpTp.x,tmpCoordTexture3D.y+tmpTp.y,a_up);

  
}