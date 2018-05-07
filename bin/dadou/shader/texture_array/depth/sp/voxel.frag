#version 330 compatibility
#extension GL_EXT_texture_array : enable

uniform sampler3D texture;
uniform sampler2DArray habillage;
uniform vec3 lightPos;
uniform float sizeX;
uniform float sizeY;
uniform float couleurfactor;

in vec3 vcoordTexture3D;
in vec3 vcoordTexture3DP;
in  float up;
in vec3 normalCube;
in float deltatv;
in  float u;
 in vec2 habTexCoord;
in vec2 vcoordTexture2D;

in vec4 vvertex1;
in vec4 vvertex2;
in vec4 vvertex3;



in vec4 projection;

struct  Pixel {
	vec4 color;
	vec3 normal;
};

Pixel getColor() {
	vec4 empty = vec4(0,0,0,1);
	



vec4 tmp = texture3D(texture,vcoordTexture3D);
Pixel p;
p.color=tmp;

if (u < deltatv) {

return p;
}
if (u>=1.0) {
vec4 tmp2 = texture3D(texture,vcoordTexture3DP);
p.color=tmp2;

return p;
}
vec4 tmp2 =  texture3D(texture, vcoordTexture3DP);
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


		tmp = texture2DArray(habillage, vec3(habTexCoord.x,habTexCoord.y,dot(normalCube,tmp.xyz)*255));
if (tmp.xyz == empty ) {
	discard;
	}  else {

    
	//gl_FragData[0]	 = projection/projection.w;
	vec3 planNormal = cross(vvertex1.xyz-vvertex2.xyz,vvertex1.xyz-vvertex3.xyz);
	gl_FragData[0]=vec4(planNormal,dot(planNormal,vvertex1.xyz));

	
	}
	
}
	


}