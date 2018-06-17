package net.upd4ting.gameapi.schematic;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Material;

import net.upd4ting.gameapi.nms.NMSHandler;
import net.upd4ting.gameapi.nms.common.PasteHandler;

public class Schematic {
	
	public interface SchematicEvent {
		BlockInfo onPaste(Location loc, BlockInfo info);
		void onPasteEnd();
		void onFileNotFound();
	}
	
	public static class BlockInfo {
		public final Material material;
		public final byte data;
		
		public BlockInfo(Material material, byte data) {
			this.material = material;
			this.data = data;
		}
	}
	
	private final File schematicFile;
	private SchematicEvent event;
	
	public Schematic(File file) {
		this.schematicFile = file;
	}
	
	public Schematic(File file, SchematicEvent event) {
		this(file);
		this.event = event;
	}

	public void paste(Location loc, Boolean log) {
    	PasteHandler handler = (PasteHandler) NMSHandler.getModule("Paste");
    	handler.paste(schematicFile, event, loc, log);
    }
}
