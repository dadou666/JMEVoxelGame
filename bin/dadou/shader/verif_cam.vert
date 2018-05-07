uniform float dimX;
uniform float dimY;
uniform float dimZ;
varying vec3 coordTex ;

void main(void)
{

     gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;       


   coordTex =vec3((gl_Vertex.x+dimX/2)/dimX,(gl_Vertex.y+dimY/2)/dimY,(gl_Vertex.z+dimZ/2)/dimZ);
  // (1/dim)*(gl_Vertex.xyz+vec3(dim/2,dim/2,dim/2));

}
        