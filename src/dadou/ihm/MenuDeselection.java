package dadou.ihm;

public class MenuDeselection {
	public MenuControlleur menu;
	public int delay=0;
	public int delayMax=10;
	public MenuDeselection(MenuControlleur menu) {
		this.menu = menu;
	}
	public void activer() {
		menu.selected = true;
		delay =delayMax;
	}
	public void gerer() {
		if (delay > 0) {
			if (delay==1) {
				menu.selected = false;
				menu.updateText();
			}
			delay--;
			
		}
		
	}
	

}
