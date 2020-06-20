package srv.domain.spotlight;

public class SpotLight {
	
	private int sid;
	private String imgType;
	private Integer imgSize;
	private byte[] img;
	private String spotText;
	
	
	public SpotLight() {
		super();
	}
	
	public SpotLight(int sid, String imgType, Integer imgSize, byte[] img, String spotText) {
		super();
		this.sid = sid;
		this.imgType = imgType;
		this.imgSize = imgSize;
		this.img = img;
		this.spotText = spotText;
	}
	
	public int getSid() {
		return sid;
	}
	public SpotLight setSid(int sid) {
		this.sid = sid;
		return this;
	}
	public String getImgType() {
		return imgType;
	}
	public SpotLight  setImgType(String imgType) {
		this.imgType = imgType;
		return this;
	}
	public Integer getImgSize() {
		return imgSize;
	}
	public SpotLight  setImgSize(Integer imgSize) {
		this.imgSize = imgSize;
		return this;
	}
	public byte[] getImg() {
		return img;
	}
	public SpotLight  setImg(byte[] img) {
		this.img = img;
		return this;
	}
	public String getSpotText() {
		return spotText;
	}
	public SpotLight  setSpotText(String spotText) {
		this.spotText = spotText;
		return this;
	}

}
