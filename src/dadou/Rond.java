package dadou;

public class Rond {
	public float rayon;
	public float red;
	public float green;
	public float blue;
	public void initShader(Shader shader) {
		shader.glUniformfARB("rayon", rayon);
		shader.glUniformfARB("color", red, green, blue,0.0f);
	}

}
