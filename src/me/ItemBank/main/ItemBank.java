package me.ItemBank.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemBank extends JavaPlugin {
	Bank bank;

	CommandHandler commandHandler = new CommandHandler(this);

	boolean debugMessagesEnabled = true;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		commandHandler.command(sender, command, label, args);
		return true;
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();

		bank = new Bank(this);
		bank.setup();
		bank.bankItemsData.setup();

		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
		this.getServer().getPluginManager().registerEvents(bank, this);

		consoleMessage("ItemBank loaded");
	}

	public void consoleMessage(String message) {
		getLogger().info(message);
	}

	public void consoleMessageD(String message) {
		if (debugMessagesEnabled) {
			getLogger().info(message);
		}
	}
}
