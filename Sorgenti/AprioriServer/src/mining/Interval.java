
package mining;

import java.io.Serializable;

/**
 * Modella un intervallo di valori reali chiuso a sinistra ed aperto a destra.
 * Implementa l'interfaccia Serializable in modo che le sue istanze siano serializzabili 
 * in flussi di byte che le rappresentino.
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class Interval implements Serializable{

	/**
	 * Estremo inferiore dell'intervallo istanza della classe corrente.
	 */
	private float inf;

	/**
	 * Estremo superiore dell'intervallo istanza della classe corrente.
	 */
	private float sup; 

	/**
	 * Avvalora gli estremi superiore ed inferiore dell'intervallo chiamando i metodi setInf() e setSup().
	 * che ricevono in input i parametri del costruttore.
	 * @param inf estremo inferiore dell'istanza corrente di Interval
	 * @param sup estremo superiore dell'istanza corrente di Interval
	 */
	Interval(float inf, float sup){
		setInf(inf);
		setSup(sup);
	}

	/**
	 * Avvalora l'estremo inferiore dell'istanza corrente di Interval.
	 * @param inf estremo inferiore dell'intervallo
	 */
	private void setInf(float inf){
		this.inf=inf;
	}

	/**
	 * Avvalora l'estremo superiore dell'istanza corrente di Interval.
	 * @param sup estremo superiore dell'intervallo
	 */
	private void setSup(float sup){
		this.sup=sup;
	}

	/**
	 * Restituisce l'estremo inferiore dell'istanza corrente di Interval.
	 * @return Estremo inferiore dell'Interval corrente
	 */
	protected float getInf(){
		return inf;
	}

	/**
	 * Restituisce l'estremo superiore dell'istanza corrente di Interval.
	 * @return Estremo superiore dell'Interval corrente
	 */
	protected float getSup(){
		return sup;
	}

	/**
	 * Restituisce il valore di verit&agrave; true se il parametro &egrave; maggiore od uguale ad inf, false altrimenti
	 * @param value Valore assunto da un attributo continuo per cui verificare l'appartenenza all'intervallo.
	 * @return true se value >= inf, false altrimenti
	 */
	boolean checkValueInclusion(float value){
		return(value>=inf && value<sup) ? true: false;
	}

	/**
	 * Restituisce un oggetto String contenente una stringa di testo ottenuta concatenando gli estremi dell'intervallo. Per esempio:
	 * 		
	 * 		[ min, max [
	 * 
	 * dove min max sono astrazioni dei reali valori assunti dagli stessi estremi. Si noti che l'intervallo &egrave; limitato e chiuso 
	 * a sinistra ed aperto a destra.
	 * 
	 * @return Stringa rappresentante un intervallo di valori reali
	 */
	public String toString(){
		String value = "";		
		return value+="["+ getInf() +","+ getSup() +"[";
	}
}
