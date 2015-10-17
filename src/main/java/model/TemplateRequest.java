package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "username", "repository" })
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplateRequest {

	@XmlElement(name = "repository")
	String repository;

	@XmlElement(name = "username")
	String username;

	public TemplateRequest() {

	}

	public TemplateRequest(String repository, String username) {
		this();
		this.repository = repository;
		this.username = username;
	}

	public String getRepository() {
		return repository;
	}

	public String getUsername() {
		return username;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "TemplateRequest [repository=" + repository + ", username=" + username + "]";
	}

}
