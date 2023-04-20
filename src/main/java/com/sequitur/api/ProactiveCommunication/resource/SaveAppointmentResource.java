package com.sequitur.api.ProactiveCommunication.resource;


import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class SaveAppointmentResource {

    private String appointmentDate;

    private String appointmentTime;

    private String appointmentLocation;

    private String reason;

}
