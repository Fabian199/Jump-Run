package com.jimdo.Fabian996.JumpAndRun.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Score {

	public static File scorefile = new File("plugins/Jump", "scores.yml");
	public static FileConfiguration score = YamlConfiguration.loadConfiguration(scorefile);

	public static void save() throws IOException {
		score.save(scorefile);
	}
}
