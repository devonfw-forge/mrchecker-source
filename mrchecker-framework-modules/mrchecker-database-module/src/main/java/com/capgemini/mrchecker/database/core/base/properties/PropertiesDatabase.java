package com.capgemini.mrchecker.database.core.base.properties;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PropertiesDatabase {

	private final String dbDrivers  = "./lib/dbdrivers";
	private String jdbcOracle = dbDrivers + "/oracle/ojdbc8.jar";
	private String proxy      = "";

	@Inject(optional = true)
	private void setJdbcOracle(@Named("database.oracle") String path) {
		jdbcOracle = path;
	}

	public String getJdbcOracle() {
		return jdbcOracle;
	}

	@Inject(optional = true)
	private void setProxy(@Named("database.proxy") String path) {
		proxy = path;
	}
	
	public String getProxy() {
		return proxy;
	}
}