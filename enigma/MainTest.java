package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.*;

import static enigma.EnigmaException.*;

import static enigma.TestUtils.*;

public class MainTest {

    public void printMessageLine(String msg) {
        msg =  msg.replaceAll("\\s+", "");
        int msgInd = 0;
        char[] msgArray = msg.toCharArray();
        for (char c : msgArray) {
            if (msgInd % 5 != 0 || msgInd == 0) {
                System.out.print(c);
            } else {
                System.out.print(" ");
                System.out.print(c);
            }
            msgInd += 1;
        }
    }

    @Test
    public void testPrintMessage() {
        String m1 = "HELLO MY NAME IS RYAN";
        System.out.println(m1);
        printMessageLine(m1);
    }

    @Test
    public void testScanner() {
        Scanner sc = new Scanner("trivial.in");
        while (sc.hasNextLine()) {
            System.out.println(sc.nextLine());
        }
    }
}
