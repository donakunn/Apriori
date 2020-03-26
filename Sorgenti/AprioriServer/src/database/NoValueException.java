
package database;

/**
 * Modella un'eccezione personalizzata che occorre quando alcun valore &egrave; presente all'interno del resultset.
 *                 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class NoValueException extends Exception {

	/**
	 * Costruttore dell'oggetto eccezione NoValueException.
	 * Richiama il costruttore della superclasse Exception passandogli un riferimento ad un oggetto String contenente un messaggio di errore.
	 * @param mes oggetto String delineante un messaggio d'errore
	 */
	public NoValueException(String mes){
		super(mes);
	}
}