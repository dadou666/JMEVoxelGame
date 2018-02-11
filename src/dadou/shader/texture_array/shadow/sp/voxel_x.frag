#extension GL_EXT_texture_array : enable
varying float fogFactor; 
varying vec2 vcoordTexture2D;
uniform sampler2D shadowTexture;

uniform sampler2D shadowVertex1Texture;
uniform sampler2D shadowVertex2Texture;
uniform sampler2D shadowVertex3Texture;
varying vec4 vvertex1;
varying vec4 vvertex2;
varying vec4 vvertex3;
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
uniform 	float obscur;
 uniform float couleurfactor;
 uniform float transparence;

 varying vec4 ShadowCoord; 
 varying vec3 vnormal;
varying vec4 position;
varying vec2 habTexCoord;
varying vec4 flumieres[8];
uniform float tex_dx;
uniform float tex_dy;
struct  Pixel {
	vec4 color;
	vec3 normal;
};


varying vec3 vlightPos;
uniform int nbLumiere;
uniform int useShadow;
 uniform	float fogDensity;
uniform vec3 fogColor;
Pixel getColor() {
	vec4 empty = vec4(0,0,0,0);
	


	float tx=  vcoordTexture2D.x;
	float ty=  vcoordTexture2D.y;

	
vec4 tmp = texture3D(texture, vec3(x,tx,ty));
Pixel p;
p.color = tmp;
p.normal = vnormal;
if (x < deltat) {

return p;
}
if (x>=1.0) {
p.normal = -vnormal;
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
p.normal = -vnormal;
p.color = tmp2;
return p;
}

return p;




}

float lumiereVisibilite(vec3 normal) {

	float t=0.0;
	
for(int i=0;i < nbLumiere;i++) {
		vec4 lumiere=flumieres[i];
		vec3 dir=position.xyz-lumiere.xyz;
		float l=length(dir);
		if ( l <= lumiere.w && dot(dir,normal) >0) {
		float tmp =1.0-(l/lumiere.w);
		if (tmp > t) {
			t=tmp;
		}


		}
		
	}
return obscur*(1-t)+t;
}

float visibiliteOmbre(vec3 normal) {
	if (useShadow==0) {
		return 1.0f;
	}
	vec3 lightDir =normalize(position.xyz-vlightPos);
	vec4 sc=ShadowCoord;
	float cf = dot(normal, lightDir);
	float cosTheta = clamp(cf,0.0,1.0);
 	float bias = 0.005*tan(acos(cosTheta)); // cosTheta is dot( n,l ), clamped between 0 and 1
	bias =0.00;// clamp(bias, 0.0,0.005); 
	sc.z=  sc.z-bias;
  	sc=sc/sc.w;
	float t=0.0;
	if (cf <=0) {
	return obscur;
	}
	vec2 p=0.5*(sc.xy+vec2(1,1));
	vec3 v1s = texture2D(shadowVertex1Texture,p).xyz;
	vec3 v2s = texture2D(shadowVertex2Texture,p).xyz;
	vec3 v3s = texture2D(shadowVertex3Texture,p).xyz;
	

			float e = 0.0001;
			if (length(v1s- vvertex1.xyz)<=e &&  length(v2s- vvertex2.xyz)<=e && length(v3s- vvertex3.xyz)<=e ) {
		return 1.0;
	}
		
		
	
	

			if (texture2D(shadowTexture,p).z < sc.z  ) {
				return obscur;
			}
		
	return 1.0;
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
	

	
	

	Pixel p = getColor();
	vec4 tmp = p.color;
	vec3 cmp =tmp.xyz;
		vec3 empty = vec3(0.0,0.0,0.0);
		
	if (cmp == empty   ) {
	discard;

	
	}  else {
	

	tmp = (1-couleurfactor)*texture2DArray(habillage, vec3(habTexCoord.y,habTexCoord.x,tmp.x*255.0));
		if (tmp.xyz == empty   ) {
	discard;

	
	} else {
   float  cf = max(lumiereVisibilite(p.normal.xyz),visibiliteOmbre(p.normal.xyz));
    gl_FragData[0]		= vec4(brouillard(cf*tmp.xyz),transparence);



	}
	
	 




	
	}
	


}