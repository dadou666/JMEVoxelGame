package dadou;

import com.jme.bounding.BoundingVolume;

public interface OctreeVisitor <T> {
	public BoundingVolume getBoudingVolume();
	public void execute(Octree<T> oct);
	

}
