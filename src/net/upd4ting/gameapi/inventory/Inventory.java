package net.upd4ting.gameapi.inventory;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import net.upd4ting.gameapi.task.TaskManager;

public abstract class Inventory {
    public static final HashMap<UUID, Inventory> currentInventory = new HashMap<>();

    protected final String  title;
    protected final Integer size;
    protected Integer page;
    protected Integer maxPage;
    protected final Player  player;
    protected final Boolean autoRefresh;
    private final HashMap<Integer, InventoryItem[]> pageItems = new HashMap<>();

    public Inventory(String title, Integer size, Player player, Boolean autoRefresh) {
        this.title = title;
        this.size = size;
        this.player = player;
        this.page = 1;
        this.maxPage = 1;
        this.autoRefresh = autoRefresh;
    }

    public abstract void init();

    public void open() {
        init();
        refresh(false, false);
    }

    public void clear() {
        Integer index;
        for (InventoryItem[] list : pageItems.values()) {
            index = 0;
            for (InventoryItem i : list) {
                if (i != null && i.isRefreshable())
                    list[index] = null;
                index++;
            }
        }
    }

    public void refresh(Boolean refresh, Boolean changedPage) {
        if (refresh) {
            clear();
            init();
        }

        InventoryItem[] items = pageItems.getOrDefault(page, new InventoryItem[size]);

        ItemStack[] is = new ItemStack[items.length];

        if (refresh && !changedPage)
            is = player.getOpenInventory().getTopInventory().getContents();

        //Ajout des items de la page actuelle
        for (int i = 0; i <= items.length - 1; i++) {
            if (items[i] == null || (!items[i].isRefreshable() && refresh && !changedPage)) continue;
            //if (is[i] != null && items[i] == null) is[i] = null; // Supression des items supprimé durant l'update
            is[i] = items[i].getItem();
        }

        //Ajout des items situé a toute les pages !
        InventoryItem[] items2 = pageItems.getOrDefault(-1, new InventoryItem[size]);
        for (int i = 0; i <= items2.length - 1; i++) {
            if (items2[i] == null || (!items2[i].isRefreshable() && refresh)) continue;
            is[i] = items2[i].getItem();
        }

        if (!refresh) {
            org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, size, title);
            inv.setContents(is);
            player.openInventory(inv);
        } else {
            org.bukkit.inventory.Inventory viewedInv = player.getOpenInventory().getTopInventory();
            viewedInv.setContents(is);
            player.updateInventory();
        }

        currentInventory.put(player.getUniqueId(), this);
    }

    //Ajout d'un item dans toutes les pages..
    public void addItem(Integer pos, InventoryItem item) {
        InventoryItem[] is = new InventoryItem[size];
        if (pageItems.get(-1) != null) is = pageItems.get(-1);
        is[pos] = item;
        pageItems.put(-1, is);
    }


    //Ajout d'un item dans une page spécifique
    public void addItem(Integer pos, Integer page, InventoryItem item) {
        if (maxPage < page) maxPage = page;
        InventoryItem[] is = new InventoryItem[size];
        if (pageItems.get(page) != null) is = pageItems.get(page);
        is[pos] = item;
        pageItems.put(page, is);
    }

    public void generateReturn(Inventory inv, ItemStack itemstack) {
        addItem(size - 1, new InventoryItem(itemstack, e -> {
            if (inv == null) player.closeInventory();
            else inv.open();
        }));
    }

    public void generateReturn(Inventory inv, Integer pos, ItemStack itemstack) {
        addItem(pos, new InventoryItem(itemstack, event -> {
            if (inv == null) player.closeInventory();
            else inv.open();
        }));
    }

    public void generatePageNavigation(Integer pos, ItemStack itemstack) {
        addItem(pos, new InventoryItem(itemstack, e -> {
            if (e.getClick() == ClickType.LEFT ||
                    e.getClick() == ClickType.SHIFT_LEFT) {
                if (page > 1) {
                    page--;
                    refresh(true, true);
                }
            } else if (e.getClick() == ClickType.RIGHT ||
                    e.getClick() == ClickType.SHIFT_RIGHT) {
                if (page < maxPage) {
                    page++;
                    refresh(true, true);
                }
            }
        }));
    }

    public InventoryItem[] getItems() {
        InventoryItem[] is = pageItems.getOrDefault(page, new InventoryItem[size]);
        InventoryItem[] allPage = pageItems.getOrDefault(-1, new InventoryItem[size]);
        for (int i = 0; i <= allPage.length - 1; i++) {

            if (allPage[i] == null)
                continue;

            is[i] = allPage[i];
        }
        return is;
    }
    
    public static void initialize() {
    	TaskManager.runTask("InventoryRefresh", new RefreshInventory(), 20);
    }
}
