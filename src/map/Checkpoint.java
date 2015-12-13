/*
 * Checkpoints act as a save point and also a turn indicator
 */

package map;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

import player.SkillCard;
import render.Renderable;
import res.Resource;
import ui.GameScreen;

public class Checkpoint implements Renderable, Serializable {
	
	private int x, y;
	private List<SkillCard> skillCards;
	private boolean used;
	private Rectangle boundaries;
	private transient Image sprite;
	
	public Checkpoint(int x, int y, List<SkillCard> skillCards) {
		this.x = x;
		this.y = y;
		this.skillCards = skillCards;
		used = false;
		sprite = Resource.checkpoint;
		boundaries = new Rectangle(x, y, ((BufferedImage)sprite).getWidth(), ((BufferedImage)sprite).getHeight());
	}
	
	public List<SkillCard> drawCard() {
		if (!used) {
			used = true;
			sprite = Resource.usedCheckpoint;
		}
		return skillCards;
	}
	
	public boolean isUsed() {
		return used;
	}
	
	public Rectangle getBoundaries() {
		return boundaries;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.drawImage((BufferedImage)sprite, null, x - GameScreen.getScreen().getCameraX(), y - GameScreen.getScreen().getCameraY());
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		if (used)
			sprite = Resource.usedCheckpoint;
		else
			sprite = Resource.checkpoint;
	}

}
