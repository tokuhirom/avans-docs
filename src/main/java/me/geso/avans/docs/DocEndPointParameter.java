package me.geso.avans.docs;

import lombok.Data;
import lombok.ToString;
import me.geso.avans.annotation.BodyParam;
import me.geso.avans.annotation.JsonParam;
import me.geso.avans.annotation.PathParam;
import me.geso.avans.annotation.QueryParam;
import me.geso.tinyvalidator.Constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tokuhirom on 9/12/14.
 */
@ToString
@Data
public class DocEndPointParameter {
    private String name;
    private String type;
    private List<DocTypeParameterConstraint> constraints;

    List<DocTypeParameterConstraint> buildConstraints(Parameter parameter) {
        List<DocTypeParameterConstraint> annotations = new ArrayList<>();
        for (Annotation annotation : parameter.getAnnotations()) {
            Constraint constraint = annotation.getClass().getAnnotation(
                    Constraint.class);
            if (constraint != null) {
                annotations.add(DocTypeParameterConstraint.build(annotation));
            }
        }
        return annotations;
    }

    private void initialize(Parameter parameter) {
        for (Annotation annotation : parameter.getAnnotations()) {
            if (annotation instanceof QueryParam) {
                this.name = ((QueryParam) annotation).value();
                this.type = "QueryParam";
                return;
            } else if (annotation instanceof PathParam) {
                this.name = ((PathParam) annotation).value();
                this.type = "PathParam";
                return;
            } else if (annotation instanceof BodyParam) {
                this.name = ((BodyParam) annotation).value();
                this.type = "BodyParam";
                return;
            } else if (annotation instanceof JsonParam) {
                this.name = null;
                this.type = "JsonParam";
                return;
            }
        }
        this.name = parameter.getName();
        this.type = null;
    }

    static DocEndPointParameter build(Parameter parameter) {
        DocEndPointParameter endPointParameter = new DocEndPointParameter();
        endPointParameter.initialize(parameter);
        endPointParameter.setConstraints(endPointParameter
                .buildConstraints(parameter));
        return endPointParameter;
    }
}
