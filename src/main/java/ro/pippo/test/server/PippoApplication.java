package ro.pippo.test.server;

import ro.pippo.core.Application;

public class PippoApplication extends Application {

    @Override
    protected void onInit() {
        getRouter().ignorePaths("/favicon.ico");

        // send a text 'Hello World' as response
        GET("/text", (routeContext) -> {
            routeContext.text().send("Hello World");
        });

        // send a json as response
        GET("/json", (routeContext) -> {
            routeContext.json().send(createContact());
        });

        // send xml as response
        GET("/xml", (routeContext) -> {
            routeContext.xml().send(createContact());
        });

        // send an object and negotiate the Response content-type, default to XML
        GET("/negotiate", (routeContext) -> {
            routeContext.xml().negotiateContentType().send(createContact());
        });

        // send a template as response
        GET("/template", (routeContext) -> {
            routeContext.setLocal("greeting", "Hello");
            routeContext.render("hello");
        });

        // test session set
        GET("/session/set", (routeContext) -> {
            String value = routeContext.setSession("KEY", "Hello World");
            routeContext.text().send(value);
        });

        // test session get
        GET("/session/get", (routeContext) -> {
            routeContext.text().send(routeContext.<String>getSession("KEY"));
        });

        // test session delete
        GET("/session/delete", (routeContext) -> {
            String value = routeContext.removeSession("KEY");
            routeContext.text().send(value);
        });
    }

    private Contact createContact() {
        return new Contact(12345, "John", "0733434435", "Sunflower Street, No. 6");
    }

}
