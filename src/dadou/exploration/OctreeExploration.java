package dadou.exploration;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;

import dadou.Octree;

public class OctreeExploration extends Octree<Object> {

	public OctreeExploration(Vector3f pos, int level, float d) {
		super(pos, level, d);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public BoundingBox boxExploration = new BoundingBox();
	public BoundingBox boxExplorationOld = new BoundingBox();
	private static final long serialVersionUID = -6048859510820172993L;
	public Object marqueVisite = new Object();
	public Object marqueVisitePrecedent = null;

	public void modifierBoxExploration() {
		boxExploration.getCenter().set(boxExplorationOld.getCenter());
		boxExploration.xExtent = boxExplorationOld.xExtent;
		boxExploration.yExtent = boxExplorationOld.yExtent;
		boxExploration.zExtent = boxExplorationOld.zExtent;
	}

	public boolean augmenteBoxExploration(Vector3f pos, float rayon) {
		Vector3f o = this.boxExploration.getCenter();
		float maxX = Math.max(pos.x + rayon, o.x + this.boxExploration.xExtent);
		float maxY = Math.max(pos.y + rayon, o.y + this.boxExploration.yExtent);
		float maxZ = Math.max(pos.z + rayon, o.z + this.boxExploration.zExtent);

		float minX = Math.min(pos.x - rayon, o.x - this.boxExploration.xExtent);
		float minY = Math.min(pos.y - rayon, o.y - this.boxExploration.yExtent);
		float minZ = Math.min(pos.z - rayon, o.z - this.boxExploration.zExtent);
		this.boxExplorationOld.getCenter().set((maxX + minX) / 2,
				(maxY + minY) / 2, (maxZ + minZ) / 2);
		this.boxExplorationOld.xExtent = (maxX - minX) / 2.0f;
		this.boxExplorationOld.yExtent = (maxY - minY) / 2.0f;
		this.boxExplorationOld.zExtent = (maxZ - minZ) / 2.0f;

		bs.getCenter().set(pos);
		boxExplorationOld.mergeLocal(bs);
		if (boxExplorationOld.xExtent > boxExploration.xExtent) {
			return true;
		}
		if (boxExplorationOld.yExtent > boxExploration.yExtent) {
			return true;
		}
		if (boxExplorationOld.zExtent > boxExploration.zExtent) {
			return true;
		}
		return false;
	}

	public void modifierBoxExploration(Vector3f pos, float rayon) {
		this.augmenteBoxExploration(pos, rayon);
		boxExploration.getCenter().set(boxExplorationOld.getCenter());
		boxExploration.xExtent = boxExplorationOld.xExtent;
		boxExploration.yExtent = boxExplorationOld.yExtent;
		boxExploration.zExtent = boxExplorationOld.zExtent;
		// Log.print(" boxExploration =" + boxExploration);
	}
	public void init(Vector3f p,float rayon
		) {
		boxExploration.getCenter().set(p);
		boxExploration.xExtent = rayon;
		boxExploration.yExtent = rayon;
		boxExploration.zExtent = rayon;
	}

}
