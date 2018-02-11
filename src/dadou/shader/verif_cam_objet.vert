
varying vec3 coordTex ;

void main(void)
{

     gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;       

	vec4 u =  gl_MultiTexCoord0;
   coordTex = u.xyz;

}
        