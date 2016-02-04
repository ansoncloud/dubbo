package com.hyf.http.service;

import javax.net.ssl.SSLEngine;

import com.hyf.http.util.HttpServerUtil;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

/**
 * 初始化httpserver服务的些配置项目
 * @author 黄永丰
 * @createtime 2016年2月3日
 * @version 1.0
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel>
{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = ch.pipeline();
		//开户https访问
		if (HttpServerUtil.SSL)
		{
			SSLEngine engine = HttpSSLContext.getSSLContext().createSSLEngine();
			engine.setNeedClientAuth(HttpServerUtil.WAY_ENCRYPTION);
			engine.setUseClientMode(false);
			engine.setWantClientAuth(false);
			engine.setEnabledProtocols(new String[] { "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2" });
			pipeline.addLast("ssl", new SslHandler(engine));
		}

		/**40秒没有数据读入，发生超时机制*/
		pipeline.addLast(new ReadTimeoutHandler(HttpServerUtil.READTIME));

		/**40秒没有输入写入，发生超时机制*/
		pipeline.addLast(new WriteTimeoutHandler(HttpServerUtil.WRITETIME));

		/**http-request解码器 http服务器端对request解码*/
		pipeline.addLast("decoder", new HttpRequestDecoder());

		/**http-response解码器 http服务器端对response编码 */
		pipeline.addLast("encoder", new HttpResponseEncoder());

		/**HttpObjectAggregator会把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse。*/
		pipeline.addLast("aggregator", new HttpObjectAggregator(HttpServerUtil.AGGREGATOR));

		/**
		 * 压缩 Compresses an HttpMessage and an HttpContent in gzip or deflate encoding while
		 * respecting the "Accept-Encoding" header. If there is no matching encoding, no compression is done.
		 */
		pipeline.addLast("deflater", new HttpContentCompressor());
		
		/**自己定义http请求消息处理*/
		pipeline.addLast("handler", new HttpServerHandler());

	}

}
