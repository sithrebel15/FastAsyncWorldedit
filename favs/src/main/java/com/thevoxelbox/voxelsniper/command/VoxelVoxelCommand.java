/**
 This file is part of VoxelSniper, licensed under the MIT License (MIT).

 Copyright (c) The VoxelBox <http://thevoxelbox.com>
 Copyright (c) contributors

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

package com.thevoxelbox.voxelsniper.command;

import com.boydti.fawe.bukkit.favs.PatternUtil;
import com.thevoxelbox.voxelsniper.RangeBlockHelper;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class VoxelVoxelCommand extends VoxelCommand {
    public VoxelVoxelCommand(VoxelSniper plugin) {
        super("VoxelVoxel", plugin);
        this.setIdentifier("v");
        this.setPermission("voxelsniper.sniper");
    }

    public boolean onCommand(Player player, String[] args) {
        Sniper sniper = this.plugin.getSniperManager().getSniperForPlayer(player);
        SnipeData snipeData = sniper.getSnipeData(sniper.getCurrentToolId());
        if(args.length == 0) {
            Block material1 = (new RangeBlockHelper(player, player.getWorld())).getTargetBlock();
            if(material1 != null) {
                if(!player.hasPermission("voxelsniper.ignorelimitations") && this.plugin.getVoxelSniperConfiguration().getLiteSniperRestrictedItems().contains(Integer.valueOf(material1.getTypeId()))) {
                    player.sendMessage("You are not allowed to use " + material1.getType().name() + ".");
                    return true;
                }

                snipeData.setVoxelId(material1.getTypeId());
                snipeData.getVoxelMessage().voxel();
                snipeData.setPattern(null, null);
            }

            return true;
        } else {
            Material material = Material.matchMaterial(args[0]);
            if(material != null && material.isBlock()) {
                if(!player.hasPermission("voxelsniper.ignorelimitations") && this.plugin.getVoxelSniperConfiguration().getLiteSniperRestrictedItems().contains(Integer.valueOf(material.getId()))) {
                    player.sendMessage("You are not allowed to use " + material.name() + ".");
                    return true;
                } else {
                    snipeData.setVoxelId(material.getId());
                    snipeData.getVoxelMessage().voxel();
                    snipeData.setPattern(null, null);
                    return true;
                }
            } else {
                PatternUtil.parsePattern(player, snipeData, args[0]);
                return true;
            }
        }
    }

    public static Class<?> inject() {
        return VoxelVoxelCommand.class;
    }
}
