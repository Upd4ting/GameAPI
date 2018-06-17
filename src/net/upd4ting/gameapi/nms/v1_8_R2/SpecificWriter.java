package net.upd4ting.gameapi.nms.v1_8_R2;

import com.comphenix.protocol.events.PacketContainer;

import net.minecraft.server.v1_8_R2.IScoreboardCriteria.EnumScoreboardHealthDisplay;
import net.minecraft.server.v1_8_R2.PacketPlayOutScoreboardScore.EnumScoreboardAction;
import net.upd4ting.gameapi.nms.common.SpecificWriterHandler;

public class SpecificWriter implements SpecificWriterHandler {

	@Override
	public void write(PacketContainer container, SpecificWriterType type) {
        switch (type) {
            case DISPLAY:
                container.getSpecificModifier(EnumScoreboardHealthDisplay.class)
                        .write(0, EnumScoreboardHealthDisplay.INTEGER);
                break;
            case ACTIONCHANGE:
                container.getSpecificModifier(EnumScoreboardAction.class)
                        .write(0, EnumScoreboardAction.CHANGE);
                break;
            case ACTIONREMOVE:
                container.getSpecificModifier(EnumScoreboardAction.class)
                        .write(0, EnumScoreboardAction.REMOVE);
                break;
        }
	}
}
