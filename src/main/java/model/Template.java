package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "username", "repository", "ref", "files", "variables", "encoding" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Template {

	@XmlElement(name = "ref")
	private String ref;

	@XmlElement(name = "encoding", defaultValue = "UTF-8")
	private String encoding;
	
	@XmlElementWrapper(name = "files")
	@XmlElement(name = "file")
	private List<String> files;
	
	@XmlElement(name = "repository", required = true)
	private String repository;
	
	@XmlElement(name = "rootPath")
	private String rootPath;

	@XmlElement(name = "username", required = true)
	private String username;
	
	@XmlElementWrapper(name = "variables")
	@XmlElement(name = "variable")
	private List<TemplateVariable> variables;

	public Template() {

	}

	public Template(String username, String repository, String ref, String rootPath, String encoding, String[] files, TemplateVariable... variables) {
		this.username = username;
		this.encoding = encoding;
		this.repository = repository;
		this.rootPath = rootPath;
		this.ref = ref;
		this.files = Arrays.asList(files);
		this.variables = Arrays.asList(variables);
	}
	
	public String getCommit() {
		return ref;
	}

	public String getEncoding() {
		if (encoding == null) {
			encoding = "UTF-8";
		}
		return encoding;
	}
	
	public List<String> getFiles() {
		if (files == null) {
			files = new ArrayList<>();
		}
		return files;
	}
	
	public String getRepository() {
		return repository;
	}
	
	public String getRootPath() {
		return rootPath;
	}

	public String getUsername() {
		return username;
	}
	
	public List<TemplateVariable> getVariables() {
		if (variables == null) {
			variables = new ArrayList<>();
		}
		return variables;
	}
	
	public void setCommit(String commit) {
		this.ref = commit;
	}
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}
	
	public void setRepository(String repository) {
		this.repository = repository;
	}
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setVariables(List<TemplateVariable> variables) {
		this.variables = variables;
	}

	@Override
	public String toString() {
		return "Template [username=" + username + ", repository=" + repository + ", variables=" + variables + "]";
	}

}
