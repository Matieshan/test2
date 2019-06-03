package cn.tedu.store.service.exception;

/**
 * 违反了Unique约束的异常 
 */
public class DuplicateKeyException 
	extends ServiceException {

	private static final long serialVersionUID = 1575222253909645563L;

	public DuplicateKeyException() {
		super();
	}

	public DuplicateKeyException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public DuplicateKeyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DuplicateKeyException(String arg0) {
		super(arg0);
	}

	public DuplicateKeyException(Throwable arg0) {
		super(arg0);
	}

}
