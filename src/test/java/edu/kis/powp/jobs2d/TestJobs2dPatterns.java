package edu.kis.powp.jobs2d;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.kis.legacy.drawer.panel.DefaultDrawerFrame;
import edu.kis.legacy.drawer.panel.DrawPanelController;
import edu.kis.legacy.drawer.shape.line.DottedLine;
import edu.kis.legacy.drawer.shape.line.SpecialLine;
import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.command.*;
import edu.kis.powp.jobs2d.drivers.DriverManager;
import edu.kis.powp.jobs2d.drivers.adapter.AbstractDriverAdapter;
import edu.kis.powp.jobs2d.drivers.adapter.DrawerToJobs2dAdapter;
import edu.kis.powp.jobs2d.drivers.adapter.LineDrawerAdapter;
import edu.kis.powp.jobs2d.events.SelectChangeVisibleOptionListener;
import edu.kis.powp.jobs2d.events.SelectTestFigureOptionListener;
import edu.kis.powp.jobs2d.events.SelectTestFigureOptionListener2;
import edu.kis.powp.jobs2d.events.SelectTestFigureOptionListener3;
import edu.kis.powp.jobs2d.features.DrawerFeature;
import edu.kis.powp.jobs2d.features.DriverFeature;
import edu.kis.powp.jobs2d.shapes.lines.UserLine;

public class TestJobs2dPatterns {
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Setup test concerning preset figures in context.
	 * 
	 * @param application Application context.
	 */
	private static void setupPresetTests(Application application) {
		SelectTestFigureOptionListener selectTestFigureOptionListener = new SelectTestFigureOptionListener(
				DriverFeature.getDriverManager());

		application.addTest("Figure Joe 1", selectTestFigureOptionListener);

		SelectTestFigureOptionListener2 selectTestFigureOptionListener2 = new SelectTestFigureOptionListener2(
				DriverFeature.getDriverManager());

		application.addTest("Figure Joe 2", selectTestFigureOptionListener2);

		SelectTestFigureOptionListener3 selectTestFigureOptionListener3 = new SelectTestFigureOptionListener3(
				DriverFeature.getDriverManager());

		application.addTest("Figure Jane", selectTestFigureOptionListener3);

		application.addTest("Test Command", e->{
			DriverCommand otc = new OperateToCommand(100, 100);
			DriverCommand spc = new SetPositionCommand(150, 150);

			otc.execute(DriverFeature.getDriverManager().getCurrentDriver());
			spc.execute(DriverFeature.getDriverManager().getCurrentDriver());
			otc.execute(DriverFeature.getDriverManager().getCurrentDriver());
		});

		application.addTest("Test RECT", e->{
			ComplexCommand complexCommand = CommandFactory.getRectangle();
			complexCommand.execute(DriverFeature.getDriverManager().getCurrentDriver());
		});

		application.addTest("Test TRIANGLE", e->{
			ComplexCommand complexCommand = CommandFactory.getTriangle();
			complexCommand.execute(DriverFeature.getDriverManager().getCurrentDriver());
		});
	}

	/**
	 * Setup driver manager, and set default driver for application.
	 * 
	 * @param application Application context.
	 */
	private static void setupDrivers(Application application) {
		Job2dDriver loggerDriver = new LoggerDriver();
		DriverFeature.addDriver("Logger Driver", loggerDriver);
		DriverFeature.getDriverManager().setCurrentDriver(loggerDriver);

		Job2dDriver testDriver = new DrawerToJobs2dAdapter();
		DriverFeature.addDriver("Graphics Driver", testDriver);

		Job2dDriver dottedLineDriver = new LineDrawerAdapter(new DottedLine());
		DriverFeature.addDriver("Dotted Line Driver", dottedLineDriver);

		Job2dDriver specialLineDriver = new LineDrawerAdapter(new SpecialLine());
		DriverFeature.addDriver("Special Line Driver", specialLineDriver);

		Job2dDriver specifiedLineDriver = new LineDrawerAdapter(new UserLine(Color.MAGENTA, 2, true));
		DriverFeature.addDriver("Specified Line Driver", specifiedLineDriver);

		Job2dDriver janeDriver = new AbstractDriverAdapter();
		DriverFeature.addDriver("Jane Driver", janeDriver);

		DriverFeature.updateDriverInfo();
	}

	/**
	 * Auxiliary routines to enable using Buggy Simulator.
	 * 
	 * @param application Application context.
	 */
	private static void setupDefaultDrawerVisibilityManagement(Application application) {
		DefaultDrawerFrame defaultDrawerWindow = DefaultDrawerFrame.getDefaultDrawerFrame();

		application.addComponentMenuElementWithCheckBox(DrawPanelController.class, "Default Drawer Visibility",
				new SelectChangeVisibleOptionListener(defaultDrawerWindow), true);

		defaultDrawerWindow.setVisible(true);
	}

	/**
	 * Setup menu for adjusting logging settings.
	 * 
	 * @param application Application context.
	 */
	private static void setupLogger(Application application) {
		application.addComponentMenu(Logger.class, "Logger", 0);
		application.addComponentMenuElement(Logger.class, "Clear log",
				(ActionEvent e) -> application.flushLoggerOutput());
		application.addComponentMenuElement(Logger.class, "Fine level", (ActionEvent e) -> logger.setLevel(Level.FINE));
		application.addComponentMenuElement(Logger.class, "Info level", (ActionEvent e) -> logger.setLevel(Level.INFO));
		application.addComponentMenuElement(Logger.class, "Warning level",
				(ActionEvent e) -> logger.setLevel(Level.WARNING));
		application.addComponentMenuElement(Logger.class, "Severe level",
				(ActionEvent e) -> logger.setLevel(Level.SEVERE));
		application.addComponentMenuElement(Logger.class, "OFF logging", (ActionEvent e) -> logger.setLevel(Level.OFF));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Application app = new Application("2d jobs Visio");
				DrawerFeature.setupDrawerPlugin(app);
//				setupDefaultDrawerVisibilityManagement(app);

				DriverFeature.setupDriverPlugin(app);
				setupDrivers(app);
				setupPresetTests(app);
				setupLogger(app);

				app.setVisibility(true);
			}
		});
	}

}
