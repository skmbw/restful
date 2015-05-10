package com.vteba.service.restful.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * 代表一个Json对象
 * @author yinlei
 * @date 2015-5-1 23:37
 */
public class NodeObject {
	private boolean primitive;// 基本类型数组，不需要使用{}
	private List<Node> nodeList = new ArrayList<Node>();
	private String index;

	public NodeObject() {
	}
	
	public NodeObject(String index) {
		super();
		this.index = index;
	}

	public List<Node> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<Node> nodeList) {
		this.nodeList = nodeList;
	}

	public void add(Node node) {
		nodeList.add(node);
	}
	
	public boolean isPrimitive() {
		return primitive;
	}

	public void setPrimitive(boolean primitive) {
		this.primitive = primitive;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (!primitive) {
			sb.append("{");
		}
		boolean dot = false;
		for (Node node : nodeList) {
			if (!dot) {
				sb.append(node.toString());
				dot = true;
			} else {
				sb.append(",").append(node.toString());
			}
		}
		if (!primitive) {
			sb.append("}");
		}
		return sb.toString();
	}
	
}
