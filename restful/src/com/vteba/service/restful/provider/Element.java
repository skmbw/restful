package com.vteba.service.restful.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Json片段，可能是一个json对象，或者一个json数组。
 * @author yinlei
 * @date 2015-5-10 16:38
 */
public class Element {
	private boolean array;// 是否数组
	private List<NodeObject> nodeList = new ArrayList<NodeObject>();
	private String current;// 现在的节点的索引
	private NodeObject currentNode;// 现在的节点
	private HashMap<String, NodeObject> contains = new HashMap<String, NodeObject>();
	
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
		this.contains.put(this.currentNode.getIndex(), currentNode);
	}

	public String getCurrent() {
		return current;
	}

	public NodeObject getCurrentNode() {
		return currentNode;
	}

//	public void setCurrentNode(NodeObject currentNode) {
//		this.currentNode = currentNode;
//		this.current = currentNode.getIndex();
//		this.contains.put(this.current, currentNode);
//	}
	
//	public boolean isContains(String current) {
//		return contains.containsKey(current);
//	}
	
	public NodeObject getNodeObject(String current) {
		return contains.get(current);
	}
}
