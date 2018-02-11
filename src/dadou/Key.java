package dadou;

import org.lwjgl.input.Keyboard;

public class Key {
	int key;
	boolean release = true;

	public Key(int key) {
		this.key = key;
	//	System.out.println(Keyboard.getKeyName(key));

	}
	public Key(char c) {
	
	}
	public boolean isDown() {
		return Keyboard.isKeyDown(key);
	}
	public boolean isPressed() {

		if (Keyboard.isKeyDown(key)) {

			if (!release) {
				return false;
			}
			release = false;
			return true;

		}
		release = true;
		return false;
	}

}
