package me.ItemBank.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class BankItemsData {
	ItemBank plugin;

	ConcurrentHashMap<Material, BankItem> bankItemData = new ConcurrentHashMap<Material, BankItem>();

	public FileConfiguration bankFileConfig;
	public File bankFile;

	public BankItemsData(ItemBank plugin) {
		this.plugin = plugin;
	}

	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		bankFile = new File(plugin.getDataFolder(), "bankItemsData.yml");

		if (!bankFile.exists()) {
			try {
				bankFile.createNewFile();
			} catch (IOException e) {
				plugin.getServer().getLogger().info(ChatColor.RED + "Could not create bankItemsData.yml file");
			}
		}

		bankFileConfig = YamlConfiguration.loadConfiguration(bankFile);
		loadBankData();
	}

	private void loadBankData() {
		Set<String> materialNames;
		try {
			materialNames = bankFileConfig.getConfigurationSection("Items").getKeys(false);
		} catch (java.lang.NullPointerException e) {
			saveBankData();
			return;
		}

		// For each item
		for (String materialName : materialNames) {
			Set<String> UUIDs = null;
			try {
				UUIDs = bankFileConfig.getConfigurationSection("Items." + materialName).getKeys(false);
//				plugin.consoleMessage(UUIDs.toString());
			} catch (Exception e) {
				plugin.consoleMessage(e.toString());
			}

			if (UUIDs != null) {
				// For ever ID that has this item
				for (String idString : UUIDs) {
//					plugin.consoleMessage(materialName + " : " + idString);
					UUID id = UUID.fromString(idString);
					Material material = Material.valueOf(materialName);
					Integer amount = Integer
							.parseInt(bankFileConfig.getString("Items." + materialName + "." + idString));
					if (!itemExists(material)) {
						bankItemData.put(material, new BankItem(material, plugin));
					}
					bankItemData.get(material).deposit(id, amount);
				}
//				plugin.consoleMessage("\n");
			}
		}

		saveBankData();
	}

	public void saveBankData() {
		// For each item
		for (BankItem bankItem : bankItemData.values()) {

			if (bankItem.numberOfPeopleWithItem() < 1) {
				bankFileConfig.set("Items." + bankItem.material.toString(), new ArrayList<String>());
			}

			for (UUID id : bankItem.accountAmounts.keySet()) {
				bankFileConfig.set("Items." + bankItem.material.toString() + "." + id, bankItem.accountAmounts.get(id));
			}
		}

		try {
			bankFileConfig.save(bankFile);
		} catch (IOException e) {
			plugin.getServer().getLogger().info(ChatColor.RED + "Could not save bankItemsData.yml file");
		}
	}

	public boolean itemExists(Material material) {
		if (bankItemData.get(material) == null) {
			return false;
		}

		return true;
	}
}
