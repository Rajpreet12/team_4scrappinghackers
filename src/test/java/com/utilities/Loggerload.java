package com.utilities;
import org.apache.logging.log4j.LogManager;

public class Loggerload {

		private static org.apache.logging.log4j.Logger logger= LogManager.getLogger();
		public static void info(String message) {
			logger.info(message);
		}
		public static void warn(String message) {
			logger.warn(message);
		}

		public static void error(String message) {
			logger.error(message);
		}

		public static void fatal(String message) {
			logger.fatal(message);
		}

		public static void debug(String message) {
			logger.debug(message);
		}
	}

