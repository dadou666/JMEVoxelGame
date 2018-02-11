package dadou;

public class EffetEcran {
	public float rayon;
	public float red;
	public float green;
	public float blue;
	public boolean estDivision = false;
	public Shader shader() {
		if (estDivision) {
			return Game.shaderNormalAvecDivisionEcran;
		}
		return  Game.shaderNormalAvecRond;
	}
	public void initShader(Shader shader) {
		shader.glUniformfARB("rayon", rayon);
		shader.glUniformfARB("color", red, green, blue,0.0f);
	}

}
