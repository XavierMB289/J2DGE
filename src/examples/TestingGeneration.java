package examples;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import engine.Window;
import generate.RandGenDungeon;

public class TestingGeneration extends Window{
	
	private static final long serialVersionUID = -7136638971122120827L;
	
	RandGenDungeon rgd = null;
	
	Map<Integer, Boolean> keys = null;
	
	int squareSize = 20;
	
	int xPos = 10;
	int yPos = 10;
	
	String[] dungeon = null;

	public TestingGeneration() {
		super("Testing Generation");
		setWindowSize(new Dimension(400, 400));
		addKeyListen(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				keys.put(e.getKeyCode(), true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keys.put(e.getKeyCode(), false);
			}
			
		});
		
		//Initialize here...
		rgd = new RandGenDungeon(new int[] {4, 5, 10, 7}, true);
		dungeon = rgd.generateDungeon();
		
		keys = new HashMap<>();
		
		setVisible();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics bg = image.getGraphics();
		
		//drawing all the 1s as squares
		if(dungeon.length > 0 && rgd.FINISH_SETUP) {
			for(int y = 0; y < dungeon.length; y++) {
				char[] line = dungeon[y].toCharArray();
				for(int x = 0; x < line.length; x++) {
					if(line[x] == '1') {
						bg.fillRect(x*squareSize+xPos, y*squareSize+yPos, squareSize, squareSize);
					}
				}
			}
		}
		
		g.drawImage(image, 0, 0, null);
	}
	
	public void update() {
		
		if(keys.containsKey(KeyEvent.VK_UP)) {
			if(keys.get(KeyEvent.VK_UP)) {
				yPos+=3;
			}
		}
		if(keys.containsKey(KeyEvent.VK_DOWN)) {
			if(keys.get(KeyEvent.VK_DOWN)) {
				yPos-=3;
			}
		}
		if(keys.containsKey(KeyEvent.VK_LEFT)) {
			if(keys.get(KeyEvent.VK_LEFT)) {
				xPos+=3;
			}
		}
		if(keys.containsKey(KeyEvent.VK_RIGHT)) {
			if(keys.get(KeyEvent.VK_RIGHT)) {
				xPos-=3;
			}
		}
	}
	
}
