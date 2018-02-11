package dadou.mutator;

import dadou.ModelClasse;



public class AnimationEtape {
	public Animation animation;
	public int periode=5;
	public int tick=0;
	public int idx=0;
	public boolean loop = false;
	public AnimationEtape(Animation animation,int periode,boolean loop) {
		this.animation = animation;
		this.periode = periode;
		this.loop =loop;
	}
	public ModelClasse donnerModelClasse() {
		if (tick >=periode) {
			tick =0;
			idx++;
		
			if (idx>= animation.etapes.size()) {
				if (loop) {
					idx =0;
					
					return animation.etapes.get(0);
				}
				return null;
			}
		
			
		}
		tick++;
		return animation.etapes.get(idx);
		
	}

}
