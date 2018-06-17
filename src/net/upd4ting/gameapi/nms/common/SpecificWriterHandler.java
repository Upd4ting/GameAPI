package net.upd4ting.gameapi.nms.common;

import com.comphenix.protocol.events.PacketContainer;

public interface SpecificWriterHandler {
	enum SpecificWriterType { DISPLAY, ACTIONCHANGE, ACTIONREMOVE}
	
	void write(PacketContainer container, SpecificWriterType type);
}
