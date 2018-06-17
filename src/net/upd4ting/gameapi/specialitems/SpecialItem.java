package net.upd4ting.gameapi.specialitems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.upd4ting.gameapi.GameAPI;

@SuppressWarnings({"deprecation", "DeprecatedIsStillUsed", "JavaDoc"})
public abstract class SpecialItem {

	private static final HashMap<Integer, SpecialItem>	registeredItems			= new HashMap<>();
	private static boolean							enabled					= false;
	private static int								autoIncrement;

	private ItemStack								itemStack;
	private String									name;
	private List<String>							lore;
	private int										id;

	// Les réactions du item aux listeners
	private boolean									rightClickable			= false;
	private boolean									leftClickable			= false;
	private boolean									droppable				= true;
	private boolean									inventoryClickable		= false;
	private boolean									movable					= true;

	// Les interactions à ne PAS considérer (porte/coffres) lors des actions
	private final List<Material>							disabledMaterialClick	= new ArrayList<>();

	// Désactive l'effet vanilla de l'interact ou non
	private boolean									cancelInteractEvent		= true;

	// Items spéciales
	private int										opLevel					= 0;
	private int										vipLevel				= 0;

	public SpecialItem(String name, String[] lore, ItemStack itemStack) {
		this.name = name;
		this.lore = lore != null ? Arrays.asList(lore) : null;
		this.itemStack = itemStack;
		buildItem();
	}

	// Plus rapide > Java 7
	public SpecialItem(String name, ItemStack itemStack, String... lore) {
		this.name = name;
		this.lore = lore != null ? Arrays.asList(lore) : null;
		this.itemStack = itemStack;
		buildItem();
	}

	/**
	 * Renommer un specialItem sans affecter son ID
	 * @param newName The new name
	 */
	public static void rename(ItemStack itemStack, String newName) {
		SpecialItem specialItem = getSpecialItem(itemStack);
		if (specialItem != null) {
			ItemMeta im = itemStack.getItemMeta();
			im.setDisplayName(specialItem.encodeInName(newName, specialItem.id));
			itemStack.setItemMeta(im);
		}
	}

	/**
	 * Obtenir un nom encodé sans retoucher à l'objet courant (Prévu pour usage interne - notament un build levelInfos)
	 * @param newName The new name
	 */
	protected String getEncodedNewName(String newName) {
		return encodeInName(newName, id);
	}

	/**
	 * L'itemStack est bien ce spécialItem (vérif par itemID)
	 * @param itemStack The itemstack
	 * @return If that's the same or not
	 */
	public boolean isTheSame(ItemStack itemStack) {
		SpecialItem item = getSpecialItem(itemStack);
		return item != null && this.id == item.id;
	}

	/**
	 * Vérifie que l'itemID soit l'item dans la main du joueur
	 * @param player The player
	 * @return If item in hand or not
	 */
	public boolean isItemInHand(Player player) {
		return isTheSame(player.getItemInHand());
	}

	/**
	 * Recupère la lore
	 * @return List of lores
	 */
	public List<String> getLore() {
		if (itemStack.getItemMeta().getLore() == null) return new ArrayList<>();
		return itemStack.getItemMeta().getLore();
	}

	/**
	 * Crée l'itemStack
	 */
	private void buildItem() {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(name);
		if (lore != null) itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
	}

	/**
	 * Recupère un specialItem par son nom
	 * @param id The id
	 * @return le specialItem
	 */
	public static SpecialItem get(Integer id) {
		return registeredItems.get(id);
	}

	/**
	 * Enregistre un nouveau item
	 * @param item
	 */
	public static int registerItem(SpecialItem item) {
		if (!enabled) registerListeners();
		item.encodeItemStack();
		registeredItems.put(item.getId(), item);
		return item.getId();
	}

	/**
	 * Supprimer un SpecialItem
	 */
	public void remove() {
		registeredItems.remove(getId());
	}

	/**
	 * Enregistrer les listeners
	 */
	private static void registerListeners() {
		JavaPlugin plugin = GameAPI.getInstance();
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new SpecialItemInventoryMoveItemListener(), plugin);
		pm.registerEvents(new SpecialItemPlayerInteractListener(), plugin);
		pm.registerEvents(new SpecialItemPlayerInteractEntityListener(), plugin);
		pm.registerEvents(new SpecialItemPlayerDropItemListener(), plugin);
		pm.registerEvents(new SpecialItemPlayerItemConsumeListener(), plugin);
		enabled = true;
	}

	/**
	 * Recupère l'item à partir d'un itemStack
	 * @param itemStack
	 * @return le item ou null
	 */
	public static SpecialItem getSpecialItem(ItemStack itemStack) {
		if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getItemMeta().hasDisplayName()) {
			return registeredItems.get(decodeId(itemStack.getItemMeta().getDisplayName()));
		}
		return null;
	}

	/**
	 * Effet lié à l'activation de l'objet par un clic-droit Passé en deprecated pour notifier de la presence des deux
	 * méthodes appelantes capables de travailler directement sur les listeners ciblés Ne pas oublier de passer l'item en
	 * RightClickable
	 * @see #rightClickEvent(PlayerInteractEvent listener)
	 * @see #rightClickEvent(InventoryClickEvent listener)
	 * @param player
	 * @deprecated
	 */
	protected void rightClickEvent(Player player) {
		// Do Nothing
	}

	/**
	 * Click droit suite à une interaction (hors inventaire) sur un block d'air ou un block plein. Ne pas oublier de
	 * passer l'item en RightClickable En cas d'override pensez à récupérer le bout de code : if (listener.hasBlock() &&
	 * getDisabledMaterialClick().contains(listener.getClickedBlock().getType())) Qui permet d'exclure certains type de
	 * block afin de permettre l'interaction.
	 * @param event
	 */
	protected void rightClickEvent(PlayerInteractEvent event) {
		// Bloc non déclencheur
		if (event.hasBlock() && getDisabledMaterialClick().contains(event.getClickedBlock().getType())) return;
		rightClickEvent(event.getPlayer());
		if (cancelInteractEvent) event.setCancelled(true);
	}

	/**
	 * Click droit dans l'inventaire Ne pas oublier de passer l'item en RightClickable
	 * @param event
	 */
	protected void rightClickEvent(InventoryClickEvent event) {
		rightClickEvent((Player) event.getWhoClicked());
		event.setCancelled(true);
	}

	/**
	 * Effet lié à l'activation de l'objet par un clic-gauche Passé en deprecated pour notifier de la presence des deux
	 * méthodes appelantes capables de travailler directement sur les listeners ciblés Ne pas oublier de passer l'item en
	 * LeftClickable
	 * @see #leftClickEvent(PlayerInteractEvent listener)
	 * @see #leftClickEvent(InventoryClickEvent listener)
	 * @param player
	 * @deprecated
	 */
	protected void leftClickEvent(Player player) {
		// Do Nothing
	}

	/**
	 * Click gauche suite à une interaction (hors inventaire) sur un block d'air ou un block plein. Ne pas oublier de
	 * passer l'item en LeftClickable En cas d'override pensez à récupérer le bout de code : if (listener.hasBlock() &&
	 * getDisabledMaterialClick().contains(listener.getClickedBlock().getType())) Qui permet d'exclure certains type de
	 * block afin de permettre l'interaction.
	 * @param event
	 */
	protected void leftClickEvent(PlayerInteractEvent event) {
		// Bloc non déclencheur
		if (event.hasBlock() && getDisabledMaterialClick().contains(event.getClickedBlock().getType())) return;
		leftClickEvent(event.getPlayer());
		if (cancelInteractEvent) event.setCancelled(true);
	}

	/**
	 * Click gauche dans l'inventaire Ne pas oublier de passer l'item en LeftClickable
	 * @param event
	 */
	protected void leftClickEvent(InventoryClickEvent event) {
		leftClickEvent((Player) event.getWhoClicked());
		event.setCancelled(true);
	}

	/**
	 * Effet lié à l'activation de l'objet par un clic molette
	 * @param player
	 */
	public void middleClickEvent(Player player) {
		// Do Nothing
	}

	/**
	 * Middle click inventory listener
	 * @param event
	 */
	public void middleClickEvent(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) middleClickEvent((Player) event.getWhoClicked());
	}

	/**
	 * Clic dans l'inventaire (action par défaut)
	 * @param player
	 */
	public abstract void inventoryClickEvent(Player player);

	/**
	 * Si besoin d'override et recup l'listener complet
	 * @param event
	 */
	protected void inventoryClickEvent(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) inventoryClickEvent((Player) event.getWhoClicked());
	}

	/**
	 * Encode le nom dans l'objet
	 */
	private SpecialItem encodeItemStack() {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(encodeInName(itemMeta.getDisplayName(), id = autoIncrement++));
		itemStack.setItemMeta(itemMeta);
		return this;
	}

	/**
	 * Cache l'identifiant de l'objet dans son nom
	 * @param itemName
	 * @param itemID
	 * @return
	 */
	private String encodeInName(String itemName, int itemID) {
		String id = Integer.toString(itemID);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < id.length(); i++) {
			builder.append("§").append(id.charAt(i));
		}
		return builder + "§S" + itemName;
	}

	/**
	 * Decode l'identifiant de l'objet
	 * @param itemName
	 * @return
	 */
	private static int decodeId(String itemName) {
		int intId = -1;
		if (itemName.contains("§S")) {
			String[] stringID = itemName.split("§S");
			if (stringID.length > 0) {
				itemName = stringID[0].replaceAll("§", "");
				try {
					intId = Integer.parseInt(itemName);
				} catch (NumberFormatException ignored) {
				}
			}
		}
		return intId;
	}

	protected void dropEvent(PlayerDropItemEvent e) {
		
	}
	
	/*
	 * ²² Get et Set
	 */

	/**
	 * Recupère l'objet sans attributs
	 * @return
	 */
	public ItemStack getStaticItem() {
		return itemStack;
	}

	/**
	 * L'objet cloné (éditable)
	 * @return
	 */
	public ItemStack getClonedItem() {
		return itemStack.clone();
	}
	/**
	 * L'objet cloné (éditable) avec un amount et ou une durabilité
	 * @param amount
	 * @param durability
	 * @return
	 */
	public ItemStack getClonedItem(int amount, short durability) {
		ItemStack item = itemStack.clone();
		if (amount > 1) item.setAmount(amount);
		if (durability > 0) item.setDurability(durability);
		return item;
	}

	public void setItem(ItemStack item) {
		this.itemStack = item;
	}

	/**
	 * Le nom de l'objet avec son ID
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Recupère le nom décodé de l'ID, sans son identifiant
	 * @return
	 */
	public String getUnEncodedName() {
		String result = name;
		if (result.contains("§S")) {
			String[] stringID = result.split("§S");
			if (stringID.length > 1) {
				result = stringID[1];
			}
		}
		return result;
	}

	/**
	 * Le nom de l'objet, si modification direct l'objet perd son ID est devient inutilisable
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Recupère la lore de l'objet actuel != de celle stocké par item
	 * @return La lore ou une nouvelle lore vierge
	 */
	public List<String> getItemLore() {
		return this.getStaticItem().getItemMeta().getLore() == null ? new ArrayList<>() : this.getStaticItem().getItemMeta().getLore();
	}

	/**
	 * Recupère la lore à appliquer à l'objet
	 * @return
	 */
	public List<String> getCustomLore() {
		return lore;
	}

	/**
	 * Définir la lore de l'objet
	 * @param lore
	 */
	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	/**
	 * L'objet s'active sur un clic droit ?
	 * @return
	 */
	public boolean isRightClickable() {
		return rightClickable;
	}

	/**
	 * Active ou non le clic droit de l'objet
	 * @param rightClickable
	 */
	protected void setRightClickable(boolean rightClickable) {
		this.rightClickable = rightClickable;
	}

	/**
	 * L'objet s'active sur un clic gauche ?
	 * @return
	 */
	public boolean isLeftClickable() {
		return leftClickable;
	}

	/**
	 * Active ou non le clic gauche de l'objet
	 * @param leftClickable
	 */
	protected void setLeftClickable(boolean leftClickable) {
		this.leftClickable = leftClickable;
	}

	/**
	 * L'objet peut être jetté par le joueur
	 * @return
	 */
	public boolean isDroppable() {
		return droppable;
	}

	/**
	 * Faire en sorte que l'objet puisse être jetté par le joueur
	 * @param droppable
	 */
	public void setDroppable(boolean droppable) {
		this.droppable = droppable;
	}

	/**
	 * L'objet est clickable dans l'inventaire ?
	 */
	public boolean isInventoryClickable() {
		return inventoryClickable;
	}

	/**
	 * Définit si l'objet est clickable dans l'inventaire
	 * @param inventoryClickable
	 */
	protected void setInventoryClickable(boolean inventoryClickable) {
		this.inventoryClickable = inventoryClickable;
	}

	/**
	 * L'id caché de l'objet
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * L'id caché de l'objet
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * L'OP level de l'objet
	 * @return
	 */
	public int getOpLevel() {
		return opLevel;
	}

	/**
	 * Définir l'OP level de l'objet
	 * @param opLevel
	 */
	public void setOpLevel(int opLevel) {
		this.opLevel = opLevel;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public boolean isMovable() {
		return this.movable;
	}

	/**
	 * La liste des matériaux sur lequel l'objet ne s'activera pas et n'annulera pas l'listener
	 * @return
	 */
	public List<Material> getDisabledMaterialClick() {
		return disabledMaterialClick;
	}

	/**
	 * Rajoute un material qui ne sera pas pris en compte lors d'un click
	 * @param material
	 */
	public void addDisabledMaterialClick(Material material) {
		// On sait jamais... pas envie d'utiliser un Set<> xD //Likaos
		if (!disabledMaterialClick.contains(material)) disabledMaterialClick.add(material);
	}

	/**
	 * Rajoute un material qui ne sera pas pris en compte lors d'un click
	 * @param material
	 */
	public void removeDisabledMaterialClick(Material material) {
		disabledMaterialClick.remove(material);
	}

	/**
	 * Liste non exhaustives des materiaux interactifs
	 */
	public static void addDefaultDisabledMaterials(SpecialItem item) {
		item.addDisabledMaterialClick(Material.WOODEN_DOOR);
		item.addDisabledMaterialClick(Material.TRAPPED_CHEST);
		item.addDisabledMaterialClick(Material.TRAP_DOOR);
		item.addDisabledMaterialClick(Material.CHEST);
		item.addDisabledMaterialClick(Material.WOOD_BUTTON);
		item.addDisabledMaterialClick(Material.STONE_BUTTON);
		item.addDisabledMaterialClick(Material.WORKBENCH);
		item.addDisabledMaterialClick(Material.ENDER_CHEST);
		item.addDisabledMaterialClick(Material.BREWING_STAND);
		item.addDisabledMaterialClick(Material.LEVER);
	}

	/**
	 * Lancer un cooldown sur l'objet qui va s'afficher en plus du nom à chaque seconde
	 * @param player le joueur
	 * @param cooldown en secondes uniquement, pour des méthodes plus précises débrouillez vous xD
	 */
	public void runCooldown(Player player, int cooldown) {
		new SpecialItemCooldownIndicatorRunnable(player, this, cooldown).start();
	}

	/**
	 * Lancer un cooldown sur l'objet qui va s'afficher en plus du nom à chaque seconde
	 * @param player joueur
	 * @param cooldown cooldown en secondes
	 * @param cdPrefixe prefix avant les secondes
	 * @param cdColor couleurs des secondes
	 * @param cdSuffixe suffixe après les secondes
	 */
	public void runCooldown(Player player, int cooldown, String cdPrefixe, ChatColor cdColor, String cdSuffixe) {
		new SpecialItemCooldownIndicatorRunnable(player, this, cooldown, cdPrefixe, cdColor, cdSuffixe).start();
	}

	/**
	 * Lancer un cooldown sur l'objet qui va s'afficher en plus du nom à chaque seconde
	 * @param player joueur
	 * @param cooldown cooldown en secondes
	 * @param cdPrefixe prefix avant les secondes
	 * @param cdColor couleurs des secondes
	 * @param cdSuffixe suffixe après les secondes
	 * @param reloadingRunnable un runnable à exécuter toutes les secondes pendant le reload
	 * @param readyRunnable un runnable à exécuter à la fin
	 */
	public void runCooldown(Player player, int cooldown, String cdPrefixe, ChatColor cdColor, String cdSuffixe, Runnable reloadingRunnable, Runnable readyRunnable) {
		new SpecialItemCooldownIndicatorRunnable(player, this, cooldown, cdPrefixe, cdColor, cdSuffixe, reloadingRunnable, readyRunnable).start();
	}

	/**
	 * Recupère le premier specialItem dans l'inventaire
	 * @param inventory
	 * @return
	 */
	public ItemStack getFirstInInventory(Inventory inventory) {
		ListIterator<ItemStack> it = inventory.iterator();
		ItemStack item;
		while (it.hasNext()) {
			item = it.next();
			if (this.isTheSame(item)) return item;
		}
		return null;
	}

	/**
	 * Recupère le premier specialItem dans l'inventaire (static)
	 * @param inventory
	 * @param specialItem
	 * @return
	 */
	public static ItemStack getFirstInInventory(Inventory inventory, SpecialItem specialItem) {
		ListIterator<ItemStack> it = inventory.iterator();
		ItemStack item;
		while (it.hasNext()) {
			item = it.next();
			if (specialItem.isTheSame(item)) return item;
		}
		return null;
	}

	/**
	 * Est-ce que l'listener vanilla d'interact est annulé après l'interaction ?
	 * @return
	 */
	public boolean isCancelInteractEvent() {
		return cancelInteractEvent;
	}

	/**
	 * Définit ou non si l'listener est annulé après l'interaction (utile pour les livres :O)
	 * @param cancelInteractEvent
	 */
	public void setCancelInteractEvent(boolean cancelInteractEvent) {
		this.cancelInteractEvent = cancelInteractEvent;
	}

}
