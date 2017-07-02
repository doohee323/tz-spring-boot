package com.tz.extend.test;

/**
 * <pre>
 * </pre>
 */
public interface Test {

	/**
	 * <pre>
	 * </pre>
	 */
	void scenario(String message, Object... args);

	/**
	 * <pre>
	 * </pre>
	 */
	void given(String message, Object... args);

	/**
	 * <pre>
	 * </pre>
	 */
	void when(String message, Object... args);

	/**
	 * <pre>
	 * </pre>
	 */
	void then(String message, Object... args);

	/**
	 * <pre>
	 * </pre>
	 */
	void and(String message, Object... args);
}
