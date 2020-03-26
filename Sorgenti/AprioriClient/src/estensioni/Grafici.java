package estensioni;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 * Si occupa della creazione di un grafico di tipo curva di precisione, 
 * riportante la distribuzione delle frequenze assolute di pattern frequenti
 * rispetto al supporto minimo, e la distribuzione delle frequenze assolute
 * delle regole d'associazione rispetto alla loro confidenza. Le distribuzioni
 * sono presentate in un'unico grafico, con colori diversi che delineano le due diverse
 * distribuzioni, che pure hanno lo stesso dominio di valori, essendo supporto minimo
 * e confidenza minima due numeri reali. In corrispondenza dell'intervallo delle ascisse 
 * sono riportati gli intervalli in cui ricadono i valori di supporto e di confidenza
 * di ciascun pattern e regola d'associazione, rispettivamente.
 *                 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni                 
 */
public class Grafici extends JFrame {

	/**
	 * Costruttore; si occupa di creare un frame (o finestra) nel quale riportare il grafico, 
	 *              impostandone le caratteristiche grafiche, le dimensioni ed il contenuto.
	 *              Se occorrono errori nella generazione del grafico, un'eccezione di tipo 
	 *              IOException &egrave; catturata e gestita emettendo un apposito messaggio.
	 * @param title Titolo del grafico
	 * @param suppCount vettore contenente le frequenze assolute dei pattern frequenti
	 * @param confCount vettore contenente le frequenze assolute delel regole d'associazione confidenti
	 */
	public Grafici(final String title, int suppCount[], int confCount[]) {
		super(title);
		ChartPanel Panel;
		try {
			Panel = new ChartPanel(createChart(suppCount,confCount));
			JFrame f = new JFrame(title);
			f.setTitle(title);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setLayout(new BorderLayout(0, 5));
			f.add(Panel, BorderLayout.CENTER);
			Panel.setMouseWheelEnabled(true);
			Panel.setPreferredSize(new java.awt.Dimension(800, 600));
			setContentPane(Panel);
		} catch (IOException e) {						
			JOptionPane.showMessageDialog(null, "Errore nella generazione del grafico","ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Permette di generare un Dataset contenente una ( o pi&ugrave; ) serie di dati, in base ai quali
	 * sar&agrave; poi generato un'apposito grafico riportante i valori delle suddette serie.
	 * @param suppCount vettore contenente le frequenze assolute dei pattern frequenti
	 * @param confCount vettore contenente le frequenze assolute delel regole d'associazione confidenti
	 * @return Dataset contenente una o pi&ugrave; serie di dati
	 */
	public CategoryDataset createDataset1(int suppCount[], int confCount[]) {

		final DefaultCategoryDataset result = new DefaultCategoryDataset();

		final String supp = "Pattern Frequenti ";
		final String conf = "Regole d'associazione Confidenti";
		final String type = "0.";

		for (int i=0, j=1; i<10; i++,j++) {
			if ( j==10) { 
				result.addValue(suppCount[i], supp, type + i+"-"+ "1.0");
			}
			else {
				result.addValue(suppCount[i], supp, type + i+"-"+ type + j);
			}
		}

		for (int i=0, j=1; i<10; i++,j++) {
			if ( j==10) { 
				result.addValue(confCount[i], conf, type + i+"-"+ "1.0");
			}
			else {
				result.addValue(confCount[i], conf, type + i+"-"+ type +j );
			}
		}

		return result;
	}

	/**
	 * Si occupa di creare il grafico vero e proprio, impostandone gli assi orizzontale e verticale
	 * e il rendering, basandosi inoltre su un Dataset di valori precedentemente creato.
	 * Inoltre, il metodo si occupa di convertire il grafico in forma di array di byte e invia tale
	 * informazione al server.
	 * @param suppCount array contenente le frequenze assolute dei pattern frequenti, rilevate rispetto al minimo supporto
	 * @param confCount array contenente le frequenze assolute delle regole d'associazione confidenti, rilevate rispetto
	 *        alla minima confidenza.
	 * @return Grafico a curva di precisione
	 * @throws IOException errore di I/O inerente lo/gli stream
	 */
	private JFreeChart createChart(int suppCount[], int confCount[]) throws IOException {

		final CategoryDataset dataset = createDataset1(suppCount,confCount);
		final NumberAxis range = new NumberAxis("Numero Pattern Frequenti/Regole di Associazione confidenti");
		range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		final LineAndShapeRenderer lineRender = new LineAndShapeRenderer();
		lineRender.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		final CategoryPlot catePlot = new CategoryPlot(dataset, null, range, lineRender);
		catePlot.setDomainGridlinesVisible(true);

		final CategoryAxis dominio = new CategoryAxis("Intervalli di Supporto/Confidenza");
		final CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot(dominio);
		plot.add(catePlot, 2);

		final JFreeChart Grafico = new JFreeChart("Curva di Precisione ",new Font("Calibri", Font.BOLD, 14),plot,true);
		BufferedImage objBufferedImage=Grafico.createBufferedImage(600,800);
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		try {
			ImageIO.write(objBufferedImage, "png", bas);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Errore nella generazione del File","ERROR", JOptionPane.ERROR_MESSAGE);
		}
		byte[] byteArray=bas.toByteArray();

		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage image = ImageIO.read(in);
		File outputfile = new File("image.png");
		ImageIO.write(image, "png", outputfile);

		return Grafico;
	}
}