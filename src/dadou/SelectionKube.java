package dadou;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;
import dadou.tools.construction.Selection;

public class SelectionKube {
	Vector3f selPos = new Vector3f();
	float selDist = 10;
	Vector2f screenPos = new Vector2f();
	BrickEditor BrickEditor;
	public Objet3D camSelection;
	Shader shader;
	VBOTexture2D selection;
	public int x;
	public int y;
	public int z;

	public SelectionKube(BrickEditor be) {
		this.BrickEditor = be;
		shader = new Shader(Shader.class, "box.frag", "box.vert", null);
		selection = this.createSelectionBox();

		camSelection = new Objet3D();
		camSelection.brique = selection;

	}

	public void gererMouseWheel() throws CouleurErreur {

		int wheel = Mouse.getDWheel();
		if (wheel < 0) {
			selDist -= 1.0f;
		}
		if (wheel > 0) {
			selDist += 1.0f;
		}
		int x = Mouse.getX();
		int y = Mouse.getY();

		screenPos.set(x, y);
		Camera cam = BrickEditor.game.getCamera();

		selPos.set(cam.getWorldCoordinates(screenPos, 1));
		selPos.subtractLocal(cam.getWorldCoordinates(screenPos, 0));
		selPos.normalizeLocal();
		selPos.multLocal(selDist);
		selPos.addLocal(cam.getLocation());
		this.setPosition();

	}

	public VBOTexture2D createSelectionBox() {

		VBOTexture2D worldBox;
		worldBox = new VBOTexture2D(shader);
		int i = 0;

		worldBox.addNormal(0, 0, 1);
		worldBox.addVertex(0, 0, 0);// 0

		worldBox.addNormal(0, 0, -1);
		worldBox.addVertex(0, 1, 0);// 1

		worldBox.addNormal(0, 0, -1);
		worldBox.addVertex(1, 1, 0);// 2

		worldBox.addNormal(0, 0, -1);
		worldBox.addVertex(1, 0, 0);// 3
		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		worldBox.addNormal(0, 0, 1);
		worldBox.addVertex(0, 0, 1);// 0

		worldBox.addNormal(0, 0, 1);
		worldBox.addVertex(0, 1, 1);// 1

		worldBox.addNormal(0, 0, 1);
		worldBox.addVertex(1, 1, 1);// 2

		worldBox.addNormal(0, 0, 1);
		worldBox.addVertex(1, 0, 1);// 3
		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(0, 0, 0);// 0

		worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(0, 0, 1);// 1

		worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(0, 1, 1);// 2

		worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(0, 1, 0);// 3

		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(1, 0, 0);// 0

		worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(1, 0, 1);// 1

		worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(1, 1, 1);// 2

		worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(1, 1, 0);// 3
		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		worldBox.addNormal(0, -1, 0);
		worldBox.addVertex(0, 0, 0);// 0

		worldBox.addNormal(0, -1, 0);
		worldBox.addVertex(0, 0, 1);// 1

		worldBox.addNormal(0, -1, 0);
		worldBox.addVertex(1, 0, 1);// 2

		worldBox.addNormal(0, -1, 0);
		worldBox.addVertex(1, 0, 0);// 3
		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		worldBox.addNormal(0, 1, 0);
		worldBox.addVertex(0, 1, 0);// 0

		worldBox.addNormal(0, 1, 0);
		worldBox.addVertex(0, 1, 1);// 1

		worldBox.addNormal(0, 1, 0);
		worldBox.addVertex(1, 1, 1);// 2

		worldBox.addNormal(0, 1, 0);
		worldBox.addVertex(1, 1, 0);// 3

		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		worldBox.createVBO();
		return worldBox;

		// shader.use();
		// shader.glUniformfARB("size", dim);
		// shader.glUniformfARB("color", 1, 0, 0, 0);

	}

	public void setPosition() {
		camSelection.positionToZero();

		int dim = BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int dx = dim / 2;
		int dy = dim / 2;
		int dz = dim / 2;
		x = (int) (selPos.x + dx) - (dx);
		y = (int) (selPos.y + dy) - (dy);
		z = (int) (selPos.z + dz) - (dz);

		camSelection.translation(x, y, z);
		x+=dx;
		y+=dy;
		z+=dz;
	}
	public Color color;

	public void dessiner() {
		if (color == null) {
			return;
		}
		shader.use();
		shader.glUniformfARB("dx", 1);
		shader.glUniformfARB("dy", 1);
		shader.glUniformfARB("dz", 1);
		// selection.shader.glUniformfARB("size", 1);

		shader.glUniformfARB("color", color.getRed(), color.getGreen(), color.getBlue(), 0.0f);
		camSelection.dessiner(BrickEditor.game.getCamera());
	}

}
