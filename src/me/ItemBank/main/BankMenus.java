package me.ItemBank.main;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BankMenus {
	ItemBank plugin;

	BankMainMenu bankManinMenu;
	DepositOrWithdrawMenu depositOrWithdrawMenu;
	DepositMenu depositMenu;
	WithdrawMenu withdrawMenu;
	ListOfItemsMenu listOfItemsMenu;
	AmountMenu amountMenu;

	public BankMenus(ItemBank plugin) {
		this.plugin = plugin;

		bankManinMenu = new BankMainMenu(plugin, "Item Bank");
		depositOrWithdrawMenu = new DepositOrWithdrawMenu(plugin, "Deposit or Withdraw");
		depositMenu = new DepositMenu(plugin, "Deposit Menu");
		withdrawMenu = new WithdrawMenu(plugin, "Withdraw");
		listOfItemsMenu = new ListOfItemsMenu(plugin, "List of Items");
		amountMenu = new AmountMenu(plugin, "Amount Menu");
	}

	public void setupMenus() {
		bankManinMenu.setup();
		depositOrWithdrawMenu.setup();
		depositMenu.setup();
		withdrawMenu.setup();
		listOfItemsMenu.setup();
		amountMenu.setup();
	}

	public ItemStack makeButton(Material material, String displayName, ArrayList<String> lore) {
		ItemStack button = new ItemStack(material);

		ItemMeta meta = button.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);

		button.setItemMeta(meta);

		return button;
	}

	public ItemStack makeButton(Material material, String displayName) {
		ItemStack button = new ItemStack(material);

		ItemMeta meta = button.getItemMeta();
		meta.setDisplayName(displayName);

		button.setItemMeta(meta);

		return button;
	}
}
