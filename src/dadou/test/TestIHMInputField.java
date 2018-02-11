package dadou.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import dadou.Button;
import dadou.Game;
import dadou.ihm.IHM;
import dadou.ihm.InputfieldList;
import dadou.ihm.Widget;
import dadou.param.GraphicsParam;

public class TestIHMInputField {


	public static void main(String[] args) throws LWJGLException {
		// TODO Auto-generated method stub
	//	GraphicsParam.gp = new GraphicsParam();
		Game g = new Game();
		InputfieldList inputFieldList = new InputfieldList();
		Map<Widget, String> msg = new HashMap<Widget, String>();
		Button button = new Button(0);
		IHM ihm = g.nouvelleIHM();
		Widget w;
		ihm.setSize(128, 32);
		ihm.beginX();
		ihm.space(500);
		ihm.beginY();
		ihm.space(200);
		Widget.drawText(ihm.widget(), "Nom1", Color.BLUE, Color.RED,Color.white,20);
		Widget.drawText(ihm.widget(), "Nom2", Color.BLUE, Color.RED,Color.white,20);
		Widget.drawText(ihm.widget(), "Nom3", Color.BLUE, Color.RED,Color.white,20);
		Widget.drawText(ihm.widget(), "Nom4", Color.BLUE, Color.RED,Color.white,20);
		ihm.end();
		
		ihm.beginY();
		ihm.space(200);
		
		inputFieldList.setInputField(ihm.widget(),"Dadou");
		inputFieldList.setInputField(ihm.widget(),"Nini");
		inputFieldList.setInputField(ihm.widget(),"Quentin");
		inputFieldList.setInputField(ihm.widget(),"Margaux");
		ihm.end();
		ihm.end();
		Color color = Color.YELLOW;

		boolean selection = true;
		StringBuilder sb = new StringBuilder();
		while (!g.isClosed()) {
			// System.out.println(" frame "+k);
			// k++;
			g.checkEscape();

			g.clear();

			// obj.translation(-dim / 2, -dim / 2, -dim / 2);
			// obj.rotation(axe2, 0.01f);
			// obj.rotation(axe, 0.01f);

			ihm.dessiner(g.shaderWidget);
	
			if (button.isPressed()) {
				int x = Mouse.getX();
				int y = Mouse.getY();

				Widget sel = ihm.getWidgetAt(x, y);
				if (sel != null) {
				inputFieldList.select(sel);
					
				}
			}
			inputFieldList.processKeyboard();
			Display.update();
			Display.sync(30);

			g.displayBackBuffer();

		}

	}

}
