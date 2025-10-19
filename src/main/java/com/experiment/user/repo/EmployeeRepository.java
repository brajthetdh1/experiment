package com.experiment.user.repo;

import com.experiment.user.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByDepartment(String department);
    List<Employee> findByActiveTrue();
    List<Employee> findBySalaryGreaterThan(Double salary);
    List<Employee> findByLastNameContainingIgnoreCase(String lastName);

    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :minSalary AND :maxSalary")
    List<Employee> findEmployeesBySalaryRange(Double minSalary, Double maxSalary);

    long countByDepartment(String department);
}