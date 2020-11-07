package utp.pl.dentist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import utp.pl.dentist.model.Appointment;
import utp.pl.dentist.service.AppointmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/appointment")
public class AppointmentController {

    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/all")
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointment getAppointment(@PathVariable("id") long id) {
        return appointmentService.getAppointment(id);
    }

    @PostMapping()
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.createAppointment(appointment);
    }

    @PutMapping()
    public Appointment updateAppointment(@RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(appointment);
    }

    @GetMapping("/allNames")
    public List<String> getAllNames() {
        return appointmentService.getAllNames();
    }

    @GetMapping("/allEmails")
    public List<String> getAllEmails() {
        return appointmentService.getAllEmails();
    }

    @GetMapping("/allByLocalDate/{localDate}")
    public List<Appointment> getByLocalDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                    LocalDate localDate) {
        return appointmentService.getByLocalDate(localDate);
    }

    @GetMapping("/isOpen/{localDateTime}")
    public boolean isOpen(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                  LocalDateTime localDateTime) {
        return appointmentService.isOpen(localDateTime);
    }

    @GetMapping("/isAvailable/{localDateTime}")
    public boolean isAvailable(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                       LocalDateTime localDateTime) {
        return appointmentService.isAvailable(localDateTime);
    }

}
