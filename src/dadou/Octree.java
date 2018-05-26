package dadou;

import java.io.Serializable;
import java.util.List;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.Plane;
import com.jme.math.Vector3f;
import com.jme.renderer.AbstractCamera;
import com.jme.renderer.Camera;

public class Octree<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8217058298599474763L;
	public final static int[] frustumPlanes = new int[] {
			AbstractCamera.NEAR_PLANE, AbstractCamera.FAR_PLANE,
			AbstractCamera.BOTTOM_PLANE, AbstractCamera.TOP_PLANE,
			AbstractCamera.LEFT_PLANE, AbstractCamera.RIGHT_PLANE, };
	public Octree<T>[] children;
	public T value;
	public BoundingBox box;
	public int level;
	public float d;
	public float dCulling;
	public Octree<T> parent;
	public float dim;
	transient public Object mark;

	public Octree(Vector3f pos, int level, float d) {
		dim = (float) (d * Math.pow(2, level - 1));
		box = new BoundingBox(pos, dim, dim, dim);
		this.level = level;
		this.d = d;
		dCulling = (float) (Math.sqrt(2.0f) * dim);
	}

	public void initRec() {
		if (level == 0) {
			return;
		}
		float u = box.xExtent / 2;

		this.init();
		for (Octree o : children) {
			o.initRec();
		}

	}

	public void visit(OctreeVisitor<T> visitor) {
		if (this.box.intersects(visitor.getBoudingVolume())) {
			if (this.value != null) {
				visitor.execute(this);
			}
		}
		if (children == null) {
			return;
		}
		for (Octree<T> o : this.children) {
			o.visit(visitor);

		}

	}

	public BoundingSphere bs = new BoundingSphere();

	public boolean collision(Vector3f pos, float rayon) {
		bs.setCenter(pos);
		bs.setRadius(rayon);
		return box.intersects(bs);
	}

	public void init() {
		if (level == 0) {
			return;
		}
		float u = box.xExtent / 2;

		float tmp[] = new float[] { u, -u };
		Vector3f posBox = new Vector3f();
		Vector3f center = box.getCenter();
		children = new Octree[8];
		for (float dx : tmp) {
			for (float dy : tmp) {
				for (float dz : tmp) {

					posBox.set(center);
					posBox.x += dx;
					posBox.y += dy;
					posBox.z += dz;
					int idx = getIdxForPos(posBox);
					children[idx] = new Octree(posBox, level - 1, d);
					children[idx].parent = this;

				}
			}

		}

	}

	public void initAll() {
		children = new Octree[8];

	}

	public void getOctreeForList(OrientedBoundingBox box, List<T> ls) {
		if (box.intersects(box)) {
			if (this.value != null  ) {
				ls.add(this.value);

			}
			if (children == null) {
				return;
			}
			for (Octree<T> o : children) {
				o.getOctreeForList(box, ls);
			}
		}

	}

	public Octree<T> getOctreeFor(OrientedBoundingBox box) {

		Octree<T> r = null;
		for (Octree<T> o : children) {
			if (o!=null && o.box.intersects(box)) {
				if (r != null) {
					return this;
				}
				r = o;
			}

		}
		return r;

		// return this.getOctreeForMinMax(box.min, box.max);

	}

	public Octree<T> getOctreeFor(BoundingBox box) {

		Octree<T> r = null;
		for (Octree<T> o : children) {
			if (o.box.intersects(box)) {
				if (r != null) {
					return this;
				}
				r = o;
			}

		}
		return r;

		// return this.getOctreeForMinMax(box.min, box.max);

	}

	public Octree<T> getOctreeForMinMax(Vector3f min, Vector3f max) {
		int idx = 0;
		Vector3f center = box.getCenter();
		if (center.x <= max.x && center.x >= min.x) {
			return this;
		}
		if (center.y <= max.y && center.y >= min.y) {
			return this;
		}

		if (center.z <= max.z && center.z >= min.z) {
			return this;
		}
		if (min.x >= center.x) {
			idx += 1;
		} else {
			if (max.x > center.x) {
				return this;
			}
		}
		if (min.y >= center.y) {
			idx += 2;
		} else {
			if (max.y > center.y) {
				return this;
			}
		}
		if (min.z >= center.z) {
			idx += 4;
		} else {
			if (max.z > center.z) {
				return this;
			}
		}
		return children[idx];
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

	public Octree<T> getOctreeFor(Vector3f pos, float rayon) {
		return this.getOctreeFor(pos.x, pos.y, pos.z, rayon);
	}

	public Octree<T> getOctreeFor(BoundingSphere bs) {
		Octree<T> r = null;
		for (Octree<T> child : children) {
			
			if (child != null && child.box.intersects(bs)) {
				if (r != null) {
					return this;
				}
				r = child;
			}
		}
		return r;

	}

	public Octree<T> getOctreeFor(float x, float y, float z, float rayon) {
		int idx = 0;
		Vector3f center = box.getCenter();
		if (x - rayon >= center.x) {
			idx += 1;
		} else if (x + rayon > center.x) {
			return this;

		}
		if (y - rayon >= center.y) {
			idx += 2;
		} else if (y + rayon > center.y) {
			return this;
		}
		if (z - rayon >= center.z) {
			idx += 4;
		} else if (z + rayon > center.z) {
			return this;
		}
		return this.children[idx];
	}

	public int getIdxForPos(Vector3f pos) {
		return this.getIdxForPos(pos.x, pos.y, pos.z);
	}

	public Octree<T> getForPos(Vector3f pos) {
		if (children == null) {
			return null;

		}

		return children[this.getIdxForPos(pos)];

	}

	public Octree<T> getForLeafPos(Vector3f pos) {
		Octree<T> r = this.getForPos(pos);
		if (r == null) {
			return this;
		}

		return r.getForLeafPos(pos);

	}

	public Octree<T> createOctreeFor(Vector3f pos) {
		if (level == 0) {
			return this;
		}
		if (children == null) {
			children = new Octree[8];

		}
		int idx = this.getIdxForPos(pos);
		Octree<T> r = children[idx];
		if (r == null) {
			Vector3f posBox = new Vector3f();
			Vector3f center = box.getCenter();
			posBox.set(center);
			float d = box.xExtent / 2;
			if (pos.x >= center.x) {
				posBox.x += d;
			} else {
				posBox.x -= d;
			}
			if (pos.y >= center.y) {
				posBox.y += d;
			} else {
				posBox.y -= d;
			}
			if (pos.z >= center.z) {
				posBox.z += d;
			} else {
				posBox.z -= d;
			}
			r = new Octree<T>(posBox, level - 1, this.d);
			r.parent = this;
			children[idx] = r;

		}
		return r;

	}

	public Octree<T> createLeafFor(Vector3f pos) {
		if (level == 0) {
			return this;
		}

		return createOctreeFor(pos).createLeafFor(pos);

	}

	public int getOctreeFor(Camera cam, List<VisibleOctree<T>> list, int idx) {

		if (children == null && level > 0) {
			return idx;
		}
		int side;

		int planeOk = 0;

		for (int plane : frustumPlanes) {
			side = box.whichSide(cam.getPlane(plane));
			// side = this.getSide(cam.getPlane(plane));
			if (side == Plane.NEGATIVE_SIDE) {
				// System.out.println(" box="+box);
				return idx;
			} else if (side == Plane.POSITIVE_SIDE) {
				planeOk++;
			}

		}

		if (planeOk == frustumPlanes.length || level == 0) {

			return add(list, idx);

		}
		for (Octree<T> octree : children) {
			if (octree != null)
				idx = octree.getOctreeFor(cam, list, idx);
		}
		return idx;
	}

	public int getOctreeLeafFor(Camera cam, List<VisibleOctree<T>> list, int idx) {

		if (children == null && level > 0) {
			return idx;
		}
		int side;

		int planeOk = 0;

		for (int plane : frustumPlanes) {
			side = box.whichSide(cam.getPlane(plane));
			// side = this.getSide(cam.getPlane(plane));
			if (side == Plane.NEGATIVE_SIDE) {
				// System.out.println(" box="+box);
				return idx;
			} else if (side == Plane.POSITIVE_SIDE) {
				planeOk++;
			}

		}

		if (planeOk == frustumPlanes.length || level == 0) {
			return this.getAllOctreeLeafIn(list, idx);
		}
		for (Octree<T> octree : children) {
			if (octree != null)
				idx = octree.getOctreeLeafFor(cam, list, idx);
		}
		return idx;
	}

	public void execute(final Camera cam, final OctreeAction<T> action) {

		int side;

		int planeOk = 0;

		for (int plane : frustumPlanes) {
			side = box.whichSide(cam.getPlane(plane));
			// side = this.getSide(cam.getPlane(plane));
			if (side == Plane.NEGATIVE_SIDE) {
				// System.out.println(" box="+box);
				return;
			} else if (side == Plane.POSITIVE_SIDE) {
				planeOk++;
			}

		}

		// System.out.println(" ---- ");
		if (!action.execute(this, planeOk == frustumPlanes.length)) {
			return;

		}
		// Vector3f pos = cam.getLocation();
		// Vector3f center = box.getCenter();
		if (children != null) {
			for (Octree<T> o : children) {
				if (o != null) {
					o.execute(cam, action);
				}
			}
		}

	}

	public Octree<T> octreeForAlignedAxisBox(Vector3f pos, Vector3f dim) {

		if (level == 0) {
			return this;
		}
		int idx = this.getIdxForPos(pos.x, pos.y, pos.z);

		int newIdx = this.getIdxForPos(pos.x + dim.x, pos.y, pos.z);
		if (idx != newIdx) {
			return this;

		}

		newIdx = this.getIdxForPos(pos.x, pos.y + dim.y, pos.z);
		if (idx != newIdx) {
			return this;

		}
		newIdx = this.getIdxForPos(pos.x, pos.y, pos.z + dim.z);
		if (idx != newIdx) {
			return this;

		}

		newIdx = this.getIdxForPos(pos.x + dim.x, pos.y + dim.y, pos.z);
		if (idx != newIdx) {
			return this;

		}

		newIdx = this.getIdxForPos(pos.x, pos.y + dim.y, pos.z + dim.z);
		if (idx != newIdx) {
			return this;

		}
		newIdx = this.getIdxForPos(pos.x + dim.x, pos.y, pos.z + dim.z);
		if (idx != newIdx) {
			return this;

		}

		newIdx = this.getIdxForPos(pos.x + dim.x, pos.y + dim.y, pos.z + dim.z);
		if (idx != newIdx) {
			return this;

		}
		return this.createOctreeFor(pos).octreeForAlignedAxisBox(pos, dim);

	}

	public int add(List<VisibleOctree<T>> list, int idx) {
		VisibleOctree<T> vo;
		if (list.size() == idx) {
			vo = new VisibleOctree<T>();

			list.add(vo);

		} else {
			vo = list.get(idx);
		}
		vo.octree = this;
		return idx + 1;
	}

	public int getAllOctreeLeafIn(List<VisibleOctree<T>> list, int idx) {
		if (level == 0) {
			return add(list, idx);

		}

		for (Octree<T> o : children) {
			if (o != null)
				idx = o.getAllOctreeLeafIn(list, idx);
		}
		return idx;
	}

}
