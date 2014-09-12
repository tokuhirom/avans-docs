package me.geso.avans.docs;

import lombok.Data;
import lombok.ToString;
import me.geso.avans.Dispatcher;
import me.geso.routes.HttpRoute;
import me.geso.routes.WebRouter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by tokuhirom on 9/12/14.
 */
@ToString
@Data
public class Doc {
    private List<DocEndPoint> endpoints;
    private List<DocType> apiClasses;

    static Doc build(Dispatcher dispatcher) {
        final WebRouter<Dispatcher.Action> router = dispatcher.getRouter();
        return Doc.build(router);
    }

    static Doc build(final WebRouter<Dispatcher.Action> router) {
        final List<HttpRoute<Dispatcher.Action>> patterns = router.getPatterns();

        List<DocEndPoint> endpoints = patterns.stream()
                .map(route -> {
                    return DocEndPoint.build(
                            route.getMethods(),
                            route.getPathRoute().getPath(),
                            route.getPathRoute().getDestination().getMethod());
                }).collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

        List<DocType> apiClasses = patterns.stream()
                .map(route -> route.getPathRoute().getDestination().getMethod())
                .flatMap(
                        method -> {
                            Type genericReturnType = method.getGenericReturnType();
                            if (genericReturnType instanceof ParameterizedType) {
                                List<Type> collect = Arrays
                                        .stream(((ParameterizedType) genericReturnType)
                                                .getActualTypeArguments())
                                        .collect(toList());
                                collect.add(((ParameterizedType) genericReturnType)
                                        .getRawType());
                                return collect.stream();
                            } else {
                                return Stream.of(genericReturnType);
                            }
                        }
                )
                .distinct()
                .filter(type -> type instanceof Class)
                .map(type -> (Class<?>) type)
                .filter(klass -> !klass.getPackage().getName().startsWith("java."))
                .filter(klass -> !klass.getPackage().getName().startsWith("javax."))
                .map(klass -> DocType.build(klass))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

        Doc info = new Doc();
        info.setEndpoints(endpoints);
        info.setApiClasses(apiClasses);
        return info;
    }
}
