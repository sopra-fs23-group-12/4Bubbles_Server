package ch.uzh.ifi.hase.soprafs23;

//netty-socketio corundumstudio is the library to use socketio for java  (https://github.com/mrniko/netty-socketio/tree/master/src)
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@RestController
@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String helloWorld() {
        return "The application is running.";
    }


    //imported from the Spring Boot Netty Socket.IO Example (https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d)
    // and Spring Boot Netty Socket.IO Example by https://github.com/jamesjieye/netty-socketio.spring
    @Value("${rt-server.host}")
    private String host;

    @Value("${rt-server.port}")
    private Integer port;


    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setOrigin("*");
        //config.setAllowHeaders("*");
        return new SocketIOServer(config);
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }
        };
    }
}
