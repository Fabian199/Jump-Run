package com.jimdo.Fabian996.JumpAndRun.Commands;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.jimdo.Fabian996.JumpAndRun.Main.JumpMain;
import com.jimdo.Fabian996.JumpAndRun.Utils.Score;

public class jumpCMD implements CommandExecutor {

	public static String Prefix = "§7[§6Alpen§9Jump§7]§r ";
	
	private JumpMain plugin;
	
	public jumpCMD(JumpMain jumpCMD) {
		this.plugin = jumpCMD;
	}

	File file = new File("plugins/Jump", "arena.yml");
	FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("jump")){
			Player p = (Player)cs;
			if(args.length > 2){
				p.sendMessage(Prefix + "Nutze: /jump help");
			}else if(args[0].equalsIgnoreCase("join")){
				join(p);
				return true;
			}else if(args[0].equalsIgnoreCase("leave")){
				leave(p);
				return true;
			}else if(args[0].equalsIgnoreCase("help")){
				help(p);
				return true;
			}else if(args[0].equalsIgnoreCase("set")){
				if(args[1].equalsIgnoreCase("spawn")){
					try {
						setspawn(p);
					} catch (Exception e) {
						e.getMessage();
					}
				}
				return true;
			}else if(args[0].equalsIgnoreCase("credits")){
				credits(p);
				return true;
			}else if(args[0].equalsIgnoreCase("score")){
				score(p);
				return true;
			}else if(args[0].equalsIgnoreCase("build")){
				if(args[1].equalsIgnoreCase("join")){
					this.plugin.inBuild.add(p.getName());
					p.sendMessage(Prefix + "§2Du hast den Bau bereich beigetreten");
					build(p);
					return true;
				}else if(args[1].equalsIgnoreCase("leave")){
					this.plugin.inBuild.remove(p.getName());
					p.getInventory().clear();
					p.sendMessage(Prefix + "§8Du hast den Bau bereich verlassen");
					return true;
				}
			}
		}
		return true;
	}
	
	public void setspawn(Player p) throws Exception{
		 if (p.hasPermission("Jump.admin")){
			 String world = p.getWorld().getName();
			 double x = p.getLocation().getX();
			 double y = p.getLocation().getY();
			 double z = p.getLocation().getZ();
			 double yaw = p.getLocation().getYaw();
			 double pitch = p.getLocation().getPitch();
			 
			 cfg.set("Spawn.World", world);
			 cfg.set("Spawn.PosX", Double.valueOf(x));
			 cfg.set("Spawn.PosY", Double.valueOf(y));
			 cfg.set("Spawn.PosZ", Double.valueOf(z));
			 cfg.set("Spawn.PosYaw", Double.valueOf(yaw));
			 cfg.set("Spawn.PosPitch", Double.valueOf(pitch));
			 cfg.save(file);
			 p.sendMessage(Prefix + "§aSie haben den Spawn der Jump Arena gesetzt !");
		 }
	}
	
	public void join(Player p ){
		if(!this.plugin.inJump.contains(p.getName())){
			if((p.hasPermission("Jump.User")) || (p.hasPermission("Jump.Admin"))){
				this.plugin.oldItems.put(p.getName(), p.getInventory().getContents());
				this.plugin.oldLoc.put(p.getName(), p.getLocation());
				p.getInventory().clear();
				p.updateInventory();
					
				String world = cfg.getString("Spawn.World");
				double x = cfg.getDouble("Spawn.PosX");
				double y = cfg.getDouble("Spawn.PosY");
				double z = cfg.getDouble("Spawn.PosZ");
				double yaw = cfg.getDouble("Spawn.PosYaw");
				double pitch = cfg.getDouble("Spawn.PosPitch");
				
				Location l = new Location(Bukkit.getWorld(world), x, y, z);
				l.setPitch((float)pitch);
				l.setYaw((float)yaw);

				p.teleport(l);
				this.plugin.checkpoint.clear();}
				this.plugin.inJump.add(p.getName());
				this.plugin.gm.put(p.getName(), p.getGameMode());
				p.setGameMode(GameMode.ADVENTURE);
				p.sendMessage(Prefix + "§2Sie befinden sich nun in der Jump Arena! Viel Spaß");
				int food = p.getFoodLevel();
				this.plugin.food.put(p.getName(), Integer.valueOf(food));
				int heart = (int) p.getHealthScale();
				this.plugin.heart.put(p.getName(), Integer.valueOf(heart));
				p.setHealthScale(20);
				p.setFoodLevel(20);
				
				Integer s = Integer.valueOf(Score.score.getInt("score." + p.getName()));
		        Score.score.set("score." + p.getName(), Integer.valueOf(s.intValue() + 1));
		        this.plugin.Scoresave();
			}else{
			p.sendMessage(Prefix + "§cSie befinden sich bereits in der Jump Arena! §7Nutze: §6/jump leave zum verlassen");
		}
	}
		
	public void leave(Player p){
		if(this.plugin.inJump.contains(p.getName())){
			if((p.hasPermission("Jump.User")) || (p.hasPermission("Jump.Admin"))){
				Location loc = (Location)this.plugin.oldLoc.get(p.getName());
				ItemStack[] CItemStack = (ItemStack[])this.plugin.oldItems.get(p.getName());
				
				p.getInventory().clear();
				p.getInventory().setContents(CItemStack);
				p.teleport(loc);
				p.sendMessage(Prefix + "§aSie verließen die Jump Arena!");
				p.setGameMode(GameMode.SURVIVAL);
				p.setFoodLevel(((Integer)this.plugin.food.get(p.getName())).intValue());
				p.setHealthScale(((Integer)this.plugin.heart.get(p.getName())).intValue());
				this.plugin.inJump.remove(p.getName());
				p.setGameMode((GameMode)this.plugin.gm.get(p.getName()));
			}
		}else{
			p.sendMessage(Prefix + "§cSie sind nicht in einer Jump-Arena! §7Nutze: §6/jump join zum beitreten");
		}
	}
	

	public static void build(Player p) {
		ItemStack CheckPoint = new ItemStack(Material.GOLD_BLOCK);
		ItemMeta CheckPointmeta = CheckPoint.getItemMeta();
		CheckPointmeta.setDisplayName("§6Checkpoint Block");
		CheckPoint.setItemMeta(CheckPointmeta);
		
		ItemStack Boost = new ItemStack(Material.TNT);
		ItemMeta Boostmeta = Boost.getItemMeta();
		Boostmeta.setDisplayName("§cBoost Block");
		Boost.setItemMeta(Boostmeta);
		
		ItemStack Speed = new ItemStack(Material.SEA_LANTERN);
		ItemMeta Speedmeta = Speed.getItemMeta();
		Speedmeta.setDisplayName("§3Speed Block");
		Speed.setItemMeta(Speedmeta);
		
		ItemStack Blindness = new ItemStack(Material.COAL_BLOCK);
		ItemMeta Blindnessmeta = Blindness.getItemMeta();
		Blindnessmeta.setDisplayName("§7Blindness Block");
		Blindness.setItemMeta(Blindnessmeta);
		
		ItemStack Todes = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta Todesmeta = Todes.getItemMeta();
		ArrayList<String> Tod = new ArrayList<String>();
		Todesmeta.setDisplayName("§4Todes Block");
		Tod.add("§c-10 Score Punkte");
		Todesmeta.setLore(Tod);
		Todes.setItemMeta(Todesmeta);
		
		ItemStack Jump = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta Jumpmeta = Jump.getItemMeta();
		Jumpmeta.setDisplayName("§aJump Block");
		Jump.setItemMeta(Jumpmeta);
		
		ItemStack Fertig = new ItemStack(Material.DIAMOND_BLOCK);
		ItemMeta Fertigmeta = Fertig.getItemMeta();
		ArrayList<String> Finish = new ArrayList<String>();
		Fertigmeta.setDisplayName("§bZiel Block");
		Finish.add("§a+100 Score Punkte");
		Fertigmeta.setLore(Finish);
		Fertig.setItemMeta(Fertigmeta);
		
		ItemStack Next = new ItemStack(Material.SKULL_ITEM,1 ,(short) 3);
		SkullMeta Nextmeta = (SkullMeta)Next.getItemMeta();
		Nextmeta.setOwner("MHF_ArrowRight");
		Nextmeta.setDisplayName("§9Next");
		Next.setItemMeta(Nextmeta);
		
		p.getInventory().setItem(0, CheckPoint);
		p.getInventory().setItem(2, Boost);
		p.getInventory().setItem(3, Speed);
		p.getInventory().setItem(4, Blindness);
		p.getInventory().setItem(5, Todes);
  		p.getInventory().setItem(6, Jump);
		p.getInventory().setItem(7, Fertig);
		p.getInventory().setItem(8, Next);
	}
	
	public void help(Player p) {
	    if ((p.hasPermission("jump.user")) || (p.hasPermission("jump.admin"))) {
	    	p.sendMessage(Prefix + "§7--------------- §2User §7----------------");
	    	p.sendMessage(Prefix + "§3/jump leave §7die Arena zu verlassen!");
	    	p.sendMessage(Prefix + "§3/jump join §7der Arena beitreten!");
	    	p.sendMessage(Prefix + "§3/jump help §7für Hilfe!");
	    	p.sendMessage(Prefix + "§3/jump credits §7for the credits!");
	    	p.sendMessage(Prefix + "§3/jump score §7Zeigt dir den Score an!");
	    	p.sendMessage(Prefix + "       ");
	    	if (p.hasPermission("Jump.admin")) {
	    		p.sendMessage(Prefix + "§8--------------- §4Admin §8---------------");
	    		p.sendMessage(Prefix + "§3/jump set §7<§6spawn§7> §7Um den Spawn zusetzen!");
	    		p.sendMessage(Prefix + "§3/jump build §7<§6join§7/§6leave§7> §7Bekommst alle Jump Blocke!");
	    	}
	    }
	}
	
	public void credits(Player p) {
		if ((p.hasPermission("jump.user")) || (p.hasPermission("jump.admin"))) {
			p.sendMessage(Prefix + "§7--------------- §9Credits §7-----------------");
			p.sendMessage(Prefix + "§6All coded by Fabian996");
			p.sendMessage(Prefix + "§7--------------- §9Credits §7-----------------");
		}
	}
	
	public void score(Player p) {
		if ((p.hasPermission("jump.user")) || (p.hasPermission("jump.admin"))){
			Integer s = Integer.valueOf(Score.score.getInt("score." + p.getName()));
			Integer w = Integer.valueOf(Score.score.getInt("wins." + p.getName()));
			p.sendMessage(Prefix + "§6Score: " + s +  " §aGewonnen: " + w);
		}
	}
}	
