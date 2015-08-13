package com.yzqc.support.utils;

import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.CustomSearchFilter;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/10.
 * 查询条件工具类
 */
public class SpecificationBuilder {

    /**
     * 组装查询条件
     *
     * @param queryMap 查询参数集合
     * @param type     实体类型
     * @param <T>      实体类型
     * @return Specification
     */
    public static <T extends Serializable> Specification<T> buildSpecification(Map<String, Object> queryMap, Class<T> type) {
        Map<String, CustomSearchFilter> filters = CustomSearchFilter.parse(queryMap);
        Specification<T> specification = DynamicSpecifications.bySearchFilter(filters.values(), type);
        return specification;
    }
}
