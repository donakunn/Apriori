
package utility;
/**
 * Classe generica che modella una struttura coda, nonch&egrave; contenitore con politica FIFO ( First In First Out ) 
 * per i pattern frequenti scoperti a livello k e da usare per generare i pattern candidati a livello k+1.
 *                 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class Queue<T> {

	/**
	 * Punta all'elemento di testa della coda.
	 */
	private Record begin = null;

	/**
	 * Punta all'ultimo elemento della coda.
	 */
	private Record end = null;

	/**
	 *Classe interna ( inner class ), ausiliaria per la definizione della classe Queue.
	 */
	private class Record {

		/**
		 * Delinea il valore del record corrente.
		 */
		public T elem;

		/**
		 * Riferimento al record successivo.
		 */
		public Record next;

		/**
		 * Costruttore di un'istanza della classe Record; ne avvalora il contenuto con il valore e di tipo generico T passato in input.
		 * @param e riferimento ad un oggetto di tipo T generico
		 */
		public Record(T e) {
			this.elem = e; 
			this.next = null;
		}
	}

	/**
	 * Controlla se la coda &egrave; vuota. In particolare confronta il riferimento al primo elemento della coda con il riferimento nullo
	 * e restituisce l'esito di tae confronto.
	 * @return Valore booleano risultante dal confronto suddetto
	 */
	public boolean isEmpty() {
		return this.begin == null;
	}

	/**
	 * Permette l'inserimento di un elemento nell'ultima posizione della coda.
	 * @param e Elemento da inserire nella coda
	 */
	public void enqueue(T e) {
		if (this.isEmpty())
			this.begin = this.end = new Record(e);
		else {
			this.end.next = new Record(e);
			this.end = this.end.next;
		}
	}

	/**
	 * Legge il valore dell'elemento di testa della coda.
	 * @return Valore dell'elemento di test della coda
	 * @throws EmptyQueueException lettura da una coda vuota
	 */
	public T first() throws EmptyQueueException{
		if(this.begin==null) throw new EmptyQueueException("Can't read from an empty Queue!");

		else
			return this.begin.elem;
	}

	/**
	 * Permette di cancellare l'elemento di testa della coda
	 *  @throws EmptyQueueException lettura da una coda vuota
	 */
	public void dequeue() throws EmptyQueueException{
		if(this.begin==this.end){									
			if(this.begin==null) throw new EmptyQueueException("Queue is empty!");
			else
				this.begin=this.end=null;
		}
		else{
			begin=begin.next;
		}
	}
}