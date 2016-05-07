package org.chudian.mj.common;

/**
 * @author Rick E-mail: maodai1990@163.com
 * @version 创建时间：2014-5-16 上午10:43:46 类说明
 */

public class Response {
	protected int status;
	protected Object body;
	protected String message;
	protected long currentPage;
	protected long totalPage;
	protected long totalRow;

	public Response() {
	}

	public Response(int status) {
		this.status = status;
	}

	public Response(int status, String message) {
		this(status);
		this.message = message;
	}

	public Response(int status, Object body) {
		this(status);
		this.body = body;
	}

	public Response(int status, String message, Object body) {
		this(status, message);
		this.body = body;
	}
	public Response(int status,  long totalRow, Object body) {
		this.status = status;
		this.totalRow = totalRow;
		this.body = body;
	}


	public Response(int status, long totalRow, long totalPage, Object body) {
		this.status = status;
		this.totalRow = totalRow;
		this.totalPage = totalPage;
		this.body = body;
	}

	public long getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(long totalRow) {
		this.totalRow = totalRow;
	}

	public long getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
