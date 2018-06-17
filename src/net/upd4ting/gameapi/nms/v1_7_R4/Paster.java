package net.upd4ting.gameapi.nms.v1_7_R4;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.minecraft.server.v1_7_R4.NBTBase;
import net.upd4ting.gameapi.exception.DataException;
import net.upd4ting.gameapi.nms.common.PasteHandler;
import net.upd4ting.gameapi.schematic.Schematic.BlockInfo;
import net.upd4ting.gameapi.schematic.Schematic.SchematicEvent;
import net.upd4ting.gameapi.schematic.jnbt.ByteArrayTag;
import net.upd4ting.gameapi.schematic.jnbt.CompoundTag;
import net.upd4ting.gameapi.schematic.jnbt.NBTInputStream;
import net.upd4ting.gameapi.schematic.jnbt.NamedTag;
import net.upd4ting.gameapi.schematic.jnbt.ShortTag;
import net.upd4ting.gameapi.schematic.jnbt.Tag;
import net.upd4ting.gameapi.util.Util;

public class Paster implements PasteHandler {

	public Paster() {
		try {
			Method nbtCreateTagMethod = NBTBase.class.getDeclaredMethod("createTag", Byte.TYPE);
		    nbtCreateTagMethod.setAccessible(true);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void paste(File schematicFile, SchematicEvent event, Location loc, Boolean log) {
        try {
			long start = System.currentTimeMillis();
			if (log)
				Bukkit.getLogger().info("Pasting " + schematicFile.getAbsolutePath() + "...");

			if (!schematicFile.exists()) {
				event.onFileNotFound();
				return;
			}

			NBTInputStream nbtStream = new NBTInputStream(
	                new GZIPInputStream(new FileInputStream(schematicFile)));


	        NamedTag rootTag = nbtStream.readNamedTag();
	        nbtStream.close();
	        if (!rootTag.getName().equals("Schematic")) {
	            throw new DataException("Tag \"Schematic\" does not exist or is not first");
	        }

	        CompoundTag schematicTag = (CompoundTag) rootTag.getTag();

	        // Check
	        Map<String, Tag> schematic = schematicTag.getValue();
	        if (!schematic.containsKey("Blocks")) {
	            throw new DataException("Schematic file is missing a \"Blocks\" tag");
	        }

	        // Get information
	        short width = getChildTag(schematic, "Width", ShortTag.class).getValue();
	        short length = getChildTag(schematic, "Length", ShortTag.class).getValue();
	        short height = getChildTag(schematic, "Height", ShortTag.class).getValue();


			// Get blocks
			final byte[] blockId = getChildTag(schematic, "Blocks", ByteArrayTag.class).getValue();
			final byte[] blockData = getChildTag(schematic, "Data", ByteArrayTag.class).getValue();
			
			byte[] addId = new byte[0];
	        short[] blocks = new short[blockId.length]; // Have to later combine IDs

	        // We support 4096 block IDs using the same method as vanilla Minecraft, where
	        // the highest 4 bits are stored in a separate byte array.
	        if (schematic.containsKey("AddBlocks")) {
	            addId = getChildTag(schematic, "AddBlocks", ByteArrayTag.class).getValue();
	        }

	        // Combine the AddBlocks data with the first 8-bit block ID
	        for (int index = 0; index < blockId.length; index++) {
	            if ((index >> 1) >= addId.length) { // No corresponding AddBlocks index
	                blocks[index] = (short) (blockId[index] & 0xFF);
	            } else {
	                if ((index & 1) == 0) {
	                    blocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blockId[index] & 0xFF));
	                } else {
	                    blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blockId[index] & 0xFF));
	                }
	            }
	        }
	        
	        Util.loadChunks(loc, width > length ? width + 100 : length + 100, true);

	        for (int x = 0; x < width; ++x) {
	            for (int y = 0; y < height; ++y) {
	                for (int z = 0; z < length; ++z) {
	                    int index = y * width * length + z * width + x;
	                    Location l = new Location(loc.getWorld(), x + loc.getX(), y + loc.getY(), z + loc.getZ());
	                    Block block = l.getBlock();
	                    byte d = blockData[index];
						Material m = Material.getMaterial(blocks[index]);
						
						if (m == null || m == Material.AIR)
							continue;
						
						BlockInfo info = event.onPaste(l, new BlockInfo(m, d));
						
						if (info != null) {
							m = info.material;
							d = info.data;
						}
						
						block.setTypeIdAndData(m.getId(), d, false);
	                }
	            }
	        }
	        
			if (log)
				Bukkit.getLogger().info("Pasted successfully in " + (System.currentTimeMillis() - start) + " milliseconds.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		event.onPasteEnd();
	}
	
	/**
	 * Get child tag of a NBT structure.
	 *
	 * @param items The parent tag map
	 * @param key The name of the tag to get
	 * @param expected The expected type of the tag
	 * @return child tag casted to the expected type
	 * @throws IllegalArgumentException if the tag does not exist or the tag is not of the
	 * expected type
	 */
	private <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws IllegalArgumentException {
		if (!items.containsKey(key)) {
			throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
		}
		Tag tag = items.get(key);
		if (!expected.isInstance(tag)) {
			throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
		}
		return expected.cast(tag);
	}
}
