package utp.pl.dentist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utp.pl.dentist.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByLocalDateTimeBetween(LocalDateTime from, LocalDateTime to);
}