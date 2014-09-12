package me.geso.avans.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.geso.avans.APIResponse;
import me.geso.avans.ControllerBase;
import me.geso.avans.Dispatcher;
import me.geso.avans.annotation.GET;
import me.geso.webscrew.response.WebResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class DocumentBuilderTest {

    public static class Foo extends ControllerBase {
        @GET("/")
        public WebResponse root() {
            return this.renderText("HOGE");
        }

        @GET("/p")
        public APIResponse<String> stringResponse() {
            return new APIResponse<>(200, new ArrayList<>(), "hoge");
        }
    }

    @Test
    public void test() throws Exception {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.registerClass(Foo.class);
        final Doc doc = new DocumentBuilder().build(dispatcher);
        assertEquals(doc.getApiClasses().size(), 2);
        String types = doc.getApiClasses().stream().map(it -> it.getName()).sorted().collect(Collectors.joining(","));
        assertEquals("me.geso.avans.APIResponse,me.geso.webscrew.response.WebResponse", types);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(doc);
        System.out.println(json);
        mapper.readValue(json, Doc.class); // It should work.
    }
}
