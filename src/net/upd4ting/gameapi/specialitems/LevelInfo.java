package net.upd4ting.gameapi.specialitems;

import java.util.List;

/**
 * Donne des String différentes en fonction du niveau de l'objet
 * @author Likaos
 *
 */
public class LevelInfo {

	private List<String>	lore;

	public LevelInfo(List<String> lore) {
		this.lore = lore;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

}
