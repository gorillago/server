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
package org.quartzpowered.network.init;

import com.google.inject.assistedinject.Assisted;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import org.quartzpowered.network.codec.CodecFactory;
import org.quartzpowered.network.pipeline.NoopHandler;
import org.quartzpowered.network.protocol.packet.Packet;

import javax.inject.Inject;
import javax.inject.Singleton;

public class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Inject private CodecFactory codecFactory;
    @Inject private NoopHandler noop;

    private final boolean clientSide;
    private final SimpleChannelInboundHandler<Packet> handler;

    @Inject
    private NetworkChannelInitializer(@Assisted boolean clientSide,
                                      @Assisted SimpleChannelInboundHandler<Packet> handler) {
        this.clientSide = clientSide;
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast("encryption", noop)
                .addLast("frame", codecFactory.createFrameCodec())
                .addLast("compression", noop)
                .addLast("packet", codecFactory.createPacketCodec(clientSide))
                .addLast("handler", handler);
    }
}
