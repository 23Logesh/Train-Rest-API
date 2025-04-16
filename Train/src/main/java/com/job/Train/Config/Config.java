package com.job.Train.Config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    ModelMapper modelMapper;

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
