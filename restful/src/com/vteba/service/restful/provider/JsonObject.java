package com.vteba.service.restful.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 容器，承装json字符片段
 * @author yinlei
 * @date 2015-5-1 23:38
 */
public class JsonObject {
	private Map<String, Element> elementList = new HashMap<String, Element>();
	
	public void add(String name, Element element) {
		this.elementList.put(name, element);
	}
	
	public Element get(String name) {
		return this.elementList.get(name);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean dot = false;
		for (Entry<String, Element> entry : elementList.entrySet()) {
			Element element = entry.getValue();
			sb.append(",\"").append(entry.getKey()).append("\":");
			if (element.isArray()) {
				sb.append("[");
				List<NodeObject> nodelList = element.getNodeList();
				boolean append = false;
				for (NodeObject object : nodelList) {
					if (!dot) {
						sb.append(object.toString());
						dot = true;
					} else {
						if (!append) {
							append = true;
						} else {
							sb.append(",");
						}
						sb.append(object.toString());
					}
				}
				sb.append("]");
			} else {
				NodeObject object = element.getNodeList().get(0);
				sb.append(object.toString());
			}	
		}
		return sb.toString();
	}
}
