

attribute vec3 vertex;



attribute vec3 normal;



varying float cf;
uniform float dx;
uniform float dy;
uniform float dz;
varying vec3 vnormal;
varying vec3 vvertexD; 
void main(void) {
vec3 vertexD = vertex * vec3(dx,dy,dz);
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertexD, 1.0);
  


vvertexD=vertexD;
vnormal=normal;
 vec3  N = normalize(gl_NormalMatrix * normal);
 vec3 v = vec3(gl_ModelViewMatrix * vec4(vertexD,1.0));

  vec3 lp =gl_LightSource[0].position.xyz;
 

  vec3 lp1 =gl_LightSource[1].position.xyz;
  vec3 L1 = normalize(lp1 - v);
  


  
  lp = lp- 55*gl_LightSource[0].spotDirection ;
   vec3 L = normalize(lp - v);
   
   cf = min(abs(dot(L,N))+abs(dot(L1,N)),1.0);
}