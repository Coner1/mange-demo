package uap.web.log;

import uap.iweb.log.ILogger;

public class Slf4jLogger implements  ILogger{

	@Override
	public void info(String msg) {
	}

	@Override
	public void console(String msg) {
	}

	@Override
	public void debug(String msg) {
	}

	@Override
	public void error(String msg, Throwable t) {
	}

	@Override
	public void error(String msg) {
	}

	@Override
	public void error(Throwable e) {
	}

	@Override
	public void warn(String msg) {
	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	@Override
	public boolean isInfoEnabled() {
		return false;
	}

	@Override
	public boolean isWarnEnabled() {
		return false;
	}

	@Override
	public boolean isErrorEnabled() {
		return false;
	}

}
