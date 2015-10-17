package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplatePutResponse {

	@XmlElement(name = "uuid")
	private String uuid;
	
	@XmlElement(name = "url")
	private String url;
	
	public TemplatePutResponse() {
		
	}
	
	public TemplatePutResponse(String uuid, String url) {
		this.uuid = uuid;
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
