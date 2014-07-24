package wcs;

import static java.lang.System.out;
import java.awt.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class Configurator extends JFrame implements ActionListener {

	final static String[] labels = { "Sites or JSK Folder: ",
			"Webapp Folder: ", "CSDT Jar:" };

	final static String SELECT_FOLDER = "Select Folder App";
	final static String SELECT_FOLDER2 = "Select Folder WebApp";
	final static String SELECT_JAR = "Select JAR";
	final static String CONFIGURE = "Configure";

	JButton[] buttons = { //
	new JButton(SELECT_FOLDER), //
			new JButton(SELECT_FOLDER2), //
			new JButton(SELECT_JAR), //
			new JButton(CONFIGURE) };

	JTextField[] textfields = { new JTextField(35), new JTextField(35),
			new JTextField(35) };

	JFileChooser fileChooser = new JFileChooser();
	JFileChooser dirChooser = new JFileChooser();

	public Configurator() {

		// beginning
		setTitle("AgileSites Configurator");
		int numPairs = labels.length;

		// Create and populate the panel.
		JPanel p = new JPanel(new SpringLayout());
		for (int i = 0; i < numPairs; i++) {
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			p.add(l);
			JTextField textField = textfields[i];
			textField.setEditable(false);
			l.setLabelFor(textField);
			p.add(textField);
			JButton button = buttons[i];
			button.setActionCommand(labels[i]);
			button.addActionListener(this);
			p.add(button);
		}

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(p, numPairs, 3, // rows, cols
				6, 6, // initX, initY
				6, 6);

		fileChooser.addActionListener(this);
		dirChooser.addActionListener(this);
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// lay out and show
		getContentPane().add(p);
		getContentPane().add(buttons[buttons.length - 1], BorderLayout.SOUTH);
		buttons[buttons.length - 1].setEnabled(false);
		buttons[buttons.length - 1].addActionListener(this);
		buttons[buttons.length - 1].setActionCommand(CONFIGURE);

		pack();

		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();

		if (cmd.equals(labels[0]))
			selectFolder0();
		else if (cmd.equals(labels[1]))
			selectFolder1();
		else if (cmd.equals(labels[2]))
			selectJar();
		else if (ae.getSource() == buttons[buttons.length - 1])
			configure();
	}

	Properties iniFile = null;
	String jarFile = null;
	String webApp = null;
	String version = "unknown";
	String site = null;
	String defaultpw = null;

    public Configurator(String topdir) {
    	defaultpw = "xceladmin";
    	searchIniJar(new File(topdir));
    }

	private void selectFolder0() {
		if (dirChooser.showDialog(this, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		textfields[0].setText(dirChooser.getSelectedFile().getAbsolutePath());
		searchIniJar(dirChooser.getSelectedFile());
		if (webApp != null) {
			textfields[1].setText(webApp);
		}
		if (jarFile != null) {
			textfields[2].setText(jarFile);
		}
		if (iniFile == null)
			JOptionPane.showMessageDialog(
							this,
							"The folder is not a JSK/Fatwire/Sites installation folder.",
							"WARNING", JOptionPane.ERROR_MESSAGE);
		enableConfigure();
	}

	private void selectFolder1() {
		if (dirChooser.showDialog(this, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		textfields[1].setText(dirChooser.getSelectedFile().getAbsolutePath());

		File xcel = new File(new File(textfields[1].getText()), "Xcelerate");

		if (!xcel.exists())
			JOptionPane.showMessageDialog(this,
					"The folder is not a JSK/Fatwire/Sites webapp folder.",
					"WARNING", JOptionPane.ERROR_MESSAGE);
		else
			webApp = textfields[1].getText();

		enableConfigure();
	}

	private void selectJar() {
		if (fileChooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION) {
			jarFile = fileChooser.getSelectedFile().getAbsolutePath();
			textfields[1].setText(jarFile);
			if (jarFile == null || !jarFile.endsWith(".jar")) {
				JOptionPane.showMessageDialog(this, "The file is not a jar.",
						"WARNING", JOptionPane.ERROR_MESSAGE);
			}
			enableConfigure();
		}
	}

	private void enableConfigure() {
    out.println();
		out.println();
		out.println("CS version: " + version);
		out.println("CS Home   : " + iniFile.getProperty("CSFTAppServerRoot"));
		out.println("CS Shared : " + iniFile.getProperty("CSInstallSharedDirectory"));		
		out.println("CS WebApp : " + webApp);
		out.println("CSDT jar  : " + jarFile);

		if (iniFile != null && jarFile != null && webApp != null) {
			buttons[buttons.length - 1].setEnabled(true);
		}
	}

	private void searchIniJar(File file) {
		for (File son : file.listFiles()) {
			// out.println(""+son);
			if (son.isDirectory()) {
			  out.print(".");
				searchIniJar(son);
			} else if (son.getName().equals("omii.ini")) {
				if(!son.getParentFile().getName().equalsIgnoreCase("ominstallinfo"))
				  continue;		
				System.out.print("#");
				iniFile = new Properties();
				try {
					iniFile.load(new FileReader(son.getAbsolutePath()));
					// try to locate the webapp folder for tomcat
					if (iniFile.getProperty("CSInstallAppServerType").startsWith("tomcat")) {
						webApp = iniFile.getProperty("CSInstallAppServerPath").replace('\\', '/');
						if(!webApp.endsWith("/")) webApp = webApp + "/";
						webApp = webApp + "webapps" + iniFile.getProperty("sCgiPath");
					}
					// try to locate the webapp folder for weblogic
					if (iniFile.getProperty("CSInstallAppServerType").startsWith("wls")) {
						String jsproot = iniFile.getProperty("csjsproot");
						if (jsproot!=null) {
							webApp = new File(jsproot).getParentFile().getAbsolutePath().replace('\\', '/');
					    	if(!webApp.endsWith("/")) webApp = webApp + "/";
					    }
					}
					// try to set the home folder
					if(iniFile.getProperty("CSFTAppServerRoot")==null) 
				               iniFile.setProperty("CSFTAppServerRoot",  
                                   son.getParentFile().getParentFile().getAbsolutePath());
				} catch (Exception e) {
					iniFile = null;
					e.printStackTrace();
				}
			} else if (son.getName().equals("omproduct.ini")) {
				System.out.print("#");
				try {
					Properties productFile = new Properties();
					productFile.load(new FileReader(son.getAbsolutePath()));
					version = productFile.getProperty("Version0");
				} catch (Exception e) {
					iniFile = null;
					e.printStackTrace();
				}		    

			} else if (son.getName().endsWith(".jar")
					&& son.getName().startsWith("csdt-client")) {
				System.out.print("#");
				jarFile = son.getAbsolutePath();

				// heuristic to choose the right version
				if(jarFile.endsWith("-1.2.2.jar") || jarFile.endsWith("-1.2.jar"))
					version = "11.1.1.6.0";
				if(jarFile.endsWith("-11.1.1.8.0.jar"))
					version = "11.1.1.8.0";

			}
		}
	}

	String q(String s) {
		StringBuffer sb = new StringBuffer();
		sb.append("\"");
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '"')
				sb.append("\\\"");
			else if (s.charAt(i) == '\\')
				sb.append("\\\\");
			else
				sb.append(s.charAt(i));
		sb.append("\"\n");
		return sb.toString();
	}

	private void configure() {

		String pw = defaultpw;
		if(pw==null) 
			     pw = JOptionPane.showInputDialog(this,
				"Password for " + iniFile.getProperty("CSInstallAppName"),
				"Password Request", 1);

		if (pw == null)
			return;

		if(site==null)
		  site = JOptionPane.showInputDialog(this,
				"Input your custom site name here.\n"+
				"Leave blank if you plan to add a custom site later.",
				"Your Custom Site", 1);

		if(site!=null && site.length()>0)
		 	JOptionPane.showMessageDialog(null, "The site "+site
				+" will be configured in the framework.\n"
				+"However you still need to create this site in Sites,\n" 
				+"then generate the code for the site in AgileSites,"
				+"\nas described in the tutorial, section New Site.", 
				"IMPORTANT", JOptionPane.INFORMATION_MESSAGE);

		String url = iniFile.getProperty("CSConnectString");
		if(url.endsWith("/")) url = url.substring(0, url.length()-1);

		try {

			FileReader fr = new FileReader("build.sbt.dist");
			FileWriter fw = new FileWriter("build.sbt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("wcsUrl ")) {
					fw.write("wcsUrl in ThisBuild := "
							+ q(url));
				} else if (line.startsWith("wcsUser ")) {
					fw.write("wcsUser in ThisBuild := "
							+ q(iniFile.getProperty("CSInstallAppName")));
				} else if (line.startsWith("wcsPassword ")) {
					fw.write("wcsPassword in ThisBuild := " + q(pw));
				} else if (line.startsWith("wcsHome ")) {
					fw.write("wcsHome in ThisBuild := "
							+ q(iniFile.getProperty("CSFTAppServerRoot")));
				} else if (line.startsWith("wcsShared ")) {
					fw.write("wcsShared in ThisBuild := "
							+ q(iniFile.getProperty("CSInstallSharedDirectory")));
				} else if (line.startsWith("wcsWebapp ")) {
					fw.write("wcsWebapp in ThisBuild := " + q(webApp));
				} else if (line.startsWith("wcsCsdtJar ")) {
					fw.write("wcsCsdtJar in ThisBuild := " + q(jarFile));
				} else if (line.startsWith("wcsVersion ")) {
					fw.write("wcsVersion in ThisBuild := " + q(version));
				} else if (line.startsWith("wcsSites ") && site !=null) {
					fw.write("wcsSites in ThisBuild := \""+site+",Demo\"\n");
				} else
					fw.write(line + "\n");
				line = br.readLine();
			}

			br.close();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
	 		JOptionPane.showMessageDialog(this, e.getMessage(),
					"CANNOT CONFIGURE", JOptionPane.ERROR_MESSAGE);
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		final String arg = args.length<1 ? null : args[0]; 

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(arg==null)
				  new Configurator().setVisible(true);
				else 
				  new Configurator(arg).configure();
			}
		});
	}

}

/**
 * A 1.4 file that provides utility methods for
 * creating form- or grid-style layouts with SpringLayout.
 * These utilities are used by several programs, such as
 * SpringBox and SpringCompactGrid.
 */
class SpringUtilities {
    /**
     * A debugging utility that prints to stdout the component's
     * minimum, preferred, and maximum sizes.
     */
    public static void printSizes(Component c) {
        System.out.println("minimumSize = " + c.getMinimumSize());
        System.out.println("preferredSize = " + c.getPreferredSize());
        System.out.println("maximumSize = " + c.getMaximumSize());
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code>
     * components of <code>parent</code> in
     * a grid. Each component is as big as the maximum
     * preferred width and height of the components.
     * The parent is made just big enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeGrid(Container parent,
                                int rows, int cols,
                                int initialX, int initialY,
                                int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeGrid must use SpringLayout.");
            return;
        }

        Spring xPadSpring = Spring.constant(xPad);
        Spring yPadSpring = Spring.constant(yPad);
        Spring initialXSpring = Spring.constant(initialX);
        Spring initialYSpring = Spring.constant(initialY);
        int max = rows * cols;

        //Calculate Springs that are the max of the width/height so that all
        //cells have the same size.
        Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0)).
                                    getWidth();
        Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0)).
                                    getHeight();
        for (int i = 1; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                            parent.getComponent(i));

            maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
            maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
        }

        //Apply the new width/height Spring. This forces all the
        //components to have the same size.
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                            parent.getComponent(i));

            cons.setWidth(maxWidthSpring);
            cons.setHeight(maxHeightSpring);
        }

        //Then adjust the x/y constraints of all the cells so that they
        //are aligned in a grid.
        SpringLayout.Constraints lastCons = null;
        SpringLayout.Constraints lastRowCons = null;
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                                 parent.getComponent(i));
            if (i % cols == 0) { //start of new row
                lastRowCons = lastCons;
                cons.setX(initialXSpring);
            } else { //x position depends on previous component
                cons.setX(Spring.sum(lastCons.getConstraint(SpringLayout.EAST),
                                     xPadSpring));
            }

            if (i / cols == 0) { //first row
                cons.setY(initialYSpring);
            } else { //y position depends on previous row
                cons.setY(Spring.sum(lastRowCons.getConstraint(SpringLayout.SOUTH),
                                     yPadSpring));
            }
            lastCons = cons;
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH,
                            Spring.sum(
                                Spring.constant(yPad),
                                lastCons.getConstraint(SpringLayout.SOUTH)));
        pCons.setConstraint(SpringLayout.EAST,
                            Spring.sum(
                                Spring.constant(xPad),
                                lastCons.getConstraint(SpringLayout.EAST)));
    }

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
                                                int row, int col,
                                                Container parent,
                                                int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code>
     * components of <code>parent</code> in
     * a grid. Each component in a column is as wide as the maximum
     * preferred width of the components in that column;
     * height is similarly determined for each row.
     * The parent is made just big enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                                   getConstraintsForCell(r, c, parent, cols).
                                       getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                                    getConstraintsForCell(r, c, parent, cols).
                                        getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }
}
