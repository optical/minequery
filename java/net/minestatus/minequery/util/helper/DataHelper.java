/*
 * Minequery
 * Copyright (C) 2011 Vex Software LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.minestatus.minequery.util.helper;

import net.minestatus.minequery.Minequery;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper for commonly used data.
 */
public class DataHelper {
	private static Minequery minequery = Minequery.getInstance();

	/**
	 * Gets a <code>Map</code> of all data.
	 *
	 * @return A <code>Map</code> of all data.
	 */
	public static Map<String, Object> getData() {
		Map<String, Object> items = new HashMap<String, Object>();
		items.put("serverName", minequery.getConfiguration().getString("details.server_name"));
		items.put("serverIP", minequery.getServerIP());
		items.put("serverPort", minequery.getServerPort());
		items.put("playerCount", minequery.getServer().getOnlinePlayers().length);
		items.put("maxPlayers", minequery.getServer().getMaxPlayers());
		items.put("playerList", getPlayerList());
		items.put("extendedPlayerList", getExtendedPlayerList());
		items.put("plugins", getPluginList());
		items.put("versions", getVersions());

		return items;
	}

	/**
	 * Obtains all the players currently on the server.
	 *
	 * @return An array of players on the server.
	 */
	private static String[] getPlayerList() {
		String[] playerList = new String[minequery.getServer().getOnlinePlayers().length];
		for (int i = 0; i < minequery.getServer().getOnlinePlayers().length; i++) {
			playerList[i] = minequery.getServer().getOnlinePlayers()[i].getName();
		}

		return playerList;
	}

	/**
	 * Obtains all the players currently on the server with additional information.
	 *
	 * @return A <code>List</code> of <code>Map</code>s of players along with their info.
	 */
	private static List<Map<String, Object>> getExtendedPlayerList() {
		Player[] players = minequery.getServer().getOnlinePlayers();
		List<Map<String, Object>> playerList = new ArrayList<Map<String, Object>>();

		for (Player player : players) {
			Map<String, Object> playerMap = new HashMap<String, Object>();
			playerMap.put("name", player.getName());
			playerMap.put("displayName", player.getDisplayName());
			playerMap.put("health", player.getHealth());

			Map<String, Object> locationMap = new HashMap<String, Object>();
			locationMap.put("blockX", player.getLocation().getBlockX());
			locationMap.put("blockY", player.getLocation().getBlockY());
			locationMap.put("blockZ", player.getLocation().getBlockZ());
			locationMap.put("pitch", player.getLocation().getPitch());
			locationMap.put("x", player.getLocation().getX());
			locationMap.put("y", player.getLocation().getY());
			locationMap.put("z", player.getLocation().getZ());
			locationMap.put("world", player.getLocation().getWorld().getName());

			playerMap.put("location", locationMap);
			playerMap.put("isDead", player.isDead());
			playerMap.put("isSleeping", player.isSleeping());
			playerMap.put("isOp", player.isOp());

			playerList.add(playerMap);
		}

		return playerList;
	}

	/**
	 * Obtains all the plugins currently enabled on the server.
	 *
	 * @return A <code>List</code> of <code>Map</code>s of plugins with their name and version.
	 */
	private static List<Map<String, String>> getPluginList() {
		Plugin[] plugins = minequery.getServer().getPluginManager().getPlugins();
		List<Map<String, String>> pluginList = new ArrayList<Map<String, String>>();

		for (Plugin plugin : plugins) {
			Map<String, String> pluginMap = new HashMap<String, String>();
			pluginMap.put("name", plugin.getDescription().getName());
			pluginMap.put("version", plugin.getDescription().getVersion());
			pluginList.add(pluginMap);
		}

		return pluginList;
	}

	/**
	 * Obtains the version numbers of CraftBukkit, Minecraft, and Minequery.
	 *
	 * @return A <code>Map</code> of versions.
	 */
	private static Map<String, String> getVersions() {
		Map<String, String> versions = new HashMap<String, String>();

		// Find the CraftBukkit build and the Minecraft version.
		String version = minequery.getServer().getVersion();
		Matcher matcher = Pattern.compile("git-Bukkit-.*-b(\\d+)jnks \\(MC: (.*)\\)").matcher(version);
		List<Object> matchList = new ArrayList<Object>();

		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				matchList.add(matcher.group(i));
			}
		}

		// One day the version string could completely change,
		// so check if we have at last two matches.
		if (matchList.size() > 1) {
			versions.put("craftbukkit", matchList.get(0).toString());
			versions.put("minecraft", matchList.get(1).toString());
		}

		// Add the Minequery version.
		versions.put("minequery", minequery.getDescription().getVersion());

		return versions;
	}
}
