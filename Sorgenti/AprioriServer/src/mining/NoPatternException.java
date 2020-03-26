
package mining;

/**
 * Modella un'eccezione personalizzata che occorre quando nessuna regola confidente &egrave; generata da un pattern frequente.
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class NoPatternException extends Exception{

	/**
	 * Costruttore dell'oggetto eccezione NoPatternException.
	 * Richiama il costruttore della superclasse Exception passandogli un riferimento ad un oggetto String contenente un messaggio di errore.
	 * @param mes oggetto String delineante un messaggio d'errore
	 */
	public NoPatternException(String mes) {
		super(mes);
	}
}
