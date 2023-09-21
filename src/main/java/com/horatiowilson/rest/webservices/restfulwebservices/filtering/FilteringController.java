package com.horatiowilson.rest.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public SomeBean filtering() {
        return new SomeBean("value1", "value2", "value3", "value4");
    }

    @GetMapping("/filtering-dynamic")
    public MappingJacksonValue filteringDynamic() {
        SomeBean someBean = new SomeBean("value1", "value2", "value3", "value4");
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field2");
        FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    @GetMapping("/filtering-list")
    public MappingJacksonValue filteringList() {
        List<SomeBean> list = Arrays.asList(new SomeBean("value1", "value2", "value3", "value4"),
                new SomeBean("value5", "value6", "value7", "value8"));
        return getMappingJacksonValue(list, Stream.of("field3", "field4")
                .collect(Collectors.toCollection(HashSet::new)));
    }

    private MappingJacksonValue getMappingJacksonValue(List<SomeBean> list, Set<String> filterList) {
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(filterList);
        FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }
}
