package net.upd4ting.gameapi.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class that contains some util for
 * inventory
 */
public class UtilInventory {

    public static String toBase64(PlayerInventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Save every armors
            for (int i = 0; i < inventory.getArmorContents().length; i++) {
                dataOutput.writeObject(inventory.getArmorContents()[i]);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static void fromBase64(PlayerInventory inventory, String data)  {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            // Read armors
            ItemStack[] armors = new ItemStack[inventory.getArmorContents().length];
            for (int i = 0; i < inventory.getArmorContents().length; i++) {
                armors[i] = (ItemStack) dataInput.readObject();
            }
            inventory.setArmorContents(armors);

            dataInput.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }
}
