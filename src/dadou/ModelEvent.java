package dadou;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jme.math.Vector3f;

import dadou.event.GameEvent;
import dadou.event.GameEventLeaf;
import dadou.event.GameEventTree;
import dadou.mutator.Animation;
import dadou.mutator.Exploser;
import dadou.tools.canon.Obus;

public class ModelEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3989651613573754810L;
	public Map<String, String> donnees = new HashMap<String, String>();

	public ModeleEventInterface creerModeleEventInterface(String nomClasse, MondeInterfacePrive i) {
		Class clazz = i.modeleEventInterfaceClasses.get("monde." + nomClasse);
		if (clazz == null) {
		
			return null;
		}
		try {
			ModeleEventInterface r = (ModeleEventInterface) clazz.newInstance();
			r.i = i.mondeInterface;
			r.me = this;
			return r;
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public ModelEvent() {

		this.init();

	}

	public Map<String, String> donnees() {
		if (this.donnees == null) {
			this.donnees = new HashMap<>();
		}
		return this.donnees;
	}

	public void init() {

	}

	public void entree( ObjetMobile o, String position) {
		try {
			if (o.explose()) {
				return;
			}
			o.mei.entree(o, position);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void sortie( ObjetMobile om) {
		try {
			if (om.explose()) {
				return;
			}
			om.mei.sortie(om);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void entreeZoneDetection(ElementJeux obj, ElementJeux oEntree) {
		try {
			obj.mei.entreeZoneDetection(obj, oEntree);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void sortieZoneDetection(ElementJeux obj, ElementJeux oSortie) {
		try {

			obj.mei.sortieZoneDetection(obj, oSortie);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void tire(ObjetMobile om, String position, Obus obus) {
		try {
			if (om.explose()) {
				return;
			}
			if (obus != null) {
				om.mei.impactPosition.set(obus.objet3D.getTranslationGlobal());
			}
			om.mei.tire(om, position);

		} catch (Throwable t) {
			t.printStackTrace();
		}

	}
	public void finDecomposition(ObjetMobile om) {
		try {
			om.mei.finDecomposition(om);
			
		} catch(Throwable t) {
			t.printStackTrace();
		}
		
	}

	public void collisionCamera(ObjetMobile om, String nomJoueur) {
		try {
			if (om.explose()) {
				return;
			}
			if (om.construction != null) {
				return;
			}
			om.mei.collisionCamera(om);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void finDeplacement(ObjetMobile om) {
		try {
			if (om.explose()) {
				return;
			}
			om.mei.finDeplacement(om);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void finSon(ObjetMobile om, String nomSon) {
		try {
			if (om.explose()) {
				return;
			}
			om.mei.finSon(om, nomSon);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void finParcour(ObjetMobile om) {
		try {
			if (om.explose()) {
				return;
			}
			om.mei.finParcour(om);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void demarer(ObjetMobile om, Object args) {
		try {
			om.mei.demarer(om, args);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public boolean boucle(ObjetMobile om) {
		try {
			if (om.explose()) {
				return false;
			}
			if (om.construction == null) {
				om.mei.boucle(om);
			} else {
				om.construction.process(om);
			}
			return false;
		} catch (Throwable t) {
			t.printStackTrace();
			return true;
		}

	}

	public void finEchelle(ObjetMobile om) {
		try {
			if (om.explose()) {
				return;
			}
			om.mei.finEchelle(om);
		} catch (Throwable t) {
			t.printStackTrace();

		}
	}

	public void collisionObjetMobile(ObjetMobile om1, ObjetMobile om2) {
		try {
			if (om1.explose()) {
				return;
			}
			if (om1.construction != null) {
				return;
			}
			om1.mei.collisionObjetMobile(om1, om2);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void collisionZoneDetection(ObjetMobile om1, ObjetMobile om2) {
		try {
			if (om1.explose()) {
				return;
			}
			om1.mei.collisionZoneDetection(om1, om2);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void collisionDecor(ObjetMobile om) {
		try {
			if (om.explose()) {
				return;
			}
			om.mei.collisionDecor(om);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void finOrientation(ObjetMobile om, Vector3f destination) {
		try {
			if (om.explose()) {
				return;
			}
			om.mei.finOrientation(om, destination);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void finAnimation(ObjetMobile om, Animation animation) {
		try {
			if (om.explose()) {
				return;
			}
			Animation an;
			om.mei.finAnimation(om, animation);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void finTrajectoire(ObjetMobile om) {
		try {
			if (om.explose()) {
				return;
			}
			om.mei.finTrajectoire(om);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

}
