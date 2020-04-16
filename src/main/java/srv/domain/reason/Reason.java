package srv.domain.reason;

import java.io.Serializable;

/**
 * An instance of this class encapsulates a reason (justification) for access.
 * Some users are required to to provide. We store a list of quick reasons from
 * which the user can pick. Or the user can provide their own via the "Other"
 * reason.
 * 
 * @author mahiggs
 *
 */
public class Reason implements Serializable {

	private Integer rid;
	private String reason;

	public Reason(Integer rid, String reason) {
		super();
		this.rid = rid;
		this.reason = reason;
	}

	public Reason() {
		super();
	}

	public Integer getRid() {
		return rid;
	}

	public Reason setRid(Integer rid) {
		this.rid = rid;
		return this;
	}

	public String getReason() {
		return reason;
	}

	public Reason setReason(String reason) {
		this.reason = reason;
		return this;
	}

}
