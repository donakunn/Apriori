
package utility;

/**
 * Modella una struttura dati lista linkata da usare come contenitore per i pattern frequenti e le regole confidenti.
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class LinkList {	

	/**
	 * Riferimento alla prima posizione della lista collegata.
	 */
	private Puntatore inizioLista = null;

	/**
	 * Controlla che la lista sia vuota  confrontando il riferimento al primo elemento della lista 
	 * con il riferimento nullo.
	 * @return valore booleano risultante dal suddetto confronto
	 */
	public boolean isEmpty() {
		return inizioLista == null;
	}

	/**
	 * Restituisce il riferimento alla prima posizione della lista corrente.
	 * @return riferimento al primo elemento della lista
	 */
	public Puntatore firstList() {
		return null;
	}	

	/**
	 * Verifica se l'elemento successivo &egrave; l'ultimo elemento della lista corrente; il riferimento al successivo 
	 * elemento della lista &egrave; confrontato con il riferimento nullo.
	 * @param p puntatore alla posizione successiva della lista
	 * @return valore booleano risultante dal confronto 
	 */
	public boolean endList(Puntatore p) {
		if (isEmpty()) return true;
		if (p == firstList())
			return inizioLista == null; 
		else
			return ((Puntatore)p).link.successivo == null; 
	}

	/**
	 * Legge un valore dalla posizione della lista corrente indicata.
	 * @param p posizione della lista da cui leggere un elemento
	 * @return valore contenuto nella posizione indicata da p
	 */
	public Object readList(Puntatore p) {
		if (isEmpty())
			throw new IndexOutOfBoundsException("Lista vuota");
		if (p == firstList())
			return inizioLista.link.elemento;
		else
			return ((Puntatore) p).link.successivo.link.elemento;
	}

	/**
	 * Aggiunge un elemento in testa alla lista.
	 * @param e elemento da aggiungere
	 */
	public void add(Object e) {
		Puntatore temp;

		if (!isEmpty()) {
			temp = inizioLista;
			inizioLista = new Puntatore(new Cella(e));
			inizioLista.link.successivo = temp;
		}
		else {
			inizioLista = new Puntatore(new Cella(e));
		}
	}

	/**
	 * Puntatore all'oggetto successivo nella lista rispetto a quello attuale.
	 * @param p Puntatore all'oggetto corrente
	 * @return Puntatore all'oggetto successivo della lista collegata
	 */
	public Puntatore succ(Puntatore p) {
		if (endList(p))
			throw new IndexOutOfBoundsException(
					"Posizione fine lista non valida");
		if (isEmpty())
			throw new IndexOutOfBoundsException("Lista vuota");
		if (p == firstList())
			return inizioLista;			
		else if (p == inizioLista)
			return inizioLista.link.successivo;
		else
			return ((Puntatore) p).link.successivo;
	}
}
