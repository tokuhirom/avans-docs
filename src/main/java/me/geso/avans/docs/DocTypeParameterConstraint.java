package me.geso.avans.docs;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by tokuhirom on 9/12/14.
 */
@ToString
@Data
public class DocTypeParameterConstraint {
    private Object value;
    private String name;

    @SneakyThrows
    static DocTypeParameterConstraint build(Annotation annotation) {
        DocTypeParameterConstraint constraint = new DocTypeParameterConstraint();
        constraint.setName(annotation.annotationType().getSimpleName());
        Method method = annotation.annotationType().getMethod("value");
        if (method != null) {
            constraint.setValue(method.invoke(annotation));
        }
        return constraint;
    }
}
