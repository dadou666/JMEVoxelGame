package dadou;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.collision.GestionCollision;

public class OcclusionVolume {
	OrientedBoundingBoxWithVBO volume;
	OcclusionVolume childs[];
	float step;
	float profondeur = 0.0f;

	public void init(OcclusionVolume parent, float x, float y, float extentX, float extentY, float step) {
		Vector3f dirX = parent.volume.getXAxis();
		Vector3f dirY = parent.volume.getYAxis();
		this.volume = new OrientedBoundingBoxWithVBO();
		Vector3f newCenter = this.volume.getCenter();
		newCenter.set(parent.volume.getCenter());
		newCenter.addLocal(dirX.x * x * extentX, dirX.y * x * extentX, dirX.z * x * extentX);
		newCenter.addLocal(dirY.x * x * extentX, dirY.y * x * extentX, dirY.z * x * extentX);

		this.volume.setXAxis(parent.volume.getXAxis());
		this.volume.setYAxis(parent.volume.getYAxis());
		this.volume.setZAxis(parent.volume.getZAxis());

		this.volume.getExtent().x = extentX;
		this.volume.getExtent().y = extentY;
		this.volume.getExtent().z = parent.volume.getExtent().z;
		this.step = step;
	}

	public OcclusionVolume() {
		this.volume = new OrientedBoundingBoxWithVBO();
	}

	Vector3f tmpA = new Vector3f();
	Vector3f tmpB = new Vector3f();
	Vector2f screenPos = new Vector2f();

	public void init(Camera cam, float screenWidth, float screenHeight, float step) {

		screenPos.set(screenWidth / 2, 0);

		tmpA.set(cam.getWorldCoordinates(screenPos, 0));
		screenPos.set(0, 0);
		tmpB.set(cam.getWorldCoordinates(screenPos, 0));
		tmpA.subtractLocal(tmpB);
		float extentX = tmpA.length();

		screenPos.set(0, screenHeight / 2);

		tmpA.set(cam.getWorldCoordinates(screenPos, 0));
		screenPos.set(0, 0);
		tmpB.set(cam.getWorldCoordinates(screenPos, 0));
		tmpA.subtractLocal(tmpB);
		float extentY = tmpA.length();

		screenPos.set(screenWidth / 2, screenHeight / 2);
		tmpA.set(cam.getWorldCoordinates(screenPos, 0));

		volume.getCenter().set(tmpA);

		volume.getExtent().set(extentX, extentY, step / 2.0f);
		volume.getXAxis().set(cam.getLeft());
		volume.getYAxis().set(cam.getUp());
		volume.getZAxis().set(cam.getDirection());

	}

	static float plusMinus[] = new float[] { -1.0f, 1.0f };

	public void compute(float profondeurMax, int niveau, GestionCollision gc) {
		while (profondeur < profondeurMax) {
			if (gc.verifierCollision(volume)) {
				if (niveau == 0) {
					return;
				}
				if (childs == null) {
					childs = new OcclusionVolume[4];
				}
				int idx = 0;
				float extentX = volume.extent.x / 2.0f;
				float extentY = volume.extent.y / 2.0f;
				for (float x : plusMinus) {
					for (float y : plusMinus) {
						if (childs[idx] == null) {

						}
						childs[idx] = new OcclusionVolume();
						childs[idx].init(this, x, y, extentX, extentY, step);

						childs[idx].compute(profondeur, niveau - 1, gc);

					}

				}

				return;
			}
			Vector3f dirZ = volume.getZAxis();
			volume.getCenter().addLocal(dirZ.x * step, dirZ.y * step, dirZ.z * step);
			profondeur += step;

		}

	}

}
