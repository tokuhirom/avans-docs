package me.geso.avans.docs;

import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
* Created by tokuhirom on 9/12/14.
*/
@ToString
@Data
public class DocEndPoint {
    private List<String> httpMethods;
    private String path;
    private String returnType;
    private List<DocEndPointParameter> parameters;

    String buildReturnType(Method method) {
        final Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            StringBuilder buf = new StringBuilder();
            final Type rawType = ((ParameterizedType) genericReturnType)
                    .getRawType();
            if (rawType instanceof Class) {
                buf.append(((Class<?>) rawType).getSimpleName());
            } else {
                buf.append(rawType.getTypeName());
            }
            buf.append("<");
            final String subtypes = Arrays
                    .stream(((ParameterizedType) genericReturnType)
                            .getActualTypeArguments()).map(
                            it -> {
                                if (it instanceof Class) {
                                    return ((Class<?>) it).getSimpleName();
                                } else {
                                    return it.getTypeName();
                                }
                            }
                    ).collect(Collectors.joining(","));
            buf.append(subtypes);
            buf.append(">");
            return buf.toString();
        } else {
            return genericReturnType.toString();
        }
    }

    List<DocEndPointParameter> buildParameters(Method method) {
        List<DocEndPointParameter> params = Arrays
                .stream(method.getParameters()).map(
                        parameter -> DocEndPointParameter.build(parameter)
                ).collect(Collectors.toList());
        return Collections.unmodifiableList(params);
    }

    static DocEndPoint build(List<String> httpMethods, String path, Method method) {
        DocEndPoint endPoint = new DocEndPoint();
        endPoint.setHttpMethods(httpMethods);
        endPoint.setPath(path);
        endPoint.setParameters(endPoint.buildParameters(method));
        endPoint.setReturnType(endPoint.buildReturnType(method));
        return endPoint;
    }
}
