package com.hp.proserv.mo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringServiceTest {
	
	private String springVersion;

	public String getSpringVersion() {
		return springVersion;
	}

	public void setSpringVersion(String springVersion) {
		this.springVersion = springVersion;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext context;
		try{
			context = new ClassPathXmlApplicationContext("context.xml");
			SpringServiceTest sstest = (SpringServiceTest) context.getBean("springServiceTest");
			System.out.println("Hello "+System.getProperty("user.name"));
			System.out.println("We are using spring "+sstest.getSpringVersion()+" for this project");			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
