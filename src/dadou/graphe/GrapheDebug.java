package dadou.graphe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.jme.math.Vector3f;

import dadou.CameraPosition;
import dadou.Game;
import dadou.Shader;
import dadou.tools.BrickEditor;

public class GrapheDebug {

	public Graphe graphe;
	public Shader shader;
	public List<GrapheLigne> lignes = new ArrayList<>();

	public GrapheDebug(List<GrapheLigne> lignes) {
		shader =Game.shaderSansVBO;
		this.lignes = lignes;
	}



	public GrapheDebug(BrickEditor be, Graphe graphe) {
		shader =Game.shaderSansVBO;
		Map<String, Vector3f> positions = new HashMap<>();
		for (Map.Entry<String, CameraPosition> e : be.decor.DecorDeBriqueData.cameraPositions.entrySet()) {
			positions.put(e.getKey(), e.getValue().translation);
		}
		if (!graphe.racines.isEmpty()) {
			for (GrapheElement ge : graphe.racines) {
				for (GrapheElementRef ger : ge.suivants) {
					if (ger.poid > 0) {
						GrapheLigne gl = new GrapheLigne(positions.get(ge.nom), positions.get(ger.nom));
						lignes.add(gl);
					}

				}
			}

		} else {
			/*
			 * for (GrapheElementRef ger : graphe.racine.suivants) { if
			 * (ger.poid > 0) { GrapheLigne gl = new GrapheLigne(
			 * positions.get(graphe.racine.nom), positions.get(ger.nom));
			 * lignes.add(gl); }
			 * 
			 * }
			 */}
		for (GrapheElement ge : graphe.elements.values()) {
			for (GrapheElementRef ger : ge.suivants) {
				if (ger.poid > 0) {
					GrapheLigne gl = new GrapheLigne(positions.get(ge.nom), positions.get(ger.nom));
					gl.fermeture = ger.fermeture;
					lignes.add(gl);
				}

			}
		}

		this.graphe = graphe;

	}

	public void dessiner() {

		GL11.glPushMatrix();
		// GL11.glLoadIdentity();
		shader.use();

		// shader.use();
		for (GrapheLigne gl : lignes) {

			shader.glUniformfARB("color", 1.0f, 0.0f, 1.0f, 0.0f);

			GL11.glLineWidth(3.0f);
			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex3f(gl.debut.x, gl.debut.y, gl.debut.z);

			GL11.glVertex3f(gl.fin.x, gl.fin.y, gl.fin.z);
			GL11.glEnd();

		}
		// this.dessiner(graphe.racine);

		GL11.glPopMatrix();

	}

}
