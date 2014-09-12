package me.geso.avans.docs;

import me.geso.avans.Dispatcher;

/**
 * Created by tokuhirom on 9/12/14.
 */
public class DocumentBuilder {
    public DocumentBuilder() {
    }

    public Doc build(Dispatcher dispatcher) {
        return Doc.build(dispatcher);
    }
}
