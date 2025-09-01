package UTRLab;
import java.util.*;

public class Parser {
    public static int pokazivac = 0;
    public static String ulaz;
    public static String izlaz = "";
    public static boolean kraj;

    public static void S () {
        if (!kraj) izlaz += "S";
        if (ulaz.charAt(pokazivac) != 'a' && ulaz.charAt(pokazivac) != 'b'){
            if (ulaz.charAt(pokazivac) == 'k')
                kraj = true;
            return;
        }
        if (ulaz.charAt(pokazivac) == 'a') {
            pokazivac++;
            A();
            B();
        } else {
            pokazivac++;
            B();
            A();
        }
    }

    public static void A () {
        if (!kraj) izlaz += "A";
        if (ulaz.charAt(pokazivac) != 'a' && ulaz.charAt(pokazivac) != 'b') {
            kraj = true;
            return;
        }
        if (ulaz.charAt(pokazivac) == 'b') {
            pokazivac++;
            C();
        } else {
            pokazivac++;
            return;
        }
    }

    public static void B () {
        if (!kraj) izlaz += "B";
        if (ulaz.charAt(pokazivac) != 'c' || ulaz.charAt(pokazivac+1) != 'c') {
            return;
        }
        pokazivac = pokazivac + 2;
        S();
        if (ulaz.charAt(pokazivac) != 'b' || ulaz.charAt(pokazivac+1) != 'c')
            return;
        pokazivac = pokazivac + 2;
    }

    public static void C () {
        izlaz += "C";
        A();
        A();
        return;
    }

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> retciList = new ArrayList<>();
        while (scanner.hasNext())
            retciList.add(scanner.nextLine());
        scanner.close();
        String[] retci = retciList.toArray(new String[0]);
        String ulazni_niz = retci[0];
        ulazni_niz += "k";

        ulaz = ulazni_niz;

        S();
        System.out.println(izlaz);
        if (ulaz.charAt(pokazivac) != 'k' || kraj) {
            System.out.println("NE");
        } else {
            System.out.println("DA");
        }
    }
}