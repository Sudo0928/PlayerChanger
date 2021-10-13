package com.minshigee.playerchanger.controllers;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.PlayInfo;
import com.minshigee.playerchanger.domain.PCH_Status;
import com.minshigee.playerchanger.repositories.GameRepository;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class GameController {

    public static int curTime = 0;
    public static int invPer = 30;
    private static final GameRepository repository = new GameRepository();

    public static void startGame(Player player){
        if(PlayInfo.gameStatus == PCH_Status.DISABLED){
            changeStatusToSetting(player.getServer());
            player.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 게임이 대기 상태로 변경되었습니다. 유저들의 Ready를 유도해주세요!");
        }
        else if(PlayInfo.gameStatus == PCH_Status.SETTING){
            changeStatusToStarting(player.getServer());
            player.sendMessage(ChatColor.RED + "[PlayerChanger]: 게임이 시작되었습니다.");
        }
        else{
            player.sendMessage(ChatColor.RED + "[PlayerChanger]: 이미 게임이 시작되었습니다.");
        }
    }

    private static void changeStatusToSetting(Server server){
        PlayInfo.gameStatus = PCH_Status.SETTING;
        repository.makeSettingInfoSchedule(server);
    }

    private static void changeStatusToStarting(Server server){
        PlayInfo.gameStatus = PCH_Status.STARTING;
        repository.initGameStarting(PlayerChanger.playerChanger.getServer());
        repository.makeGameScheduler(server);
    }

    public static void stopGame(){
        repository.resetGameStopping(PlayerChanger.playerChanger.getServer());
    }

}