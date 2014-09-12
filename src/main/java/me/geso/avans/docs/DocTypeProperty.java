package me.geso.avans.docs;

import lombok.Data;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * Created by tokuhirom on 9/12/14.
 */
@Data
public class DocTypeProperty {
    private String type;
    private String name;

    static DocTypeProperty build(PropertyDescriptor propertyDescriptor) {
        DocTypeProperty prop = new DocTypeProperty();
        Method readMethod = propertyDescriptor.getReadMethod();
        if (readMethod != null) {
            prop.setType(readMethod.getGenericReturnType().toString()
                    .replaceAll("java\\.util\\.", "")
                    .replaceAll("java\\.lang\\.", ""));
        }
        prop.setName(propertyDescriptor.getName());
        return prop;
    }
}
