#extension GL_EXT_texture_array : enable
varying float fogFactor; 
varying vec2 vcoordTexture2D;
uniform sampler3D texture;
uniform sampler2DArray habillage;

varying  float y;
varying  float yp;
uniform float deltat;
 uniform float tpx;
uniform float tpy;
 uniform float tpz;
uniform float tex_dx;
uniform float tex_dy;
uniform float sizeX;
uniform float sizeZ;
varying float cf;
uniform sampler2D shadowTexture;
uniform sampler2D shadowVertex1Texture;
uniform sampler2D shadowVertex2Texture;
uniform sampler2D shadowVertex3Texture;

varying vec4 vvertex1;
varying vec4 vvertex2;
varying vec4 vvertex3;
uniform float couleurfactor;
uniform float transparence;
varying vec3 normals;
varying vec4 position;
varying vec4 ShadowCoord; 
varying vec2 habTexCoord;
varying vec4 flumieres[8];
uniform vec3 lightPos;
varying vec3 vlightPos;
uniform float obscur;
uniform	float fogDensity;
uniform vec3 fogColor;
uniform int useShadow;
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

float visibiliteOmbre() {
		if (useShadow==0 || transparence > 0.0) {
		return 1.0f;
	}
	vec3 lightDir =normalize(position.xyz-vlightPos);
	vec4 sc=ShadowCoord;
	float cf = dot(normals, lightDir);
	float cosTheta = clamp(cf,0.0,1.0);
 	float bias = 0.005*tan(acos(cosTheta)); // cosTheta is dot( n,l ), clamped between 0 and 1
	bias = clamp(bias, 0.0,0.005); 
	sc.z=  sc.z-bias;
  	sc=sc/sc.w;
	float t=0.0;
	vec2 p=0.5*(sc.xy+vec2(1,1));
	vec3 v1s = texture2D(shadowVertex1Texture,p);
	vec3 v2s = texture2D(shadowVertex2Texture,p);
	vec3 v3s = texture2D(shadowVertex3Texture,p);
	
	if (v1s == vvertex1 && v2s == vvertex2 && v3s ==vvertex3) {
		return 1.0f;
	}
	for(float py=-1.0 ;py <= 1.0;py+=1.0){
		for(float px=-1.0 ;px <= 1.0;px+=1.0){
			vec2 r=0.5*(sc.xy+vec2(1,1))+vec2(px*tex_dx,py*tex_dy);
			if (texture2D(shadowTexture,r).z < sc.z && sc.z < 1.0 ) {
				t+=1.0; 
			}
		}
	} 
	t=t/9.0;
	return (obscur*t+1-t);
}

vec3 brouillard(vec3 color) {
  

  	
  
     	 float LOG2 = 1.442695;
	float l = length(position.xyz);
	float fogFactor = exp2( -fogDensity * 
					   fogDensity * 
					   l * 
					   l * 
					   LOG2 );
	fogFactor = clamp(fogFactor, 0.0, 1.0);
    return  mix(fogColor,color, fogFactor );  
}


void main(void) {
	

	vec4 tmp = getColor();
vec3 cmp =tmp.xyz;
		vec3 empty = vec3(0.0,0.0,0.0);

	if (cmp == empty ) {
	discard;
	}  else {
	
	
	tmp = (1-couleurfactor)*texture2DArray(habillage, vec3(habTexCoord.x,habTexCoord.y,tmp.y*255.0));
	
	if (tmp.xyz == empty ) {
	discard;
	}  else {
	
	float vis=visibiliteOmbre();
		gl_FragData[0]		= vec4(brouillard(vis*tmp.xyz),transparence);
		
	}

	
	
	}
	


}