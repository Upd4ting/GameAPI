package net.upd4ting.gameapi.inventory;

/**
 * Task that update inventory!
 * @author Upd4ting
 *
 */
public class RefreshInventory implements Runnable {

	@Override
	public void run() {
		for (Inventory inv : Inventory.currentInventory.values()){
			if (inv.autoRefresh) 
				inv.refresh(true, false);
		}
	}
}
