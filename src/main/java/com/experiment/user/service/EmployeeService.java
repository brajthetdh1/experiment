package com.experiment.user.service;

import com.experiment.user.entity.Employee;
import com.experiment.user.repo.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        logger.info("Getting all employees");
        List<Employee> employees = employeeRepository.findAll();
        logger.debug("Retrieved {} employees", employees.size());
        return employees;
    }

    public Optional<Employee> getEmployeeById(Long id) {
        logger.info("Getting employee by ID: {}", id);
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            logger.debug("Found employee: {}", employee.get().getEmail());
        } else {
            logger.warn("Employee not found with ID: {}", id);
        }
        return employee;
    }

    public Employee createEmployee(Employee employee) {
        logger.info("Creating new employee: {}", employee.getEmail());

        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            logger.error("Employee creation failed - Email already exists: {}", employee.getEmail());
            throw new RuntimeException("Employee with email " + employee.getEmail() + " already exists");
        }

        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Employee created successfully with ID: {}", savedEmployee.getId());
        return savedEmployee;
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        logger.info("Updating employee with ID: {}", id);

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            logger.debug("Found existing employee: {}", employee.getEmail());

            // Check if email is being changed and if it already exists
            if (!employee.getEmail().equals(employeeDetails.getEmail()) &&
                    employeeRepository.findByEmail(employeeDetails.getEmail()).isPresent()) {
                logger.error("Employee update failed - Email already exists: {}", employeeDetails.getEmail());
                throw new RuntimeException("Email " + employeeDetails.getEmail() + " already exists");
            }

            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            employee.setEmail(employeeDetails.getEmail());
            employee.setDepartment(employeeDetails.getDepartment());
            employee.setPosition(employeeDetails.getPosition());
            employee.setSalary(employeeDetails.getSalary());
            employee.setHireDate(employeeDetails.getHireDate());
            employee.setActive(employeeDetails.getActive());

            Employee updatedEmployee = employeeRepository.save(employee);
            logger.info("Employee updated successfully: {}", updatedEmployee.getEmail());
            return updatedEmployee;
        }

        logger.error("Employee update failed - Employee not found with ID: {}", id);
        throw new RuntimeException("Employee not found with id: " + id);
    }

    public void deleteEmployee(Long id) {
        logger.info("Deleting employee with ID: {}", id);

        if (!employeeRepository.existsById(id)) {
            logger.error("Employee deletion failed - Employee not found with ID: {}", id);
            throw new RuntimeException("Employee not found with id: " + id);
        }

        employeeRepository.deleteById(id);
        logger.info("Employee deleted successfully with ID: {}", id);
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        logger.info("Getting employees by department: {}", department);
        List<Employee> employees = employeeRepository.findByDepartment(department);
        logger.debug("Found {} employees in department: {}", employees.size(), department);
        return employees;
    }

    public List<Employee> getActiveEmployees() {
        logger.info("Getting all active employees");
        List<Employee> employees = employeeRepository.findByActiveTrue();
        logger.debug("Found {} active employees", employees.size());
        return employees;
    }

    public List<Employee> getEmployeesBySalaryRange(Double minSalary, Double maxSalary) {
        logger.info("Getting employees by salary range: {} - {}", minSalary, maxSalary);
        List<Employee> employees = employeeRepository.findEmployeesBySalaryRange(minSalary, maxSalary);
        logger.debug("Found {} employees in salary range", employees.size());
        return employees;
    }

    public List<Employee> searchEmployeesByLastName(String lastName) {
        logger.info("Searching employees by last name: {}", lastName);
        List<Employee> employees = employeeRepository.findByLastNameContainingIgnoreCase(lastName);
        logger.debug("Found {} employees with last name containing: {}", employees.size(), lastName);
        return employees;
    }

    public long getEmployeeCountByDepartment(String department) {
        logger.debug("Getting employee count for department: {}", department);
        long count = employeeRepository.countByDepartment(department);
        logger.info("Department {} has {} employees", department, count);
        return count;
    }
}