// One time use for creating map files

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import map.GameMap;

public class MapSerializer {

	public static void main(String[] args) {
		serializeMap("tutorial");
		serializeMap("easy");
		serializeMap("normal");
		serializeMap("hard");
		serializeMap("final");
	}

	private static void serializeMap(String mapName) {
		File mapFile = new File("src/res/map/" + mapName + ".glm");
		try (FileOutputStream fileOut = new FileOutputStream(mapFile)) {
			if (!mapFile.exists())
				mapFile.createNewFile();
			try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
				out.writeObject(GameMap.getGameMap(mapName));
			} catch (IOException e) {}
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		} catch (IOException e) {}
	}

}
