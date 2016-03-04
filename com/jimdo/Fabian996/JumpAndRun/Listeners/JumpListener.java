package com.jimdo.Fabian996.JumpAndRun.Listeners;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.jimdo.Fabian996.JumpAndRun.Commands.jumpCMD;
import com.jimdo.Fabian996.JumpAndRun.Main.JumpMain;
import com.jimdo.Fabian996.JumpAndRun.Utils.Score;

public class JumpListener implements Listener{

	private JumpMain plugin;
	
	public static String Prefix = "§7[§6Alpen§9Jump§7]§r ";
	
	File file = new File("plugins/Jump", "arena.yml");
	FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public JumpListener(JumpMain jumpMain) {
		this.plugin = jumpMain;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void HotBarClick(PlayerInteractEvent e){
		Player p = (Player)e.getPlayer();
		if((this.plugin.inBuild.contains(p.getName()) && (e.getPlayer().getItemInHand().getType() == Material.SKULL_ITEM))){
			p.getInventory().clear();
			ItemStack Back = new ItemStack(Material.SKULL_ITEM,1 ,(short) 3);
			SkullMeta Backmeta = (SkullMeta)Back.getItemMeta();
			Backmeta.setOwner("MHF_ArrowLeft");
			Backmeta.setDisplayName("§3Back");
			Back.setItemMeta(Backmeta);
			
			ItemStack Leave = new ItemStack(Material.REDSTONE);
			ItemMeta Leavemeta = Leave.getItemMeta();
			Leavemeta.setDisplayName("§3Leave");
			Leave.setItemMeta(Leavemeta);
			
			
			
			p.getInventory().setItem(0, Back);
			p.getInventory().setItem(8, Leave);
			
			if((e.getItem().getType().equals(Material.SKULL_ITEM)) && (e.getItem().getItemMeta().getDisplayName() != null)){
				if((e.getItem().hasItemMeta()) && (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Back"))){
					if(e.getAction() == Action.RIGHT_CLICK_AIR ){
					jumpCMD.build(p);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void HotBarClick2(PlayerInteractEvent e){
		Player p = (Player)e.getPlayer();
		if((this.plugin.inBuild.contains(p.getName()) && (e.getPlayer().getItemInHand().getType() == Material.REDSTONE))){
			p.getInventory().clear();
			p.chat("/jump build leave");
		}
	}
	
	@EventHandler
	public void Wasser(PlayerMoveEvent e){
		Player p = (Player)e.getPlayer();
		if((this.plugin.inJump.contains(p.getName()) && (e.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_WATER) || (e.getPlayer().getLocation().getBlock().getType() == Material.WATER))){

			String world = cfg.getString("Spawn.World");
			double x = cfg.getDouble("Spawn.PosX");
			double y = cfg.getDouble("Spawn.PosY");
			double z = cfg.getDouble("Spawn.PosZ");
			double yaw = cfg.getDouble("Spawn.PosYaw");
			double pitch = cfg.getDouble("Spawn.PosPitch");
			
			Location l = new Location(Bukkit.getWorld(world), x, y, z);
			l.setPitch((float)pitch);
			l.setYaw((float)yaw);
			
			Location loc = (Location)this.plugin.checkpoint.get(p.getName());
			p.teleport(loc);
			
			Integer s = Integer.valueOf(Score.score.getInt("score." + p.getName()) - 5);
			Score.score.set("score." + p.getName(), s);
			this.plugin.Scoresave();
		}
	}
	
	@EventHandler
	public void Lava(PlayerMoveEvent e){
		Player p = (Player)e.getPlayer();
		if((this.plugin.inJump.contains(p.getName()) && (e.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_LAVA) || (e.getPlayer().getLocation().getBlock().getType() == Material.LAVA))){
			
			String world = cfg.getString("Spawn.World");
			double x = cfg.getDouble("Spawn.PosX");
			double y = cfg.getDouble("Spawn.PosY");
			double z = cfg.getDouble("Spawn.PosZ");
			double yaw = cfg.getDouble("Spawn.PosYaw");
			double pitch = cfg.getDouble("Spawn.PosPitch");
			
			Location l = new Location(Bukkit.getWorld(world), x, y, z);
			l.setPitch((float)pitch);
			l.setYaw((float)yaw);
			
			Location loc = (Location)this.plugin.checkpoint.get(p.getName());
			p.setFireTicks(0);
			p.getActivePotionEffects().clear();
			p.teleport(loc);
			
			Integer s = Integer.valueOf(Score.score.getInt("score." + p.getName()) - 5);
			Score.score.set("score." + p.getName(), s);
			this.plugin.Scoresave();
		}
	}
	
	@EventHandler
	public void jump(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if ((this.plugin.inJump.contains(p.getName())) && (e.getPlayer().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.EMERALD_BLOCK)) {
		      p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 2));
		}
	}
	
	@EventHandler
	public void speed(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if ((this.plugin.inJump.contains(p.getName())) && (e.getPlayer().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.SEA_LANTERN)) {
		      p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2));
		}
	}
	
	@EventHandler
	public void Tod(PlayerMoveEvent e){
	    Player p = e.getPlayer();

	    if ((this.plugin.inJump.contains(p.getName())) && (e.getPlayer().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.REDSTONE_BLOCK)){

	      String world = cfg.getString("Spawn.World");
	      double x = cfg.getDouble("Spawn.PosX");
	      double y = cfg.getDouble("Spawn.PosY");
	      double z = cfg.getDouble("Spawn.PosZ");
	      double yaw = cfg.getDouble("Spawn.PosYam");
	      double pitch = cfg.getDouble("Spawn.PosPitch");

	      Location l = new Location(Bukkit.getWorld(world), x, y, z);
	      l.setPitch((float)pitch);
	      l.setYaw((float)yaw);
	      p.teleport(l);

	      Integer s = Integer.valueOf(Score.score.getInt("score." + p.getName()) - 10);
	      Score.score.set("score." + p.getName(), s);
	      this.plugin.Scoresave();

	      p.sendMessage(Prefix + "Versuch es bitte nochmal");
	    }
	}
	
	@EventHandler
	public void Blindnes(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if ((this.plugin.inJump.contains(p.getName())) && (e.getPlayer().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.COAL_BLOCK)) {
		      p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 10));
		}
	}
	
	@EventHandler
	public void checkpoint(PlayerMoveEvent e){
		Player p = (Player)e.getPlayer();
		if((this.plugin.inJump.contains(p.getName()) && (e.getPlayer().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.GOLD_BLOCK))){
			this.plugin.checkpoint.put(p.getName(), p.getLocation());
		}
	}
	
	@EventHandler
	public void Anfang(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if((this.plugin.inJump.contains(p.getName())) && (e.getPlayer().getLocation().subtract(1.0D, 1.0D, 1.0D).getBlock().getType() == Material.ENDER_STONE)){

	 	    String world = cfg.getString("Spawn.World");
	 	    double x = cfg.getDouble("Spawn.PosX");
	 	    double y = cfg.getDouble("Spawn.PosY");
	 	    double z = cfg.getDouble("Spawn.PosZ");
	 	    double yaw = cfg.getDouble("Spawn.PosYam");
	 	    double pitch = cfg.getDouble("Spawn.PosPitch");

	 	    Location l = new Location(Bukkit.getWorld(world), x, y, z);
	 	    l.setPitch((float)pitch);
	 	    l.setYaw((float)yaw);
	 	    p.teleport(l);

	 	    Location loc = (Location)this.plugin.checkpoint.get(p.getName());
	 	    p.teleport(loc);
    	}
    }
      
	@EventHandler
	public void Ende(PlayerMoveEvent e){
		Player p = e.getPlayer();

	    if ((this.plugin.inJump.contains(p.getName())) && (e.getPlayer().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.DIAMOND_BLOCK)) {
	      Location loc = (Location)this.plugin.oldLoc.get(p.getName());
	      ItemStack[] i = (ItemStack[])this.plugin.oldItems.get(p.getName());
	      

	      p.getInventory().clear();
	      p.getInventory().setContents(i);
	      p.teleport(loc);
	      
	      Integer s = Integer.valueOf(Score.score.getInt("score." + p.getName()) + 99);
	      Score.score.set("score." + p.getName(), s);
	      this.plugin.Scoresave();

	      Integer w = Integer.valueOf(Score.score.getInt("wins." + p.getName()) + 1);
	      Score.score.set("wins." + p.getName(), w);
	      this.plugin.Scoresave();

	      p.setGameMode((GameMode)this.plugin.gm.get(p.getName()));
	      p.sendMessage(Prefix + "§6Sie haben den Parkour gemeistert! Herzliche Glückwünsche !!");

	      this.plugin.inJump.remove(p.getName());
	    }
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if ((e.getEntity() instanceof Player)) {
			Player p = (Player)e.getEntity();
			if (this.plugin.inJump.contains(p.getName()))
				e.setCancelled(true); 
		}
	}

	@EventHandler
	public void food(FoodLevelChangeEvent e) {
		Player p = (Player)e.getEntity();
		if (this.plugin.inJump.contains(p.getName()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onCommandsDeny(PlayerCommandPreprocessEvent e) {
		if ((this.plugin.inJump.contains(e.getPlayer().getName())) && (!e.getMessage().contains("/jump"))) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(Prefix + "That Command is now allowed in the Jump arena! §c[" + e.getMessage() + "§c]");
	    }
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (this.plugin.inJump.contains(e.getPlayer().getName()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (this.plugin.inJump.contains(e.getPlayer().getName()))
			e.setCancelled(true); 
		}
}
