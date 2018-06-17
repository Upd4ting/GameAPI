package net.upd4ting.gameapi.nms.v1_7_R4;

import com.comphenix.protocol.events.PacketContainer;

import net.upd4ting.gameapi.nms.common.SpecificWriterHandler;

public class SpecificWriter implements SpecificWriterHandler {

	@Override
	public void write(PacketContainer container, SpecificWriterType type) {
        switch (type) {
            case DISPLAY:
                container.getStrings().writeSafely(2, "integer");
                break;
            case ACTIONCHANGE:
                container.getIntegers().write(1, 0);
                break;
            case ACTIONREMOVE:
                container.getIntegers().write(1, 1);
                break;
        }
	}
}
