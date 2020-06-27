package com.christianoette.practice.jobs;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class CommonJobConfiguration {

    private final JobRepository jobRepository;
    private final JobRegistry jobRegistry;
    private final JobExplorer jobExplorer;

    public CommonJobConfiguration(JobRepository jobRepository,
                                  JobRegistry jobRegistry,
                                  JobExplorer jobExplorer) {
        this.jobRepository = jobRepository;
        this.jobRegistry = jobRegistry;
        this.jobExplorer = jobExplorer;
    }

    @Bean
    public JobLauncher asyncJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public JobOperator jobOperator() throws Exception {
        SimpleJobOperator simpleJobOperator = new SimpleJobOperator();
        simpleJobOperator.setJobRepository(jobRepository);
        simpleJobOperator.setJobLauncher(asyncJobLauncher());
        simpleJobOperator.setJobRegistry(jobRegistry);
        simpleJobOperator.setJobExplorer(jobExplorer);
        return simpleJobOperator;
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }
}
