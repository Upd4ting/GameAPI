package net.upd4ting.gameapi.board;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.nms.NMSHandler;
import net.upd4ting.gameapi.nms.common.SpecificWriterHandler;
import net.upd4ting.gameapi.nms.common.SpecificWriterHandler.SpecificWriterType;

/**
 * CLass to handle scoreboard score by score
 * @author Upd4ting
 *
 */
public class Sidebar {

    private final Player player;
    private final HashMap<String, Integer> linesA;
    private final HashMap<String, Integer> linesB;
    
    private Boolean a = true;
    
    private final SpecificWriterHandler handler;
    
    private String getBuffer() {
    	return a?"A":"B";
    }
    private HashMap<String, Integer> linesBuffer() {
    	return a?linesA:linesB;
    }
    private HashMap<String, Integer> linesDisplayed() {
    	return (!a)?linesA:linesB;
    }
    private void swapBuffer() {
    	a = !a;
    }

    public Sidebar(Player p) {
    	
        this.player = p;
        this.linesA = new HashMap<>();
        this.linesB = new HashMap<>();
        this.handler = (SpecificWriterHandler) NMSHandler.getModule("SpecificWriter");
        
        PacketContainer createA = new PacketContainer(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
    	createA.getStrings()
        		.write(0, "A") // Unique name
                .write(1, ""); // Display name
    	createA.getIntegers()
                .write(0, 0); // Mode : create
    	handler.write(createA, SpecificWriterType.DISPLAY);
    	
    	PacketContainer createB = new PacketContainer(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
    	createB.getStrings()
        		.write(0, "B") // Unique name
                .write(1, ""); // Display name
    	createB.getIntegers()
                .write(0, 0); // Mode : create
    	handler.write(createB, SpecificWriterType.DISPLAY);

    	
        try {
            GameAPI.getProtocolManager().sendServerPacket(p, createA);
            GameAPI.getProtocolManager().sendServerPacket(p, createB);
        } catch( InvocationTargetException e ) {
            e.printStackTrace();
        }
    }
    
    public void send() {
    	PacketContainer display = new PacketContainer(PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE);
    	display.getIntegers()
                .write(0, 1); // Position : sidebar
    	display.getStrings()
                .write(0, getBuffer()); // Unique name
    	
        swapBuffer();
        
        try {
        	GameAPI.getProtocolManager().sendServerPacket(player, display);
        } catch( InvocationTargetException e ) {
            e.printStackTrace();
        }
        
        for (String text : linesDisplayed().keySet()) {
        	if (linesBuffer().containsKey(text)) {
        		if (Objects.equals(linesBuffer().get(text), linesDisplayed().get(text))) {
        			continue;
        		}
        	}
        	
        	setLine(text, linesDisplayed().get(text));
        }
        for (String text : new ArrayList<>(linesBuffer().keySet())) {
        	if (!linesDisplayed().containsKey(text)) {
        		removeLine(text);
        	}
        }
    }

    public void remove() {
        PacketContainer removeA = new PacketContainer(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        removeA.getStrings()
                .write(0, "A")
                .write(1, "");
        removeA.getIntegers()
                .write(0, 1);
        PacketContainer removeB = new PacketContainer(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        removeB.getStrings()
                .write(0, "B")
                .write(1, "");
        removeB.getIntegers()
                .write(0, 1);
        
        try {
        	GameAPI.getProtocolManager().sendServerPacket(this.player, removeA);
        	GameAPI.getProtocolManager().sendServerPacket(this.player, removeB);
        } catch( InvocationTargetException e ) {
            e.printStackTrace();
        }
    }
    
    public void clear() {
    	for (String text : new ArrayList<>(linesBuffer().keySet())) {
    		removeLine(text);
    	}
    }
    
    public void setLine(String text, Integer line) {
    	if (text == null) return;
    	
    	if (text.length() > 40)
            text = text.substring(0, 40);
    	
        if (linesBuffer().containsKey(text))
            removeLine(text);
        
        PacketContainer set = new PacketContainer(PacketType.Play.Server.SCOREBOARD_SCORE);
        set.getStrings()
                .write(0, text)
                .write(1, getBuffer());
        set.getIntegers()
                .write(0, line);
        handler.write(set, SpecificWriterType.ACTIONCHANGE);
        
        try {
        	GameAPI.getProtocolManager().sendServerPacket(this.player, set);
            linesBuffer().put(text, line);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void removeLine(String text) {
        
    	if (text.length() > 40)
            text = text.substring(0, 40);
    	
    	if (!linesBuffer().containsKey(text))
    		return;
    	
    	Integer line = linesBuffer().get(text);
    	
        PacketContainer reset = new PacketContainer(PacketType.Play.Server.SCOREBOARD_SCORE);
        reset.getStrings()
                .write(0, text)
                .write(1, getBuffer());
        reset.getIntegers()
                .write(0, line);
        handler.write(reset, SpecificWriterType.ACTIONREMOVE);
        
        try {
        	GameAPI.getProtocolManager().sendServerPacket(this.player, reset);
            linesBuffer().remove(text);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setName(String displayName) {
    	
    	if(displayName.length() > 32)
    		displayName = displayName.substring(0, 32);
    	
        PacketContainer nameA = new PacketContainer(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        nameA.getStrings()
                .write(0, "A")
                .write(1, displayName);
        nameA.getIntegers()
                .write(0, 2);
        handler.write(nameA, SpecificWriterType.DISPLAY);
        
        PacketContainer nameB = new PacketContainer(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        nameB.getStrings()
                .write(0, "B")
                .write(1, displayName);
        nameB.getIntegers()
                .write(0, 2);
        handler.write(nameB, SpecificWriterType.DISPLAY);

        
        try {
        	GameAPI.getProtocolManager().sendServerPacket(this.player, nameA);
        	GameAPI.getProtocolManager().sendServerPacket(this.player, nameB);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }        
    }
    
    public String getName() {
    	return player.getName();
    }
}