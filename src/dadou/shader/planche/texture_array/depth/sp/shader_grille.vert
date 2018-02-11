attribute vec3 vertex;
attribute vec2 coordTexture2D;
attribute vec3 normal;
varying vec3 vNormal;
varying vec2 vcoordTexture2D;
varying float cf;
varying float fogFactor;
varying vec4 position;
varying vec4 projection;
void main(void) {
  gl_Position =  gl_ModelViewProjectionMatrix*vec4(vertex, 1.0);
  projection = gl_Position;
   position =gl_ModelViewMatrix * vec4(vertex,1.0);
vcoordTexture2D= coordTexture2D;
vNormal =normal;
 vec3  N = normalize(gl_NormalMatrix * normal);
 vec3 v = vec3(gl_ModelViewMatrix * vec4(vertex,1.0));

   	 float LOG2 = 1.442695;
	gl_FogFragCoord = length(v);
	fogFactor = exp2( -gl_Fog.density * 
					   gl_Fog.density * 
					   gl_FogFragCoord * 
					   gl_FogFragCoord * 
					   LOG2 );
	fogFactor = clamp(fogFactor, 0.0, 1.0);
	
  vec3 lp =gl_LightSource[0].position.xyz;
 

  vec3 lp1 =gl_LightSource[1].position.xyz;
  vec3 L1 = normalize(lp1 - v);
  


  
  lp = lp- 55*gl_LightSource[0].spotDirection ;
   vec3 L = normalize(lp - v);
   
   cf = min(abs(dot(L,N))+abs(dot(L1,N)),1.0);
}