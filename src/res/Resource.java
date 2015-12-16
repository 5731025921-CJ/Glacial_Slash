package res;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Resource {

	public static BufferedImage tileGround, tileLeft, tileRight, tileMid, tileTop, tileTopLeft, tileTopRight, tileIce;
	public static BufferedImage checkPoint, usedCheckPoint;
	public static BufferedImage[] mana = new BufferedImage[21], maxMana = new BufferedImage[21];
	public static BufferedImage slash;
	public static BufferedImage doubleJump, glacialDrift, iceSummon, skyUppercut, concentration;
	public static BufferedImage[] standSprite = new BufferedImage[2];
	public static BufferedImage[][] jumpSprite = new BufferedImage[2][4];
	public static BufferedImage[][] walkSprite = new BufferedImage[2][6];
	public static BufferedImage[][] cutSprite = new BufferedImage[2][9];
	public static BufferedImage[][] dashSprite = new BufferedImage[2][1];
	public static BufferedImage[][] iceSummonSprite = new BufferedImage[2][8];
	public static BufferedImage[] cardAnimation = new BufferedImage[12];
	public static BufferedImage background;
	public static BufferedImage title, startButton, loadButton, exitButton;
	public static BufferedImage upButton, leftButton, downButton, rightButton, dButton, escButton, fButton, rButton,
	sButton, spaceButton, eButton, plusButton;
	public static BufferedImage walkContent, jumpContent, yourHandContent, doublejumpContent, glacialDriftContent,
	iceSummonContent, skyUppercutContent, exitContent, returnContent, theEndContent, concentrationContent,
	manaSourceContent, yourManaContent;

	public static InputStream tutorialMap, easyMap, normalMap, hardMap, finalMap;
	public static Clip titleBGM, stageBGM;
	public static URL cardSound, checkPointSound, dashSound, doubleJumpSound, iceSummonSound, jumpSound, skyUppercutSound;

	private static ClassLoader loader = Resource.class.getClassLoader();

	static {

		// Load tileset
		tileGround = readImage(loader.getResource("res/tile/tile_ground.png"));
		tileLeft = readImage(loader.getResource("res/tile/tile_left.png"));
		tileRight = readImage(loader.getResource("res/tile/tile_right.png"));
		tileMid = readImage(loader.getResource("res/tile/tile_mid.png"));
		tileTop = readImage(loader.getResource("res/tile/tile_top.png"));
		tileTopLeft = readImage(loader.getResource("res/tile/tile_topleft.png"));
		tileTopRight = readImage(loader.getResource("res/tile/tile_topright.png"));
		tileIce = readImage(loader.getResource("res/tile/tile_ice.png"));
		checkPoint = readImage(loader.getResource("res/tile/manasource.png"));
		usedCheckPoint = readImage(loader.getResource("res/tile/usedmanasource.png"));

		// Load mana display
		for (int i = 0; i <= 20; i++)
			mana[i] = readImage(loader.getResource("res/mana/" + i + ".png"));
		for (int i = 0; i <= 20; i++)
			maxMana[i] = readImage(loader.getResource("res/mana/m" + i + ".png"));
		slash = readImage(loader.getResource("res/mana/slash.png"));

		// Load cards
		doubleJump = readImage(loader.getResource("res/card/DoubleJump.png"));
		glacialDrift = readImage(loader.getResource("res/card/GlacialDrift.png"));
		iceSummon = readImage(loader.getResource("res/card/IceSummon.png"));
		skyUppercut = readImage(loader.getResource("res/card/SkyUppercut.png"));
		concentration = readImage(loader.getResource("res/card/Concentration.png"));

		// Load player sprite
		standSprite[0] = readImage(loader.getResource("res/sprite/stand/stl0.png"));
		standSprite[1] = readImage(loader.getResource("res/sprite/stand/str0.png"));

		for (int i = 0; i < 4; i++) {
			jumpSprite[0][i] = readImage(loader.getResource("res/sprite/jump/j0_" + i + ".png"));
			jumpSprite[1][i] = readImage(loader.getResource("res/sprite/jump/j1_" + i + ".png"));
		}

		for (int i = 0; i < 6; i++) {
			walkSprite[0][i] = readImage(loader.getResource("res/sprite/walk/w0_" + i + ".png"));
			walkSprite[1][i] = readImage(loader.getResource("res/sprite/walk/w1_" + i + ".png"));
		}

		for (int i = 0; i < 9; i++) {
			cutSprite[0][i] = readImage(loader.getResource("res/sprite/cut/c0_" + i + ".png"));
			cutSprite[1][i] = readImage(loader.getResource("res/sprite/cut/c1_" + i + ".png"));
		}

		for (int j = 0; j < 1; j++) {
			dashSprite[0][j] = readImage(loader.getResource("res/sprite/dash/d0_" + j + ".png"));
			dashSprite[1][j] = readImage(loader.getResource("res/sprite/dash/d1_" + j + ".png"));
		}

		for (int i = 0; i < 8; i++) {
			iceSummonSprite[0][i] = readImage(loader.getResource("res/sprite/icesummon/i0_" + i + ".png"));
			iceSummonSprite[1][i] = readImage(loader.getResource("res/sprite/icesummon/i1_" + i + ".png"));
		}

		// Load card animation
		for (int i = 0; i < 12; i++)
			cardAnimation[i] = readImage(loader.getResource("res/card/animation/card_" + i + ".png"));

		background = readImage(loader.getResource("res/map/background.png"));

		// Load title image
		title = readImage(loader.getResource("res/title/title.png"));
		startButton = readImage(loader.getResource("res/title/new.png"));
		loadButton = readImage(loader.getResource("res/title/load.png"));
		exitButton = readImage(loader.getResource("res/title/exit.png"));

		// Load HUD
		upButton = readImage(loader.getResource("res/map/tutorial/up.png"));
		leftButton = readImage(loader.getResource("res/map/tutorial/left.png"));
		downButton = readImage(loader.getResource("res/map/tutorial/down.png"));
		rightButton = readImage(loader.getResource("res/map/tutorial/right.png"));
		dButton = readImage(loader.getResource("res/map/tutorial/D.png"));
		escButton = readImage(loader.getResource("res/map/tutorial/esc.png"));
		fButton = readImage(loader.getResource("res/map/tutorial/F.png"));
		rButton = readImage(loader.getResource("res/map/tutorial/R.png"));
		sButton = readImage(loader.getResource("res/map/tutorial/S.png"));
		spaceButton = readImage(loader.getResource("res/map/tutorial/space.png"));
		eButton = readImage(loader.getResource("res/map/tutorial/E.png"));
		plusButton = readImage(loader.getResource("res/map/tutorial/plus.png"));

		// Load content
		walkContent = readImage(loader.getResource("res/map/content/walk.png"));
		jumpContent = readImage(loader.getResource("res/map/content/jump.png"));
		yourHandContent = readImage(loader.getResource("res/map/content/yourhand.png"));
		exitContent = readImage(loader.getResource("res/map/content/exit.png"));
		returnContent = readImage(loader.getResource("res/map/content/return.png"));
		doublejumpContent = readImage(loader.getResource("res/map/content/doublejump.png"));
		glacialDriftContent = readImage(loader.getResource("res/map/content/glacialdrift.png"));
		iceSummonContent = readImage(loader.getResource("res/map/content/icesummon.png"));
		skyUppercutContent = readImage(loader.getResource("res/map/content/skyuppercut.png"));
		theEndContent = readImage(loader.getResource("res/map/content/the_end.png"));
		concentrationContent = readImage(loader.getResource("res/map/content/concentration.png"));
		manaSourceContent = readImage(loader.getResource("res/map/content/manasource.png"));
		yourManaContent = readImage(loader.getResource("res/map/content/yourmana.png"));

		// load map
		/*
		// Use with MapSerializer
		tutorialMap = loader.getResourceAsStream("res/map/tutorial.txt");
		easyMap = loader.getResourceAsStream("res/map/easy.txt");
		normalMap = loader.getResourceAsStream("res/map/normal.txt");
		hardMap = loader.getResourceAsStream("res/map/hard.txt");
		finalMap = loader.getResourceAsStream("res/map/final.txt");
		*/
		tutorialMap = loader.getResourceAsStream("res/map/tutorial.glm");
		easyMap = loader.getResourceAsStream("res/map/easy.glm");
		normalMap = loader.getResourceAsStream("res/map/normal.glm");
		hardMap = loader.getResourceAsStream("res/map/hard.glm");
		finalMap = loader.getResourceAsStream("res/map/final.glm");
		
		// load sound
		try {
			titleBGM = AudioSystem.getClip();
			titleBGM.open(AudioSystem.getAudioInputStream(loader.getResource("res/sound/titleBGM.wav")));
		} catch (LineUnavailableException e) {
		} catch (UnsupportedAudioFileException e) {
		} catch (IOException e) {}

		try {
			stageBGM = AudioSystem.getClip();
			stageBGM.open(AudioSystem.getAudioInputStream(loader.getResource("res/sound/stageBGM.wav")));
		} catch (LineUnavailableException e) {
		} catch (UnsupportedAudioFileException e) {
		} catch (IOException e) {}

		cardSound = loader.getResource("res/sound/soundeffect/card.wav");
		checkPointSound = loader.getResource("res/sound/soundeffect/checkpoint.wav");
		dashSound = loader.getResource("res/sound/soundeffect/dash.wav");
		doubleJumpSound = loader.getResource("res/sound/soundeffect/doublejump.wav");
		iceSummonSound = loader.getResource("res/sound/soundeffect/icesummon.wav");
		jumpSound = loader.getResource("res/sound/soundeffect/jump.wav");
		skyUppercutSound = loader.getResource("res/sound/soundeffect/skyuppercut.wav");

	}

	private static BufferedImage readImage(URL in) {
		try {
			return ImageIO.read(in);
		} catch (IOException e) {
			return null;
		}
	}

}
