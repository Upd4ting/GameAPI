package net.upd4ting.gameapi;

import com.google.gson.*;
import net.upd4ting.gameapi.util.CC;
import net.upd4ting.gameapi.util.Reflector;
import net.upd4ting.gameapi.util.Util;
import org.bukkit.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to save games
 */
public final class GameSaver {
	
	public static void saveAll() {
		for (Game g : GameManager.getGames())
			save(g);
	}
	
	public static void save(Game game) {
		File f = remove(game);

		GsonBuilder gb = getGsonBuilder();

		Gson gson = gb.create();

		String root = gson.toJson(game);

		try {
			Files.write(f.toPath(), Collections.singleton(root), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadSavedGames() {
		File file = new File(GameAPI.getInstance().getDataFolder(), "arenas");

		if (!file.exists())
			return;

		for (File f : file.listFiles()) {
			String nameFile = f.getName();
			String gameName = nameFile.split("-")[0];
			String worldName = nameFile.split("-")[1];

			// Don't load the bungee game we are in multi arena!
			if (gameName.equals("bungee"))
				continue;

			// Verify that world still exist
			File fileWorld = new File(Bukkit.getWorldContainer(), worldName);

			if (!fileWorld.exists()) {
				Util.log(CC.red + "World " + worldName + " of arena " + gameName + " doesn't exist anymore, deleting this arena...");
				f.delete();
				continue;
			}

			// Load arena world and arena
			World world = new WorldCreator(worldName).createWorld();

			Game game = ((GameAPI.GamePlugin)GameAPI.getInstance()).createGame(world, gameName);
			GameManager.registerGame(game);
			Util.log(ChatColor.GREEN + "Arena " + gameName + " fully loaded!");
		}
	}

	public static void load(Game game) {
		File f = get(game);

		if (!f.exists()) return;

		try {
			String root = Files.readAllLines(f.toPath()).get(0);

			GsonBuilder gb = getGsonBuilder();

			Game savedGame = gb.create().fromJson(root, game.getClass());

			List<Field> fields = new ArrayList<>();
			Reflector.getAllFields(fields, game.getClass());

			for (Field field : fields) {
				if (!field.isAnnotationPresent(Game.Changeable.class))
					continue;

				field.setAccessible(true);
				Field fieldSaved = Reflector.getFieldFromAll(savedGame.getClass(), field.getName());
				fieldSaved.setAccessible(true);
				field.set(game, fieldSaved.get(savedGame));
			}
		} catch (IOException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static File remove(Game game) {
		File f = get(game);
		f.delete();

		return f;
	}

	private static File get(Game game) {
		File file = new File(GameAPI.getInstance().getDataFolder(), "arenas");
		file.mkdirs();

        return new File(file, game.getName() + "-" + game.getWorld().getName());
	}

	private static GsonBuilder getGsonBuilder() {
		GsonBuilder gb = new GsonBuilder();
		gb.setExclusionStrategies(new ExclusionStrategy() {
			@Override
			public boolean shouldSkipField(FieldAttributes fieldAttributes) {
				return fieldAttributes.getAnnotation(Game.Changeable.class) == null; // Skip field that does not have annotation changeable
			}

			@Override
			public boolean shouldSkipClass(Class<?> aClass) {
				return false;
			}
		});

		gb.registerTypeAdapter(Location.class, (JsonDeserializer<Location>) (je, t, context) -> {
			JsonObject object = je.getAsJsonObject();
			double x = object.get("x").getAsDouble();
			double y = object.get("y").getAsDouble();
			double z = object.get("z").getAsDouble();
			float yaw = object.get("yaw").getAsFloat();
			float pitch = object.get("pitch").getAsFloat();
			String world = object.get("world").getAsString();

			Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
			return loc;
        });

		gb.registerTypeAdapter(Location.class, new JsonSerializer<Location>() {
			@Override
			public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
				JsonObject object = new JsonObject();
				object.addProperty("x", location.getX());
				object.addProperty("y", location.getY());
				object.addProperty("z", location.getZ());
				object.addProperty("yaw", location.getYaw());
				object.addProperty("pitch", location.getPitch());
				object.addProperty("world", location.getWorld().getName());
				return object;
			}
		});

		return gb;
	}
}
