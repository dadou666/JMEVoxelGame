
attribute vec3 vertex;
attribute vec3 coordTexture3D;
varying vec3 vcoordTexture3D;
varying vec3 vcoordTexture3DP;
attribute vec3 normal;
uniform float echelle;
varying  float u;
varying  float up;
varying float cf;
varying float deltatv;
 uniform vec3 tp;


uniform mat3 textureMat;
uniform mat3 textureMatInv;

uniform vec3 deltat;
varying vec2 habTexCoord;


varying vec3 normals;
varying vec4 position;

uniform mat4 DepthBiasMVP;
varying vec4 ShadowCoord; 

uniform mat4 modelView;
uniform vec4 lumieres[8];
uniform int nbLumiere;

uniform vec4 zones[8];
uniform int nbZone;

uniform vec3 lightPos; 
varying vec3 vlightPos;
varying vec3 normalCube;

void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*echelle, 1.0);
  ShadowCoord = DepthBiasMVP*vec4(vertex*echelle, 1.0);
  vec3 tmpVertex = textureMat*vertex;
  vec3 tmpCoordTexture3D =textureMat*coordTexture3D;
  vec3 tmpTp=textureMat*tp;
  vec3 tmpDeltat =textureMat*deltat;
  vec3 tmpNormal=textureMat*normal;
  normalCube= normal;
  
  habTexCoord = tmpVertex.xy;
  deltatv=tmpDeltat.z;
  u =tmpCoordTexture3D.z+tmpTp.z+tmpDeltat.z/2;
  up= u-tmpDeltat.z*tmpNormal.z;
  
  
  normals = normalize(gl_NormalMatrix * normal);
  position =gl_ModelViewMatrix * vec4(vertex*echelle,1.0);
  
  vec3  v = vec3(position);    

 vcoordTexture3D = textureMatInv*vec3(tmpCoordTexture3D.x+tmpTp.x,tmpCoordTexture3D.y+tmpTp.y,u);
 vcoordTexture3DP = textureMatInv*vec3(tmpCoordTexture3D.x+tmpTp.x,tmpCoordTexture3D.y+tmpTp.y,up);

  
  
}