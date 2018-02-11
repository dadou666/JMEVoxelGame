package dadou.test;

import dadou.Game;
import dadou.Objet3D;
import dadou.VoxelTexture3D;

public class TestDroneVoxel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game g = new Game();
		Objet3D camObj = new Objet3D();
		camObj.camera  = g.getCamera();
		VoxelTexture3D tex = new VoxelTexture3D(4);
	
		
		
		while (!g.isClosed()) {
			
			
			
			
		}
	}

}
