package org.quartzpowered.protocol.packet.play.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.quartzpowered.network.protocol.packet.Packet;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttachEntityPacket extends Packet {
    private int entityID;
    private int vehicleID;
    private boolean leash;
}