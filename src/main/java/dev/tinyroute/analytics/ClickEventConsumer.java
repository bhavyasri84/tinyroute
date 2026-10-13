package dev.tinyroute.analytics;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
public class ClickEventConsumer {
    private final ClickEventRepository repository;
    public ClickEventConsumer(ClickEventRepository repository){this.repository=repository;}
    @KafkaListener(topics="tinyroute.click-events",groupId="tinyroute-analytics")
    @Transactional
    public void consume(ClickEventMessage event){
        if(event==null||event.eventId()==null||event.shortCode()==null)return;
        if(repository.existsByEventId(event.eventId()))return;
        try{repository.save(new ClickEventEntity(event));}
        catch(DataIntegrityViolationException duplicate){ /* duplicate event delivery is harmless */ }
    }
}
