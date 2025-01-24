package com.capgemini.mrchecker.jemmy;

import com.capgemini.mrchecker.jemmy.utils.ObjectPool;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.test.core.logger.BFLoggerInstance;
import io.qameta.allure.Attachment;
import org.apache.commons.io.IOUtils;
import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.JemmyException;
import org.netbeans.jemmy.operators.FrameOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JInternalFrameOperator;
import org.netbeans.jemmy.util.PNGEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public abstract class AppBaseOperator extends Page {

	private static ObjectPool<JFrameOperator> framePool;

	private static final Object screenshotLock = new Object();
	private static IEnvironmentService environmentService;

	private final BFLoggerInstance bfLogger = BFLogger.getLog();

	protected AppBaseOperator parent;

	protected JFrameOperator frame;

	protected JInternalFrameOperator internalFrame;

	static {
		// Read Environment variables either from environments.csv or any other input data.
		setEnvironmentInstance();
	}

	public AppBaseOperator(String classNameToStart, String appTitle) {
		this(classNameToStart, appTitle, new String[0]);
	}

	public AppBaseOperator(String classNameToStart, String appTitle, String[] args) {
		this(classNameToStart, new FrameOperator.FrameByTitleFinder(appTitle), args);
	}

	public AppBaseOperator(String classNameToStart, ComponentChooser componentChooser, String[] args) {
		try {
			var threadCount = Integer.parseInt(System.getProperty("thread.count", "1"));
			framePool = ObjectPool.getInstance(threadCount, new JFrameOperatorFactory(classNameToStart, args, componentChooser));
			frame = framePool.borrowObject();
		} catch (InterruptedException e) {
			throw new JemmyException("Could not start app: " + e.getClass().getSimpleName() + "\\" + e.getMessage());
		}
	}

	public AppBaseOperator(AppBaseOperator parent, String title) {
		this.parent = parent;
		internalFrame = new JInternalFrameOperator(parent.getFrame(), title);
	}

	public AppBaseOperator(String classNameToStart, ComponentChooser componentChooser) {
		this(classNameToStart, componentChooser, new String[0]);
	}

	public String getTitle() {
		return internalFrame != null ? internalFrame.getTitle() : frame.getTitle();
	}

	public JFrameOperator getFrame() {
		return frame != null ? frame : parent.getFrame();
	}

	public JInternalFrameOperator getInternalFrame() {
		return getInternalFrame();
	}

	@Override
	public void onTestExecutionException() {
		super.onTestExecutionException();
		screenshot();
	}

	@Override
	public void onTestClassFinish() {
		super.onTestClassFinish();
		framePool.returnObject(frame);
	}

	@Attachment(type = "image/png")
	public byte[] screenshot() {
		var fileName = "screenshot_" + UUID.randomUUID() + ".png";
		var filePath = Paths.get(fileName);
		var reuslt = new byte[0];
		try {
			Files.createFile(filePath);
			synchronized (screenshotLock) {
				frame.toFront();
				PNGEncoder.captureScreen(frame.getContentPane(), fileName);
			}
			try (var fis = new FileInputStream(fileName)) {
				reuslt = IOUtils.toByteArray(fis);
			}
		} catch (IOException e) {
			BFLogger.logError("Could not create a screenshot: " + e.getMessage());
		} finally {
			try {
				Files.deleteIfExists(filePath);
			} catch (IOException e) {
				BFLogger.logError("Could not delete temp screenshot file");
			}
		}

		return reuslt;
	}

	@Override
	public ModuleType getModuleType() {
		return ModuleType.JEMMY;
	}

	private static void setEnvironmentInstance() {
		/*
		 * Environment variables either from environments.csv or any other input data. For now there is no properties
		 * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
		 * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
		 */
	}

	private static class JFrameOperatorFactory implements ObjectPool.ObjectFactory<JFrameOperator> {
		public String classNameToStart;
		public String[] args;
		public ComponentChooser componentChooser;

		public JFrameOperatorFactory(String classNameToStart, String[] args, ComponentChooser componentChooser) {
			this.classNameToStart = classNameToStart;
			this.args = args;
			this.componentChooser = componentChooser;
		}

		@Override
		public JFrameOperator createObject(int index) {
			try {
				ClassReference cr = new ClassReference(classNameToStart);
				cr.startApplication(args);
				return new JFrameOperator(componentChooser, index);
			} catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException e) {
				throw new JemmyException("Could not start app: " + e.getClass().getSimpleName() + "\\" + e.getMessage());
			}
		}
	}
}