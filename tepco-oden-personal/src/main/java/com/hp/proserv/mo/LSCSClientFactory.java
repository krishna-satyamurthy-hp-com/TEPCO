package com.hp.proserv.mo;

import java.util.Properties;

import com.interwoven.wcm.lscs.Client;
import com.interwoven.wcm.lscs.Factory;
import com.interwoven.wcm.lscs.LSCSException;

public class LSCSClientFactory {

	private Properties lscsProperties;
	private Factory lscsFactory;

	public Properties getLscsConnectionProperties() {
		return this.lscsProperties;
	}

	public void setLscsConnectionProperties(Properties paramProperties) {
		this.lscsProperties = paramProperties;
	}

	public Factory getLscsFactory() {
		return this.lscsFactory;
	}

	public void setLscsFactory(Factory paramFactory) {
		this.lscsFactory = paramFactory;
	}

	public Client getLSCSClient() {
		return this.lscsFactory.getClient(this.lscsProperties);
	}
	
}
