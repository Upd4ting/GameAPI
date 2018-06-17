package net.upd4ting.gameapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import net.upd4ting.gameapi.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class AntiLeak {
	
	public static void checkBlacklist() {
		try {
			URL url = new URL("https://upd4ting.net/spigotblacklist.html");
		    HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
		    httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
		    
			BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
			String str;
			while ((str = localBufferedReader.readLine()) != null) {
				if (str.contains(GameAPI.uid) || GameAPI.uid == null || Objects.equals(GameAPI.uid, "0")) {
					Util
							.log("§4Your id is blacklisted, Contact the developer at spigotmc. §c" + GameAPI.spigotmclink);
					Util.log("§4Please use the title: §c§lBLACKLISTED PLUGIN!");

					//Bukkit.getPluginManager().disablePlugin(GameAPI.getInstance());

					Class clazz = Bukkit.getPluginManager().getClass();
					Method m = clazz.getDeclaredMethod("disablePlugin", Plugin.class);
					m.invoke(Bukkit.getPluginManager(), GameAPI.getInstance());

					GameAPI.instance = null;
				}
			}
		} catch (Exception ignored) {}
	}
}
