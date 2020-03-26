
package mining;

/**
 * Modella un'eccezione personalizzata che occorre quando si tenta di creare una regola d'associazione 
 * da un pattern di lunghezza 1. 
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class OneLevelPatternException extends Exception {

	/**
	 * Costruttore dell'oggetto eccezione OneLevelPatternException.
	 * Richiama il costruttore della superclasse Exception passandogli un riferimento ad un oggetto String contenente un messaggio di errore.
	 * @param mes oggetto String delineante un messaggio d'errore
	 */	
	public OneLevelPatternException(String mes) {
		super(mes);
	}
}
