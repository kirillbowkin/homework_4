package com.sample.hotel.listener;

import com.sample.hotel.entity.Booking;
import com.sample.hotel.entity.BookingStatus;
import com.sample.hotel.entity.RoomReservation;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.core.event.EntitySavingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class BookingEventListener {

    @Autowired
    private DataManager dataManager;

    @EventListener
    public void onBookingSaving(EntitySavingEvent<Booking> event) {
        Booking savingBooking = event.getEntity();
        LocalDate arrivalDate = savingBooking.getArrivalDate();
        Integer nightsOfStay = savingBooking.getNightsOfStay();
        savingBooking.setDepartureDate(arrivalDate.plus(nightsOfStay, ChronoUnit.DAYS));
    }

    @EventListener
    public void onBookingChangedBeforeCommit(EntityChangedEvent<Booking> event) {
        if (event.getChanges().isChanged("status")) {
            Id<Booking> entityId = event.getEntityId();
            Booking booking = dataManager.load(entityId).optional().orElse(null);

            if(booking.getStatus().equals(BookingStatus.CANCELLED)) {
                RoomReservation roomReservation = booking.getRoomReservation();
                dataManager.remove(roomReservation);
            }
        }

    }
}