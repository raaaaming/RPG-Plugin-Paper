package com.raaaaming.rpgpaper;

import com.raaaaming.rpgpaper.cmds.MainCommand;
import com.raaaaming.rpgpaper.cmds.StatCommand;
import com.raaaaming.rpgpaper.files.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin implements Listener {

    public static String prefix = "&f&l [ &bRPG Plugin &f&l] &7";
    public static HashMap<Player, Integer> level = new HashMap<>();
    public static HashMap<Player, Integer> exp = new HashMap<>();
    public static HashMap<Player, Integer> statpoint = new HashMap<>();
    public static HashMap<Player, Integer> atk = new HashMap<>();
    public static HashMap<Player, Integer> def = new HashMap<>();
    public static HashMap<Player, Integer> agi = new HashMap<>();
    public static HashMap<Player, Integer> health = new HashMap<>();
    public static HashMap<Player, Integer> critical = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        CustomConfig.setup();
        CustomConfig.get().addDefault("최대 레벨", 50);
        CustomConfig.get().addDefault("레벨당 획득 스탯", 1);
        CustomConfig.get().options().copyDefaults(true);
        CustomConfig.save();
        getLogger().info(prefix + "플러그인이 활성화되었습니다.");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("rpg").setExecutor(new MainCommand());
        getCommand("stat").setExecutor(new StatCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(prefix + "플러그인이 비활성화되었습니다.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (level.get(p) == null) {level.put(p, 1);}
        if (statpoint.get(p) == null) {
            statpoint.put(p, 10);
            Bukkit.broadcastMessage(" &f&l[ &e★ &f&l] &7뉴비 등장! 환영해주세요!");
        } else {
            Bukkit.broadcastMessage(" &f&l[ &a+ &f&l] &7로그인했습니다. (&f&l" + p.getName() + "&7)");
            p.setHealth(30 + health.get(p));
            p.setHealthScale(20);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        p.setHealth(30 + health.get(p));
        p.setHealthScale(20);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Bukkit.broadcastMessage(" &f&l[ &c+ &f&l] &7로그아웃했습니다. (&f&l" + p.getName() + "&7)");
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = MainCommand.inv;
        if (e.getClickedInventory() == inv) {
            if (statpoint.get(p) > 0) {
                e.setCancelled(true);
                if (e.getRawSlot() == 4) {
                    statpoint.put(p, statpoint.get(p) - 1);
                    atk.put(p, atk.get(p) + 1);
                    p.sendMessage(prefix + "공격력을 1 추가하였습니다.");
                } else if (e.getRawSlot() == 20) {
                    statpoint.put(p, statpoint.get(p) - 1);
                    def.put(p, def.get(p) + 1);
                    p.sendMessage(prefix + "방어력을 1 추가하였습니다.");
                } else if (e.getRawSlot() == 24) {
                    statpoint.put(p, statpoint.get(p) - 1);
                    agi.put(p, agi.get(p) + 1);
                    p.sendMessage(prefix + "민첩을 1 추가하였습니다.");
                } else if (e.getRawSlot() == 39) {
                    statpoint.put(p, statpoint.get(p) - 1);
                    health.put(p, health.get(p) + 1);
                    p.sendMessage(prefix + "체력을 1 추가하였습니다.");
                } else if (e.getRawSlot() == 41) {
                    statpoint.put(p, statpoint.get(p) - 1);
                    critical.put(p, critical.get(p) + 1);
                    p.sendMessage(prefix + "크리티컬을 1 추가하였습니다.");
                }
            } else {
                p.sendMessage(prefix + "스탯포인트가 부족합니다.");
            }
            p.closeInventory();
            p.performCommand("/rpg stat");
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();
        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("경험치북")) {
                ItemStack item = p.getInventory().getItemInMainHand();
                String b = item.getItemMeta().getDisplayName().replace("경험치북 (", "");
                Integer c = Integer.valueOf(b.replace(")", ""));
                p.getInventory().removeItem(item);
                exp.put(p, exp.get(p) + c);
            }
        }
    }

    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent e) {
        Player p = e.getPlayer();
        if (e.getOldLevel() == 50) {
            e.getPlayer().setLevel(50);
            p.sendTitle("LEVEL UP!", "이미 최대 레벨에 도달하셨습니다.");
            statpoint.put(p, statpoint.get(p) + CustomConfig.get().getInt("레벨당 획득 스탯"));
        } else if (e.getOldLevel() < 50) {
            p.sendTitle("LEVEL UP!", e.getOldLevel() + "Lv -> " + e.getNewLevel() + "Lv");
            statpoint.put(p, statpoint.get(p) + CustomConfig.get().getInt("레벨당 획득 스탯"));
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        Player attacker = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();
        //데미지 계산식: 기본 데미지 2 + 타격자의 공격력 스탯 * 0.5 - 피격자의 방어력 스탯 * 0.5
        //크리티컬 확률식: 타격 시 (15 + 타격자의 크리티컬 스탯 * 0.25)%
        if ((int) Math.random() * 100 > 15 + critical.get(attacker) * 0.25) {
            e.setDamage(2 + atk.get(attacker) * 0.5 - def.get(victim) * 0.5);
        } else {
            e.setDamage(2 + atk.get(attacker) * 0.5 - def.get(victim) * 0.5);
            e.setDamage(2 + atk.get(attacker) * 0.5 - def.get(victim) * 0.5);
            attacker.sendMessage("크리티컬!");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        //move speed 변경해주는 구문 작성
        //기본값을 1로 지정하고 1스탯당 0.25 추가
        p.setWalkSpeed((float) (1 + agi.get(p)*0.25));
    }
}
