package com.curlymaple.sandbox;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Handles a server-side channel.
 */
@Sharable
public class SandboxServerHandler extends SimpleChannelInboundHandler<String> {
	// @Override
	// public void channelActive(ChannelHandlerContext ctx) throws Exception {
	// logger.info("channelActive");
	// }

	private static final Logger logger = Logger.getLogger("server");

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg)
			throws Exception {
		try {
			final Channel channel = ctx.pipeline().channel();
			if (msg.indexOf("<policy-file-request/>") != -1) {
				ChannelFuture cf = channel.write(Config.CROSSDOMAIN + "\0");
				cf.addListener(new GenericFutureListener<Future<? super Void>>() {
					public void operationComplete(Future<? super Void> future)
							throws Exception {
						if (null != channel) {
							channel.disconnect();
						}
					}
				});
				logger.info("send crossdomain to:" + channel.remoteAddress());
			} else {
				logger.error(msg);
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {

		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		boolean isIOException = cause instanceof IOException;
		if (isIOException) {
			logger.info(cause.getMessage());
		} else {
			logger.error("exception:", cause);
		}
		ctx.close();
	}

}