
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

varying vec3 vnormal;
varying vec4 position;

uniform mat4 DepthBiasMVP;
varying vec4 ShadowCoord; 
varying vec2 habTexCoord;
uniform mat4 modelView;
uniform vec4 lumieres[8];
uniform int nbLumiere;
uniform vec3 lightPos; 
varying vec3 vlightPos;
varying  vec4 flumieres[8];


 
void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*echelle, 1.0);
  ShadowCoord = DepthBiasMVP*vec4(vertex*echelle, 1.0);
  habTexCoord = vertex.yz;
  x =coordTexture3D.x+tpx+deltat/2;
  xp= x-deltat*normal.x;
  vlightPos =(modelView*vec4(lightPos,1.0)).xyz;
for(int k=0;k <nbLumiere;k++) {
vec4 lumiere = lumieres[k];
flumieres[k]=vec4((modelView*vec4(lumiere.xyz,1.0)).xyz,lumiere.w);
}
vnormal = normalize(gl_NormalMatrix * normal);

 position =gl_ModelViewMatrix * vec4(vertex*echelle,1.0);
  vec3  v = vec3(position);     
  vcoordTexture2D = vec2(coordTexture3D.y+tpy,coordTexture3D.z+tpz);


  
  
}