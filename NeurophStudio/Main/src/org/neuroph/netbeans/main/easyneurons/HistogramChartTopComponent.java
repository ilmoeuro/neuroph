package org.neuroph.netbeans.main.easyneurons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.netbeans.api.settings.ConvertAsProperties;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.nugs.graph2d.Graph2DProperties;
import org.nugs.graph2d.Hist2D;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays the Histogram graph.
 */
@ConvertAsProperties(dtd = "-//org.neuroph.netbeans.main.easyneurons//Test//EN",
autostore = false)
public final class HistogramChartTopComponent extends TopComponent {

    private static HistogramChartTopComponent instance;
    /**
     * path to the icon used by the component and its open action
     */
    private static final String PREFERRED_ID = "HistogramChartTopComponent";

    /**
     * Component constructor. Initializes layout.
     */
    public HistogramChartTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(HistogramChartTopComponent.class, "CTL_HistogramChartTopComponent"));
        setToolTipText(NbBundle.getMessage(HistogramChartTopComponent.class, "HINT_HistogramChartTopComponent"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        jPanel1.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance. To obtain the singleton instance, use
     * {@link #findInstance}.
     */
    public static synchronized HistogramChartTopComponent getDefault() {
        if (instance == null) {
            instance = new HistogramChartTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the HistogramChartTopComponent instance. Never call
     * {@link #getDefault} directly!
     */
    public static synchronized HistogramChartTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(HistogramChartTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof HistogramChartTopComponent) {
            return (HistogramChartTopComponent) win;
        }
        Logger.getLogger(HistogramChartTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ONLY_OPENED;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    /**
     * Populates a bin vector with weights from the network. Calls
     * createChartPanel method and displays the histogram graph. Utilizes the
     * JFreeChart API for histogram graphing.
     *
     * @param nnet Network object to iterate over connections.
     */
    public void populateHistBins(NeuralNetwork nnet) {
        jPanel1.removeAll();
        ArrayList<List> bin = new ArrayList();
        int countConnections = 0;
        //iterate over the layers and connections pulling weights.
        for (int i = 0; i < nnet.getLayersCount(); i++) {
            for (Neuron neuron : nnet.getLayerAt(i).getNeurons()) {
                //Neuron n = (Neuron)it.next();
                List out = Arrays.asList(neuron.getWeights());
                countConnections += out.size();//count the number of connections
                bin.add(out);
            }
        }

        //array to hold weight values
        double[] values = new double[countConnections];
        int idx = 0;


        //iterate over the bin, grabbing the values of the weights
        for (int i = 0; i < bin.size(); i++) {
            List lst = bin.get(i);
            for (Iterator it = lst.iterator(); it.hasNext();) {
                values[idx] = Double.parseDouble(it.next().toString());
                idx++;
            }
        }

        //get the jfreechart
        jPanel1.add(createChartPanel(values));
        jPanel1.validate();
        jPanel1.getParent().repaint();

    }//end populateHistBins method

    /**
     * Create a the histogram chart. Accepts a vector list containing the binned
     * weight values for all connections in the network, and and int value
     * representing the number of connections in the network. Returns a
     * ChartPanel (JFreeChart API) to the caller, containing the histogram
     * graph.
     *
     * @param bin ArrayList object containing binned weights.
     * @param count Integer variable representing the number of connections.
     * @return panel Panel object containing the histogram of weights.
     */
    protected ChartPanel createChartPanel(double[] values) {
        String xaxis = "Weight";
        String yaxis = "Number";
        String plotTitle = "Network Weights Histogram";
        PlotOrientation orientation = PlotOrientation.VERTICAL;

        Graph2DProperties prop = new Graph2DProperties(plotTitle, xaxis, yaxis);
        prop.setOrientation(orientation);

        int numberOfBins = 50;

        Hist2D hist = new Hist2D();
        return hist.createChartPanel(values, numberOfBins, prop);
    }//end createFrame method
}