package com.mobiground.configuration.cofigexample;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import com.mobiground.ecap.configuration.Configuration;
import com.mobiground.ecap.manager.db.hibernate.HibernateDatabaseManagerImpl;

public class ModelConfigurationLegacy extends Configuration {

	private String driverClass;
	private String urlOne;
	private String pass;
	private String username;

	public DataSourceTransactionManager getDataSourceTransactionManagerOne(
			DriverManagerDataSource driverManagerDataSource) {
		return new DataSourceTransactionManager(driverManagerDataSource);
	}

	public LocalSessionFactoryBean getSessionFactoryOne(DriverManagerDataSource driverManagerDataSource) {
		LocalSessionFactoryBean sessionFactoryOne = new LocalSessionFactoryBean();
		sessionFactoryOne.setHibernateProperties(hibernateProperties());
		sessionFactoryOne.setConfigLocations(classPathResources());
		sessionFactoryOne.setDataSource(driverManagerDataSource);
		return sessionFactoryOne;
	}

	private ClassPathResource[] classPathResources() {
		ClassPathResource[] classPathResources = {
				new ClassPathResource("/config/mapping/CategoryRepository-mapping.xml"),
				new ClassPathResource("/config/mapping/ContentRepository-mapping.xml"),
				new ClassPathResource("/config/mapping/CategoryContentNumRepository-mapping.xml"),
				new ClassPathResource("/config/mapping/NoContentPortalRepository-mapping.xml"),
				new ClassPathResource("/config/mapping/TerminalRepository-mapping.xml"),
				new ClassPathResource("/config/mapping/TSiteRepository-mapping.xml"),
				new ClassPathResource("/config/mapping/TTolsTaskCahceRepository-mapping.xml"),
				new ClassPathResource("/config/mapping/por-mapping.xml"),
				new ClassPathResource("/config/mapping/VficherosPublicadosCategoriaAgrupadoRepository-mapping.xml"), };
		return classPathResources;
	}

	private String[] claspathResource() {
		//@formatter:off
		return new String[]{"/config/mapping/CategoryRepository-mapping.xml",
							"/config/mapping/ContentRepository-mapping.xml",
							"/config/mapping/CategoryContentNumRepository.hbm.xml",
							"/config/mapping/NoContentPortalRepository.hbm.xml",
							"/config/mapping/TerminalRepository.hbm.xml",
							"/config/mapping/TSiteRepository.hbm.xml",
							"/config/mapping/TTolsTaskCacheRepository.hbm.xml",
							"/config/mapping/VficherosPublicadosCategoriaAgrupadoRepository.hbm.xml"
							}; 
		//@formatter:on
	}

	public DriverManagerDataSource getDriverManagerDataSourceOne() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(urlOne);
		dataSource.setPassword(pass);
		dataSource.setUsername(username);
		setCommonProperties(dataSource);
		return dataSource;
	}

	public HibernateDatabaseManagerImpl getDataBaseManager(SessionFactory sessionFactory) {
		HibernateDatabaseManagerImpl databaseManager = new HibernateDatabaseManagerImpl();
		databaseManager.setSessionFactory(sessionFactory);
		return databaseManager;
	}

	private void setCommonProperties(DriverManagerDataSource dataSource) {
		dataSource.setDriverClassName(driverClass);
	}

	private Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
		hibernateProperties.setProperty("hibernate.show_sql", "true");
		hibernateProperties.setProperty("hibernate.format_sql", "true");
		hibernateProperties.setProperty("hibernate.use_sql_comments", "true");
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
		return hibernateProperties;
	}

	/**
	 * @param driverClass the driverClass to set
	 */
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	/**
	 * @param urlOne the urlOne to set
	 */
	public void setUrlOne(String urlOne) {
		this.urlOne = urlOne;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
