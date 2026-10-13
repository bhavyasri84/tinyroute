package dev.tinyroute;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class TinyRouteApplication {
    public static void main(String[] args) { SpringApplication.run(TinyRouteApplication.class, args); }
}
