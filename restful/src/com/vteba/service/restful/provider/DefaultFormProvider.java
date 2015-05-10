package com.vteba.service.restful.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.vteba.user.model.User;

/**
 * 表单类型的提交的数据解析器。接受表单post提交。返回json数据。
 * 
 * @author yinlei
 * @see
 * @since 2015年4月30日 上午10:26:23
 */
@Named// 表示一个bean
@Provider// 表示是一个数据解析提供者
@Consumes(value = { MediaType.APPLICATION_FORM_URLENCODED })// 接受表单类型的数据
@Produces(value = { MediaType.APPLICATION_JSON })// 产生json格式的数据
public class DefaultFormProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		// 这里一般要做一些限制，根据mediaType，等参数
		return true;
	}

	@Override
	public long getSize(Object t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		
		return -1L;
	}

	// 产生json数据给调用端
	@Override
	public void writeTo(Object t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		
		byte[] b = JSON.toJSONBytes(t);
		entityStream.write(b);
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		// 一般要做验证
		return true;
	}

	// 从客户端提交的数据，解析数据
	@Override
	public Object readFrom(Class<Object> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		// 这里只是简单的处理了，更通用的做法是 要能够处理任何类型。
		User user = new User();
		
		String content = IOUtils.toString(entityStream);
		String[] forms = content.split("&");
		if (forms != null) {
			for (String form : forms) {
				String[] args = form.split("=");
				if (args != null && args.length == 2) {
					String key = args[0];
					String value = args[1];
					if (key.equals("id")) {
						Long id = Long.valueOf(value);
						user.setId(id);
						continue;
					} else if (key.equals("name")) {
						user.setName(value);
						continue;
					}
				}
			}
		}
		return user;
	}

	public static String buildJson(String content) {
		if (content != null && !content.equals("")) {
			StringBuilder json = new StringBuilder();
			//boolean array = true;
			boolean append = false;
			String[] forms = content.split("&");
			if (forms != null) {
				
				JsonObject jsonObject = new JsonObject();
				for (String form : forms) {
					String[] args = form.split("=");
					if (args != null && args.length == 2) {
						String key = args[0];
						String value = args[1];
						if (key != null && value != null) {
							int dotIndex = key.indexOf(".");
							if (dotIndex > -1) {// 含有多级属性
								int arrayIndex = key.indexOf("[");
								if (arrayIndex > -1) {// 数组或者list
									String prefix = key.substring(0, dotIndex - 3);
									String current = key.substring(dotIndex - 2, dotIndex - 1);
									
									Element element = jsonObject.get(prefix);
									NodeObject nodeObject = null;
									if (element == null) {// 新出现的节点，创建
										element = new Element();
										element.setArray(true);
										//element.setCurrent(current);
										nodeObject = new NodeObject(current);
										element.add(nodeObject);
										
										jsonObject.add(prefix, element);
									//} else if (!current.equals(element.getCurrent())) {
									} else {
										nodeObject = element.getNodeObject(current);
										if (nodeObject == null) {
											//element.setCurrent(current);
											nodeObject = new NodeObject(current);
											element.add(nodeObject);
										}
//										else {
//											nodeObject = element.getCurrentNode();
//										}
									}
									
									Node node = new Node();
									String name = key.substring(dotIndex + 1);
									node.setName(name);
									node.setValue(value);
									nodeObject.add(node);
								} else {// 多级属性，但是不是list
									//array = false;
									String prefix = key.substring(0, dotIndex);
									Element element = jsonObject.get(prefix);
									NodeObject nodeObject = null;
									if (element == null) {
										element = new Element();
										nodeObject = new NodeObject();
										element.add(nodeObject);
										
										jsonObject.add(prefix, element);
									} else {
										nodeObject = element.getCurrentNode();
									}
									
									Node node = new Node();
									String name = key.substring(dotIndex + 1);
									node.setName(name);
									node.setValue(value);
									nodeObject.add(node);
								}
							} else {// 无级联属性
								int arrayIndex = key.indexOf("[");// 基本类型数组
								if (arrayIndex > -1) {
									String prefix = key.substring(0, arrayIndex);
									
									Element element = jsonObject.get(prefix);
									NodeObject nodeObject = null;
									if (element == null) {
										element = new Element();
										element.setArray(true);
										nodeObject = new NodeObject();
										nodeObject.setPrimitive(true);
										element.add(nodeObject);
										
										jsonObject.add(prefix, element);
									} else {
										nodeObject = element.getNodeList().get(0);
									}
									
									Node node = new Node();
									node.setValue(value);
									node.setPrimitive(true);
									nodeObject.add(node);
								} else {// 普通键值对
									//array = false;
									if (!append) {
										json.append("\"").append(key).append("\":\"").append(value).append("\"");
										append = true;
									} else {
										json.append(",\"").append(key).append("\":\"").append(value).append("\"");
									}
								}
							}
						}
					}
				}
				json.append(jsonObject.toString());
			}
//			if (array) {
//				json.replace(0, 1, "{").append("}");
//			} else {
//				json.insert(0, "{").append("}");
//			}
			if (json.charAt(0) == ',') {
				json.replace(0, 1, "{").append("}");
			} else {
				json.insert(0, "{").append("}");
			}
			return json.toString();
		}
		return null;
	}
	
	public static String buildJson(InputStream is) {
		try {
			String content = IOUtils.toString(is);
			return buildJson(content);
		} catch (IOException e) {
			
		}
		return null;
	}
	
	public static void main(String[] args) {
		StringBuilder text = new StringBuilder();
		text.append("{\"").append("id\":\"").append("2\",").append("\"name\":\"yinlei\"}");
		User user = JSON.parseObject(text.toString(), User.class);
		System.out.println(user);
		long d = System.currentTimeMillis();
		// 测试1
		String content = "userList[1].name=yinlei3&user.id=66&user.name=yinleiaa&strList[0]=aa&id=22&name=yinlei&userList[0].id=33&createDate=2015-04-30 14:22:17&userList[0].name=yinlei2&userList[0].createDate=2015-5-1 14:28:11&userList[1].id=44&userList[1].createDate=2015-2-4 14:28:11&strList[1]=bb";
		content = buildJson(content);
		System.out.println(content);
		user = JSON.parseObject(content, User.class);
		System.out.println(System.currentTimeMillis() - d);
		
		// 测试2
		d = System.currentTimeMillis();
		content = "userList[1].name=yinlei3&strList[0]=aa&userList[0].id=33&userList[0].name=yinlei2&userList[0].createDate=2015-5-1 14:28:11&userList[1].id=44&userList[1].createDate=2015-2-4 14:28:11&strList[1]=bb";
		content = buildJson(content);
		System.out.println(content);
		user = JSON.parseObject(content, User.class);
		System.out.println(System.currentTimeMillis() - d);
		
		// 测试3
		d = System.currentTimeMillis();
		content = "userList[1].name=yinlei3&userList[0].id=33&userList[0].name=yinlei2&userList[0].createDate=2015-5-1 14:28:11&userList[1].id=44&userList[1].createDate=2015-2-4 14:28:11";
		content = buildJson(content);
		System.out.println(content);
		user = JSON.parseObject(content, User.class);
		System.out.println(System.currentTimeMillis() - d);
		
		// 测试4
		d = System.currentTimeMillis();
		content = "user.id=66&user.name=yinleiaa&user.createDate=2015-04-30 14:22:17";
		content = buildJson(content);
		System.out.println(content);
		user = JSON.parseObject(content, User.class);
		System.out.println(System.currentTimeMillis() - d);
		
		// 测试5
		d = System.currentTimeMillis();
		content = "id=22&name=yinlei&createDate=2015-04-30 14:22:17";
		content = buildJson(content);
		System.out.println(content);
		user = JSON.parseObject(content, User.class);
		System.out.println(System.currentTimeMillis() - d);
	}
}
