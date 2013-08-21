package com.wordpress.capehart.kindlecollectionmanager;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;

public class Logger {
	private Path logfilePath;
	private static Logger logger = new Logger();
	
	public static Logger getInstance() {
		return logger;
	}
	
	private Logger() {
		this(Paths.get("logs"));
	}
	
	private Logger(Path logDirectory) {
		StringBuilder logfileName = new StringBuilder("log");
		Calendar today = Calendar.getInstance();
		logfileName.append(today.get(Calendar.MONTH));
		logfileName.append(today.get(Calendar.DAY_OF_MONTH));
		logfileName.append(today.get(Calendar.HOUR_OF_DAY));
		logfileName.append(today.get(Calendar.MINUTE));
		logfileName.append(today.get(Calendar.SECOND));
		logfileName.append(".log");
		
		this.logfilePath = logDirectory.resolve(Paths.get(logfileName.toString()));
	}
	
	public void log(LogLevel level, String... messages) {
		try(BufferedWriter logfileWriter = Files.newBufferedWriter(logfilePath, 
							Charset.forName("UTF-8"),
							StandardOpenOption.CREATE,
							StandardOpenOption.APPEND)) {
			logfileWriter.write(level + ":\n");
			for(String message : messages) {
				logfileWriter.write("\t" + message + "\n");
			}
			logfileWriter.write("\n");
		} catch (IOException e) {
			System.out.println("Could not write to logfile:");
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
