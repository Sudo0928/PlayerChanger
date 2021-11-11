package com.minshigee.playerchanger.logic.ability.domain;

public class AbilityContent {
    private final AbilityType abilityType;
    private final double coolTime;
    private final String abilityHelp;

    public AbilityContent(){
        this.abilityType = AbilityType.None;
        this.coolTime = 0;
        this.abilityHelp = null;
    }

    public AbilityContent(AbilityType abilityList, double coolTime, String abilityHelp){
        this.abilityType = abilityList;
        this.coolTime = coolTime;
        this.abilityHelp = abilityHelp;
    }

    public AbilityType getAbilityType() { return abilityType; }

    public double getCoolTime() { return coolTime; }

    public String getAbilityHelp() { return abilityHelp; }
}