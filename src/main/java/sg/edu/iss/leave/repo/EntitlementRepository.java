package sg.edu.iss.leave.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.iss.leave.model.Entitlement;

@Repository
public interface EntitlementRepository extends JpaRepository<Entitlement, Integer> {

}
