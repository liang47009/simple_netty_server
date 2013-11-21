package com.curlymaple.sandbox;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SandboxServer {

	private static final Logger logger = Logger.getLogger("server");

	public void run(String ip, int port) throws Exception {
		System.setProperty("io.netty.noUnsafe", "true");
		System.setProperty("io.netty.noJavassist", "false");
		System.setProperty("io.netty.noPreferDirect", "true");
		System.setProperty("io.netty.noResourceLeakDetection", "false");
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new SandboxServerInitializer());
			b.bind(ip, port).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		String ip = Config.getProperty(Config.IP);
		String port = Config.getProperty(Config.PORT);
		logger.info("Server ip:" + ip + ", port:" + port);
		new SandboxServer().run(ip, Integer.valueOf(port));
	}
}