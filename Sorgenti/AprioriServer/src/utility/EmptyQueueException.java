
package utility;

/**
 * Modella l'eccezione che occorre quando si tenta di leggere/cancellare da una coda vuota. 
 * da un pattern di lunghezza 1.
 *                 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class EmptyQueueException extends Exception {

	/**
	 * Costruttore dell'oggetto eccezione EmptyQueueException.
	 * Richiama il costruttore della superclasse Exception passandogli un riferimento ad un oggetto String contenente un messaggio di errore.
	 * @param mes oggetto String delineante un messaggio d'errore
	 */		
	public EmptyQueueException(String mes) {
		super(mes);
	}
}
