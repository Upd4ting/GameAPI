package net.upd4ting.gameapi;

/*public class Kit {
	private static List<Kit> kits = new ArrayList<Kit>();
	
	private Integer id;
	private String name;
	private String permission;
	private Double price;
	private List<String> description;
	private List<ItemStack> items;
	private BlockInfo info;
	private Boolean defaut;
	
	public Kit(String name, List<String> description, String permission, Double price, List<ItemStack> items, BlockInfo info, Boolean defaut) {
		this.id = kits.size();
		this.name = name;
		this.description = description;
		this.permission = permission;
		this.price = price;
		this.items = items;
		this.info = info;
		this.defaut = defaut;
		kits.add(this);
	}
	
	public ItemStack contructItemStack(Player p, Game game) {
		GamePlayer uhcp = GamePlayer.instanceOf(p);
		
		final List<String> desc = new ArrayList<>();
		desc.add("");
		desc.addAll(description);
		desc.add("");
		
		ItemStack item = new ItemStack(info.material, 1, (short) info.data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		
		if (p.hasPermission(permission)) {
			// Si purchase on regarde si il l'a ou si il doit l'acheter
			
			if (uhcp.getSelectedKit() == id) {
				game.sendActionConfigMessage(config.getSelectedKit(), new ActionMessage() {
					@Override
					public void run() {
						desc.add(config.getSelectedKit());
					}
				});
			} else if (kitConfig.isKitPurchaseEnabled()) {
				if (uhcp.hasKit(id)) {
					Util.sendActionConfigMessage(config.getCanSelectKit(), new ActionMessage() {
						@Override
						public void run() {
							desc.add(config.getCanSelectKit());
						}
					});
				} else {
					Util.sendActionConfigMessage(config.getCostKit().replace("%m", Double.toString(price)), new ActionMessage() {
						@Override
						public void run() {
							desc.add(config.getCostKit().replace("%m", Double.toString(price)));
						}
					});
				}
			} else {
				Util.sendActionConfigMessage(config.getCanSelectKit(), new ActionMessage() {
					@Override
					public void run() {
						desc.add(config.getCanSelectKit());
					}
				});
			}
		} else {
			Util.sendActionConfigMessage(config.getDontHavePermissionKit(), new ActionMessage() {
				@Override
				public void run() {
					desc.add(config.getDontHavePermissionKit());
				}
			});
		}
		
		im.setLore(desc);
		item.setItemMeta(im);
		
		return item;
	}
	
	public void give(Player p) {
		for (ItemStack i : items)
			p.getInventory().addItem(i);
		p.updateInventory();
	}
	
	public Integer getID() { return id; }
	public String getName() { return name; }
	public String getPermission() { return permission; }
	public Double getPrice() { return price; }
	public List<String> getDescription() { return description; }
	public List<ItemStack> getItems() { return items; }
	public BlockInfo getInfo() { return info; }
	public Boolean isDefault() { return defaut; }
	
	public static Kit createKit(String name, List<String> description, String permission, Double price, List<ItemStack> items, BlockInfo info, Boolean defaut) {
		return new Kit(name, description, permission, price, items, info, defaut);
	}
	
	public static Kit getKitByID(Integer id) {
		for (Kit k : kits) {
			if (k.getID() == id)
				return k;
		}
		
		return null;
	}
	
	public static Kit getDefaultKit() {
		for (Kit k : kits) {
			if (k.isDefault())
				return k;
		}
		return null;
	}
	
	public static List<Kit> getKits() {
		return kits;
	}
}
*/