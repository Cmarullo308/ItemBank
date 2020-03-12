package me.ItemBank.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Bank implements Listener {
	ItemBank plugin;

	BankItemsData bankItemsData;
	BankMenus bankMenus;

	public Bank(ItemBank plugin) {
		bankMenus = new BankMenus(plugin);
		bankItemsData = new BankItemsData(plugin);

		this.plugin = plugin;
	}

	public void setup() {
		bankMenus.setupMenus();
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		String inventoryName = event.getView().getTitle();

		// Bank Main Menu
		if (inventoryName.equals(bankMenus.bankManinMenu.menuName)) {
			if (event.getRawSlot() < 18) {
				event.setCancelled(true);
			}
		} else if (inventoryName.equals(bankMenus.depositOrWithdrawMenu.menuName)) {
			if (event.getRawSlot() < 18) {
				event.setCancelled(true);
			}
		} else if (inventoryName.equals(bankMenus.depositMenu.menuName)) {
			if (event.getRawSlot() > 44 && event.getRawSlot() < 54) {
				event.setCancelled(true);
			}
		} else if (inventoryName.equals(bankMenus.withdrawMenu.menuName)) {
			if (event.getRawSlot() < 45) {
				event.setCancelled(true);
			}
		} else if (inventoryName.equals(bankMenus.listOfItemsMenu.menuName)) {
			if (event.getRawSlot() > 44 && event.getRawSlot() < 54) {
				event.setCancelled(true);
			}
		} else if (inventoryName.equals(bankMenus.amountMenu.menuName)) {
			if (event.getRawSlot() < 27) {
				event.setCancelled(true);
			}
		}
	}
}
