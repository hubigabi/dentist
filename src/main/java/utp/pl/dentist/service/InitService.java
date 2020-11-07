package utp.pl.dentist.service;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import utp.pl.dentist.model.Appointment;
import utp.pl.dentist.model.OpenDay;
import utp.pl.dentist.model.OpenHour;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class InitService {

    private AppointmentService appointmentService;

    @Autowired
    public InitService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        ArrayList<Appointment> appointments = new ArrayList<>();

        Lorem loremIpsum = LoremIpsum.getInstance();
        final String EMAIL_SUFFIX = "@gmail.com";

        final int APPOINTMENTS_NUMBER = 100;
        final int APPOINTMENT_DURATION_HOURS = 1;

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime appointmentDateTime = currentDateTime.minusDays(5).withHour(OpenHour.OPENING_HOUR.hour).withMinute(0).withSecond(0).withNano(0);
        appointmentDateTime = changeDateToOpen(appointmentDateTime);

        for (int i = 0; i < APPOINTMENTS_NUMBER; i++) {
            String name = loremIpsum.getName();
            String email = name.replaceAll("\\s+", "") + EMAIL_SUFFIX;

            appointmentDateTime = appointmentDateTime.plusHours(1);
            if (appointmentDateTime.getHour() + APPOINTMENT_DURATION_HOURS > OpenHour.CLOSING_HOUR.hour) {
                appointmentDateTime = appointmentDateTime.plusDays(1).withHour(OpenHour.OPENING_HOUR.hour);
                appointmentDateTime = changeDateToOpen(appointmentDateTime);
            }

            Appointment appointment = new Appointment(0L, name, email, appointmentDateTime);
            appointments.add(appointment);
        }
        appointmentService.saveAll(appointments);

        System.out.println(appointmentService.getAllAppointments());
    }

    public LocalDateTime changeDateToOpen(LocalDateTime localDateTime) {
        List<Integer> openDays = OpenDay.OPEN_DAYS.days;

        while (!openDays.contains(localDateTime.getDayOfWeek().getValue())) {
            localDateTime = localDateTime.plusDays(1);
        }
        return localDateTime;
    }
}

