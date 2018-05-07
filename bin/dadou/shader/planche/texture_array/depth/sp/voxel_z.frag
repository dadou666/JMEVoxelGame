#extension GL_EXT_texture_array : enable
varying float fogFactor; 
varying vec2 vcoordTexture2D;
uniform sampler3D texture;
uniform sampler2DArray habillage;
uniform vec3 lightPos;
varying  float z;
uniform float sizeX;
uniform float sizeY;
uniform float deltat;
varying  float zp;
varying float cf;
 uniform float tpx;
uniform float tpy;
 uniform float tpz;

uniform float couleurfactor;
 varying vec2 habTexCoord;
 varying vec4 vvertex1;
varying vec4 vvertex2;
varying vec4 vvertex3;

varying vec3 normals;
varying vec4 position;
varying vec4 projection;

vec4 getColor() {
	vec4 empty = vec4(0,0,0,1);
	


	float tx= vcoordTexture2D.x;
	float ty= vcoordTexture2D.y;

vec4 tmp = texture3D(texture, vec3(tx,ty,z));
if (z < deltat) {
return tmp;
}
if (z>=1.0) {
vec4 tmp2 = texture3D(texture, vec3(tx,ty,zp));
return tmp2;
}


return tmp;


}



void main(void) {
	

	vec4 tmp =getColor();
	vec3 cmp= tmp.xyz;
		vec3 empty = vec3(0,0,0);
	if (cmp == empty ) {
	discard;
	}  else {


		tmp = (1-couleurfactor)*texture2DArray(habillage, vec3(habTexCoord.x,habTexCoord.y,tmp.z*255));
if (tmp.xyz == empty ) {
	discard;
	}  else {
 gl_FragData[0]	 = projection/projection.w;
	gl_FragData[1]=vvertex1/vvertex1.w;
	gl_FragData[2]=vvertex2/vvertex2.w;
	gl_FragData[3]=vvertex3/vvertex3.w;
	}
	
}
	


}