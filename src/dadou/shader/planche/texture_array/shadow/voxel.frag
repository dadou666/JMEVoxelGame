#extension GL_EXT_texture_array : enable
varying float fogFactor; 

uniform sampler3D texture;
uniform sampler2DArray  habillage;
varying vec3 vcoordTexture3D;
varying vec3 vcoordTexture3DP;
varying  float u;
uniform float sizeX;
uniform float sizeY;

varying  float up;
varying float cf;


uniform float transparence;
varying vec3 normals;
varying vec4 position;
varying vec4 projection;
varying vec4 ShadowCoord; 
varying vec2 habTexCoord;
varying vec4 flumieres[8];
uniform float obscur;
varying vec3 vlightPos;
uniform	float fogDensity;
uniform vec3 fogColor;
 uniform float couleurfactor;
 varying vec3 normalCube;

varying float deltatv;
vec4 getColor() {
	vec4 empty = vec4(0,0,0,1);


vec4 tmp = texture3D(texture, vcoordTexture3D);
if (u < deltatv) {
return tmp;
}
if (u>=1.0) {
vec4 tmp2 = texture3D(texture,vcoordTexture3DP);
return tmp2;
}


return tmp;


}


float visibiliteOmbre() {

		return 1.0;
	
}



void main(void) {
	

	vec4 tmp =getColor();
	vec3 cmp= tmp.xyz;
		vec3 empty = vec3(0,0,0);
	if (cmp == empty ) {
	discard;
	}  else {


	tmp = (1-couleurfactor)*texture2DArray(habillage, vec3(habTexCoord.x,habTexCoord.y,dot(normalCube,tmp.xyz)*255));
if (tmp.xyz == empty ) {
discard;
	}  else {
		float vis=visibiliteOmbre();
		gl_FragData[0]		= vec4(vis*tmp.xyz,transparence);
		gl_FragData[1] = vec4(0, length(position.xyz),0,0);
	
	
	 }
	
}
	


}