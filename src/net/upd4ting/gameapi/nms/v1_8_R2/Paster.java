package net.upd4ting.gameapi.nms.v1_8_R2;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.NBTBase;
import net.minecraft.server.v1_8_R2.NBTTagByte;
import net.minecraft.server.v1_8_R2.NBTTagByteArray;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.NBTTagDouble;
import net.minecraft.server.v1_8_R2.NBTTagFloat;
import net.minecraft.server.v1_8_R2.NBTTagInt;
import net.minecraft.server.v1_8_R2.NBTTagIntArray;
import net.minecraft.server.v1_8_R2.NBTTagList;
import net.minecraft.server.v1_8_R2.NBTTagLong;
import net.minecraft.server.v1_8_R2.NBTTagShort;
import net.minecraft.server.v1_8_R2.NBTTagString;
import net.minecraft.server.v1_8_R2.TileEntity;
import net.upd4ting.gameapi.exception.DataException;
import net.upd4ting.gameapi.nms.common.PasteHandler;
import net.upd4ting.gameapi.schematic.Schematic.BlockInfo;
import net.upd4ting.gameapi.schematic.Schematic.SchematicEvent;
import net.upd4ting.gameapi.schematic.jnbt.ByteArrayTag;
import net.upd4ting.gameapi.schematic.jnbt.ByteTag;
import net.upd4ting.gameapi.schematic.jnbt.CompoundTag;
import net.upd4ting.gameapi.schematic.jnbt.DoubleTag;
import net.upd4ting.gameapi.schematic.jnbt.EndTag;
import net.upd4ting.gameapi.schematic.jnbt.FloatTag;
import net.upd4ting.gameapi.schematic.jnbt.IntArrayTag;
import net.upd4ting.gameapi.schematic.jnbt.IntTag;
import net.upd4ting.gameapi.schematic.jnbt.ListTag;
import net.upd4ting.gameapi.schematic.jnbt.LongTag;
import net.upd4ting.gameapi.schematic.jnbt.NBTInputStream;
import net.upd4ting.gameapi.schematic.jnbt.NamedTag;
import net.upd4ting.gameapi.schematic.jnbt.ShortTag;
import net.upd4ting.gameapi.schematic.jnbt.StringTag;
import net.upd4ting.gameapi.schematic.jnbt.Tag;
import net.upd4ting.gameapi.util.Util;

public class Paster implements PasteHandler {
	
	private Method nbtCreateTagMethod;
	
	public Paster() {
		try {
		    this.nbtCreateTagMethod = NBTBase.class.getDeclaredMethod("createTag", Byte.TYPE);
		    this.nbtCreateTagMethod.setAccessible(true);
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

	        // Need to pull out tile entities
	        List<Tag> tileEntities = getChildTag(schematic, "TileEntities", ListTag.class)
	                .getValue();
	        Map<BlockPosition, Map<String, Tag>> tileEntitiesMap =
					new HashMap<>();

	        for (Tag tag : tileEntities) {
	            if (!(tag instanceof CompoundTag)) continue;
	            CompoundTag t = (CompoundTag) tag;

	            int x = 0;
	            int y = 0;
	            int z = 0;

	            Map<String, Tag> values = new HashMap<>();

	            for (Map.Entry<String, Tag> entry : t.getValue().entrySet()) {
					switch (entry.getKey()) {
						case "x":
							if (entry.getValue() instanceof IntTag) {
								x = ((IntTag) entry.getValue()).getValue();
							}
							break;
						case "y":
							if (entry.getValue() instanceof IntTag) {
								y = ((IntTag) entry.getValue()).getValue();
							}
							break;
						case "z":
							if (entry.getValue() instanceof IntTag) {
								z = ((IntTag) entry.getValue()).getValue();
							}
							break;
					}

	                values.put(entry.getKey(), entry.getValue());
	            }

	            BlockPosition vec = new BlockPosition(x, y, z);
	            tileEntitiesMap.put(vec, values);
	        }
	        
	        Util.loadChunks(loc, width > length ? width + 100 : length + 100, true);
			
	        for (int x = 0; x < width; ++x) {
	            for (int y = 0; y < height; ++y) {
	                for (int z = 0; z < length; ++z) {
	                    int index = y * width * length + z * width + x;
	                    BlockPosition pt = new BlockPosition(x, y, z);
	                    Location l = new Location(loc.getWorld(), x + loc.getX(), y + loc.getY(), z + loc.getZ());
	                    Block block = l.getBlock();
	                    byte d = blockData[index];
						Material m = Material.getMaterial(blocks[index]);
						
						if (m == Material.AIR)
							continue;
						
						BlockInfo info = event.onPaste(l, new BlockInfo(m, d));
						
						if (info != null) {
							m = info.material;
							d = info.data;
						}
						
						block.setTypeIdAndData(m.getId(), d, false);
						
	                    if (tileEntitiesMap.containsKey(pt)) {
	                    	CraftWorld cw = ((CraftWorld)l.getWorld());
	                    	TileEntity tileEntity = cw.getHandle().getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
	                    	CompoundTag nativeTag = new CompoundTag(tileEntitiesMap.get(pt));
	                    	
	                    	if (tileEntity != null) {
	                    		NBTTagCompound tag = (NBTTagCompound)fromNative(nativeTag);
	                    		tag.set("x", new NBTTagInt(l.getBlockX()));
	                    		tag.set("y", new NBTTagInt(l.getBlockY()));
	                    		tag.set("z", new NBTTagInt(l.getBlockZ()));
	                    		tileEntity.a(tag);
	                    	}
	                    }
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
	
	private NBTBase fromNative(Tag foreign)
	{
		if (foreign == null) {
			return null;
		}
		if ((foreign instanceof CompoundTag))
		{
			NBTTagCompound tag = new NBTTagCompound();
			for (Map.Entry<String, Tag> entry : ((CompoundTag)foreign).getValue().entrySet()) {
				tag.set(entry.getKey(), fromNative(entry.getValue()));
			}
			return tag;
		}
		if ((foreign instanceof ByteTag)) {
			return new NBTTagByte(((ByteTag) foreign).getValue());
		}
		if ((foreign instanceof ByteArrayTag)) {
			return new NBTTagByteArray(((ByteArrayTag)foreign).getValue());
		}
		if ((foreign instanceof DoubleTag)) {
			return new NBTTagDouble(((DoubleTag) foreign).getValue());
		}
		if ((foreign instanceof FloatTag)) {
			return new NBTTagFloat(((FloatTag) foreign).getValue());
		}
		if ((foreign instanceof IntTag)) {
			return new NBTTagInt(((IntTag) foreign).getValue());
		}
		if ((foreign instanceof IntArrayTag)) {
			return new NBTTagIntArray(((IntArrayTag)foreign).getValue());
		}
		if ((foreign instanceof ListTag))
		{
			NBTTagList tag = new NBTTagList();
			ListTag foreignList = (ListTag)foreign;
			for (Tag t : foreignList.getValue()) {
				tag.add(fromNative(t));
			}
			return tag;
		}
		if ((foreign instanceof LongTag)) {
			return new NBTTagLong(((LongTag) foreign).getValue());
		}
		if ((foreign instanceof ShortTag)) {
			return new NBTTagShort(((ShortTag) foreign).getValue());
		}
		if ((foreign instanceof StringTag)) {
			return new NBTTagString(((StringTag)foreign).getValue());
		}
		if ((foreign instanceof EndTag)) {
			try {
				return (NBTBase)this.nbtCreateTagMethod.invoke(null, new Object[] {(byte) 0});
			} catch (Exception e) {
				return null;
			}
		}
		throw new IllegalArgumentException("Don't know how to make NMS " + foreign.getClass().getCanonicalName());
	}

}
