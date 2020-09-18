package suncrafterina.service.dto;

public class ResponseStatusDTO {

	private String message;
	private int status;
	private Long timeStamp;

	public ResponseStatusDTO() {}

	public ResponseStatusDTO(String message, int status, Long timeStamp) {
		super();
		this.message = message;
		this.status = status;
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
}
