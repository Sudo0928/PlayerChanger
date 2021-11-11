package com.minshigee.playerchanger.logic.ability.domain.interface_;

import com.minshigee.playerchanger.logic.ability.domain.AbilityType;
import org.bukkit.entity.Player;

public interface iAbilities {
    public<T> Player updateAbility(T event);
    public AbilityType getAbilityType();
}
