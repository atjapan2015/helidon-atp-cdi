/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.atp.mybatis;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import io.helidon.atp.mybatis.common.config.AppConfig;
import io.helidon.atp.mybatis.entity.Employee;
import io.helidon.atp.mybatis.facade.EmployeeFacade;

/**
 * A simple JAX-RS resource to greet you. Examples:
 *
 * Get default greeting message: curl -X GET http://localhost:8080/greet
 *
 * Get greeting message for Joe: curl -X GET http://localhost:8080/greet/Joe
 *
 * Change greeting curl -X PUT -H "Content-Type: application/json" -d
 * '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 *
 * The message is returned as a JSON object.
 */
@Path("/atp")
@RequestScoped
public class AtpResource {

	private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

	@Inject
	transient Logger logger;

	/**
	 * The greeting message provider.
	 */
	private final AtpProvider greetingProvider;

	@Inject
	EmployeeFacade employeeFacade;

	/**
	 * Using constructor injection to get a configuration property. By default this
	 * gets the value from META-INF/microprofile-config
	 *
	 * @param greetingConfig the configured greeting message
	 */
	@Inject
	public AtpResource(AtpProvider greetingConfig, @Named("codetokyo") DataSource ds) {
		this.greetingProvider = greetingConfig;
		AppConfig.ds = ds;
	}

	/**
	 * Return a wordly greeting message.
	 *
	 * @return {@link JsonObject}
	 */
	@SuppressWarnings("checkstyle:designforextension")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject getDefaultMessage() {
		return createResponse("World");
	}

	/**
	 * Return a greeting message using the name that was provided.
	 *
	 * @param name the name to greet
	 * @return {@link JsonObject}
	 */
	@SuppressWarnings("checkstyle:designforextension")
	@Path("/{name}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject getMessage(@PathParam("name") String name) {
		return createResponse(name);
	}

	/**
	 * Set the greeting to use in future messages.
	 *
	 * @param jsonObject JSON containing the new greeting
	 * @return {@link Response}
	 */
	@SuppressWarnings("checkstyle:designforextension")
	@Path("/greeting")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateGreeting(JsonObject jsonObject) {

		if (!jsonObject.containsKey("greeting")) {
			JsonObject entity = JSON.createObjectBuilder().add("error", "No greeting provided").build();
			return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
		}

		String newGreeting = jsonObject.getString("greeting");

		greetingProvider.setMessage(newGreeting);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@Path("/employees")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> listEmployees() throws SQLException {

		List<Employee> employees = employeeFacade.selectEmployeeByExample();
		employees.forEach(v -> {
			logger.info(v.getEmployeeId() + ":" + v.getLastName() + " " + v.getFirstName());
		});

		return employees;
	}

	@Path("/testinsert")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public void insertEmployeeAndManager() throws Exception {

		employeeFacade.insertEmployeeAndManager();
	}

	private JsonObject createResponse(String who) {
		String msg = String.format("%s %s!", greetingProvider.getMessage(), who);

		return JSON.createObjectBuilder().add("message", msg).build();
	}
}
