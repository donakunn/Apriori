
package utility;

/**
 * Modella un singolo elemento della struttura dati lista collegata.
 * 
 * @author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */

class Cella {

	/**
	 * Valore contenuto nella cella corrente.
	 */
	Object elemento;

	/**
	 * Riferimento alla cella successiva.
	 */
	Puntatore successivo=null; 

	/**
	 * Costruttore di un'istanza della classe Cella; ne avvalora il contenuto con il valore del parametro passato in input.
	 * @param e Riferimento ad un'istanza Object 
	 */
	public Cella(Object e){
		elemento = e;
	}
}
