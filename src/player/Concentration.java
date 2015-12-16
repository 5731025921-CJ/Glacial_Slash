package player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.List;

import exception.SkillCardUnusableException;
import res.Resource;

public class Concentration extends SkillCard {
	
	private static final long serialVersionUID = 3351882579142512755L;

	private SkillCard[] drawnCards;

	public Concentration(SkillCard[] drawnCards) {
		super(2, Resource.concentration);
		this.drawnCards = drawnCards;
	}

	@Override
	public void activate() throws SkillCardUnusableException {
		List<SkillCard> hand = PlayerStatus.getPlayer().getHand();
		playActivateAnimation();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					activateAnimationThread.join();
				} catch (InterruptedException e) {
					return;
				}
				for (SkillCard s : drawnCards) {
					synchronized (hand) {
						hand.add(s);
					}
				}
				synchronized (hand) {
					Collections.sort(hand);
				}
			}
		}).start();
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		originalCardImage = SkillCard.CONCENTRATION.cardImage;
		cardImage = originalCardImage;
	}
	
	@Override
	public int compareTo(SkillCard other) {
		if (other instanceof Concentration) {
			Concentration otherConcentration = (Concentration)other;
			int drawnCardNumber = Integer.min(this.drawnCards.length, otherConcentration.drawnCards.length);
			for (int i = 0; i < drawnCardNumber; i++) {
				int drawnCardCompare = this.drawnCards[i].compareTo(otherConcentration.drawnCards[i]);
				if (drawnCardCompare != 0)
					return drawnCardCompare;
			}
			return Integer.compare(this.drawnCards.length, otherConcentration.drawnCards.length);
		}
		else {
			return super.compareTo(other);
		}
	}
	
}
