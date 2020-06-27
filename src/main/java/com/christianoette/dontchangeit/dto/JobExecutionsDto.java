package com.christianoette.dontchangeit.dto;

import org.springframework.batch.core.JobExecution;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JobExecutionsDto {
    @SuppressWarnings("WeakerAccess")
    public List<ExecutionDto> executions;

    public JobExecutionsDto(Collection<JobExecution> jobExecutions) {
        executions = jobExecutions.stream().
                map(ExecutionDto::new)
                .collect(Collectors.toList());
    }
}
