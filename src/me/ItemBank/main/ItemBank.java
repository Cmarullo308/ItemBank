package me.ItemBank.main;

import java.text.DecimalFormat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemBank extends JavaPlugin {
	Bank bank;

	String helpMessage;

	DecimalFormat decimalFormat;

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

		this.getServer().getPluginManager().registerEvents(bank, this);

		helpMessage = createHelpMessage();

		decimalFormat = new DecimalFormat("#.##");
		decimalFormat.setGroupingUsed(true);
		decimalFormat.setGroupingSize(3);

		consoleMessage("ItemBank loaded");
	}

	private String createHelpMessage() {
		String message = "";

		message += "Commands:\n";
		message += "/CreateBank (While looking at a sign)\n   Alias: cb\n";
		message += "/OpenBank\n   Alias: ob";

		return message;
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
