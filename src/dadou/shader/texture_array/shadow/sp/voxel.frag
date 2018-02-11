#extension GL_EXT_texture_array : enable
varying float fogFactor; 
varying vec3 vcoordTexture3D;
varying vec3 vcoordTexture3DP;

uniform sampler2D shadowTexture;
uniform sampler3D texture;
uniform sampler2DArray habillage;
 varying float u;
varying float deltatv;
uniform vec3 lightPos; 
uniform float sizeY;
uniform float sizeZ;
 varying float up;
 varying  float cf;
uniform mat3 textureMat;
uniform mat3 textureMatInv;
uniform 	float obscur;
 uniform float couleurfactor;
 uniform float transparence;
varying vec3 normalCube;
 varying vec4 ShadowCoord; 
 varying vec3 vnormal;
varying vec4 position;
varying vec2 habTexCoord;
varying vec4 flumieres[8];
varying vec4 fzones[8];

varying vec4 sommet;
uniform sampler2D shadowVertex1Texture;
uniform vec4 planCam;
uniform vec3 colorZone;

uniform float tex_dx;
uniform float tex_dy;

   

int pointInLight(vec3 p, vec4 plan){
	vec3 n = plan.xyz;
	
	float t2=(plan.w-dot(p,plan.xyz))/dot(plan.xyz,planCam.xyz);
	
	if (t2 < -0.5) {
		return 0;
	}
	
    return 1;
}  

struct  Pixel {
	vec4 color;
	vec3 normal;
};


varying vec3 vlightPos;
uniform int nbLumiere;
uniform int nbZone;
uniform int useShadow;




Pixel getColor() {
	vec4 empty = vec4(0,0,0,0);
	vec4 tmp = texture3D(texture, vcoordTexture3D);
	Pixel p;
	p.color = tmp;
	p.normal = vnormal;
	if (u < deltatv) {
		return p;
	}
	if (u>=1.0) {
		p.normal = -vnormal;
		vec4 tmp2 = texture3D(texture, vcoordTexture3DP);
		p.color = tmp2;
		return p;
	}
	vec4 tmp2 = texture3D(texture,vcoordTexture3DP);
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
		if ( l <= lumiere.w && dot(dir,normal) >=0.01) {
			float tmp =1.0-(l/lumiere.w);
			if (tmp > t) {
				t=tmp;
			}
		}
			
	}
	return obscur*(1-t)+t;
}

vec3 donnerColorZone(vec3 color) {
     	for(int i=0;i < nbZone;i++) {
		vec4 zone=fzones[i];
		vec3 dir=position.xyz-zone.xyz;
		float l=length(dir);
		if ( l <= zone.w ) {
			return mix(color,colorZone,0.5);
		}
			
	}
	return color;

}

float visibiliteOmbre3(vec3 normal) {
	if (useShadow==0) {
		return 1.0f;
	}
	vec3 lightDir =normalize(position.xyz-vlightPos);
	vec4 sc=ShadowCoord;
	float cf = dot(normal, lightDir);
	

  	sc=sc/sc.w;
	float t=0.0;
	if (cf <=0) {
		return obscur;
	}
	float u=cf*(1.0-obscur)+obscur;
	vec2 p=0.5*(sc.xy+vec2(1,1));
	float dx=1.0;
	float dy=1.0;
	float total=0.0;
	for(float py=-dx ;py <= dy;py+=1.0){
		for(float px=-dx ;px <= dy;px+=1.0){
			vec2 r=p+vec2(px*tex_dx,py*tex_dy);
		
		    vec4 plan = texture2D(shadowVertex1Texture,r);
		
			if (plan.xyz != vec3(0,0,0)) {
			
			
			if (pointInLight(sommet.xyz,plan)==1) {
				total+=1.0;
			}
			}
		}
	}
	total = total /9.0; 

	return obscur*(1-total)+total*u;
}






void main(void) {
	Pixel p = getColor();
	vec4 tmp = p.color;
	vec3 cmp =tmp.xyz;
	vec3 empty = vec3(0.0,0.0,0.0);
		
	if (cmp == empty) {
		discard;
	}  else {
		tmp = (1-couleurfactor)*texture2DArray(habillage, vec3(habTexCoord.x,habTexCoord.y,dot(normalCube,tmp.xyz)*255.0));
		if (tmp.xyz == empty   ) {
			discard;
		} else {
   			float  cf =(visibiliteOmbre3(p.normal.xyz)+lumiereVisibilite(p.normal.xyz))/2.0;
   	
    		
    			
		    gl_FragData[0]= vec4(donnerColorZone(cf*tmp).xyz,transparence);
		    gl_FragData[1]=vec4(dot(p.normal,position.xyz), length(position.xyz),0.0,0.0);
		}
	}
}