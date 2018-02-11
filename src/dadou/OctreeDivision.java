package dadou;

import java.util.List;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;

import dadou.collision.EtatOctree;

public class OctreeDivision {
	BoundingBox[] children;
	BoundingBox box;
	float dim;
	int level;

	OctreeDivision() {
		box = new BoundingBox();
		children = new BoundingBox[8];
		for (int i = 0; i < children.length; i++) {
			children[i] = new BoundingBox();
		}

	}

	public void init(Octree<EtatOctree> o) {
		box.getCenter().set(o.box.getCenter());
		level = o.level;

		box.xExtent = o.box.xExtent;
		box.yExtent = o.box.yExtent;
		box.zExtent = o.box.zExtent;

		for (int i = 0; i < children.length; i++) {
			children[i].getCenter().set(o.children[i].box.getCenter());
			children[i].xExtent = o.children[i].box.xExtent;
			children[i].yExtent = o.children[i].box.yExtent;
			children[i].zExtent = o.children[i].box.zExtent;
			
		}

	}

	public void init() {
		if (level == 0) {
			return;
		}
		float u = box.xExtent / 2;

		float tmp[] = new float[] { u, -u };
		Vector3f posBox = new Vector3f();
		Vector3f center = box.getCenter();
		if (children != null) {
			children = new BoundingBox[8];
		}
		for (float dx : tmp) {
			for (float dy : tmp) {
				for (float dz : tmp) {

					posBox.set(center);
					posBox.x += dx;
					posBox.y += dy;
					posBox.z += dz;
					int idx = getIdxForPos(posBox.x, posBox.y, posBox.z);
					children[idx] = new BoundingBox(posBox, dim / 2.0f, dim / 2.0f, dim / 2.0f);

				}
			}

		}

	}

	public int getIdxForPos(float x, float y, float z) {
		int idx = 0;
		Vector3f center = box.getCenter();
		if (x >= center.x) {
			idx += 1;
		}
		if (y >= center.y) {
			idx += 2;
		}
		if (z >= center.z) {
			idx += 4;
		}
		return idx;
	}

	public void calculer(BoundingSphere bs, List<BoundingBox> list) {
		if (level == 0 && bs.intersects(box)) {
			BoundingBox b = new BoundingBox(box.getCenter(), box.xExtent, box.yExtent, box.zExtent);
			list.add(b);
			return;
		}
		float px = box.getCenter().x;
		float py = box.getCenter().y;
		float pz = box.getCenter().z;
		float xExtent = box.xExtent;
		float yExtent = box.yExtent;
		float zExtent = box.zExtent;
		int tmp = level;
		for (BoundingBox bb : children) {

			if (bb.intersects(bs)) {
				this.box.getCenter().set(bb.getCenter());
				this.box.xExtent = bb.xExtent;
				this.box.yExtent = bb.yExtent;
				this.box.zExtent = bb.zExtent;
				this.level--;
				this.init();
				this.calculer(bs, list);

			}
			level = tmp;
			box.xExtent = xExtent;
			box.yExtent = yExtent;
			box.zExtent = zExtent;
			box.getCenter().set(px, py, pz);
			this.init();

		}

	}

}
