package dadou;

public interface OctreeAction<T> {
	public boolean execute(Octree<T> oct,boolean allIn);

}
