package sg.edu.iss.leave.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.iss.leave.model.Holiday;
@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

	Holiday findByHolidayDate(LocalDate date);

}
