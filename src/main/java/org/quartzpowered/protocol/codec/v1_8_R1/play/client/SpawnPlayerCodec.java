package org.quartzpowered.protocol.codec.v1_8_R1.play.client;

import org.quartzpowered.network.buffer.Buffer;
import org.quartzpowered.network.protocol.codec.Codec;
import org.quartzpowered.protocol.data.metadata.Metadata;
import org.quartzpowered.protocol.packet.play.client.SpawnPlayerPacket;

public class SpawnPlayerCodec implements Codec<SpawnPlayerPacket> {
    @Override
    public void encode(Buffer buffer, SpawnPlayerPacket packet) {
        buffer.writeVarInt(packet.getEntityId());
        buffer.writeUuid(packet.getPlayerId());
        buffer.writeFixedPointInt(packet.getX());
        buffer.writeFixedPointInt(packet.getY());
        buffer.writeFixedPointInt(packet.getZ());
        buffer.writeAngle((float) packet.getYaw());
        buffer.writeAngle((float) packet.getPitch());
        buffer.writeShort(packet.getHeldItem());
        packet.getMetadata().write(buffer);
    }

    @Override
    public void decode(Buffer buffer, SpawnPlayerPacket packet) {
        packet.setEntityId(buffer.readVarInt());
        packet.setPlayerId(buffer.readUuid());
        packet.setX(buffer.readFixedPointInt());
        packet.setY(buffer.readFixedPointInt());
        packet.setZ(buffer.readFixedPointInt());
        packet.setYaw(buffer.readAngle());
        packet.setPitch(buffer.readAngle());
        packet.setHeldItem(buffer.readShort());

        Metadata metadata = new Metadata();
        metadata.read(buffer);
        packet.setMetadata(metadata);
    }
}
