package me.ItemBank.main;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;

public class BankItem {
	ItemBank plugin;

	Material material;
	HashMap<UUID, Integer> accountAmounts = new HashMap<UUID, Integer>();

	public BankItem(Material material, ItemBank plugin) {
		this.plugin = plugin;
		this.material = material;
	}

	public int numberOfPeopleWithItem() {
		return accountAmounts.size();
	}

	public void deposit(UUID account, Integer amount) {
		if (accountAmounts.containsKey(account)) {
			accountAmounts.put(account, accountAmounts.get(account) + amount);
		} else {
			accountAmounts.put(account, amount);
		}

		plugin.bankItemsData.saveBankData();
	}

	public String toString() {
		return "BankItem: " + material + "\n" + accountAmounts.toString();
	}
}
