package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import exception.UnableToLoadGameException;
import player.PlayerStatus;
import res.Resource;

public class TitleScreen extends JComponent {

	private static final long serialVersionUID = 8480810473369561460L;
	
	private static TitleScreen screen;
	
	public static synchronized TitleScreen getScreen() {
		if (screen == null)
			screen = new TitleScreen();
		return screen;
	}
	
	private TitleScreen() {
		
		this.setPreferredSize(new Dimension(GameScreen.SCREEN_WIDTH, GameScreen.SCREEN_HEIGHT));
		
		Ellipse2D newGameButton = new Ellipse2D.Double(120, 400, 200, 200);
		Ellipse2D loadGameButton = new Ellipse2D.Double(540, 400, 200, 200);
		Ellipse2D exitGameButton = new Ellipse2D.Double(960, 400, 200, 200);

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				Point pressed = e.getPoint();
				if (newGameButton.contains(pressed)) {
					JOptionPane.showMessageDialog(null, "Choose save file location\nFile name will automatically be appended with \".gls\"", "New game", JOptionPane.PLAIN_MESSAGE);
					JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						PlayerStatus.newPlayer(appendGLS(fileChooser.getSelectedFile()).getPath());
						synchronized (TitleWindow.getWindow()) {
							TitleWindow.getWindow().notifyAll();
						}
					}
				}
				else if (loadGameButton.contains(pressed)) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(new FileNameExtensionFilter("Glacial Slash save", "gls"));
					if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						try {
							PlayerStatus.loadPlayer(appendGLS(fileChooser.getSelectedFile()).getPath());
						} catch (UnableToLoadGameException e1) {
							return;
						}
						synchronized (TitleWindow.getWindow()) {
							TitleWindow.getWindow().notifyAll();
						}
					}
				}
				else if (exitGameButton.contains(pressed)) {
					System.exit(0);
				}
			}
		});

	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(Resource.title, null, 0, 0);
		g2d.drawImage(Resource.startButton, null, 120, 400);
		g2d.drawImage(Resource.loadButton, null, 540, 400);
		g2d.drawImage(Resource.exitButton, null, 960, 400);
	}
	
	private static File appendGLS(File f) {
		String filePath = f.getPath();
		String extension = "";
		int lastDot = filePath.lastIndexOf(".");
		if (lastDot >= 0)
			extension = filePath.substring(lastDot+1);
		if (!"gls".equalsIgnoreCase(extension))
			return new File(f.toString() + ".gls");
		else
			return f;
	}

}
