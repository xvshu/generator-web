package com.eloancn.framework.utils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
  
public class NettyClient {  
  
  
    public static void send(final String host,final int port,final String message) {  
        // Configure the client.  
        ClientBootstrap bootstrap = new ClientBootstrap(  
                new NioClientSocketChannelFactory(  
                        Executors.newCachedThreadPool(),  
                        Executors.newCachedThreadPool()));  
        // Set up the pipeline factory.  
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {  
            public ChannelPipeline getPipeline() throws Exception {  
                return Channels.pipeline(new SimpleChannelUpstreamHandler (){
                        	
                            @Override  
                            public void channelConnected(  
                                    ChannelHandlerContext ctx, ChannelStateEvent e) {  
                                ChannelBuffer sendBuff = ChannelBuffers.dynamicBuffer();  
                                sendBuff.writeBytes(message.getBytes());  
                                e.getChannel().write(sendBuff);  
                            }  
                          
                            @Override  
                            public void messageReceived(  
                                    ChannelHandlerContext ctx, MessageEvent e) {  
                                // Send back the received message to the remote peer.  
//                                ChannelBuffer acceptBuff = (ChannelBuffer) e.getMessage();  
//                                String info = acceptBuff.toString(Charset.defaultCharset());  
                                e.getChannel().close();  
                            }  
                          
                            @Override  
                            public void exceptionCaught(  
                                    ChannelHandlerContext ctx, ExceptionEvent e) {  
                                // Close the connection when an exception is raised.  
                                e.getCause();  
                                e.getChannel().close();  
                            }  
        
                        });  
            }  
        });  
        // Start the connection attempt.  
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));  
        // Wait until the connection is closed or the connection attempt fails.  
        future.getChannel().getCloseFuture().awaitUninterruptibly();  
        // Shut down thread pools to exit.  
        bootstrap.releaseExternalResources();  
    }  
}