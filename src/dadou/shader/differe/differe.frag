uniform sampler2D colorTextureID;
uniform sampler2D shadowTextureID;
uniform float tdx;
uniform float tdy;
uniform float obscurite;
uniform int showEdge;
uniform	float fogDensity;
uniform vec3 fogColor;
varying vec2 vcoordTexture2D ;

float pointProcheCote() {
	if (showEdge ==0) {
		return 1.0;
	}
		vec3 normal = texture2D(shadowTextureID,vcoordTexture2D).xyz;
	float m=2.0;
	float n=0.0f;
	for(float py=-m ;py <= m;py+=1.0){
		for(float px=-m ;px <= m;px+=1.0){
			vec2 r=vcoordTexture2D+vec2(px*tdx,py*tdy);
		
			if (abs(texture2D(shadowTextureID,r).x - normal.x) <=0.001) {
			     n+=1.0f;
			}
			
		}
	}
	
	float u=2*m+1;
	
	return n/(u*u);

}

vec3 brouillard(vec3 color,float l) {
    float LOG2 = 1.442695;

	float fogFactor = exp2( -fogDensity * 
					   fogDensity * 
					   l * 
					   l * 
					   LOG2 );
	fogFactor = clamp(fogFactor, 0.0, 1.0);
    return  mix(fogColor,color, fogFactor );  
}

void main( void )
{



	
	vec4 image =vec4(0.0,0.0,0.0,0.0);//texture2D( colorTextureID,vcoordTexture2D);

	float n=0.0f;
	for(float py=-1.0 ;py <= 1.0;py+=1.0){
		for(float px=-1.0 ;px <= 1.0;px+=1.0){
			vec2 r=vcoordTexture2D+vec2(px*tdx,py*tdy);
			image=image+texture2D(colorTextureID,r);
		
			
		}
	} 
	image=image/9.0f;
		image=image*pointProcheCote();
	float dist = texture2D(shadowTextureID,vcoordTexture2D).y;
	
	gl_FragColor =vec4(brouillard(image.xyz,dist),0);  
	gl_FragDepth =1.0;
}