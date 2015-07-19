/**
 * This file is a component of Quartz Powered, this license makes sure any work
 * associated with Quartz Powered, must follow the conditions of the license included.
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Quartz Powered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.quartzpowered.protocol.codec.v1_8_R1.play.client;

import org.quartzpowered.network.buffer.Buffer;
import org.quartzpowered.network.protocol.codec.Codec;
import org.quartzpowered.network.session.profile.PlayerProfile;
import org.quartzpowered.network.session.profile.PlayerProperty;
import org.quartzpowered.protocol.data.Gamemode;
import org.quartzpowered.protocol.data.chat.component.serialize.ComponentSerializer;
import org.quartzpowered.protocol.data.info.PlayerInfo;
import org.quartzpowered.protocol.data.info.PlayerInfoAction;
import org.quartzpowered.protocol.packet.play.client.PlayerInfoPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerInfoCodec implements Codec<PlayerInfoPacket> {
    @Override
    public void encode(Buffer buffer, PlayerInfoPacket packet) {
        PlayerInfoAction action = packet.getAction();
        List<PlayerInfo> info = packet.getInfo();

        buffer.writeVarInt(action.getId());
        buffer.writeVarInt(info.size());

        for (PlayerInfo entry : info) {
            PlayerProfile profile = entry.getProfile();
            boolean hasDisplayName = entry.hasDisplayName();
            List<PlayerProperty> properties = profile.getProperties();

            buffer.writeUuid(profile.getUniqueId());

            switch (action) {
                case ADD:
                    buffer.writeString(profile.getName());
                    buffer.writeVarInt(properties.size());

                    for (PlayerProperty property : properties) {
                        boolean isSigned = property.isSigned();

                        buffer.writeString(property.getName());
                        buffer.writeString(property.getValue());
                        buffer.writeBoolean(isSigned);

                        if (isSigned) {
                            buffer.writeString(property.getSignature());
                        }
                    }

                    buffer.writeVarInt(entry.getGamemode().getId());
                    buffer.writeVarInt(entry.getPing());

                    buffer.writeBoolean(hasDisplayName);
                    if (hasDisplayName) {
                        buffer.writeString(ComponentSerializer.toString(entry.getDisplayName()));
                    }
                    break;

                case UPDATE_GAMEMODE:
                    buffer.writeVarInt(entry.getGamemode().getId());
                    break;

                case UPDATE_LATENCY:
                    buffer.writeVarInt(entry.getPing());
                    break;

                case UPDATE_DISPLAY_NAME:
                    buffer.writeBoolean(hasDisplayName);
                    if (hasDisplayName) {
                        buffer.writeString(ComponentSerializer.toString(entry.getDisplayName()));
                    }
                    break;

                case REMOVE:
                    break;
            }
        }
    }

    @Override
    public void decode(Buffer buffer, PlayerInfoPacket packet) {
        PlayerInfoAction action = PlayerInfoAction.fromId(buffer.readVarInt());

        int infoSize = buffer.readVarInt();
        List<PlayerInfo> info = new ArrayList<>(infoSize);

        for (int i = 0; i < infoSize; i++) {
            PlayerInfo entry = new PlayerInfo();

            UUID playerId = buffer.readUuid();

            switch (action) {
                case ADD:
                    String name = buffer.readString();
                    int propertiesSize = buffer.readVarInt();
                    List<PlayerProperty> properties = new ArrayList<>(propertiesSize);
                    for (int j = 0; j < propertiesSize; j++) {
                        String propertyName = buffer.readString();
                        String propertyValue = buffer.readString();
                        String signature = null;
                        if (buffer.readBoolean()) {
                            signature = buffer.readString();
                        }

                        properties.add(new PlayerProperty(propertyName, propertyValue, signature));
                    }
                    entry.setProfile(new PlayerProfile(name, playerId, properties));

                    entry.setGamemode(Gamemode.fromId(buffer.readVarInt()));
                    entry.setPing(buffer.readVarInt());

                    if (buffer.readBoolean()) {
                        entry.setDisplayName(ComponentSerializer.parse(buffer.readString()));
                    }
                    break;

                case UPDATE_GAMEMODE:
                    entry.setGamemode(Gamemode.fromId(buffer.readVarInt()));
                    break;

                case UPDATE_LATENCY:
                    entry.setPing(buffer.readVarInt());
                    break;

                case UPDATE_DISPLAY_NAME:
                    if (buffer.readBoolean()) {
                        entry.setDisplayName(ComponentSerializer.parse(buffer.readString()));
                    }
                    break;

                case REMOVE:
                    break;
            }
            info.add(entry);
        }

        packet.setInfo(info);
    }
}
