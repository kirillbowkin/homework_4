package com.sample.hotel.listener;

import com.sample.hotel.entity.Booking;
import io.jmix.core.event.EntitySavingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class BookingEventListener {

    @EventListener
    public void onBookingSaving(EntitySavingEvent<Booking> event) {
        Booking savingBooking = event.getEntity();
        LocalDate arrivalDate = savingBooking.getArrivalDate();
        Integer nightsOfStay = savingBooking.getNightsOfStay();
        savingBooking.setDepartureDate(arrivalDate.plus(nightsOfStay, ChronoUnit.DAYS));
    }
}