package dadou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme.math.Vector3f;

public class SphereGenerateur {
	Map<String, Integer> cacheIdx= new HashMap<>();
	public List<VBOTriangle> triangles= new ArrayList<>();
	public List<Vector3f> points= new ArrayList<>();;
	public Vector3f center;
	public float radius;

	public class Context {

		public Vector3f tmp = new Vector3f();

	

		public void m(Vector3f a, Vector3f b, float r, Vector3f center) {
			tmp = new Vector3f();
			tmp.set(b);
			tmp.subtractLocal(a);
			tmp.multLocal(0.5f);
			tmp.addLocal(a);
			tmp.subtractLocal(center);
			tmp.normalizeLocal();
			tmp.multLocal(r);
			tmp.addLocal(center);
		}

		public void normal(Vector3f p,
				Vector3f center) {
		
			tmp.set(p);
			
			tmp.subtractLocal(center);
		
		
		}
	}
	public SphereGenerateur( float radius, int n) {
		this(new Vector3f(0,0,0),radius,n);
	}

	public SphereGenerateur(Vector3f center, float radius, int n) {
		points = new ArrayList<Vector3f>();
		this.center = center;
		this.radius= radius;
		Vector3f p = null;
		p = new Vector3f();
		p.set(center);
		p.x = center.x + radius;

		points.add(p);

		p = new Vector3f();
		p.set(center);
		p.x = center.x - radius;

		points.add(p);
		p = new Vector3f();
		p.set(center);
		p.y = center.y + radius;

		points.add(p);
		p = new Vector3f();
		p.set(center);
		p.y = center.y - radius;

		points.add(p);
		p = new Vector3f();
		p.set(center);
		p.z = center.z + radius;

		points.add(p);
		p = new Vector3f();
		p.set(center);
		p.z = center.z - radius;

		points.add(p);

		Context ctx = new Context();
		this.decomposer(ctx, 0, 2, 4, n);
		this.decomposer(ctx, 0, 3, 4, n);
		this.decomposer(ctx, 0, 2, 5, n);
		this.decomposer(ctx, 0, 3, 5, n);

		this.decomposer(ctx, 1, 2, 4, n);
		this.decomposer(ctx, 1, 3, 4, n);
		this.decomposer(ctx, 1, 2, 5, n);
		this.decomposer(ctx, 1, 3, 5, n);

	}

	private void decomposer(Context ctx, int a, int b, int c, int niveau) {
		if (niveau == 0) {

			VBOTriangle t0 = new VBOTriangle();

			t0.a = a;
			t0.b = b;
			t0.c = c;
			this.triangles.add(t0);
			return;
			
		}
		String clefCache = null;
		if (a < b) {
			clefCache = "" + a + "_" + b;
		} else {
			clefCache = "" + b + "_" + a;
		}
		Integer iab = this.cacheIdx.get(clefCache);

		if (iab == null) {
			Vector3f pa = this.points.get(a);
			Vector3f pb = this.points.get(b);
			ctx.m(pa, pb, radius, center);
			iab = points.size();
			this.cacheIdx.put(clefCache, iab);
			this.points.add(ctx.tmp);

		}

		if (c < b) {
			clefCache = "" + c + "_" + b;
		} else {
			clefCache = "" + b + "_" + c;
		}
		Integer icb = this.cacheIdx.get(clefCache);

		if (icb == null) {
			Vector3f pa = this.points.get(c);
			Vector3f pb = this.points.get(b);
			ctx.m(pa, pb, radius, center);
			icb = points.size();
			this.cacheIdx.put(clefCache, icb);
			this.points.add(ctx.tmp);

		}
		if (c < a) {
			clefCache = "" + c + "_" + a;
		} else {
			clefCache = "" + a + "_" + c;
		}
		Integer ica = this.cacheIdx.get(clefCache);

		if (ica == null) {
			Vector3f pa = this.points.get(c);
			Vector3f pb = this.points.get(a);
			ctx.m(pa, pb, radius, center);
			ica = points.size();
			this.cacheIdx.put(clefCache, ica);
			this.points.add(ctx.tmp);

		}
	

		
			int nn = niveau - 1;
			this.decomposer(ctx, a, ica, iab, nn);
			this.decomposer(ctx, b, icb, iab, nn);
			this.decomposer(ctx, c, ica, icb, nn);
			this.decomposer(ctx, icb, ica, iab, nn);

		

	}

	public VBOTexture2D creerVBOTexture2D(Shader shader) {
		VBOTexture2D r = new VBOTexture2D(shader);
		Context ctx = new Context();
		for (Vector3f p : this.points) {
			ctx.normal(p, center);
			r.addVertex(p.x, p.y, p.z);
			r.addNormal(ctx.tmp.x, ctx.tmp.y, ctx.tmp.z);
		}
		for (VBOTriangle tri : this.triangles) {
			r.addTri(tri.a, tri.b, tri.c);

		}
		r.createVBO();
		return r;
	}

}
