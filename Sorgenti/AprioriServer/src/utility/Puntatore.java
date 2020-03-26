
package utility;
/**
 * Modella il puntatore all'elemento successivo nella struttura dati lista linkata.
 * 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class Puntatore  {

	/**
	 * Riferimento ad una cella.
	 */
	public Cella link;

	/**
	 * Costruttore di un'istanza della classe Puntatore.
	 * @param c riferimento ad un oggetto di tipo Cella
	 */
	public Puntatore(Cella c) {
		link = c;
	}
}