package io.helidon.atp.mybatis.facade.impl;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.mybatis.cdi.Transactional;

import io.helidon.atp.mybatis.common.exception.DBOperationException;
import io.helidon.atp.mybatis.entity.Employee;
import io.helidon.atp.mybatis.entity.EmployeeExample;
import io.helidon.atp.mybatis.entity.Manager;
import io.helidon.atp.mybatis.facade.EmployeeFacade;
import io.helidon.atp.mybatis.service.EmployeeService;
import io.helidon.atp.mybatis.service.ManagerService;

@RequestScoped
@Transactional
public class EmployeeFacadeImpl implements EmployeeFacade {

	@Inject
	EmployeeService employeeService;

	@Inject
	ManagerService managerService;

	@Override
	public List<Employee> selectEmployeeByExample() {

		List<Employee> employees = null;
		try {

			employees = employeeService.selectByExample(new EmployeeExample());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return employees;
	}

	@Override
	public void insertEmployeeAndManager() throws DBOperationException {

		List<Employee> employees = null;
		try {

			employees = employeeService.selectByExample(new EmployeeExample());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Employee employee = employees.get(0);
		String newEmployeeId = String.valueOf(Integer.valueOf(employee.getEmployeeId()).intValue() + 1);
		employee.setEmployeeId(newEmployeeId);

		employeeService.insert(employee);

		if ("103".contentEquals(newEmployeeId)) {
			throw new DBOperationException("103 is not allowed");
		}

		Manager manager = new Manager();
		manager.setEmployeeId(newEmployeeId);
		managerService.insert(manager);

	}
}
