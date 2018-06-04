uniform sampler2D colorTextureID;
uniform sampler2D shadowTextureID;
uniform float rayon;
uniform vec4 color;
varying vec2 vcoordTexture2D ;
uniform float tdx;
uniform float tdy;
uniform int showEdge;
uniform	float fogDensity;
uniform vec3 fogColor;
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
void main( void )
{

  
   float n = length(vcoordTexture2D.y-0.5);
   if (n > rayon )  {
   gl_FragColor =n*color; } else {
   
  
vec4 image =vec4(0.0,0.0,0.0,0.0);//texture2D( colorTextureID,vcoordTexture2D);
	
	for(float py=-1.0 ;py <= 1.0;py+=1.0){
		for(float px=-1.0 ;px <= 1.0;px+=1.0){
			vec2 r=vcoordTexture2D+vec2(px*tdx,py*tdy);
			image=image+texture2D(colorTextureID,r);
			
		}
	} 
	image=image/9.0f;
	
	
	image=image*pointProcheCote();
	float dist = texture2D(shadowTextureID,vcoordTexture2D).y;
	float total = 0.0f;
		float max = 0.0f;
		float d = 10.0f;
		float epsilon =0.0005;
		float u=4.0f;
		//if (dist < 20) {
			for (float py = -d; py <= d; py += 1.0) {
				for (float px = -d; px <= d; px += 1.0) {
					vec2 r = vcoordTexture2D + vec2(u*px * tdx, u*py * tdy);
					if (r.x >= 0.0 && r.x <= 1.0 && r.y >= 0.0 && r.y <= 1.0) {
						float dist2 = texture2D(shadowTextureID, r).y;
						if (dist2 >= dist+epsilon || dist >=500) {
							total = total + 1.0;
						}
						max = max + 1.0;
					}

				}
			}

	gl_FragColor =vec4((total / max) *brouillard(image.xyz,dist),0);
	gl_FragDepth =1.0; }
	//gl_FragColor =normal+0.000001*mix(gl_Fog.color, image *cf, fogFactor );
}
