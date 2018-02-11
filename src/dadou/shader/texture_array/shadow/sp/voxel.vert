
attribute vec3 vertex;
attribute vec3 coordTexture3D;
attribute vec3 normal;

varying vec3 vcoordTexture3D;
varying vec3 vcoordTexture3DP;
uniform float echelle;
uniform vec3 deltat;
 varying float u;
 varying float up;
 varying  float cf;

  uniform vec3 tp;
varying vec3 normalCube;

varying vec3 vnormal;
varying vec4 position;

uniform mat4 DepthBiasMVP;
varying vec4 ShadowCoord; 
varying vec2 habTexCoord;

uniform mat3 textureMat;
uniform mat3 textureMatInv;

varying float deltatv;

uniform mat4 modelView;
uniform vec4 lumieres[8];
uniform int nbLumiere;
varying vec4 flumieres[8];

uniform vec4 zones[8];
uniform int nbZone;
varying vec4 fzones[8];



uniform vec3 lightPos; 
varying vec3 vlightPos;
varying vec4 sommet;
uniform mat4 transformationMat;
 
 
void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*echelle, 1.0);
  ShadowCoord = DepthBiasMVP*vec4(vertex*echelle, 1.0);
  sommet = transformationMat*vec4(vertex*echelle, 1.0); 
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
  
    vlightPos =(modelView*vec4(lightPos,1.0)).xyz;
for(int k=0;k <nbLumiere;k++) {
vec4 lumiere = lumieres[k];
flumieres[k]=vec4((modelView*vec4(lumiere.xyz,1.0)).xyz,lumiere.w);
}

for(int k=0;k <nbZone;k++) {
vec4 zone = zones[k];
fzones[k]=vec4((modelView*vec4(zone.xyz,1.0)).xyz,zone.w);
}

vnormal = normalize(gl_NormalMatrix * normal);

 position =gl_ModelViewMatrix * vec4(vertex*echelle,1.0);


 vcoordTexture3D = textureMatInv*vec3(tmpCoordTexture3D.x+tmpTp.x,tmpCoordTexture3D.y+tmpTp.y,u);
 vcoordTexture3DP = textureMatInv*vec3(tmpCoordTexture3D.x+tmpTp.x,tmpCoordTexture3D.y+tmpTp.y,up);
  


  
  
}