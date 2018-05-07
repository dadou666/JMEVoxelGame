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
   varying vec4 vvertex1;
varying vec4 vvertex2;
varying vec4 vvertex3;

uniform float couleurfactor;
 varying vec2 habTexCoord;
 



varying vec4 projection;

struct  Pixel {
	vec4 color;
	vec3 normal;
};

Pixel getColor() {
	vec4 empty = vec4(0,0,0,1);
	


	float tx= vcoordTexture2D.x;
	float ty= vcoordTexture2D.y;

vec4 tmp = texture3D(texture, vec3(tx,ty,z));
Pixel p;
p.color=tmp;

if (z < deltat) {

return p;
}
if (z>=1.0) {
vec4 tmp2 = texture3D(texture, vec3(tx,ty,zp));
p.color=tmp2;

return p;
}
vec4 tmp2 =  texture3D(texture, vec3(tx,ty,zp));
if (tmp2.xyz != empty.xyz && tmp.xyz != empty.xyz) {
p.color=empty;
return p; 

}
if (tmp.xyz == empty.xyz){

p.color=tmp2;
return p;
}

return p;


}


void main(void) {
	
Pixel p= getColor();
	vec4 tmp = p.color;
	vec3 cmp= tmp.xyz;
		vec3 empty = vec3(0,0,0);
	if (cmp == empty  ) {
	discard;
	}  else {


		tmp = texture2DArray(habillage, vec3(habTexCoord.x,habTexCoord.y,tmp.z*255));
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