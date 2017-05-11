package com.coalesce.realtime;

import com.coalesce.command.CommandBuilder;
import com.coalesce.command.CommandContext;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.realtime.location.LocationModule;
import com.coalesce.realtime.time.TimeModule;
import org.bukkit.entity.Player;

import java.util.Calendar;

import static org.bukkit.ChatColor.*;

public class RealTimePlugin extends CoPlugin {

	private LocationModule locationModule;
	private TimeModule timeModule;

	@Override
	public void onPluginEnable() throws Exception {

		this.displayName = DARK_AQUA + "RealTime";

		//Create command
		addCommand(new CommandBuilder(this, "realtime")
				.description("Main RealTime Command")
				.executor(this::realTimeCommand)
				.playerOnly()
				.build());

		//Setup modules
		locationModule = new LocationModule(this);
		timeModule = new TimeModule(this);

		addModules(	locationModule,
					timeModule);
	}

	public void realTimeCommand(CommandContext context){

		context.send(GRAY + "" + STRIKETHROUGH + "--------------------------------");
		context.send("");
		context.send(DARK_AQUA +"" + BOLD + " RealTime");
		context.send(GRAY + " Time synchronization in Minecraft");
		context.send("");
		context.send(GRAY + " Your time: " + getTimeString(context.asPlayer()));
		context.send("");
		context.send(GRAY + "" + STRIKETHROUGH + "--------------------------------");

	}

	private String getTimeString(Player player){

		Calendar playerCalendar = timeModule.getTimezomeCalendars().get(player.getUniqueId());

		if (playerCalendar == null){
			return GRAY + "Unknown";
		}

		playerCalendar.setTimeInMillis(System.currentTimeMillis());
		return new StringBuilder().append(WHITE).append(playerCalendar.get(Calendar.HOUR)).append(GRAY).append(":").append(GRAY).append(playerCalendar.get(Calendar.MINUTE)).toString();
	}

	public LocationModule getLocationModule() {
		return locationModule;
	}

	public TimeModule getTimeModule() {
		return timeModule;
	}

}
