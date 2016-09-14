package com.mobiground.configuration.factory.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.mobiground.ecap.configuration.Configuration;

public class MobiBeanDefinition extends RootBeanDefinition {

	private static final long serialVersionUID = -7869022514649376112L;

	private String parentName;
	private String beanName;
	private Class<?> beanClass;
	private List<Dependency> dependencies;
	private List<Object> cdiBeans;
	private Method factoryMethod;
	private Configuration configuration;

	public <CONFIG_CLASS extends Configuration> MobiBeanDefinition(Class<?> beanClass,
			CONFIG_CLASS configurationInstance) {
		this.dependencies = new ArrayList<Dependency>();
		this.cdiBeans = new ArrayList<Object>();
		this.beanClass = beanClass;
		this.configuration = configurationInstance;
		super.setBeanClass(beanClass);
		super.setAutowireCandidate(Boolean.TRUE);
		super.setAutowireMode(AUTOWIRE_NO);
		super.addQualifier(new AutowireCandidateQualifier(beanClass));
		super.setScope(SCOPE_SINGLETON);
		super.setDependencyCheck(DEPENDENCY_CHECK_NONE);
		if (beanClass.getGenericInterfaces() != null && beanClass.getGenericInterfaces().length > 0) {

			super.setTargetType(beanClass.getInterfaces()[0]);
		}

	}

	public Object factoryMethod() {
		Object bean = null;
		try {
			bean = this.factoryMethod.invoke(configuration, getArgs(cdiBeans));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO excepciones de este proyecto
			throw new RuntimeException(e);
		}
		return bean;
	}

	private Object[] getArgs(List<Object> args) {
		Object[] a = {};
		return args.toArray(a);
	}

	public static void main(String[] args) {
		String[] ids = { "20", "30" };

		System.out.println(Arrays.toString(ids).replaceAll("\\[|\\]", ""));
	}

	@Override
	public String getParentName() {
		return this.parentName;
	}

	@Override
	public void setParentName(String parentName) {
		this.parentName = parentName;

	}

	@Override
	public RootBeanDefinition cloneBeanDefinition() {
		return this;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void addDependency(Dependency dependency) {
		this.dependencies.add(dependency);
	}

	public List<Dependency> getDependencies() {
		return this.dependencies;
	}

	public void setFactoryMethod(Method factoryMethod) {
		this.factoryMethod = factoryMethod;
	}

	public List<Object> getCdiBeans() {
		return this.cdiBeans;
	}

	public void addCdiBeans(Object cdiBeans) {
		this.cdiBeans.add(cdiBeans);
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

}
