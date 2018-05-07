uniform sampler2D depthTextureID;

varying vec2 vcoordTexture2D ;


void main( void )
{



	//gl_FragColor =vec4(1,vcoordTexture2D.x,vcoordTexture2D.y,0);
	vec4 p=texture2D(depthTextureID,vcoordTexture2D);

	
	gl_FragColor=vec4(p.z,p.z,p.z,1.0); 

}
