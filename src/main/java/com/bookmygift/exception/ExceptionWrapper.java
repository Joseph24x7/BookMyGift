package com.bookmygift.exception;

public interface ExceptionWrapper {

	Object run() throws Exception;

	public static Object wrapException(ExceptionWrapper wrapper) {
		try {
			return wrapper.run();
		} catch (Exception e) {
			throw new ServiceException(ErrorEnums.GENERAL_EXCEPTION, e.getMessage());
		}

	}

}
