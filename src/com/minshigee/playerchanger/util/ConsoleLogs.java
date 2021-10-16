package com.minshigee.playerchanger.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ConsoleLogs {
    private static final String prefix = ChatColor.BLUE + "[ChangedChaser]:";
    public static void printConsoleLog(String msg){
        Bukkit.getConsoleSender().sendMessage(prefix + msg);
    }
    public static void printLogToPlayer(Player player, String msg){
        player.sendMessage(prefix + msg);
    }
}
