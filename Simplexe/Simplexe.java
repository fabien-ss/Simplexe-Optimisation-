public class Simplexe{

    String[] valeurs_ajoutees;
    String[] bases;
    float[][] E;
//associer chaque ligne a la ligne pivot
    public void association_linge_pivot(int ligne, int colonne){
        float pivot = E[ligne][colonne];
        float[][] newmat = new float[E.length][E[0].length];
        for(int i = 0; i < E.length; i++){
            if(i != ligne){
                for(int j = 0; j < E[i].length; j++){
                    newmat[i][j] = E[i][j] - (E[i][colonne] / pivot) * E[ligne][j];
                }
            }
        }
        newmat[ligne] = E[ligne];
        E = newmat;
    } 
//normalisation du pivot
    public void transformation_pivot(int ligne, int colonne){
        float pivot = E[ligne][colonne];
        for(int i = 0; i < E[ligne].length; i++){
            E[ligne][i] = E[ligne][i] / pivot; 
        }
    }
//1ere etape, recherche de la valeur positive la plus grande
    public int indice_valeur_maximum_positif(float[] valeurs){
        int Max = -1;
        for(int i = 0; i < valeurs.length - 1; i++){
            if (valeurs[i] > Max && valeurs[i] > 0){
                Max = i;
            }
        }
        return Max;
    }
//minimum positif
    public int rapport_minimum_positif(int laharana_ngeza_indrindra){
//valeur solution
        int indice = 0;
        int laharana_solution = E[0].length - 1;
        float rapportinitiale = Float.MAX_VALUE;
        for(int i = 0; i < E.length - 1; i++){
            if(E[i][laharana_ngeza_indrindra]!=0){
                float rapport = (float) E[i][laharana_solution] / E[i][laharana_ngeza_indrindra];
                if(rapportinitiale > rapport && rapport > 0){
                    indice = i;
                    rapportinitiale = rapport;
                }
            }
        } 
        return indice;
    }

    public void Maximisation(){
        int ligne_farany = E.length - 1;
        int laharana_ngeza_indrindra = indice_valeur_maximum_positif(E[ligne_farany]);
        int ligne_ngeza = rapport_minimum_positif(laharana_ngeza_indrindra); 
        String sortie = bases[ligne_ngeza];
        String entree = valeurs_ajoutees[laharana_ngeza_indrindra];
        
        bases[ligne_ngeza] = entree;
        //valeurs_ajoutees[laharana_ngeza_indrindra] = sortie;

        transformation_pivot(ligne_ngeza, laharana_ngeza_indrindra);
        association_linge_pivot(ligne_ngeza, laharana_ngeza_indrindra);
        int arret = indice_valeur_maximum_positif(E[ligne_farany]);
        this.printE();
        if(arret >= 0){
            this.Maximisation();
        }
    }

    public String completervide(String nombre){
        String snombre = nombre + "";
        String nformat = "";
        int size = 12;
        if(snombre.length() < size){
            int diff = size - snombre.length(); 
            for(int i = 0; i < diff; i++){
                nformat += " ";
            }
        }
        return "|" + nformat + snombre + "";
    }

    public String trait(int nombre, int colonne){
        String r = "";
        for(int i = 0; i < nombre * colonne; i++) r += "_";
        return r;
    }

    public void printE(){
        System.out.print(completervide(""));
        for(int i = 0; i < valeurs_ajoutees.length; i++){
            System.out.print(completervide(valeurs_ajoutees[i]));
        }
        System.out.println("");
        System.out.println(trait(15, valeurs_ajoutees.length));
        System.out.println(" ");
        for(int i = 0; i < E.length; i++){
            System.out.print(completervide(bases[i]));
            for(int j = 0; j < E[i].length; j++){
                System.out.print(completervide(E[i][j]+""));
            }
            System.out.println(" ");
            System.out.println(trait(15, valeurs_ajoutees.length));
            System.out.println(" ");
        }
        float max_z = E[E.length-1][E[0].length-1] * -1;
        System.out.println("Z = " + max_z);
    }

    public Simplexe(float[][] E){
        this.setE(E);
    }

    public void setBases(String[] bases){
        this.bases = bases;
    }

    public String[] getBases(){
        return this.bases;
    }

    public void setVA(String[] va){
        this.valeurs_ajoutees = va;
    }

    public String[] getVA(){
        return this.valeurs_ajoutees;
    }

    public void setE(float[][] E){
        this.E = E;
    }

    public float[][] getE(){
        return this.E;
    }

    public static void main(String[] args){
        float[] l1 = { 1, 3, 1, 1, 0, 0, 0, 3};
        float[] l2 = { -1, 0, 3, 0, 1, 0, 0, 2 };
        float[] l3 = { 2, 4, -1, 0, 0, 1, 0, 4 };
        float[] l4 = { 1, 3, -1, 0, 0, 0, 1, 2 };
        float[] l5 = { 1, 5, 1, 0, 0, 0, 0, 0 };

        float[][] E = { l1, l2, l3, l4, l5 };
        Simplexe Maximisation = new Simplexe(E);
        
        String[] va = { "x1", "x2", "x3", "x4", "x5", "x6", "x7","solutions" };
        String[] bases = { "x4", "x5", "x6", "x7", "z"};

        Maximisation.setVA(va);
        Maximisation.setBases(bases);
        Maximisation.printE();
        Maximisation.Maximisation();
        
    }
}