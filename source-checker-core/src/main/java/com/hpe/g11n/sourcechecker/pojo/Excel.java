package com.hpe.g11n.sourcechecker.pojo;

import java.util.List;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月31日
 * @Time: 上午11:13:52
 *
 */
public class Excel {
	public String name;
	public List<String> header;
	public List<List<String>> value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	public List<List<String>> getValue() {
		return value;
	}

	public void setValue(List<List<String>> value) {
		this.value = value;
	}

}
