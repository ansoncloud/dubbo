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
		log.info("------------Http Server listening on " + HttpServerUtil.PORT + "...--------------------");
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
		// 1、接收客户端连接用
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// 2、处理网络读写事件
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try
		{
			// 3、配置服务器启动类
			ServerBootstrap b = new ServerBootstrap();
			// 设置httpserver
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new HttpServerInitializer())
				.option(ChannelOption.SO_BACKLOG,1024);//服务端处理线程全忙后，允许多少个新请求进入等待。
//				.childOption(ChannelOption.SO_KEEPALIVE, true);保持长连接状态
			// 绑定端口 等待绑定成功
			ChannelFuture cf = b.bind(port).sync();
			String httpstr = HttpServerUtil.SSL ? "https" : "http";
			log.info("--------------" + httpstr + " server start sucessful bind at port " + HttpServerUtil.PORT + "--------------");
			log.info("--------------Open your browser and navigate to " + httpstr + "://localhost:" + HttpServerUtil.PORT + "/openapi --------------");
			// 等待服务器退出
			cf.channel().closeFuture().sync();
		}
		finally
		{
			//释放线程资源
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
