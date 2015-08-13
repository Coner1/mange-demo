package org.springside.modules.persistence;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

public class CustomSearchFilter {

    public enum Operator {
        EQ, LIKE, GT, LT, GTE, LTE, IN
    }

    public String fieldName;
    public Object value;
    public Operator operator;

    public CustomSearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public static Map<String, CustomSearchFilter> parse(Map<String, Object> searchParams) {
        Map<String, CustomSearchFilter> filters = Maps.newHashMap();

        for (Entry<String, Object> entry : searchParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null || StringUtils.isBlank(value.toString())) {
                continue;
            }

            String[] names = StringUtils.split(key, "_");
            if (names.length != 2) {
                throw new IllegalArgumentException(key + " is not a valid search filter name");
            }
            String filedName = names[1];
            Operator operator = Operator.valueOf(names[0]);

            CustomSearchFilter filter = new CustomSearchFilter(filedName, operator, value);
            filters.put(key, filter);
        }

        return filters;
    }
}
