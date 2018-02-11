package dadou.test;

import java.util.HashMap;

public class TestMap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HashMap<String, String> map = new HashMap<>();
		map.put("jj", "pl");
		map.put("lol", "kk");
		for (String v : map.values()) {
			if (v.equals("kk")) {
				map.remove("lol");
			}
		}

	}

}
