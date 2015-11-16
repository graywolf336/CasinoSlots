package com.craftyn.casinoslots.actions;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;

public interface IAction {
    public String getName();

    public boolean isValid();

    public boolean execute(Type type, Reward reward, Player player);
}
