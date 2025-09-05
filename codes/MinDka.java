package UTRLab;
import java.util.*;

public class MinDka {
    public static class Stanje {
        String ime;
        Map<String, Stanje> prijelazi = new HashMap<>();
        boolean prihvatljiv;

        public Stanje (String ime, String[] abc) {
            this.ime = ime;
            for (String s : abc)
                prijelazi.put(s, null);
            this.prihvatljiv = false;
        }

        public void addPrijelaz (String znak,Stanje ime) {
            prijelazi.put(znak,ime);
        }
    }

    public static class ParStanja {
        String ime;
        int isti;
        Stanje stanje1;
        Stanje stanje2;
        Set<ParStanja> utjece = new HashSet<>();

        public ParStanja (Stanje stanje1, Stanje stanje2) {
            this.stanje1 = stanje1;
            this.stanje2 = stanje2;
            if (stanje1.ime.compareTo(stanje2.ime) < 0)
                this.ime = stanje1.ime + "+" + stanje2.ime;
            else
                this.ime = stanje2.ime + "+" + stanje1.ime;
            if (stanje1.prihvatljiv && stanje2.prihvatljiv || !stanje1.prihvatljiv && !stanje2.prihvatljiv)
                isti = 1;
            else
                isti = 0;
        }

        public void setOvisi (ParStanja ovisio) {
            this.utjece.add(ovisio);
        }

        public void Pogledaj () {
            for (ParStanja par : utjece) {
                if (par.isti != 0) {
                    par.isti = 0;
                    par.Pogledaj();
                }
            }
        }
    }

    private static Map<String, Stanje> makniNedohvatljiva (String stanje,String[] abeceda, Map<String, Stanje> sva_stanja, Map<String, Stanje> novo) {
        for (String abc : abeceda) {
            if (!novo.containsKey(sva_stanja.get(stanje).prijelazi.get(abc).ime)) {
                novo.put(sva_stanja.get(stanje).prijelazi.get(abc).ime,sva_stanja.get(stanje).prijelazi.get(abc));
                novo = makniNedohvatljiva(sva_stanja.get(stanje).prijelazi.get(abc).ime,abeceda ,sva_stanja, novo);
            }
        }
        return novo;
    }

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> retciList = new ArrayList<>();
        while (scanner.hasNext()) {
            retciList.add(scanner.nextLine());
        }
        scanner.close();
        String[] retci = retciList.toArray(new String[0]);

        String[] stanja = retci[0].split(",");
        Map<String, Stanje> sva_stanja = new HashMap<>();
        String[] abeceda = retci[1].split(",");
        String[] prih_stanja = retci[2].split(",");
        String poc_stanje = retci[3];

        for (String s : stanja) {
            Stanje s1 = new Stanje(s, abeceda);
            sva_stanja.put(s,s1);
        }
        for (String s1 : prih_stanja) {
            if (sva_stanja.containsKey(s1)) {
                Stanje s = sva_stanja.get(s1);
                s.prihvatljiv = true;
            }
        }
        for (int i = 4; i < retci.length; i++) {
            String[] redak = retci[i].split("->");
            String stanje = redak[0].split(",")[0];
            String znak = redak[0].split(",")[1];

            sva_stanja.get(stanje).addPrijelaz(znak,sva_stanja.get(redak[1]));
        }

        //makni nedohvatljiva
        Map<String, Stanje> novo = new HashMap<>();
        novo.put(poc_stanje,sva_stanja.get(poc_stanje));
        novo = makniNedohvatljiva(poc_stanje,abeceda,sva_stanja,novo);

        Map<String,ParStanja> parovi = new HashMap<>();
        for (Stanje stanje1: novo.values()) {
            for (Stanje stanje2: novo.values()) {
                if (!stanje1.ime.equals(stanje2.ime)) {
                    ParStanja par = new ParStanja(stanje1,stanje2);
                    parovi.putIfAbsent(par.ime,par);
                }
            }
        }

        //provodenje algoritma
        for (ParStanja par:parovi.values()) {
            if (par.isti == 1) {
                int uvjet = 1;
                for (String abc: abeceda) {
                    Stanje prvi = par.stanje1.prijelazi.get(abc);
                    Stanje drugi = par.stanje2.prijelazi.get(abc);
                    if (!prvi.ime.equals(drugi.ime)) {
                        String imePara;
                        if (prvi.ime.compareTo(drugi.ime) < 0)
                            imePara = prvi.ime + "+" + drugi.ime;
                        else
                            imePara = drugi.ime + "+" + prvi.ime;
                        if (parovi.get(imePara).isti == 0)
                            uvjet = 0;
                        else
                            parovi.get(imePara).utjece.add(par);
                    }
                }
                if (uvjet == 0) {
                    par.isti = 0;
                    par.Pogledaj();
                }
            }
        }

        //micanje istovjetnih
        for (ParStanja par: parovi.values()){
            if (par.isti==1){
                if (par.stanje1.ime.compareTo(par.stanje2.ime)<0){
                    novo.remove(par.stanje2.ime);
                }else novo.remove(par.stanje1.ime);
            }
        }
        //promjena prijelaza
        for (Stanje stanje: novo.values()) {
            for (String abc: abeceda) {
                Stanje pocetno = stanje.prijelazi.get(abc);
                if (!novo.containsKey(pocetno.ime)) {
                    for (String stanj1 : novo.keySet()){
                        String stvorenoIme = stanj1 + "+" + pocetno.ime;
                        if (parovi.containsKey(stvorenoIme)) {
                            ParStanja par = parovi.get(stvorenoIme);
                            if (par.isti == 1) {
                                stanje.addPrijelaz(abc,novo.get(stanj1));
                            }
                        }
                    }
                }
            }
        }

        int prvi = 0;
        for (String s1 : novo.keySet()) {
            if (prvi == 0) {
                System.out.print(s1);
                prvi = 1;
            } else
                System.out.print("," + s1);
        }
        System.out.print("\n");
        System.out.println(retci[1]);
        int prviS = 1;
        for (Stanje stanje: novo.values()) {
            if (stanje.prihvatljiv==true) {
                if (prviS == 1) {
                    System.out.print(stanje.ime);
                    prviS = 0;
                } else
                    System.out.print("," + stanje.ime);
            }
        }
        System.out.print("\n");

        if (novo.containsKey(poc_stanje))
            System.out.println(poc_stanje);
        else {
            for (Stanje stanje: novo.values()) {
                String par = stanje.ime + "+" + poc_stanje;
                if ((parovi.containsKey(par)) && parovi.get(par).isti==1) {
                    System.out.println(novo.get(stanje.ime).ime);
                    break;
                }
            }
        }
        //prijelazi
        int prvi_red = 1;
        for (Stanje stanje: novo.values()) {
            for (String a:abeceda) {
                System.out.println(stanje.ime + "," + a + "->" + stanje.prijelazi.get(a).ime);
            }
        }
    }
}
