package com.christianoette.practice.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndlessJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public EndlessJobConfiguration(JobBuilderFactory jobBuilderFactory,
                                   StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    @EndlessJob
    public Job endlessJob() {
        return jobBuilderFactory.get("endlessJob")
                .start(endlessStep())
                .build();
    }

    @Bean
    public Step endlessStep() {
        return stepBuilderFactory.get("jsonItemReader")
                .tasklet((contribution, chunkContext) -> {
                    ExecutionContext executionContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();
                    int count = executionContext.getInt("contextInfo", 0);
                    executionContext.putInt("contextInfo", count);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return RepeatStatus.CONTINUABLE;
                })
                .build();
    }

}
