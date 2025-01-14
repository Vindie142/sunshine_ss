package com.vindie.sunshine_ss.common.timers.queue;

import com.vindie.sunshine_ss.common.service.properties.PropertiesService;
import com.vindie.sunshine_ss.queue.dto.EventLine;
import com.vindie.sunshine_ss.queue.repo.EventLineRepo;
import com.vindie.sunshine_ss.queue.repo.QueueElementRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.TRUE;

@Service
@AllArgsConstructor
@Slf4j
public class QueueCleanerTimer {

    public static final int INTERVAL_HOURS = 5;

    private QueueElementRepo queueElementRepo;
    private EventLineRepo eventLineRepo;
    private PropertiesService properties;

    @Transactional
    @Scheduled(fixedRate = INTERVAL_HOURS, timeUnit = TimeUnit.HOURS)
    public void timer() {
        log.info("Start QueueCleanerTimer");
        LocalDate older = LocalDateTime.now().minus(properties.ttl.queueTTL).toLocalDate();
        queueElementRepo.deleteOlder(older);

        List<EventLine> eventLines = eventLineRepo.findAll()
                .stream()
                .filter(ev -> TRUE.equals(ev.getProcessed()))
                .filter(ev -> queueElementRepo.findFirstByEventLineId(ev.getId()).isEmpty())
                .toList();
        eventLineRepo.deleteAll(eventLines);
        log.info("End   QueueCleanerTimer");
    }
}
