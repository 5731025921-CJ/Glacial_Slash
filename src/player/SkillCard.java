/*
 * Abstract class representing every kind of cards
 */

package player;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import exception.SkillCardUnusableException;
import render.Animation;
import res.Resource;
import sound.SoundEffectUtility;

public abstract class SkillCard implements Comparable<SkillCard>, Serializable {

	private static final long serialVersionUID = 2144325510872245603L;

	public static final SkillCard SKY_UPPERCUT = new SkyUppercut(), DOUBLE_JUMP = new DoubleJump(), GLACIAL_DRIFT = new GlacialDrift(), ICE_SUMMON = new IceSummon(), CONCENTRATION = new Concentration(null);
	public static final int CARD_IMAGE_WIDTH = 120, CARD_IMAGE_HEIGHT = 180;
	protected boolean isBeingUsed;
	protected int cost;
	protected transient Image cardImage;
	protected transient Image originalCardImage;
	protected transient Thread activateAnimationThread;
	
	public abstract void activate() throws SkillCardUnusableException;
	
	protected SkillCard(int cost, Image cardImage) {
		this.cost = cost;
		this.cardImage = cardImage;
		this.originalCardImage = cardImage;
		this.isBeingUsed = false;
	}
	
	public static final SkillCard createSkillCard(String name) {
		// Use when add cards only
		// Use dummy constants when referred, like activation
		if (name.startsWith("S")) return new SkyUppercut();
		else if (name.startsWith("D")) return new DoubleJump();
		else if (name.startsWith("G")) return new GlacialDrift();
		else if (name.startsWith("I")) return new IceSummon();
		else if (name.startsWith("C")) {
			String[] format = name.split("\\s+");
			int n = Integer.parseInt(format[1]);
			SkillCard[] drawnCards = new SkillCard[n];
			for (int i = 0; i < n; i++) {
				drawnCards[i] = SkillCard.createSkillCard(format[2+i]);
			}
			return new Concentration(drawnCards);
		}
		else return null;
	}
	
	protected void playActivateAnimation() {
		this.isBeingUsed = true;
		SoundEffectUtility.playSoundEffect(Resource.cardSound);
		activateAnimationThread = new Thread(new Animation(Resource.cardAnimation, 2) {
			
			@Override
			public void run() {
				BufferedImage originalCardImage = (BufferedImage)SkillCard.this.originalCardImage;
				Graphics2D imageGraphics;
				frameDelayCount = frameDelay;
				while (currentFrame < frameCount) {
					if (frameDelayCount <= 0) {
						cardImage = new BufferedImage(originalCardImage.getWidth(), originalCardImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
						imageGraphics = ((BufferedImage)cardImage).createGraphics();
						imageGraphics.drawImage(originalCardImage, null, 0, 0);
						imageGraphics.drawImage(animation[currentFrame], null, 0, 0);
						currentFrame++;
						frameDelayCount = frameDelay;
					}
					frameDelayCount--;
					synchronized (PlayerStatus.getPlayer().getPlayerCharacter()) {
						try {
							PlayerStatus.getPlayer().getPlayerCharacter().wait();
						} catch (InterruptedException e) {
							return;
						}
					}
				}
				cardImage = new BufferedImage(originalCardImage.getWidth(), originalCardImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
				imageGraphics = ((BufferedImage)cardImage).createGraphics();
				imageGraphics.drawImage(originalCardImage, null, 0, 0);
			}
		});
		activateAnimationThread.start();
	}
	
	protected void joinActivateAnimationThread() throws InterruptedException {
		activateAnimationThread.join();
	}
	
	@Override
	public int compareTo(SkillCard other) {
		int costCompare = Integer.compare(this.cost, other.cost);
		if (costCompare == 0)
			return this.getClass().getSimpleName().compareToIgnoreCase(other.getClass().getSimpleName());
		else
			return costCompare;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.getClass().getSimpleName().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (isBeingUsed != ((SkillCard)obj).isBeingUsed)
			return false;
		return true;
	}

}
