package generate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import backends.AppPage;
import engine.Window;

@SuppressWarnings("static-access")
public class DialogBox extends AppPage{
	
	Window window;
	
	Image arrow = null;
	
	ArrayList<String> lines;
	
	String[] words;
	
	Point middle;
	double height, width;
	int lineNum, charNum, charTimer, pageNum;
	Color back, front;
	private boolean running = true;
	
	//Customizable Vars
	int buffer = 50;
	int lineMaxLength = 25;
	int charMaxTimer = 8;
	
	public DialogBox(Window w, String str, Color[] colors) {
		super();
		//variable setup
		window = w;
		arrow = window.getImage("dialogArrow");
		words = str.split(" ");
		middle = new Point(window.WIDTH/2, window.H12 * 8);
		height = window.H12*4;
		width = window.W12*8;
		back = colors[0];
		front = colors[1];
		//setup lines
		lines = new ArrayList<>();
		String temp = "";
		int lineLength = 0;
		for(String s : words) {
			if(s.length() <= lineMaxLength) {
				if(lineLength + s.length() <= lineMaxLength) {
					temp += s + " ";
					lineLength += s.length();
					//if last word, but not enough for a whole line
					if(words[words.length-1].equals(s)) {
						lines.add(temp.substring(0, temp.length()-1));
					}
				}else {
					lines.add(temp.substring(0, temp.length()-1));
					temp = s + " ";
					lineLength = s.length();
				}
			}
		}
	}

	@Override
	public void paint(Graphics2D g) {
		if(running) {
			int tempLeft = (int)(middle.x-width/2);
			int tempLeftBuffer = tempLeft - buffer/2;
			int tempTop = (int)(middle.y-height/2);
			int tempTopBuffer = tempTop - buffer/2;
			int tempRight = (int)(middle.x+width/2);
			int tempRightBuffer = tempRight + buffer/2;
			int tempBottom = (int)(middle.y + height/2);
			int tempBottomBuffer = tempBottom + buffer/2;
			
			g.setColor(front);
			g.fillRect(tempLeftBuffer, tempTopBuffer, (int)width+buffer, (int)height+buffer);
			g.setColor(back);
			g.fillRect(tempRightBuffer, tempTop, buffer/2, (int)height+buffer);
			g.fillRect(tempLeft, tempBottomBuffer, (int)width+buffer, buffer/2);
			//drawing text
			for(int i = 0; i < 4; i++) {
				String temp = "";
				if(pageNum*4+i < lines.size()) {
					if(lineNum == i) {
						temp = lines.get(pageNum*4+i).substring(0, charNum);
					}else if(lineNum > i) {
						temp = lines.get(pageNum*4+i);
					}
				}
				
				double yMath = tempTop + ((double)i/4*height) + buffer;
				g.setColor(Color.black);
				g.drawString(temp, tempLeft, (int)yMath);
			}
			//Drawing the arrow
			if(lineNum >= 4) {
				if(arrow != null) {
					g.drawImage(arrow, tempRight-arrow.getWidth(null)/2, tempBottom-arrow.getHeight(null)/2, null);
				}else {
					g.drawString("NEXT", tempRight, tempBottom);
				}
			}
		}
	}

	@Override
	public void update() {
		//Typewriter Logic
		if(running) {
			String currentLine = lines.get(pageNum*4+lineNum%4);
			if(charNum >= currentLine.length()) {
				charNum = 0;
				lineNum++;
			}else {
				if(charTimer >= charMaxTimer) {
					charTimer = 0;
					charNum++;
				}else {
					charTimer++;
				}
			}
			//Speed Logic
			if(window.keyIsDown(window.ENTER) || window.keyIsDown(window.ENTER_ALT)) {
				if(lineNum >= 4) {
					lineNum = 0;
					charNum = 0;
					pageNum++;
				}
			}else if(window.keyIsDown(window.ACTION) || window.keyIsDown(window.ACTION_ALT)) {
				lineNum = 4;
				charNum = lines.get(lines.size() < pageNum*4+3 ? lines.size()-1 : pageNum*4+3).length() - 2;
			}
			if(pageNum*4 > lines.size()) {
				running = false;
				window.EH.addDestroy(this);
				window.EH.nextEvent();
			}
		}
	}

}
