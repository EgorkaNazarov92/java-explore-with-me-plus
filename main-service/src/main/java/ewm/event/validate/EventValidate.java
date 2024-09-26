package ewm.event.validate;

import ewm.error.exception.ValidationException;
import ewm.event.dto.CreateEventDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ewm.event.dto.UpdateEventDto;
import org.slf4j.Logger;

public class EventValidate {
    public static void EventDateValidate(CreateEventDto dto, Logger log){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dto.getEventDate(), formatter);
        if(dateTime.isBefore(LocalDateTime.now().plusHours(2))){
            String messageError = "Событие должно начинаться не раньше чем через 2 часа.";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
    }

    public static void UpdateEventDateValidate(UpdateEventDto dto, Logger log){
        if(dto.getEventDate() != null){
            if(dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))){
                String messageError = "Событие должно начинаться не раньше чем через 2 часа.";
                log.error(messageError);
                throw new ValidationException(messageError);
            }
        }
    }
}
