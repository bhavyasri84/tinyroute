package dev.tinyroute.link;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;
@Component
public class Base62CodeGenerator {
    private static final char[] ALPHABET="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final SecureRandom random=new SecureRandom();
    public String nextCode(int length) {
        StringBuilder out=new StringBuilder(length);
        for(int i=0;i<length;i++) out.append(ALPHABET[random.nextInt(ALPHABET.length)]);
        return out.toString();
    }
}
