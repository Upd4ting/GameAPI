package net.upd4ting.gameapi.nms.common;

import java.io.File;

import org.bukkit.Location;

import net.upd4ting.gameapi.schematic.Schematic.SchematicEvent;

public interface PasteHandler {
	void paste(File schematicFile, SchematicEvent event, Location loc, Boolean log);
}
