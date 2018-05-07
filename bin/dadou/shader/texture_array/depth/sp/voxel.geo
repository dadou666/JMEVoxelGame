#version 330 compatibility
layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;
in vec2 a_vcoordTexture2D[];
in vec3 a_normalCube[];
in float a_u[];
in float a_up[];
in float a_deltatv[];
in vec2 a_habTexCoord[];
in vec3 a_vcoordTexture3D[];
in vec3 a_vcoordTexture3DP[];
in vec3 a_vnormal[];
in vec4 a_position[];
in vec4 a_projection[];
in vec4 sommet[];

out vec3 vcoordTexture3D;
out vec3 vcoordTexture3DP;
out  float up;
out vec3 normalCube;
out float deltatv;
out  float u;
out vec2 habTexCoord;
out vec2 vcoordTexture2D;



out    vec4 vvertex1;
out    vec4 vvertex2;
out    vec4 vvertex3;



void main()
{	
  for(int i=0; i<3; i++)
  {
    gl_Position = gl_in[i].gl_Position;

    vvertex1=vec4(sommet[0].xyz,0.666);
    vvertex2=vec4(sommet[1].xyz,0.666);
    vvertex3=vec4(sommet[2].xyz,0.666);

    
 vcoordTexture3D=a_vcoordTexture3D[i];
vcoordTexture3DP=a_vcoordTexture3DP[i];
 up=a_up[i];
 normalCube=a_normalCube[i];
 deltatv=a_deltatv[i];
 u=a_u[i];
 habTexCoord=a_habTexCoord[i];
 vcoordTexture2D=a_vcoordTexture2D[i];
    EmitVertex();
  }
  
  EndPrimitive();
}  