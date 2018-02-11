package dadou;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Quaternion;
import com.jme.renderer.Camera;
import java.awt.Rectangle;
import dadou.VoxelTexture3D.CouleurErreur;

public class VBOMinimunPourBriqueAvecTexture3D extends VBOBriqueTexture3D implements Element3D {

	public float dimX;

	public float dimY;
	public float dimZ;
	public float fx;
	public float fy;

	public float fz;

	public int sizeX;
	public int sizeY;
	public int sizeZ;

	public float dimCaseY;
	public float dimCaseZ;
	public float dimCaseX;



	public VBOMinimunPourBriqueAvecTexture3D(ModelClasse mc, VoxelTexture3D tex, VoxelShaderParam vsp, int x, int y,
			int z, float dim, int sizeX, int sizeY, int sizeZ) throws CouleurErreur {

		// super(vsp, dim, sizeX, maxY(x,z,sizeX, sizeZ, tex), sizeZ);
		this.tex = tex;
		

		this.dimCaseX = dim;
		this.dimCaseY = dim;
		this.dimCaseZ = dim;
		dimX = dim * sizeX;

		dimZ = dim * sizeZ;

		fx = tex.fX;
		fy = tex.fY;
		fz = tex.fZ;
		this.px = x;
		this.py = y;
		this.pz = z;
		this.sizeX = sizeX;
		this.sizeZ = sizeZ;

		this.sizeY = sizeY;
		PX = new Poid(sizeY, sizeZ);
		PY = new Poid(sizeX, sizeZ);
		PZ = new Poid(sizeX, sizeY);
		dimY = dim * sizeY;
		this.vsp = vsp;

		Shader sx = this.voxelShader().shaderVoxelX;
		Shader sy = this.voxelShader().shaderVoxelY;
		Shader sz = this.voxelShader().shaderVoxelZ;
		faceX.tex = tex;
		faceY.tex = tex;
		faceZ.tex = tex;
		faceX.mc = mc;
		faceY.mc = mc;
		faceZ.mc = mc;
		X = new VBOTexture3D(sx);

		this.initXAvecPartition();

		Y = new VBOTexture3D(sy);

		initYAvecPartition();

		Z = new VBOTexture3D(sz);

		this.initZAvecPartition();

		// TODO Auto-generated constructor stub
	}

	public int px, pz, py;

	public FaceX faceX = new FaceX();

	public FaceY faceY = new FaceY();
	public FaceZ faceZ = new FaceZ();

	

	public void initXAvecPartition() throws CouleurErreur {

		float coordTextureX = 0.0f;

		int base = 0;
		for (int cx = sizeX; cx >= 0; cx--) {
			int ux = px + cx;
			faceX.calculerPartition(ux, py, pz, sizeZ, sizeY, PX);
			for (Rectangle rect : faceX.rs) {
				float fx = cx;
				fx = fx * dimCaseX;
				coordTextureX = cx;
				coordTextureX = coordTextureX * tex.fX;
				float minY = rect.x;
				float minZ = rect.y;
				float maxZ = rect.y + rect.height;
				float maxY = rect.x + rect.width;
				X.addVertex(fx, minY * dimCaseY, minZ * dimCaseZ);
				X.addNormal(1, 0, 0);
				X.addVertex(fx, minY * dimCaseY, maxZ * dimCaseZ);
				X.addNormal(1, 0, 0);
				X.addVertex(fx, maxY * dimCaseY, maxZ * dimCaseZ);
				X.addNormal(1, 0, 0);
				X.addVertex(fx, maxY * dimCaseY, minZ * dimCaseZ);
				X.addNormal(1, 0, 0);
				X.addCoordTexture3D(coordTextureX, minY * fy, minZ * fz);
				X.addCoordTexture3D(coordTextureX, minY * fy, maxZ * fz);
				X.addCoordTexture3D(coordTextureX, maxY * fy, maxZ * fz);
				X.addCoordTexture3D(coordTextureX, maxY * fy, minZ * fz);

				X.addTri(base, base + 1, base + 2);
				X.addTri(base, base + 2, base + 3);
				numTri++;
				base += 4;
			}

		}

		X.createVBO();

	}

	

	public void initYAvecPartition() throws CouleurErreur {
		float coordTextureY = 0.0f;
		int base = 0;

		for (int cy = sizeY; cy >= 0; cy--) {
			int uy = py + cy;
			faceY.calculerPartition(px, uy, pz, sizeZ, sizeX, PY);
			for (Rectangle rect : faceY.rs) {
				float fy = cy;
				fy = fy * dimCaseY;
				coordTextureY = cy;
				coordTextureY = coordTextureY * tex.fY;
				float minX = rect.x;
				float maxX = rect.x + rect.width;
				float minZ = rect.y;
				float maxZ = rect.y + rect.height;
				Y.addVertex(minX * dimCaseX, fy, minZ * dimCaseZ);
				Y.addNormal(0, 1, 0);
				Y.addVertex(minX * dimCaseX, fy, maxZ * dimCaseZ);
				Y.addNormal(0, 1, 0);
				Y.addVertex(maxX * dimCaseX, fy, maxZ * dimCaseZ);
				Y.addNormal(0, 1, 0);
				Y.addVertex(maxX * dimCaseX, fy, minZ * dimCaseZ);
				Y.addNormal(0, 1, 0);
				// System.out.println(" coordTextureY="+coordTextureY);
				Y.addCoordTexture3D(minX * fx, coordTextureY, minZ * fz);
				Y.addCoordTexture3D(minX * fx, coordTextureY, maxZ * fz);
				Y.addCoordTexture3D(maxX * fx, coordTextureY, maxZ * fz);
				Y.addCoordTexture3D(maxX * fx, coordTextureY, minZ * fz);

				Y.addTri(base, base + 1, base + 2);
				Y.addTri(base, base + 2, base + 3);
				numTri++;
				base += 4;
			}

		}

		Y.createVBO();

	}

	
	public void initZAvecPartition() throws CouleurErreur {

		float coordTextureZ = 0.0f;
		int base = 0;

		for (int cz = sizeZ; cz >= 0; cz--) {
			int uz = pz + cz;
			faceZ.calculerPartition(px, py, uz, sizeX, sizeY, PZ);
			for (Rectangle rect : faceZ.rs) {
				float fz = cz;
				fz = fz * dimCaseZ;
				coordTextureZ = cz;
				coordTextureZ = coordTextureZ * tex.fZ;
				float minX = rect.x;
				float minY = rect.y;
				float maxX = rect.x + rect.width;
				float maxY = rect.y + rect.height;
				Z.addVertex(minX * dimCaseX, minY * dimCaseY, fz);
				Z.addNormal(0, 0, 1);
				Z.addVertex(minX * dimCaseX, maxY * dimCaseY, fz);
				Z.addNormal(0, 0, 1);
				Z.addVertex(maxX * dimCaseX, maxY * dimCaseY, fz);
				Z.addNormal(0, 0, 1);
				Z.addVertex(maxX * dimCaseX, minY * dimCaseY, fz);
				Z.addNormal(0, 0, 1);
				Z.addCoordTexture3D(minX * fx, minY * fy, coordTextureZ);
				Z.addCoordTexture3D(minX * fx, maxY * fy, coordTextureZ);
				Z.addCoordTexture3D(maxX * fx, maxY * fy, coordTextureZ);
				Z.addCoordTexture3D(maxX * fx, minY * fy, coordTextureZ);

				Z.addTri(base, base + 1, base + 2);
				Z.addTri(base, base + 2, base + 3);
				numTri++;
				base += 4;

			}

		}

		Z.createVBO();

	}

	public BriqueAvecTexture3D creerBriqueAvecTexture3D(VoxelTexture3D tex) {
		float tx = px;
		float ty = py;
		float tz = pz;
		tx = tx * fx;
		ty = ty * fy;
		tz = tz * fz;
		BriqueAvecTexture3D r = new BriqueAvecTexture3D(this, tex);
		r.tpos.set(tx, ty, tz);
		return r;
	}

	public VoxelShader voxelShader() {
		if (Game.rm == Game.RenderMode.Depth) {
			return Game.vsDepth;
		}

		return Game.vsShadow;

	}

}
