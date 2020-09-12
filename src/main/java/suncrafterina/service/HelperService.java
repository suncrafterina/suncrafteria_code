package suncrafterina.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class HelperService {

    public static double bytesToKB(double bytes){
        return  (bytes / 1024.0);
    }
    private static final Random generator = new Random();

    public static String generatePin() {

        return new StringBuilder().append(100000 + generator.nextInt(900000)).toString();
    }
}
