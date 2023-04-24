package ch.uzh.ifi.hase.soprafs23;

//this class is for the websockets

// class copied from https://github.com/jamesjieye/netty-socketio.spring
// also mentioned in the tutorial https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ServerCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer server;

    @Autowired
    public ServerCommandLineRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        server.startAsync();
        /* server.start();
        Thread.sleep(Integer.MAX_VALUE);
        server.stop(); */
    }
}