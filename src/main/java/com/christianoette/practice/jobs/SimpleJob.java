package com.christianoette.practice.jobs;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited()
@Qualifier("SimpleJob")
public @interface SimpleJob {
}
