
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

uniform mat4 DepthBiasMVP;
varying vec4 ShadowCoord; 
uniform mat4 modelView;
uniform vec4 lumieres[8];
uniform int nbLumiere;
varying vec4 flumieres[8];
uniform vec3 lightPos; 
varying vec3 vlightPos;



void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex*echelle, 1.0);
  ShadowCoord = DepthBiasMVP*vec4(vertex*echelle, 1.0);
  vvertex1 = DepthBiasMVP*vec4(vertex1*echelle, 1.0);
  vvertex1=vvertex1 / vvertex1.w;
  vvertex2 = DepthBiasMVP*vec4(vertex2*echelle, 1.0);
  vvertex2=vvertex2 / vvertex2.w;
  vvertex3 = DepthBiasMVP*vec4(vertex3*echelle, 1.0);
    vvertex3=vvertex3 / vvertex3.w;
habTexCoord=vertex.xy;
  vlightPos =(modelView*vec4(lightPos,1.0)).xyz;
    for(int k=0;k <nbLumiere;k++) {
vec4 lumiere = lumieres[k];
flumieres[k]=vec4((modelView*vec4(lumiere.xyz,1.0)).xyz,lumiere.w);
}
  z =coordTexture3D.z+tpz+deltat/2;
  zp=z-deltat*normal.z;
  vnormal = normalize(gl_NormalMatrix * normal);
  position =gl_ModelViewMatrix * vec4(vertex*echelle,1.0);
  
  vec3  v = vec3(position);    

  vcoordTexture2D =vec2(coordTexture3D.x+tpx,coordTexture3D.y+tpy);

  
  
}