package site.yolowa.dto;

// sns ���� ������ ó���ϱ� ���� dto ��ü
public class Sns {
	private String blog;
	private String twitter;
	private String facebook;
	private String instagram;
	
	public String getBlog() {
		return blog;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getInstagram() {
		return instagram;
	}
	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	@Override
	public String toString() {
		return "Sns [blog=" + blog + ", twitter=" + twitter + ", facebook=" + facebook + ", instagram=" + instagram
				+ "]";
	}
}
