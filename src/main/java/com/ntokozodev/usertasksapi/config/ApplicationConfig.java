package com.ntokozodev.usertasksapi.config;

import com.ntokozodev.usertasksapi.model.db.Task;
import com.ntokozodev.usertasksapi.model.task.TaskDTO;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationConfig {
    @Bean
    ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.typeMap(Task.class, TaskDTO.class)
                .addMappings(mapper -> mapper.using(dateTimeStringConverter()).map(Task::getDate_time, TaskDTO::setDate_time));

        return modelMapper;
    }

    private static Converter<LocalDateTime, String> dateTimeStringConverter() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new AbstractConverter<LocalDateTime, String>() {
            protected String convert(LocalDateTime source) {
                return source == null ? null : source.format(formatter);
            }
        };
    }
}
