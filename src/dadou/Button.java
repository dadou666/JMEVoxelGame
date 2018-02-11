package dadou;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Button {
	int button;
	boolean release = true;

	public Button(int button) {
		this.button = button;

	}
	public boolean isDown() {
		return Mouse.isButtonDown(button);
	}
	public boolean isPressed() {

		if (Mouse.isButtonDown(button)) {

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
