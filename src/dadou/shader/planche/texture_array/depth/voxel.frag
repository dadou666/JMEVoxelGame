#extension GL_EXT_texture_array : enable
varying float fogFactor; 
varying vec2 vcoordTexture2D;
uniform sampler3D texture;
uniform sampler2DArray habillage;
uniform vec3 lightPos;
varying  float u;
uniform float sizeX;
uniform float sizeY;

varying float deltatv;
varying  float up;
varying float cf;

uniform float couleurfactor;
 varying vec2 habTexCoord;
 varying vec3 vcoordTexture3D;
varying vec3 vcoordTexture3DP;

varying vec3 normals;
varying vec4 position;
varying vec4 projection;
varying vec3 normalCube;

vec4 getColor() {
	vec4 empty = vec4(0,0,0,1);
	




vec4 tmp = texture3D(texture,vcoordTexture3D);
if (u < deltatv) {
return tmp;
}
if (u>=1.0) {
vec4 tmp2 = texture3D(texture, vcoordTexture3DP);
return tmp2;
}


return tmp;


}



void main(void) {
	

	vec4 tmp =getColor();
	vec3 cmp= tmp.xyz;
		vec3 empty = vec3(0,0,0);
	


		tmp = (1-couleurfactor)*texture2DArray(habillage, vec3(habTexCoord.x,habTexCoord.y,dot(normalCube,tmp.xyz)*255));
if (tmp.xyz == empty ) {
	discard;
	}  else {
 gl_FragData[0]	 = projection/projection.w;
	}
	

	


}