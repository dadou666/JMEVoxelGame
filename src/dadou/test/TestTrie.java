package dadou.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jme.math.Vector3f;

import dadou.Log;
import dadou.graphe.GrapheElement;

public class TestTrie {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Vector3f> positions = new ArrayList<>();
		Vector3f p = new Vector3f(0,1,0);
		positions.add(new Vector3f(0,0,1));
		positions.add(new Vector3f(1,0,1));
		positions.add(new Vector3f(0,0,0.5f));
		positions.add(new Vector3f(0,1.25f,0));
		
		Collections.sort(positions, (Vector3f a, Vector3f b) -> {
			Vector3f tmp = new Vector3f();
			tmp.set(a);
			tmp.subtractLocal(p);
			float da= tmp.length();
			tmp.set(b);
			tmp.subtractLocal(p);
			float db=tmp.length();
		
			return 	Float.compare(da,db);
					


		});
		
		for(Vector3f u:positions) {
			Vector3f tmp = new Vector3f();
			tmp.set(u);
			tmp.subtractLocal(p);
			Log.print(" d ="+tmp.length());
			
		}

	}

}
