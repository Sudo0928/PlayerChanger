package com.minshigee.playerchanger.logic.ability.domain.ability;

import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import com.minshigee.playerchanger.logic.ability.domain.AbilityContent;
import com.minshigee.playerchanger.logic.ability.domain.AbilityType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class GoldenPig extends Abilities {

    public GoldenPig()
    {
        super(AbilityType.GoldenPig);
        abilityContents.put(abilityType, new AbilityContent(abilityType, 0.1
                , ChatColor.GREEN + "\n──────[능력 정보]──────\n"
                + ChatColor.AQUA + "황금돼지 " + ChatColor.WHITE + "[" + ChatColor.GRAY + "능력 활성화" + ChatColor.WHITE + "]" + ChatColor.AQUA + "C 등급" + ChatColor.WHITE + "인간\n"
                + "상대방에게 타격을 받을 경우 그 자리에 금, 사과, 돼지고기를 뿌린다.\n"
                + ChatColor.GREEN + "─────────────────\n")
        );
    }

    @Override
    public <T> Player updateAbility(T event) {
        if(!(event instanceof EntityDamageByEntityEvent))
            return null;

        EntityDamageByEntityEvent evDamage = (EntityDamageByEntityEvent) event;
        Player player = ((Player)(evDamage).getEntity()).getPlayer();

        if(!(evDamage.getEntity() instanceof Player))
            return null;

        getThisAbilityPlayer(abilityType).stream().filter(p -> p.equals(player)).filter(p -> checkCoolDown(p)).forEach(p -> {
            p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.APPLE, 1));
            p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.GOLD_NUGGET, 1));
            p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.PORKCHOP, 1));
            setPlayerCoolDown(p, Abilities.getAbilityContent(abilityType).getCoolTime());
        });
        return null;
    }
}
