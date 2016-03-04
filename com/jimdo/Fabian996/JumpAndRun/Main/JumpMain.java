package com.jimdo.Fabian996.JumpAndRun.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.jimdo.Fabian996.JumpAndRun.Commands.jumpCMD;
import com.jimdo.Fabian996.JumpAndRun.Events.SignEvent;
import com.jimdo.Fabian996.JumpAndRun.Listeners.JumpListener;
import com.jimdo.Fabian996.JumpAndRun.Utils.Score;


public class JumpMain extends JavaPlugin{

	public Server SERVER = getServer();
	public ConsoleCommandSender CONSOLE = this.SERVER.getConsoleSender();
	
	public ArrayList<String> inJump = new ArrayList<>();
	public ArrayList<String> inBuild = new ArrayList<>();
	public HashMap<String, Location> oldLoc = new HashMap<>();
	public HashMap<String, ItemStack[]> oldItems = new HashMap<>();
	public HashMap<String, Location> checkpoint = new HashMap<>();
	public HashMap<String, Integer> food = new HashMap<>();
	public HashMap<String, Integer> heart = new HashMap<>();
	public HashMap<String, GameMode> gm = new HashMap<>();
	
	Logger log = getLogger();
	
	@Override
	public void onEnable() {
		//Console
		System.out.println("[AlpenJump] =================================");
		System.out.println("[AlpenJump] Author: " + getDescription().getAuthors());
		System.out.println("[AlpenJump] Version: v" + getDescription().getVersion());
		System.out.println("[AlpenJump] Website: " + getDescription().getWebsite());
		System.out.println("[AlpenJump] Status: Aktiviert");
		System.out.println("[AlpenJump] =================================");
		
		//Register
		registerCommands();
		registerListeners();
		
	}
	
	private void registerListeners() {
		new JumpListener(this);
		getServer().getPluginManager().registerEvents(new SignEvent(), this);
	}

	private void registerCommands() {
		getCommand("jump").setExecutor(new jumpCMD(this));		
	}

	@Override
	public void onDisable() {
		//Console
		System.out.println("[AlpenJump] =================================");
		System.out.println("[AlpenJump] Author: " + getDescription().getAuthors());
		System.out.println("[AlpenJump] Version: v" + getDescription().getVersion());
		System.out.println("[AlpenJump] Website: " + getDescription().getWebsite());
		System.out.println("[AlpenJump] Status: Deaktiviert");
		System.out.println("[AlpenJump] =================================");	
	}
	
	public void Scoresave() {
		try {
			Score.save();
		}catch (IOException e) {
	      e.printStackTrace();
	    }
	}
}
