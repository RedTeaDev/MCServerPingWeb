package dev.redteadev.mcserverpingweb;

import dev.redcoke.mcserverping.MCServerPing;
import dev.redcoke.mcserverping.MCServerPingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

@RestController
public class Controller {
    /**
     * get JSON data of the server, Read more about the JSON format here: https://github.com/RedCokeDevelopment/MCServerPing/wiki/Response-format
     * @param server Server Address
     * @param port port
     * @return JSON data of the server
     */
    @RequestMapping("/ping")
    public String ping(@RequestParam(name = "address", required = true) String server, @RequestParam(name = "port", required = false) Integer port) {
        if (port == null) {
            port = 25565;
        } // fill in default port if not specified
        try {
            return MCServerPing.getPing(server, port).getAsJsonString();
        } catch (NoRouteToHostException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No route to host");
        } catch (SocketTimeoutException ex) {
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Socket timeout");
        } catch (TimeoutException ex) {
            throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, ex.getMessage());
        } catch (ConnectException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @RequestMapping("/")
    public String root() {
        return "Endpoint: /ping?address=<server>&port=<port>";
    }

}
