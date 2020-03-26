
package data;

import java.util.Iterator;

/**
 * Realizza un iteratore che itera sugli elementi della sequenza composta da un certo numero di valori reali 
 * tra loro equidistanti, detti "punti di taglio" o "cut points", e compresi tra l'estremo inferiore 
 * e quello superiore; i punti di taglio sono ottenuti attarverso un algoritmo di discretizzazione apposito.
 * 
 * @author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class ContinuousAttributeIterator implements Iterator<Float> {

	/**
	 * Estremo inferiore dell'intervallo in cui ricade il valore di un attributo continuo.
	 */
	private float min;

	/**
	 * Estremo superiore dell'intervallo in cui ricade il valore di un attributo continuo.
	 */
	private float max;

	/**
	 * Passo j-esimo dell'algoritmo di discretizzazione dell'intervallo di valori compresi tra l'estremo inferiore e quello superiore.
	 * Il suo valore è compreso tra o e numValues, con numValues maggiore di 1.
	 */	
	private int j=0;

	/**
	 * Numero dei punti di taglio che discretizzano l'intervallo di valori compreso tra l'estremo inferiore e quello superiore.
	 */
	private int numValues;

	/**
	 * Costruttore; avvalora i membri attributo della classe con i parametri in input.
	 * @param min estremo inferiore dell'intervallo di discretizzazione
	 * @param max estremo superiore dell'intervallo di discretizzazione
	 * @param numValues numero dei punti di taglio
	 */
	ContinuousAttributeIterator(float min,float max,int numValues){
		this.min=min;
		this.max=max;
		this.numValues=numValues;
	}

	/**
	 * Determina se vi sono elementi al seguito dell'elemento correntemente puntato dall'iteratore.
	 * Definisce un corpo per il metodo astratto boolean hasNext() dell'interfaccia generica Iterator&lt;T&gt;.
	 * @return true se j &lt;&#61; numValues, false altrimenti
	 */
	public boolean hasNext() {
		return (j<=numValues);
	}

	/**
	 * Incrementa j, restituisce il cut point in posizione j-1 ( cut point: (max-min) / numValues*(j-1) ).
	 * Dettagli sull'algoritmo di discretizzazione di un intervallo di valori reali sono qui di seguito riportati:
	 * 			
	 * 		Sia dato il numero intero numValues &gt; 1, i valori reali min e max, estremi dell'intervallo [min, max[; tra i due estremi 
	 * 		sussiste la seguente relazione d'ordine:
	 * 		
	 * 				min &lt; max
	 * 		
	 * 		L'algoritmo di dscretizzazione dell'intervallo suddetto genera, nell'ordine, i seguenti punti discreti:
	 * 	
	 * 				min + 1*(max-min)/numValues
	 *				min + 2*(max-min)/numValues
	 *   			min + 3*(max-min)/numValues
	 *				....
	 *				min + (numValues - 1)*(max-min)/numValues
	 *
	 *In generale, al passo j-esimo otteniamo il punto di taglio min + (j-1)*(max-min)/numValues.
	 * Definisce un corpo per il metodo T next() dell'interfaccia generica Iterator&lt;T&gt;.
	 * @return numero reale successivo
	 */
	public Float next() {
		j++;
		return min+((max-min)/numValues)*(j-1);
	}

	/**
	 * Rimuove l'ultimo elemento puntati dall'iteratore corrente.
	 * Definisce un corpo per il metodo void remove() dell'interfaccia generica Iterator&lt;T&gt;
	 * Corpo attualmente non creato.
	 */
	public void remove() {
	}
}
