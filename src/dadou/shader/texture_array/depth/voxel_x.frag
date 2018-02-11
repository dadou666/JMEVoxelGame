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
 


 varying vec4 projection;
 

struct  Pixel {
	vec4 color;
	vec3 normal;
};




 
Pixel getColor() {
	vec4 empty = vec4(0,0,0,0);
	


	float tx=  vcoordTexture2D.x;
	float ty=  vcoordTexture2D.y;

	
vec4 tmp = texture3D(texture, vec3(x,tx,ty));
Pixel p;
p.color = tmp;

if (x < deltat) {

return p;
}
if (x>=1.0) {

vec4 tmp2 = texture3D(texture, vec3(xp,tx,ty));
p.color = tmp2;
return p;
}
vec4 tmp2 = texture3D(texture, vec3(xp,tx,ty));
if (tmp2.xyz != empty.xyz && tmp.xyz != empty.xyz) {
p.color = empty;
return p; 


}
if (tmp.xyz == empty.xyz){

p.color = tmp2;
return p;
}

return p;




}


void main(void) {
	

	
	

Pixel p= getColor();
	vec4 tmp = p.color;
	vec3 cmp =tmp.xyz;
		vec3 empty = vec3(0.0,0.0,0.0);
		
	if (cmp == empty ) {
	discard;

	
	}  else {
	

	tmp = texture2DArray(habillage, vec3(habTexCoord.y,habTexCoord.x,tmp.x*255.0));
		if (tmp.xyz == empty   ) {
	discard;

	
	} else {
	
    
	gl_FragData[0]	 = projection/projection.w;
	
	
	}
	
	 




	
	}
	


}