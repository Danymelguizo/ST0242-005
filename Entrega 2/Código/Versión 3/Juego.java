

/**
 * Esta clase maneja el juego
 * Se tiene una referencia al tablero y al pacman
 * En esta clase se hace la interacción con el usuario
 * @author Helmuth Trefftz
 */
import java.util.Scanner;
import java.util.Random;

public class Juego {

    /**
     * El número de puntos iniciales de vida del pacman
     */
    public static final int PUNTOS_VIDA_INICIALES = 10;
    int vida = PUNTOS_VIDA_INICIALES;
    Tablero tablero;
    Pacman pacman;
    Fantasma fantasma1;
    Fantasma fantasma2;

    /** 
     * Constructor
     * Se crea un tablero
     */
    public Juego() {
        tablero = new Tablero(this);
    }

    /** 
     * Interacción con el usuario
     */
    public void jugar() {
        boolean ganaElJuego = false;
        tablero.dibujarTablero();   
        Scanner in = new Scanner(System.in);
        String linea = in.nextLine();
        Random arepiaExplosiva = new Random();
                    
        while (!ganaElJuego) {      
            int fila = pacman.posicion.fila;
            int col = pacman.posicion.col;
            int nuevaFila = fila;
            int nuevaCol = col;
            
            switch (linea) {
                // En este punto se inserta el código para las teclas
                // "a" y "d"
                case "w":   
                    nuevaFila = fila - 1;
                    break;
                case "s":
                    nuevaFila = fila + 1;
                    break;
                case "a":
                    nuevaCol = col - 1;
                    break;
                case "d":
                    nuevaCol = col + 1;
                    break;
                default :
                    nuevaCol = col;
                    nuevaFila = fila;
                    System.out.println("Movimiento invalido.");
                    break;       
            }
            
            if (validarCasilla(nuevaFila, nuevaCol)) {
                Celda anterior = tablero.tablero[fila][col];
                Celda nueva = tablero.tablero[nuevaFila][nuevaCol];
                if(nueva.tieneArepita){
                    int explota = 0;
                    int noExplota = 1;
                    int determina = arepiaExplosiva.nextInt(2);
                    if(determina == explota){
                        vida = vida - 0;
                    } else if(determina == noExplota && vida < 10){
                        vida = vida + 2;
                    }
                    anterior.tieneArepita = false;
                    nueva.tieneArepita = false;
                }
                anterior.caracter = null;
                nueva.caracter = pacman;
                pacman.posicion = new Posicion(nuevaFila, nuevaCol);
                // Aquí hay que verificar si el jugador ganó el juego
                // Esto es, si llega a una parte del laberinto
                // que es una salida
                if(nueva.esSalida == true){
                    ganaElJuego = true;
                    break;
                }
                if(vida <= 0){
                    ganaElJuego = false;
                    break;
                }
            }
            if(nuevaFila == fantasma1.posicion.fila && col == fantasma1.posicion.col){
                ganaElJuego = false;
                break;
            }
            if(nuevaCol == fantasma1.posicion.col && nuevaFila == fantasma1.posicion.fila){
                ganaElJuego = false;
                break;
            }
            if(nuevaFila == fantasma2.posicion.fila && col == fantasma2.posicion.col){
                ganaElJuego = false;
                break;
            }
            if(nuevaCol == fantasma2.posicion.col && nuevaFila == fantasma2.posicion.fila){
                ganaElJuego = false;
                break;
            }
            if(movimientoNPC(fantasma1) == true){
                ganaElJuego = false;
                break;
            }
            if(movimientoNPC(fantasma2) == true){
                ganaElJuego = false;
                break;
            }
            tablero.dibujarTablero();
            //System.out.print("Tienes "+vida+" de vida.");
            linea = in.nextLine();
        }
        if(ganaElJuego) {
            System.out.println("Has ganado el juego, ¡felicitaciones!");
        }
        if(!ganaElJuego){
            System.out.println("Has muerto.");
        }   
    }   

    /**
     * En este metodo se debe chequear las siguientes condiciones:
     * (i) Que el usuario no se salga de las filas del tablero
     * (ii) Que el usuario no se salga de las columnas del tablero
     * (iii) Que la posición no sea un muro
     * (iv) Que no haya un caracter en esa posición
     * 
     * @param nuevaFila Fila hacia donde se quiere mover el usuario
     * @param nuevaCol Columna hacia donde se quiere mover el usuario
     * @return true si es una jugada válida, false de lo contrario
     */
    public boolean validarCasilla(int nuevaFila, int nuevaCol) {
        // Aquí hay que verificar que sea un movimiento válido
        // Ver los comentarios del método
        boolean validarCasilla = true;
        if(tablero.tablero[nuevaFila][nuevaCol].caracterCelda() == '*'){
            return validarCasilla = false;
        } else if (tablero.tablero[nuevaFila][nuevaCol].caracterCelda() == 'M') {
            return validarCasilla = false;
        } else if (tablero.tablero[nuevaFila][nuevaCol].caracterCelda() == '^') {
            return validarCasilla = true;
        }
        return validarCasilla;
    }
    public boolean movimientoNPC(Fantasma fantasma){
        int fila = fantasma.posicion.fila;
        int col = fantasma.posicion.col; 
        int nuevaFila = fila;   
        int nuevaCol = col;
        boolean perdio = false;
        if(nuevaFila == pacman.posicion.fila && col == pacman.posicion.col){
            perdio = true;
        }
        if(nuevaCol == pacman.posicion.col && nuevaFila == pacman.posicion.fila){
            perdio = true;
        }
        if(fantasma.posicion.col == pacman.posicion.col) {
                    if (pacman.posicion.fila < fantasma.posicion.fila) {
                        nuevaFila = fila - 1;
                    } else if (pacman.posicion.fila > fantasma.posicion.fila) {
                        nuevaFila = fila + 1;
                    }
        }
        if (fantasma.posicion.fila == pacman.posicion.fila) {
                    if (pacman.posicion.col < fantasma.posicion.col) {
                        nuevaCol = col - 1;
                    } else if (pacman.posicion.col > fantasma.posicion.col) {
                        nuevaCol = col + 1;
                    }
        }
        if (validarCasilla(nuevaFila,nuevaCol)) {
                Celda anterior = tablero.tablero[fila][col];
                Celda nueva = tablero.tablero[nuevaFila][nuevaCol];
                anterior.caracter = null;
                nueva.caracter = fantasma;
                fantasma.posicion = new Posicion(nuevaFila,nuevaCol);
        }   
        return perdio;
    }        
}      
