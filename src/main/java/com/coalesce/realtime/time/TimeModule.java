package com.coalesce.realtime.time;

import com.coalesce.plugin.CoModule;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class TimeModule extends CoModule {

	private BukkitTask timeUpdater;
	//May change this to a config option. As of now, it is set to 1 min
	private static final int UPDATE_SPEED = 60 * 20;

	public TimeModule(CoPlugin plugin) {
		super(plugin, "Time Module");
	}

	@Override
	protected void onEnable() throws Exception {

		timeUpdater = Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {

			

		}, UPDATE_SPEED, UPDATE_SPEED);

	}

	@Override
	protected void onDisable() throws Exception {

	}
}
