uniform sampler2D shadowTextureID1;
uniform sampler2D shadowTextureID2;

varying vec2 vcoordTexture2D ;


void main( void )
{



	//gl_FragColor =vec4(1,vcoordTexture2D.x,vcoordTexture2D.y,0);
	vec4 p0=texture2D(shadowTextureID1,vcoordTexture2D);
	vec4 p1=texture2D(shadowTextureID2,vcoordTexture2D);
	
	if (p0.z <= p1.z) {
		gl_FragData[0]	 =p0;
	
	} else {
	gl_FragData[0]	 =p1;
	}

}
