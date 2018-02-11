uniform sampler3D texture0;


varying vec3 coordTex ;
void main(void) {



if (coordTex.x >= 0 && coordTex.x <= (1) && coordTex.y >= 0 && coordTex.y <= (1) && coordTex.z >= 0 && coordTex.z <= (1)) {
vec3 tpos = vec3((coordTex.x),(coordTex.y),(coordTex.z));
vec4 color0 =texture3D(texture0, tpos);

vec4 empty = vec4(0,0,0,1.0);

if (color0.xyz == empty.xyz ) {
//gl_FragColor =vec4(coordTex,1.0);

	discard; 
	
	} else {
gl_FragColor =color0;
 gl_FragDepth=1.0;
   }
   
    } else {
    
    discard;
    }

}