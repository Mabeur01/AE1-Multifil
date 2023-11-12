package AE1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * Classe que representa el procés de fabricació d'una peça Tetromino.
 */
public class Manufacture implements Runnable {
    private final BlockingQueue<String> cuaOrdres; 
    private final String tipus;  
    private final boolean escriureATextFile;  
    private final String nomFitxerSortida; 

    /**
     * Constructor de la classe Manufacture.
     *
     * @param tipus              Tipus de peça a fabricar.
     * @param escriureATextFile Indica si s'ha de escriure la informació a un fitxer de text.
     * @param nomFitxerSortida  Nom del fitxer de sortida.
     * @param cuaOrdres         Cua on es guardaran les ordres de fabricació.
     */
    public Manufacture(String tipus, boolean escriureATextFile, String nomFitxerSortida, BlockingQueue<String> cuaOrdres) {
        this.tipus = tipus;
        this.escriureATextFile = escriureATextFile;
        this.nomFitxerSortida = nomFitxerSortida;
        this.cuaOrdres = cuaOrdres;
    }

    /**
     * Mètode que representa el procés de fabricació d'una peça Tetromino.
     */
    @Override
    public void run() {
    	int tempsDeFabricació = obtenirTempsDeFabricació(tipus);

        long tempsInici = System.currentTimeMillis();
        long tempsFinal = tempsInici + tempsDeFabricació; 
        int iteracions = 0;

        while (System.currentTimeMillis() < tempsFinal) {
            iteracions++;
        }

        String marcaTemps = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String informacióPeça = tipus + "_" + marcaTemps;

        try {
            cuaOrdres.put(informacióPeça);  
            System.out.println(informacióPeça);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (escriureATextFile) {
            escriureAlFitxer(nomFitxerSortida, informacióPeça);
        }
    }

    /**
     * Obté el temps de fabricació en mil·lisegons segons el tipus de peça.
     *
     * @param tipus Tipus de peça.
     * @return Temps de fabricació en mil·lisegons.
     */
    public int obtenirTempsDeFabricació(String tipus) {
        switch (tipus) {
            case "I":
                return 1000;
            case "O":
                return 2000;
            case "T":
                return 3000;
            case "J":
            case "L":
                return 4000;
            case "S":
            case "Z":
                return 5000;
            default:
                return 0;
        }
    }

    /**
     * Escriu la informació de la peça a un fitxer de text.
     *
     * @param nomFitxer Nom del fitxer de sortida.
     * @param contingut Informació de la peça.
     */
    public void escriureAlFitxer(String nomFitxer, String contingut) {
        try (BufferedWriter escriptor = new BufferedWriter(new FileWriter(nomFitxer, true))) {
            escriptor.write(contingut);
            escriptor.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Completa la fabricació, escrivint les ordres a un fitxer de log.
     */
    public void completarFabricació() {
        String nomFitxer = "LOG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";
        escriureAlFitxer(nomFitxer, "Ordres de fabricació:");
        while (!cuaOrdres.isEmpty()) {
            String ordre = cuaOrdres.poll();
            escriureAlFitxer(nomFitxer, ordre);
        }
    }
}
