package com.minshigee.playerchanger.logic.ability.domain.ability;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import com.minshigee.playerchanger.logic.ability.domain.AbilityContent;
import com.minshigee.playerchanger.logic.ability.domain.AbilityType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Rush extends Abilities{

    public Rush()
    {
        super(AbilityType.Rush);
        abilityContents.put(abilityType, new AbilityContent(abilityType, 10
                , ChatColor.GREEN + "\n──────[능력 정보]──────\n"
                + ChatColor.AQUA + "급발진 " + ChatColor.WHITE + "[" + ChatColor.GRAY + "능력 활성화" + ChatColor.WHITE + "]" + ChatColor.AQUA + "B 등급" + ChatColor.WHITE + "인간\n"
                + "금괴를 우클릭시 급발진 한다.\n"
                + "스킬 시전중 플레이어와 부딪힐 경우, \n"
                + "해당 플레이어는 데미지를 받으며 밀려난다.\n"
                + ChatColor.GREEN + "─────────────────\n")
        );
    }

    @Override
    public <T> Player updateAbility(T event) {
        if(!(event instanceof PlayerInteractEvent))
            return null;

        PlayerInteractEvent playerIE = (PlayerInteractEvent) event;
        Player player = playerIE.getPlayer();

        getThisAbilityPlayer(abilityType).stream().filter(p -> p.equals(player)).filter(p -> checkCoolDown(p)).filter(p -> p.getItemInHand().getType().equals(Material.GOLD_INGOT)).forEach(p -> {
            setPlayerCoolDown(p, Abilities.getAbilityContent(abilityType).getCoolTime());
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 255));
            new BukkitRunnable() {
                private int count = 0;
                @Override
                public void run() {
                    Vector vector = p.getLocation().getDirection().normalize();
                    p.setVelocity(new Vector(vector.getX() * 0.7, p.getVelocity().getY(), vector.getZ() * 0.7));
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
                    if(count++ > 30) this.cancel();
                    p.getNearbyEntities(1, 2, 1).forEach(e -> {
                        Vector v1 = p.getLocation().getDirection();
                        Vector v2 = e.getLocation().getDirection();
                        e.setVelocity(v1.subtract(v2));
                        if(e instanceof Player) {
                            Player ep = (Player)e;
                            ep.getWorld().playSound(ep.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                            ep.damage(2);
                        }
                    });
                }
            }.runTaskTimer(PlayerChanger.singleton, 0L, 1L);

//            RayTraceResult raycast = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getLocation().getDirection(), 10);
//            if(raycast == null) {
//                Location loc = p.getLocation();
//                Vector dir = loc.getDirection();
//                dir.normalize();
//                dir.multiply(10);
//                loc.add(dir);
//                p.teleport(loc);
//            } else {
//                p.teleport(raycast.getHitPosition().toLocation(p.getWorld()));
//            }
        });

        return null;
    }
}