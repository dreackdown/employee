package dev.hugofaria.employee.repository;

import dev.hugofaria.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Modifying
    @Query("UPDATE Employee e SET e.enabled = false WHERE e.employeeId =:id")
    void disableEmployee(@Param("id") Long id);

    //%AND%
    // Fernanda
    // Alessandra
    @Query("SELECT e FROM Employee e WHERE e.firstName LIKE LOWER(CONCAT ('%',:firstName,'%'))")
    Page<Employee> findEmployeeByName(@Param("firstName") String firstName, Pageable pageable);
}