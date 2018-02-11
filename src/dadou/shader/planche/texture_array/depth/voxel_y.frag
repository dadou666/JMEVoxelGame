#extension GL_EXT_texture_array : enable
varying float fogFactor; 
varying vec2 vcoordTexture2D;
uniform sampler3D texture;
uniform sampler2DArray habillage;
uniform vec3 lightPos;
varying  float y;
varying  float yp;
uniform float deltat;
 uniform float tpx;
uniform float tpy;
 uniform float tpz;

uniform float sizeX;
uniform float sizeZ;
varying float cf;

uniform float couleurfactor;
 varying vec2 habTexCoord;
 
varying vec3 normals;
varying vec4 position;
varying vec4 projection;

vec4 getColor() {
	vec4 empty = vec4(0,0,0,0);
	


	float tx=  vcoordTexture2D.x;
	float ty=  vcoordTexture2D.y;

vec4 tmp = texture3D(texture, vec3(tx,y,ty));
if (y < deltat) {
return tmp;
}
if (y>=1.0) {
vec4 tmp2 = texture3D(texture, vec3(tx,yp,ty));
return tmp2;
}



return tmp;



}

void main(void) {
	

	vec4 tmp = getColor();
vec3 cmp =tmp.xyz;
		vec3 empty = vec3(0.0,0.0,0.0);

	if (cmp == empty ) {
	discard;
	}  else {
	

		tmp = (1-couleurfactor)*texture2DArray(habillage, vec3(habTexCoord.x,habTexCoord.y,tmp.z*255));
	
	if (tmp.xyz == empty ) {
	discard;
	}  else {
	
gl_FragData[0]	 = projection/projection.w;
	}

	
	
	}
	


}