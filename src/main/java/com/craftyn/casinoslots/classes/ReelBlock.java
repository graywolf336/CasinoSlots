package com.craftyn.casinoslots.classes;

import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

/**
 * Representation of the blocks which go into a reel in a slot machine, containing the block's data.
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class ReelBlock {
    private MaterialData data;
    
    /**
     * Creates a new {@link ReelBlock}.
     *
     * @param data the {@link MaterialData} for the reel's block
     */
    public ReelBlock(MaterialData data) {
        this.data = data;
    }

    /**
     * The {@link MaterialData} for the reel block.
     *
     * @return the {@link MaterialData} we are using
     */
    public MaterialData getBlockData() {
        return this.data;
    }

    /**
     * The string value of this, but with the typed name instead of number.
     *
     * @return string of the MaterialData like "stone:2".
     */
    @SuppressWarnings("deprecation")
    public String toString() {
        return this.data.getItemType().toString().toLowerCase() + ":" + this.data.getData();
    }

    public boolean equals(Object object) {
        if (object == null)
            return false;
        else if (!(object instanceof ReelBlock))
            return false;

        ReelBlock b = (ReelBlock) object;

        return this.data.equals(b.getBlockData());
    }

    @SuppressWarnings("deprecation")
    public static ReelBlock fromBlock(Block block) {
        return new ReelBlock(new MaterialData(block.getType(), block.getData()));
    }
}
