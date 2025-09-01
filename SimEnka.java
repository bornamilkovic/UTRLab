package UTRLab;
import java.util.*;

public class SimEnka {
    public static class Stanje {
        String ime;
        boolean prihvatljiv;
        Set<Stanje> sva_stanja = new HashSet<>();
        Map<String, Set<Stanje>> prijelazi = new HashMap<>();
        Set<Stanje> e_okolina = new HashSet<>();

        public void addStanje (Set<Stanje> stanje) {
            this.sva_stanja = stanje;
        }

        public Stanje (String ime, String[] abc) {
            this.ime = ime;
            for (String s : abc) {
                prijelazi.put(s, new HashSet<>());
            }
        }

        public void setPrihvatljiv (boolean prihvatljiv) {
            this.prihvatljiv = prihvatljiv;
        }

        public void setPrijelazi (String znak, Set<Stanje> stanja) {
            prijelazi.put(znak,stanja);
        }

        public void addE_okolina(Set<Stanje> stanja) {
            e_okolina.addAll(stanja);
            for (Stanje stanje : stanja) {
                e_okolina.addAll(stanje.getE_okolina());
                for (Stanje stanje1 : sva_stanja) {
                    if (stanje1.e_okolina.contains(this)) {
                        stanje1.e_okolina.add(stanje);
                        stanje1.e_okolina.addAll(stanje.getE_okolina());
                    }
                }
            }
        }

        public Set<Stanje> getE_okolina () {
            return e_okolina;
        }

        public String getIme () {
            return ime;
        }

        @Override
        public String toString() {
            return "Stanje{" +
                    "ime='" + ime + '\'' +
                    ", prihvatljiv=" + prihvatljiv;
        }
    }

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<String> retciList = new ArrayList<>();

        while (scanner.hasNext()) {
            retciList.add(scanner.nextLine());
        }
        scanner.close();

        String[] retci = retciList.toArray(new String[0]);
        String[] nizovi = retci[0].split("\\|");
        String[] stanja = retci[1].split(",");
        Map<String, Stanje> sva_stanja = new HashMap<>();
        String[] abeceda = retci[2].split(",");
        String[] prih_stanja = retci[3].split(",");
        String poc_stanje = retci[4];
        for(String s : stanja){
            Stanje s1 = new Stanje(s, abeceda);
            sva_stanja.put(s,s1);
        }
        Set<Stanje> stanjeSet = new HashSet<>(sva_stanja.values());
        for (Stanje stanje : sva_stanja.values()) {
            stanje.addStanje(stanjeSet);
        }
        for (String s1 : prih_stanja) {
            Stanje s = sva_stanja.get(s1);
            s.setPrihvatljiv(true);
        }
        for (int i = 5; i < retci.length; i++) {
            String[] redak = retci[i].split("->");
            String stanje = redak[0].split(",")[0];
            String znak = redak[0].split(",")[1];
            Set<Stanje> prijelazna = new HashSet<>();

            for (String s: redak[1].split(",")) {
                if (s.equals("#"))
                    continue;
                prijelazna.add(sva_stanja.get(s));
            }
            if (znak.equals("$"))
                sva_stanja.get(stanje).addE_okolina(prijelazna);
            else
                sva_stanja.get(stanje).setPrijelazi(znak,prijelazna);
        }
        TreeSet<String> prosla = new TreeSet<>();
        for (int i = 0; i < nizovi.length; i++) {
            prosla = new TreeSet<>();
            String[] znakovi = nizovi[i].split(",");
            for (int j = -1; j < znakovi.length; j++) {
                TreeSet<String> trenutna_stanja = new TreeSet<>();
                if (j == -1) {
                    prosla.add(poc_stanje);
                    for (Stanje st : sva_stanja.get(poc_stanje).e_okolina) {
                        prosla.add(st.getIme());
                    }
                    int k = 0;
                    for (String st: prosla){
                        if (k == 0) {
                            k = 1;
                            System.out.print(st);
                        } else {
                            System.out.print(",");
                            System.out.print(st);
                        }
                    }
                } else {
                    for (String s1 : prosla) {
                        for  (Stanje ime : sva_stanja.get(s1).prijelazi.get(znakovi[j])) {
                            trenutna_stanja.add(ime.getIme());

                            for (Stanje stanje : ime.getE_okolina()) {
                                trenutna_stanja.add(stanje.getIme());
                            }
                        }
                    }
                    System.out.print("|");

                    if (trenutna_stanja.isEmpty())
                        System.out.print("#");
                    else {
                        int k = 0;
                        for (String stanje : trenutna_stanja) {
                            if (k > 0)
                                System.out.print(",");
                            System.out.print(stanje);
                            k++;
                        }
                    }
                    prosla = new TreeSet<>();
                    prosla.addAll(trenutna_stanja);
                }
            }
            System.out.print("\n");
        }
    }
}


