package com.minshigee.playerchanger;

import com.minshigee.playerchanger.domain.module.Controller;
import com.minshigee.playerchanger.logic.view.ViewController;
import com.minshigee.playerchanger.logic.view.ViewData;
import com.minshigee.playerchanger.logic.view.ViewRepository;
import com.minshigee.playerchanger.util.MessageUtil;
import com.minshigee.playerchanger.logic.CommandsExecutor;
import com.minshigee.playerchanger.logic.EventsListener;
import com.minshigee.playerchanger.logic.ability.AbilitiesController;
import com.minshigee.playerchanger.logic.ability.AbilitiesData;
import com.minshigee.playerchanger.logic.ability.AbilitiesRepository;
import com.minshigee.playerchanger.logic.change.ChangeController;
import com.minshigee.playerchanger.logic.change.ChangeData;
import com.minshigee.playerchanger.logic.change.ChangeRepository;
import com.minshigee.playerchanger.logic.game.GameController;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.logic.game.GameRepositoy;
import com.minshigee.playerchanger.logic.mission.MissionController;
import com.minshigee.playerchanger.logic.mission.MissionData;
import com.minshigee.playerchanger.logic.mission.MissionRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerChanger extends JavaPlugin {

    public static FileConfiguration config;
    private static ArrayList<Class> ContainerSequenceList = new ArrayList<>();
    private static HashMap<Class, Object> Container = new HashMap<>();
    public static PlayerChanger singleton;

    public static Object getInstanceOfClass(Class cls){
        return Container.get(cls);
    }
    public static ArrayList<Class> getContainerKeys(){
        return ContainerSequenceList;
    }

    private Integer viewCode = 0;

    @Override
    public void onEnable() {
        super.onEnable();
        singleton = this;
        initPlugin();
        MessageUtil.printConsoleLog(ChatColor.GREEN + "작동되었습니다.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MessageUtil.printConsoleLog(ChatColor.GREEN + "종료되었습니다.");
    }

    private void initPlugin() {
        config = this.getConfig();
        config.options().copyDefaults(true);
        saveConfig();

        registerModules();
        registerCommandExecutor();
        registerEventListener();
        logDIResult();
    }

    private void registerModules(){
        injectDependency(GameController.class, GameRepositoy.class, GameData.class); //ViewCode = 0
        injectDependency(ViewController.class, ViewRepository.class, ViewData.class); //ViewCode = 4
        injectDependency(ChangeController.class, ChangeRepository.class, ChangeData.class); //ViewCode = 3
        injectDependency(MissionController.class, MissionRepository.class, MissionData.class); //ViewCode = 1
        injectDependency(AbilitiesController.class, AbilitiesRepository.class, AbilitiesData.class); //ViewCode = 2
    }

    private <T,K,S> void injectDependency(T conClass, K repoClass, S dbClass){
        try {
            S tmpDB = (S) ((Class)dbClass).getConstructor().newInstance();
            K tmpRepo = (K) ((Class)repoClass).getConstructor(tmpDB.getClass(), Integer.class).newInstance(tmpDB, viewCode++);
            T tmpCont = (T) ((Class)conClass).getConstructor(tmpRepo.getClass()).newInstance(tmpRepo);
            if(!(tmpCont instanceof Controller))
                return;
            if(!((Controller<?>) tmpCont).getIsAvailable())
                return;
            ContainerSequenceList.add(tmpCont.getClass());
            registerInstanceToContainer(tmpDB);
            registerInstanceToContainer(tmpRepo);
            registerInstanceToContainer(tmpCont);
        }
        catch (Exception e){
            MessageUtil.printConsoleLog(ChatColor.RED + "DI에 실패했습니다. { " + ((Class)conClass).getName() + " } 관련 모듈. log: " + e.getMessage());
        }
    }

    private void registerEventListener(){
        Bukkit.getPluginManager().registerEvents(
                registerInstanceToContainer(new EventsListener()),
                this
        );
    }

    private void registerCommandExecutor(){
        try {
            this.getCommand("ph").setExecutor(registerInstanceToContainer(new CommandsExecutor()));
        }
        catch (Exception e){
            MessageUtil.printConsoleLog(ChatColor.RED + " 명령어 등록에 실패했습니다.");
        }
    }

    private <T> T registerInstanceToContainer(T instance){
        Container.put(instance.getClass(), instance);
        return instance;
    }

    private void logDIResult(){
        ContainerSequenceList.forEach(o -> MessageUtil.printConsoleLog(ChatColor.AQUA + "DI Cheker: " + o.getName()));
        //Container.values().forEach(o -> MessageUtil.printConsoleLog(ChatColor.AQUA + "DI Cheker: " + o.getClass().getName()));
    }

}
