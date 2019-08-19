package generate;

import java.io.Serializable;

public class RandGenDungeon implements Serializable{
	
	private static final long serialVersionUID = -8722286054461845598L;

	int width, height, roomW, roomH;
	
	public boolean FINISH_SETUP = false;
	private boolean forceDoor = false;
	
	/**
	 * @author Xavier Bennett
	 * @param data [number of rooms (width), number of rooms (height), room width, room height]
	 * 
	 *  Room Width
	 *   |------|
	 *  1111111111
	 *  1000000001 -
	 *  1000000001  |Room Height
	 *  1000000001 -
	 *  1111111111
	 */
	public RandGenDungeon(int[] data, boolean forceDoor) {
		if(data.length != 4) {
			System.err.println("Too much/little data input for RandGenDungeon");
			System.exit(-1);
		}
		width = data[0];
		height = data[1];
		roomW = data[2];
		roomH = data[3];
		
		this.forceDoor = forceDoor;
		
		if(width <= 2) {
			System.err.println("width needs to be greater than 2 in RandGenDungeon");
			System.exit(-1);
		}
		if(height <= 2) {
			System.err.println("height needs to be greater than 2 in RandGenDungeon");
			System.exit(-1);
		}
		if(roomW <= 4) {
			System.err.println("roomW needs to be greater than 4 in RandGenDungeon");
			System.exit(-1);
		}
		if(roomH <= 4) {
			System.err.println("roomH needs to be greater than 4 in RandGenDungeon");
			System.exit(-1);
		}
	}
	
	private String[] generate() {
		String[] ret = new String[height*(1+roomH)+1];
		FINISH_SETUP = false;
		
		String row = "1";
		
		//Generating wall
		for(int i = 1; i < width*(1+roomW)+1; i++) {
			row = row + "1";
		}
		
		ret[0] = row;
		
		//generating roomSpace
		row = "1";
		
		for(int i = 1; i < width*(1+roomW)+1; i+=1+roomW) {
			for(int j = i; j < i+roomW; j++) {
				row = row + "0";
			}
			row = row + "1";
		}
		
		//generating Map
		for(int i = 1; i < ret.length; i+=1+roomH) {
			for(int j = i; j < i+roomH; j++) {
				ret[j] = row;
			}
			ret[i+roomH] = ret[0];
		}
		
		return ret;
	}
	
	/**
	 * @author Xavier Bennett
	 * 
	 * @param bools [north, east]
	 * @return a set of directions
	 */
	private String getDirections(boolean[] bools) {
		String ret = "";
		
		if(bools[0]) {
			if(Math.random() > 0.5) {
				ret = "n";
			}
		}
		if(bools[1]) {
			if(ret.isEmpty()) {
				ret = "e";
			}else {
				if(Math.random() > 0.5) {
					ret += "e";
				}
			}
		}
		
		if(ret.isEmpty() && bools[0]) {
			ret = "n";
		}
		
		return ret;
	}
	
	public String[] generateDungeon() {
		
		String[] ret = generate();
		
		String[][] directions = new String[height][width];
		
		//Getting the directions per room
		for(int y = 0; y < directions.length; y++) {
			boolean[] dirs = new boolean[2];
			for(int x = 0; x < directions[y].length; x++) {
				dirs[0] = y > 0;
				dirs[1] = x < directions[y].length-1;
				directions[y][x] = getDirections(dirs);
				if(forceDoor) {
					if(x != directions[y].length-1 && y != 0) {
						while(directions[y][x].equals("")) {
							directions[y][x] = getDirections(dirs);
						}
					}
				}
			}
		}
		
		//Changing "ret" based on directions
		for(int y = 0; y < directions.length; y++) {
			for(int x = 0; x < directions[y].length; x++) {
				char[] dirs = directions[y][x].toCharArray();
				int randomDoor = (int)Math.round(Math.random()*4-2);
				
				String line = "";
				
				if(dirs.length > 0) {
					if(dirs[0] == 'n') {
						line = ret[y*(1+roomH)];
						int halfRoom = (roomW/2);
						line = line.substring(0, halfRoom+x*(roomW+1)+randomDoor) + "0" + line.substring(1+halfRoom+x*(roomW+1)+randomDoor);
						ret[y*(1+roomH)] = line;
					}else if(dirs[0] == 'e') {
						int halfRoom = (roomH/2);
						line = ret[halfRoom+y*(1+roomH)];
						line = line.substring(0, (x+1)*(roomW+1)) + "0" + line.substring(1+(x+1)*(roomW+1));
						ret[halfRoom+y*(1+roomH)] = line;
					}
				}
				if(dirs.length > 1) {
					if(dirs[1] == 'e') {
						int halfRoom = (roomH/2);
						line = ret[halfRoom+y*(1+roomH)+randomDoor+1];
						line = line.substring(0, (x+1)*(roomW+1)) + "0" + line.substring(1+(x+1)*(roomW+1));
						ret[halfRoom+y*(1+roomH)+randomDoor+1] = line;
					}
				}
			}
		}
		
		FINISH_SETUP = true;
		
		//returning the finished dungeon
		return ret;
		
	}
	
}
