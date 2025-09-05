package UTRLab;
import java.util.*;

public class SimPa {
    public static class Stanje {
        String ime;
        String[] ulazi;
        String[] stog;
        Map<String, String> prijelazi;

        public Stanje (String ime, String[] ulazi, String[] stog) {
            this.ime=ime;
            this.ulazi=ulazi;
            this.stog=stog;
            this.prijelazi = new HashMap<>();
        }

        public void setPrijelazi (String ulaz, String izlaz) {
            this.prijelazi.put(ulaz,izlaz);
        }

        public String getPrijelazi (String ulaz) {
            return prijelazi.get(ulaz);
        }
    }

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> retciList = new ArrayList<>();

        while (scanner.hasNext())
            retciList.add(scanner.nextLine());
        scanner.close();
        String[] retci = retciList.toArray(new String[0]);

        String[] ulaz = retci[0].split("\\|");
        String[] stanja = retci[1].split(",");
        Map<String, Stanje> sva_stanja = new HashMap<>();
        String[] ulazni_znakovi = retci[2].split(",");
        String[] znakovi_stoga = retci[3].split(",");
        String[] prihvatljiva_stanja = retci[4].split(",");
        String poc_stanje = retci[5];

        for (String s : stanja)
            sva_stanja.put(s, new Stanje(s, ulazni_znakovi, znakovi_stoga));


        for (int i = 7; i < retci.length; i++) {
            String[] redak = retci[i].split("->");
            String stanje = redak[0].split(",")[0];
            String ulazni_znak = redak[0].split(",")[1];
            String znak_stoga = redak[0].split(",")[2];
            String ul = ulazni_znak + "," + znak_stoga;

            sva_stanja.get(stanje).setPrijelazi(ul, redak[1]);
        }

        //pokretanje simulacije
        for (int i = 0 ; i < ulaz.length ; i++) {
            Map<Integer, String> stog = new HashMap<>();
            stog.put(1, retci[6]);
            int velicina_stoga = 1;

            String[] niz = ulaz[i].split(",");
            String stanje = poc_stanje;

            String s1 = poc_stanje + "#" + stog.get(1);
            System.out.print(s1);
            int fail = 0;
            for (int j = 0 ; j < niz.length ; j++) {
                if (sva_stanja.get(stanje).prijelazi.containsKey(niz[j]+ "," + stog.get(velicina_stoga))) {
                    String izlaz = sva_stanja.get(stanje).getPrijelazi(niz[j] + "," + stog.get(velicina_stoga));
                    stog.remove(velicina_stoga);
                    velicina_stoga--;
                    stanje = izlaz.split(",")[0];
                    String dodaj_stog = izlaz.split(",")[1];
                    if (!(dodaj_stog.equals("$"))) {
                        for (int n = dodaj_stog.length()-1 ; n >= 0 ; n--) {
                            stog.put(++velicina_stoga, "" + dodaj_stog.charAt(n));
                        }
                    }

                } else if (sva_stanja.get(stanje).prijelazi.containsKey("$," + stog.get(velicina_stoga))) {
                    j--;
                    String izlaz = sva_stanja.get(stanje).getPrijelazi("$," + stog.get(velicina_stoga));
                    stog.remove(velicina_stoga);
                    velicina_stoga--;
                    stanje = izlaz.split(",")[0];
                    String dodaj_stog = izlaz.split(",")[1];
                    if (!dodaj_stog.equals("$")) {
                        for (int n = dodaj_stog.length() - 1 ; n >= 0 ; n--) {
                            stog.put(++velicina_stoga, "" + dodaj_stog.charAt(n));
                        }
                    }
                } else {
                    fail = 1;
                }
                if (fail == 0) {
                    System.out.print("|");
                    System.out.print(stanje + "#");
                    if (velicina_stoga == 0)
                        System.out.print("$");
                    else {
                        for (int k = velicina_stoga; k > 0 ; k--)
                            System.out.print(stog.get(k));
                    }
                } else {
                    System.out.print("|fail");
                    break;
                }
            }

            if (fail == 1) {
                System.out.print("|0\n");
            } else {
                boolean pisi = true;
                int gotovo = 0;
                for (String s : prihvatljiva_stanja) {
                    if (stanje.equals(s)) {
                        System.out.print("|1\n");
                        gotovo = 1;
                        pisi = false;
                        break;
                    }
                }
                if (gotovo == 0) {
                    int nema_vise = 1;
                    while (nema_vise == 1) {
                        nema_vise = 0;
                        if (sva_stanja.get(stanje).prijelazi.containsKey("$," + stog.get(velicina_stoga))) {
                            nema_vise = 1;
                            String izlaz = sva_stanja.get(stanje).getPrijelazi("$," + stog.get(velicina_stoga));
                            stog.remove(velicina_stoga);
                            velicina_stoga--;
                            stanje = izlaz.split(",")[0];
                            String dodaj_stog = izlaz.split(",")[1];
                            if (!dodaj_stog.equals("$")) {
                                for (int n = dodaj_stog.length() - 1 ; n >= 0 ; n--)
                                    stog.put(++velicina_stoga, "" + dodaj_stog.charAt(n));
                            }
                        }
                        if (nema_vise == 1) {
                            System.out.print("|");
                            System.out.print(stanje + "#");
                            if (velicina_stoga == 0)
                                System.out.print("$");
                            else {
                                for (int k = velicina_stoga; k > 0 ; k--)
                                    System.out.print(stog.get(k));     //ispisi znakove stoga
                            }
                            for (String s : prihvatljiva_stanja) {
                                if (stanje.equals(s)) {
                                    System.out.print("|1\n");
                                    nema_vise = 0;
                                    pisi = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (pisi)
                    System.out.print("|0\n");
            }
        }
    }
}
