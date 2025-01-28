package com.capgemini.mrchecker.jemmy;

import com.capgemini.mrchecker.jemmy.base.runtime.ScreenshotsConfig;
import com.capgemini.mrchecker.jemmy.logging.JemmyLogConfig;
import com.capgemini.mrchecker.jemmy.utils.ObjectPool;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public abstract class AppBaseOperator extends Page {

	static {
		JemmyLogConfig.configure();
	}

	private static final Object screenshotLock = new Object();
	private static IEnvironmentService environmentService;

	private AppBaseOperator parent;

	private JFrameOperator frame;

	private JInternalFrameOperator internalFrame;

	static {
		// Read Environment variables either from environments.csv or any other input data.
		setEnvironmentInstance();
	}

	protected AppBaseOperator(String classNameToStart, String appTitle) {
		this(classNameToStart, appTitle, new String[0]);
	}

	protected AppBaseOperator(String classNameToStart, String appTitle, String[] args) {
		this(classNameToStart, new FrameOperator.FrameByTitleFinder(appTitle), args);
	}

	protected AppBaseOperator(String classNameToStart, ComponentChooser componentChooser, String[] args) {
		try {
			int threadCount = Integer.parseInt(System.getProperty("thread.count", "1"));
			frame = ObjectPool.getInstance(threadCount, new JFrameOperatorFactory(classNameToStart, args, componentChooser)).borrowObject();
		} catch (InterruptedException e) {
			throw new JemmyException("Could not start app: " + e.getClass().getSimpleName() + "\\" + e.getMessage());
		}
	}

	protected AppBaseOperator(AppBaseOperator parent, String title) {
		this.parent = parent;
		internalFrame = new JInternalFrameOperator(parent.getTopFrame(), title);
	}

	protected AppBaseOperator(String classNameToStart, ComponentChooser componentChooser) {
		this(classNameToStart, componentChooser, new String[0]);
	}

	public String getTitle() {
		return internalFrame != null ? internalFrame.getTitle() : getTopFrame().getTitle();
	}

	public JFrameOperator getTopFrame() {
		return frame != null ? frame : getParent().getTopFrame();
	}

	protected JInternalFrameOperator getFrame() {
		return internalFrame;
	}

	protected AppBaseOperator getParent() {
		return parent;
	}

	@Override
	public void onTestExecutionException() {
		super.onTestExecutionException();
		makeScreenshot();
	}

	@Override
	public void onTestClassFinish() {
		super.onTestClassFinish();
		ObjectPool.getInstance().returnObject(frame);
	}

	public void screenshot() {
		if (ScreenshotsConfig.ALWAYS == ScreenshotsConfig.getConfig()) {
			makeScreenshot();
		}
	}

	@Attachment(type = "image/png")
	private byte[] makeScreenshot() {
		String fileName = "screenshot_" + UUID.randomUUID() + ".png";
		Path filePath = Paths.get(fileName);
		byte[] reuslt = new byte[0];
		try {
			Files.createFile(filePath);
			synchronized (screenshotLock) {
				frame.toFront();
				PNGEncoder.captureScreen(frame.getContentPane(), fileName);
			}
			try (FileInputStream fis = new FileInputStream(fileName)) {
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

	/**
	 * Environment variables either from environments.csv or any other input data. For now there is no properties
	 * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
	 * <code>environmentInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);<code/>
	 */
	private static void setEnvironmentInstance() {

	}

	private static class JFrameOperatorFactory implements ObjectPool.ObjectFactory<JFrameOperator> {
		public final String classNameToStart;
		public final String[] args;
		public final ComponentChooser componentChooser;

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