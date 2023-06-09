package com.will.fapl.auth.aop;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@SecurityRequirement(name = "bearer")
public @interface LoginRequired {

}
