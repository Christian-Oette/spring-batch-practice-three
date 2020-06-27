package com.christianoette.dontchangeit.dto;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;

import java.util.Date;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ExecutionDto {

    public final BatchStatus status;
    public final JobParameters jobParameters;
    public final Date createdTime;
    public final Date endTime;
    public final ExitStatus exitStatus;
    public final List<Throwable> failureExceptions;
    public final Long jobId;
    public final Date startTime;
    public final Date lastUpdated;
    public final Long id;
    public final Integer version;
    public final boolean isRunning;
    public final boolean isStopping;
    public final String contextInfo;

    public ExecutionDto(JobExecution jobExecution) {
        this.createdTime = jobExecution.getCreateTime();
        this.startTime = jobExecution.getStartTime();
        this.lastUpdated = jobExecution.getLastUpdated();
        this.status = jobExecution.getStatus();
        this.id = jobExecution.getId();
        this.jobParameters = jobExecution.getJobParameters();
        this.endTime = jobExecution.getEndTime();
        this.exitStatus = jobExecution.getExitStatus();
        this.failureExceptions = jobExecution.getFailureExceptions();
        this.jobId = jobExecution.getJobId();
        this.version = jobExecution.getVersion();
        this.isRunning = jobExecution.isRunning();
        this.isStopping = jobExecution.isStopping();
        this.contextInfo = String.valueOf(jobExecution
               .getExecutionContext()
                .get("contextInfo"));
    }
}
