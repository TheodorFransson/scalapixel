package scalapixel.theme;

import com.formdev.flatlaf.FlatDarkLaf;

public class DarkerLaf extends FlatDarkLaf  {
    public static boolean setup() {
        return setup( new DarkerLaf() );
    }

    @Override
    public String getName() {
        return "DarkerLaf";
    }
}