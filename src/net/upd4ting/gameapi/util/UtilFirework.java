package net.upd4ting.gameapi.util;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftFirework;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;


public class UtilFirework  {


	/**
	 * Lancer un feu d'artifice aléatoire.
	 *
	 * @param l Location du feu d'artifice
	 * @param type Type du firework
	 * @param power Puissance du feu d'artifice
	 * @param instant Explose instantanément à son lancer.
	 *
	 * @return Custom Firework
	 */
	public static Firework playRandomFireworkColor(Location l, Type type, int power ,boolean instant) {
		Builder fwB = FireworkEffect.builder();
		Random r = new Random();

		fwB.flicker(r.nextBoolean());
		fwB.trail(r.nextBoolean());
		fwB.with(type);
		fwB.withColor(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
		fwB.withFade(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
		FireworkEffect fe = fwB.build();

        return spawnFirework(l, fe, power, instant);
	}

	/**
	 * Lancer un feu d'artifice aléatoire.
	 *
	 * @param l Location du feu d'artifice
	 * @param power Puissance du feu d'artifice
	 * @param instant Explose instantanément à son lancer.
	 *
	 * @return Custom Firework
	 */
	public static Firework playRandomFireworkColor(Location l, int power ,boolean instant) {
		Builder fwB = FireworkEffect.builder();
		Random r = new Random();

		fwB.flicker(r.nextBoolean());
		fwB.trail(r.nextBoolean());
		fwB.with(Type.values()[r.nextInt(Type.values().length)]);
		fwB.withColor(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
		fwB.withFade(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
		FireworkEffect fe = fwB.build();

        return spawnFirework(l, fe, power, instant);
	}

	/**
	 * Lancer un feu d'artifice.
	 *
	 * @param location Location du feu d'artifice
	 * @param effect Type du firework
	 * @param power Puissance du feu d'artifice
	 * @param instant Explose instantanément à son lancer.
	 *
	 * @return Custom Firework
	 */
	public static Firework spawnFirework(Location location, FireworkEffect effect, int power, boolean instant){
		Entity e = location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		Firework f = (Firework)e;
		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(effect);
		if(!instant) {
			fm.setPower(power);
		} else {
			fm.setPower(1);
		}
		f.setFireworkMeta(fm);
		if(instant) {
			((CraftFirework)f).getHandle().expectedLifespan = 1;
		}
		return f;
	}

	/**
	 * Lancer un feu d'artifice aléatoire.
	 *
	 * @param location Location du feu d'artifice
	 * @param instant Explose instantanément à son lancer.
	 *
	 * @return Custom Firework
	 */
	public static Firework spawnRandomFirework(Location location, boolean instant) {
		Random r = new Random();
		
		FireworkEffect effect = FireworkEffect.builder()
				.flicker(r.nextBoolean())
				.trail(r.nextBoolean())
				.with(Type.values()[r.nextInt(Type.values().length)])
				.withColor(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)))
				.withFade(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)))
				.build();
		
		int power = r.nextInt(3);
		return spawnFirework(location, effect, power, instant);
	}

}
