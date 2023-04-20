package com.sequitur.api.ProactiveCommunication.resource;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class AppointmentResource {

    private Long id;

    private String appointmentDate;

    private String appointmentTime;

    private String appointmentLocation;

    private String reason;

    private boolean accepted;
}
