package com.myspace.employeerating;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.*;

public class MyApp {

	public static void main(String[] args) {

		Employee e = new Employee();
		e.setYearsOfExperience(5);
		e.setRating("good");
		
		
		String url = "http://localhost:8089/kie-server-7.11.0.Final-ee7/services/rest/server";
		String username = "kieserver";
		String password = "kieserver";
		String container = "empHike";
		String session = "defaultKieSession";

		KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(url, username, password);
		Set<Class<?>> allClasses = new HashSet<Class<?>>();
		allClasses.add(Employee.class);
		
		config.addJaxbClasses(allClasses);
	
		
		KieServicesClient client  = KieServicesFactory.newKieServicesClient(config);
		RuleServicesClient ruleClient = client.getServicesClient(RuleServicesClient.class);
		List<Command<?>> commands = new ArrayList<Command<?>>();
		commands.add((Command<?>) KieServices.Factory.get().getCommands().newInsert(e,"Employee Identifier"));
		commands.add((Command<?>) KieServices.Factory.get().getCommands().newFireAllRules("fire-identifier"));
		BatchExecutionCommand batchCommand = KieServices.Factory.get().getCommands().newBatchExecution(commands,session);
		ServiceResponse<String> response = ruleClient.executeCommands(container, batchCommand);
		System.out.println(response.getResult());
	
	
	
	}

}
