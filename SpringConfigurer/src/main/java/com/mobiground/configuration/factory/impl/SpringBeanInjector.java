package com.mobiground.configuration.factory.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.mobiground.configuration.factory.BeanInjector;
import com.mobiground.configuration.factory.bean.Dependency;
import com.mobiground.configuration.factory.bean.MobiBeanDefinition;
import com.mobiground.ecap.configuration.Configuration;
import com.mobiground.ecap.exception.configuration.MobiSpringBean;
import com.mobiground.ecap.util.string.UString;

/**
 * The Class SpringBeanInjector.
 */
public class SpringBeanInjector implements BeanInjector {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiground.configuration.factory.BeanInjector#injectBeans(org.springframework.context.
	 * ConfigurableApplicationContext, com.mobiground.ecap.configuration.Configuration)
	 */
	@Override
	@Deprecated
	public <CONFIG_CLASS extends Configuration> void injectBeans(final ConfigurableApplicationContext context,
			final CONFIG_CLASS configurationInstance) {
		List<MobiSpringBean> beans = null;
		fillBeans(configurationInstance, beans);
		addBeansToContext(context, beans);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiground.configuration.factory.BeanInjector#getBeanDefinition(com.mobiground.ecap.configuration.
	 * Configuration)
	 */
	@Override
	public <CONFIG_CLASS extends Configuration> List<MobiBeanDefinition> getBeanDefinition(
			final CONFIG_CLASS configurationInstance) {
		List<MobiBeanDefinition> beanDefinitions = new ArrayList<>();
		Iterator<Method> toBeRegisterdBeanIterator = getAllBeanToBeRegistered(configurationInstance);
		while (toBeRegisterdBeanIterator.hasNext()) {
			Method toBeInjectedBen = (Method) toBeRegisterdBeanIterator.next();
			if (isValidBean(toBeInjectedBen)) {
				beanDefinitions.add(getBeanDefinition(configurationInstance, toBeInjectedBen));
			}
		}
		return beanDefinitions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiground.configuration.factory.BeanInjector#addDependencies(com.mobiground.configuration.factory.bean.
	 * MobiBeanDefinition, org.springframework.context.ApplicationContext)
	 */
	@Override
	public MobiBeanDefinition addDependencies(final MobiBeanDefinition beanDefinition,
			final ApplicationContext context) {
		Iterator<Dependency> iteratorDependencies = beanDefinition.getDependencies().iterator();
		while (iteratorDependencies.hasNext()) {
			Dependency dependency = (Dependency) iteratorDependencies.next();
			beanDefinition.addCdiBeans(ApplicationContext.class.isAssignableFrom(dependency.getClazz()) ? context
					: getBeanFrom(dependency, context, beanDefinition.getConfiguration().getConfigName()));
		}
		return beanDefinition;
	}

	/**
	 * Gets the bean from.
	 *
	 * @param dependency the dependency
	 * @param context the context
	 * @param prefix the prefix
	 * @return the bean from
	 */
	private Object getBeanFrom(final Dependency dependency, final ApplicationContext context, final String prefix) {
		Object bean = null;
		if (!context.containsBean(dependency.getBeanName())) {
			Map<String, Object> beans = (Map<String, Object>) context.getBeansOfType(dependency.getClazz());
			String[] keySet = beans.keySet().toArray(new String[] {});
			String beanKey = keySet[0];
			if (beans.keySet().size() > 1) {
				bean = setByPrefix(prefix, beans, keySet, beanKey);
			} else {
				bean = beans.get(beanKey);
			}
		} else {
			bean = context.getBean(dependency.getBeanName());
		}

		return bean;
	}

	/**
	 * Sets the by prefix.
	 *
	 * @param prefix the prefix
	 * @param beans the beans
	 * @param keySet the key set
	 * @param beanKey the bean key
	 * @return the object
	 */
	private Object setByPrefix(final String prefix, final Map<String, Object> beans, final String[] keySet,
			final String beanKey) {
		Object bean = null;
		if (UString.isEmpty(prefix)) {
			bean = beans.get(checkAllBeanNames(keySet, beanKey));
		} else {
			bean = checkAllBeansPrefix(prefix, beans, keySet, beanKey);
		}
		return bean;
	}

	/**
	 * Check all beans prefix.
	 *
	 * @param prefix the prefix
	 * @param beans the beans
	 * @param keySet the key set
	 * @param beanKey the bean key
	 * @return the object
	 */
	private Object checkAllBeansPrefix(final String prefix, final Map<String, Object> beans, final String[] keySet,
			final String beanKey) {
		Object bean = null;
		for (String key : keySet) {
			if (isSamePrefix(prefix, key)) {
				bean = beans.get(beanKey);
			}
		}
		return bean;
	}

	/**
	 * Checks if is same prefix.
	 *
	 * @param prefix the prefix
	 * @param key the key
	 * @return true, if is same prefix
	 */
	private boolean isSamePrefix(final String prefix, final String key) {
		return StringUtils.startsWith(key, prefix);
	}

	/**
	 * Check all bean names.
	 *
	 * @param keySet the key set
	 * @param beanKey the bean key
	 * @return the string
	 */
	private String checkAllBeanNames(final String[] keySet, final String beanKey) {
		String key = beanKey;
		for (int i = 1; i < keySet.length; i++) {
			key = overriveBeanName(keySet, beanKey, i);
		}
		return key;
	}

	/**
	 * Overrive bean name.
	 *
	 * @param keySet the key set
	 * @param beanKey the bean key
	 * @param i the i
	 * @return the string
	 */
	private String overriveBeanName(final String[] keySet, final String beanKey, final int i) {
		String key = beanKey;
		if (isShorterThan(keySet[i], beanKey)) {
			key = keySet[i];
		}
		return key;
	}

	/**
	 * Checks if is shorter than.
	 *
	 * @param keySet the key set
	 * @param beanKey the bean key
	 * @return true, if is shorter than
	 */
	private boolean isShorterThan(final String keySet, final String beanKey) {
		return keySet.length() < beanKey.length();
	}

	/**
	 * Adds the beans to context.
	 *
	 * @param context the context
	 * @param beans the beans
	 */
	private static void addBeansToContext(final ConfigurableApplicationContext context,
			final List<MobiSpringBean> beans) {
		DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();
		for (MobiSpringBean mobiSpringBean : beans) {
			factory.registerSingleton(mobiSpringBean.getName(), mobiSpringBean.getBean());

		}
	}

	/**
	 * Fill beans.
	 *
	 * @param <CONFIG_CLASS> the generic type
	 * @param configurationInstance the configuration instance
	 * @param beans the beans
	 */
	private static <CONFIG_CLASS extends Configuration> void fillBeans(final CONFIG_CLASS configurationInstance,
			final List<MobiSpringBean> beans) {
		Iterator<Method> toBeRegisterdBeanIterator = getAllBeanToBeRegistered(configurationInstance);
		while (toBeRegisterdBeanIterator.hasNext()) {
			Method toBeInjectedBen = (Method) toBeRegisterdBeanIterator.next();
			fillBean(configurationInstance, configurationInstance.getConfigName(), beans, toBeInjectedBen);
		}
	}

	/**
	 * Gets the all bean to be registered.
	 *
	 * @param <T> the generic type
	 * @param configurationInstance the configuration instance
	 * @return the all bean to be registered
	 */
	private static <T> Iterator<Method> getAllBeanToBeRegistered(final T configurationInstance) {
		Method[] toBeRegistered = configurationInstance.getClass().getDeclaredMethods();
		List<Method> methodList = Arrays.asList(toBeRegistered);
		Iterator<Method> methodIterator = methodList.iterator();
		return methodIterator;
	}

	/**
	 * Fill bean.
	 *
	 * @param <T> the generic type
	 * @param configurationInstance the configuration instance
	 * @param prefix the prefix
	 * @param beans the beans
	 * @param toBeInjectedBen the to be injected ben
	 */
	private static <T> void fillBean(final T configurationInstance, final String prefix,
			final List<MobiSpringBean> beans, final Method toBeInjectedBen) {
		try {
			if (isValidBean(toBeInjectedBen)) {
				MobiSpringBean mobiSpringBean = fillMobiSpringBean(configurationInstance, prefix, toBeInjectedBen);
				beans.add(mobiSpringBean);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fill mobi spring bean.
	 *
	 * @param <T> the generic type
	 * @param configurationInstance the configuration instance
	 * @param prefix the prefix
	 * @param method the method
	 * @return the mobi spring bean
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private static <T> MobiSpringBean fillMobiSpringBean(final T configurationInstance, final String prefix,
			final Method method) throws IllegalAccessException, InvocationTargetException {
		MobiSpringBean mobiSpringBean;
		mobiSpringBean = new MobiSpringBean();
		mobiSpringBean.setBean(method.invoke(configurationInstance));
		mobiSpringBean.setName(createBeanName(prefix, mobiSpringBean));
		return mobiSpringBean;
	}

	/**
	 * Gets the bean definition.
	 *
	 * @param <CONFIG_CLASS> the generic type
	 * @param configurationInstance the configuration instance
	 * @param method the method
	 * @return the bean definition
	 */
	private <CONFIG_CLASS extends Configuration> MobiBeanDefinition getBeanDefinition(
			final CONFIG_CLASS configurationInstance, final Method method) {
		Class<?> clazz = method.getReturnType();
		MobiBeanDefinition beanDefinition = new MobiBeanDefinition(clazz, configurationInstance);
		beanDefinition.setBeanName(beanName(configurationInstance.getConfigName(), clazz));
		beanDefinition.setFactoryMethod(method);
		getDependencyBeans(configurationInstance.getConfigName(), method.getParameterTypes(), beanDefinition);
		return beanDefinition;
	}

	/**
	 * Gets the dependency beans.
	 *
	 * @param prefix the prefix
	 * @param factoryMethodParams the factory method params
	 * @param beanDefinition the bean definition
	 */
	private void getDependencyBeans(final String prefix, final Class<?>[] factoryMethodParams,
			final MobiBeanDefinition beanDefinition) {
		Iterator<Class<?>> iterator = IteratorUtils.arrayIterator(factoryMethodParams);
		while (iterator.hasNext()) {
			Class<?> clazz = (Class<?>) iterator.next();
			Dependency dependency = new Dependency();
			dependency.setClazz(clazz);
			dependency.setBeanName(beanName(prefix, clazz));
			beanDefinition.addDependency(dependency);
		}

	}

	/**
	 * Bean name.
	 *
	 * @param prefix the prefix
	 * @param clazz the clazz
	 * @return the string
	 */
	private String beanName(final String prefix, final Class<?> clazz) {
		return UString.join(prefix,
			UString.isEmpty(prefix) ? UString.uncapitalize(clazz.getSimpleName()) : clazz.getSimpleName());
	}

	/**
	 * Checks if is valid bean.
	 *
	 * @param toBeInjectedBean the to be injected bean
	 * @return true, if is valid bean
	 */
	private static boolean isValidBean(final Method toBeInjectedBean) {
		return !toBeInjectedBean.isSynthetic() && !Modifier.isPrivate(toBeInjectedBean.getModifiers())
				&& Object.class.isAssignableFrom(toBeInjectedBean.getReturnType());
	}

	/**
	 * Creates the bean name.
	 *
	 * @param prefix the prefix
	 * @param mobiSpringBean the mobi spring bean
	 * @return the string
	 */
	private static String createBeanName(final String prefix, final MobiSpringBean mobiSpringBean) {
		String beanName = mobiSpringBean.getBean().getClass().getSimpleName();
		return UString.isEmpty(prefix) ? UString.uncapitalize(beanName) : UString.join(prefix, beanName);
	}

}
