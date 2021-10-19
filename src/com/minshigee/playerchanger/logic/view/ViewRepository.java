package com.minshigee.playerchanger.logic.view;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.annotation.IsRepository;
import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.logic.view.domain.ShowType;
import com.minshigee.playerchanger.logic.view.domain.ViewTask;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


@IsRepository
public class ViewRepository extends Repository<ViewData> {
    public ViewRepository(ViewData localDB) {
        super(localDB);
        createViewScheduler();
    }
    private int curTime = 0; // 1 == 0.1sec

    private void createViewScheduler(){
        new BukkitRunnable(){
            @Override
            public void run() {
                curTime += 1;
                excuteViewTask(curTime);
                if(curTime % 20 == 0){
                    updateUserScoreboards();
                }
            }
        }.runTaskTimerAsynchronously(PlayerChanger.singleton, 0, 2);
    }
    private void excuteViewTask(int time){if(localDB.getViewTask() == null || localDB.getViewTask().getStartTime() > time) return;ViewTask task = localDB.getAndPopTask();task.executeTask();}
    public void addViewTask(int laterTime, ShowType type, Player[] players, String msg) {localDB.addViewTask(new ViewTask(curTime + laterTime, type, players, msg));}
    public void addScoreboardData(int num, Pair<String,String> data){localDB.registerStringScoreboard(num,data);}

    private void updateUserScoreboards(){GameData.getParticipants().forEach(this::updateUserScoreboard);}
    private void updateUserScoreboard(Participant participant){

    }
}
