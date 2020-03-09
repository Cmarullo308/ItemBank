package me.ItemBank.main;

import org.bukkit.event.Listener;

import me.ItemBank.main.ItemBank;

public class EventListener implements Listener {
	ItemBank plugin;

	public EventListener(ItemBank plugin) {
		this.plugin = plugin;
	}
}
