package com.hyf.http.service.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hyf.http.service.HttpServerInitializer;
import com.hyf.http.util.GlobalContainer;
import com.hyf.http.util.HttpServerUtil;

/**
 * 启动http服务类
 * @author 黄永丰
 * @createtime 2016年2月4日
 * @version 1.0
 */
public class HttpServer
{

	private static Log log = LogFactory.getLog(HttpServer.class);

	/** 启动http服务 */
	public static void main(String[] args) throws Exception
	{
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		applicationContext.start();
		GlobalContainer.setApplicationContext(applicationContext);
		HttpServer server = new HttpServer();
		log.info("------------Http Server listening on "+HttpServerUtil.PORT+"...--------------------");
		server.start(HttpServerUtil.PORT);
	}

	/**
	 * 启动http服务线程
	 * @author 黄永丰
	 * @createtime 2016年2月3日
	 * @param port 端口
	 * @throws Exception
	 */
	public void start(int port) throws Exception
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try
		{
			ServerBootstrap b = new ServerBootstrap();
			//设置httpserver
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new HttpServerInitializer())
				.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
			//
			ChannelFuture cf = b.bind(port).sync();
			
			String httpstr = HttpServerUtil.SSL ? "https" : "http";
			log.info("--------------"+httpstr + " server start sucessful bind at port " + HttpServerUtil.PORT + "--------------");
			log.info("--------------Open your browser and navigate to " + httpstr + "://localhost:" + HttpServerUtil.PORT + "/openapi --------------");
			
			//
			cf.channel().closeFuture().sync();
		}
		finally
		{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
