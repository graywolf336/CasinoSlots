package com.craftyn.casinoslots.util;

import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.block.NoteBlock;

public class Util {
    /**
     * Plays the CasinoSlot game sound at the given location.
     * 
     * @param location the {@link Location} to play the sound at.
     */
    public static void playGameSound(Location location) {
        playNoteBlockSound(location, Instrument.PIANO, new Note((byte) 0, Tone.C, false));
    }
    
    /**
     * Plays a note at the given location, if the block is a {@link NoteBlock}.
     * 
     * @param location the {@link Location} to play the sound at.
     * @param instrument the {@link Instrument} to use.
     * @param note the {@link Note} to play.
     */
    public static void playNoteBlockSound(Location location, Instrument instrument, Note note) {
        if(location.getBlock().getState() instanceof NoteBlock) {
            NoteBlock n = (NoteBlock) location.getBlock().getState();
            n.play(instrument, note);
        }
    }
}
