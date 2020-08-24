package com.csc.rabbitmqdemo.entry;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -4076516019527805945L;
	
	private String name;
	
	private String address;

	public User(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", address='" + address + '\'' +
				'}';
	}
}