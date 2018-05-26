package dadou;

import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

import com.jme.input.InputSystem;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;
import com.jme.system.lwjgl.LWJGLDisplaySystem;

import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.param.GraphicsParam;

public class Game {
	public DisplaySystem displaySystem;
	protected Camera cam;
	public static File logError = new File("err.txt");
	public static File logOut = new File("out.txt");

	public enum RenderMode {
		Depth, Shadow, Normal
	};

	static public boolean devMode = false;
	static public float obscurite = 0.40f;
	static public RenderMode rm;
	static public boolean aliasing = true;
	static public int showEdge =1;
	static public Vector3f colorTerrain = new Vector3f(1.0f,1.0f,1.0f);
	static public float minColor = 0.55f;
	static public VoxelShader vs;

	static public VoxelShader vsDepth;
	static public VoxelShader vsShadow;
	static public FloatBuffer bufferTextureX;
	static public FloatBuffer bufferTextureY;
	static public FloatBuffer bufferTextureZ;

	static public FloatBuffer bufferTextureXInv;
	static public FloatBuffer bufferTextureYInv;
	static public FloatBuffer bufferTextureZInv;

	static public void initMat() {
		bufferTextureX = BufferUtils.createFloatBuffer(9);
		bufferTextureY = BufferUtils.createFloatBuffer(9);
		bufferTextureZ = BufferUtils.createFloatBuffer(9);

		bufferTextureXInv = BufferUtils.createFloatBuffer(9);
		bufferTextureYInv = BufferUtils.createFloatBuffer(9);
		bufferTextureZInv = BufferUtils.createFloatBuffer(9);

		Matrix3f matTextureX = new Matrix3f();
		Matrix3f matTextureY = new Matrix3f();
		Matrix3f matTextureZ = new Matrix3f();

		Matrix3f matTextureXInv = new Matrix3f();
		Matrix3f matTextureYInv = new Matrix3f();
		Matrix3f matTextureZInv = new Matrix3f();

		matTextureX.setRow(0, new Vector3f(0, 0, 1));
		matTextureX.setRow(1, new Vector3f(0, 1, 0));
		matTextureX.setRow(2, new Vector3f(1, 0, 0));

		matTextureY.setRow(0, new Vector3f(1, 0, 0));
		matTextureY.setRow(1, new Vector3f(0, 0, 1));
		matTextureY.setRow(2, new Vector3f(0, 1, 0));

		matTextureZ.setRow(0, new Vector3f(1, 0, 0));
		matTextureZ.setRow(1, new Vector3f(0, 1, 0));
		matTextureZ.setRow(2, new Vector3f(0, 0, 1));
		bufferTextureX.rewind();
		bufferTextureY.rewind();
		bufferTextureZ.rewind();
		bufferTextureXInv.rewind();
		bufferTextureYInv.rewind();
		bufferTextureZInv.rewind();
		matTextureX.fillFloatBuffer(bufferTextureX);
		matTextureY.fillFloatBuffer(bufferTextureY);
		matTextureZ.fillFloatBuffer(bufferTextureZ);

		matTextureZInv = matTextureZ.invert();
		matTextureYInv = matTextureY.invert();
		matTextureXInv = matTextureX.invert();

		matTextureXInv.fillFloatBuffer(bufferTextureXInv);
		matTextureYInv.fillFloatBuffer(bufferTextureYInv);
		matTextureZInv.fillFloatBuffer(bufferTextureZInv);
	}

	static public VoxelShader vsDepthPlanche;
	static public VoxelShader vsShadowPlanche;
	static public Shader shaderNormalAvecRond;
	static public Shader shaderNormalAvecDivisionEcran;

	static public Shader shaderParticule;
	static public Shader shaderParticuleDevant;
	static public Shader shaderSprite;
	static public Shader shaderWorldBox;
	static public Shader shaderWorldBoxDepth;
	static public Shader shaderWorldBoxVisibilite;
	static public Shader shaderBase;
	static public Shader shaderSoleil;
	static public Shader shaderTerrain;
	static public Shader shaderLumiere;
	static public Shader shaderSkyBox;
	static public Shader shaderSansVBO;

	static public int modifierOmbre = 0;
	static public float fogDensity = 0.0015f;
	static public Vector3f fogColor = new Vector3f(0.5f, 0.5f, 0.5f);
	static public float screenPixelCount;
	static public Shader shaderWidget;

	public static int shadowMapWidth = 2000;
	public static int shadowMapHeight = 2000;
	static public float profondeur;
	public static boolean pleineEcran = false;
	static public Fps fps;
	static public Fps fpsGlobal = new Fps();
	static public Fps fpsMesure = new Fps();

	public static EtatOmbre etatOmbre = EtatOmbre.OmbreActive;
	public Key keyUp = new Key(Keyboard.KEY_UP);
	public Key keyDown = new Key(Keyboard.KEY_DOWN);
	public Key keyLeft = new Key(Keyboard.KEY_LEFT);
	public Key keyRight = new Key(Keyboard.KEY_RIGHT);
	public Key keyAdd = new Key(Keyboard.KEY_ADD);
	public Key keySubstract = new Key(Keyboard.KEY_SUBTRACT);
	public Key keySpace = new Key(Keyboard.KEY_SPACE);
	public Key KeyReturn = new Key(Keyboard.KEY_RETURN);

	public boolean fullscreen = true;
	public Canvas canvas;

	public Game() {
		this.startFullscreen();
	}

	public Game(int width, int height) {
		this.fullscreen = false;
		this.initCanvas(width, height);
	}

	public Camera getCamera() {
		return cam;
	}

	public void checkEscape() {
		if (KeyInput.get().isKeyDown(KeyInput.KEY_ESCAPE)) {
			if (fps != null) {

			}
			System.exit(0);

		}
	}

	private void startFullscreen() {

		// PropertiesIO properties = new PropertiesIO("properties.cfg");
		// properties.load();

		displaySystem = DisplaySystem.getDisplaySystem();
		GraphicsParam gp = GraphicsParam.gp;

		displaySystem.createWindow(gp.displayMode.getWidth(), gp.displayMode.getHeight(),
				gp.displayMode.getBitsPerPixel(), gp.displayMode.getFrequency(), Game.pleineEcran);

		// displaySystem.switchContext(displaySystem);
		cam = displaySystem.getRenderer().createCamera(displaySystem.getWidth(), displaySystem.getHeight());
		// displaySystem.getRenderer().setBackgroundColor(ColorRGBA.yellow.clone());
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
		System.out.println("GL_VERSION=" + GL11.glGetString(GL11.GL_VERSION));
		this.initCamera();
		displaySystem.getRenderer().setCamera(cam);
		KeyInput.setProvider(InputSystem.INPUT_SYSTEM_LWJGL);
		MouseInput.setProvider(InputSystem.INPUT_SYSTEM_LWJGL);
		// vs = new VoxelShader();

		this.initShader();

	}

	public void initShader() {
		initMat();
		vsDepth = new VoxelShaderDepthArray();
		vsShadow = new VoxelShaderShadowArray();

		vsDepthPlanche = new VoxelShaderPlancheDepthArray();
		vsShadowPlanche = new VoxelShaderPlancheShadowArray();

		vs = vsShadow;
		shaderSoleil = new Shader(Shader.class, "soleil.frag", "soleil.vert", null);
		shaderLumiere = new Shader(Shader.class, "lumiere.frag", "lumiere.vert", null);
		shaderWidget = new Shader(Shader.class, "widget.frag", "widget.vert", null);
		shaderParticule = new Shader(Shader.class, "particule.frag", "particule.vert", null);
		shaderParticuleDevant = new Shader(Shader.class, "particuleDevant.frag", "particuleDevant.vert", null);
		shaderBase = new Shader(Shader.class, "base.frag", "base.vert", null);
		shaderSkyBox = new Shader(Shader.class, "skybox.frag", "skybox.vert", null);

		shaderTerrain = new Shader(Shader.class,"terrain.frag","terrain.vert",null);
		shaderWorldBox = new Shader(Shader.class, "shader_grille.frag", "shader_grille.vert", null);
		shaderWorldBoxDepth = new Shader(Shader.class, "texture_array/depth/shader_grille.frag",
				"texture_array/depth/shader_grille.vert", null);
		shaderWorldBoxVisibilite = new Shader(Shader.class, "visibilite.frag", "visibilite.vert", null);
		shaderSansVBO = new Shader(Shader.class, "base_sans_vbo.frag", "base_sans_vbo.vert", null);
		shaderNormalAvecRond = new Shader(Shader.class, "differe/differe_avec_rond.frag",
				"differe/differe_avec_rond.vert", null);
		shaderNormalAvecDivisionEcran =new Shader(Shader.class, "differe/differe_division_ecran.frag",
				"differe/differe_avec_rond.vert", null);

	}

	private void initCanvas(int width, int height) {

		displaySystem = DisplaySystem.getDisplaySystem();

		canvas = displaySystem.createCanvas(width, height);
	}

	public Camera createCamera(int width, int height, boolean parallelProjection) {
		Camera nc = displaySystem.getRenderer().createCamera(width, height);
		if (parallelProjection) {
			nc.setParallelProjection(true);
			nc.setFrustum(1, 6000, -256, 256, -256, 256);
		} else {
			nc.setFrustumPerspective(45.0f, (float) displaySystem.getWidth() / (float) displaySystem.getHeight(), 1,
					3000);
			nc.setParallelProjection(false);
		}
		Vector3f loc = new Vector3f(0.0f, 0.0f, 25.0f);
		Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
		/** Move our camera to a correct place and orientation. */
		nc.setFrame(loc, left, up, dir);
		/** Signal that we've changed our camera's location/frustum. */
		nc.apply();
		nc.update();
		return nc;
	}

	public void startCanvas(int width, int height) {

		displaySystem.initForCanvas(width, height);

		// displaySystem.switchContext(displaySystem);
		cam = displaySystem.getRenderer().createCamera(width, height);

		// displaySystem.getRenderer().setBackgroundColor(ColorRGBA.yellow.clone());
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
		System.out.println("GL_VERSION=" + GL11.glGetString(GL11.GL_VERSION));
		this.initCamera();
		displaySystem.getRenderer().setCamera(cam);
		KeyInput.setProvider(InputSystem.INPUT_SYSTEM_LWJGL);
		MouseInput.setProvider(InputSystem.INPUT_SYSTEM_LWJGL);
		// vs = new VoxelShader();

		initShader();

	}

	public void resize(int width, int height) {
		this.getCamera().resize(width, height);
		displaySystem.getRenderer().reinit(width, height);
	}

	public int getWidth() {
		return displaySystem.getWidth();
	}

	public int getHeight() {
		return displaySystem.getHeight();
	}

	public IHM nouvelleIHM() {
		IHM ihm = new IHM();
		ihm.time = System.currentTimeMillis();
		ihm.screenWidth = displaySystem.getWidth();
		ihm.screenHeight = displaySystem.getHeight();
		ihm.shaderWidget = this.shaderWidget;
		return ihm;
	}

	public VoxelShaderParam createVoxelShaderParam() {
		VoxelShaderParam vsp = new VoxelShaderParam();

		return vsp;
	}

	public VoxelShaderParam createVoxelShaderParam(Habillage habillage) {
		VoxelShaderParam vsp = new VoxelShaderParam();

		vsp.habillage = habillage;
		return vsp;
	}

	public void initCamera() {
		cam.setFrustumPerspective(45.0f, (float) displaySystem.getWidth() / (float) displaySystem.getHeight(), 1, 1000);
		cam.setParallelProjection(false);
		Vector3f loc = new Vector3f(0.0f, 0.0f, 25.0f);
		Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
		/** Move our camera to a correct place and orientation. */
		cam.setFrame(loc, left, up, dir);
		/** Signal that we've changed our camera's location/frustum. */
		cam.update();
		// cam.setViewPort(-0.5f, 0.5f, -0.5f, 0.5f);
		/** Assign the camera to this renderer. */
		displaySystem.getRenderer().setCamera(cam);
		cam.update();
		GL11.glShadeModel(GL11.GL_FLAT); // Enables Smooth Shading
		// GL11.glClearColor(0.0f, 1.0f, 0.0f, 0.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL12.GL_TEXTURE_3D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT2);
		lightPos = BufferUtils.createFloatBuffer(4);
		lightPos2 = BufferUtils.createFloatBuffer(4);

	}

	FloatBuffer lightPos;
	FloatBuffer lightPos2;

	public void setLightDirection(int light, float x, float y, float z) {

		GL11.glEnable(light);
		lightPos.clear();
		lightPos.put(0, x);
		lightPos.put(1, y);
		lightPos.put(2, z);
		// lightPos.put(3, 1);
		GL11.glLight(light, GL11.GL_SPOT_DIRECTION, lightPos);

	}

	public void setLight0Pos(float x, float y, float z) {
		GL11.glEnable(GL11.GL_LIGHT0);
		lightPos.clear();
		lightPos.put(0, x);
		lightPos.put(1, y);
		lightPos.put(2, z);
		lightPos.put(3, 1);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPos);

	}

	public void setLight2Pos(float x, float y, float z) {
		GL11.glEnable(GL11.GL_LIGHT2);
		lightPos.clear();
		lightPos.put(0, x);
		lightPos.put(1, y);
		lightPos.put(2, z);
		lightPos.put(3, 1);
		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_POSITION, lightPos);

	}

	public void setLight2Direction(float x, float y, float z) {
		GL11.glEnable(GL11.GL_LIGHT2);
		lightPos2.clear();
		lightPos2.put(0, x);
		lightPos2.put(1, y);
		lightPos2.put(2, z);
		// lightPos2.put(3, 0);
		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_SPOT_DIRECTION, lightPos2);

	}

	public void setLight1Direction(float x, float y, float z) {
		GL11.glEnable(GL11.GL_LIGHT1);
		lightPos.clear();
		lightPos.put(0, x);
		lightPos.put(1, y);
		lightPos.put(2, z);
		// lightPos.put(3, 1);
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPOT_DIRECTION, lightPos);

	}

	public void setLight1Pos(float x, float y, float z) {
		GL11.glEnable(GL11.GL_LIGHT1);
		lightPos.clear();
		lightPos.put(0, x);
		lightPos.put(1, y);
		lightPos.put(2, z);
		lightPos.put(3, 1);
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPos);

	}

	public void clear() {
		// displaySystem.switchContext(displaySystem);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

	}
	
	public boolean isClosed() {
		if (fps == null) {

			fps = new Fps();
		}

		return displaySystem.isClosing();

	}

	public void displayBackBuffer() {
		GL11.glFlush();

	}

}
