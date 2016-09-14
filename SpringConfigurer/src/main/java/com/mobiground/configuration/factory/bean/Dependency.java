package com.mobiground.configuration.factory.bean;

public class Dependency {

	public Dependency() {
		super();
	}

	public Dependency(String beanName, Class clazz) {
		super();
		this.beanName = beanName;
		this.clazz = clazz;
	}

	private String beanName;

	private Class clazz;

	/**
	 * @return the beanName
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * @return the clazz
	 */
	public Class getClazz() {
		return clazz;
	}

	/**
	 * @param beanName the beanName to set
	 */
	public void setBeanName(final String beanName) {
		this.beanName = beanName;
	}

	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(final Class clazz) {
		this.clazz = clazz;
	}
}
