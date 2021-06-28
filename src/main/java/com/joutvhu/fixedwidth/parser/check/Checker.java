package com.joutvhu.fixedwidth.parser.check;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class Checker {

    public Result check(Class<?> model) {
        return Result
                .builder()
                .entries(inspect(model))
                .build();
    }

    private List<Entry> inspect(Class<?> model) {
        return getInspectors().flatMap(m -> m.inspect(model).stream()).collect(toList());
    }

    private Stream<ModelInspector> getInspectors() {
        return StreamSupport.stream(ServiceLoader.load(ModelInspector.class).spliterator(), false);
    }

    @Data
    @Builder
    public static class Result {
        private List<Entry> entries;
    }

    @Data
    @Builder
    public static class Entry {
        private Severity severity;
        private String message;
    }

    public enum Severity {
        ERROR,
        WARNING,
        ;
    }
}
