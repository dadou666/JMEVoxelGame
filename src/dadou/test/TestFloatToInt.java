package dadou.test;

import java.awt.Color;

public class TestFloatToInt {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		float x = 1.45f;
		byte b = -15;
		System.out.println(" " +x+" to int "+ (int) (x));
		System.out.println(" " + (127 + (int) (byte) (int) (200)));
		System.out.println("b =" + (int) b);
		Color color = Color.WHITE;
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		System.out.println(" b=" + red + " " + green + " " + blue);
		System.out.println(" a=" + (byte) 255);
		System.out.println(" a=" + (byte) 129);
		System.out.println(" a=" + (byte) 1);

	}

}
