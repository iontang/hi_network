package com.protocol.http.server.version2;



import com.protocol.http.util.ConfigUtil;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;

public final class HelidonServer {

    private static Routing createRouting() {
        return Routing.builder()
                .get(ConfigUtil.getConfig("urlPath"), (req, resp) -> resp.send("Greetings, Helidon!"))
                .build();
    }

    public static void main(final String[] args) {
        ServerConfiguration serverConfig =
                ServerConfiguration.builder()
                        .port(Integer.parseInt(ConfigUtil.getConfig("port")))
                        .build();

        WebServer.create(serverConfig, createRouting())
                .start()
                .thenAccept(server -> System.out.println("Started on port: " + server.port()));
    }
}