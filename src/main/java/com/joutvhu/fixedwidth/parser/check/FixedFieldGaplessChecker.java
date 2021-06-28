package com.joutvhu.fixedwidth.parser.check;

import com.joutvhu.fixedwidth.parser.annotation.FixedField;
import com.joutvhu.fixedwidth.parser.support.FinalTypeFinder;
import com.joutvhu.fixedwidth.parser.util.CommonUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toMap;

public class FixedFieldGaplessChecker implements ModelInspector, FinalTypeFinder {

    @Override
    public List<Checker.Entry> inspect(Class<?> model) {
        List<Checker.Entry> entries = new ArrayList<>();
        Map<FixedField, String> fieldsByName = collectFixedFields(model);
        List<FixedField> fields = sortByStartIndex(fieldsByName.keySet());

        if (!fields.isEmpty()) {
            FixedField first = fields.get(0);
            if (first.start() > 0) {
                entries.add(createWarningForFirstEntry(model, fieldsByName.get(first), first));
            }
        }

        for (int index = 0; index < fields.size() - 1; index++) {
            FixedField current = fields.get(index);
            FixedField next = fields.get(index + 1);
            if (!isGapless(current, next)) {
                entries.add(warning(model, fieldsByName.get(current), current, fieldsByName.get(next), next));
            }
        }

        return entries;
    }

    private Map<FixedField, String> collectFixedFields(Class<?> model) {
        return getFixedFields(model).stream().collect(toMap(f -> f.getAnnotation(FixedField.class), Field::getName));
    }

    private List<FixedField> sortByStartIndex(Collection<FixedField> fields) {
        List<FixedField> sorted = new ArrayList<>(fields);
        sorted.sort(Comparator.comparing(FixedField::start));
        return sorted;
    }

    private Checker.Entry createWarningForFirstEntry(Class<?> model, String name, FixedField field) {
        String message = "Model {modelName}. A gap exists between position zero and the first field '{fieldName}' starting at position {fieldStart}";

        Map<String, Supplier<String>> arguments = new HashMap<>();
        arguments.put("{modelName}", model::getName);
        arguments.put("{fieldName}", () -> name);
        arguments.put("{fieldStart}", () -> String.valueOf(field.start()));

        return Checker.Entry.builder()
                .severity(Checker.Severity.WARNING)
                .message(CommonUtil.formatMessage(message, arguments))
                .build();
    }

    private boolean isGapless(FixedField first, FixedField second) {
        return first.start() + first.length() == second.start();
    }

    private Checker.Entry warning(Class<?> model, String firstName, FixedField first, String secondName, FixedField second) {
        String message = "Model: {modelName}.  A gap exists between " +
                "'{firstFieldName}' ({firstFieldStart} - {firstFieldEnd}) and " +
                "'{secondFieldName}' ({secondFieldStart} - {secondFieldEnd})";

        Map<String, Supplier<String>> arguments = new HashMap<>();
        arguments.put("{modelName}", model::getName);

        arguments.put("{firstFieldName}", () -> firstName);
        arguments.put("{firstFieldStart}", () -> String.valueOf(first.start()));
        arguments.put("{firstFieldEnd}", () -> String.valueOf(first.start() + first.length()));

        arguments.put("{secondFieldName}", () -> secondName);
        arguments.put("{secondFieldStart}", () -> String.valueOf(second.start()));
        arguments.put("{secondFieldEnd}", () -> String.valueOf(second.start() + second.length()));

        return Checker.Entry
                .builder()
                .severity(Checker.Severity.WARNING)
                .message(CommonUtil.formatMessage(message, arguments))
                .build();
    }
}
