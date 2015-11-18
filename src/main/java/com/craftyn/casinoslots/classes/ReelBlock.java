package com.craftyn.casinoslots.classes;

import org.bukkit.material.MaterialData;

/**
 * Representation of the blocks which go into a reel in a slot machine, containing the block's data and the amount.
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class ReelBlock {
    private MaterialData data;
    private int count;
    
    /**
     * Creates a new {@link ReelBlock} with a default count of 1.
     * 
     * @param data the {@link MaterialData} for the reel's block
     */
    public ReelBlock(MaterialData data) {
        this.data = data;
        this.count = 1;
    }

    /**
     * Creates a new {@link ReelBlock} with a default count of 1.
     * 
     * @param data the {@link MaterialData} for the reel's block
     * @param count the amount of blocks
     */
    public ReelBlock(MaterialData data, int count) {
        this.data = data;
        this.count = count;
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
     * The amount of blocks.
     * 
     * @return the number of blocks
     */
    public int getCount() {
        return this.count;
    }
    
    /**
     * The string value of this, but with the typed name instead of number.
     * 
     * @return string of the MaterialData like "stone:2".
     */
    @SuppressWarnings("deprecation")
    public String toStringType() {
        return this.data.getItemType().toString() + ":" + this.data.getData();
    }
    
    /**
     * The string value of this, but with the numbers.
     * 
     * @return string of the MaterialData like "1:2".
     */
    @SuppressWarnings("deprecation")
    public String toString() {
        return this.data.getItemTypeId() + ":" + this.data.getData();
    }
}
