package com.minshigee.playerchanger.logic.ability.domain.ability;

import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import com.minshigee.playerchanger.logic.ability.domain.AbilityContent;
import com.minshigee.playerchanger.logic.ability.domain.AbilityType;
import com.minshigee.playerchanger.logic.game.GameData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class Missionary extends Abilities {
    public Missionary()
    {
        super(AbilityType.Missionary);
        abilityContents.put(abilityType, new AbilityContent(abilityType, 0.1
                , ChatColor.GREEN + "\n──────[능력 정보]──────\n"
                + ChatColor.AQUA + "전도사 " + ChatColor.WHITE + "[" + ChatColor.GRAY + "능력 활성화" + ChatColor.WHITE + "]" + ChatColor.AQUA + "A 등급" + ChatColor.WHITE + "인간\n"
                + "상대방에게 타격을 받을 경우 10%확률로 모든 생존자에게 같은 데미지를 공유한다.\n"
                + ChatColor.GREEN + "─────────────────\n")
        );
    }

    @Override
    public<T> Player updateAbility(T event)
    {
        if(!(event instanceof EntityDamageByEntityEvent))
            return null;

        EntityDamageByEntityEvent evDamage = (EntityDamageByEntityEvent) event;
        Player player = ((Player)(evDamage).getEntity()).getPlayer();

        if(!((evDamage).getEntity() instanceof Player))
            return null;

        getThisAbilityPlayer(abilityType).stream().filter(p -> p.getPlayer().equals(player)).filter(p -> checkCoolDown(p)).forEach(p -> {
            Random random = new Random();
            int tmp = random.nextInt(100);
            GameData.getParticipantsAlive().forEach(lParticipant -> {
                if(!lParticipant.getPlayer().equals(player) && tmp <= 10) {
                    lParticipant.getPlayer().damage(evDamage.getDamage());
                    setPlayerCoolDown(p, Abilities.getAbilityContent(abilityType).getCoolTime());
                }
            });
        });
        return null;
    }


}
