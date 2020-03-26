
package data;

/**
 * Modella un'eccezione personalizzata, che occorre quando l'insieme di training &egrave; vuoto.
 * 
 * @author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class EmptySetException extends Exception {

	/**
	 * Costruttore dell'oggetto eccezione EmptySetException: richiama il costruttore della superclasse Exception passandogli un riferimento ad un oggetto String contenente un messaggio di errore.
	 * @param mes oggetto String delineante un messaggio d'errore
	 */	
	public EmptySetException(String mes) {
		super(mes);
	}
}
