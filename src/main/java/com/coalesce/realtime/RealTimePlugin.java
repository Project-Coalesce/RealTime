package com.coalesce.realtime;

import com.coalesce.plugin.CoPlugin;
import com.coalesce.realtime.location.LocationModule;
import com.coalesce.realtime.time.TimeModule;

public class RealTimePlugin extends CoPlugin {

	private LocationModule locationModule;
	private TimeModule timeModule;

	@Override
	public void onPluginEnable() throws Exception {

		this.displayName = "RealTime";

		//Setup modules
		locationModule = new LocationModule(this);
		timeModule = new TimeModule(this);

		addModules(	locationModule,
					timeModule);
	}

}
