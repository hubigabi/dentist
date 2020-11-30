package utp.pl.dentist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import utp.pl.dentist.model.Appointment;
import utp.pl.dentist.model.OpenDay;
import utp.pl.dentist.model.OpenHour;
import utp.pl.dentist.repository.AppointmentRepository;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppointmentService {

    private static final int APPOINTMENT_DURATION_HOURS = 1;

    private AppointmentRepository appointmentRepository;
    private MailService mailService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, MailService mailService) {
        this.appointmentRepository = appointmentRepository;
        this.mailService = mailService;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public Appointment getAppointment(long id) {
        return appointmentRepository.findById(id).get();
    }

    public Appointment createAppointment(Appointment appointment) {
        appointment.setLocalDateTime(appointment.getLocalDateTime().withMinute(0).withSecond(0).withNano(0));
        if (appointment.getLocalDateTime().isAfter(LocalDateTime.now())
                && isOpen(appointment.getLocalDateTime())
                && isAvailable(appointment.getLocalDateTime())) {
            appointment = appointmentRepository.save(appointment);

            try {
                mailService.sendMail("hubigabi19@gmail.com", "Dentist - appointment",
                        "Your appointment will be: " + getFormattedString(appointment.getLocalDateTime()),
                        false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return appointment;
        } else {
            return new Appointment();
        }
    }

    public Appointment updateAppointment(Appointment appointment) {
        appointment.setLocalDateTime(appointment.getLocalDateTime().withMinute(0).withSecond(0).withNano(0));
        if (appointment.getLocalDateTime().isAfter(LocalDateTime.now())
                && isOpen(appointment.getLocalDateTime())
                && isAvailable(appointment.getLocalDateTime())) {
            appointment = appointmentRepository.save(appointment);

            try {
                mailService.sendMail("hubigabi19@gmail.com", "Dentist - appointment",
                        "Your appointment will be: " + getFormattedString(appointment.getLocalDateTime()), false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return appointment;
        } else {
            return new Appointment();
        }
    }

    private String getFormattedString(LocalDateTime localDateTime) {
        return String.format("%02d", localDateTime.getHour())
                + ":" + String.format("%02d", localDateTime.getMinute())
                + " " + String.format("%02d", localDateTime.getDayOfMonth())
                + "-" + String.format("%02d", localDateTime.getMonth().getValue())
                + "-" + localDateTime.getYear();
    }

    public List<Appointment> saveAll(Iterable<Appointment> comments) {
        return appointmentRepository.saveAll(comments);
    }

    public List<String> getAllNames() {
        return appointmentRepository.findAll().stream()
                .map(Appointment::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAllEmails() {
        return appointmentRepository.findAll().stream()
                .map(Appointment::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Appointment> getByLocalDate(LocalDate localDate) {
        return appointmentRepository.findAllByLocalDateTimeBetween(
                LocalDateTime.of(localDate, LocalTime.MIDNIGHT),
                LocalDateTime.of(localDate, LocalTime.MAX));
    }

    public boolean isOpen(LocalDateTime localDateTime) {
        List<Integer> openDays = OpenDay.OPEN_DAYS.days;

        return openDays.contains(localDateTime.getDayOfWeek().getValue())
                && localDateTime.getHour() >= OpenHour.OPENING_HOUR.hour
                && localDateTime.getHour() + APPOINTMENT_DURATION_HOURS <= OpenHour.CLOSING_HOUR.hour;
    }

    public boolean isAvailable(LocalDateTime localDateTime) {
        return getByLocalDate(localDateTime.toLocalDate()).stream()
                .mapToInt(appointment -> appointment.getLocalDateTime().getHour())
                .noneMatch(value -> value == localDateTime.getHour());
    }

//    @Scheduled(fixedRate = 1000 * 60)
    @Scheduled(cron = "0 0 8 * * 1-5", zone = "Europe/Warsaw")
    public void sendTodayScheduleMail() throws MessagingException {
        String mail = "hubigabi19@gmail.com";
        String subject = "Dentist - today schedule";
        String text = "Today schedule:\n"
                + getByLocalDate(LocalDate.now()).stream()
                .map(appointment -> String.format("%02d", appointment.getLocalDateTime().getHour())
                        + ":" + String.format("%02d", appointment.getLocalDateTime().getMinute())
                        + " - " + appointment.getName()
                )
                .collect(Collectors.joining("\n"));

        log.info("Today schedule:");
        log.info(text);

        mailService.sendMail(mail, subject, text, false);
    }

}
