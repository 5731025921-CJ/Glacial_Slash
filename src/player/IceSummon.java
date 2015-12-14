package player;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;

import exception.SkillCardUnusableException;
import res.Resource;

public class IceSummon extends SkillCard {
	
	private static final long serialVersionUID = -9142814818198840454L;

	private transient Thread iceSummonThread;

	public IceSummon() {
		super(1, Resource.iceSummon);
	}

	@Override
	public void activate() throws SkillCardUnusableException {
		if (!PlayerStatus.getPlayer().getPlayerCharacter().isOnGround()) throw new SkillCardUnusableException(SkillCardUnusableException.UnusableType.ACTIVATE_CONDITION_NOT_MET);
		Point frontTile = PlayerStatus.getPlayer().getPlayerCharacter().getFrontTile();
		if (!PlayerStatus.getPlayer().getCurrentMap().getTileType((int)frontTile.getX(), (int)frontTile.getY()).isPassable()) throw new SkillCardUnusableException(SkillCardUnusableException.UnusableType.ACTIVATE_CONDITION_NOT_MET);
		Point spriteFrontTile = PlayerStatus.getPlayer().getPlayerCharacter().getSpriteFrontTile();
		if (!PlayerStatus.getPlayer().getCurrentMap().isOnGround(new Rectangle((int)spriteFrontTile.getX()*PlayerStatus.getPlayer().getCurrentMap().getTileWidth(), (int)spriteFrontTile.getY()*PlayerStatus.getPlayer().getCurrentMap().getTileHeight(), PlayerStatus.getPlayer().getCurrentMap().getTileWidth(), PlayerStatus.getPlayer().getCurrentMap().getTileHeight()))) throw new SkillCardUnusableException(SkillCardUnusableException.UnusableType.ACTIVATE_CONDITION_NOT_MET);
		if (!PlayerStatus.getPlayer().getCurrentMap().getTileType((int)spriteFrontTile.getX(), (int)spriteFrontTile.getY()).isPassable()) throw new SkillCardUnusableException(SkillCardUnusableException.UnusableType.ACTIVATE_CONDITION_NOT_MET);
		playActivateAnimation();
		PlayerStatus.getPlayer().getPlayerCharacter().performIceSummon();
		iceSummonThread = new Thread (new Runnable() {
			
			@Override
			public void run() {
				try {
					PlayerStatus.getPlayer().getPlayerCharacter().getIceSummonAnimationThread().join();
				} catch (InterruptedException e) {
					return;
				}
				PlayerStatus.getPlayer().getCurrentMap().freeze((int)spriteFrontTile.getX(), (int)spriteFrontTile.getY());
			}
		});
		iceSummonThread.start();
	}
	
	protected Thread getIceSummonThread() {
		return iceSummonThread;
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		originalCardImage = SkillCard.ICE_SUMMON.cardImage;
		cardImage = originalCardImage;
	}

}
