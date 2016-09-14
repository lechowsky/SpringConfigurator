package com.mobiground.configuration.factory;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.mobiground.configuration.factory.bean.Dependency;
import com.mobiground.configuration.factory.bean.MobiBeanDefinition;
import com.mobiground.ecap.configuration.Configuration;

/**
 * The Interface BeanInjector.
 */
public interface BeanInjector {

	/**
	 * Inject beans.
	 *
	 * @param <CONFIG_CLASS> the generic type
	 * @param context the context
	 * @param configurationInstance the configuration instance
	 */
	@Deprecated
	<CONFIG_CLASS extends Configuration> void injectBeans(ConfigurableApplicationContext context,
			CONFIG_CLASS configurationInstance);

	/**
	 * Gets the bean definition, this method iterate over configuration instance, and gets all method that comply <br>
	 * the Spring '@Bean' bean declaration, also gets all method parameter and adds to List of {@link Dependency} of
	 * <br>
	 * beanDefinition, sets the factoryMethod which will be responsible of instantiate the corresponding bean in <br>
	 * postProcessFactory Example: Given a method like this:
	 * 
	 * <pre>
	 * 		Public Object getObject(String text){
	 * 			return new Object(text);
	 * 		}
	 * </pre>
	 *
	 * @param <CONFIG_CLASS> the generic type
	 * @param configurationInstance the configuration instance
	 * @return the bean definition
	 */
	<CONFIG_CLASS extends Configuration> List<MobiBeanDefinition> getBeanDefinition(CONFIG_CLASS configurationInstance);

	/**
	 * Adds the dependencies taking out them from context, and subsequently, add them to cdiBeans beanDefinition field.
	 * If configuration instance has prefix, this method will pick up all beans of parameter factory method type and it
	 * chooses the appropriate bean, In case of prefix has not value, but the context has more than matched class, the
	 * method would choose non prefixed bean.
	 *
	 * @param beanDefinition the bean definition
	 * @param context the context
	 * @return the mobi bean definition
	 */
	MobiBeanDefinition addDependencies(MobiBeanDefinition beanDefinition, ApplicationContext context);
}
