package dadou;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Quaternion;
import com.jme.renderer.Camera;
import java.awt.Rectangle;
import dadou.VoxelTexture3D.CouleurErreur;

public class VBOMinimunPourPlancheAvecTexture3D extends VBOBriqueTexture3D implements Element3D {

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




	
	boolean partition = false;

	public VBOMinimunPourPlancheAvecTexture3D(boolean partition, ModelClasse mc,
			VoxelTexture3D tex, VoxelShaderParam vsp, int x, int y, int z,
			float dim, int sizeX, int sizeY, int sizeZ) throws CouleurErreur {

		// super(vsp, dim, sizeX, maxY(x,z,sizeX, sizeZ, tex), sizeZ);
		this.tex = tex;
		this.partition = partition;

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
		faceX.estPlanche = true;
		faceY.estPlanche = true;
		faceZ.estPlanche = true;
		
		faceX.mc = mc;
		faceY.mc = mc;
		faceZ.mc = mc;
		X = new VBOTexture3D(sx);
		if (partition) {
			this.initXAvecPartition();
		} else {
			initX();
		}
		Y = new VBOTexture3D(sy);
		if (partition) {
			initYAvecPartition();
		} else {
			initY();
		}
		Z = new VBOTexture3D(sz);
		if (partition) {
			this.initZAvecPartition();
		} else {
			initZ();
		}
		// TODO Auto-generated constructor stub
	}

	public VBOMinimunPourPlancheAvecTexture3D(ModelClasse mc,
			VoxelTexture3D tex, VoxelShaderParam vsp, int x, int y, int z,
			float dim, int sizeX, int sizeY, int sizeZ) throws CouleurErreur {

		this(false, mc, tex, vsp, x, y, z, dim, sizeX, sizeY, sizeZ);
	}

	public int px, pz, py;


	
	public FaceX faceX = new FaceX();

	public FaceY faceY = new FaceY();
	public FaceZ faceZ = new FaceZ();

	public void initX() throws CouleurErreur {

		float coordTextureX = 0.0f;

		int base = 0;
		for (int cx = sizeX-1; cx >= 0; cx--) {
			int ux = px + cx;
			if (!faceX.calculer(ux, py, pz, sizeZ, sizeY, PX)) {
				float fx = cx;
				fx = fx * dimCaseX;
				coordTextureX = cx;
				coordTextureX = coordTextureX * tex.fX;
				float minY = faceX.getMinY();
				float minZ = faceX.getMinZ();
				float maxZ = faceX.getMaxZ();
				float maxY = faceX.getMaxY();
				X.addVertex(fx+0.5f, minY * dimCaseY, minZ * dimCaseZ);
				X.addNormal(1, 0, 0);
				X.addVertex(fx+0.5f, minY * dimCaseY, maxZ * dimCaseZ);
				X.addNormal(1, 0, 0);
				X.addVertex(fx+0.5f, maxY * dimCaseY, maxZ * dimCaseZ);
				X.addNormal(1, 0, 0);
				X.addVertex(fx+0.5f, maxY * dimCaseY, minZ * dimCaseZ);
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

	public void initXAvecPartition() throws CouleurErreur {

		float coordTextureX = 0.0f;

		int base = 0;
		for (int cx = sizeX-1; cx >= 0; cx--) {
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
				X.addVertex(fx+0.5f, minY * dimCaseY, minZ * dimCaseZ);
				X.addNormal(1, 0, 0);
				X.addVertex(fx+0.5f, minY * dimCaseY, maxZ * dimCaseZ);
				X.addNormal(1, 0, 0);
				X.addVertex(fx+0.5f, maxY * dimCaseY, maxZ * dimCaseZ);
				X.addNormal(1, 0, 0);
				X.addVertex(fx+0.5f, maxY * dimCaseY, minZ * dimCaseZ);
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

	public void initY() throws CouleurErreur {
		float coordTextureY = 0.0f;
		int base = 0;

		for (int cy = sizeY-1; cy >= 0; cy--) {
			int uy = py + cy;
			if (!faceY.calculer(px, uy, pz, sizeZ, sizeX, PY)) {
				float fy = cy;
				fy = fy * dimCaseY;
				coordTextureY = cy;
				coordTextureY = coordTextureY * tex.fY;
				float minX = faceY.minX;
				float maxX = faceY.maxX;
				float minZ = faceY.minZ;
				float maxZ = faceY.maxZ;
				Y.addVertex(minX * dimCaseX, fy+0.5f, minZ * dimCaseZ);
				Y.addNormal(0, 1, 0);
				Y.addVertex(minX * dimCaseX, fy+0.5f, maxZ * dimCaseZ);
				Y.addNormal(0, 1, 0);
				Y.addVertex(maxX * dimCaseX, fy+0.5f, maxZ * dimCaseZ);
				Y.addNormal(0, 1, 0);
				Y.addVertex(maxX * dimCaseX, fy+0.5f, minZ * dimCaseZ);
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

	public void initYAvecPartition() throws CouleurErreur {
		float coordTextureY = 0.0f;
		int base = 0;

		for (int cy = sizeY-1; cy >= 0; cy--) {
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
				Y.addVertex(minX * dimCaseX, fy+0.5f, minZ * dimCaseZ);
				Y.addNormal(0, 1, 0);
				Y.addVertex(minX * dimCaseX, fy+0.5f, maxZ * dimCaseZ);
				Y.addNormal(0, 1, 0);
				Y.addVertex(maxX * dimCaseX, fy+0.5f, maxZ * dimCaseZ);
				Y.addNormal(0, 1, 0);
				Y.addVertex(maxX * dimCaseX, fy+0.5f, minZ * dimCaseZ);
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

	public void initZ() throws CouleurErreur {

		float coordTextureZ = 0.0f;
		int base = 0;

		for (int cz = sizeZ-1; cz >= 0; cz--) {
			int uz = pz + cz;
			if (!faceZ.calculer(px, py, uz, sizeX, sizeY, PZ)) {
				float fz = cz;
				fz = fz * dimCaseZ;
				coordTextureZ = cz;
				coordTextureZ = coordTextureZ * tex.fZ;
				float minX = faceZ.getMinX();
				float minY = faceZ.getMinY();
				float maxX = faceZ.getMaxX();
				float maxY = faceZ.getMaxY();
				Z.addVertex(minX * dimCaseX, minY * dimCaseY, fz+0.5f);
				Z.addNormal(0, 0, 1);
				Z.addVertex(minX * dimCaseX, maxY * dimCaseY, fz+0.5f);
				Z.addNormal(0, 0, 1);
				Z.addVertex(maxX * dimCaseX, maxY * dimCaseY, fz+0.5f);
				Z.addNormal(0, 0, 1);
				Z.addVertex(maxX * dimCaseX, minY * dimCaseY, fz+0.5f);
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

	public void initZAvecPartition() throws CouleurErreur {

		float coordTextureZ = 0.0f;
		int base = 0;

		for (int cz = sizeZ-1; cz >= 0; cz--) {
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
				Z.addVertex(minX * dimCaseX, minY * dimCaseY, fz+0.5f);
				Z.addNormal(0, 0, 1);
				Z.addVertex(minX * dimCaseX, maxY * dimCaseY, fz+0.5f);
				Z.addNormal(0, 0, 1);
				Z.addVertex(maxX * dimCaseX, maxY * dimCaseY, fz+0.5f);
				Z.addNormal(0, 0, 1);
				Z.addVertex(maxX * dimCaseX, minY * dimCaseY, fz+0.5f);
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

	@Override
	public void dessiner(Camera cam) {
		// TODO Auto-generated method stub

	}



	@Override
	public void delete() {
		// TODO Auto-generated method stub
	//	X.delete();
	//	Y.delete();
	//	Z.delete();

	}

	@Override
	public void initFromShadowMap(Objet3D obj, Camera cam) {
		// TODO Auto-generated method stub
		
	}
	public VoxelShader voxelShader() {
		if (Game.rm ==Game.RenderMode.Depth) {
			return Game.vsDepthPlanche;
		}
	
			return Game.vsShadowPlanche;
	
		
	}

}
