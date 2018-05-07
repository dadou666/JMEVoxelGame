#extension GL_EXT_texture_array : enable
varying float fogFactor; 
varying vec2 vcoordTexture2D;
uniform sampler3D texture;
uniform sampler2DArray habillage;
 varying float x;
uniform float deltat;
uniform vec3 lightPos; 
uniform float sizeY;
uniform float sizeZ;
 varying float xp;
 varying  float cf;
 uniform float tpx;
uniform float tpy;
 uniform float tpz;
  varying  float culling;
 uniform float couleurfactor;
 uniform float h;
 varying vec2 habTexCoord;
 
 varying vec3 normals;
varying vec4 position;
 varying vec4 projection;
 varying vec4 vvertex1;
varying vec4 vvertex2;
varying vec4 vvertex3;
 
 
vec4 getColor() {
	vec4 empty = vec4(0,0,0,0);
	


	float tx=  vcoordTexture2D.x;
	float ty=  vcoordTexture2D.y;

	
vec4 tmp = texture3D(texture, vec3(x,tx,ty));
if (x < deltat) {
return tmp;
}
if (x>=1.0) {
vec4 tmp2 = texture3D(texture, vec3(xp,tx,ty));
return tmp2;
}

return tmp;




}


void main(void) {
	

	
	


	vec4 tmp = getColor();
	vec3 cmp =tmp.xyz;
		vec3 empty = vec3(0.0,0.0,0.0);
		
	if (cmp == empty   ) {
	discard;

	
	}  else {
	

	tmp = (1-couleurfactor)*texture2DArray(habillage, vec3(habTexCoord.y,habTexCoord.x,tmp.x*255.0));
		if (tmp.xyz == empty   ) {
	discard;

	
	} else {
	
    
	gl_FragData[0]	 = projection/projection.w;
	
	gl_FragData[1]=vvertex1/vvertex1.w;
	gl_FragData[2]=vvertex2/vvertex2.w;
	gl_FragData[3]=vvertex3/vvertex3.w;

	
	}
	
	 




	
	}
	


}