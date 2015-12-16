/*
 * The physical player playing the game
 * Shows the status on the screen, such as mana and hand
 */

package player;

import java.util.List;

import javax.swing.JOptionPane;

import exception.SkillCardUnusableException;
import exception.UnableToLoadGameException;
import map.FinalMap;
import map.GameMap;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import render.Renderable;
import res.Resource;
import ui.GameScreen;

public class PlayerStatus implements Renderable, Serializable {
	
	private static final long serialVersionUID = -9047898387025697426L;

	private static PlayerStatus player;
	private int currentMana, maxMana;
	private int originalFacingDirection;
	private List<SkillCard> hand;
	private GameMap currentMap;
	private transient PlayerCharacter playerCharacter;
	private Point lastCheckPointPosition;
	private String saveLocation;
	private transient Thread removeCardFromHandThread;
	
	public static synchronized PlayerStatus newPlayer(String saveLocation) {
		player = new PlayerStatus();
		player.saveLocation = saveLocation;
		player.savePlayer();
		return player;
	}
	
	public static synchronized void loadPlayer(String saveLocation) throws UnableToLoadGameException {
		try (FileInputStream fileIn = new FileInputStream(saveLocation)) {
			try (ObjectInputStream in = new ObjectInputStream(fileIn)) {
				player = (PlayerStatus)in.readObject();
			} catch (ClassNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "Unable to load the game", "Error", JOptionPane.ERROR_MESSAGE);
				throw new UnableToLoadGameException();
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(null, "Unable to load the game", "Error", JOptionPane.ERROR_MESSAGE);
				throw new UnableToLoadGameException();
			}
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "File is not found", "Error", JOptionPane.ERROR_MESSAGE);
			throw new UnableToLoadGameException();
		} catch (IOException e2) {
			JOptionPane.showMessageDialog(null, "Unable to load the game", "Error", JOptionPane.ERROR_MESSAGE);
			throw new UnableToLoadGameException();
		}
		player.saveLocation = saveLocation;
		player.getPlayerCharacter().setFacingDirection(player.originalFacingDirection);
	}
	
	public synchronized void savePlayer() {
		lastCheckPointPosition.setLocation(playerCharacter.getX(), playerCharacter.getY() + Resource.standSprite[0].getHeight());
		originalFacingDirection = playerCharacter.getFacingDirection();
		try (FileOutputStream fileOut = new FileOutputStream(saveLocation)){
			try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
				out.writeObject(player);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Unable to save the game", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "File is not found", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e2) {
			JOptionPane.showMessageDialog(null, "Unable to save the game", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static synchronized PlayerStatus getPlayer() {
		return player;
	}

	private PlayerStatus() {
		maxMana = 0;
		currentMana = 0;
		hand = new ArrayList<>();
		originalFacingDirection = PlayerCharacter.RIGHT;
		currentMap = GameMap.getGameMap("tutorial");
		lastCheckPointPosition = currentMap.getInitialPosition();
		playerCharacter = new PlayerCharacter();
		playerCharacter.setPosition(lastCheckPointPosition);
		playerCharacter.setFacingDirection(originalFacingDirection);
	}
	
	public PlayerCharacter getPlayerCharacter() {
		return playerCharacter;
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		playerCharacter = new PlayerCharacter();
		playerCharacter.setPosition(lastCheckPointPosition);
	}
	
	protected void chargeMana() {
		maxMana++;
		currentMana = maxMana;
	}
	
	protected List<SkillCard> getHand() {
		return hand;
	}

	protected void useCard(SkillCard used) throws SkillCardUnusableException {
		SkillCard using;
		try {
			using = hand.get(hand.indexOf(used));
			if (currentMana >= using.cost) {
				using.activate();
				currentMana -= using.cost;
				removeCardFromHandThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							using.joinActivateAnimationThread();
						} catch (InterruptedException e) {
							return;
						}
						synchronized (hand) {
							hand.remove(using);
						}
					}
				});
				removeCardFromHandThread.start();
			}
			else throw new SkillCardUnusableException(SkillCardUnusableException.UnusableType.NOT_ENOUGH_MANA);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new SkillCardUnusableException(SkillCardUnusableException.UnusableType.NO_SUCH_CARD_IN_HAND);
		}
	}
	
	public GameMap getCurrentMap() {
		return currentMap;
	}
	
	public void drawNewHand(List<SkillCard> newHand) {
		chargeMana();
		try {
			removeCardFromHandThread.interrupt();
		} catch (NullPointerException e) {}
		synchronized (hand) {
			hand = newHand;
		}
	}

	protected void returnToLastCheckPoint() {
		try {
			loadPlayer(saveLocation);
		} catch (UnableToLoadGameException e) {
			System.exit(0);
		}
	}
	
	protected void goToNextMap() {
		currentMap = currentMap.getNextMap();
		lastCheckPointPosition = currentMap.getInitialPosition();
		playerCharacter.setPosition(lastCheckPointPosition);
		savePlayer();
	}

	@Override
	public void render(Graphics2D g) {
		// render mana
		if (maxMana != 0) { // Since division by zero is undefined
			g.drawImage(Resource.mana[currentMana], null, 20, 20);
			g.drawImage(Resource.slash, null, 80, 20);
			g.drawImage(Resource.maxMana[maxMana], null, 130, 50);
		}
		
		// render informations
		g.drawImage(Resource.escButton, null, 1160, -20);
		g.drawImage(Resource.exitContent, null, 1160, 20);
		g.drawImage(Resource.returnContent, null, 1000, 70);
		g.drawImage(Resource.rButton, null, 1160, 30);
		
		// render cards in hand
		synchronized (hand) {
			int n = hand.size();
			for (int i = 0; i < n; i++) {
				int x = 10 + SkillCard.CARD_IMAGE_WIDTH*i;
				int y = GameScreen.SCREEN_HEIGHT - (SkillCard.CARD_IMAGE_HEIGHT + 20);
				g.drawImage((BufferedImage)hand.get(i).cardImage, null, x, y);
			}
		}
	
		// render end game text
		if (currentMap instanceof FinalMap) {
			((FinalMap)currentMap).renderTheEndContent(g);
		}
	}

}
