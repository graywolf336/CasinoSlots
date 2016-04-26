package com.craftyn.casinoslots.enums;

/**
 * Representation of the type of columns in a slot machine, containing the index and task delay.
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public enum SlotMachineColumnType {
    /** The first column in a slot machine, the one which runs the longest. */
    FIRST(0, 100L),
    /** The second column in a slot machine, the one which runs the second longest. */
    SECOND(1, 80L),
    /** The third column in a slot machine, the one which runs the shortest amount of time. */
    THIRD(2, 60L);
    
    private int number;
    private long delay;
    private SlotMachineColumnType(int number, long delay) {
        this.number = number;
        this.delay = delay;
    }
    
    /**
     * The index of the column.
     * 
     * @return the index number of the column
     */
    public int getIndex() {
        return this.number;
    }
    
    /**
     * The index the first row in this column's block is at, bottom most.
     * 
     * @return the index of the first row's block
     */
    public int getFirstRow() {
        return this.number + 6;
    }
    
    /**
     * The index the second row in this column's block is at, middle.
     * 
     * @return the index of the second row's block
     */
    public int getSecondRow() {
        return this.number + 3;
    }
    
    /**
     * The index the third row in this column's block is at, top most.
     * 
     * @return the index of the third row's block
     */
    public int getThirdRow() {
        return this.number;
    }
    
    /**
     * The delay for the rotation tasks.
     * 
     * @return the delay to be applied to the rotate tasks
     */
    public long getDelay() {
        return this.delay;
    }
}
