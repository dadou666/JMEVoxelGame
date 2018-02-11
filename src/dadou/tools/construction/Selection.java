package dadou.tools.construction;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.BriqueAvecTexture3D;
import dadou.CameraPosition;
import dadou.Kube;
import dadou.Log;
import dadou.Lumiere;
import dadou.ImageEcran;
import dadou.Objet3D;
import dadou.ObjetMobile;
import dadou.OctreeActionDecor;
import dadou.Shader;
import dadou.VBOTexture2D;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.greffon.FormeAvecMin;
import dadou.tools.BrickEditor;
import dadou.tools.EtatBrickEditor;

public class Selection extends EtatBrickEditor {

	VBOTexture2D selection;

	public Objet3D camSelection;
	public Objet3D objetSelection;
	public VBOTexture2D boxObjetSelection;
	public int idxObjetSelection = 0;

	public enum Axe {
		X, Y, Z
	};

	public Axe axe;

	public BoxRemplissage BoxRemplissage;
	public BoxCopie BoxCopie;
	public BoxSelectionAction BoxSelectionAction;
	public BoxColler BoxColler;
	public boolean dessinerLigne = false;

	public EtatBoxSelection EtatBoxSelection = new NoBoxSelection();

	VBOTexture2D boxSelection;

	public Objet3D boxCamSelection;

	float dx = 1;
	float dy = 1;
	float dz = 1;
	public int x = 0;
	public int y = 0;
	public int z = 0;
	Color oldColor;
	Shader shader;

	public void setPosition(Vector3f pos) {
		EtatBoxSelection.setPosition(this, pos);
	}

	public void setBlock(Vector3f pos, Color color) throws CouleurErreur {
		EtatBoxSelection.setBlock(this, pos, color);

	}

	public void setBoxSelection(boolean flag) {
		if (flag) {
			EtatBoxSelection = new DebutBoxSelection();
		} else {
			EtatBoxSelection = new NoBoxSelection();
		}

	}

	public Selection(BrickEditor b, int ix, int iy, int iz) {
		this.BrickEditor = b;
		this.x = ix;
		this.y = iy;
		this.z = iz;
		shader = new Shader(Shader.class, "box.frag", "box.vert", null);
		selection = this.createSelectionBox();
		camSelection = new Objet3D();
		camSelection.brique = selection;
		boxCamSelection = new Objet3D();
		boxSelection = this.createSelectionBox();
		boxCamSelection.brique = boxSelection;
		boxObjetSelection = this.createSelectionBox();
		objetSelection = new Objet3D();
		objetSelection.brique = boxObjetSelection;

		BoxRemplissage = new BoxRemplissage();
		BoxCopie = new BoxCopie();
		BoxColler = new BoxColler();
		BoxRemplissage.editor = b;
		BoxCopie.editor = b;
		BoxColler.editor = b;

	}

	public void dessiner(Camera cam) {
		EtatBoxSelection.dessiner(this, cam);
		this.dessinerObjetSelection(cam);
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

	Kube cube = new Kube();
	float selDist = 10;
	Vector3f selPos = new Vector3f();
	Vector2f screenPos = new Vector2f();
	public boolean cameraTrajectoire = false;
	public boolean modifierValeurGreffon = false;

	public void gererMouseWheel() throws CouleurErreur {
		BrickEditor.gererCamera();
		if (modifierValeurGreffon) {
			int wheel = Mouse.getDWheel();
			if (wheel < 0) {
				FormeAvecMin.min -= 0.05f;
			}
			if (wheel > 0) {
				FormeAvecMin.min += 0.05f;
			}
			FormeAvecMin.min = Math.max(0.0f, Math.min(FormeAvecMin.min, 1.0f));
			return;
		}
		if (!this.menuAxeSelected()) {
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

			if (BrickEditor.keyMoins.isPressed()) {
				if (BrickEditor.lumiereModification != null) {
					BrickEditor.lumiereModification.rayon -= 0.5f;
					BrickEditor.lumiereModification.reset();
					BrickEditor.decor.ajouterLumieres();
				}
			}
			if (BrickEditor.keyPlus.isPressed()) {
				if (BrickEditor.lumiereModification != null) {
					BrickEditor.lumiereModification.rayon += 0.5f;
					BrickEditor.lumiereModification.reset();
					BrickEditor.decor.ajouterLumieres();
				}
			}
		} else {
			Vector3f N = axe();
			if (BrickEditor.keyMoins.isPressed()) {
				if (BrickEditor.lumiereModification != null) {
					BrickEditor.lumiereModification.pos.subtractLocal(
							N.x / 2.0f, N.y / 2.0f, N.z / 2.0f);
					BrickEditor.decor.ajouterLumieres();
				} else {
					selPos.subtractLocal(N);
				}
			}
			if (BrickEditor.keyPlus.isPressed()) {
				if (BrickEditor.lumiereModification != null) {
					BrickEditor.lumiereModification.pos.addLocal(N.x / 2.0f,
							N.y / 2.0f, N.z / 2.0f);
					BrickEditor.decor.ajouterLumieres();
				} else {
					selPos.addLocal(N);
				}
			}
		}
		if (BrickEditor.keyReturn.isPressed()
				&& BrickEditor.lumiereModification != null) {
			BrickEditor.decor.DecorDeBriqueData.lumieres
					.remove(BrickEditor.lumiereModification);
			BrickEditor.lumiereModification = null;
			BrickEditor.decor.ajouterLumieres();
		}
		setPosition(selPos);

	}

	public Vector3f axe() {
		if (axe == Axe.X) {
			return Vector3f.UNIT_X;
		}
		if (axe == Axe.Y) {
			return Vector3f.UNIT_Y;
		}
		if (axe == Axe.Z) {
			return Vector3f.UNIT_Z;
		}
		return null;
	}

	public boolean menuAxeSelected() {

		if (BrickEditor.menuAxeX.selected) {
			return true;
		}
		if (BrickEditor.menuAxeY.selected) {
			return true;
		}
		if (BrickEditor.menuAxeZ.selected) {
			return true;
		}
		return false;

	}

	public boolean placerLumiere = false;

	public boolean formeSelected() {

		return false;
	}

	public void modifierTailleForme(int d) {
		BrickEditor.canon.tailleObus += d;
		BrickEditor.setText(BrickEditor.tailleObus, ""
				+ BrickEditor.canon.tailleObus);
	}

	public void gerer() throws CouleurErreur {

		this.gererMouseWheel();
		if (BrickEditor.b.isPressed()) {
			if (!placerLumiere) {
			try {	setBlock(selPos, BrickEditor.kubeCourant()); }catch(CouleurErreur ce) {
				Log.print(" erreur");
			}
			} else if (BrickEditor.lumiereModification == null) {
				Lumiere l = new Lumiere();
				l.pos.set(selPos);
				l.rayon = 3.0f;
				if (BrickEditor.decor.DecorDeBriqueData.lumieres == null) {
					BrickEditor.decor.DecorDeBriqueData.lumieres = new ArrayList<>();
				}
				BrickEditor.decor.DecorDeBriqueData.lumieres.add(l);
				BrickEditor.decor.ajouterLumieres();

			}
		}
		if (!this.menuAxeSelected()) {
			if (this.formeSelected()) {
				if (BrickEditor.keyMoins.isPressed()) {
					this.modifierTailleForme(-1);
				}
				if (BrickEditor.keyPlus.isPressed()) {
					this.modifierTailleForme(1);
				}
			} else {
				if (BrickEditor.keyMoins.isPressed()) {
					this.idxObjetSelection = Math.max(0, idxObjetSelection - 1);
				}
				if (BrickEditor.keyPlus.isPressed()) {
					this.idxObjetSelection++;
				}
			}
		}
		if (!this.cameraTrajectoire) {
			if (BrickEditor.keyRShift.isPressed()) {
				if (BrickEditor.arbreCameraPositions.nomGroupeEnCours != null) {
					String groupe = BrickEditor.arbreCameraPositions.nomGroupeEnCours;
					CameraPosition cp = BrickEditor.scc.creerCameraPosition();
					cp.groupe = groupe;
					BrickEditor.decor.DecorDeBriqueData.ajouterCameraPosition(
							null, cp);
					BrickEditor.swingEditor.reloadTree();
				}

			}
			if (BrickEditor.keyLShift.isPressed()) {
				if (BrickEditor.arbreCameraPositions.nomGroupeEnCours != null) {
					String groupe = BrickEditor.arbreCameraPositions.nomGroupeEnCours;
					CameraPosition cp = new CameraPosition();
					cp.groupe = groupe;
					cp.rotationX.loadIdentity();
					;
					cp.rotationY.loadIdentity();
					;
					cp.translation.set(camSelection.getTranslationGlobal());
					BrickEditor.decor.DecorDeBriqueData.ajouterCameraPosition(
							null, cp);
					BrickEditor.swingEditor.reloadTree();
				}

			}
		}
		if (BrickEditor.keySpace.isPressed()) {
			String groupe = BrickEditor.arbreCameraPositions.nomGroupeEnCours;
			if (groupe != null) {
				this.cameraTrajectoire = !this.cameraTrajectoire;
				System.out.println("Camera trajectoire [" + groupe + "]="
						+ this.cameraTrajectoire);
				if (this.cameraTrajectoire) {
					BrickEditor.decor.DecorDeBriqueData
							.supprimerGroupeCameraPosition(groupe);
				} else {
					BrickEditor.swingEditor.reloadTree();
				}
			}

		}
		if (this.cameraTrajectoire) {
			if (BrickEditor.arbreCameraPositions.nomGroupeEnCours != null) {
				String groupe = BrickEditor.arbreCameraPositions.nomGroupeEnCours;
				CameraPosition cp = BrickEditor.scc.creerCameraPosition();
				cp.groupe = groupe;
				BrickEditor.decor.DecorDeBriqueData.ajouterCameraPosition(null,
						cp);
			}

		}
		if (BrickEditor.keyReturn.isPressed()) {
			BrickEditor.swingEditor.selectModelInstance(
					BrickEditor.decorDeBriqueData.mondeCourant,
					BrickEditor.objetCourant.model.modelClasse.nomModele(),
					BrickEditor.modelName);
		}

	}

	public void echanger() {

	}

	public void dessinerObjetSelection(Camera cam) {
		OctreeActionDecor action = this.BrickEditor.decor.action;
		if (action.objetMobilesVisible.size() == 0) {
			this.BrickEditor.initCurrentModelName((ObjetMobile) null);
			return;

		}
		// action.trierObjetMobile(cam);
		this.idxObjetSelection = Math.min(idxObjetSelection,
				action.objetMobilesVisible.size() - 1);

		ObjetMobile om = action.objetMobilesVisible.get(0);
		if (om.model != null && om.model.rotation != null) {
			objetSelection.setRotation(om.model.rotation);
		}
		if (this.BrickEditor.swingEditor != null) {
			om.dessinerBox(cam, objetSelection, boxObjetSelection);
		}
		this.BrickEditor.initCurrentModelName(om);

	}

}
