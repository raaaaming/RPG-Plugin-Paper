package com.raaaaming.rpgpaper.cmds;

import com.raaaaming.rpgpaper.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MainCommand implements CommandExecutor {

    public static Inventory inv = Bukkit.createInventory(null, 54, "stat");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage(Main.prefix + "==========[RPG 도움말]==========");
            p.sendMessage(Main.prefix + "/rpg level check");
            p.sendMessage(Main.prefix + "/rpg level add (레벨)");
            p.sendMessage(Main.prefix + "/rpg level set (레벨)");
            p.sendMessage(Main.prefix + "/rpg level remove (레벨)");
            p.sendMessage(Main.prefix + "/rpg exp add (경험치)");
            p.sendMessage(Main.prefix + "/rpg exp remove (경험치)");
            p.sendMessage(Main.prefix + "/rpg exp book (경험치) (개수)");
            p.sendMessage(Main.prefix + "/rpg stat");
            p.sendMessage(Main.prefix + "/rpg stat add (스탯포인트)");
            p.sendMessage(Main.prefix + "/rpg stat reset");
            p.sendMessage(Main.prefix + "===============================");
        } else {
            if (args[0].equals("level")) {
                Integer n = Integer.valueOf(args[2]);
                if (args[1].equals("check")) {
                    p.sendMessage(Main.prefix + "현재 " + p.getName() + "님의 레벨은 " + Main.level.get(p));
                } else if (args[1].equals("add")) {
                    Main.level.put(p, Main.level.get(p) + n);
                    p.sendMessage(Main.prefix + p.getName() + "님의 레벨을 " + n + "레벨 올렸습니다.");
                } else if (args[1].equals("set")) {
                    Main.level.put(p, n);
                    p.sendMessage(Main.prefix + p.getName() + "님의 레벨을 " + n + "레벨로 설정했습니다.");
                } else if (args[1].equals("remove")) {
                    Main.level.put(p, Main.level.get(p) - n);
                    p.sendMessage(Main.prefix + p.getName() + "님의 레벨을 " + n + "레벨 내렸습니다.");
                }
            } else if (args[0].equals("exp")) {
                Integer n = Integer.valueOf(args[2]);
                Integer n2 = Integer.valueOf(args[3]);
                if (args[1].equals("add")) {
                    Main.exp.put(p, Main.exp.get(p) + n);
                    p.sendMessage(Main.prefix + p.getName() + "님의 레벨을 " + n + "레벨 올렸습니다.");
                } else if (args[1].equals("remove")) {
                    Main.exp.put(p, Main.exp.get(p) - n);
                    p.sendMessage(Main.prefix + p.getName() + "님의 레벨을 " + n + "레벨 내렸습니다.");
                } else if (args[1].equals("book")) {
                    ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, n2);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(Main.prefix + "경험치북 (" + n + ")");
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);
                    p.getInventory().addItem(item);
                }
            } else if (args[0].equals("stat")) {
                Integer n = Integer.valueOf(args[2]);
                if (args[1].equals("add")) {
                    Main.statpoint.put(p, Main.statpoint.get(p) + n);
                    p.sendMessage(Main.prefix + p.getName() + "님의 스탯포인트를 " + n + "개 추가하였습니다.");
                } else if (args[1].equals("reset")) {
                    Main.statpoint.put(p, Main.statpoint.get(p) + Main.atk.get(p) + Main.def.get(p) + Main.agi.get(p) + Main.health.get(p) + Main.critical.get(p));
                    Main.atk.put(p, 0);
                    Main.def.put(p, 0);
                    Main.agi.put(p, 0);
                    Main.health.put(p, 0);
                    Main.critical.put(p, 0);
                } else {
                    if (args.length == 1) {
                        ItemStack item = new ItemStack(Material.DIAMOND_AXE, 1);
                        ItemMeta meta = item.getItemMeta();
                        meta.setCustomModelData(1000);
                        item.setItemMeta(meta);
                        ItemStack item1 = new ItemStack(Material.DIAMOND_AXE, 1);
                        ItemMeta meta1 = item1.getItemMeta();
                        meta1.setDisplayName("공격력 Up!");
                        meta1.setLore(Arrays.asList("공격력: " + Main.atk.get(p)));
                        meta1.setCustomModelData(1001);
                        item1.setItemMeta(meta);
                        ItemStack item2 = new ItemStack(Material.DIAMOND_AXE, 1);
                        ItemMeta meta2 = item2.getItemMeta();
                        meta1.setDisplayName("방어력 Up!");
                        meta1.setLore(Arrays.asList("방어력: " + Main.def.get(p)));
                        meta2.setCustomModelData(1001);
                        item2.setItemMeta(meta);
                        ItemStack item3 = new ItemStack(Material.DIAMOND_AXE, 1);
                        ItemMeta meta3 = item3.getItemMeta();
                        meta1.setDisplayName("민첩 Up!");
                        meta1.setLore(Arrays.asList("민첩: " + Main.agi.get(p)));
                        meta3.setCustomModelData(1001);
                        item3.setItemMeta(meta3);
                        ItemStack item4 = new ItemStack(Material.DIAMOND_AXE, 1);
                        ItemMeta meta4 = item4.getItemMeta();
                        meta1.setDisplayName("체력 Up!");
                        meta1.setLore(Arrays.asList("체력: " + Main.health.get(p)));
                        meta4.setCustomModelData(1001);
                        item.setItemMeta(meta4);
                        ItemStack item5 = new ItemStack(Material.DIAMOND_AXE, 1);
                        ItemMeta meta5 = item5.getItemMeta();
                        meta1.setDisplayName("크리티컬 Up!");
                        meta1.setLore(Arrays.asList("크리티컬: " + Main.critical.get(p)));
                        meta5.setCustomModelData(1001);
                        item5.setItemMeta(meta5);
                        inv.setItem(53, item);
                        inv.setItem(4, item1);
                        inv.setItem(20, item2);
                        inv.setItem(24, item3);
                        inv.setItem(39, item4);
                        inv.setItem(41, item5);
                    }
                }
            }
        }
        return false;
    }
}
