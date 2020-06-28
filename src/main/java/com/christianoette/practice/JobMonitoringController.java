package com.christianoette.practice;

import com.christianoette.dontchangeit.dto.JobExecutionsDto;
import com.christianoette.dontchangeit.dto.JsonWrapper;
import com.christianoette.practice.jobs.SimpleJob;
import com.christianoette.practice.services.TriggerJobService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "job-monitoring")
public class JobMonitoringController {

    private final JobExplorer jobExplorer;
    private final TriggerJobService triggerJobService;
    private final List<Job> jobs;
    private final JobOperator jobOperator;
    private final Job simpleJob;
    private final JobRegistry jobRegistry;
    private final JobRepository jobRepository;

    public JobMonitoringController(JobRepository jobRepository,
                                   JobExplorer jobExplorer,
                                   TriggerJobService triggerJobService,
                                   List<Job> jobs, JobOperator jobOperator,
                                   @SimpleJob Job simpleJob,
                                   JobRegistry jobRegistry) {
        this.jobExplorer = jobExplorer;
        this.triggerJobService = triggerJobService;
        this.jobs = jobs;
        this.jobOperator = jobOperator;
        this.simpleJob = simpleJob;
        this.jobRegistry = jobRegistry;
        this.jobRepository = jobRepository;
    }

    @GetMapping("/job-definitions")
    public JsonWrapper jobDefinitions() {
        return new JsonWrapper(jobs.size());
    }

    @GetMapping("/job-names")
    public JsonWrapper getJobNames() {
        Collection<String> jobNames = jobRegistry.getJobNames();
        return new JsonWrapper(jobNames);
    }

    @PostMapping("/job/start-new")
    public void runJob(@RequestParam(value = "jobName") String jobName) throws NoSuchJobException {
        Job job = jobRegistry.getJob(jobName);
        triggerJobService.triggerJob(job);
    }

    @GetMapping("/job/{jobName}/instances")
    public JsonWrapper getJobInstances(@PathVariable String jobName) {
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, 100);
        return new JsonWrapper(jobInstances);
    }

    @GetMapping("/job/instance/{instanceId}")
    public JsonWrapper getJobExecutions(@PathVariable Long instanceId) {
        JobInstance jobInstance = jobExplorer.getJobInstance(instanceId);
        List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
        return new JsonWrapper(new JobExecutionsDto(jobExecutions));
    }

    @PostMapping("/execution/{executionId}/stop")
    public void stopExecution(@PathVariable Long executionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        jobOperator.stop(executionId);
    }

    @PostMapping("/execution/{executionId}/restart")
    public void restartExecution(@PathVariable Long executionId) throws JobParametersInvalidException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException {
        jobOperator.restart(executionId);
    }

    @PostMapping("/simple-job/start/{parameter}")
    public void startSimpleJobWithParameter(@PathVariable String parameter) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("parameter", parameter)
                .toJobParameters();
        triggerJobService.triggerJob(simpleJob, jobParameters);
    }
}
