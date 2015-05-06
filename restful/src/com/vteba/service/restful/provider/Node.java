package com.vteba.service.restful.provider;

public class Node {
	private String name;// key
	private String value;
	private boolean primitive;// 基本类型时，不需要name

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isPrimitive() {
		return primitive;
	}

	public void setPrimitive(boolean primitive) {
		this.primitive = primitive;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (primitive) {
			sb.append("\"").append(value).append("\"");
		} else {
			sb.append("\"").append(name).append("\":\"").append(value).append("\"");
		}
		return sb.toString();
	}

}
