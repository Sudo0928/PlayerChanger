package com.minshigee.playerchanger.logic.ability.domain;

import com.minshigee.playerchanger.logic.ability.domain.interface_.iAbilities;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Abilities implements iAbilities {
    public Abilities(AbilityType abilityType) { this.abilityType = abilityType; }

    protected static HashMap<Player, Ability> playersAbility = new HashMap<>();

    protected static HashMap<AbilityType, AbilityContent> abilityContents = new HashMap<>();

    protected AbilityType abilityType;

    @Override
    public <T> Player updateAbility(T event) {
        return null;
    }

    public Boolean checkCoolDown(Player player)
    {
        Long time = getPlayerAbility(player).getCoolDown();
        if(getPlayerAbility(player) != null && time > System.currentTimeMillis()){
            long longRemaining = time - System.currentTimeMillis();
            int intRemaining = (int) (longRemaining / 1000);
            if(intRemaining <= 5) player.sendMessage("쿨타임이 " + intRemaining + " 초 남았습니다.");
            return false;
        }
        return true;
    }

    public AbilityType getAbilityType(){
        return abilityType;
    }

    public static HashMap<Player, Ability> getPlayersAbility(){
        return playersAbility;
    }
    public static Ability getPlayerAbility(Player player){
        return playersAbility.get(player);
    }
    public static void setPlayerAbility(Player player, Ability ability){
        playersAbility.replace(player, ability);
    }

    public static HashMap<AbilityType, AbilityContent> getAbilityContents(){ return abilityContents; }
    public static AbilityContent getAbilityContent(AbilityType abilitytype) { return abilityContents.get(abilitytype); }

    public static void setPlayerCoolDown(Player player, double second) { playersAbility.get(player).setCoolDown(System.currentTimeMillis() + (long)(second * 1000)); }

    public ArrayList<Player> getThisAbilityPlayer(AbilityType abilityType){
        ArrayList<Player> tmpParts = new ArrayList<>();
        playersAbility.forEach((player, abType) -> { if(abType.getAbilityType().equals(abilityType)) tmpParts.add(player); });
        return tmpParts;
    }
}
