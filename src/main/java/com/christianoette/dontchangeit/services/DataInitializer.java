package com.christianoette.dontchangeit.services;

import com.christianoette.practice.jobs.SimpleJob;
import com.christianoette.practice.services.TriggerJobService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private static final Logger LOGGER = LogManager.getLogger(DataInitializer.class);

    private final TriggerJobService triggerJobService;
    private final Job simpleJob;

    public DataInitializer(TriggerJobService triggerJobService, @SimpleJob Job simpleJob) {
        this.triggerJobService = triggerJobService;
        this.simpleJob = simpleJob;
    }

    @EventListener(value = ApplicationReadyEvent.class)
    public void initializeJobs() {
        LOGGER.info("Run jobs to add initial data to in memory database");
        triggerJobService.triggerJob(simpleJob);
        triggerJobService.triggerJob(simpleJob);
    }
}
