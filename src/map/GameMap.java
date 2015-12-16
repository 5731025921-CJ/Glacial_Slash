/*
 * Represent each map of the game
 */

package map;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import player.PlayerStatus;
import player.SkillCard;
import render.Renderable;
import res.Resource;
import sound.SoundEffectUtility;
import ui.GameScreen;

public class GameMap implements Renderable, Serializable {
	
	private static final long serialVersionUID = 6666274260861170041L;

	public static final int TILE_WIDTH = 70, TILE_HEIGHT = 70;
	protected int width, height;
	protected Tile[][] tileMap;
	protected ManaSource[] manaSources;
	protected Point initialPosition, transitionPoint;
	protected String nextMapName;
	protected boolean isManaSourceSaving;
	
	private static GameMap readGameMap(InputStream in) {
		try (ObjectInputStream serialIn = new ObjectInputStream(in)) {
			return (GameMap)serialIn.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public static GameMap getGameMap(String mapName) {
		// Used with MapSerializer
		/*
		if ("tutorial".equalsIgnoreCase(mapName)) return new TutorialMap();
		else if ("easy".equalsIgnoreCase(mapName)) return new GameMap(Resource.easyMap);
		else if ("normal".equalsIgnoreCase(mapName)) return new GameMap(Resource.normalMap);
		else if ("hard".equalsIgnoreCase(mapName)) return new GameMap(Resource.hardMap);
		else if ("final".equalsIgnoreCase(mapName)) return new FinalMap();
		*/
		
		if ("tutorial".equalsIgnoreCase(mapName)) return readGameMap(Resource.tutorialMap);
		else if ("easy".equalsIgnoreCase(mapName)) return readGameMap(Resource.easyMap);
		else if ("normal".equalsIgnoreCase(mapName)) return readGameMap(Resource.normalMap);
		else if ("hard".equalsIgnoreCase(mapName)) return readGameMap(Resource.hardMap);
		else if ("final".equalsIgnoreCase(mapName)) return readGameMap(Resource.finalMap);
		
		else return null;
	}

	protected GameMap(InputStream mapFile) {
		Scanner fileScanner;
		fileScanner = new Scanner(mapFile);
		
		width = Integer.parseInt(fileScanner.nextLine());
		height = Integer.parseInt(fileScanner.nextLine());
		
		tileMap = new Tile[width][height];
			
		for (int tileY = 0; tileY < height; tileY++) {
			String buffer = fileScanner.nextLine().trim();
			for (int tileX = 0; tileX < width; tileX++) {
				switch (buffer.charAt(tileX)) {
				case '#': tileMap[tileX][tileY] = Tile.GROUND; break;
				case '<': tileMap[tileX][tileY] = Tile.TOPLEFT; break;
				case '_': tileMap[tileX][tileY] = Tile.TOP; break;
				case '>': tileMap[tileX][tileY] = Tile.TOPRIGHT; break;
				case '+': tileMap[tileX][tileY] = Tile.LEFT; break;
				case '-': tileMap[tileX][tileY] = Tile.MID; break;
				case '*': tileMap[tileX][tileY] = Tile.RIGHT; break;
				case 'X': transitionPoint = new Point(tileX*TILE_WIDTH + 35, tileY*TILE_HEIGHT); break;
				case 'P': initialPosition = new Point(tileX*TILE_WIDTH, (tileY+1)*TILE_HEIGHT);
				}
				if (tileMap[tileX][tileY] == null)
					tileMap[tileX][tileY] = Tile.AIR;
			}
		}
		
		if ("save".equalsIgnoreCase(fileScanner.nextLine()))
			isManaSourceSaving = true;
		else
			isManaSourceSaving = false;
			
		int checkpointCount = Integer.parseInt(fileScanner.nextLine());
		manaSources = new ManaSource[checkpointCount];
		for (int i = 0; i < checkpointCount; i++) {
			int tileX = Integer.parseInt(fileScanner.nextLine());
			int tileY = Integer.parseInt(fileScanner.nextLine());
			int screenX = tileX*TILE_WIDTH + (TILE_WIDTH-Resource.checkPoint.getWidth())/2;
			int screenY = (tileY-1)*TILE_HEIGHT + (TILE_HEIGHT-Resource.checkPoint.getHeight());
	
			int cardCount = Integer.parseInt(fileScanner.nextLine());
			List<SkillCard> hand = new ArrayList<>();
			for (int j = 0; j < cardCount; j++) {
				hand.add(SkillCard.createSkillCard(fileScanner.nextLine().trim()));
			}
			
			manaSources[i] = new ManaSource(screenX, screenY, hand);
		}
		
		nextMapName = fileScanner.nextLine().trim();

		fileScanner.close();
		
	}
	
	public GameMap getNextMap() {
		return getGameMap(nextMapName);
	}
	
	public Point getInitialPosition() {
		return initialPosition;
	}

	public int getScreenWidth() {
		return width*TILE_WIDTH;
	}

	public int getScreenHeight() {
		return height*TILE_HEIGHT;
	}
	
	public void freeze(int tileX, int tileY) {
		tileMap[tileX][tileY] = Tile.ICE;
	}
	
	public Tile getTileType(int tileX, int tileY) {
		return tileMap[tileX][tileY];
	}
			
	public boolean collideWithTransitionPoint(Rectangle collisionBox) {
		try {
			return collisionBox.contains(transitionPoint);
		} catch (NullPointerException e) {
			return false;
		}
	}

	public Point getFrontTile(Rectangle collisionBox, int direction) {
		direction = Integer.signum(direction);
		int frontBoundary = (int)(collisionBox.getX() + (direction < 0 ? 0 : 1)*collisionBox.getWidth()) - direction;
		int lowerBoundary = (int)(collisionBox.getY() + collisionBox.getHeight()) - 1;
		int currentTileX = frontBoundary / TILE_WIDTH;
		int currentTileY = lowerBoundary / TILE_HEIGHT;
		return new Point(currentTileX + direction, currentTileY);
	}
	
	public boolean isOnGround(Rectangle collisionBox) {
		return movableHeight(collisionBox, 1) == 0;
	}

	public int movableWidth(Rectangle collisionBox, int direction) {
		direction = Integer.signum(direction);
		if (direction == 0)
			return 0;
		
		int frontBoundary = (int)(collisionBox.getX() + (direction < 0 ? 0 : 1)*collisionBox.getWidth());
		int upperBoundary = (int)collisionBox.getY() + 1, lowerBoundary = (int)(collisionBox.getY() + collisionBox.getHeight() - 1);
		int upperBoundaryTile = upperBoundary/TILE_HEIGHT, lowerBoundaryTile = lowerBoundary/TILE_HEIGHT;
		
		int startTile = frontBoundary/TILE_WIDTH;
		
		int maxMovableWidth = (direction < 0 ? 0 : 1)*(this.getScreenWidth()) - frontBoundary;

		for (int tileY = upperBoundaryTile; tileY <= lowerBoundaryTile; tileY++) {
			int tileX = startTile;
			try {
				while (tileMap[tileX][tileY].isPassable()) {
					tileX += direction;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
			tileX += (direction < 0)? 1 : 0;
			if (Math.abs(tileX*TILE_WIDTH - frontBoundary) < Math.abs(maxMovableWidth)) {
				maxMovableWidth = tileX*TILE_WIDTH - frontBoundary;
			}
		}
		return maxMovableWidth;
	}

	public int movableHeight(Rectangle collisionBox, int direction) {
		direction = Integer.signum(direction);
		if (direction == 0)
			return 0;
		
		int frontBoundary = (int)(collisionBox.getY() + (direction < 0 ? 0 : 1)*collisionBox.getHeight());
		int leftBoundary = (int)collisionBox.getX() + 1, rightBoundary = (int)(collisionBox.getX() + collisionBox.getWidth() - 1);
		int leftBoundaryTile = leftBoundary/TILE_WIDTH, rightBoundaryTile = rightBoundary/TILE_WIDTH;
		
		int startTile = frontBoundary/TILE_HEIGHT;
		
		int maxMovableHeight = (direction < 0 ? 0 : 1)*(this.getScreenHeight()) - frontBoundary;

		for (int tileX = leftBoundaryTile; tileX <= rightBoundaryTile; tileX++) {
			int tileY = startTile;
			try {
				while (tileMap[tileX][tileY].isPassable()) {
					tileY += direction;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
			tileY += (direction < 0)? 1 : 0;
			if (Math.abs(tileY*TILE_HEIGHT - frontBoundary) < Math.abs(maxMovableHeight)) {
				maxMovableHeight = tileY*TILE_HEIGHT - frontBoundary;
			}
		}
		return maxMovableHeight;
	}
	
	public void collideManaSources(Rectangle collisionBox) {
		for (ManaSource s : manaSources) {
			if (s.getBoundaries().intersects(collisionBox)) {
				if (!s.isUsed()) {
					SoundEffectUtility.playSoundEffect(Resource.checkPointSound);
					PlayerStatus.getPlayer().drawNewHand(s.drawCard());
					if (isManaSourceSaving)
						PlayerStatus.getPlayer().savePlayer();
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		int cameraX = GameScreen.getScreen().getCameraX();
		int cameraY = GameScreen.getScreen().getCameraY();
		int firstTileX = cameraX/TILE_WIDTH;
		int firstTileY = cameraY/TILE_HEIGHT;
		int lastTileX = firstTileX + GameScreen.SCREEN_WIDTH/TILE_WIDTH + 1;
		int lastTileY = firstTileY + GameScreen.SCREEN_HEIGHT/TILE_HEIGHT + 1;
		
		g.drawImage(Resource.background, 0, 0, null);
			
		for (ManaSource s : manaSources) {
			s.render(g);
		}
	
		try {
			for (int tileX = firstTileX; tileX <= lastTileX; tileX++) {
				try {
					for (int tileY = firstTileY; tileY <= lastTileY; tileY++) {
						g.drawImage(tileMap[tileX][tileY].getTileSprite(), tileX*TILE_WIDTH-cameraX, tileY*TILE_HEIGHT-cameraY, null);
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		} catch (ArrayIndexOutOfBoundsException e) {}

	}

}
