import java.text.DecimalFormat;
import java.util.*;
import java.util.Scanner;

public class Simplexe{

    String[] valeurs_ajoutees;
    String[] bases;
    double[][] E;
    double[] z;
    String[] variableZ;
    
//simplexe à 2 phases
    
//associer chaque ligne a la ligne pivot
    public void association_linge_pivot(int ligne, int colonne){
        double pivot = E[ligne][colonne];
        double[][] newmat = new double[E.length][E[0].length];
        for(int i = 0; i < E.length; i++){
            if(i != ligne){
                for(int j = 0; j < E[i].length; j++){
                    newmat[i][j] = E[i][j] - ((E[i][colonne] / pivot) * E[ligne][j]);
                }
            }
        }
        newmat[ligne] = E[ligne];
        E = newmat;
    } 
//calcul de Z en phase 1
    public double Z(){
        double z = 0;
        for(int i=0; i<bases.length; i++){
            if(bases[i].contains("a")){
                z += this.E[i][E[i].length-1];
            }
        }
        return z;
    }
//normalisation du pivot
    public void transformation_pivot(int ligne, int colonne){
        double pivot = E[ligne][colonne];
        for(int i = 0; i < E[ligne].length; i++){
            E[ligne][i] = E[ligne][i] / pivot; 
        }
    }
//1ere etape, recherche de la valeur positive la plus grande
    public int indice_valeur_maximum_positif(double[] valeurs){
        int Max = -1;
        for(int i = 0; i < valeurs.length - 1; i++){
            if (valeurs[i] > Max && valeurs[i] > 0){
                Max = i;
            }
        }
        return Max;
    }
    public int indice_valeur_maximum_negatif2(double[] valeurs){
        double Max = 999999;
        int indice = 999999;
        for(int i = 0; i < valeurs.length - 1; i++){
            if(valeurs[i] < Max){
                System.out.println("ligne " + Max);
                Max = valeurs[i];
                indice = i;
            }
        }
        return indice;
    }
    public int indice_valeur_maximum_negatif(double[] valeurs){
        double Max = 999999;
        int indice = 999999;
        for(int i = 0; i < valeurs.length - 1; i++){
            if(valeurs[i] < 0 && valeurs[i] < Max){
                System.out.println("ligne " + Max);
                Max = valeurs[i];
                indice = i;
            }
        }
        return indice;
    }
//minimum positif
    public int rapport_minimum_positif(int laharana_ngeza_indrindra){
//valeur solution
        int indice = 0;
        int laharana_solution = E[0].length - 1;

        double rapportinitiale = 99999999;
        for(int i = 0; i < E.length - 1; i++){
            if(E[i][laharana_ngeza_indrindra] >  0){
                double rapport = (double) E[i][laharana_solution] / E[i][laharana_ngeza_indrindra];
                if(rapportinitiale > rapport && rapport > 0){
                    indice = i;
                    rapportinitiale = rapport;
                }
                if(rapport == (double) 0){
                    return i;
                }
            }
        } 
        return indice;
    }

    public boolean isInArray(String value){
        boolean r = false;
        for(int i = 0; i < this.bases.length; i++){
            if(this.bases[i].contains(value)){
                
                return true;
            }
        }
        System.out.println("Il n' y a plus de variable aritificielle");
        return r;
    }
   
    public void getVariables(){
        Vector<Integer> indicesVariableArtificielle = new Vector<Integer>();   
        
        for(int i = 0; i < this.valeurs_ajoutees.length; i++) {
            System.out.println(this.valeurs_ajoutees[i]+"mist");
            if(this.valeurs_ajoutees[i].contains("a")){
                System.out.println(this.valeurs_ajoutees[i]+"mist");
                indicesVariableArtificielle.add(i);
            }
        }
        //Integer[] indices = indicesVariableArtificielle.toArray();
        int[] tab  = new int[indicesVariableArtificielle.size()];
        for(int i = 0; i < indicesVariableArtificielle.size(); i++){
            tab[i] = indicesVariableArtificielle.get(i);
        }
        //System.out.println(tab[0]+"fdsfcle");
        this.E = Simplexe.supprimerColonnes(this.E, tab);
    }

    public static double[][] supprimerColonnes(double[][] tableau, int[] indicesColonnes) {
        int lignes = tableau.length;
        int colonnes = tableau[0].length;
        int nouvellesColonnes = colonnes - indicesColonnes.length;

        // Vérifier si les indices de colonnes sont valides
        for (int indiceColonne : indicesColonnes) {
            if (indiceColonne < 1 || indiceColonne >= colonnes) {
                throw new IllegalArgumentException("Indice de colonne invalide : " + indiceColonne);
            }
        }

        // Création d'un ensemble contenant les indices des colonnes à supprimer
        Set<Integer> indicesASupprimer = new HashSet<>();
        for (int indiceColonne : indicesColonnes) {
            indicesASupprimer.add(indiceColonne);
        }

        // Création du nouveau tableau avec les colonnes à supprimer
        double[][] nouveauTableau = new double[lignes][nouvellesColonnes];
        int nouvelleColonne = 0;

        for (int colonne = 0; colonne < colonnes; colonne++) {
            if (!indicesASupprimer.contains(colonne)) {
                for (int ligne = 0; ligne < lignes; ligne++) {
                    nouveauTableau[ligne][nouvelleColonne] = tableau[ligne][colonne];
                }
                nouvelleColonne++;
            }
        }

        return nouveauTableau;
    }

    public void Phase1_Minimisation() throws Exception{
        System.out.println("z = " + Z());
        int ligne_farany = E.length - 1;
        //indice valeur negatif kely indrindra 
        int laharana_ngeza_indrindra = indice_valeur_maximum_negatif(E[ligne_farany]);
        System.out.println("laharana_ngeza_indrindra = " + laharana_ngeza_indrindra);
        //maka rapport minimum positif
        int ligne_ngeza = rapport_minimum_positif(laharana_ngeza_indrindra); 
        
        System.out.println("ligne ngeza_ngeza = " + ligne_ngeza);

        String entree = valeurs_ajoutees[laharana_ngeza_indrindra];
        
        bases[ligne_ngeza] = entree;

        transformation_pivot(ligne_ngeza, laharana_ngeza_indrindra);
        association_linge_pivot(ligne_ngeza, laharana_ngeza_indrindra);
        int arret = indice_valeur_maximum_negatif(E[ligne_farany]);
        
        double max_z = Z();
        this.E[E.length - 1][E[0].length - 1] = max_z;
        System.out.println("arret = " + max_z);

        this.printE();
        if(max_z != 0.0 || isInArray("a")){
            if(arret == 999999){
                throw new Exception("No solution found");
            }
            this.Phase1_Minimisation();
        }
    }
    
    public void Maximisation(){
        int ligne_farany = E.length - 1;
        int laharana_ngeza_indrindra = indice_valeur_maximum_positif(E[ligne_farany]);

        int ligne_ngeza = rapport_minimum_positif(laharana_ngeza_indrindra); 

        String sortie = bases[ligne_ngeza];
        String entree = valeurs_ajoutees[laharana_ngeza_indrindra];
        
        bases[ligne_ngeza] = entree;

        transformation_pivot(ligne_ngeza, laharana_ngeza_indrindra);

        association_linge_pivot(ligne_ngeza, laharana_ngeza_indrindra);

        int arret = indice_valeur_maximum_positif(E[ligne_farany]);
        
        this.printE();
        if(arret >= 0){
            this.Maximisation();
        }
    }

    public void Minimisation() throws Exception{
        int ligne_farany = E.length - 1;

        int laharana_ngeza_indrindra = indice_valeur_maximum_negatif(E[ligne_farany]);
        int ligne_ngeza = rapport_minimum_positif(laharana_ngeza_indrindra); 

        String entree = valeurs_ajoutees[laharana_ngeza_indrindra];
        
        bases[ligne_ngeza] = entree;

        transformation_pivot(ligne_ngeza, laharana_ngeza_indrindra);
        association_linge_pivot(ligne_ngeza, laharana_ngeza_indrindra);
        int arret = indice_valeur_maximum_negatif(E[ligne_farany]);
        
        this.printE();
        if(arret != 999999){
            this.Minimisation();
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
                int decimalPlaces = 3;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(decimalPlaces);
                String nb = df.format(E[i][j]);
                System.out.print(completervide(nb+""));
            }
            System.out.println(" ");
            System.out.println(trait(15, valeurs_ajoutees.length));
            System.out.println(" ");
        }
        double max_z = E[E.length-1][E[0].length-1] * -1;
        System.out.println("Z = " + max_z);
    }

    
    public Vector<Integer> getIndiceVariableAleatoire(){
        Vector<Integer> integers = new Vector<Integer>();
        for (int i = 0; i < this.valeurs_ajoutees.length; i++) {
            if(this.valeurs_ajoutees[i].contains("a")){
                integers.add(i);
            }
        }
        return integers;
    }
    public static boolean isInArray(int i, Vector<Integer> array){
        for (int j = 0; j < array.size(); j++) {
            if(array.get(j) == i){
                return true;
            }
        }
        return false;
    }
    //apres 1ere phase
    public void refactorisation(){
        Vector<Integer> indices = getIndiceVariableAleatoire();
        double[][] matrice = new double[this.bases.length][this.valeurs_ajoutees.length - indices.size()];
        for (int i = 0; i < this.E.length - 1; i++) {
            Vector<Double> ligne = new Vector<Double>();
            for (int j = 0; j < this.valeurs_ajoutees.length; j++) {
                if(!isInArray(j, indices)){
                    ligne.add(this.E[i][j]);
                }
            }
            double[] ligne_convertie = listeDoubleEnTableau(ligne);
            matrice[i] = ligne_convertie;
        }
        this.E = matrice;

        Vector<String> newvariablesAjoutees = new Vector<String>();
        for (int i = 0; i < this.valeurs_ajoutees.length; i++) {
            if(!isInArray(i, indices)){
                newvariablesAjoutees.add(this.valeurs_ajoutees[i]);
            }
        }
        this.valeurs_ajoutees = listeStringEnTableau(newvariablesAjoutees);
        this.replaceLastLigne();
    }
    public static double[] listeDoubleEnTableau(Vector<Double> vector){
        double[] doubles = new double[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            doubles[i] = vector.get(i);
        }
        return doubles;   
    }
    public static String[] listeStringEnTableau(Vector<String> vector){
        String[] strings = new String[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            strings[i] = vector.get(i);
        }
        return strings;
    }
    public void replaceLastLigne(){
        double[] newLigne = new double[this.E[0].length];
        int i = 0;
        while(i < newLigne.length){
            System.out.print(this.getVA()[i]+"= ");
            float value = 0;
            Scanner scanner = new Scanner(System.in);
                try{
                    value = scanner.nextFloat();
                }
                catch(Exception e){
                    value = scanner.nextFloat();
                }
            
            newLigne[i] = value;
            i += 1;
        }
        this.E[this.E.length - 1] = newLigne;
    }
    public void refactorZ(){
        for(int i = 0; i < this.bases.length; i ++){
            for(int j = 0; j < this.getVariableZ().length; i++){
                if(this.bases[i].contains(this.getVariableZ()[j])){
                    for(int k = 0; k < this.getZ().length; i++){
                        this.getZ()[j] = this.getZ()[j] - this.E[i][k]*(this.getZ()[j] - this.E[i][k]);
                    }
                    this.getZ()[j] = this.E[i][this.E[i].length - 1];
                }
            }
        }
    }
    public void printVZ(){
        for (double ds : this.getZ()) {
            System.out.println(ds);
        }
    }
    public Simplexe(){}
    public Simplexe(double[][] E){
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

    public void setE(double[][] E){
        this.E = E;
    }

    public double[][] getE(){
        return this.E;
    }

    public void setVariableZ(String[] variableZ) {
        this.variableZ = variableZ;
    }
    public String[] getVariableZ() {
        return variableZ;
    }
    public void setZ(double[] z) {
        this.z = z;
    }
    public double[] getZ() {
        return z;
    }
    
    public static void main(String[] args) throws Exception{
        
        String[] va = { "x1", "x2", "e1", "e2" , "e3","solutions" };

        double[] l1 = { -4, 6, 1, 0, 0, 9 };
        double[] l2 = { 1, 1, 0, 1, 0, 4 };
        double[] l3 = { 0, 1, 0, 0, 1, 2 };
        double[] l4 = { 1, -2, 0, 0, 0,0 };

        double[][] E = { l1, l2, l3, l4};

        String[] bases = { "e1", "e2", "e3", "A"};

        Simplexe Maximisation = new Simplexe();
        Maximisation.setBases(bases);
        Maximisation.setE(E);
        Maximisation.setVA(va);
  /*       
        double[] z = { 1, 2};
        String[] variableDeZ = { "x1", "x2" };
*/
       // Maximisation.setZ(z);
        Maximisation.setVariableZ(va);
        Maximisation.setVA(va);
        Maximisation.setBases(bases);
        Maximisation.printE();
        //x1 + x2
        // x1 -0.273e1 + 0.364e2 + 2
        //Maximisation.Phase1_Minimisation();
        //Maximisation.refactorisation();
        Maximisation.printE();
        System.out.println("Phase 2");
        Maximisation.Minimisation();
    }
}