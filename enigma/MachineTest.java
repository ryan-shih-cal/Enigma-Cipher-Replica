package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import static enigma.EnigmaException.*;

import static enigma.TestUtils.*;

public class MachineTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void testInsertRotors() {
        Alphabet alpha = new Alphabet();
        Rotor I = new MovingRotor("Rotor I", new Permutation("(AELTPHQXRU)(BKNW)(CMOY)(DFG)(IV)(JZ)(S)", alpha), "Q");
        Rotor III = new MovingRotor("Rotor III", new Permutation("(ABDHPEJT)(CFLVMZOYQIRWUKXSG)(N)", alpha), "V");
        Rotor IV = new MovingRotor("Rotor IV", new Permutation("(AEPLIYWCOXMRFZBSTGJQNH)(DV)(KU)", alpha), "J");
        Rotor Beta = new FixedRotor("Rotor Beta", new Permutation("(ALBEVFCYODJWUGNMQTZSKPR)(HIX)", alpha));
        Rotor B = new Reflector("Rotor B", new Permutation("(AE)(BN)(CK)(DQ)(FU)(GY)(HW)(IJ)(LO)(MP)(RX)(SZ)(TV)", alpha));
        Collection<Rotor> rotorList = new LinkedList<Rotor>();
        rotorList.add(B); rotorList.add(Beta); rotorList.add(IV); rotorList.add(III); rotorList.add(I);
        Permutation plugboard = new Permutation("(YF)(ZH)", alpha);
        Machine m = new Machine(alpha, 5, 3, rotorList);
        m.setPlugboard(plugboard);

        m.insertRotors(new String[]{"Rotor B", "Rotor Beta", "Rotor IV", "Rotor III", "Rotor I"});
        assertArrayEquals(new Rotor[]{B, Beta, IV, III, I}, m._rotors);
    }

    @Test
    public void testSetRotors() {
        Alphabet alpha = new Alphabet();
        Rotor I = new MovingRotor("Rotor I", new Permutation("(AELTPHQXRU)(BKNW)(CMOY)(DFG)(IV)(JZ)(S)", alpha), "Q");
        Rotor III = new MovingRotor("Rotor III", new Permutation("(ABDHPEJT)(CFLVMZOYQIRWUKXSG)(N)", alpha), "V");
        Rotor IV = new MovingRotor("Rotor IV", new Permutation("(AEPLIYWCOXMRFZBSTGJQNH)(DV)(KU)", alpha), "J");
        Rotor Beta = new FixedRotor("Rotor Beta", new Permutation("(ALBEVFCYODJWUGNMQTZSKPR)(HIX)", alpha));
        Rotor B = new Reflector("Rotor B", new Permutation("(AE)(BN)(CK)(DQ)(FU)(GY)(HW)(IJ)(LO)(MP)(RX)(SZ)(TV)", alpha));
        Collection<Rotor> rotorList = new LinkedList<Rotor>();
        rotorList.add(B); rotorList.add(Beta); rotorList.add(IV); rotorList.add(III); rotorList.add(I);
        Permutation plugboard = new Permutation("(YF)(ZH)", alpha);
        Machine m = new Machine(alpha, 5, 3, rotorList);
        m.setPlugboard(plugboard);

        m.insertRotors(new String[]{"Rotor B", "Rotor Beta", "Rotor III", "Rotor IV", "Rotor I"});
        m.setRotors("AXLE");
        assertArrayEquals(new int[]{0, 23, 11, 4}, m.settings);
    }

    @Test
    public void testAdvSetting() {
        Alphabet alpha = new Alphabet();
        Rotor I = new MovingRotor("Rotor I", new Permutation("(AELTPHQXRU)(BKNW)(CMOY)(DFG)(IV)(JZ)(S)", alpha), "Q");
        Rotor III = new MovingRotor("Rotor III", new Permutation("(ABDHPEJT)(CFLVMZOYQIRWUKXSG)(N)", alpha), "V");
        Rotor IV = new MovingRotor("Rotor IV", new Permutation("(AEPLIYWCOXMRFZBSTGJQNH)(DV)(KU)", alpha), "J");
        Rotor Beta = new FixedRotor("Rotor Beta", new Permutation("(ALBEVFCYODJWUGNMQTZSKPR)(HIX)", alpha));
        Rotor B = new Reflector("Rotor B", new Permutation("(AE)(BN)(CK)(DQ)(FU)(GY)(HW)(IJ)(LO)(MP)(RX)(SZ)(TV)", alpha));
        Collection<Rotor> rotorList = new LinkedList<Rotor>();
        rotorList.add(B); rotorList.add(Beta); rotorList.add(IV); rotorList.add(III); rotorList.add(I);
        Permutation plugboard = new Permutation("(YF)(ZH)", alpha);
        Machine m = new Machine(alpha, 5, 3, rotorList);
        m.setPlugboard(plugboard);

        m.insertRotors(new String[]{"Rotor B", "Rotor Beta", "Rotor III", "Rotor IV", "Rotor I"});
        m.setRotors("AXLE");
        m.advSetting();
        assertArrayEquals(new int[]{0, 23, 11, 5}, m.settings);
        for (int i = 0; i < 11; i += 1) {
            m.advSetting();
        }
        assertArrayEquals(new int[]{0, 23, 11, 16}, m.settings);
        m.advSetting();
        assertArrayEquals(new int[]{0, 23, 12, 17}, m.settings);
    }

    @Test
    public void testConvertInt() {
        Alphabet alpha = new Alphabet();
        Rotor I = new MovingRotor("Rotor I", new Permutation("(AELTPHQXRU)(BKNW)(CMOY)(DFG)(IV)(JZ)(S)", alpha), "Q");
        Rotor III = new MovingRotor("Rotor III", new Permutation("(ABDHPEJT)(CFLVMZOYQIRWUKXSG)(N)", alpha), "V");
        Rotor IV = new MovingRotor("Rotor IV", new Permutation("(AEPLIYWCOXMRFZBSTGJQNH)(DV)(KU)", alpha), "J");
        Rotor Beta = new FixedRotor("Rotor Beta", new Permutation("(ALBEVFCYODJWUGNMQTZSKPR)(HIX)", alpha));
        Rotor B = new Reflector("Rotor B", new Permutation("(AE)(BN)(CK)(DQ)(FU)(GY)(HW)(IJ)(LO)(MP)(RX)(SZ)(TV)", alpha));
        Collection<Rotor> rotorList = new LinkedList<Rotor>();
        rotorList.add(B); rotorList.add(Beta); rotorList.add(IV); rotorList.add(III); rotorList.add(I);
        Permutation plugboard = new Permutation("(YF)(ZH)", alpha);
        Machine m = new Machine(alpha, 5, 3, rotorList);
        m.setPlugboard(plugboard);

        m.insertRotors(new String[]{"Rotor B", "Rotor Beta", "Rotor III", "Rotor IV", "Rotor I"});
        m.setRotors("AXLE");
        assertEquals(25, m.convert(24));
    }

    @Test
    public void testConvertString() {
        Alphabet alpha = new Alphabet();
        Rotor I = new MovingRotor("Rotor I", new Permutation("(AELTPHQXRU)(BKNW)(CMOY)(DFG)(IV)(JZ)(S)", alpha), "Q");
        Rotor III = new MovingRotor("Rotor III", new Permutation("(ABDHPEJT)(CFLVMZOYQIRWUKXSG)(N)", alpha), "V");
        Rotor IV = new MovingRotor("Rotor IV", new Permutation("(AEPLIYWCOXMRFZBSTGJQNH)(DV)(KU)", alpha), "J");
        Rotor Beta = new FixedRotor("Rotor Beta", new Permutation("(ALBEVFCYODJWUGNMQTZSKPR)(HIX)", alpha));
        Rotor B = new Reflector("Rotor B", new Permutation("(AE)(BN)(CK)(DQ)(FU)(GY)(HW)(IJ)(LO)(MP)(RX)(SZ)(TV)", alpha));
        Collection<Rotor> rotorList = new LinkedList<Rotor>();
        rotorList.add(B); rotorList.add(Beta); rotorList.add(IV); rotorList.add(III); rotorList.add(I);
        Permutation plugboard = new Permutation("(YF)(ZH)", alpha);
        Machine m = new Machine(alpha, 5, 3, rotorList);
        m.setPlugboard(plugboard);

        m.insertRotors(new String[]{"Rotor B", "Rotor Beta", "Rotor III", "Rotor IV", "Rotor I"});
        m.setRotors("AXLE");
        assertEquals("Z", m.convert("Y"));
    }
}
