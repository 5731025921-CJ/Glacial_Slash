package map;

import java.awt.Graphics2D;

import res.Resource;
import ui.GameScreen;

public class TutorialMap extends GameMap {

	private static final long serialVersionUID = -1627634380172291416L;
	
	protected TutorialMap() {
		super(Resource.tutorialMap);
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		GameScreen gameScreen = GameScreen.getScreen();
		g.drawImage(Resource.leftButton, null, 140 - gameScreen.getCameraX(), 900 - gameScreen.getCameraY());
		g.drawImage(Resource.rightButton, null, 220 - gameScreen.getCameraX(), 900 - gameScreen.getCameraY());
		g.drawImage(Resource.walkContent, null, 210 - gameScreen.getCameraX(), 980 - gameScreen.getCameraY());
		g.drawImage(Resource.spaceButton, null, 1540 - gameScreen.getCameraX(), 1000 - gameScreen.getCameraY());
		g.drawImage(Resource.jumpContent, null, 1570 - gameScreen.getCameraX(), 1080 - gameScreen.getCameraY());
		g.drawImage(Resource.manaSourceContent, null, 2900 - gameScreen.getCameraX(), 960 - gameScreen.getCameraY());
		g.drawImage(Resource.downButton, null, 3640 - gameScreen.getCameraX(), 800 - gameScreen.getCameraY());
		g.drawImage(Resource.plusButton, null, 3740 - gameScreen.getCameraX(), 840 - gameScreen.getCameraY());
		g.drawImage(Resource.sButton, null, 3720 - gameScreen.getCameraX(), 800 - gameScreen.getCameraY());
		g.drawImage(Resource.iceSummonContent, null, 3660 - gameScreen.getCameraX(), 790 - gameScreen.getCameraY());
		g.drawImage(Resource.spaceButton, null, 6240 - gameScreen.getCameraX(), 750 - gameScreen.getCameraY());
		g.drawImage(Resource.doublejumpContent, null, 6220 - gameScreen.getCameraX(), 740 - gameScreen.getCameraY());
		g.drawImage(Resource.upButton, null, 8240 - gameScreen.getCameraX(), 800 - gameScreen.getCameraY());
		g.drawImage(Resource.plusButton, null, 8340 - gameScreen.getCameraX(), 840 - gameScreen.getCameraY());
		g.drawImage(Resource.fButton, null, 8320 - gameScreen.getCameraX(), 800 - gameScreen.getCameraY());
		g.drawImage(Resource.skyUppercutContent, null, 8270 - gameScreen.getCameraX(), 780 - gameScreen.getCameraY());
		g.drawImage(Resource.eButton, null, 9500 - gameScreen.getCameraX(), 750 - gameScreen.getCameraY());
		g.drawImage(Resource.concentrationContent, null, 9480 - gameScreen.getCameraX(), 730 - gameScreen.getCameraY());
		g.drawImage(Resource.rightButton, null, 9980 - gameScreen.getCameraX(), 400 - gameScreen.getCameraY());
		g.drawImage(Resource.plusButton, null, 10080 - gameScreen.getCameraX(), 440 - gameScreen.getCameraY());
		g.drawImage(Resource.dButton, null, 10060 - gameScreen.getCameraX(), 400 - gameScreen.getCameraY());
		g.drawImage(Resource.glacialDriftContent, null, 10010 - gameScreen.getCameraX(), 390 - gameScreen.getCameraY());
		
		if (manaSources[0].isUsed()) {
			g.drawImage(Resource.yourHandContent, null, 20, 480);
			g.drawImage(Resource.yourManaContent, null, 20, 120);
		}
	}
	
}
