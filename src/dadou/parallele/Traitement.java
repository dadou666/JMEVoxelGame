package dadou.parallele;

abstract public class Traitement {
	Traitement suivant;
	public boolean estTermine = false;
	abstract void executer();

}
