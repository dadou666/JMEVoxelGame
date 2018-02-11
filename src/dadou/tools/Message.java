/**
 * 
 */
package dadou.tools;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import com.jme.renderer.Camera;

import dadou.Button;
import dadou.ihm.IHM;
import dadou.ihm.InputfieldList;
import dadou.ihm.Widget;
import dadou.tools.construction.EtatBoxSelection;

/**
 * @author DAVID
 *
 */
public class Message extends EtatBrickEditor {
	IHM ihm;
	Button button;
	Widget message;
	Widget valider;
	public Message(BrickEditor be) {
		this.BrickEditor = be;
		ihm = be.game.nouvelleIHM();
		

		button = new Button(0);
		ihm.setSize(128, 32);
	

		ihm.beginX(()-> {
			ihm.space(300);
			ihm.beginY(()->{
				ihm.space(300);
				ihm.setSize(512, 32);
				message = ihm.widget();
				valider = ihm.widget();
				Widget.drawText(message, "", Color.BLUE, Color.white, Color.red,0);
				Widget.drawText(valider, "Valider", Color.GREEN, Color.white, Color.red,0);
				ihm.space(300);
				
			});
			
			ihm.space(300);
			
		});
	

	
	
		
		
	}
	public void afficherMessage(String s) {
		Widget.drawText(message,s, Color.BLUE, Color.white, Color.red,0);
		old = this.BrickEditor.etat;
		this.BrickEditor.etat = this;
		
	}
	int delayButton = 10;
	int delayValue =0;
	public void gerer() {

		if (delayValue > 0) {
			delayValue--;
			if (delayValue == 0) {
				Widget.drawText(valider, "Valider", Color.GREEN,
						Color.white, Color.red);
				this.BrickEditor.etat = old;
				
			}
			return;
		}
		
		if (button.isPressed()) {
			int x = Mouse.getX();
			int y = Mouse.getY();

			Widget sel = ihm.getWidgetAt(x, y);
			if (sel == valider) {
				delayValue = delayButton;
				Widget.drawText(valider, "Valider", Color.GREEN,
						Color.blue, Color.red);

				return;
			}
		}
		

	}

	public void dessiner(Camera cam) {
		ihm.dessiner(BrickEditor.game.shaderWidget);
	}
}
