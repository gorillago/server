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
package org.quartzpowered.protocol.data;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartzpowered.network.buffer.Buffer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSlot {
    private int itemId;
    private int count;
    private int damage;

    public void write(Buffer buffer) {
        buffer.writeShort(itemId);
        buffer.writeByte(count);
        buffer.writeShort(damage);
        buffer.writeByte(0);
    }

    public ItemSlot read(Buffer buffer) {
        itemId = buffer.readShort();
        count = buffer.readByte();
        damage = buffer.readShort();
        buffer.readByte();

        return this;
    }
}
