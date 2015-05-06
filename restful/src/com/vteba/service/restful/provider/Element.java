package com.vteba.service.restful.provider;

import java.util.ArrayList;
import java.util.List;

public class Element {
	private boolean array;// 是否数组
	private List<NodeObject> nodeList = new ArrayList<NodeObject>();
	private String current;// 现在的节点的索引
	private NodeObject currentNode;// 现在的节点
	
	public boolean isArray() {
		return array;
	}

	public void setArray(boolean array) {
		this.array = array;
	}

	public List<NodeObject> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<NodeObject> nodeList) {
		this.nodeList = nodeList;
	}

	public void add(NodeObject nodeObject) {
		this.nodeList.add(nodeObject);
		this.currentNode = nodeObject;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public NodeObject getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(NodeObject currentNode) {
		this.currentNode = currentNode;
	}
}
