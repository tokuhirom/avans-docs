package me.geso.avans.docs;

import lombok.Data;
import lombok.SneakyThrows;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tokuhirom on 9/12/14.
 */
@Data
public class DocType {
    private String name;
    private List<DocTypeProperty> properties;

    @SneakyThrows
    static DocType build(Class<?> klass) {
        BeanInfo beanInfo = Introspector.getBeanInfo(klass);
        List<DocTypeProperty> properties = Arrays
                .stream(beanInfo
                        .getPropertyDescriptors())
                .map(propertyDescriptor -> DocTypeProperty
                        .build(propertyDescriptor))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

        DocType apiClass = new DocType();
        apiClass.setName(klass.getName());
        apiClass.setProperties(properties);
        return apiClass;
    }
}
