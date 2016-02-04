package com.hyf.http.service;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hyf.http.entity.ResultStruct;
import com.hyf.http.util.GlobalContainer;
import com.hyf.http.util.HttpServerUtil;
import com.hyf.http.util.MethodReflect;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

/**
 * http请求消息处理
 * @author 黄永丰
 * @createtime 2016年2月3日
 * @version 1.0
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object>
{
	private static Log log = LogFactory.getLog(HttpServerHandler.class);

	/** 存http传入对象 */
	private HttpRequest request;
	/** 打印 */
	private StringBuilder requestbuffer = new StringBuilder();
	/***/
	private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); // Disk

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		String buf = "";// http请求后返回的数据
		// 传入msg对象是否是HttpRequest
		if (msg instanceof HttpRequest)
		{
			// 1、记录打印信息
			HttpRequest request = this.request = (HttpRequest) msg;
			requestbuffer.append("\r\n############################BEGIN REQUEST##########################\r\n");
			requestbuffer.append("VERSION: ").append(request.getProtocolVersion()).append("\r\n");
			requestbuffer.append("HOSTNAME: ").append(HttpHeaders.getHost(request, "unknown")).append("\r\n");
			requestbuffer.append("REQUEST_URI: ").append(request.getUri()).append("\r\n");
			// 2、获取http的headers信息并打印
			HttpHeaders headers = request.headers();
			for (Entry<String, String> entry : headers)
			{
				requestbuffer.append("HEADER: " + entry.getKey() + '=' + entry.getValue() + "\r\n");
			}
			// 3、首先对HTTP请求消息的解码结构进行判断,如果解码失败,直接构造HTTP404 错误返回,
			if (request.getDecoderResult().isFailure())
			{
				requestbuffer.append("\r\n############################END REQUEST##########################\r\n");
				buf = "解码失败.";
				requestbuffer.append("请求完返回结果信息: ");
				requestbuffer.append(buf + "\r\n");
				log.error(requestbuffer.toString());
				writeResponse(request, ctx, new ResultStruct(-1, buf).toString());
				return;
			}
			// 4、判断URI
			try
			{
				URI uri = new URI(request.getUri());
				// 每次请求都会有这个请求的，直接return就可以了
				if (uri.getPath().equals("/favicon.ico"))
				{
					return;
				}
				// 自己定义的请求路径才通过
				if (!uri.getPath().equals("/openapi"))
				{
					requestbuffer.append("\r\n############################END REQUEST##########################\r\n");
					buf = "url路径错误.";
					requestbuffer.append("请求完返回结果信息: ");
					requestbuffer.append(buf + "\r\n");
					log.error(requestbuffer.toString());
					writeResponse(request, ctx, new ResultStruct(-1, buf).toString());
					return;
				}
			}
			catch (Exception e)
			{
				requestbuffer.append("URI解释出错." + e.getMessage());
				log.error(requestbuffer.toString());
				buf = "URI解释出错." + e.getMessage();
				writeResponse(request, ctx, new ResultStruct(-1, buf).toString());
				return;
			}
			// 5、GET请求处理
			if (request.getMethod().equals(HttpMethod.GET))
			{
				try
				{
					requestbuffer.append("\r\n############################END REQUEST##########################\r\n");
					Map<String, String> paramsMap = new HashMap<String, String>();
					// 获取http请求传入的参数
					QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
					Map<String, List<String>> paramsList = queryStringDecoder.parameters();
					if (!paramsList.isEmpty())
					{
						for (Entry<String, List<String>> params : paramsList.entrySet())
						{
							String key = params.getKey().trim();
							List<String> vals = params.getValue();
							if (vals.size() > 0) paramsMap.put(key, vals.get(0).trim());
						}
					}
					// 调用对应的接口
					buf = doPost(paramsMap);
					requestbuffer.append("请求完返回结果信息: ");
					requestbuffer.append(buf + "\r\n");
					log.info(requestbuffer.toString());
					// 将http响应的结果返回给用户
					writeResponse(request, ctx, new ResultStruct(-1, buf).toString());
					return;
				}
				catch (Exception e)
				{
					requestbuffer.append("解释HTTP/HTTPS GET协议出错." + e.getMessage());
					log.error(requestbuffer.toString());
					buf = "解释HTTP/HTTPS GET协议出错." + e.getMessage();
					writeResponse(request, ctx, new ResultStruct(-1, buf).toString());
					return;
				}
			}

			// 6、POST请求处理
			if (request.getMethod().equals(HttpMethod.POST))
			{
				try
				{
					requestbuffer.append("\r\n############################END REQUEST##########################\r\n");
					Map<String, String> paramsMap = new HashMap<String, String>();
					HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);
					while (decoder.hasNext())
					{
						InterfaceHttpData ifhData = decoder.next();
						if (ifhData != null)
						{
							try
							{
								/** HttpDataType有三种类型 Attribute, FileUpload, InternalAttribute */
								if (ifhData.getHttpDataType() == HttpDataType.Attribute)
								{
									Attribute attribute = (Attribute) ifhData;
									String value = attribute.getValue();
									String key = attribute.getName();
									paramsMap.put(key, value);
								}
							}
							finally
							{
								ifhData.release();
							}
						}
					}
					if (paramsMap.isEmpty())
					{
						HttpContent httpContent = (HttpContent) request;
						String data = httpContent.content().toString(CharsetUtil.UTF_8);
						JSONObject obj = JSONObject.fromObject(data);
						paramsMap.putAll(obj);
					}
					buf = doPost(paramsMap);
					requestbuffer.append("请求完返回结果信息: ");
					requestbuffer.append(buf + "\r\n");
					log.info(requestbuffer.toString());
					// 将http响应的结果返回给用户
					writeResponse(request, ctx, new ResultStruct(-1, buf).toString());
					return;
				}
				catch (Exception e)
				{
					requestbuffer.append("解释HTTP/HTTPS POST协议出错." + e.getMessage());
					log.error(requestbuffer.toString());
					buf = "解释HTTP/HTTPS POST协议出错." + e.getMessage();
					writeResponse(request, ctx, new ResultStruct(-1, buf).toString());
					return;
				}
			}
			// 7、如果不是GET和POST请求就返回错误
			buf = "只支持HTTP/HTTPS GET/POST协议.";
			requestbuffer.append("请求完返回结果信息: ");
			requestbuffer.append(buf + "\r\n");
			log.error(requestbuffer.toString());
			writeResponse(request, ctx, new ResultStruct(-1, buf).toString());
			return;
		}
		// 如果请求有误 将http响应的结果返回给用户(传入msg对象不是HttpRequest)
		buf = "http请求失败.";
		requestbuffer.append("请求完返回结果信息: ");
		requestbuffer.append(buf + "\r\n");
		log.error(requestbuffer.toString());
		writeResponse(request, ctx, new ResultStruct(-1, buf).toString());
		return;
	}

	/**
	 * 将http响应的结果返回给用户
	 * @author 黄永丰
	 * @createtime 2016年2月3日
	 * @param currentObj
	 * @param ctx
	 * @param exceptionerror
	 * @param sendmsg
	 */
	private void writeResponse(HttpObject currentObj, ChannelHandlerContext ctx, String sendmsg)
	{
		// 1、获取请求响应状态
		HttpResponseStatus mystatus = currentObj.getDecoderResult().isSuccess() ? HttpResponseStatus.OK : HttpResponseStatus.BAD_REQUEST;
		// 2、设置返回类型
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, mystatus, Unpooled.copiedBuffer(sendmsg, CharsetUtil.UTF_8));
		// response.headers().set(CONTENT_TYPE,"text/plain; charset=UTF-8");
		response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
		// 3、获取http协议的Keep-Alive
		// HTTP/1.1起，默认都开启了Keep-Alive，保持连接特性，简单地说，当一个网页打开完成后，客户端和服务器之间用于传输HTTP数据的TCP连接不会关闭，
		// 如果客户端再次访问这个服务器上的网页，会继续使用这一条已经建立的连接Keep-Alive不会永久保持连接，它有一个保持时间，可以在不同的服务器软件（如Apache）中设定这个时间
		boolean keepAlive = HttpHeaders.isKeepAlive(request);
		boolean bkeepAlive = HttpServerUtil.KEEP_ALIVE;
		if (keepAlive && !bkeepAlive)
		{
			response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
			response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
		ChannelFuture future = ctx.writeAndFlush(response);
		// 如果http不是长连接那一定要关闭
		if (bkeepAlive) future.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 获取dubbo服务并调用对应的接口
	 * @author 黄永丰
	 * @createtime 2016年2月3日
	 * @param params http请求传入参数
	 * @return 返回dubbo调用接口后的返回数据
	 */
	private String doPost(Map<String, String> paramsMap)
	{
		if (!paramsMap.containsKey("interfaceName"))
		{
			return "参数并不包含interfaceName,请检查提交的参数.";
		}
		// 获取接口名称
		String interfaceName = paramsMap.get("interfaceName").toLowerCase();
		// 获取bubbo服务名称
		String serviceName = getServiceName(interfaceName);
		if (serviceName == null)
		{
			return "参数interfaceName出错,请检查提交的参数.";
		}
		// 获取dubbo服务对象
		Object client = GlobalContainer.getApplicationContext().getBean(serviceName);
		if (client == null)
		{
			return "服务名称为" + serviceName + "为空,请联系后台管理员.";
		}
		// 用反射调用对应系统的方法
		String data = (String) MethodReflect.invokeMethod(client, "doPost", new Object[] { paramsMap });
		if (data == null)
		{
			return "调用系统服务接口方法失败.";
		}
		return data;
	}

	/**
	 * 骑过接口名称找到对应的服务名称
	 * @author 黄永丰
	 * @createtime 2016年2月3日
	 * @param interfaceName 接口名称
	 * @return 返回dubbo服务名称
	 */
	public static String getServiceName(String interfaceName)
	{
		String result = null;
		String[] strs = interfaceName.split("\\.");
		if (strs.length > 2)
		{
			result = strs[1];
			result += "Service";
		}
		return result;
	}

}
