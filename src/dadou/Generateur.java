package dadou;

import dadou.Matrice.IncompatibleSizeException;
import dadou.Matrice.SingularMatrixException;

public class Generateur {

	/**
	 * @param args
	 * @throws IncompatibleSizeException
	 * @throws SingularMatrixException
	 */
	private float dim;
   private Matrice mat;
   private Matrice coeff;
	public Generateur( Matrice mat,float dim) throws SingularMatrixException, IncompatibleSizeException{
		this.mat = mat;
		this.dim = dim;
		coeff = this.matriceCoeff();
	}
	public static void main(String[] args)
			throws Matrice.SingularMatrixException,
			Matrice.IncompatibleSizeException {
		// TODO Auto-generated method stub

		int n = 2;
		Matrice mat = new Matrice(n, n);
		mat.setElem(1, 1, 2);
		mat.setElem(1, 2, 8);
		mat.setElem(2, 2, 5);
		mat.setElem(2,1, 3);
		Generateur coeff = new Generateur(mat,1.0f);
		for (int c = 0; c < n; c++) {
			for (int l = 0; l < n; l++) {
				System.out.println(" calcul=" + coeff.valeur(c, l)
						+ "  hauteur=" + mat.getElem(l+1, c+1));

			}
		}

	}

	public  float valeur(float x, float y) {
		int k = 1;
		float r = 0.0f;
		int n = mat.nColonnes;
		for (int c = 0; c < n; c++) {
			for (int l = 0; l < n; l++) {
				r += (float) (Math.pow(x, c) * Math.pow(y, l))
						* coeff.getElem(k, 1);
				k++;

			}
		}
		return r;

	}

	public  Matrice matriceCoeff()
			throws Matrice.SingularMatrixException,
			Matrice.IncompatibleSizeException {
		Matrice matHauteur =mat;
		int n = matHauteur.nColonnes * matHauteur.nLignes;
		Matrice coeff = new Matrice(n, n);
		Matrice vectHauteur = new Matrice(n,1);
		int i = 1;
		for (int c = 1; c <= matHauteur.nColonnes; c++) {
			for (int l = 1; l <= matHauteur.nLignes; l++) {
				float x = (c-1);
				float y = (l-1);
				x = x * dim;
				y = y * dim;
				float ligne[] = ligne(matHauteur.nColonnes, matHauteur.nLignes, x, y);
				for (int k = 1; k <= n; k++) {
					coeff.setElem(i, k, ligne[k-1]);
				}
				vectHauteur.setElem(i, 1, matHauteur.getElem(l, c));
				i++;

			}
		}

		coeff=coeff.inverse();
		Matrice result = Matrice.multiply(coeff, vectHauteur);

		return result;

	}

	public static float[] ligne(int nc, int nl, float x, float y) {
		float r[] = new float[nc * nl];
		int i = 0;
		for (int c = 0; c < nc; c++) {
			for (int l = 0; l < nl; l++) {
				r[i] = (float) (Math.pow(x, c) * Math.pow(y, l));
				i++;

			}
		}
		return r;

	}

}
