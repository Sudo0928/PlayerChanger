package com.minshigee.playerchanger.logic.ability.domain;

public class  Ability{
    private AbilityType abilityType;
    private Long coolDown;

    public Ability(){
        this.abilityType = AbilityType.None;
        this.coolDown = 0L;
    }

    public Ability(AbilityType abilityType, Long coolDown)
    {
        this.abilityType = abilityType;
        this.coolDown = coolDown;
    }

    public AbilityType getAbilityType() { return abilityType; }
    public void setAbilityType(AbilityType abilityType) { this.abilityType = abilityType; }

    public Long getCoolDown() {
        return coolDown;
    }
    public void setCoolDown(Long coolDown) {
        this.coolDown = coolDown;
    }
}
