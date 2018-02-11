attribute vec3 vertex;
attribute vec3 normal;

varying float cf;

void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex, 1.0);

 vec3  N = normalize(gl_NormalMatrix * normal);
 vec3 v = vec3(gl_ModelViewMatrix * vec4(vertex,1.0));

  vec3 lp =gl_LightSource[0].position.xyz;
 

  vec3 lp1 =gl_LightSource[1].position.xyz;
  vec3 L1 = normalize(lp1 - v);
  


  
  lp = lp- 55*gl_LightSource[0].spotDirection ;
   vec3 L = normalize(lp - v);
   
   cf = min(abs(dot(L,N))+abs(dot(L1,N)),1.0);
}